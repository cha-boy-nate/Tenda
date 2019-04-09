package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0185R;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

@RestrictTo({Scope.LIBRARY_GROUP})
public class AlertDialogLayout extends LinearLayoutCompat {
    public AlertDialogLayout(@Nullable Context context) {
        super(context);
    }

    public AlertDialogLayout(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!tryOnMeasure(widthMeasureSpec, heightMeasureSpec)) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private boolean tryOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int id;
        int heightToGive;
        int heightToGive2;
        View middlePanel;
        AlertDialogLayout alertDialogLayout = this;
        int i2 = widthMeasureSpec;
        int i3 = heightMeasureSpec;
        View topPanel = null;
        View buttonPanel = null;
        View middlePanel2 = null;
        int count = getChildCount();
        for (i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                id = child.getId();
                if (id == C0185R.id.topPanel) {
                    topPanel = child;
                } else if (id == C0185R.id.buttonPanel) {
                    buttonPanel = child;
                } else {
                    if (id != C0185R.id.contentPanel) {
                        if (id != C0185R.id.customPanel) {
                            return false;
                        }
                    }
                    if (middlePanel2 != null) {
                        return false;
                    }
                    middlePanel2 = child;
                }
            }
        }
        i = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int childState = 0;
        int usedHeight = getPaddingTop() + getPaddingBottom();
        if (topPanel != null) {
            topPanel.measure(i2, 0);
            usedHeight += topPanel.getMeasuredHeight();
            childState = View.combineMeasuredStates(0, topPanel.getMeasuredState());
        }
        int buttonHeight = 0;
        int buttonWantsHeight = 0;
        if (buttonPanel != null) {
            buttonPanel.measure(i2, 0);
            buttonHeight = resolveMinimumHeight(buttonPanel);
            buttonWantsHeight = buttonPanel.getMeasuredHeight() - buttonHeight;
            usedHeight += buttonHeight;
            childState = View.combineMeasuredStates(childState, buttonPanel.getMeasuredState());
        }
        id = 0;
        if (middlePanel2 != null) {
            if (i == 0) {
                View view = topPanel;
                topPanel = null;
            } else {
                topPanel = MeasureSpec.makeMeasureSpec(Math.max(0, heightSize - usedHeight), i);
            }
            middlePanel2.measure(i2, topPanel);
            id = middlePanel2.getMeasuredHeight();
            usedHeight += id;
            childState = View.combineMeasuredStates(childState, middlePanel2.getMeasuredState());
        }
        int remainingHeight = heightSize - usedHeight;
        if (buttonPanel != null) {
            usedHeight -= buttonHeight;
            heightToGive = Math.min(remainingHeight, buttonWantsHeight);
            if (heightToGive > 0) {
                remainingHeight -= heightToGive;
                buttonHeight += heightToGive;
            }
            int remainingHeight2 = remainingHeight;
            buttonPanel.measure(i2, MeasureSpec.makeMeasureSpec(buttonHeight, 1073741824));
            usedHeight += buttonPanel.getMeasuredHeight();
            childState = View.combineMeasuredStates(childState, buttonPanel.getMeasuredState());
            remainingHeight = remainingHeight2;
        }
        if (middlePanel2 != null && remainingHeight > 0) {
            usedHeight -= id;
            heightToGive2 = remainingHeight;
            heightToGive = remainingHeight - heightToGive2;
            remainingHeight = MeasureSpec.makeMeasureSpec(id + heightToGive2, i);
            middlePanel2.measure(i2, remainingHeight);
            usedHeight += middlePanel2.getMeasuredHeight();
            childState = View.combineMeasuredStates(childState, middlePanel2.getMeasuredState());
            remainingHeight = heightToGive;
        }
        heightToGive = remainingHeight;
        remainingHeight = 0;
        heightToGive2 = 0;
        while (heightToGive2 < count) {
            View child2 = getChildAt(heightToGive2);
            View buttonPanel2 = buttonPanel;
            middlePanel = middlePanel2;
            if (child2.getVisibility() != 8) {
                remainingHeight = Math.max(remainingHeight, child2.getMeasuredWidth());
            }
            heightToGive2++;
            buttonPanel = buttonPanel2;
            middlePanel2 = middlePanel;
        }
        middlePanel = middlePanel2;
        setMeasuredDimension(View.resolveSizeAndState(remainingHeight + (getPaddingLeft() + getPaddingRight()), i2, childState), View.resolveSizeAndState(usedHeight, i3, 0));
        if (widthMode != 1073741824) {
            forceUniformWidth(count, i3);
        }
        return true;
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
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

    private static int resolveMinimumHeight(View v) {
        int minHeight = ViewCompat.getMinimumHeight(v);
        if (minHeight > 0) {
            return minHeight;
        }
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            if (vg.getChildCount() == 1) {
                return resolveMinimumHeight(vg.getChildAt(0));
            }
        }
        return 0;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int paddingTop;
        int i;
        AlertDialogLayout alertDialogLayout = this;
        int paddingLeft = getPaddingLeft();
        int width = right - left;
        int childRight = width - getPaddingRight();
        int childSpace = (width - paddingLeft) - getPaddingRight();
        int totalLength = getMeasuredHeight();
        int count = getChildCount();
        int gravity = getGravity();
        int majorGravity = gravity & 112;
        int minorGravity = gravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (majorGravity == 16) {
            paddingTop = getPaddingTop() + (((bottom - top) - totalLength) / 2);
        } else if (majorGravity != 80) {
            paddingTop = getPaddingTop();
        } else {
            paddingTop = ((getPaddingTop() + bottom) - top) - totalLength;
        }
        Drawable dividerDrawable = getDividerDrawable();
        if (dividerDrawable == null) {
            i = 0;
        } else {
            i = dividerDrawable.getIntrinsicHeight();
        }
        int dividerHeight = i;
        int i2 = 0;
        while (i2 < count) {
            int i3;
            View child = alertDialogLayout.getChildAt(i2);
            if (child == null || child.getVisibility() == 8) {
                i3 = i2;
            } else {
                int layoutGravity;
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                i = lp.gravity;
                if (i < 0) {
                    layoutGravity = minorGravity;
                } else {
                    layoutGravity = i;
                }
                int layoutDirection = ViewCompat.getLayoutDirection(this);
                i = GravityCompat.getAbsoluteGravity(layoutGravity, layoutDirection) & 7;
                int childLeft = i != 1 ? i != 5 ? lp.leftMargin + paddingLeft : (childRight - childWidth) - lp.rightMargin : ((((childSpace - childWidth) / 2) + paddingLeft) + lp.leftMargin) - lp.rightMargin;
                if (alertDialogLayout.hasDividerBeforeChildAt(i2)) {
                    paddingTop += dividerHeight;
                }
                int childTop = paddingTop + lp.topMargin;
                LayoutParams lp2 = lp;
                i3 = i2;
                setChildFrame(child, childLeft, childTop, childWidth, childHeight);
                paddingTop = childTop + (childHeight + lp2.bottomMargin);
            }
            i2 = i3 + 1;
            alertDialogLayout = this;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }
}
