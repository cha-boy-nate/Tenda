package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
    private static final int DEFAULT_FADE_COLOR = -858993460;
    private static final int DEFAULT_OVERHANG_SIZE = 32;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    private boolean mDisplayListReflectionLoaded;
    final ViewDragHelper mDragHelper;
    private boolean mFirstLayout;
    private Method mGetDisplayList;
    private float mInitialMotionX;
    private float mInitialMotionY;
    boolean mIsUnableToDrag;
    private final int mOverhangSize;
    private PanelSlideListener mPanelSlideListener;
    private int mParallaxBy;
    private float mParallaxOffset;
    final ArrayList<DisableLayerRunnable> mPostedRunnables;
    boolean mPreservedOpenState;
    private Field mRecreateDisplayList;
    private Drawable mShadowDrawableLeft;
    private Drawable mShadowDrawableRight;
    float mSlideOffset;
    int mSlideRange;
    View mSlideableView;
    private int mSliderFadeColor;
    private final Rect mTmpRect;

    private class DisableLayerRunnable implements Runnable {
        final View mChildView;

        DisableLayerRunnable(View childView) {
            this.mChildView = childView;
        }

        public void run() {
            if (this.mChildView.getParent() == SlidingPaneLayout.this) {
                this.mChildView.setLayerType(0, null);
                SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
            }
            SlidingPaneLayout.this.mPostedRunnables.remove(this);
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        private static final int[] ATTRS = new int[]{16843137};
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(@NonNull android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull LayoutParams source) {
            super(source);
            this.weight = source.weight;
        }

        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, ATTRS);
            this.weight = a.getFloat(0, 0.0f);
            a.recycle();
        }
    }

    public interface PanelSlideListener {
        void onPanelClosed(@NonNull View view);

        void onPanelOpened(@NonNull View view);

        void onPanelSlide(@NonNull View view, float f);
    }

    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect = new Rect();

        AccessibilityDelegate() {
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            AccessibilityNodeInfoCompat superNode = AccessibilityNodeInfoCompat.obtain(info);
            super.onInitializeAccessibilityNodeInfo(host, superNode);
            copyNodeInfoNoChildren(info, superNode);
            superNode.recycle();
            info.setClassName(SlidingPaneLayout.class.getName());
            info.setSource(host);
            ViewParent parent = ViewCompat.getParentForAccessibility(host);
            if (parent instanceof View) {
                info.setParent((View) parent);
            }
            int childCount = SlidingPaneLayout.this.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = SlidingPaneLayout.this.getChildAt(i);
                if (!filter(child) && child.getVisibility() == 0) {
                    ViewCompat.setImportantForAccessibility(child, 1);
                    info.addChild(child);
                }
            }
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(SlidingPaneLayout.class.getName());
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            if (filter(child)) {
                return false;
            }
            return super.onRequestSendAccessibilityEvent(host, child, event);
        }

        public boolean filter(View child) {
            return SlidingPaneLayout.this.isDimmed(child);
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat dest, AccessibilityNodeInfoCompat src) {
            Rect rect = this.mTmpRect;
            src.getBoundsInParent(rect);
            dest.setBoundsInParent(rect);
            src.getBoundsInScreen(rect);
            dest.setBoundsInScreen(rect);
            dest.setVisibleToUser(src.isVisibleToUser());
            dest.setPackageName(src.getPackageName());
            dest.setClassName(src.getClassName());
            dest.setContentDescription(src.getContentDescription());
            dest.setEnabled(src.isEnabled());
            dest.setClickable(src.isClickable());
            dest.setFocusable(src.isFocusable());
            dest.setFocused(src.isFocused());
            dest.setAccessibilityFocused(src.isAccessibilityFocused());
            dest.setSelected(src.isSelected());
            dest.setLongClickable(src.isLongClickable());
            dest.addAction(src.getActions());
            dest.setMovementGranularities(src.getMovementGranularities());
        }
    }

    private class DragHelperCallback extends Callback {
        DragHelperCallback() {
        }

        public boolean tryCaptureView(View child, int pointerId) {
            if (SlidingPaneLayout.this.mIsUnableToDrag) {
                return false;
            }
            return ((LayoutParams) child.getLayoutParams()).slideable;
        }

        public void onViewDragStateChanged(int state) {
            if (SlidingPaneLayout.this.mDragHelper.getViewDragState() != 0) {
                return;
            }
            if (SlidingPaneLayout.this.mSlideOffset == 0.0f) {
                SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
                slidingPaneLayout.updateObscuredViewsVisibility(slidingPaneLayout.mSlideableView);
                slidingPaneLayout = SlidingPaneLayout.this;
                slidingPaneLayout.dispatchOnPanelClosed(slidingPaneLayout.mSlideableView);
                SlidingPaneLayout.this.mPreservedOpenState = false;
                return;
            }
            slidingPaneLayout = SlidingPaneLayout.this;
            slidingPaneLayout.dispatchOnPanelOpened(slidingPaneLayout.mSlideableView);
            SlidingPaneLayout.this.mPreservedOpenState = true;
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
            SlidingPaneLayout.this.setAllChildrenVisible();
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            SlidingPaneLayout.this.onPanelDragged(left);
            SlidingPaneLayout.this.invalidate();
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left;
            LayoutParams lp = (LayoutParams) releasedChild.getLayoutParams();
            int startToRight;
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                startToRight = SlidingPaneLayout.this.getPaddingRight() + lp.rightMargin;
                if (xvel < 0.0f || (xvel == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f)) {
                    startToRight += SlidingPaneLayout.this.mSlideRange;
                }
                left = (SlidingPaneLayout.this.getWidth() - startToRight) - SlidingPaneLayout.this.mSlideableView.getWidth();
            } else {
                startToRight = SlidingPaneLayout.this.getPaddingLeft() + lp.leftMargin;
                if (xvel <= 0.0f) {
                    if (xvel != 0.0f || SlidingPaneLayout.this.mSlideOffset <= 0.5f) {
                        left = startToRight;
                    }
                }
                left = startToRight + SlidingPaneLayout.this.mSlideRange;
            }
            SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
            SlidingPaneLayout.this.invalidate();
        }

        public int getViewHorizontalDragRange(View child) {
            return SlidingPaneLayout.this.mSlideRange;
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            LayoutParams lp = (LayoutParams) SlidingPaneLayout.this.mSlideableView.getLayoutParams();
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                int startBound = SlidingPaneLayout.this.getWidth() - ((SlidingPaneLayout.this.getPaddingRight() + lp.rightMargin) + SlidingPaneLayout.this.mSlideableView.getWidth());
                return Math.max(Math.min(left, startBound), startBound - SlidingPaneLayout.this.mSlideRange);
            }
            startBound = SlidingPaneLayout.this.getPaddingLeft() + lp.leftMargin;
            return Math.min(Math.max(left, startBound), SlidingPaneLayout.this.mSlideRange + startBound);
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }

        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, pointerId);
        }
    }

    static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new C01591();
        boolean isOpen;

        /* renamed from: android.support.v4.widget.SlidingPaneLayout$SavedState$1 */
        static class C01591 implements ClassLoaderCreator<SavedState> {
            C01591() {
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, null);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.isOpen = in.readInt() != 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.isOpen);
        }
    }

    public static class SimplePanelSlideListener implements PanelSlideListener {
        public void onPanelSlide(View panel, float slideOffset) {
        }

        public void onPanelOpened(View panel) {
        }

        public void onPanelClosed(View panel) {
        }
    }

    public SlidingPaneLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlidingPaneLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingPaneLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSliderFadeColor = DEFAULT_FADE_COLOR;
        this.mFirstLayout = true;
        this.mTmpRect = new Rect();
        this.mPostedRunnables = new ArrayList();
        float density = context.getResources().getDisplayMetrics().density;
        this.mOverhangSize = (int) ((32.0f * density) + 0.5f);
        setWillNotDraw(false);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        ViewCompat.setImportantForAccessibility(this, 1);
        this.mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
        this.mDragHelper.setMinVelocity(400.0f * density);
    }

    public void setParallaxDistance(@Px int parallaxBy) {
        this.mParallaxBy = parallaxBy;
        requestLayout();
    }

    @Px
    public int getParallaxDistance() {
        return this.mParallaxBy;
    }

    public void setSliderFadeColor(@ColorInt int color) {
        this.mSliderFadeColor = color;
    }

    @ColorInt
    public int getSliderFadeColor() {
        return this.mSliderFadeColor;
    }

    public void setCoveredFadeColor(@ColorInt int color) {
        this.mCoveredFadeColor = color;
    }

    @ColorInt
    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }

    public void setPanelSlideListener(@Nullable PanelSlideListener listener) {
        this.mPanelSlideListener = listener;
    }

    void dispatchOnPanelSlide(View panel) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            panelSlideListener.onPanelSlide(panel, this.mSlideOffset);
        }
    }

    void dispatchOnPanelOpened(View panel) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            panelSlideListener.onPanelOpened(panel);
        }
        sendAccessibilityEvent(32);
    }

    void dispatchOnPanelClosed(View panel) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            panelSlideListener.onPanelClosed(panel);
        }
        sendAccessibilityEvent(32);
    }

    void updateObscuredViewsVisibility(View panel) {
        int left;
        boolean z;
        View view = panel;
        boolean isLayoutRtl = isLayoutRtlSupport();
        int startBound = isLayoutRtl ? getWidth() - getPaddingRight() : getPaddingLeft();
        int endBound = isLayoutRtl ? getPaddingLeft() : getWidth() - getPaddingRight();
        int topBound = getPaddingTop();
        int bottomBound = getHeight() - getPaddingBottom();
        int bottom;
        int top;
        int right;
        if (view == null || !viewIsOpaque(panel)) {
            left = 0;
            bottom = 0;
            top = 0;
            right = 0;
        } else {
            left = panel.getLeft();
            right = panel.getRight();
            top = panel.getTop();
            bottom = panel.getBottom();
        }
        int i = 0;
        int childCount = getChildCount();
        while (i < childCount) {
            View child = getChildAt(i);
            if (child == view) {
                z = isLayoutRtl;
                return;
            }
            if (child.getVisibility() == 8) {
                z = isLayoutRtl;
            } else {
                int clampedChildLeft = Math.max(isLayoutRtl ? endBound : startBound, child.getLeft());
                int clampedChildTop = Math.max(topBound, child.getTop());
                z = isLayoutRtl;
                int clampedChildRight = Math.min(isLayoutRtl ? startBound : endBound, child.getRight());
                int clampedChildBottom = Math.min(bottomBound, child.getBottom());
                if (clampedChildLeft < left || clampedChildTop < top || clampedChildRight > right || clampedChildBottom > bottom) {
                    clampedChildRight = 0;
                } else {
                    int i2 = clampedChildRight;
                    clampedChildRight = 4;
                }
                child.setVisibility(clampedChildRight);
            }
            i++;
            isLayoutRtl = z;
            view = panel;
        }
        SlidingPaneLayout slidingPaneLayout = this;
        z = isLayoutRtl;
    }

    void setAllChildrenVisible() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 4) {
                child.setVisibility(0);
            }
        }
    }

    private static boolean viewIsOpaque(View v) {
        boolean z = true;
        if (v.isOpaque()) {
            return true;
        }
        if (VERSION.SDK_INT >= 18) {
            return false;
        }
        Drawable bg = v.getBackground();
        if (bg == null) {
            return false;
        }
        if (bg.getOpacity() != -1) {
            z = false;
        }
        return z;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
        int count = this.mPostedRunnables.size();
        for (int i = 0; i < count; i++) {
            ((DisableLayerRunnable) this.mPostedRunnables.get(i)).run();
        }
        this.mPostedRunnables.clear();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize;
        int i;
        int i2;
        int i3;
        SlidingPaneLayout slidingPaneLayout = this;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize2 = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != 1073741824) {
            if (!isInEditMode()) {
                throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
            } else if (widthMode == Integer.MIN_VALUE) {
                widthMode = 1073741824;
            } else if (widthMode == 0) {
                widthMode = 1073741824;
                widthSize = 300;
            }
        } else if (heightMode == 0) {
            if (!isInEditMode()) {
                throw new IllegalStateException("Height must not be UNSPECIFIED");
            } else if (heightMode == 0) {
                heightMode = Integer.MIN_VALUE;
                heightSize2 = 300;
            }
        }
        int layoutHeight = 0;
        int maxLayoutHeight = 0;
        if (heightMode == Integer.MIN_VALUE) {
            maxLayoutHeight = (heightSize2 - getPaddingTop()) - getPaddingBottom();
        } else if (heightMode == 1073741824) {
            int paddingTop = (heightSize2 - getPaddingTop()) - getPaddingBottom();
            maxLayoutHeight = paddingTop;
            layoutHeight = paddingTop;
        }
        float weightSum = 0.0f;
        boolean canSlide = false;
        int widthAvailable = (widthSize - getPaddingLeft()) - getPaddingRight();
        int widthRemaining = widthAvailable;
        int childCount = getChildCount();
        if (childCount > 2) {
            Log.e(TAG, "onMeasure: More than two child views are not supported.");
        }
        slidingPaneLayout.mSlideableView = null;
        int i4 = 0;
        while (true) {
            int i5 = 8;
            if (i4 >= childCount) {
                break;
            }
            View child = getChildAt(i4);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int widthMode2 = widthMode;
            if (child.getVisibility() == 8) {
                lp.dimWhenOffset = false;
                heightSize = heightSize2;
            } else {
                if (lp.weight > 0) {
                    weightSum += lp.weight;
                    if (lp.width == 0) {
                        heightSize = heightSize2;
                    }
                }
                widthMode = lp.leftMargin + lp.rightMargin;
                heightSize = heightSize2;
                if (lp.width == -2) {
                    heightSize2 = MeasureSpec.makeMeasureSpec(widthAvailable - widthMode, Integer.MIN_VALUE);
                } else if (lp.width == -1) {
                    heightSize2 = MeasureSpec.makeMeasureSpec(widthAvailable - widthMode, 1073741824);
                } else {
                    heightSize2 = MeasureSpec.makeMeasureSpec(lp.width, 1073741824);
                }
                int horizontalMargin = widthMode;
                if (lp.height == -2) {
                    i5 = MeasureSpec.makeMeasureSpec(maxLayoutHeight, Integer.MIN_VALUE);
                } else if (lp.height == -1) {
                    i5 = MeasureSpec.makeMeasureSpec(maxLayoutHeight, 1073741824);
                } else {
                    i5 = MeasureSpec.makeMeasureSpec(lp.height, 1073741824);
                }
                child.measure(heightSize2, i5);
                widthMode = child.getMeasuredWidth();
                heightSize2 = child.getMeasuredHeight();
                float weightSum2 = weightSum;
                if (heightMode == Integer.MIN_VALUE && heightSize2 > layoutHeight) {
                    layoutHeight = Math.min(heightSize2, maxLayoutHeight);
                }
                widthRemaining -= widthMode;
                boolean canSlide2 = widthRemaining < 0;
                lp.slideable = canSlide2;
                canSlide2 |= canSlide;
                if (lp.slideable) {
                    slidingPaneLayout.mSlideableView = child;
                }
                canSlide = canSlide2;
                weightSum = weightSum2;
            }
            i4++;
            widthMode = widthMode2;
            heightSize2 = heightSize;
        }
        heightSize = heightSize2;
        if (!canSlide) {
            if (weightSum <= 0.0f) {
                i = heightMode;
                i2 = maxLayoutHeight;
                i3 = childCount;
                setMeasuredDimension(widthSize, (getPaddingTop() + layoutHeight) + getPaddingBottom());
                slidingPaneLayout.mCanSlide = canSlide;
                if (slidingPaneLayout.mDragHelper.getViewDragState() != 0 && !canSlide) {
                    slidingPaneLayout.mDragHelper.abort();
                    return;
                }
            }
        }
        widthMode = widthAvailable - slidingPaneLayout.mOverhangSize;
        heightSize2 = 0;
        while (heightSize2 < childCount) {
            int fixedPanelWidthLimit;
            View child2 = getChildAt(heightSize2);
            if (child2.getVisibility() == i5) {
                fixedPanelWidthLimit = widthMode;
                i = heightMode;
                i2 = maxLayoutHeight;
                i3 = childCount;
            } else {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (child2.getVisibility() == i5) {
                    fixedPanelWidthLimit = widthMode;
                    i = heightMode;
                    i2 = maxLayoutHeight;
                    i3 = childCount;
                } else {
                    boolean skippedFirstPass = lp2.width == 0 && lp2.weight > 0.0f;
                    i = skippedFirstPass ? 0 : child2.getMeasuredWidth();
                    boolean z;
                    if (!canSlide || child2 == slidingPaneLayout.mSlideableView) {
                        i3 = childCount;
                        z = skippedFirstPass;
                        i5 = i;
                        i = heightMode;
                        if (lp2.weight > 0.0f) {
                            if (lp2.width != 0) {
                                heightMode = MeasureSpec.makeMeasureSpec(child2.getMeasuredHeight(), 1073741824);
                            } else if (lp2.height == -2) {
                                heightMode = MeasureSpec.makeMeasureSpec(maxLayoutHeight, Integer.MIN_VALUE);
                            } else if (lp2.height == -1) {
                                heightMode = MeasureSpec.makeMeasureSpec(maxLayoutHeight, 1073741824);
                            } else {
                                heightMode = MeasureSpec.makeMeasureSpec(lp2.height, 1073741824);
                            }
                            if (canSlide) {
                                i4 = widthAvailable - (lp2.leftMargin + lp2.rightMargin);
                                fixedPanelWidthLimit = widthMode;
                                i2 = maxLayoutHeight;
                                maxLayoutHeight = MeasureSpec.makeMeasureSpec(i4, 1073741824);
                                if (i5 != i4) {
                                    child2.measure(maxLayoutHeight, heightMode);
                                }
                            } else {
                                fixedPanelWidthLimit = widthMode;
                                i2 = maxLayoutHeight;
                                child2.measure(MeasureSpec.makeMeasureSpec(i5 + ((int) ((lp2.weight * ((float) Math.max(0, widthRemaining))) / weightSum)), 1073741824), heightMode);
                            }
                        } else {
                            fixedPanelWidthLimit = widthMode;
                            i2 = maxLayoutHeight;
                        }
                    } else if (lp2.width < 0) {
                        if (i <= widthMode) {
                            i = heightMode;
                            if (lp2.weight <= 0.0f) {
                                fixedPanelWidthLimit = widthMode;
                                i2 = maxLayoutHeight;
                                i3 = childCount;
                            }
                        } else {
                            i = heightMode;
                        }
                        if (skippedFirstPass) {
                            i3 = childCount;
                            if (lp2.height == -2) {
                                childCount = MeasureSpec.makeMeasureSpec(maxLayoutHeight, Integer.MIN_VALUE);
                                heightMode = 1073741824;
                            } else if (lp2.height == -1) {
                                heightMode = 1073741824;
                                childCount = MeasureSpec.makeMeasureSpec(maxLayoutHeight, 1073741824);
                            } else {
                                heightMode = 1073741824;
                                childCount = MeasureSpec.makeMeasureSpec(lp2.height, 1073741824);
                            }
                        } else {
                            i3 = childCount;
                            heightMode = 1073741824;
                            childCount = MeasureSpec.makeMeasureSpec(child2.getMeasuredHeight(), 1073741824);
                        }
                        child2.measure(MeasureSpec.makeMeasureSpec(widthMode, heightMode), childCount);
                        fixedPanelWidthLimit = widthMode;
                        i2 = maxLayoutHeight;
                    } else {
                        i3 = childCount;
                        z = skippedFirstPass;
                        i5 = i;
                        i = heightMode;
                        fixedPanelWidthLimit = widthMode;
                        i2 = maxLayoutHeight;
                    }
                }
            }
            heightSize2++;
            heightMode = i;
            childCount = i3;
            widthMode = fixedPanelWidthLimit;
            maxLayoutHeight = i2;
            i5 = 8;
        }
        i = heightMode;
        i2 = maxLayoutHeight;
        i3 = childCount;
        setMeasuredDimension(widthSize, (getPaddingTop() + layoutHeight) + getPaddingBottom());
        slidingPaneLayout.mCanSlide = canSlide;
        if (slidingPaneLayout.mDragHelper.getViewDragState() != 0) {
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        SlidingPaneLayout slidingPaneLayout = this;
        boolean isLayoutRtl = isLayoutRtlSupport();
        if (isLayoutRtl) {
            slidingPaneLayout.mDragHelper.setEdgeTrackingEnabled(2);
        } else {
            slidingPaneLayout.mDragHelper.setEdgeTrackingEnabled(1);
        }
        int width = r - l;
        int paddingStart = isLayoutRtl ? getPaddingRight() : getPaddingLeft();
        int paddingEnd = isLayoutRtl ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        int xStart = paddingStart;
        int nextXStart = xStart;
        if (slidingPaneLayout.mFirstLayout) {
            float f = (slidingPaneLayout.mCanSlide && slidingPaneLayout.mPreservedOpenState) ? 1.0f : 0.0f;
            slidingPaneLayout.mSlideOffset = f;
        }
        int i = 0;
        while (i < childCount) {
            int paddingStart2;
            int i2;
            View child = getChildAt(i);
            if (child.getVisibility() == 8) {
                paddingStart2 = paddingStart;
            } else {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                int offset = 0;
                if (lp.slideable) {
                    int margin = lp.leftMargin + lp.rightMargin;
                    int range = (Math.min(nextXStart, (width - paddingEnd) - slidingPaneLayout.mOverhangSize) - xStart) - margin;
                    slidingPaneLayout.mSlideRange = range;
                    int lpMargin = isLayoutRtl ? lp.rightMargin : lp.leftMargin;
                    paddingStart2 = paddingStart;
                    lp.dimWhenOffset = ((xStart + lpMargin) + range) + (childWidth / 2) > width - paddingEnd ? 1 : 0;
                    paddingStart = (int) (((float) range) * slidingPaneLayout.mSlideOffset);
                    xStart += paddingStart + lpMargin;
                    slidingPaneLayout.mSlideOffset = ((float) paddingStart) / ((float) slidingPaneLayout.mSlideRange);
                } else {
                    paddingStart2 = paddingStart;
                    if (slidingPaneLayout.mCanSlide) {
                        i2 = slidingPaneLayout.mParallaxBy;
                        if (i2 != 0) {
                            xStart = nextXStart;
                            offset = (int) ((1065353216 - slidingPaneLayout.mSlideOffset) * ((float) i2));
                        }
                    }
                    xStart = nextXStart;
                }
                if (isLayoutRtl) {
                    i2 = (width - xStart) + offset;
                    paddingStart = i2 - childWidth;
                } else {
                    paddingStart = xStart - offset;
                    i2 = paddingStart + childWidth;
                }
                child.layout(paddingStart, paddingTop, i2, child.getMeasuredHeight() + paddingTop);
                nextXStart += child.getWidth();
            }
            i++;
            paddingStart = paddingStart2;
        }
        if (slidingPaneLayout.mFirstLayout) {
            if (slidingPaneLayout.mCanSlide) {
                if (slidingPaneLayout.mParallaxBy != 0) {
                    parallaxOtherViews(slidingPaneLayout.mSlideOffset);
                }
                if (((LayoutParams) slidingPaneLayout.mSlideableView.getLayoutParams()).dimWhenOffset) {
                    dimChildView(slidingPaneLayout.mSlideableView, slidingPaneLayout.mSlideOffset, slidingPaneLayout.mSliderFadeColor);
                }
            } else {
                for (i2 = 0; i2 < childCount; i2++) {
                    dimChildView(getChildAt(i2), 0.0f, slidingPaneLayout.mSliderFadeColor);
                }
            }
            updateObscuredViewsVisibility(slidingPaneLayout.mSlideableView);
        }
        slidingPaneLayout.mFirstLayout = false;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            this.mFirstLayout = true;
        }
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (!isInTouchMode() && !this.mCanSlide) {
            this.mPreservedOpenState = child == this.mSlideableView;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        boolean z = true;
        if (!this.mCanSlide && action == 0 && getChildCount() > 1) {
            View secondChild = getChildAt(1);
            if (secondChild != null) {
                this.mPreservedOpenState = this.mDragHelper.isViewUnder(secondChild, (int) ev.getX(), (int) ev.getY()) ^ true;
            }
        }
        if (this.mCanSlide) {
            if (!this.mIsUnableToDrag || action == 0) {
                if (action != 3) {
                    if (action != 1) {
                        boolean interceptTap = false;
                        float x;
                        float y;
                        if (action == 0) {
                            this.mIsUnableToDrag = false;
                            x = ev.getX();
                            y = ev.getY();
                            this.mInitialMotionX = x;
                            this.mInitialMotionY = y;
                            if (this.mDragHelper.isViewUnder(this.mSlideableView, (int) x, (int) y) && isDimmed(this.mSlideableView)) {
                                interceptTap = true;
                            }
                        } else if (action == 2) {
                            x = ev.getX();
                            y = ev.getY();
                            float adx = Math.abs(x - this.mInitialMotionX);
                            float ady = Math.abs(y - this.mInitialMotionY);
                            if (adx > ((float) this.mDragHelper.getTouchSlop()) && ady > adx) {
                                this.mDragHelper.cancel();
                                this.mIsUnableToDrag = true;
                                return false;
                            }
                        }
                        if (!this.mDragHelper.shouldInterceptTouchEvent(ev)) {
                            if (!interceptTap) {
                                z = false;
                            }
                        }
                        return z;
                    }
                }
                this.mDragHelper.cancel();
                return false;
            }
        }
        this.mDragHelper.cancel();
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.mCanSlide) {
            return super.onTouchEvent(ev);
        }
        this.mDragHelper.processTouchEvent(ev);
        float x;
        float y;
        switch (ev.getActionMasked()) {
            case 0:
                x = ev.getX();
                y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                break;
            case 1:
                if (isDimmed(this.mSlideableView)) {
                    x = ev.getX();
                    y = ev.getY();
                    float dx = x - this.mInitialMotionX;
                    float dy = y - this.mInitialMotionY;
                    int slop = this.mDragHelper.getTouchSlop();
                    if ((dx * dx) + (dy * dy) < ((float) (slop * slop)) && this.mDragHelper.isViewUnder(this.mSlideableView, (int) x, (int) y)) {
                        closePane(this.mSlideableView, 0);
                        break;
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean closePane(View pane, int initialVelocity) {
        if (!this.mFirstLayout) {
            if (!smoothSlideTo(0.0f, initialVelocity)) {
                return false;
            }
        }
        this.mPreservedOpenState = false;
        return true;
    }

    private boolean openPane(View pane, int initialVelocity) {
        if (!this.mFirstLayout) {
            if (!smoothSlideTo(1.0f, initialVelocity)) {
                return false;
            }
        }
        this.mPreservedOpenState = true;
        return true;
    }

    @Deprecated
    public void smoothSlideOpen() {
        openPane();
    }

    public boolean openPane() {
        return openPane(this.mSlideableView, 0);
    }

    @Deprecated
    public void smoothSlideClosed() {
        closePane();
    }

    public boolean closePane() {
        return closePane(this.mSlideableView, 0);
    }

    public boolean isOpen() {
        if (this.mCanSlide) {
            if (this.mSlideOffset != 1.0f) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    public boolean canSlide() {
        return this.mCanSlide;
    }

    public boolean isSlideable() {
        return this.mCanSlide;
    }

    void onPanelDragged(int newLeft) {
        if (this.mSlideableView == null) {
            this.mSlideOffset = 0.0f;
            return;
        }
        boolean isLayoutRtl = isLayoutRtlSupport();
        LayoutParams lp = (LayoutParams) this.mSlideableView.getLayoutParams();
        this.mSlideOffset = ((float) ((isLayoutRtl ? (getWidth() - newLeft) - this.mSlideableView.getWidth() : newLeft) - ((isLayoutRtl ? getPaddingRight() : getPaddingLeft()) + (isLayoutRtl ? lp.rightMargin : lp.leftMargin)))) / ((float) this.mSlideRange);
        if (this.mParallaxBy != 0) {
            parallaxOtherViews(this.mSlideOffset);
        }
        if (lp.dimWhenOffset) {
            dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
        }
        dispatchOnPanelSlide(this.mSlideableView);
    }

    private void dimChildView(View v, float mag, int fadeColor) {
        LayoutParams lp = (LayoutParams) v.getLayoutParams();
        if (mag > 0.0f && fadeColor != 0) {
            int color = (((int) (((float) ((ViewCompat.MEASURED_STATE_MASK & fadeColor) >>> 24)) * mag)) << 24) | (ViewCompat.MEASURED_SIZE_MASK & fadeColor);
            if (lp.dimPaint == null) {
                lp.dimPaint = new Paint();
            }
            lp.dimPaint.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_OVER));
            if (v.getLayerType() != 2) {
                v.setLayerType(2, lp.dimPaint);
            }
            invalidateChildRegion(v);
        } else if (v.getLayerType() != 0) {
            if (lp.dimPaint != null) {
                lp.dimPaint.setColorFilter(null);
            }
            DisableLayerRunnable dlr = new DisableLayerRunnable(v);
            this.mPostedRunnables.add(dlr);
            ViewCompat.postOnAnimation(this, dlr);
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int save = canvas.save();
        if (!(!this.mCanSlide || lp.slideable || this.mSlideableView == null)) {
            canvas.getClipBounds(this.mTmpRect);
            Rect rect;
            if (isLayoutRtlSupport()) {
                rect = this.mTmpRect;
                rect.left = Math.max(rect.left, this.mSlideableView.getRight());
            } else {
                rect = this.mTmpRect;
                rect.right = Math.min(rect.right, this.mSlideableView.getLeft());
            }
            canvas.clipRect(this.mTmpRect);
        }
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(save);
        return result;
    }

    void invalidateChildRegion(View v) {
        if (VERSION.SDK_INT >= 17) {
            ViewCompat.setLayerPaint(v, ((LayoutParams) v.getLayoutParams()).dimPaint);
            return;
        }
        if (VERSION.SDK_INT >= 16) {
            if (!this.mDisplayListReflectionLoaded) {
                try {
                    this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[]) null);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "Couldn't fetch getDisplayList method; dimming won't work right.", e);
                }
                try {
                    this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
                    this.mRecreateDisplayList.setAccessible(true);
                } catch (NoSuchFieldException e2) {
                    Log.e(TAG, "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e2);
                }
                this.mDisplayListReflectionLoaded = true;
            }
            if (this.mGetDisplayList != null) {
                Field field = this.mRecreateDisplayList;
                if (field != null) {
                    try {
                        field.setBoolean(v, true);
                        this.mGetDisplayList.invoke(v, (Object[]) null);
                    } catch (Exception e3) {
                        Log.e(TAG, "Error refreshing display list state", e3);
                    }
                }
            }
            v.invalidate();
            return;
        }
        ViewCompat.postInvalidateOnAnimation(this, v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
    }

    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!this.mCanSlide) {
            return false;
        }
        int x;
        LayoutParams lp = (LayoutParams) this.mSlideableView.getLayoutParams();
        if (isLayoutRtlSupport()) {
            x = (int) (((float) getWidth()) - ((((float) (getPaddingRight() + lp.rightMargin)) + (((float) this.mSlideRange) * slideOffset)) + ((float) this.mSlideableView.getWidth())));
        } else {
            x = (int) (((float) (getPaddingLeft() + lp.leftMargin)) + (((float) this.mSlideRange) * slideOffset));
        }
        ViewDragHelper viewDragHelper = this.mDragHelper;
        View view = this.mSlideableView;
        if (!viewDragHelper.smoothSlideViewTo(view, x, view.getTop())) {
            return false;
        }
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    public void computeScroll() {
        if (this.mDragHelper.continueSettling(true)) {
            if (this.mCanSlide) {
                ViewCompat.postInvalidateOnAnimation(this);
            } else {
                this.mDragHelper.abort();
            }
        }
    }

    @Deprecated
    public void setShadowDrawable(Drawable d) {
        setShadowDrawableLeft(d);
    }

    public void setShadowDrawableLeft(@Nullable Drawable d) {
        this.mShadowDrawableLeft = d;
    }

    public void setShadowDrawableRight(@Nullable Drawable d) {
        this.mShadowDrawableRight = d;
    }

    @Deprecated
    public void setShadowResource(@DrawableRes int resId) {
        setShadowDrawable(getResources().getDrawable(resId));
    }

    public void setShadowResourceLeft(int resId) {
        setShadowDrawableLeft(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setShadowResourceRight(int resId) {
        setShadowDrawableRight(ContextCompat.getDrawable(getContext(), resId));
    }

    public void draw(Canvas c) {
        Drawable shadowDrawable;
        super.draw(c);
        if (isLayoutRtlSupport()) {
            shadowDrawable = this.mShadowDrawableRight;
        } else {
            shadowDrawable = this.mShadowDrawableLeft;
        }
        View shadowView = getChildCount() > 1 ? getChildAt(1) : null;
        if (shadowView != null) {
            if (shadowDrawable != null) {
                int left;
                int right;
                int top = shadowView.getTop();
                int bottom = shadowView.getBottom();
                int shadowWidth = shadowDrawable.getIntrinsicWidth();
                if (isLayoutRtlSupport()) {
                    left = shadowView.getRight();
                    right = left + shadowWidth;
                } else {
                    right = shadowView.getLeft();
                    left = right - shadowWidth;
                }
                shadowDrawable.setBounds(left, top, right, bottom);
                shadowDrawable.draw(c);
            }
        }
    }

    private void parallaxOtherViews(float slideOffset) {
        boolean dimViews;
        int childCount;
        int i;
        View v;
        boolean isLayoutRtl = isLayoutRtlSupport();
        LayoutParams slideLp = (LayoutParams) this.mSlideableView.getLayoutParams();
        if (slideLp.dimWhenOffset) {
            if ((isLayoutRtl ? slideLp.rightMargin : slideLp.leftMargin) <= 0) {
                dimViews = true;
                childCount = getChildCount();
                for (i = 0; i < childCount; i++) {
                    v = getChildAt(i);
                    if (v == this.mSlideableView) {
                        float f = 1.0f - this.mParallaxOffset;
                        int i2 = this.mParallaxBy;
                        int oldOffset = (int) (f * ((float) i2));
                        this.mParallaxOffset = slideOffset;
                        int dx = oldOffset - ((int) ((1.0f - slideOffset) * ((float) i2)));
                        v.offsetLeftAndRight(isLayoutRtl ? -dx : dx);
                        if (!dimViews) {
                            dimChildView(v, isLayoutRtl ? this.mParallaxOffset - 1.0f : 1.0f - this.mParallaxOffset, this.mCoveredFadeColor);
                        }
                    }
                }
            }
        }
        dimViews = false;
        childCount = getChildCount();
        for (i = 0; i < childCount; i++) {
            v = getChildAt(i);
            if (v == this.mSlideableView) {
                float f2 = 1.0f - this.mParallaxOffset;
                int i22 = this.mParallaxBy;
                int oldOffset2 = (int) (f2 * ((float) i22));
                this.mParallaxOffset = slideOffset;
                int dx2 = oldOffset2 - ((int) ((1.0f - slideOffset) * ((float) i22)));
                if (isLayoutRtl) {
                }
                v.offsetLeftAndRight(isLayoutRtl ? -dx2 : dx2);
                if (!dimViews) {
                    if (isLayoutRtl) {
                    }
                    dimChildView(v, isLayoutRtl ? this.mParallaxOffset - 1.0f : 1.0f - this.mParallaxOffset, this.mCoveredFadeColor);
                }
            }
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        View view = v;
        boolean z = true;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            for (int i = group.getChildCount() - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()) {
                    if (canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                        return true;
                    }
                }
            }
        }
        if (checkV) {
            if (v.canScrollHorizontally(isLayoutRtlSupport() ? dx : -dx)) {
                return z;
            }
        }
        int i2 = dx;
        z = false;
        return z;
    }

    boolean isDimmed(View child) {
        boolean z = false;
        if (child == null) {
            return false;
        }
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (this.mCanSlide && lp.dimWhenOffset && this.mSlideOffset > 0.0f) {
            z = true;
        }
        return z;
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams ? new LayoutParams((MarginLayoutParams) p) : new LayoutParams(p);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.isOpen = isSlideable() ? isOpen() : this.mPreservedOpenState;
        return ss;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (ss.isOpen) {
                openPane();
            } else {
                closePane();
            }
            this.mPreservedOpenState = ss.isOpen;
            return;
        }
        super.onRestoreInstanceState(state);
    }

    boolean isLayoutRtlSupport() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }
}
