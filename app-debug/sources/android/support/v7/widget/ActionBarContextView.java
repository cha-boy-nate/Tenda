package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.appcompat.C0185R;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ActionBarContextView extends AbsActionBarView {
    private static final String TAG = "ActionBarContextView";
    private View mClose;
    private int mCloseItemLayout;
    private View mCustomView;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private boolean mTitleOptional;
    private int mTitleStyleRes;
    private TextView mTitleView;

    protected void onMeasure(int r18, int r19) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:69:0x0149 in {6, 7, 10, 15, 24, 25, 27, 30, 31, 32, 33, 38, 39, 42, 43, 46, 47, 50, 51, 52, 53, 60, 61, 62, 63, 64, 66, 68} preds:[]
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
        r17 = this;
        r0 = r17;
        r1 = android.view.View.MeasureSpec.getMode(r18);
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r1 != r2) goto L_0x0123;
    L_0x000a:
        r3 = android.view.View.MeasureSpec.getMode(r19);
        if (r3 == 0) goto L_0x00fd;
    L_0x0010:
        r4 = android.view.View.MeasureSpec.getSize(r18);
        r5 = r0.mContentHeight;
        if (r5 <= 0) goto L_0x001b;
    L_0x0018:
        r5 = r0.mContentHeight;
        goto L_0x001f;
    L_0x001b:
        r5 = android.view.View.MeasureSpec.getSize(r19);
        r6 = r17.getPaddingTop();
        r7 = r17.getPaddingBottom();
        r6 = r6 + r7;
        r7 = r17.getPaddingLeft();
        r7 = r4 - r7;
        r8 = r17.getPaddingRight();
        r7 = r7 - r8;
        r8 = r5 - r6;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r9);
        r11 = r0.mClose;
        r12 = 0;
        if (r11 == 0) goto L_0x0053;
    L_0x0041:
        r7 = r0.measureChildView(r11, r7, r10, r12);
        r11 = r0.mClose;
        r11 = r11.getLayoutParams();
        r11 = (android.view.ViewGroup.MarginLayoutParams) r11;
        r13 = r11.leftMargin;
        r14 = r11.rightMargin;
        r13 = r13 + r14;
        r7 = r7 - r13;
    L_0x0053:
        r11 = r0.mMenuView;
        if (r11 == 0) goto L_0x0065;
    L_0x0057:
        r11 = r0.mMenuView;
        r11 = r11.getParent();
        if (r11 != r0) goto L_0x0065;
    L_0x005f:
        r11 = r0.mMenuView;
        r7 = r0.measureChildView(r11, r7, r10, r12);
    L_0x0065:
        r11 = r0.mTitleLayout;
        if (r11 == 0) goto L_0x0097;
    L_0x0069:
        r13 = r0.mCustomView;
        if (r13 != 0) goto L_0x0097;
    L_0x006d:
        r13 = r0.mTitleOptional;
        if (r13 == 0) goto L_0x0093;
    L_0x0071:
        r11 = android.view.View.MeasureSpec.makeMeasureSpec(r12, r12);
        r13 = r0.mTitleLayout;
        r13.measure(r11, r10);
        r13 = r0.mTitleLayout;
        r13 = r13.getMeasuredWidth();
        if (r13 > r7) goto L_0x0084;
    L_0x0082:
        r14 = 1;
        goto L_0x0085;
    L_0x0084:
        r14 = 0;
    L_0x0085:
        if (r14 == 0) goto L_0x0088;
    L_0x0087:
        r7 = r7 - r13;
    L_0x0088:
        r15 = r0.mTitleLayout;
        if (r14 == 0) goto L_0x008d;
    L_0x008c:
        goto L_0x008f;
    L_0x008d:
        r12 = 8;
    L_0x008f:
        r15.setVisibility(r12);
        goto L_0x0097;
    L_0x0093:
        r7 = r0.measureChildView(r11, r7, r10, r12);
    L_0x0097:
        r11 = r0.mCustomView;
        if (r11 == 0) goto L_0x00d8;
    L_0x009b:
        r11 = r11.getLayoutParams();
        r12 = r11.width;
        r13 = -2;
        if (r12 == r13) goto L_0x00a7;
    L_0x00a4:
        r12 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x00a9;
    L_0x00a7:
        r12 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
    L_0x00a9:
        r14 = r11.width;
        if (r14 < 0) goto L_0x00b4;
    L_0x00ad:
        r14 = r11.width;
        r14 = java.lang.Math.min(r14, r7);
        goto L_0x00b5;
    L_0x00b4:
        r14 = r7;
    L_0x00b5:
        r15 = r11.height;
        if (r15 == r13) goto L_0x00ba;
    L_0x00b9:
        goto L_0x00bc;
    L_0x00ba:
        r2 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
    L_0x00bc:
        r9 = r11.height;
        if (r9 < 0) goto L_0x00c7;
    L_0x00c0:
        r9 = r11.height;
        r9 = java.lang.Math.min(r9, r8);
        goto L_0x00c8;
    L_0x00c7:
        r9 = r8;
    L_0x00c8:
        r13 = r0.mCustomView;
        r15 = android.view.View.MeasureSpec.makeMeasureSpec(r14, r12);
        r16 = r1;
        r1 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r2);
        r13.measure(r15, r1);
        goto L_0x00da;
    L_0x00d8:
        r16 = r1;
    L_0x00da:
        r1 = r0.mContentHeight;
        if (r1 > 0) goto L_0x00f9;
    L_0x00de:
        r1 = 0;
        r2 = r17.getChildCount();
        r9 = 0;
    L_0x00e4:
        if (r9 >= r2) goto L_0x00f5;
    L_0x00e6:
        r11 = r0.getChildAt(r9);
        r12 = r11.getMeasuredHeight();
        r12 = r12 + r6;
        if (r12 <= r1) goto L_0x00f2;
    L_0x00f1:
        r1 = r12;
    L_0x00f2:
        r9 = r9 + 1;
        goto L_0x00e4;
    L_0x00f5:
        r0.setMeasuredDimension(r4, r1);
        goto L_0x00fc;
    L_0x00f9:
        r0.setMeasuredDimension(r4, r5);
    L_0x00fc:
        return;
    L_0x00fd:
        r16 = r1;
        r1 = new java.lang.IllegalStateException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = r17.getClass();
        r4 = r4.getSimpleName();
        r2.append(r4);
        r4 = " can only be used ";
        r2.append(r4);
        r4 = "with android:layout_height=\"wrap_content\"";
        r2.append(r4);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x0123:
        r16 = r1;
        r1 = new java.lang.IllegalStateException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = r17.getClass();
        r3 = r3.getSimpleName();
        r2.append(r3);
        r3 = " can only be used ";
        r2.append(r3);
        r3 = "with android:layout_width=\"match_parent\" (or fill_parent)";
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ActionBarContextView.onMeasure(int, int):void");
    }

    public /* bridge */ /* synthetic */ void animateToVisibility(int i) {
        super.animateToVisibility(i);
    }

    public /* bridge */ /* synthetic */ boolean canShowOverflowMenu() {
        return super.canShowOverflowMenu();
    }

    public /* bridge */ /* synthetic */ void dismissPopupMenus() {
        super.dismissPopupMenus();
    }

    public /* bridge */ /* synthetic */ int getAnimatedVisibility() {
        return super.getAnimatedVisibility();
    }

    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    public /* bridge */ /* synthetic */ boolean isOverflowMenuShowPending() {
        return super.isOverflowMenuShowPending();
    }

    public /* bridge */ /* synthetic */ boolean isOverflowReserved() {
        return super.isOverflowReserved();
    }

    public /* bridge */ /* synthetic */ boolean onHoverEvent(MotionEvent motionEvent) {
        return super.onHoverEvent(motionEvent);
    }

    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public /* bridge */ /* synthetic */ void postShowOverflowMenu() {
        super.postShowOverflowMenu();
    }

    public /* bridge */ /* synthetic */ void setVisibility(int i) {
        super.setVisibility(i);
    }

    public /* bridge */ /* synthetic */ ViewPropertyAnimatorCompat setupAnimatorToVisibility(int i, long j) {
        return super.setupAnimatorToVisibility(i, j);
    }

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    public ActionBarContextView(Context context, AttributeSet attrs) {
        this(context, attrs, C0185R.attr.actionModeStyle);
    }

    public ActionBarContextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, C0185R.styleable.ActionMode, defStyle, 0);
        ViewCompat.setBackground(this, a.getDrawable(C0185R.styleable.ActionMode_background));
        this.mTitleStyleRes = a.getResourceId(C0185R.styleable.ActionMode_titleTextStyle, 0);
        this.mSubtitleStyleRes = a.getResourceId(C0185R.styleable.ActionMode_subtitleTextStyle, 0);
        this.mContentHeight = a.getLayoutDimension(C0185R.styleable.ActionMode_height, 0);
        this.mCloseItemLayout = a.getResourceId(C0185R.styleable.ActionMode_closeItemLayout, C0185R.layout.abc_action_mode_close_item_material);
        a.recycle();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    public void setContentHeight(int height) {
        this.mContentHeight = height;
    }

    public void setCustomView(View view) {
        View view2 = this.mCustomView;
        if (view2 != null) {
            removeView(view2);
        }
        this.mCustomView = view;
        if (view != null) {
            view2 = this.mTitleLayout;
            if (view2 != null) {
                removeView(view2);
                this.mTitleLayout = null;
            }
        }
        if (view != null) {
            addView(view);
        }
        requestLayout();
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        initTitle();
    }

    public void setSubtitle(CharSequence subtitle) {
        this.mSubtitle = subtitle;
        initTitle();
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    private void initTitle() {
        if (this.mTitleLayout == null) {
            LayoutInflater.from(getContext()).inflate(C0185R.layout.abc_action_bar_title_item, this);
            this.mTitleLayout = (LinearLayout) getChildAt(getChildCount() - 1);
            this.mTitleView = (TextView) this.mTitleLayout.findViewById(C0185R.id.action_bar_title);
            this.mSubtitleView = (TextView) this.mTitleLayout.findViewById(C0185R.id.action_bar_subtitle);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(getContext(), this.mTitleStyleRes);
            }
            if (this.mSubtitleStyleRes != 0) {
                this.mSubtitleView.setTextAppearance(getContext(), this.mSubtitleStyleRes);
            }
        }
        this.mTitleView.setText(this.mTitle);
        this.mSubtitleView.setText(this.mSubtitle);
        boolean hasTitle = TextUtils.isEmpty(this.mTitle) ^ 1;
        boolean hasSubtitle = TextUtils.isEmpty(this.mSubtitle) ^ 1;
        int i = 0;
        this.mSubtitleView.setVisibility(hasSubtitle ? 0 : 8);
        LinearLayout linearLayout = this.mTitleLayout;
        if (!hasTitle) {
            if (!hasSubtitle) {
                i = 8;
            }
        }
        linearLayout.setVisibility(i);
        if (this.mTitleLayout.getParent() == null) {
            addView(this.mTitleLayout);
        }
    }

    public void initForMode(final ActionMode mode) {
        MenuBuilder menu;
        LayoutParams layoutParams;
        View view = this.mClose;
        if (view == null) {
            this.mClose = LayoutInflater.from(getContext()).inflate(this.mCloseItemLayout, this, false);
            addView(this.mClose);
        } else if (view.getParent() == null) {
            addView(this.mClose);
            this.mClose.findViewById(C0185R.id.action_mode_close_button).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    mode.finish();
                }
            });
            menu = (MenuBuilder) mode.getMenu();
            if (this.mActionMenuPresenter != null) {
                this.mActionMenuPresenter.dismissPopupMenus();
            }
            this.mActionMenuPresenter = new ActionMenuPresenter(getContext());
            this.mActionMenuPresenter.setReserveOverflow(true);
            layoutParams = new LayoutParams(-2, -1);
            menu.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
            this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
            ViewCompat.setBackground(this.mMenuView, null);
            addView(this.mMenuView, layoutParams);
        }
        this.mClose.findViewById(C0185R.id.action_mode_close_button).setOnClickListener(/* anonymous class already generated */);
        menu = (MenuBuilder) mode.getMenu();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.dismissPopupMenus();
        }
        this.mActionMenuPresenter = new ActionMenuPresenter(getContext());
        this.mActionMenuPresenter.setReserveOverflow(true);
        layoutParams = new LayoutParams(-2, -1);
        menu.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
        this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
        ViewCompat.setBackground(this.mMenuView, null);
        addView(this.mMenuView, layoutParams);
    }

    public void closeMode() {
        if (this.mClose == null) {
            killMode();
        }
    }

    public void killMode() {
        removeAllViews();
        this.mCustomView = null;
        this.mMenuView = null;
    }

    public boolean showOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.showOverflowMenu();
        }
        return false;
    }

    public boolean hideOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.hideOverflowMenu();
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.isOverflowMenuShowing();
        }
        return false;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int x;
        ActionBarContextView actionBarContextView = this;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int x2 = isLayoutRtl ? (r - l) - getPaddingRight() : getPaddingLeft();
        int y = getPaddingTop();
        int contentHeight = ((b - t) - getPaddingTop()) - getPaddingBottom();
        View view = actionBarContextView.mClose;
        if (view == null || view.getVisibility() == 8) {
            x = x2;
        } else {
            MarginLayoutParams lp = (MarginLayoutParams) actionBarContextView.mClose.getLayoutParams();
            int endMargin = isLayoutRtl ? lp.leftMargin : lp.rightMargin;
            int x3 = AbsActionBarView.next(x2, isLayoutRtl ? lp.rightMargin : lp.leftMargin, isLayoutRtl);
            x = AbsActionBarView.next(x3 + positionChild(actionBarContextView.mClose, x3, y, contentHeight, isLayoutRtl), endMargin, isLayoutRtl);
        }
        LinearLayout linearLayout = actionBarContextView.mTitleLayout;
        if (!(linearLayout == null || actionBarContextView.mCustomView != null || linearLayout.getVisibility() == 8)) {
            x += positionChild(actionBarContextView.mTitleLayout, x, y, contentHeight, isLayoutRtl);
        }
        view = actionBarContextView.mCustomView;
        if (view != null) {
            x += positionChild(view, x, y, contentHeight, isLayoutRtl);
        }
        int x4 = isLayoutRtl ? getPaddingLeft() : (r - l) - getPaddingRight();
        if (actionBarContextView.mMenuView != null) {
            x4 += positionChild(actionBarContextView.mMenuView, x4, y, contentHeight, isLayoutRtl ^ 1);
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == 32) {
            event.setSource(this);
            event.setClassName(getClass().getName());
            event.setPackageName(getContext().getPackageName());
            event.setContentDescription(this.mTitle);
            return;
        }
        super.onInitializeAccessibilityEvent(event);
    }

    public void setTitleOptional(boolean titleOptional) {
        if (titleOptional != this.mTitleOptional) {
            requestLayout();
        }
        this.mTitleOptional = titleOptional;
    }

    public boolean isTitleOptional() {
        return this.mTitleOptional;
    }
}
