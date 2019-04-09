package android.support.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.VectorDrawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.content.res.ComplexColorCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.support.v4.graphics.PathParser.PathDataNode;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VectorDrawableCompat extends VectorDrawableCommon {
    private static final boolean DBG_VECTOR_DRAWABLE = false;
    static final Mode DEFAULT_TINT_MODE = Mode.SRC_IN;
    private static final int LINECAP_BUTT = 0;
    private static final int LINECAP_ROUND = 1;
    private static final int LINECAP_SQUARE = 2;
    private static final int LINEJOIN_BEVEL = 2;
    private static final int LINEJOIN_MITER = 0;
    private static final int LINEJOIN_ROUND = 1;
    static final String LOGTAG = "VectorDrawableCompat";
    private static final int MAX_CACHED_BITMAP_SIZE = 2048;
    private static final String SHAPE_CLIP_PATH = "clip-path";
    private static final String SHAPE_GROUP = "group";
    private static final String SHAPE_PATH = "path";
    private static final String SHAPE_VECTOR = "vector";
    private boolean mAllowCaching;
    private ConstantState mCachedConstantStateDelegate;
    private ColorFilter mColorFilter;
    private boolean mMutated;
    private PorterDuffColorFilter mTintFilter;
    private final Rect mTmpBounds;
    private final float[] mTmpFloats;
    private final Matrix mTmpMatrix;
    private VectorDrawableCompatState mVectorState;

    private static abstract class VObject {
        private VObject() {
        }

        public boolean isStateful() {
            return false;
        }

        public boolean onStateChanged(int[] stateSet) {
            return false;
        }
    }

    private static class VPathRenderer {
        private static final Matrix IDENTITY_MATRIX = new Matrix();
        float mBaseHeight;
        float mBaseWidth;
        private int mChangingConfigurations;
        Paint mFillPaint;
        private final Matrix mFinalPathMatrix;
        Boolean mIsStateful;
        private final Path mPath;
        private PathMeasure mPathMeasure;
        private final Path mRenderPath;
        int mRootAlpha;
        final VGroup mRootGroup;
        String mRootName;
        Paint mStrokePaint;
        final ArrayMap<String, Object> mVGTargetsMap;
        float mViewportHeight;
        float mViewportWidth;

        public VPathRenderer() {
            this.mFinalPathMatrix = new Matrix();
            this.mBaseWidth = 0.0f;
            this.mBaseHeight = 0.0f;
            this.mViewportWidth = 0.0f;
            this.mViewportHeight = 0.0f;
            this.mRootAlpha = 255;
            this.mRootName = null;
            this.mIsStateful = null;
            this.mVGTargetsMap = new ArrayMap();
            this.mRootGroup = new VGroup();
            this.mPath = new Path();
            this.mRenderPath = new Path();
        }

        public void setRootAlpha(int alpha) {
            this.mRootAlpha = alpha;
        }

        public int getRootAlpha() {
            return this.mRootAlpha;
        }

        public void setAlpha(float alpha) {
            setRootAlpha((int) (255.0f * alpha));
        }

        public float getAlpha() {
            return ((float) getRootAlpha()) / 255.0f;
        }

        public VPathRenderer(VPathRenderer copy) {
            this.mFinalPathMatrix = new Matrix();
            this.mBaseWidth = 0.0f;
            this.mBaseHeight = 0.0f;
            this.mViewportWidth = 0.0f;
            this.mViewportHeight = 0.0f;
            this.mRootAlpha = 255;
            this.mRootName = null;
            this.mIsStateful = null;
            this.mVGTargetsMap = new ArrayMap();
            this.mRootGroup = new VGroup(copy.mRootGroup, this.mVGTargetsMap);
            this.mPath = new Path(copy.mPath);
            this.mRenderPath = new Path(copy.mRenderPath);
            this.mBaseWidth = copy.mBaseWidth;
            this.mBaseHeight = copy.mBaseHeight;
            this.mViewportWidth = copy.mViewportWidth;
            this.mViewportHeight = copy.mViewportHeight;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            this.mRootAlpha = copy.mRootAlpha;
            this.mRootName = copy.mRootName;
            String str = copy.mRootName;
            if (str != null) {
                this.mVGTargetsMap.put(str, this);
            }
            this.mIsStateful = copy.mIsStateful;
        }

        private void drawGroupTree(VGroup currentGroup, Matrix currentMatrix, Canvas canvas, int w, int h, ColorFilter filter) {
            VGroup vGroup = currentGroup;
            vGroup.mStackedMatrix.set(currentMatrix);
            vGroup.mStackedMatrix.preConcat(vGroup.mLocalMatrix);
            canvas.save();
            for (int i = 0; i < vGroup.mChildren.size(); i++) {
                VObject child = (VObject) vGroup.mChildren.get(i);
                if (child instanceof VGroup) {
                    drawGroupTree((VGroup) child, vGroup.mStackedMatrix, canvas, w, h, filter);
                } else if (child instanceof VPath) {
                    drawPath(currentGroup, (VPath) child, canvas, w, h, filter);
                }
            }
            canvas.restore();
        }

        public void draw(Canvas canvas, int w, int h, ColorFilter filter) {
            drawGroupTree(this.mRootGroup, IDENTITY_MATRIX, canvas, w, h, filter);
        }

        private void drawPath(VGroup vGroup, VPath vPath, Canvas canvas, int w, int h, ColorFilter filter) {
            VPath vPath2 = vPath;
            Canvas canvas2 = canvas;
            ColorFilter colorFilter = filter;
            float scaleX = ((float) w) / this.mViewportWidth;
            float scaleY = ((float) h) / this.mViewportHeight;
            float minScale = Math.min(scaleX, scaleY);
            Matrix groupStackedMatrix = vGroup.mStackedMatrix;
            this.mFinalPathMatrix.set(groupStackedMatrix);
            this.mFinalPathMatrix.postScale(scaleX, scaleY);
            float matrixScale = getMatrixScale(groupStackedMatrix);
            if (matrixScale != 0.0f) {
                vPath2.toPath(r0.mPath);
                Path path = r0.mPath;
                r0.mRenderPath.reset();
                float f;
                if (vPath.isClipPath()) {
                    r0.mRenderPath.addPath(path, r0.mFinalPathMatrix);
                    canvas2.clipPath(r0.mRenderPath);
                    f = scaleX;
                } else {
                    ComplexColorCompat fill;
                    Paint fillPaint;
                    Shader shader;
                    VFullPath fullPath = (VFullPath) vPath2;
                    if (fullPath.mTrimPathStart == 0.0f) {
                        if (fullPath.mTrimPathEnd == 1.0f) {
                            f = scaleX;
                            r0.mRenderPath.addPath(path, r0.mFinalPathMatrix);
                            if (fullPath.mFillColor.willDraw()) {
                                fill = fullPath.mFillColor;
                                if (r0.mFillPaint == null) {
                                    r0.mFillPaint = new Paint(1);
                                    r0.mFillPaint.setStyle(Style.FILL);
                                }
                                fillPaint = r0.mFillPaint;
                                if (fill.isGradient()) {
                                    fillPaint.setColor(VectorDrawableCompat.applyAlpha(fill.getColor(), fullPath.mFillAlpha));
                                } else {
                                    shader = fill.getShader();
                                    shader.setLocalMatrix(r0.mFinalPathMatrix);
                                    fillPaint.setShader(shader);
                                    fillPaint.setAlpha(Math.round(fullPath.mFillAlpha * 255.0f));
                                }
                                fillPaint.setColorFilter(colorFilter);
                                r0.mRenderPath.setFillType(fullPath.mFillRule != 0 ? FillType.WINDING : FillType.EVEN_ODD);
                                canvas2.drawPath(r0.mRenderPath, fillPaint);
                            }
                            if (fullPath.mStrokeColor.willDraw()) {
                                fill = fullPath.mStrokeColor;
                                if (r0.mStrokePaint == null) {
                                    r0.mStrokePaint = new Paint(1);
                                    r0.mStrokePaint.setStyle(Style.STROKE);
                                }
                                fillPaint = r0.mStrokePaint;
                                if (fullPath.mStrokeLineJoin != null) {
                                    fillPaint.setStrokeJoin(fullPath.mStrokeLineJoin);
                                }
                                if (fullPath.mStrokeLineCap != null) {
                                    fillPaint.setStrokeCap(fullPath.mStrokeLineCap);
                                }
                                fillPaint.setStrokeMiter(fullPath.mStrokeMiterlimit);
                                if (fill.isGradient()) {
                                    fillPaint.setColor(VectorDrawableCompat.applyAlpha(fill.getColor(), fullPath.mStrokeAlpha));
                                } else {
                                    shader = fill.getShader();
                                    shader.setLocalMatrix(r0.mFinalPathMatrix);
                                    fillPaint.setShader(shader);
                                    fillPaint.setAlpha(Math.round(fullPath.mStrokeAlpha * 255.0f));
                                }
                                fillPaint.setColorFilter(colorFilter);
                                fillPaint.setStrokeWidth(fullPath.mStrokeWidth * (minScale * matrixScale));
                                canvas2.drawPath(r0.mRenderPath, fillPaint);
                            }
                        }
                    }
                    float start = (fullPath.mTrimPathStart + fullPath.mTrimPathOffset) % 1.0f;
                    float end = (fullPath.mTrimPathEnd + fullPath.mTrimPathOffset) % 1.0f;
                    if (r0.mPathMeasure == null) {
                        r0.mPathMeasure = new PathMeasure();
                    }
                    r0.mPathMeasure.setPath(r0.mPath, false);
                    float len = r0.mPathMeasure.getLength();
                    start *= len;
                    end *= len;
                    path.reset();
                    if (start > end) {
                        r0.mPathMeasure.getSegment(start, len, path, true);
                        len = 0.0f;
                        r0.mPathMeasure.getSegment(0.0f, end, path, true);
                    } else {
                        len = 0.0f;
                        r0.mPathMeasure.getSegment(start, end, path, true);
                    }
                    path.rLineTo(len, len);
                    r0.mRenderPath.addPath(path, r0.mFinalPathMatrix);
                    if (fullPath.mFillColor.willDraw()) {
                        fill = fullPath.mFillColor;
                        if (r0.mFillPaint == null) {
                            r0.mFillPaint = new Paint(1);
                            r0.mFillPaint.setStyle(Style.FILL);
                        }
                        fillPaint = r0.mFillPaint;
                        if (fill.isGradient()) {
                            fillPaint.setColor(VectorDrawableCompat.applyAlpha(fill.getColor(), fullPath.mFillAlpha));
                        } else {
                            shader = fill.getShader();
                            shader.setLocalMatrix(r0.mFinalPathMatrix);
                            fillPaint.setShader(shader);
                            fillPaint.setAlpha(Math.round(fullPath.mFillAlpha * 255.0f));
                        }
                        fillPaint.setColorFilter(colorFilter);
                        if (fullPath.mFillRule != 0) {
                        }
                        r0.mRenderPath.setFillType(fullPath.mFillRule != 0 ? FillType.WINDING : FillType.EVEN_ODD);
                        canvas2.drawPath(r0.mRenderPath, fillPaint);
                    }
                    if (fullPath.mStrokeColor.willDraw()) {
                        fill = fullPath.mStrokeColor;
                        if (r0.mStrokePaint == null) {
                            r0.mStrokePaint = new Paint(1);
                            r0.mStrokePaint.setStyle(Style.STROKE);
                        }
                        fillPaint = r0.mStrokePaint;
                        if (fullPath.mStrokeLineJoin != null) {
                            fillPaint.setStrokeJoin(fullPath.mStrokeLineJoin);
                        }
                        if (fullPath.mStrokeLineCap != null) {
                            fillPaint.setStrokeCap(fullPath.mStrokeLineCap);
                        }
                        fillPaint.setStrokeMiter(fullPath.mStrokeMiterlimit);
                        if (fill.isGradient()) {
                            fillPaint.setColor(VectorDrawableCompat.applyAlpha(fill.getColor(), fullPath.mStrokeAlpha));
                        } else {
                            shader = fill.getShader();
                            shader.setLocalMatrix(r0.mFinalPathMatrix);
                            fillPaint.setShader(shader);
                            fillPaint.setAlpha(Math.round(fullPath.mStrokeAlpha * 255.0f));
                        }
                        fillPaint.setColorFilter(colorFilter);
                        fillPaint.setStrokeWidth(fullPath.mStrokeWidth * (minScale * matrixScale));
                        canvas2.drawPath(r0.mRenderPath, fillPaint);
                    }
                }
            }
        }

        private static float cross(float v1x, float v1y, float v2x, float v2y) {
            return (v1x * v2y) - (v1y * v2x);
        }

        private float getMatrixScale(Matrix groupStackedMatrix) {
            float[] unitVectors = new float[]{0.0f, 1.0f, 1.0f, 0.0f};
            groupStackedMatrix.mapVectors(unitVectors);
            float scaleX = (float) Math.hypot((double) unitVectors[0], (double) unitVectors[1]);
            float scaleY = (float) Math.hypot((double) unitVectors[2], (double) unitVectors[3]);
            float crossProduct = cross(unitVectors[0], unitVectors[1], unitVectors[2], unitVectors[3]);
            float maxScale = Math.max(scaleX, scaleY);
            if (maxScale > 0.0f) {
                return Math.abs(crossProduct) / maxScale;
            }
            return 0.0f;
        }

        public boolean isStateful() {
            if (this.mIsStateful == null) {
                this.mIsStateful = Boolean.valueOf(this.mRootGroup.isStateful());
            }
            return this.mIsStateful.booleanValue();
        }

        public boolean onStateChanged(int[] stateSet) {
            return this.mRootGroup.onStateChanged(stateSet);
        }
    }

    private static class VectorDrawableCompatState extends ConstantState {
        boolean mAutoMirrored;
        boolean mCacheDirty;
        boolean mCachedAutoMirrored;
        Bitmap mCachedBitmap;
        int mCachedRootAlpha;
        int[] mCachedThemeAttrs;
        ColorStateList mCachedTint;
        Mode mCachedTintMode;
        int mChangingConfigurations;
        Paint mTempPaint;
        ColorStateList mTint;
        Mode mTintMode;
        VPathRenderer mVPathRenderer;

        public VectorDrawableCompatState(VectorDrawableCompatState copy) {
            this.mTint = null;
            this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
            if (copy != null) {
                this.mChangingConfigurations = copy.mChangingConfigurations;
                this.mVPathRenderer = new VPathRenderer(copy.mVPathRenderer);
                if (copy.mVPathRenderer.mFillPaint != null) {
                    this.mVPathRenderer.mFillPaint = new Paint(copy.mVPathRenderer.mFillPaint);
                }
                if (copy.mVPathRenderer.mStrokePaint != null) {
                    this.mVPathRenderer.mStrokePaint = new Paint(copy.mVPathRenderer.mStrokePaint);
                }
                this.mTint = copy.mTint;
                this.mTintMode = copy.mTintMode;
                this.mAutoMirrored = copy.mAutoMirrored;
            }
        }

        public void drawCachedBitmapWithRootAlpha(Canvas canvas, ColorFilter filter, Rect originalBounds) {
            canvas.drawBitmap(this.mCachedBitmap, null, originalBounds, getPaint(filter));
        }

        public boolean hasTranslucentRoot() {
            return this.mVPathRenderer.getRootAlpha() < 255;
        }

        public Paint getPaint(ColorFilter filter) {
            if (!hasTranslucentRoot() && filter == null) {
                return null;
            }
            if (this.mTempPaint == null) {
                this.mTempPaint = new Paint();
                this.mTempPaint.setFilterBitmap(true);
            }
            this.mTempPaint.setAlpha(this.mVPathRenderer.getRootAlpha());
            this.mTempPaint.setColorFilter(filter);
            return this.mTempPaint;
        }

        public void updateCachedBitmap(int width, int height) {
            this.mCachedBitmap.eraseColor(0);
            this.mVPathRenderer.draw(new Canvas(this.mCachedBitmap), width, height, null);
        }

        public void createCachedBitmapIfNeeded(int width, int height) {
            if (this.mCachedBitmap == null || !canReuseBitmap(width, height)) {
                this.mCachedBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                this.mCacheDirty = true;
            }
        }

        public boolean canReuseBitmap(int width, int height) {
            if (width == this.mCachedBitmap.getWidth() && height == this.mCachedBitmap.getHeight()) {
                return true;
            }
            return false;
        }

        public boolean canReuseCache() {
            if (!this.mCacheDirty && this.mCachedTint == this.mTint && this.mCachedTintMode == this.mTintMode && this.mCachedAutoMirrored == this.mAutoMirrored && this.mCachedRootAlpha == this.mVPathRenderer.getRootAlpha()) {
                return true;
            }
            return false;
        }

        public void updateCacheStates() {
            this.mCachedTint = this.mTint;
            this.mCachedTintMode = this.mTintMode;
            this.mCachedRootAlpha = this.mVPathRenderer.getRootAlpha();
            this.mCachedAutoMirrored = this.mAutoMirrored;
            this.mCacheDirty = false;
        }

        public VectorDrawableCompatState() {
            this.mTint = null;
            this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
            this.mVPathRenderer = new VPathRenderer();
        }

        @NonNull
        public Drawable newDrawable() {
            return new VectorDrawableCompat(this);
        }

        @NonNull
        public Drawable newDrawable(Resources res) {
            return new VectorDrawableCompat(this);
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public boolean isStateful() {
            return this.mVPathRenderer.isStateful();
        }

        public boolean onStateChanged(int[] stateSet) {
            boolean changed = this.mVPathRenderer.onStateChanged(stateSet);
            this.mCacheDirty |= changed;
            return changed;
        }
    }

    @RequiresApi(24)
    private static class VectorDrawableDelegateState extends ConstantState {
        private final ConstantState mDelegateState;

        public VectorDrawableDelegateState(ConstantState state) {
            this.mDelegateState = state;
        }

        public Drawable newDrawable() {
            VectorDrawableCompat drawableCompat = new VectorDrawableCompat();
            drawableCompat.mDelegateDrawable = (VectorDrawable) this.mDelegateState.newDrawable();
            return drawableCompat;
        }

        public Drawable newDrawable(Resources res) {
            VectorDrawableCompat drawableCompat = new VectorDrawableCompat();
            drawableCompat.mDelegateDrawable = (VectorDrawable) this.mDelegateState.newDrawable(res);
            return drawableCompat;
        }

        public Drawable newDrawable(Resources res, Theme theme) {
            VectorDrawableCompat drawableCompat = new VectorDrawableCompat();
            drawableCompat.mDelegateDrawable = (VectorDrawable) this.mDelegateState.newDrawable(res, theme);
            return drawableCompat;
        }

        public boolean canApplyTheme() {
            return this.mDelegateState.canApplyTheme();
        }

        public int getChangingConfigurations() {
            return this.mDelegateState.getChangingConfigurations();
        }
    }

    private static class VGroup extends VObject {
        int mChangingConfigurations;
        final ArrayList<VObject> mChildren = new ArrayList();
        private String mGroupName = null;
        final Matrix mLocalMatrix = new Matrix();
        private float mPivotX = 0.0f;
        private float mPivotY = 0.0f;
        float mRotate = 0.0f;
        private float mScaleX = 1.0f;
        private float mScaleY = 1.0f;
        final Matrix mStackedMatrix = new Matrix();
        private int[] mThemeAttrs;
        private float mTranslateX = 0.0f;
        private float mTranslateY = 0.0f;

        public VGroup(VGroup copy, ArrayMap<String, Object> targetsMap) {
            super();
            this.mRotate = copy.mRotate;
            this.mPivotX = copy.mPivotX;
            this.mPivotY = copy.mPivotY;
            this.mScaleX = copy.mScaleX;
            this.mScaleY = copy.mScaleY;
            this.mTranslateX = copy.mTranslateX;
            this.mTranslateY = copy.mTranslateY;
            this.mThemeAttrs = copy.mThemeAttrs;
            this.mGroupName = copy.mGroupName;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            String str = this.mGroupName;
            if (str != null) {
                targetsMap.put(str, this);
            }
            this.mLocalMatrix.set(copy.mLocalMatrix);
            ArrayList<VObject> children = copy.mChildren;
            for (int i = 0; i < children.size(); i++) {
                VGroup copyChild = children.get(i);
                if (copyChild instanceof VGroup) {
                    this.mChildren.add(new VGroup(copyChild, targetsMap));
                } else {
                    VPath newPath;
                    if (copyChild instanceof VFullPath) {
                        newPath = new VFullPath((VFullPath) copyChild);
                    } else if (copyChild instanceof VClipPath) {
                        newPath = new VClipPath((VClipPath) copyChild);
                    } else {
                        throw new IllegalStateException("Unknown object in the tree!");
                    }
                    this.mChildren.add(newPath);
                    if (newPath.mPathName != null) {
                        targetsMap.put(newPath.mPathName, newPath);
                    }
                }
            }
        }

        public VGroup() {
            super();
        }

        public String getGroupName() {
            return this.mGroupName;
        }

        public Matrix getLocalMatrix() {
            return this.mLocalMatrix;
        }

        public void inflate(Resources res, AttributeSet attrs, Theme theme, XmlPullParser parser) {
            TypedArray a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_GROUP);
            updateStateFromTypedArray(a, parser);
            a.recycle();
        }

        private void updateStateFromTypedArray(TypedArray a, XmlPullParser parser) {
            this.mThemeAttrs = null;
            this.mRotate = TypedArrayUtils.getNamedFloat(a, parser, "rotation", 5, this.mRotate);
            this.mPivotX = a.getFloat(1, this.mPivotX);
            this.mPivotY = a.getFloat(2, this.mPivotY);
            this.mScaleX = TypedArrayUtils.getNamedFloat(a, parser, "scaleX", 3, this.mScaleX);
            this.mScaleY = TypedArrayUtils.getNamedFloat(a, parser, "scaleY", 4, this.mScaleY);
            this.mTranslateX = TypedArrayUtils.getNamedFloat(a, parser, "translateX", 6, this.mTranslateX);
            this.mTranslateY = TypedArrayUtils.getNamedFloat(a, parser, "translateY", 7, this.mTranslateY);
            String groupName = a.getString(null);
            if (groupName != null) {
                this.mGroupName = groupName;
            }
            updateLocalMatrix();
        }

        private void updateLocalMatrix() {
            this.mLocalMatrix.reset();
            this.mLocalMatrix.postTranslate(-this.mPivotX, -this.mPivotY);
            this.mLocalMatrix.postScale(this.mScaleX, this.mScaleY);
            this.mLocalMatrix.postRotate(this.mRotate, 0.0f, 0.0f);
            this.mLocalMatrix.postTranslate(this.mTranslateX + this.mPivotX, this.mTranslateY + this.mPivotY);
        }

        public float getRotation() {
            return this.mRotate;
        }

        public void setRotation(float rotation) {
            if (rotation != this.mRotate) {
                this.mRotate = rotation;
                updateLocalMatrix();
            }
        }

        public float getPivotX() {
            return this.mPivotX;
        }

        public void setPivotX(float pivotX) {
            if (pivotX != this.mPivotX) {
                this.mPivotX = pivotX;
                updateLocalMatrix();
            }
        }

        public float getPivotY() {
            return this.mPivotY;
        }

        public void setPivotY(float pivotY) {
            if (pivotY != this.mPivotY) {
                this.mPivotY = pivotY;
                updateLocalMatrix();
            }
        }

        public float getScaleX() {
            return this.mScaleX;
        }

        public void setScaleX(float scaleX) {
            if (scaleX != this.mScaleX) {
                this.mScaleX = scaleX;
                updateLocalMatrix();
            }
        }

        public float getScaleY() {
            return this.mScaleY;
        }

        public void setScaleY(float scaleY) {
            if (scaleY != this.mScaleY) {
                this.mScaleY = scaleY;
                updateLocalMatrix();
            }
        }

        public float getTranslateX() {
            return this.mTranslateX;
        }

        public void setTranslateX(float translateX) {
            if (translateX != this.mTranslateX) {
                this.mTranslateX = translateX;
                updateLocalMatrix();
            }
        }

        public float getTranslateY() {
            return this.mTranslateY;
        }

        public void setTranslateY(float translateY) {
            if (translateY != this.mTranslateY) {
                this.mTranslateY = translateY;
                updateLocalMatrix();
            }
        }

        public boolean isStateful() {
            for (int i = 0; i < this.mChildren.size(); i++) {
                if (((VObject) this.mChildren.get(i)).isStateful()) {
                    return true;
                }
            }
            return false;
        }

        public boolean onStateChanged(int[] stateSet) {
            boolean changed = false;
            for (int i = 0; i < this.mChildren.size(); i++) {
                changed |= ((VObject) this.mChildren.get(i)).onStateChanged(stateSet);
            }
            return changed;
        }
    }

    private static abstract class VPath extends VObject {
        int mChangingConfigurations;
        protected PathDataNode[] mNodes = null;
        String mPathName;

        public VPath() {
            super();
        }

        public void printVPath(int level) {
            StringBuilder stringBuilder;
            String indent = "";
            for (int i = 0; i < level; i++) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(indent);
                stringBuilder.append("    ");
                indent = stringBuilder.toString();
            }
            String str = VectorDrawableCompat.LOGTAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(indent);
            stringBuilder.append("current path is :");
            stringBuilder.append(this.mPathName);
            stringBuilder.append(" pathData is ");
            stringBuilder.append(nodesToString(this.mNodes));
            Log.v(str, stringBuilder.toString());
        }

        public String nodesToString(PathDataNode[] nodes) {
            String result = " ";
            for (int i = 0; i < nodes.length; i++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(result);
                stringBuilder.append(nodes[i].mType);
                stringBuilder.append(":");
                result = stringBuilder.toString();
                float[] params = nodes[i].mParams;
                for (float append : params) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(result);
                    stringBuilder2.append(append);
                    stringBuilder2.append(",");
                    result = stringBuilder2.toString();
                }
            }
            return result;
        }

        public VPath(VPath copy) {
            super();
            this.mPathName = copy.mPathName;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            this.mNodes = PathParser.deepCopyNodes(copy.mNodes);
        }

        public void toPath(Path path) {
            path.reset();
            PathDataNode[] pathDataNodeArr = this.mNodes;
            if (pathDataNodeArr != null) {
                PathDataNode.nodesToPath(pathDataNodeArr, path);
            }
        }

        public String getPathName() {
            return this.mPathName;
        }

        public boolean canApplyTheme() {
            return false;
        }

        public void applyTheme(Theme t) {
        }

        public boolean isClipPath() {
            return false;
        }

        public PathDataNode[] getPathData() {
            return this.mNodes;
        }

        public void setPathData(PathDataNode[] nodes) {
            if (PathParser.canMorph(this.mNodes, nodes)) {
                PathParser.updateNodes(this.mNodes, nodes);
            } else {
                this.mNodes = PathParser.deepCopyNodes(nodes);
            }
        }
    }

    private static class VClipPath extends VPath {
        public VClipPath(VClipPath copy) {
            super(copy);
        }

        public void inflate(Resources r, AttributeSet attrs, Theme theme, XmlPullParser parser) {
            if (TypedArrayUtils.hasAttribute(parser, "pathData")) {
                TypedArray a = TypedArrayUtils.obtainAttributes(r, theme, attrs, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_CLIP_PATH);
                updateStateFromTypedArray(a);
                a.recycle();
            }
        }

        private void updateStateFromTypedArray(TypedArray a) {
            String pathName = a.getString(null);
            if (pathName != null) {
                this.mPathName = pathName;
            }
            String pathData = a.getString(1);
            if (pathData != null) {
                this.mNodes = PathParser.createNodesFromPathData(pathData);
            }
        }

        public boolean isClipPath() {
            return true;
        }
    }

    private static class VFullPath extends VPath {
        private static final int FILL_TYPE_WINDING = 0;
        float mFillAlpha = 1.0f;
        ComplexColorCompat mFillColor;
        int mFillRule = 0;
        float mStrokeAlpha = 1.0f;
        ComplexColorCompat mStrokeColor;
        Cap mStrokeLineCap = Cap.BUTT;
        Join mStrokeLineJoin = Join.MITER;
        float mStrokeMiterlimit = 4.0f;
        float mStrokeWidth = 0.0f;
        private int[] mThemeAttrs;
        float mTrimPathEnd = 1.0f;
        float mTrimPathOffset = 0.0f;
        float mTrimPathStart = 0.0f;

        public VFullPath(VFullPath copy) {
            super(copy);
            this.mThemeAttrs = copy.mThemeAttrs;
            this.mStrokeColor = copy.mStrokeColor;
            this.mStrokeWidth = copy.mStrokeWidth;
            this.mStrokeAlpha = copy.mStrokeAlpha;
            this.mFillColor = copy.mFillColor;
            this.mFillRule = copy.mFillRule;
            this.mFillAlpha = copy.mFillAlpha;
            this.mTrimPathStart = copy.mTrimPathStart;
            this.mTrimPathEnd = copy.mTrimPathEnd;
            this.mTrimPathOffset = copy.mTrimPathOffset;
            this.mStrokeLineCap = copy.mStrokeLineCap;
            this.mStrokeLineJoin = copy.mStrokeLineJoin;
            this.mStrokeMiterlimit = copy.mStrokeMiterlimit;
        }

        private Cap getStrokeLineCap(int id, Cap defValue) {
            switch (id) {
                case 0:
                    return Cap.BUTT;
                case 1:
                    return Cap.ROUND;
                case 2:
                    return Cap.SQUARE;
                default:
                    return defValue;
            }
        }

        private Join getStrokeLineJoin(int id, Join defValue) {
            switch (id) {
                case 0:
                    return Join.MITER;
                case 1:
                    return Join.ROUND;
                case 2:
                    return Join.BEVEL;
                default:
                    return defValue;
            }
        }

        public boolean canApplyTheme() {
            return this.mThemeAttrs != null;
        }

        public void inflate(Resources r, AttributeSet attrs, Theme theme, XmlPullParser parser) {
            TypedArray a = TypedArrayUtils.obtainAttributes(r, theme, attrs, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_PATH);
            updateStateFromTypedArray(a, parser, theme);
            a.recycle();
        }

        private void updateStateFromTypedArray(TypedArray a, XmlPullParser parser, Theme theme) {
            this.mThemeAttrs = null;
            if (TypedArrayUtils.hasAttribute(parser, "pathData")) {
                String pathName = a.getString(null);
                if (pathName != null) {
                    this.mPathName = pathName;
                }
                String pathData = a.getString(2);
                if (pathData != null) {
                    this.mNodes = PathParser.createNodesFromPathData(pathData);
                }
                this.mFillColor = TypedArrayUtils.getNamedComplexColor(a, parser, theme, "fillColor", 1, 0);
                this.mFillAlpha = TypedArrayUtils.getNamedFloat(a, parser, "fillAlpha", 12, this.mFillAlpha);
                this.mStrokeLineCap = getStrokeLineCap(TypedArrayUtils.getNamedInt(a, parser, "strokeLineCap", 8, -1), this.mStrokeLineCap);
                this.mStrokeLineJoin = getStrokeLineJoin(TypedArrayUtils.getNamedInt(a, parser, "strokeLineJoin", 9, -1), this.mStrokeLineJoin);
                this.mStrokeMiterlimit = TypedArrayUtils.getNamedFloat(a, parser, "strokeMiterLimit", 10, this.mStrokeMiterlimit);
                this.mStrokeColor = TypedArrayUtils.getNamedComplexColor(a, parser, theme, "strokeColor", 3, 0);
                this.mStrokeAlpha = TypedArrayUtils.getNamedFloat(a, parser, "strokeAlpha", 11, this.mStrokeAlpha);
                this.mStrokeWidth = TypedArrayUtils.getNamedFloat(a, parser, "strokeWidth", 4, this.mStrokeWidth);
                this.mTrimPathEnd = TypedArrayUtils.getNamedFloat(a, parser, "trimPathEnd", 6, this.mTrimPathEnd);
                this.mTrimPathOffset = TypedArrayUtils.getNamedFloat(a, parser, "trimPathOffset", 7, this.mTrimPathOffset);
                this.mTrimPathStart = TypedArrayUtils.getNamedFloat(a, parser, "trimPathStart", 5, this.mTrimPathStart);
                this.mFillRule = TypedArrayUtils.getNamedInt(a, parser, "fillType", 13, this.mFillRule);
            }
        }

        public boolean isStateful() {
            if (!this.mFillColor.isStateful()) {
                if (!this.mStrokeColor.isStateful()) {
                    return false;
                }
            }
            return true;
        }

        public boolean onStateChanged(int[] stateSet) {
            return this.mFillColor.onStateChanged(stateSet) | this.mStrokeColor.onStateChanged(stateSet);
        }

        public void applyTheme(Theme t) {
            if (this.mThemeAttrs != null) {
            }
        }

        @ColorInt
        int getStrokeColor() {
            return this.mStrokeColor.getColor();
        }

        void setStrokeColor(int strokeColor) {
            this.mStrokeColor.setColor(strokeColor);
        }

        float getStrokeWidth() {
            return this.mStrokeWidth;
        }

        void setStrokeWidth(float strokeWidth) {
            this.mStrokeWidth = strokeWidth;
        }

        float getStrokeAlpha() {
            return this.mStrokeAlpha;
        }

        void setStrokeAlpha(float strokeAlpha) {
            this.mStrokeAlpha = strokeAlpha;
        }

        @ColorInt
        int getFillColor() {
            return this.mFillColor.getColor();
        }

        void setFillColor(int fillColor) {
            this.mFillColor.setColor(fillColor);
        }

        float getFillAlpha() {
            return this.mFillAlpha;
        }

        void setFillAlpha(float fillAlpha) {
            this.mFillAlpha = fillAlpha;
        }

        float getTrimPathStart() {
            return this.mTrimPathStart;
        }

        void setTrimPathStart(float trimPathStart) {
            this.mTrimPathStart = trimPathStart;
        }

        float getTrimPathEnd() {
            return this.mTrimPathEnd;
        }

        void setTrimPathEnd(float trimPathEnd) {
            this.mTrimPathEnd = trimPathEnd;
        }

        float getTrimPathOffset() {
            return this.mTrimPathOffset;
        }

        void setTrimPathOffset(float trimPathOffset) {
            this.mTrimPathOffset = trimPathOffset;
        }
    }

    private void inflateInternal(android.content.res.Resources r17, org.xmlpull.v1.XmlPullParser r18, android.util.AttributeSet r19, android.content.res.Resources.Theme r20) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:36:0x00e8 in {4, 11, 12, 17, 18, 23, 24, 25, 26, 30, 31, 33, 35} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r16 = this;
        r0 = r17;
        r1 = r18;
        r2 = r19;
        r3 = r20;
        r4 = r16;
        r5 = r4.mVectorState;
        r6 = r5.mVPathRenderer;
        r7 = 1;
        r8 = new java.util.ArrayDeque;
        r8.<init>();
        r9 = r6.mRootGroup;
        r8.push(r9);
        r9 = r18.getEventType();
        r10 = r18.getDepth();
        r11 = 1;
        r10 = r10 + r11;
    L_0x0023:
        if (r9 == r11) goto L_0x00dd;
    L_0x0025:
        r12 = r18.getDepth();
        r13 = 3;
        if (r12 >= r10) goto L_0x002e;
    L_0x002c:
        if (r9 == r13) goto L_0x00dd;
    L_0x002e:
        r12 = 2;
        if (r9 != r12) goto L_0x00c5;
    L_0x0031:
        r12 = r18.getName();
        r13 = r8.peek();
        r13 = (android.support.graphics.drawable.VectorDrawableCompat.VGroup) r13;
        r14 = "path";
        r14 = r14.equals(r12);
        if (r14 == 0) goto L_0x0068;
    L_0x0043:
        r14 = new android.support.graphics.drawable.VectorDrawableCompat$VFullPath;
        r14.<init>();
        r14.inflate(r0, r2, r3, r1);
        r15 = r13.mChildren;
        r15.add(r14);
        r15 = r14.getPathName();
        if (r15 == 0) goto L_0x005f;
    L_0x0056:
        r15 = r6.mVGTargetsMap;
        r11 = r14.getPathName();
        r15.put(r11, r14);
    L_0x005f:
        r7 = 0;
        r11 = r5.mChangingConfigurations;
        r15 = r14.mChangingConfigurations;
        r11 = r11 | r15;
        r5.mChangingConfigurations = r11;
        goto L_0x00c4;
    L_0x0068:
        r11 = "clip-path";
        r11 = r11.equals(r12);
        if (r11 == 0) goto L_0x0094;
    L_0x0070:
        r11 = new android.support.graphics.drawable.VectorDrawableCompat$VClipPath;
        r11.<init>();
        r11.inflate(r0, r2, r3, r1);
        r14 = r13.mChildren;
        r14.add(r11);
        r14 = r11.getPathName();
        if (r14 == 0) goto L_0x008c;
    L_0x0083:
        r14 = r6.mVGTargetsMap;
        r15 = r11.getPathName();
        r14.put(r15, r11);
    L_0x008c:
        r14 = r5.mChangingConfigurations;
        r15 = r11.mChangingConfigurations;
        r14 = r14 | r15;
        r5.mChangingConfigurations = r14;
        goto L_0x00c3;
    L_0x0094:
        r11 = "group";
        r11 = r11.equals(r12);
        if (r11 == 0) goto L_0x00c3;
    L_0x009c:
        r11 = new android.support.graphics.drawable.VectorDrawableCompat$VGroup;
        r11.<init>();
        r11.inflate(r0, r2, r3, r1);
        r14 = r13.mChildren;
        r14.add(r11);
        r8.push(r11);
        r14 = r11.getGroupName();
        if (r14 == 0) goto L_0x00bb;
    L_0x00b2:
        r14 = r6.mVGTargetsMap;
        r15 = r11.getGroupName();
        r14.put(r15, r11);
    L_0x00bb:
        r14 = r5.mChangingConfigurations;
        r15 = r11.mChangingConfigurations;
        r14 = r14 | r15;
        r5.mChangingConfigurations = r14;
        goto L_0x00c4;
    L_0x00c4:
        goto L_0x00d6;
    L_0x00c5:
        if (r9 != r13) goto L_0x00c4;
    L_0x00c7:
        r11 = r18.getName();
        r12 = "group";
        r12 = r12.equals(r11);
        if (r12 == 0) goto L_0x00d6;
    L_0x00d3:
        r8.pop();
    L_0x00d6:
        r9 = r18.next();
        r11 = 1;
        goto L_0x0023;
    L_0x00dd:
        if (r7 != 0) goto L_0x00e0;
    L_0x00df:
        return;
    L_0x00e0:
        r11 = new org.xmlpull.v1.XmlPullParserException;
        r12 = "no path defined";
        r11.<init>(r12);
        throw r11;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.VectorDrawableCompat.inflateInternal(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):void");
    }

    public /* bridge */ /* synthetic */ void applyTheme(Theme theme) {
        super.applyTheme(theme);
    }

    public /* bridge */ /* synthetic */ void clearColorFilter() {
        super.clearColorFilter();
    }

    public /* bridge */ /* synthetic */ ColorFilter getColorFilter() {
        return super.getColorFilter();
    }

    public /* bridge */ /* synthetic */ Drawable getCurrent() {
        return super.getCurrent();
    }

    public /* bridge */ /* synthetic */ int getMinimumHeight() {
        return super.getMinimumHeight();
    }

    public /* bridge */ /* synthetic */ int getMinimumWidth() {
        return super.getMinimumWidth();
    }

    public /* bridge */ /* synthetic */ boolean getPadding(Rect rect) {
        return super.getPadding(rect);
    }

    public /* bridge */ /* synthetic */ int[] getState() {
        return super.getState();
    }

    public /* bridge */ /* synthetic */ Region getTransparentRegion() {
        return super.getTransparentRegion();
    }

    public /* bridge */ /* synthetic */ void jumpToCurrentState() {
        super.jumpToCurrentState();
    }

    public /* bridge */ /* synthetic */ void setChangingConfigurations(int i) {
        super.setChangingConfigurations(i);
    }

    public /* bridge */ /* synthetic */ void setColorFilter(int i, Mode mode) {
        super.setColorFilter(i, mode);
    }

    public /* bridge */ /* synthetic */ void setFilterBitmap(boolean z) {
        super.setFilterBitmap(z);
    }

    public /* bridge */ /* synthetic */ void setHotspot(float f, float f2) {
        super.setHotspot(f, f2);
    }

    public /* bridge */ /* synthetic */ void setHotspotBounds(int i, int i2, int i3, int i4) {
        super.setHotspotBounds(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ boolean setState(int[] iArr) {
        return super.setState(iArr);
    }

    VectorDrawableCompat() {
        this.mAllowCaching = true;
        this.mTmpFloats = new float[9];
        this.mTmpMatrix = new Matrix();
        this.mTmpBounds = new Rect();
        this.mVectorState = new VectorDrawableCompatState();
    }

    VectorDrawableCompat(@NonNull VectorDrawableCompatState state) {
        this.mAllowCaching = true;
        this.mTmpFloats = new float[9];
        this.mTmpMatrix = new Matrix();
        this.mTmpBounds = new Rect();
        this.mVectorState = state;
        this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
    }

    public Drawable mutate() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.mutate();
            return this;
        }
        if (!this.mMutated && super.mutate() == this) {
            this.mVectorState = new VectorDrawableCompatState(this.mVectorState);
            this.mMutated = true;
        }
        return this;
    }

    Object getTargetByName(String name) {
        return this.mVectorState.mVPathRenderer.mVGTargetsMap.get(name);
    }

    public ConstantState getConstantState() {
        if (this.mDelegateDrawable != null && VERSION.SDK_INT >= 24) {
            return new VectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
        }
        this.mVectorState.mChangingConfigurations = getChangingConfigurations();
        return this.mVectorState;
    }

    public void draw(Canvas canvas) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.draw(canvas);
            return;
        }
        copyBounds(this.mTmpBounds);
        if (this.mTmpBounds.width() > 0) {
            if (this.mTmpBounds.height() > 0) {
                ColorFilter colorFilter = this.mColorFilter;
                if (colorFilter == null) {
                    colorFilter = this.mTintFilter;
                }
                canvas.getMatrix(this.mTmpMatrix);
                this.mTmpMatrix.getValues(this.mTmpFloats);
                float canvasScaleX = Math.abs(this.mTmpFloats[0]);
                float canvasScaleY = Math.abs(this.mTmpFloats[4]);
                float canvasSkewX = Math.abs(this.mTmpFloats[1]);
                float canvasSkewY = Math.abs(this.mTmpFloats[3]);
                if (!(canvasSkewX == 0.0f && canvasSkewY == 0.0f)) {
                    canvasScaleX = 1.0f;
                    canvasScaleY = 1.0f;
                }
                int scaledHeight = (int) (((float) this.mTmpBounds.height()) * canvasScaleY);
                int scaledWidth = Math.min(2048, (int) (((float) this.mTmpBounds.width()) * canvasScaleX));
                scaledHeight = Math.min(2048, scaledHeight);
                if (scaledWidth > 0) {
                    if (scaledHeight > 0) {
                        int saveCount = canvas.save();
                        canvas.translate((float) this.mTmpBounds.left, (float) this.mTmpBounds.top);
                        if (needMirroring()) {
                            canvas.translate((float) this.mTmpBounds.width(), 0.0f);
                            canvas.scale(-1.0f, 1.0f);
                        }
                        this.mTmpBounds.offsetTo(0, 0);
                        this.mVectorState.createCachedBitmapIfNeeded(scaledWidth, scaledHeight);
                        if (!this.mAllowCaching) {
                            this.mVectorState.updateCachedBitmap(scaledWidth, scaledHeight);
                        } else if (!this.mVectorState.canReuseCache()) {
                            this.mVectorState.updateCachedBitmap(scaledWidth, scaledHeight);
                            this.mVectorState.updateCacheStates();
                        }
                        this.mVectorState.drawCachedBitmapWithRootAlpha(canvas, colorFilter, this.mTmpBounds);
                        canvas.restoreToCount(saveCount);
                    }
                }
            }
        }
    }

    public int getAlpha() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.getAlpha(this.mDelegateDrawable);
        }
        return this.mVectorState.mVPathRenderer.getRootAlpha();
    }

    public void setAlpha(int alpha) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setAlpha(alpha);
            return;
        }
        if (this.mVectorState.mVPathRenderer.getRootAlpha() != alpha) {
            this.mVectorState.mVPathRenderer.setRootAlpha(alpha);
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setColorFilter(colorFilter);
            return;
        }
        this.mColorFilter = colorFilter;
        invalidateSelf();
    }

    PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter tintFilter, ColorStateList tint, Mode tintMode) {
        if (tint != null) {
            if (tintMode != null) {
                return new PorterDuffColorFilter(tint.getColorForState(getState(), 0), tintMode);
            }
        }
        return null;
    }

    public void setTint(int tint) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTint(this.mDelegateDrawable, tint);
        } else {
            setTintList(ColorStateList.valueOf(tint));
        }
    }

    public void setTintList(ColorStateList tint) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintList(this.mDelegateDrawable, tint);
            return;
        }
        VectorDrawableCompatState state = this.mVectorState;
        if (state.mTint != tint) {
            state.mTint = tint;
            this.mTintFilter = updateTintFilter(this.mTintFilter, tint, state.mTintMode);
            invalidateSelf();
        }
    }

    public void setTintMode(Mode tintMode) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintMode(this.mDelegateDrawable, tintMode);
            return;
        }
        VectorDrawableCompatState state = this.mVectorState;
        if (state.mTintMode != tintMode) {
            state.mTintMode = tintMode;
            this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, tintMode);
            invalidateSelf();
        }
    }

    public boolean isStateful() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.isStateful();
        }
        boolean z;
        if (!super.isStateful()) {
            VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
            if (vectorDrawableCompatState != null) {
                if (!vectorDrawableCompatState.isStateful()) {
                    if (this.mVectorState.mTint != null && this.mVectorState.mTint.isStateful()) {
                    }
                }
            }
            z = false;
            return z;
        }
        z = true;
        return z;
    }

    protected boolean onStateChange(int[] stateSet) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setState(stateSet);
        }
        boolean changed = false;
        VectorDrawableCompatState state = this.mVectorState;
        if (!(state.mTint == null || state.mTintMode == null)) {
            this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
            invalidateSelf();
            changed = true;
        }
        if (state.isStateful() && state.onStateChanged(stateSet)) {
            invalidateSelf();
            changed = true;
        }
        return changed;
    }

    public int getOpacity() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getOpacity();
        }
        return -3;
    }

    public int getIntrinsicWidth() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicWidth();
        }
        return (int) this.mVectorState.mVPathRenderer.mBaseWidth;
    }

    public int getIntrinsicHeight() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicHeight();
        }
        return (int) this.mVectorState.mVPathRenderer.mBaseHeight;
    }

    public boolean canApplyTheme() {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.canApplyTheme(this.mDelegateDrawable);
        }
        return false;
    }

    public boolean isAutoMirrored() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
        }
        return this.mVectorState.mAutoMirrored;
    }

    public void setAutoMirrored(boolean mirrored) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setAutoMirrored(this.mDelegateDrawable, mirrored);
        } else {
            this.mVectorState.mAutoMirrored = mirrored;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public float getPixelSize() {
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        if (!(vectorDrawableCompatState == null || vectorDrawableCompatState.mVPathRenderer == null || this.mVectorState.mVPathRenderer.mBaseWidth == 0.0f || this.mVectorState.mVPathRenderer.mBaseHeight == 0.0f || this.mVectorState.mVPathRenderer.mViewportHeight == 0.0f)) {
            if (this.mVectorState.mVPathRenderer.mViewportWidth != 0.0f) {
                float intrinsicWidth = this.mVectorState.mVPathRenderer.mBaseWidth;
                float intrinsicHeight = this.mVectorState.mVPathRenderer.mBaseHeight;
                return Math.min(this.mVectorState.mVPathRenderer.mViewportWidth / intrinsicWidth, this.mVectorState.mVPathRenderer.mViewportHeight / intrinsicHeight);
            }
        }
        return 1.0f;
    }

    @Nullable
    public static VectorDrawableCompat create(@NonNull Resources res, @DrawableRes int resId, @Nullable Theme theme) {
        if (VERSION.SDK_INT >= 24) {
            VectorDrawableCompat drawable = new VectorDrawableCompat();
            drawable.mDelegateDrawable = ResourcesCompat.getDrawable(res, resId, theme);
            drawable.mCachedConstantStateDelegate = new VectorDrawableDelegateState(drawable.mDelegateDrawable.getConstantState());
            return drawable;
        }
        try {
            int type;
            XmlPullParser parser = res.getXml(resId);
            AttributeSet attrs = Xml.asAttributeSet(parser);
            while (true) {
                int next = parser.next();
                type = next;
                if (next == 2 || type == 1) {
                    if (type == 2) {
                        return createFromXmlInner(res, parser, attrs, theme);
                    }
                    throw new XmlPullParserException("No start tag found");
                }
            }
            if (type == 2) {
                return createFromXmlInner(res, parser, attrs, theme);
            }
            throw new XmlPullParserException("No start tag found");
        } catch (XmlPullParserException e) {
            Log.e(LOGTAG, "parser error", e);
            return null;
        } catch (IOException e2) {
            Log.e(LOGTAG, "parser error", e2);
            return null;
        }
    }

    public static VectorDrawableCompat createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        VectorDrawableCompat drawable = new VectorDrawableCompat();
        drawable.inflate(r, parser, attrs, theme);
        return drawable;
    }

    static int applyAlpha(int color, float alpha) {
        return (color & ViewCompat.MEASURED_SIZE_MASK) | (((int) (((float) Color.alpha(color)) * alpha)) << 24);
    }

    public void inflate(Resources res, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.inflate(res, parser, attrs);
        } else {
            inflate(res, parser, attrs, null);
        }
    }

    public void inflate(Resources res, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.inflate(this.mDelegateDrawable, res, parser, attrs, theme);
            return;
        }
        VectorDrawableCompatState state = this.mVectorState;
        state.mVPathRenderer = new VPathRenderer();
        TypedArray a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_TYPE_ARRAY);
        updateStateFromTypedArray(a, parser);
        a.recycle();
        state.mChangingConfigurations = getChangingConfigurations();
        state.mCacheDirty = true;
        inflateInternal(res, parser, attrs, theme);
        this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
    }

    private static Mode parseTintModeCompat(int value, Mode defaultMode) {
        if (value == 3) {
            return Mode.SRC_OVER;
        }
        if (value == 5) {
            return Mode.SRC_IN;
        }
        if (value == 9) {
            return Mode.SRC_ATOP;
        }
        switch (value) {
            case 14:
                return Mode.MULTIPLY;
            case 15:
                return Mode.SCREEN;
            case 16:
                return Mode.ADD;
            default:
                return defaultMode;
        }
    }

    private void updateStateFromTypedArray(TypedArray a, XmlPullParser parser) throws XmlPullParserException {
        VectorDrawableCompatState state = this.mVectorState;
        VPathRenderer pathRenderer = state.mVPathRenderer;
        state.mTintMode = parseTintModeCompat(TypedArrayUtils.getNamedInt(a, parser, "tintMode", 6, -1), Mode.SRC_IN);
        ColorStateList tint = a.getColorStateList(1);
        if (tint != null) {
            state.mTint = tint;
        }
        state.mAutoMirrored = TypedArrayUtils.getNamedBoolean(a, parser, "autoMirrored", 5, state.mAutoMirrored);
        pathRenderer.mViewportWidth = TypedArrayUtils.getNamedFloat(a, parser, "viewportWidth", 7, pathRenderer.mViewportWidth);
        pathRenderer.mViewportHeight = TypedArrayUtils.getNamedFloat(a, parser, "viewportHeight", 8, pathRenderer.mViewportHeight);
        StringBuilder stringBuilder;
        if (pathRenderer.mViewportWidth <= 0.0f) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(a.getPositionDescription());
            stringBuilder.append("<vector> tag requires viewportWidth > 0");
            throw new XmlPullParserException(stringBuilder.toString());
        } else if (pathRenderer.mViewportHeight > 0.0f) {
            pathRenderer.mBaseWidth = a.getDimension(3, pathRenderer.mBaseWidth);
            pathRenderer.mBaseHeight = a.getDimension(2, pathRenderer.mBaseHeight);
            if (pathRenderer.mBaseWidth <= 0.0f) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(a.getPositionDescription());
                stringBuilder.append("<vector> tag requires width > 0");
                throw new XmlPullParserException(stringBuilder.toString());
            } else if (pathRenderer.mBaseHeight > 0.0f) {
                pathRenderer.setAlpha(TypedArrayUtils.getNamedFloat(a, parser, "alpha", 4, pathRenderer.getAlpha()));
                String name = a.getString(null);
                if (name != null) {
                    pathRenderer.mRootName = name;
                    pathRenderer.mVGTargetsMap.put(name, pathRenderer);
                }
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(a.getPositionDescription());
                stringBuilder.append("<vector> tag requires height > 0");
                throw new XmlPullParserException(stringBuilder.toString());
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(a.getPositionDescription());
            stringBuilder.append("<vector> tag requires viewportHeight > 0");
            throw new XmlPullParserException(stringBuilder.toString());
        }
    }

    private void printGroupTree(VGroup currentGroup, int level) {
        int i;
        StringBuilder stringBuilder;
        String indent = "";
        for (i = 0; i < level; i++) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(indent);
            stringBuilder.append("    ");
            indent = stringBuilder.toString();
        }
        String str = LOGTAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append(indent);
        stringBuilder.append("current group is :");
        stringBuilder.append(currentGroup.getGroupName());
        stringBuilder.append(" rotation is ");
        stringBuilder.append(currentGroup.mRotate);
        Log.v(str, stringBuilder.toString());
        str = LOGTAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append(indent);
        stringBuilder.append("matrix is :");
        stringBuilder.append(currentGroup.getLocalMatrix().toString());
        Log.v(str, stringBuilder.toString());
        for (i = 0; i < currentGroup.mChildren.size(); i++) {
            VObject child = (VObject) currentGroup.mChildren.get(i);
            if (child instanceof VGroup) {
                printGroupTree((VGroup) child, level + 1);
            } else {
                ((VPath) child).printVPath(level + 1);
            }
        }
    }

    void setAllowCaching(boolean allowCaching) {
        this.mAllowCaching = allowCaching;
    }

    private boolean needMirroring() {
        boolean z = false;
        if (VERSION.SDK_INT < 17) {
            return false;
        }
        if (isAutoMirrored() && DrawableCompat.getLayoutDirection(this) == 1) {
            z = true;
        }
        return z;
    }

    protected void onBoundsChange(Rect bounds) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setBounds(bounds);
        }
    }

    public int getChangingConfigurations() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations();
    }

    public void invalidateSelf() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.invalidateSelf();
        } else {
            super.invalidateSelf();
        }
    }

    public void scheduleSelf(Runnable what, long when) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.scheduleSelf(what, when);
        } else {
            super.scheduleSelf(what, when);
        }
    }

    public boolean setVisible(boolean visible, boolean restart) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setVisible(visible, restart);
        }
        return super.setVisible(visible, restart);
    }

    public void unscheduleSelf(Runnable what) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.unscheduleSelf(what);
        } else {
            super.unscheduleSelf(what);
        }
    }
}
