package android.support.v7.graphics.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.appcompat.C0185R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable extends Drawable {
    public static final int ARROW_DIRECTION_END = 3;
    public static final int ARROW_DIRECTION_LEFT = 0;
    public static final int ARROW_DIRECTION_RIGHT = 1;
    public static final int ARROW_DIRECTION_START = 2;
    private static final float ARROW_HEAD_ANGLE = ((float) Math.toRadians(45.0d));
    private float mArrowHeadLength;
    private float mArrowShaftLength;
    private float mBarGap;
    private float mBarLength;
    private int mDirection = 2;
    private float mMaxCutForBarSize;
    private final Paint mPaint = new Paint();
    private final Path mPath = new Path();
    private float mProgress;
    private final int mSize;
    private boolean mSpin;
    private boolean mVerticalMirror = false;

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ArrowDirection {
    }

    public DrawerArrowDrawable(Context context) {
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeJoin(Join.MITER);
        this.mPaint.setStrokeCap(Cap.BUTT);
        this.mPaint.setAntiAlias(true);
        TypedArray a = context.getTheme().obtainStyledAttributes(null, C0185R.styleable.DrawerArrowToggle, C0185R.attr.drawerArrowStyle, C0185R.style.Base_Widget_AppCompat_DrawerArrowToggle);
        setColor(a.getColor(C0185R.styleable.DrawerArrowToggle_color, 0));
        setBarThickness(a.getDimension(C0185R.styleable.DrawerArrowToggle_thickness, 0.0f));
        setSpinEnabled(a.getBoolean(C0185R.styleable.DrawerArrowToggle_spinBars, true));
        setGapSize((float) Math.round(a.getDimension(C0185R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0f)));
        this.mSize = a.getDimensionPixelSize(C0185R.styleable.DrawerArrowToggle_drawableSize, 0);
        this.mBarLength = (float) Math.round(a.getDimension(C0185R.styleable.DrawerArrowToggle_barLength, 0.0f));
        this.mArrowHeadLength = (float) Math.round(a.getDimension(C0185R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0f));
        this.mArrowShaftLength = a.getDimension(C0185R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0f);
        a.recycle();
    }

    public void setArrowHeadLength(float length) {
        if (this.mArrowHeadLength != length) {
            this.mArrowHeadLength = length;
            invalidateSelf();
        }
    }

    public float getArrowHeadLength() {
        return this.mArrowHeadLength;
    }

    public void setArrowShaftLength(float length) {
        if (this.mArrowShaftLength != length) {
            this.mArrowShaftLength = length;
            invalidateSelf();
        }
    }

    public float getArrowShaftLength() {
        return this.mArrowShaftLength;
    }

    public float getBarLength() {
        return this.mBarLength;
    }

    public void setBarLength(float length) {
        if (this.mBarLength != length) {
            this.mBarLength = length;
            invalidateSelf();
        }
    }

    public void setColor(@ColorInt int color) {
        if (color != this.mPaint.getColor()) {
            this.mPaint.setColor(color);
            invalidateSelf();
        }
    }

    @ColorInt
    public int getColor() {
        return this.mPaint.getColor();
    }

    public void setBarThickness(float width) {
        if (this.mPaint.getStrokeWidth() != width) {
            this.mPaint.setStrokeWidth(width);
            double d = (double) (width / 2.0f);
            double cos = Math.cos((double) ARROW_HEAD_ANGLE);
            Double.isNaN(d);
            this.mMaxCutForBarSize = (float) (d * cos);
            invalidateSelf();
        }
    }

    public float getBarThickness() {
        return this.mPaint.getStrokeWidth();
    }

    public float getGapSize() {
        return this.mBarGap;
    }

    public void setGapSize(float gap) {
        if (gap != this.mBarGap) {
            this.mBarGap = gap;
            invalidateSelf();
        }
    }

    public void setDirection(int direction) {
        if (direction != this.mDirection) {
            this.mDirection = direction;
            invalidateSelf();
        }
    }

    public boolean isSpinEnabled() {
        return this.mSpin;
    }

    public void setSpinEnabled(boolean enabled) {
        if (this.mSpin != enabled) {
            this.mSpin = enabled;
            invalidateSelf();
        }
    }

    public int getDirection() {
        return this.mDirection;
    }

    public void setVerticalMirror(boolean verticalMirror) {
        if (this.mVerticalMirror != verticalMirror) {
            this.mVerticalMirror = verticalMirror;
            invalidateSelf();
        }
    }

    public void draw(Canvas canvas) {
        boolean flipToPointRight;
        Canvas canvas2 = canvas;
        Rect bounds = getBounds();
        int i = this.mDirection;
        boolean z = false;
        if (i != 3) {
            switch (i) {
                case 0:
                    flipToPointRight = false;
                    break;
                case 1:
                    flipToPointRight = true;
                    break;
                default:
                    if (DrawableCompat.getLayoutDirection(this) == 1) {
                        z = true;
                    }
                    flipToPointRight = z;
                    break;
            }
        }
        if (DrawableCompat.getLayoutDirection(this) == 0) {
            z = true;
        }
        flipToPointRight = z;
        float f = r0.mArrowHeadLength;
        f = lerp(r0.mBarLength, (float) Math.sqrt((double) ((f * f) * 2.0f)), r0.mProgress);
        float arrowShaftLength = lerp(r0.mBarLength, r0.mArrowShaftLength, r0.mProgress);
        float arrowShaftCut = (float) Math.round(lerp(0.0f, r0.mMaxCutForBarSize, r0.mProgress));
        float rotation = lerp(0.0f, ARROW_HEAD_ANGLE, r0.mProgress);
        double d = (double) f;
        float canvasRotate = lerp(flipToPointRight ? 0.0f : -180.0f, flipToPointRight ? 180.0f : 0.0f, r0.mProgress);
        double cos = Math.cos((double) rotation);
        Double.isNaN(d);
        float arrowWidth = (float) Math.round(d * cos);
        d = (double) f;
        double sin = Math.sin((double) rotation);
        Double.isNaN(d);
        float arrowHeight = (float) Math.round(d * sin);
        r0.mPath.rewind();
        float topBottomBarOffset = lerp(r0.mBarGap + r0.mPaint.getStrokeWidth(), -r0.mMaxCutForBarSize, r0.mProgress);
        float arrowEdge = (-arrowShaftLength) / 2.0f;
        r0.mPath.moveTo(arrowEdge + arrowShaftCut, 0.0f);
        r0.mPath.rLineTo(arrowShaftLength - (arrowShaftCut * 2.0f), 0.0f);
        r0.mPath.moveTo(arrowEdge, topBottomBarOffset);
        r0.mPath.rLineTo(arrowWidth, arrowHeight);
        r0.mPath.moveTo(arrowEdge, -topBottomBarOffset);
        r0.mPath.rLineTo(arrowWidth, -arrowHeight);
        r0.mPath.close();
        canvas.save();
        float barThickness = r0.mPaint.getStrokeWidth();
        float height = ((float) bounds.height()) - (3.0f * barThickness);
        float f2 = r0.mBarGap;
        canvas2.translate((float) bounds.centerX(), ((float) ((((int) (height - (2.0f * f2))) / 4) * 2)) + ((1.5f * barThickness) + f2));
        if (r0.mSpin) {
            canvas2.rotate(((float) ((r0.mVerticalMirror ^ flipToPointRight) != 0 ? -1 : 1)) * canvasRotate);
        } else if (flipToPointRight) {
            canvas2.rotate(180.0f);
        }
        canvas2.drawPath(r0.mPath, r0.mPaint);
        canvas.restore();
    }

    public void setAlpha(int alpha) {
        if (alpha != this.mPaint.getAlpha()) {
            this.mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public int getIntrinsicHeight() {
        return this.mSize;
    }

    public int getIntrinsicWidth() {
        return this.mSize;
    }

    public int getOpacity() {
        return -3;
    }

    @FloatRange(from = 0.0d, to = 1.0d)
    public float getProgress() {
        return this.mProgress;
    }

    public void setProgress(@FloatRange(from = 0.0d, to = 1.0d) float progress) {
        if (this.mProgress != progress) {
            this.mProgress = progress;
            invalidateSelf();
        }
    }

    public final Paint getPaint() {
        return this.mPaint;
    }

    private static float lerp(float a, float b, float t) {
        return ((b - a) * t) + a;
    }
}
