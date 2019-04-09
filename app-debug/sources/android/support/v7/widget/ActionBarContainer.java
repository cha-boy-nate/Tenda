package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v7.appcompat.C0185R;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ActionBarContainer extends FrameLayout {
    private View mActionBarView;
    Drawable mBackground;
    private View mContextView;
    private int mHeight;
    boolean mIsSplit;
    boolean mIsStacked;
    private boolean mIsTransitioning;
    Drawable mSplitBackground;
    Drawable mStackedBackground;
    private View mTabContainer;

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ActionBarContainer(android.content.Context r6, android.util.AttributeSet r7) {
        /*
        r5 = this;
        r5.<init>(r6, r7);
        r0 = new android.support.v7.widget.ActionBarBackgroundDrawable;
        r0.<init>(r5);
        android.support.v4.view.ViewCompat.setBackground(r5, r0);
        r1 = android.support.v7.appcompat.C0185R.styleable.ActionBar;
        r1 = r6.obtainStyledAttributes(r7, r1);
        r2 = android.support.v7.appcompat.C0185R.styleable.ActionBar_background;
        r2 = r1.getDrawable(r2);
        r5.mBackground = r2;
        r2 = android.support.v7.appcompat.C0185R.styleable.ActionBar_backgroundStacked;
        r2 = r1.getDrawable(r2);
        r5.mStackedBackground = r2;
        r2 = android.support.v7.appcompat.C0185R.styleable.ActionBar_height;
        r3 = -1;
        r2 = r1.getDimensionPixelSize(r2, r3);
        r5.mHeight = r2;
        r2 = r5.getId();
        r3 = android.support.v7.appcompat.C0185R.id.split_action_bar;
        r4 = 1;
        if (r2 != r3) goto L_0x003d;
    L_0x0033:
        r5.mIsSplit = r4;
        r2 = android.support.v7.appcompat.C0185R.styleable.ActionBar_backgroundSplit;
        r2 = r1.getDrawable(r2);
        r5.mSplitBackground = r2;
    L_0x003d:
        r1.recycle();
        r2 = r5.mIsSplit;
        r3 = 0;
        if (r2 == 0) goto L_0x004b;
    L_0x0045:
        r2 = r5.mSplitBackground;
        if (r2 != 0) goto L_0x0054;
    L_0x0049:
        r3 = 1;
        goto L_0x0054;
    L_0x004b:
        r2 = r5.mBackground;
        if (r2 != 0) goto L_0x0054;
    L_0x004f:
        r2 = r5.mStackedBackground;
        if (r2 != 0) goto L_0x0054;
    L_0x0053:
        goto L_0x0049;
    L_0x0054:
        r5.setWillNotDraw(r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ActionBarContainer.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = findViewById(C0185R.id.action_bar);
        this.mContextView = findViewById(C0185R.id.action_context_bar);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setPrimaryBackground(android.graphics.drawable.Drawable r6) {
        /*
        r5 = this;
        r0 = r5.mBackground;
        if (r0 == 0) goto L_0x000d;
    L_0x0004:
        r1 = 0;
        r0.setCallback(r1);
        r0 = r5.mBackground;
        r5.unscheduleDrawable(r0);
    L_0x000d:
        r5.mBackground = r6;
        if (r6 == 0) goto L_0x0033;
    L_0x0011:
        r6.setCallback(r5);
        r0 = r5.mActionBarView;
        if (r0 == 0) goto L_0x0033;
    L_0x0018:
        r1 = r5.mBackground;
        r0 = r0.getLeft();
        r2 = r5.mActionBarView;
        r2 = r2.getTop();
        r3 = r5.mActionBarView;
        r3 = r3.getRight();
        r4 = r5.mActionBarView;
        r4 = r4.getBottom();
        r1.setBounds(r0, r2, r3, r4);
    L_0x0033:
        r0 = r5.mIsSplit;
        r1 = 1;
        r2 = 0;
        if (r0 == 0) goto L_0x003e;
    L_0x0039:
        r0 = r5.mSplitBackground;
        if (r0 != 0) goto L_0x0047;
    L_0x003d:
        goto L_0x0048;
    L_0x003e:
        r0 = r5.mBackground;
        if (r0 != 0) goto L_0x0047;
    L_0x0042:
        r0 = r5.mStackedBackground;
        if (r0 != 0) goto L_0x0047;
    L_0x0046:
        goto L_0x003d;
    L_0x0047:
        r1 = 0;
    L_0x0048:
        r5.setWillNotDraw(r1);
        r5.invalidate();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ActionBarContainer.setPrimaryBackground(android.graphics.drawable.Drawable):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setStackedBackground(android.graphics.drawable.Drawable r6) {
        /*
        r5 = this;
        r0 = r5.mStackedBackground;
        if (r0 == 0) goto L_0x000d;
    L_0x0004:
        r1 = 0;
        r0.setCallback(r1);
        r0 = r5.mStackedBackground;
        r5.unscheduleDrawable(r0);
    L_0x000d:
        r5.mStackedBackground = r6;
        if (r6 == 0) goto L_0x0037;
    L_0x0011:
        r6.setCallback(r5);
        r0 = r5.mIsStacked;
        if (r0 == 0) goto L_0x0037;
    L_0x0018:
        r0 = r5.mStackedBackground;
        if (r0 == 0) goto L_0x0037;
    L_0x001c:
        r1 = r5.mTabContainer;
        r1 = r1.getLeft();
        r2 = r5.mTabContainer;
        r2 = r2.getTop();
        r3 = r5.mTabContainer;
        r3 = r3.getRight();
        r4 = r5.mTabContainer;
        r4 = r4.getBottom();
        r0.setBounds(r1, r2, r3, r4);
    L_0x0037:
        r0 = r5.mIsSplit;
        r1 = 1;
        r2 = 0;
        if (r0 == 0) goto L_0x0042;
    L_0x003d:
        r0 = r5.mSplitBackground;
        if (r0 != 0) goto L_0x004b;
    L_0x0041:
        goto L_0x004c;
    L_0x0042:
        r0 = r5.mBackground;
        if (r0 != 0) goto L_0x004b;
    L_0x0046:
        r0 = r5.mStackedBackground;
        if (r0 != 0) goto L_0x004b;
    L_0x004a:
        goto L_0x0041;
    L_0x004b:
        r1 = 0;
    L_0x004c:
        r5.setWillNotDraw(r1);
        r5.invalidate();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ActionBarContainer.setStackedBackground(android.graphics.drawable.Drawable):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSplitBackground(android.graphics.drawable.Drawable r5) {
        /*
        r4 = this;
        r0 = r4.mSplitBackground;
        if (r0 == 0) goto L_0x000d;
    L_0x0004:
        r1 = 0;
        r0.setCallback(r1);
        r0 = r4.mSplitBackground;
        r4.unscheduleDrawable(r0);
    L_0x000d:
        r4.mSplitBackground = r5;
        r0 = 0;
        if (r5 == 0) goto L_0x0028;
    L_0x0012:
        r5.setCallback(r4);
        r1 = r4.mIsSplit;
        if (r1 == 0) goto L_0x0028;
    L_0x0019:
        r1 = r4.mSplitBackground;
        if (r1 == 0) goto L_0x0028;
    L_0x001d:
        r2 = r4.getMeasuredWidth();
        r3 = r4.getMeasuredHeight();
        r1.setBounds(r0, r0, r2, r3);
    L_0x0028:
        r1 = r4.mIsSplit;
        r2 = 1;
        if (r1 == 0) goto L_0x0033;
    L_0x002d:
        r1 = r4.mSplitBackground;
        if (r1 != 0) goto L_0x003c;
    L_0x0031:
        r0 = 1;
        goto L_0x003c;
    L_0x0033:
        r1 = r4.mBackground;
        if (r1 != 0) goto L_0x003c;
    L_0x0037:
        r1 = r4.mStackedBackground;
        if (r1 != 0) goto L_0x003c;
    L_0x003b:
        goto L_0x0031;
    L_0x003c:
        r4.setWillNotDraw(r0);
        r4.invalidate();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ActionBarContainer.setSplitBackground(android.graphics.drawable.Drawable):void");
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean isVisible = visibility == 0;
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setVisible(isVisible, false);
        }
        drawable = this.mStackedBackground;
        if (drawable != null) {
            drawable.setVisible(isVisible, false);
        }
        drawable = this.mSplitBackground;
        if (drawable != null) {
            drawable.setVisible(isVisible, false);
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return (who == this.mBackground && !this.mIsSplit) || ((who == this.mStackedBackground && this.mIsStacked) || ((who == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(who)));
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mBackground;
        if (drawable != null && drawable.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        drawable = this.mStackedBackground;
        if (drawable != null && drawable.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        drawable = this.mSplitBackground;
        if (drawable != null && drawable.isStateful()) {
            this.mSplitBackground.setState(getDrawableState());
        }
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        drawable = this.mStackedBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        drawable = this.mSplitBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    public void setTransitioning(boolean isTransitioning) {
        this.mIsTransitioning = isTransitioning;
        setDescendantFocusability(isTransitioning ? 393216 : 262144);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!this.mIsTransitioning) {
            if (!super.onInterceptTouchEvent(ev)) {
                return false;
            }
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }

    public boolean onHoverEvent(MotionEvent ev) {
        super.onHoverEvent(ev);
        return true;
    }

    public void setTabContainer(ScrollingTabContainerView tabView) {
        View view = this.mTabContainer;
        if (view != null) {
            removeView(view);
        }
        this.mTabContainer = tabView;
        if (tabView != null) {
            addView(tabView);
            LayoutParams lp = tabView.getLayoutParams();
            lp.width = -1;
            lp.height = -2;
            tabView.setAllowCollapse(false);
        }
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    public ActionMode startActionModeForChild(View child, Callback callback) {
        return null;
    }

    public ActionMode startActionModeForChild(View child, Callback callback, int type) {
        if (type != 0) {
            return super.startActionModeForChild(child, callback, type);
        }
        return null;
    }

    private boolean isCollapsed(View view) {
        if (!(view == null || view.getVisibility() == 8)) {
            if (view.getMeasuredHeight() != 0) {
                return false;
            }
        }
        return true;
    }

    private int getMeasuredHeightWithMargins(View view) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        return (view.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        if (this.mActionBarView == null && MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE) {
            i = this.mHeight;
            if (i >= 0) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(i, MeasureSpec.getSize(heightMeasureSpec)), Integer.MIN_VALUE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mActionBarView != null) {
            i = MeasureSpec.getMode(heightMeasureSpec);
            View view = this.mTabContainer;
            if (!(view == null || view.getVisibility() == 8 || i == 1073741824)) {
                int topMarginForTabs;
                if (!isCollapsed(this.mActionBarView)) {
                    topMarginForTabs = getMeasuredHeightWithMargins(this.mActionBarView);
                } else if (isCollapsed(this.mContextView)) {
                    topMarginForTabs = 0;
                } else {
                    topMarginForTabs = getMeasuredHeightWithMargins(this.mContextView);
                }
                setMeasuredDimension(getMeasuredWidth(), Math.min(getMeasuredHeightWithMargins(this.mTabContainer) + topMarginForTabs, i == Integer.MIN_VALUE ? MeasureSpec.getSize(heightMeasureSpec) : Integer.MAX_VALUE));
            }
        }
    }

    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View tabContainer = this.mTabContainer;
        boolean hasTabs = (tabContainer == null || tabContainer.getVisibility() == 8) ? false : true;
        if (!(tabContainer == null || tabContainer.getVisibility() == 8)) {
            int containerHeight = getMeasuredHeight();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tabContainer.getLayoutParams();
            tabContainer.layout(l, (containerHeight - tabContainer.getMeasuredHeight()) - lp.bottomMargin, r, containerHeight - lp.bottomMargin);
        }
        boolean needsInvalidate = false;
        if (this.mIsSplit) {
            Drawable drawable = this.mSplitBackground;
            if (drawable != null) {
                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                needsInvalidate = true;
            }
        } else {
            if (this.mBackground != null) {
                if (this.mActionBarView.getVisibility() == 0) {
                    this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
                } else {
                    View view = this.mContextView;
                    if (view == null || view.getVisibility() != 0) {
                        this.mBackground.setBounds(0, 0, 0, 0);
                    } else {
                        this.mBackground.setBounds(this.mContextView.getLeft(), this.mContextView.getTop(), this.mContextView.getRight(), this.mContextView.getBottom());
                    }
                }
                needsInvalidate = true;
            }
            this.mIsStacked = hasTabs;
            if (hasTabs) {
                Drawable drawable2 = this.mStackedBackground;
                if (drawable2 != null) {
                    drawable2.setBounds(tabContainer.getLeft(), tabContainer.getTop(), tabContainer.getRight(), tabContainer.getBottom());
                    needsInvalidate = true;
                }
            }
        }
        if (needsInvalidate) {
            invalidate();
        }
    }
}
