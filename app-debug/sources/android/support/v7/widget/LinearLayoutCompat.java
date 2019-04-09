package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0185R;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerMode {
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.gravity = -1;
            TypedArray a = c.obtainStyledAttributes(attrs, C0185R.styleable.LinearLayoutCompat_Layout);
            this.weight = a.getFloat(C0185R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = a.getInt(C0185R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height);
            this.gravity = -1;
            this.weight = weight;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
            this.gravity = -1;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.gravity = -1;
            this.weight = source.weight;
            this.gravity = source.gravity;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, C0185R.styleable.LinearLayoutCompat, defStyleAttr, 0);
        int index = a.getInt(C0185R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (index >= 0) {
            setOrientation(index);
        }
        index = a.getInt(C0185R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (index >= 0) {
            setGravity(index);
        }
        boolean baselineAligned = a.getBoolean(C0185R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!baselineAligned) {
            setBaselineAligned(baselineAligned);
        }
        this.mWeightSum = a.getFloat(C0185R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = a.getInt(C0185R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = a.getBoolean(C0185R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(a.getDrawable(C0185R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = a.getInt(C0185R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = a.getDimensionPixelSize(C0185R.styleable.LinearLayoutCompat_dividerPadding, 0);
        a.recycle();
    }

    public void setShowDividers(int showDividers) {
        if (showDividers != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = showDividers;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable divider) {
        if (divider != this.mDivider) {
            this.mDivider = divider;
            boolean z = false;
            if (divider != null) {
                this.mDividerWidth = divider.getIntrinsicWidth();
                this.mDividerHeight = divider.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (divider == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int padding) {
        this.mDividerPadding = padding;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    protected void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int count = getVirtualChildCount();
        int i = 0;
        while (i < count) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                drawHorizontalDivider(canvas, (child.getTop() - ((LayoutParams) child.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
            i++;
        }
        if (hasDividerBeforeChildAt(count)) {
            int bottom;
            View child2 = getVirtualChildAt(count - 1);
            if (child2 == null) {
                bottom = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                bottom = child2.getBottom() + ((LayoutParams) child2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, bottom);
        }
    }

    void drawDividersHorizontal(Canvas canvas) {
        int count = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i = 0;
        while (i < count) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                int position;
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (isLayoutRtl) {
                    position = child.getRight() + lp.rightMargin;
                } else {
                    position = (child.getLeft() - lp.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, position);
            }
            i++;
        }
        if (hasDividerBeforeChildAt(count)) {
            int position2;
            View child2 = getVirtualChildAt(count - 1);
            if (child2 != null) {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (isLayoutRtl) {
                    position2 = (child2.getLeft() - lp2.leftMargin) - this.mDividerWidth;
                } else {
                    position2 = child2.getRight() + lp2.rightMargin;
                }
            } else if (isLayoutRtl) {
                position2 = getPaddingLeft();
            } else {
                position2 = (getWidth() - getPaddingRight()) - this.mDividerWidth;
            }
            drawVerticalDivider(canvas, position2);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int top) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, top, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + top);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int left) {
        this.mDivider.setBounds(left, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + left, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean baselineAligned) {
        this.mBaselineAligned = baselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean enabled) {
        this.mUseLargestChild = enabled;
    }

    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i = this.mBaselineAlignedChildIndex;
        if (childCount > i) {
            View child = getChildAt(i);
            i = child.getBaseline();
            if (i != -1) {
                int childTop = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    int majorGravity = this.mGravity & 112;
                    if (majorGravity != 48) {
                        if (majorGravity == 16) {
                            childTop += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                        } else if (majorGravity == 80) {
                            childTop = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                        }
                    }
                }
                return (((LayoutParams) child.getLayoutParams()).topMargin + childTop) + i;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("base aligned child index out of range (0, ");
            stringBuilder.append(getChildCount());
            stringBuilder.append(")");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.mBaselineAlignedChildIndex = i;
    }

    View getVirtualChildAt(int index) {
        return getChildAt(index);
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float weightSum) {
        this.mWeightSum = Math.max(0.0f, weightSum);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mOrientation == 1) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @RestrictTo({Scope.LIBRARY})
    protected boolean hasDividerBeforeChildAt(int childIndex) {
        boolean hasVisibleViewBefore = false;
        if (childIndex == 0) {
            if ((this.mShowDividers & 1) != 0) {
                hasVisibleViewBefore = true;
            }
            return hasVisibleViewBefore;
        } else if (childIndex == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                hasVisibleViewBefore = true;
            }
            return hasVisibleViewBefore;
        } else if ((this.mShowDividers & 2) == 0) {
            return false;
        } else {
            hasVisibleViewBefore = false;
            for (int i = childIndex - 1; i >= 0; i--) {
                if (getChildAt(i).getVisibility() != 8) {
                    hasVisibleViewBefore = true;
                    break;
                }
            }
            return hasVisibleViewBefore;
        }
    }

    void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int weightedMaxWidth;
        int i;
        int heightMode;
        int maxWidth;
        int alternativeMaxWidth;
        int weightedMaxWidth2;
        int maxWidth2;
        int oldHeight;
        float totalWeight;
        int alternativeMaxWidth2;
        int i2;
        int i3;
        boolean delta;
        int i4 = widthMeasureSpec;
        int i5 = heightMeasureSpec;
        this.mTotalLength = 0;
        int childState = 0;
        float totalWeight2 = 0.0f;
        int count = getVirtualChildCount();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode2 = MeasureSpec.getMode(heightMeasureSpec);
        int baselineChildIndex = this.mBaselineAlignedChildIndex;
        boolean useLargestChild = this.mUseLargestChild;
        boolean matchWidth = false;
        int alternativeMaxWidth3 = 0;
        int maxWidth3 = 0;
        int weightedMaxWidth3 = 0;
        int largestChildHeight = 0;
        boolean skippedMeasure = false;
        int i6 = 0;
        boolean allFillParent = true;
        while (true) {
            weightedMaxWidth = weightedMaxWidth3;
            i = 8;
            if (i6 >= count) {
                break;
            }
            View child = getVirtualChildAt(i6);
            if (child == null) {
                r7.mTotalLength += measureNullChild(i6);
                heightMode = heightMode2;
                weightedMaxWidth3 = weightedMaxWidth;
                weightedMaxWidth = count;
            } else {
                int childState2 = childState;
                if (child.getVisibility() == 8) {
                    i6 += getChildrenSkipCount(child, i6);
                    heightMode = heightMode2;
                    weightedMaxWidth3 = weightedMaxWidth;
                    childState = childState2;
                    weightedMaxWidth = count;
                } else {
                    LayoutParams lp;
                    View child2;
                    if (hasDividerBeforeChildAt(i6)) {
                        r7.mTotalLength += r7.mDividerHeight;
                    }
                    LayoutParams lp2 = (LayoutParams) child.getLayoutParams();
                    float totalWeight3 = totalWeight2 + lp2.weight;
                    if (heightMode2 == 1073741824 && lp2.height == 0 && lp2.weight > 0.0f) {
                        i = r7.mTotalLength;
                        maxWidth = maxWidth3;
                        r7.mTotalLength = Math.max(i, (lp2.topMargin + i) + lp2.bottomMargin);
                        skippedMeasure = true;
                        lp = lp2;
                        alternativeMaxWidth = alternativeMaxWidth3;
                        heightMode = heightMode2;
                        weightedMaxWidth2 = weightedMaxWidth;
                        i5 = childState2;
                        maxWidth2 = maxWidth;
                        heightMode2 = largestChildHeight;
                        child2 = child;
                        weightedMaxWidth = count;
                    } else {
                        maxWidth = maxWidth3;
                        if (lp2.height != 0 || lp2.weight <= 0.0f) {
                            oldHeight = Integer.MIN_VALUE;
                        } else {
                            lp2.height = -2;
                            oldHeight = 0;
                        }
                        lp = lp2;
                        i5 = childState2;
                        maxWidth2 = maxWidth;
                        heightMode = heightMode2;
                        heightMode2 = largestChildHeight;
                        View child3 = child;
                        weightedMaxWidth2 = weightedMaxWidth;
                        weightedMaxWidth = count;
                        count = oldHeight;
                        alternativeMaxWidth = alternativeMaxWidth3;
                        measureChildBeforeLayout(child, i6, widthMeasureSpec, 0, heightMeasureSpec, totalWeight3 == 0.0f ? r7.mTotalLength : 0);
                        if (count != Integer.MIN_VALUE) {
                            lp.height = count;
                        }
                        i = child3.getMeasuredHeight();
                        childState = r7.mTotalLength;
                        child2 = child3;
                        r7.mTotalLength = Math.max(childState, (((childState + i) + lp.topMargin) + lp.bottomMargin) + getNextLocationOffset(child2));
                        if (useLargestChild) {
                            heightMode2 = Math.max(i, heightMode2);
                        }
                    }
                    if (baselineChildIndex >= 0 && baselineChildIndex == i6 + 1) {
                        r7.mBaselineChildTop = r7.mTotalLength;
                    }
                    if (i6 < baselineChildIndex) {
                        if (lp.weight > 0.0f) {
                            throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                        }
                    }
                    boolean matchWidthLocally = false;
                    if (widthMode != 1073741824) {
                        maxWidth3 = -1;
                        if (lp.width == -1) {
                            matchWidth = true;
                            matchWidthLocally = true;
                        }
                    } else {
                        maxWidth3 = -1;
                    }
                    childState = lp.leftMargin + lp.rightMargin;
                    weightedMaxWidth3 = child2.getMeasuredWidth() + childState;
                    oldHeight = Math.max(maxWidth2, weightedMaxWidth3);
                    alternativeMaxWidth3 = View.combineMeasuredStates(i5, child2.getMeasuredState());
                    boolean allFillParent2 = allFillParent && lp.width == maxWidth3;
                    if (lp.weight > 0.0f) {
                        i5 = Math.max(weightedMaxWidth2, matchWidthLocally ? childState : weightedMaxWidth3);
                        allFillParent = allFillParent2;
                        maxWidth3 = alternativeMaxWidth;
                    } else {
                        count = weightedMaxWidth2;
                        allFillParent = allFillParent2;
                        maxWidth3 = Math.max(alternativeMaxWidth, matchWidthLocally ? childState : weightedMaxWidth3);
                        i5 = count;
                    }
                    i6 += getChildrenSkipCount(child2, i6);
                    childState = alternativeMaxWidth3;
                    weightedMaxWidth3 = i5;
                    largestChildHeight = heightMode2;
                    alternativeMaxWidth3 = maxWidth3;
                    maxWidth3 = oldHeight;
                    totalWeight2 = totalWeight3;
                }
            }
            i6++;
            count = weightedMaxWidth;
            heightMode2 = heightMode;
            i4 = widthMeasureSpec;
            i5 = heightMeasureSpec;
        }
        i5 = childState;
        heightMode = heightMode2;
        heightMode2 = largestChildHeight;
        int i7 = alternativeMaxWidth3;
        alternativeMaxWidth3 = maxWidth3;
        maxWidth3 = i7;
        int i8 = weightedMaxWidth;
        weightedMaxWidth = count;
        count = i8;
        if (r7.mTotalLength > 0) {
            weightedMaxWidth3 = weightedMaxWidth;
            if (hasDividerBeforeChildAt(weightedMaxWidth3)) {
                r7.mTotalLength += r7.mDividerHeight;
            }
        } else {
            weightedMaxWidth3 = weightedMaxWidth;
        }
        if (useLargestChild) {
            i4 = heightMode;
            if (i4 == Integer.MIN_VALUE || i4 == 0) {
                r7.mTotalLength = 0;
                childState = 0;
                while (childState < weightedMaxWidth3) {
                    View child4 = getVirtualChildAt(childState);
                    if (child4 == null) {
                        r7.mTotalLength += measureNullChild(childState);
                        weightedMaxWidth2 = childState;
                    } else if (child4.getVisibility() == i) {
                        childState += getChildrenSkipCount(child4, childState);
                        childState++;
                        i = 8;
                    } else {
                        LayoutParams lp3 = (LayoutParams) child4.getLayoutParams();
                        i = r7.mTotalLength;
                        weightedMaxWidth2 = childState;
                        r7.mTotalLength = Math.max(i, (((i + heightMode2) + lp3.topMargin) + lp3.bottomMargin) + getNextLocationOffset(child4));
                    }
                    childState = weightedMaxWidth2;
                    childState++;
                    i = 8;
                }
                weightedMaxWidth2 = childState;
            }
        } else {
            i4 = heightMode;
        }
        r7.mTotalLength += getPaddingTop() + getPaddingBottom();
        childState = heightMeasureSpec;
        i6 = View.resolveSizeAndState(Math.max(r7.mTotalLength, getSuggestedMinimumHeight()), childState, 0);
        i = i6 & ViewCompat.MEASURED_SIZE_MASK;
        largestChildHeight = i - r7.mTotalLength;
        if (skippedMeasure) {
            weightedMaxWidth2 = largestChildHeight;
            totalWeight = totalWeight2;
            maxWidth2 = alternativeMaxWidth3;
        } else if (largestChildHeight == 0 || totalWeight2 <= 0.0f) {
            maxWidth3 = Math.max(maxWidth3, count);
            if (useLargestChild) {
                if (i4 != 1073741824) {
                    i = 0;
                    while (i < weightedMaxWidth3) {
                        alternativeMaxWidth2 = maxWidth3;
                        maxWidth3 = getVirtualChildAt(i);
                        if (maxWidth3 != 0) {
                            weightedMaxWidth2 = largestChildHeight;
                            totalWeight = totalWeight2;
                            if (maxWidth3.getVisibility() == 8) {
                                maxWidth2 = alternativeMaxWidth3;
                            } else {
                                lp3 = (LayoutParams) maxWidth3.getLayoutParams();
                                totalWeight2 = lp3.weight;
                                LayoutParams lp4;
                                if (totalWeight2 > 0.0f) {
                                    lp4 = lp3;
                                    totalWeight3 = totalWeight2;
                                    maxWidth2 = alternativeMaxWidth3;
                                    maxWidth3.measure(MeasureSpec.makeMeasureSpec(maxWidth3.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(heightMode2, 1073741824));
                                } else {
                                    lp4 = lp3;
                                    totalWeight3 = totalWeight2;
                                    maxWidth2 = alternativeMaxWidth3;
                                }
                            }
                        } else {
                            weightedMaxWidth2 = largestChildHeight;
                            totalWeight = totalWeight2;
                            maxWidth2 = alternativeMaxWidth3;
                        }
                        i++;
                        maxWidth3 = alternativeMaxWidth2;
                        largestChildHeight = weightedMaxWidth2;
                        totalWeight2 = totalWeight;
                        alternativeMaxWidth3 = maxWidth2;
                    }
                    alternativeMaxWidth2 = maxWidth3;
                    weightedMaxWidth2 = largestChildHeight;
                    totalWeight = totalWeight2;
                    maxWidth2 = alternativeMaxWidth3;
                } else {
                    alternativeMaxWidth2 = maxWidth3;
                    weightedMaxWidth2 = largestChildHeight;
                    totalWeight = totalWeight2;
                    maxWidth2 = alternativeMaxWidth3;
                }
            } else {
                alternativeMaxWidth2 = maxWidth3;
                weightedMaxWidth2 = largestChildHeight;
                totalWeight = totalWeight2;
                maxWidth2 = alternativeMaxWidth3;
            }
            alternativeMaxWidth = i4;
            i2 = count;
            maxWidth = heightMode2;
            i3 = baselineChildIndex;
            largestChildHeight = weightedMaxWidth2;
            heightMode2 = widthMeasureSpec;
            delta = useLargestChild;
            if (!(allFillParent || widthMode == 1073741824)) {
                maxWidth2 = alternativeMaxWidth2;
            }
            setMeasuredDimension(View.resolveSizeAndState(Math.max(maxWidth2 + (getPaddingLeft() + getPaddingRight()), getSuggestedMinimumWidth()), heightMode2, i5), i6);
            if (matchWidth) {
                forceUniformWidth(weightedMaxWidth3, childState);
            }
        } else {
            heightMode = i;
            weightedMaxWidth2 = largestChildHeight;
            totalWeight = totalWeight2;
            maxWidth2 = alternativeMaxWidth3;
        }
        totalWeight2 = r7.mWeightSum;
        if (totalWeight2 <= 0.0f) {
            totalWeight2 = totalWeight;
        }
        float weightSum = totalWeight2;
        r7.mTotalLength = 0;
        oldHeight = 0;
        alternativeMaxWidth3 = maxWidth3;
        largestChildHeight = weightedMaxWidth2;
        maxWidth3 = maxWidth2;
        while (oldHeight < weightedMaxWidth3) {
            delta = useLargestChild;
            View child5 = getVirtualChildAt(oldHeight);
            i2 = count;
            maxWidth = heightMode2;
            if (child5.getVisibility() == 8) {
                alternativeMaxWidth = i4;
                i3 = baselineChildIndex;
                heightMode2 = widthMeasureSpec;
            } else {
                float weightSum2;
                int delta2;
                boolean allFillParent3;
                int maxWidth4;
                LayoutParams lp5 = (LayoutParams) child5.getLayoutParams();
                float childExtra = lp5.weight;
                if (childExtra > 0.0f) {
                    i3 = baselineChildIndex;
                    baselineChildIndex = (int) ((((float) largestChildHeight) * childExtra) / weightSum);
                    weightSum2 = weightSum - childExtra;
                    delta2 = largestChildHeight - baselineChildIndex;
                    weightSum = getChildMeasureSpec(widthMeasureSpec, ((getPaddingLeft() + getPaddingRight()) + lp5.leftMargin) + lp5.rightMargin, lp5.width);
                    if (lp5.height != 0) {
                        alternativeMaxWidth = i4;
                    } else if (i4 != 1073741824) {
                        alternativeMaxWidth = i4;
                    } else {
                        alternativeMaxWidth = i4;
                        child5.measure(weightSum, MeasureSpec.makeMeasureSpec(baselineChildIndex > 0 ? baselineChildIndex : 0, 1073741824));
                        int i9 = baselineChildIndex;
                        i5 = View.combineMeasuredStates(i5, child5.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                    }
                    largestChildHeight = child5.getMeasuredHeight() + baselineChildIndex;
                    if (largestChildHeight < 0) {
                        largestChildHeight = 0;
                    }
                    child5.measure(weightSum, MeasureSpec.makeMeasureSpec(largestChildHeight, 1073741824));
                    i5 = View.combineMeasuredStates(i5, child5.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                } else {
                    alternativeMaxWidth = i4;
                    float f = childExtra;
                    i3 = baselineChildIndex;
                    heightMode2 = widthMeasureSpec;
                    weightSum2 = weightSum;
                    delta2 = largestChildHeight;
                }
                i = lp5.leftMargin + lp5.rightMargin;
                largestChildHeight = child5.getMeasuredWidth() + i;
                maxWidth3 = Math.max(maxWidth3, largestChildHeight);
                i4 = (widthMode == 1073741824 || lp5.width != -1) ? 0 : 1;
                alternativeMaxWidth3 = Math.max(alternativeMaxWidth3, i4 != 0 ? i : largestChildHeight);
                if (allFillParent) {
                    if (lp5.width == -1) {
                        allFillParent3 = true;
                        i = r7.mTotalLength;
                        maxWidth4 = maxWidth3;
                        r7.mTotalLength = Math.max(i, (((i + child5.getMeasuredHeight()) + lp5.topMargin) + lp5.bottomMargin) + getNextLocationOffset(child5));
                        allFillParent = allFillParent3;
                        largestChildHeight = delta2;
                        weightSum = weightSum2;
                        maxWidth3 = maxWidth4;
                    }
                }
                allFillParent3 = false;
                i = r7.mTotalLength;
                maxWidth4 = maxWidth3;
                r7.mTotalLength = Math.max(i, (((i + child5.getMeasuredHeight()) + lp5.topMargin) + lp5.bottomMargin) + getNextLocationOffset(child5));
                allFillParent = allFillParent3;
                largestChildHeight = delta2;
                weightSum = weightSum2;
                maxWidth3 = maxWidth4;
            }
            oldHeight++;
            useLargestChild = delta;
            count = i2;
            heightMode2 = maxWidth;
            baselineChildIndex = i3;
            i4 = alternativeMaxWidth;
        }
        delta = useLargestChild;
        i2 = count;
        maxWidth = heightMode2;
        i3 = baselineChildIndex;
        heightMode2 = widthMeasureSpec;
        r7.mTotalLength += getPaddingTop() + getPaddingBottom();
        maxWidth2 = maxWidth3;
        alternativeMaxWidth2 = alternativeMaxWidth3;
        maxWidth2 = alternativeMaxWidth2;
        setMeasuredDimension(View.resolveSizeAndState(Math.max(maxWidth2 + (getPaddingLeft() + getPaddingRight()), getSuggestedMinimumWidth()), heightMode2, i5), i6);
        if (matchWidth) {
            forceUniformWidth(weightedMaxWidth3, childState);
        }
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.width == -1) {
                    int oldHeight = lp.height;
                    lp.height = child.getMeasuredHeight();
                    measureChildWithMargins(child, uniformMeasureSpec, 0, heightMeasureSpec, 0);
                    lp.height = oldHeight;
                }
            }
        }
    }

    void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int largestChildWidth;
        boolean baselineAligned;
        int childState;
        int weightedMaxHeight;
        int weightedMaxHeight2;
        int largestChildWidth2;
        boolean matchHeightLocally;
        View child;
        LayoutParams lp;
        float totalWeight;
        int i;
        int alternativeMaxHeight;
        int widthSizeAndState;
        boolean maxHeight;
        View child2;
        LayoutParams useLargestChild;
        float childExtra;
        float weightSum;
        boolean matchHeightLocally2;
        float weightSum2;
        int childState2;
        boolean childBaseline;
        int index;
        boolean gravity;
        int i2 = widthMeasureSpec;
        int i3 = heightMeasureSpec;
        this.mTotalLength = 0;
        int count = getVirtualChildCount();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (this.mMaxAscent == null || r7.mMaxDescent == null) {
            r7.mMaxAscent = new int[4];
            r7.mMaxDescent = new int[4];
        }
        int[] maxAscent = r7.mMaxAscent;
        int[] maxDescent = r7.mMaxDescent;
        boolean matchHeight = false;
        maxAscent[3] = -1;
        maxAscent[2] = -1;
        maxAscent[1] = -1;
        maxAscent[0] = -1;
        maxDescent[3] = -1;
        maxDescent[2] = -1;
        maxDescent[1] = -1;
        maxDescent[0] = -1;
        boolean baselineAligned2 = r7.mBaselineAligned;
        boolean skippedMeasure = false;
        boolean useLargestChild2 = r7.mUseLargestChild;
        boolean isExactly = widthMode == 1073741824;
        int childState3 = 0;
        int largestChildWidth3 = 0;
        boolean matchHeight2 = matchHeight;
        matchHeight = true;
        int maxHeight2 = 0;
        float totalWeight2 = 0.0f;
        int i4 = 0;
        int alternativeMaxHeight2 = 0;
        int weightedMaxHeight3 = 0;
        while (i4 < count) {
            int totalLength;
            LayoutParams lp2;
            int maxHeight3;
            int alternativeMaxHeight3;
            View child3 = getVirtualChildAt(i4);
            if (child3 == null) {
                largestChildWidth = largestChildWidth3;
                r7.mTotalLength += measureNullChild(i4);
                baselineAligned = baselineAligned2;
                childState = childState3;
                largestChildWidth3 = largestChildWidth;
                largestChildWidth = widthMode;
            } else {
                largestChildWidth = largestChildWidth3;
                weightedMaxHeight = weightedMaxHeight3;
                if (child3.getVisibility() == 8) {
                    i4 += getChildrenSkipCount(child3, i4);
                    baselineAligned = baselineAligned2;
                    childState = childState3;
                    largestChildWidth3 = largestChildWidth;
                    weightedMaxHeight3 = weightedMaxHeight;
                    largestChildWidth = widthMode;
                } else {
                    LayoutParams lp3;
                    int margin;
                    if (hasDividerBeforeChildAt(i4)) {
                        r7.mTotalLength += r7.mDividerWidth;
                    }
                    LayoutParams lp4 = (LayoutParams) child3.getLayoutParams();
                    float totalWeight3 = totalWeight2 + lp4.weight;
                    int alternativeMaxHeight4;
                    if (widthMode == 1073741824 && lp4.width == 0 && lp4.weight > 0.0f) {
                        if (isExactly) {
                            alternativeMaxHeight4 = alternativeMaxHeight2;
                            r7.mTotalLength += lp4.leftMargin + lp4.rightMargin;
                        } else {
                            alternativeMaxHeight4 = alternativeMaxHeight2;
                            totalLength = r7.mTotalLength;
                            r7.mTotalLength = Math.max(totalLength, (lp4.leftMargin + totalLength) + lp4.rightMargin);
                        }
                        if (baselineAligned2) {
                            largestChildWidth3 = MeasureSpec.makeMeasureSpec(0, 0);
                            child3.measure(largestChildWidth3, largestChildWidth3);
                            lp2 = lp4;
                            maxHeight3 = maxHeight2;
                            i2 = i4;
                            baselineAligned = baselineAligned2;
                            alternativeMaxHeight2 = largestChildWidth;
                            weightedMaxHeight2 = weightedMaxHeight;
                            alternativeMaxHeight3 = alternativeMaxHeight4;
                            largestChildWidth = widthMode;
                            widthMode = -1;
                        } else {
                            skippedMeasure = true;
                            lp2 = lp4;
                            maxHeight3 = maxHeight2;
                            i2 = i4;
                            baselineAligned = baselineAligned2;
                            alternativeMaxHeight2 = largestChildWidth;
                            weightedMaxHeight2 = weightedMaxHeight;
                            alternativeMaxHeight3 = alternativeMaxHeight4;
                            largestChildWidth = widthMode;
                            widthMode = -1;
                        }
                    } else {
                        alternativeMaxHeight4 = alternativeMaxHeight2;
                        if (lp4.width != 0 || lp4.weight <= 0.0f) {
                            alternativeMaxHeight2 = Integer.MIN_VALUE;
                        } else {
                            lp4.width = -2;
                            alternativeMaxHeight2 = 0;
                        }
                        largestChildWidth2 = largestChildWidth;
                        lp3 = lp4;
                        weightedMaxHeight2 = weightedMaxHeight;
                        int oldWidth = alternativeMaxHeight2;
                        alternativeMaxHeight3 = alternativeMaxHeight4;
                        maxHeight3 = maxHeight2;
                        i2 = i4;
                        baselineAligned = baselineAligned2;
                        largestChildWidth = widthMode;
                        widthMode = -1;
                        measureChildBeforeLayout(child3, i4, widthMeasureSpec, totalWeight3 == 0.0f ? r7.mTotalLength : 0, heightMeasureSpec, 0);
                        totalLength = oldWidth;
                        if (totalLength != Integer.MIN_VALUE) {
                            lp2 = lp3;
                            lp2.width = totalLength;
                        } else {
                            lp2 = lp3;
                        }
                        weightedMaxHeight3 = child3.getMeasuredWidth();
                        if (isExactly) {
                            r7.mTotalLength += ((lp2.leftMargin + weightedMaxHeight3) + lp2.rightMargin) + getNextLocationOffset(child3);
                        } else {
                            alternativeMaxHeight2 = r7.mTotalLength;
                            r7.mTotalLength = Math.max(alternativeMaxHeight2, (((alternativeMaxHeight2 + weightedMaxHeight3) + lp2.leftMargin) + lp2.rightMargin) + getNextLocationOffset(child3));
                        }
                        if (useLargestChild2) {
                            alternativeMaxHeight2 = Math.max(weightedMaxHeight3, largestChildWidth2);
                        } else {
                            alternativeMaxHeight2 = largestChildWidth2;
                        }
                    }
                    matchHeightLocally = false;
                    if (heightMode != 1073741824 && lp2.height == widthMode) {
                        matchHeight2 = true;
                        matchHeightLocally = true;
                    }
                    weightedMaxHeight3 = lp2.topMargin + lp2.bottomMargin;
                    maxHeight2 = child3.getMeasuredHeight() + weightedMaxHeight3;
                    i4 = View.combineMeasuredStates(childState3, child3.getMeasuredState());
                    if (baselineAligned) {
                        childState = child3.getBaseline();
                        if (childState != widthMode) {
                            int index2 = ((((lp2.gravity < 0 ? r7.mGravity : lp2.gravity) & 112) >> 4) & -2) >> 1;
                            margin = weightedMaxHeight3;
                            maxAscent[index2] = Math.max(maxAscent[index2], childState);
                            weightedMaxHeight = alternativeMaxHeight2;
                            maxDescent[index2] = Math.max(maxDescent[index2], maxHeight2 - childState);
                        } else {
                            margin = weightedMaxHeight3;
                            weightedMaxHeight = alternativeMaxHeight2;
                        }
                    } else {
                        margin = weightedMaxHeight3;
                        weightedMaxHeight = alternativeMaxHeight2;
                    }
                    weightedMaxHeight3 = Math.max(maxHeight3, maxHeight2);
                    boolean allFillParent = matchHeight && lp2.height == -1;
                    if (lp2.weight > 0.0f) {
                        childState = Math.max(weightedMaxHeight2, matchHeightLocally ? margin : maxHeight2);
                        lp3 = lp2;
                        largestChildWidth3 = alternativeMaxHeight3;
                    } else {
                        widthMode = weightedMaxHeight2;
                        lp3 = lp2;
                        largestChildWidth3 = Math.max(alternativeMaxHeight3, matchHeightLocally ? margin : maxHeight2);
                        childState = widthMode;
                    }
                    maxHeight2 = weightedMaxHeight3;
                    matchHeight = allFillParent;
                    weightedMaxHeight3 = childState;
                    totalWeight2 = totalWeight3;
                    alternativeMaxHeight2 = largestChildWidth3;
                    childState = i4;
                    i4 = i2 + getChildrenSkipCount(child3, i2);
                    largestChildWidth3 = weightedMaxHeight;
                }
            }
            i4++;
            childState3 = childState;
            baselineAligned2 = baselineAligned;
            widthMode = largestChildWidth;
            i2 = widthMeasureSpec;
        }
        i2 = i4;
        baselineAligned = baselineAligned2;
        largestChildWidth = widthMode;
        childState = childState3;
        widthMode = weightedMaxHeight3;
        weightedMaxHeight3 = maxHeight2;
        int i5 = alternativeMaxHeight2;
        alternativeMaxHeight2 = largestChildWidth3;
        largestChildWidth3 = i5;
        if (r7.mTotalLength > 0 && hasDividerBeforeChildAt(count)) {
            r7.mTotalLength += r7.mDividerWidth;
        }
        if (maxAscent[1] == -1 && maxAscent[0] == -1 && maxAscent[2] == -1) {
            if (maxAscent[3] == -1) {
                weightedMaxHeight = childState;
                if (useLargestChild2) {
                    index2 = weightedMaxHeight3;
                    maxHeight2 = largestChildWidth;
                } else {
                    maxHeight2 = largestChildWidth;
                    if (maxHeight2 != Integer.MIN_VALUE) {
                        if (maxHeight2 == 0) {
                            index2 = weightedMaxHeight3;
                        }
                    }
                    r7.mTotalLength = 0;
                    i4 = 0;
                    while (i4 < count) {
                        child = getVirtualChildAt(i4);
                        if (child == null) {
                            r7.mTotalLength += measureNullChild(i4);
                            index2 = weightedMaxHeight3;
                            largestChildWidth = i4;
                        } else if (child.getVisibility() != 8) {
                            index2 = weightedMaxHeight3;
                            largestChildWidth = i4 + getChildrenSkipCount(child, i4);
                        } else {
                            lp = (LayoutParams) child.getLayoutParams();
                            if (isExactly) {
                                index2 = weightedMaxHeight3;
                                largestChildWidth = i4;
                                weightedMaxHeight3 = r7.mTotalLength;
                                r7.mTotalLength = Math.max(weightedMaxHeight3, (((weightedMaxHeight3 + alternativeMaxHeight2) + lp.leftMargin) + lp.rightMargin) + getNextLocationOffset(child));
                            } else {
                                index2 = weightedMaxHeight3;
                                largestChildWidth = i4;
                                r7.mTotalLength += ((lp.leftMargin + alternativeMaxHeight2) + lp.rightMargin) + getNextLocationOffset(child);
                            }
                        }
                        i4 = largestChildWidth + 1;
                        weightedMaxHeight3 = index2;
                    }
                    index2 = weightedMaxHeight3;
                    largestChildWidth = i4;
                }
                r7.mTotalLength += getPaddingLeft() + getPaddingRight();
                i2 = View.resolveSizeAndState(Math.max(r7.mTotalLength, getSuggestedMinimumWidth()), widthMeasureSpec, 0);
                weightedMaxHeight3 = i2 & ViewCompat.MEASURED_SIZE_MASK;
                childState = weightedMaxHeight3 - r7.mTotalLength;
                if (!skippedMeasure) {
                    totalWeight = totalWeight2;
                    i = weightedMaxHeight3;
                    largestChildWidth2 = alternativeMaxHeight2;
                    weightedMaxHeight3 = largestChildWidth3;
                } else if (childState != 0 || totalWeight2 <= 0.0f) {
                    largestChildWidth3 = Math.max(largestChildWidth3, widthMode);
                    if (useLargestChild2 || maxHeight2 == 1073741824) {
                        alternativeMaxHeight = largestChildWidth3;
                        i = weightedMaxHeight3;
                        largestChildWidth2 = alternativeMaxHeight2;
                    } else {
                        i3 = 0;
                        while (i3 < count) {
                            totalWeight = totalWeight2;
                            totalWeight2 = getVirtualChildAt(i3);
                            if (totalWeight2 != null) {
                                alternativeMaxHeight = largestChildWidth3;
                                i = weightedMaxHeight3;
                                if (totalWeight2.getVisibility() == 8) {
                                    largestChildWidth2 = alternativeMaxHeight2;
                                } else {
                                    lp2 = (LayoutParams) totalWeight2.getLayoutParams();
                                    weightedMaxHeight3 = lp2.weight;
                                    LayoutParams lp5;
                                    int childExtra2;
                                    if (weightedMaxHeight3 > 0) {
                                        lp5 = lp2;
                                        childExtra2 = weightedMaxHeight3;
                                        largestChildWidth2 = alternativeMaxHeight2;
                                        totalWeight2.measure(MeasureSpec.makeMeasureSpec(alternativeMaxHeight2, 1073741824), MeasureSpec.makeMeasureSpec(totalWeight2.getMeasuredHeight(), 1073741824));
                                    } else {
                                        lp5 = lp2;
                                        childExtra2 = weightedMaxHeight3;
                                        largestChildWidth2 = alternativeMaxHeight2;
                                    }
                                }
                            } else {
                                alternativeMaxHeight = largestChildWidth3;
                                i = weightedMaxHeight3;
                                largestChildWidth2 = alternativeMaxHeight2;
                            }
                            i3++;
                            largestChildWidth3 = alternativeMaxHeight;
                            totalWeight2 = totalWeight;
                            weightedMaxHeight3 = i;
                            alternativeMaxHeight2 = largestChildWidth2;
                        }
                        alternativeMaxHeight = largestChildWidth3;
                        i = weightedMaxHeight3;
                        largestChildWidth2 = alternativeMaxHeight2;
                    }
                    oldWidth = maxHeight2;
                    maxHeight2 = childState;
                    widthSizeAndState = i2;
                    weightedMaxHeight2 = widthMode;
                    alternativeMaxHeight2 = index2;
                    weightedMaxHeight3 = weightedMaxHeight;
                    childState = heightMeasureSpec;
                    weightedMaxHeight = count;
                    maxHeight = useLargestChild2;
                    if (!(matchHeight || heightMode == 1073741824)) {
                        alternativeMaxHeight2 = alternativeMaxHeight;
                    }
                    setMeasuredDimension(widthSizeAndState | (ViewCompat.MEASURED_STATE_MASK & weightedMaxHeight3), View.resolveSizeAndState(Math.max(alternativeMaxHeight2 + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight()), childState, weightedMaxHeight3 << 16));
                    if (matchHeight2) {
                        largestChildWidth3 = widthMeasureSpec;
                        return;
                    }
                    forceUniformHeight(weightedMaxHeight, widthMeasureSpec);
                } else {
                    totalWeight = totalWeight2;
                    i = weightedMaxHeight3;
                    largestChildWidth2 = alternativeMaxHeight2;
                    weightedMaxHeight3 = largestChildWidth3;
                }
                totalWeight2 = r7.mWeightSum;
                if (totalWeight2 > 0.0f) {
                    totalWeight2 = totalWeight;
                }
                maxAscent[3] = -1;
                maxAscent[2] = -1;
                maxAscent[1] = -1;
                maxAscent[0] = -1;
                maxDescent[3] = -1;
                maxDescent[2] = -1;
                maxDescent[1] = -1;
                maxDescent[0] = -1;
                alternativeMaxHeight2 = -1;
                r7.mTotalLength = 0;
                i3 = 0;
                largestChildWidth3 = weightedMaxHeight3;
                weightedMaxHeight3 = weightedMaxHeight;
                while (i3 < count) {
                    weightedMaxHeight2 = widthMode;
                    child2 = getVirtualChildAt(i3);
                    if (child2 == null) {
                        maxHeight = useLargestChild2;
                        if (child2.getVisibility() != 8) {
                            oldWidth = maxHeight2;
                            maxHeight2 = childState;
                            widthSizeAndState = i2;
                            weightedMaxHeight = count;
                            childState = heightMeasureSpec;
                        } else {
                            useLargestChild = (LayoutParams) child2.getLayoutParams();
                            childExtra = useLargestChild.weight;
                            if (childExtra <= 0.0f) {
                                weightedMaxHeight = count;
                                count = (int) ((((float) childState) * childExtra) / totalWeight2);
                                weightSum = totalWeight2 - childExtra;
                                alternativeMaxHeight3 = childState - count;
                                widthSizeAndState = i2;
                                totalLength = getChildMeasureSpec(heightMeasureSpec, ((getPaddingTop() + getPaddingBottom()) + useLargestChild.topMargin) + useLargestChild.bottomMargin, useLargestChild.height);
                                if (useLargestChild.width == 0) {
                                    if (maxHeight2 != 1073741824) {
                                        child2.measure(MeasureSpec.makeMeasureSpec(count <= 0 ? count : 0, 1073741824), totalLength);
                                        oldWidth = maxHeight2;
                                        weightedMaxHeight3 = View.combineMeasuredStates(weightedMaxHeight3, child2.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                                        totalWeight2 = weightSum;
                                        maxHeight2 = alternativeMaxHeight3;
                                    }
                                }
                                i4 = child2.getMeasuredWidth() + count;
                                if (i4 < 0) {
                                    i4 = 0;
                                }
                                oldWidth = maxHeight2;
                                child2.measure(MeasureSpec.makeMeasureSpec(i4, 1073741824), totalLength);
                                weightedMaxHeight3 = View.combineMeasuredStates(weightedMaxHeight3, child2.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                                totalWeight2 = weightSum;
                                maxHeight2 = alternativeMaxHeight3;
                            } else {
                                oldWidth = maxHeight2;
                                float f = childExtra;
                                maxHeight2 = childState;
                                widthSizeAndState = i2;
                                weightedMaxHeight = count;
                                childState = heightMeasureSpec;
                            }
                            if (isExactly) {
                                i4 = r7.mTotalLength;
                                r7.mTotalLength = Math.max(i4, (((child2.getMeasuredWidth() + i4) + useLargestChild.leftMargin) + useLargestChild.rightMargin) + getNextLocationOffset(child2));
                            } else {
                                r7.mTotalLength += ((child2.getMeasuredWidth() + useLargestChild.leftMargin) + useLargestChild.rightMargin) + getNextLocationOffset(child2);
                            }
                            matchHeightLocally2 = heightMode == 1073741824 && useLargestChild.height == -1;
                            i2 = useLargestChild.topMargin + useLargestChild.bottomMargin;
                            count = child2.getMeasuredHeight() + i2;
                            alternativeMaxHeight2 = Math.max(alternativeMaxHeight2, count);
                            if (matchHeightLocally2) {
                                weightSum2 = totalWeight2;
                                totalLength = count;
                            } else {
                                weightSum2 = totalWeight2;
                                totalLength = i2;
                            }
                            totalLength = Math.max(largestChildWidth3, totalLength);
                            if (matchHeight) {
                                maxHeight3 = totalLength;
                            } else {
                                maxHeight3 = totalLength;
                                if (useLargestChild.height == -1) {
                                    matchHeightLocally = true;
                                    if (baselineAligned) {
                                        matchHeight = matchHeightLocally;
                                        childState2 = weightedMaxHeight3;
                                    } else {
                                        childBaseline = child2.getBaseline();
                                        matchHeight = matchHeightLocally;
                                        if (childBaseline) {
                                            matchHeightLocally = (useLargestChild.gravity < false ? r7.mGravity : useLargestChild.gravity) & 112;
                                            index = ((matchHeightLocally >> 4) & -2) >> 1;
                                            gravity = matchHeightLocally;
                                            maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                                            childState2 = weightedMaxHeight3;
                                            maxDescent[index] = Math.max(maxDescent[index], count - childBaseline);
                                        } else {
                                            childState2 = weightedMaxHeight3;
                                        }
                                    }
                                    totalWeight2 = weightSum2;
                                    largestChildWidth3 = maxHeight3;
                                    weightedMaxHeight3 = childState2;
                                }
                            }
                            matchHeightLocally = false;
                            if (baselineAligned) {
                                matchHeight = matchHeightLocally;
                                childState2 = weightedMaxHeight3;
                            } else {
                                childBaseline = child2.getBaseline();
                                matchHeight = matchHeightLocally;
                                if (childBaseline) {
                                    childState2 = weightedMaxHeight3;
                                } else {
                                    if (useLargestChild.gravity < false) {
                                    }
                                    matchHeightLocally = (useLargestChild.gravity < false ? r7.mGravity : useLargestChild.gravity) & 112;
                                    index = ((matchHeightLocally >> 4) & -2) >> 1;
                                    gravity = matchHeightLocally;
                                    maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                                    childState2 = weightedMaxHeight3;
                                    maxDescent[index] = Math.max(maxDescent[index], count - childBaseline);
                                }
                            }
                            totalWeight2 = weightSum2;
                            largestChildWidth3 = maxHeight3;
                            weightedMaxHeight3 = childState2;
                        }
                    } else {
                        oldWidth = maxHeight2;
                        maxHeight2 = childState;
                        widthSizeAndState = i2;
                        weightedMaxHeight = count;
                        maxHeight = useLargestChild2;
                        childState = heightMeasureSpec;
                    }
                    i3++;
                    childState = maxHeight2;
                    i2 = widthSizeAndState;
                    useLargestChild2 = maxHeight;
                    count = weightedMaxHeight;
                    widthMode = weightedMaxHeight2;
                    maxHeight2 = oldWidth;
                    i4 = widthMeasureSpec;
                }
                widthSizeAndState = i2;
                weightedMaxHeight = count;
                weightedMaxHeight2 = widthMode;
                maxHeight = useLargestChild2;
                childState = heightMeasureSpec;
                r7.mTotalLength += getPaddingLeft() + getPaddingRight();
                if (!(maxAscent[1] == -1 && maxAscent[0] == -1 && maxAscent[2] == -1 && maxAscent[3] == -1)) {
                    alternativeMaxHeight2 = Math.max(alternativeMaxHeight2, Math.max(maxAscent[3], Math.max(maxAscent[0], Math.max(maxAscent[1], maxAscent[2]))) + Math.max(maxDescent[3], Math.max(maxDescent[0], Math.max(maxDescent[1], maxDescent[2]))));
                }
                alternativeMaxHeight = largestChildWidth3;
                alternativeMaxHeight2 = alternativeMaxHeight;
                setMeasuredDimension(widthSizeAndState | (ViewCompat.MEASURED_STATE_MASK & weightedMaxHeight3), View.resolveSizeAndState(Math.max(alternativeMaxHeight2 + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight()), childState, weightedMaxHeight3 << 16));
                if (matchHeight2) {
                    largestChildWidth3 = widthMeasureSpec;
                    return;
                }
                forceUniformHeight(weightedMaxHeight, widthMeasureSpec);
            }
        }
        weightedMaxHeight = childState;
        weightedMaxHeight3 = Math.max(weightedMaxHeight3, Math.max(maxAscent[3], Math.max(maxAscent[0], Math.max(maxAscent[1], maxAscent[2]))) + Math.max(maxDescent[3], Math.max(maxDescent[0], Math.max(maxDescent[1], maxDescent[2]))));
        if (useLargestChild2) {
            index2 = weightedMaxHeight3;
            maxHeight2 = largestChildWidth;
        } else {
            maxHeight2 = largestChildWidth;
            if (maxHeight2 != Integer.MIN_VALUE) {
                if (maxHeight2 == 0) {
                    index2 = weightedMaxHeight3;
                }
            }
            r7.mTotalLength = 0;
            i4 = 0;
            while (i4 < count) {
                child = getVirtualChildAt(i4);
                if (child == null) {
                    r7.mTotalLength += measureNullChild(i4);
                    index2 = weightedMaxHeight3;
                    largestChildWidth = i4;
                } else if (child.getVisibility() != 8) {
                    lp = (LayoutParams) child.getLayoutParams();
                    if (isExactly) {
                        index2 = weightedMaxHeight3;
                        largestChildWidth = i4;
                        weightedMaxHeight3 = r7.mTotalLength;
                        r7.mTotalLength = Math.max(weightedMaxHeight3, (((weightedMaxHeight3 + alternativeMaxHeight2) + lp.leftMargin) + lp.rightMargin) + getNextLocationOffset(child));
                    } else {
                        index2 = weightedMaxHeight3;
                        largestChildWidth = i4;
                        r7.mTotalLength += ((lp.leftMargin + alternativeMaxHeight2) + lp.rightMargin) + getNextLocationOffset(child);
                    }
                } else {
                    index2 = weightedMaxHeight3;
                    largestChildWidth = i4 + getChildrenSkipCount(child, i4);
                }
                i4 = largestChildWidth + 1;
                weightedMaxHeight3 = index2;
            }
            index2 = weightedMaxHeight3;
            largestChildWidth = i4;
        }
        r7.mTotalLength += getPaddingLeft() + getPaddingRight();
        i2 = View.resolveSizeAndState(Math.max(r7.mTotalLength, getSuggestedMinimumWidth()), widthMeasureSpec, 0);
        weightedMaxHeight3 = i2 & ViewCompat.MEASURED_SIZE_MASK;
        childState = weightedMaxHeight3 - r7.mTotalLength;
        if (!skippedMeasure) {
            totalWeight = totalWeight2;
            i = weightedMaxHeight3;
            largestChildWidth2 = alternativeMaxHeight2;
            weightedMaxHeight3 = largestChildWidth3;
        } else {
            if (childState != 0) {
            }
            largestChildWidth3 = Math.max(largestChildWidth3, widthMode);
            if (useLargestChild2) {
            }
            alternativeMaxHeight = largestChildWidth3;
            i = weightedMaxHeight3;
            largestChildWidth2 = alternativeMaxHeight2;
            oldWidth = maxHeight2;
            maxHeight2 = childState;
            widthSizeAndState = i2;
            weightedMaxHeight2 = widthMode;
            alternativeMaxHeight2 = index2;
            weightedMaxHeight3 = weightedMaxHeight;
            childState = heightMeasureSpec;
            weightedMaxHeight = count;
            maxHeight = useLargestChild2;
            alternativeMaxHeight2 = alternativeMaxHeight;
            setMeasuredDimension(widthSizeAndState | (ViewCompat.MEASURED_STATE_MASK & weightedMaxHeight3), View.resolveSizeAndState(Math.max(alternativeMaxHeight2 + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight()), childState, weightedMaxHeight3 << 16));
            if (matchHeight2) {
                forceUniformHeight(weightedMaxHeight, widthMeasureSpec);
            }
            largestChildWidth3 = widthMeasureSpec;
            return;
        }
        totalWeight2 = r7.mWeightSum;
        if (totalWeight2 > 0.0f) {
            totalWeight2 = totalWeight;
        }
        maxAscent[3] = -1;
        maxAscent[2] = -1;
        maxAscent[1] = -1;
        maxAscent[0] = -1;
        maxDescent[3] = -1;
        maxDescent[2] = -1;
        maxDescent[1] = -1;
        maxDescent[0] = -1;
        alternativeMaxHeight2 = -1;
        r7.mTotalLength = 0;
        i3 = 0;
        largestChildWidth3 = weightedMaxHeight3;
        weightedMaxHeight3 = weightedMaxHeight;
        while (i3 < count) {
            weightedMaxHeight2 = widthMode;
            child2 = getVirtualChildAt(i3);
            if (child2 == null) {
                oldWidth = maxHeight2;
                maxHeight2 = childState;
                widthSizeAndState = i2;
                weightedMaxHeight = count;
                maxHeight = useLargestChild2;
                childState = heightMeasureSpec;
            } else {
                maxHeight = useLargestChild2;
                if (child2.getVisibility() != 8) {
                    useLargestChild = (LayoutParams) child2.getLayoutParams();
                    childExtra = useLargestChild.weight;
                    if (childExtra <= 0.0f) {
                        oldWidth = maxHeight2;
                        float f2 = childExtra;
                        maxHeight2 = childState;
                        widthSizeAndState = i2;
                        weightedMaxHeight = count;
                        childState = heightMeasureSpec;
                    } else {
                        weightedMaxHeight = count;
                        count = (int) ((((float) childState) * childExtra) / totalWeight2);
                        weightSum = totalWeight2 - childExtra;
                        alternativeMaxHeight3 = childState - count;
                        widthSizeAndState = i2;
                        totalLength = getChildMeasureSpec(heightMeasureSpec, ((getPaddingTop() + getPaddingBottom()) + useLargestChild.topMargin) + useLargestChild.bottomMargin, useLargestChild.height);
                        if (useLargestChild.width == 0) {
                            if (maxHeight2 != 1073741824) {
                                if (count <= 0) {
                                }
                                child2.measure(MeasureSpec.makeMeasureSpec(count <= 0 ? count : 0, 1073741824), totalLength);
                                oldWidth = maxHeight2;
                                weightedMaxHeight3 = View.combineMeasuredStates(weightedMaxHeight3, child2.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                                totalWeight2 = weightSum;
                                maxHeight2 = alternativeMaxHeight3;
                            }
                        }
                        i4 = child2.getMeasuredWidth() + count;
                        if (i4 < 0) {
                            i4 = 0;
                        }
                        oldWidth = maxHeight2;
                        child2.measure(MeasureSpec.makeMeasureSpec(i4, 1073741824), totalLength);
                        weightedMaxHeight3 = View.combineMeasuredStates(weightedMaxHeight3, child2.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                        totalWeight2 = weightSum;
                        maxHeight2 = alternativeMaxHeight3;
                    }
                    if (isExactly) {
                        i4 = r7.mTotalLength;
                        r7.mTotalLength = Math.max(i4, (((child2.getMeasuredWidth() + i4) + useLargestChild.leftMargin) + useLargestChild.rightMargin) + getNextLocationOffset(child2));
                    } else {
                        r7.mTotalLength += ((child2.getMeasuredWidth() + useLargestChild.leftMargin) + useLargestChild.rightMargin) + getNextLocationOffset(child2);
                    }
                    if (heightMode == 1073741824) {
                    }
                    i2 = useLargestChild.topMargin + useLargestChild.bottomMargin;
                    count = child2.getMeasuredHeight() + i2;
                    alternativeMaxHeight2 = Math.max(alternativeMaxHeight2, count);
                    if (matchHeightLocally2) {
                        weightSum2 = totalWeight2;
                        totalLength = count;
                    } else {
                        weightSum2 = totalWeight2;
                        totalLength = i2;
                    }
                    totalLength = Math.max(largestChildWidth3, totalLength);
                    if (matchHeight) {
                        maxHeight3 = totalLength;
                    } else {
                        maxHeight3 = totalLength;
                        if (useLargestChild.height == -1) {
                            matchHeightLocally = true;
                            if (baselineAligned) {
                                childBaseline = child2.getBaseline();
                                matchHeight = matchHeightLocally;
                                if (childBaseline) {
                                    if (useLargestChild.gravity < false) {
                                    }
                                    matchHeightLocally = (useLargestChild.gravity < false ? r7.mGravity : useLargestChild.gravity) & 112;
                                    index = ((matchHeightLocally >> 4) & -2) >> 1;
                                    gravity = matchHeightLocally;
                                    maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                                    childState2 = weightedMaxHeight3;
                                    maxDescent[index] = Math.max(maxDescent[index], count - childBaseline);
                                } else {
                                    childState2 = weightedMaxHeight3;
                                }
                            } else {
                                matchHeight = matchHeightLocally;
                                childState2 = weightedMaxHeight3;
                            }
                            totalWeight2 = weightSum2;
                            largestChildWidth3 = maxHeight3;
                            weightedMaxHeight3 = childState2;
                        }
                    }
                    matchHeightLocally = false;
                    if (baselineAligned) {
                        matchHeight = matchHeightLocally;
                        childState2 = weightedMaxHeight3;
                    } else {
                        childBaseline = child2.getBaseline();
                        matchHeight = matchHeightLocally;
                        if (childBaseline) {
                            childState2 = weightedMaxHeight3;
                        } else {
                            if (useLargestChild.gravity < false) {
                            }
                            matchHeightLocally = (useLargestChild.gravity < false ? r7.mGravity : useLargestChild.gravity) & 112;
                            index = ((matchHeightLocally >> 4) & -2) >> 1;
                            gravity = matchHeightLocally;
                            maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                            childState2 = weightedMaxHeight3;
                            maxDescent[index] = Math.max(maxDescent[index], count - childBaseline);
                        }
                    }
                    totalWeight2 = weightSum2;
                    largestChildWidth3 = maxHeight3;
                    weightedMaxHeight3 = childState2;
                } else {
                    oldWidth = maxHeight2;
                    maxHeight2 = childState;
                    widthSizeAndState = i2;
                    weightedMaxHeight = count;
                    childState = heightMeasureSpec;
                }
            }
            i3++;
            childState = maxHeight2;
            i2 = widthSizeAndState;
            useLargestChild2 = maxHeight;
            count = weightedMaxHeight;
            widthMode = weightedMaxHeight2;
            maxHeight2 = oldWidth;
            i4 = widthMeasureSpec;
        }
        widthSizeAndState = i2;
        weightedMaxHeight = count;
        weightedMaxHeight2 = widthMode;
        maxHeight = useLargestChild2;
        childState = heightMeasureSpec;
        r7.mTotalLength += getPaddingLeft() + getPaddingRight();
        alternativeMaxHeight2 = Math.max(alternativeMaxHeight2, Math.max(maxAscent[3], Math.max(maxAscent[0], Math.max(maxAscent[1], maxAscent[2]))) + Math.max(maxDescent[3], Math.max(maxDescent[0], Math.max(maxDescent[1], maxDescent[2]))));
        alternativeMaxHeight = largestChildWidth3;
        alternativeMaxHeight2 = alternativeMaxHeight;
        setMeasuredDimension(widthSizeAndState | (ViewCompat.MEASURED_STATE_MASK & weightedMaxHeight3), View.resolveSizeAndState(Math.max(alternativeMaxHeight2 + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight()), childState, weightedMaxHeight3 << 16));
        if (matchHeight2) {
            largestChildWidth3 = widthMeasureSpec;
            return;
        }
        forceUniformHeight(weightedMaxHeight, widthMeasureSpec);
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.height == -1) {
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    int getChildrenSkipCount(View child, int index) {
        return 0;
    }

    int measureNullChild(int childIndex) {
        return 0;
    }

    void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        measureChildWithMargins(child, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    int getLocationOffset(View child) {
        return 0;
    }

    int getNextLocationOffset(View child) {
        return 0;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.mOrientation == 1) {
            layoutVertical(l, t, r, b);
        } else {
            layoutHorizontal(l, t, r, b);
        }
    }

    void layoutVertical(int left, int top, int right, int bottom) {
        int paddingLeft = getPaddingLeft();
        int width = right - left;
        int childRight = width - getPaddingRight();
        int childSpace = (width - paddingLeft) - getPaddingRight();
        int count = getVirtualChildCount();
        int i = this.mGravity;
        int majorGravity = i & 112;
        int minorGravity = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (majorGravity == 16) {
            i = getPaddingTop() + (((bottom - top) - r6.mTotalLength) / 2);
        } else if (majorGravity != 80) {
            i = getPaddingTop();
        } else {
            i = ((getPaddingTop() + bottom) - top) - r6.mTotalLength;
        }
        int i2 = 0;
        while (i2 < count) {
            int paddingLeft2;
            View child = getVirtualChildAt(i2);
            if (child == null) {
                i += measureNullChild(i2);
                paddingLeft2 = paddingLeft;
            } else if (child.getVisibility() != 8) {
                int gravity;
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int gravity2 = lp.gravity;
                if (gravity2 < 0) {
                    gravity = minorGravity;
                } else {
                    gravity = gravity2;
                }
                int layoutDirection = ViewCompat.getLayoutDirection(this);
                gravity2 = GravityCompat.getAbsoluteGravity(gravity, layoutDirection) & 7;
                int childLeft = gravity2 != 1 ? gravity2 != 5 ? lp.leftMargin + paddingLeft : (childRight - childWidth) - lp.rightMargin : ((((childSpace - childWidth) / 2) + paddingLeft) + lp.leftMargin) - lp.rightMargin;
                if (hasDividerBeforeChildAt(i2)) {
                    i += r6.mDividerHeight;
                }
                int childTop = i + lp.topMargin;
                paddingLeft2 = paddingLeft;
                paddingLeft = lp;
                setChildFrame(child, childLeft, childTop + getLocationOffset(child), childWidth, childHeight);
                i2 += getChildrenSkipCount(child, i2);
                i = childTop + ((childHeight + paddingLeft.bottomMargin) + getNextLocationOffset(child));
            } else {
                paddingLeft2 = paddingLeft;
            }
            i2++;
            paddingLeft = paddingLeft2;
        }
    }

    void layoutHorizontal(int left, int top, int right, int bottom) {
        int start;
        int dir;
        boolean isLayoutRtl;
        int layoutDirection;
        int[] maxDescent;
        int[] maxAscent;
        int paddingTop;
        int height;
        int count;
        int i;
        boolean isLayoutRtl2 = ViewUtils.isLayoutRtl(this);
        int paddingTop2 = getPaddingTop();
        int height2 = bottom - top;
        int childBottom = height2 - getPaddingBottom();
        int childSpace = (height2 - paddingTop2) - getPaddingBottom();
        int count2 = getVirtualChildCount();
        int i2 = this.mGravity;
        int majorGravity = i2 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int minorGravity = i2 & 112;
        boolean baselineAligned = this.mBaselineAligned;
        int[] maxAscent2 = this.mMaxAscent;
        int[] maxDescent2 = this.mMaxDescent;
        int layoutDirection2 = ViewCompat.getLayoutDirection(this);
        i2 = GravityCompat.getAbsoluteGravity(majorGravity, layoutDirection2);
        if (i2 == 1) {
            i2 = getPaddingLeft() + (((right - left) - r6.mTotalLength) / 2);
        } else if (i2 != 5) {
            i2 = getPaddingLeft();
        } else {
            i2 = ((getPaddingLeft() + right) - left) - r6.mTotalLength;
        }
        if (isLayoutRtl2) {
            start = count2 - 1;
            dir = -1;
        } else {
            start = 0;
            dir = 1;
        }
        int i3 = 0;
        while (i3 < count2) {
            int childIndex = start + (dir * i3);
            isLayoutRtl = isLayoutRtl2;
            View child = getVirtualChildAt(childIndex);
            if (child == null) {
                i2 += measureNullChild(childIndex);
                layoutDirection = layoutDirection2;
                maxDescent = maxDescent2;
                maxAscent = maxAscent2;
                paddingTop = paddingTop2;
                height = height2;
                count = count2;
            } else {
                int i4 = i3;
                layoutDirection = layoutDirection2;
                if (child.getVisibility() != 8) {
                    int gravity;
                    int childWidth = child.getMeasuredWidth();
                    int childHeight = child.getMeasuredHeight();
                    LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    int childBaseline = -1;
                    if (baselineAligned) {
                        height = height2;
                        if (lp.height != -1) {
                            height2 = child.getBaseline();
                            i3 = lp.gravity;
                            if (i3 >= 0) {
                                gravity = minorGravity;
                            } else {
                                gravity = i3;
                            }
                            i3 = gravity & 112;
                            count = count2;
                            if (i3 != 16) {
                                count2 = ((((childSpace - childHeight) / 2) + paddingTop2) + lp.topMargin) - lp.bottomMargin;
                            } else if (i3 != 48) {
                                i3 = lp.topMargin + paddingTop2;
                                count2 = height2 == -1 ? i3 + (maxAscent2[1] - height2) : i3;
                            } else if (i3 == 80) {
                                count2 = paddingTop2;
                            } else {
                                i3 = (childBottom - childHeight) - lp.bottomMargin;
                                count2 = height2 == -1 ? i3 - (maxDescent2[2] - (child.getMeasuredHeight() - height2)) : i3;
                            }
                            if (hasDividerBeforeChildAt(childIndex)) {
                                i2 += r6.mDividerWidth;
                            }
                            childBaseline = i2 + lp.leftMargin;
                            paddingTop = paddingTop2;
                            paddingTop2 = childIndex;
                            i = i4;
                            height2 = lp;
                            maxDescent = maxDescent2;
                            maxAscent = maxAscent2;
                            setChildFrame(child, childBaseline + getLocationOffset(child), count2, childWidth, childHeight);
                            i3 = i + getChildrenSkipCount(child, paddingTop2);
                            i2 = childBaseline + ((childWidth + height2.rightMargin) + getNextLocationOffset(child));
                        }
                    } else {
                        height = height2;
                    }
                    height2 = childBaseline;
                    i3 = lp.gravity;
                    if (i3 >= 0) {
                        gravity = i3;
                    } else {
                        gravity = minorGravity;
                    }
                    i3 = gravity & 112;
                    count = count2;
                    if (i3 != 16) {
                        count2 = ((((childSpace - childHeight) / 2) + paddingTop2) + lp.topMargin) - lp.bottomMargin;
                    } else if (i3 != 48) {
                        i3 = lp.topMargin + paddingTop2;
                        if (height2 == -1) {
                        }
                    } else if (i3 == 80) {
                        i3 = (childBottom - childHeight) - lp.bottomMargin;
                        if (height2 == -1) {
                        }
                    } else {
                        count2 = paddingTop2;
                    }
                    if (hasDividerBeforeChildAt(childIndex)) {
                        i2 += r6.mDividerWidth;
                    }
                    childBaseline = i2 + lp.leftMargin;
                    paddingTop = paddingTop2;
                    paddingTop2 = childIndex;
                    i = i4;
                    height2 = lp;
                    maxDescent = maxDescent2;
                    maxAscent = maxAscent2;
                    setChildFrame(child, childBaseline + getLocationOffset(child), count2, childWidth, childHeight);
                    i3 = i + getChildrenSkipCount(child, paddingTop2);
                    i2 = childBaseline + ((childWidth + height2.rightMargin) + getNextLocationOffset(child));
                } else {
                    maxDescent = maxDescent2;
                    maxAscent = maxAscent2;
                    paddingTop = paddingTop2;
                    height = height2;
                    count = count2;
                    i3 = i4;
                }
            }
            i3++;
            isLayoutRtl2 = isLayoutRtl;
            layoutDirection2 = layoutDirection;
            height2 = height;
            count2 = count;
            paddingTop2 = paddingTop;
            maxDescent2 = maxDescent;
            maxAscent2 = maxAscent;
        }
        i = i3;
        layoutDirection = layoutDirection2;
        maxDescent = maxDescent2;
        maxAscent = maxAscent2;
        isLayoutRtl = isLayoutRtl2;
        paddingTop = paddingTop2;
        height = height2;
        count = count2;
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    public void setOrientation(int orientation) {
        if (this.mOrientation != orientation) {
            this.mOrientation = orientation;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK & gravity) == 0) {
                gravity |= GravityCompat.START;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mGravity = gravity;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int horizontalGravity) {
        int gravity = horizontalGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i = this.mGravity;
        if ((GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK & i) != gravity) {
            this.mGravity = (-8388616 & i) | gravity;
            requestLayout();
        }
    }

    public void setVerticalGravity(int verticalGravity) {
        int gravity = verticalGravity & 112;
        int i = this.mGravity;
        if ((i & 112) != gravity) {
            this.mGravity = (i & -113) | gravity;
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    protected LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(LinearLayoutCompat.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(LinearLayoutCompat.class.getName());
    }
}
