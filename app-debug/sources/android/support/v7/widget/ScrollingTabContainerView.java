package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.appcompat.C0185R;
import android.support.v7.view.ActionBarPolicy;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ScrollingTabContainerView extends HorizontalScrollView implements OnItemSelectedListener {
    private static final int FADE_DURATION = 200;
    private static final String TAG = "ScrollingTabContainerView";
    private static final Interpolator sAlphaInterpolator = new DecelerateInterpolator();
    private boolean mAllowCollapse;
    private int mContentHeight;
    int mMaxTabWidth;
    private int mSelectedTabIndex;
    int mStackedTabMaxWidth;
    private TabClickListener mTabClickListener;
    LinearLayoutCompat mTabLayout;
    Runnable mTabSelector;
    private Spinner mTabSpinner;
    protected final VisibilityAnimListener mVisAnimListener = new VisibilityAnimListener();
    protected ViewPropertyAnimator mVisibilityAnim;

    private class TabAdapter extends BaseAdapter {
        TabAdapter() {
        }

        public int getCount() {
            return ScrollingTabContainerView.this.mTabLayout.getChildCount();
        }

        public Object getItem(int position) {
            return ((TabView) ScrollingTabContainerView.this.mTabLayout.getChildAt(position)).getTab();
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                return ScrollingTabContainerView.this.createTabView((Tab) getItem(position), true);
            }
            ((TabView) convertView).bindTab((Tab) getItem(position));
            return convertView;
        }
    }

    private class TabClickListener implements OnClickListener {
        TabClickListener() {
        }

        public void onClick(View view) {
            ((TabView) view).getTab().select();
            int tabCount = ScrollingTabContainerView.this.mTabLayout.getChildCount();
            for (int i = 0; i < tabCount; i++) {
                View child = ScrollingTabContainerView.this.mTabLayout.getChildAt(i);
                child.setSelected(child == view);
            }
        }
    }

    private class TabView extends LinearLayout {
        private final int[] BG_ATTRS = new int[]{16842964};
        private View mCustomView;
        private ImageView mIconView;
        private Tab mTab;
        private TextView mTextView;

        public TabView(Context context, Tab tab, boolean forList) {
            super(context, null, C0185R.attr.actionBarTabStyle);
            this.mTab = tab;
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, this.BG_ATTRS, C0185R.attr.actionBarTabStyle, 0);
            if (a.hasValue(0)) {
                setBackgroundDrawable(a.getDrawable(0));
            }
            a.recycle();
            if (forList) {
                setGravity(8388627);
            }
            update();
        }

        public void bindTab(Tab tab) {
            this.mTab = tab;
            update();
        }

        public void setSelected(boolean selected) {
            boolean changed = isSelected() != selected;
            super.setSelected(selected);
            if (changed && selected) {
                sendAccessibilityEvent(4);
            }
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            event.setClassName(Tab.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setClassName(Tab.class.getName());
        }

        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (ScrollingTabContainerView.this.mMaxTabWidth > 0 && getMeasuredWidth() > ScrollingTabContainerView.this.mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(ScrollingTabContainerView.this.mMaxTabWidth, 1073741824), heightMeasureSpec);
            }
        }

        public void update() {
            Tab tab = this.mTab;
            View custom = tab.getCustomView();
            CharSequence charSequence = null;
            if (custom != null) {
                TabView customParent = custom.getParent();
                if (customParent != this) {
                    if (customParent != null) {
                        customParent.removeView(custom);
                    }
                    addView(custom);
                }
                this.mCustomView = custom;
                TextView textView = this.mTextView;
                if (textView != null) {
                    textView.setVisibility(8);
                }
                ImageView imageView = this.mIconView;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
                return;
            }
            View view = this.mCustomView;
            if (view != null) {
                removeView(view);
                this.mCustomView = null;
            }
            Drawable icon = tab.getIcon();
            CharSequence text = tab.getText();
            ImageView iconView;
            if (icon != null) {
                if (this.mIconView == null) {
                    iconView = new AppCompatImageView(getContext());
                    LayoutParams lp = new LayoutParams(-2, -2);
                    lp.gravity = 16;
                    iconView.setLayoutParams(lp);
                    addView(iconView, 0);
                    this.mIconView = iconView;
                }
                this.mIconView.setImageDrawable(icon);
                this.mIconView.setVisibility(0);
            } else {
                iconView = this.mIconView;
                if (iconView != null) {
                    iconView.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
            }
            boolean hasText = TextUtils.isEmpty(text) ^ 1;
            if (hasText) {
                if (this.mTextView == null) {
                    TextView textView2 = new AppCompatTextView(getContext(), null, C0185R.attr.actionBarTabTextStyle);
                    textView2.setEllipsize(TruncateAt.END);
                    LayoutParams lp2 = new LayoutParams(-2, -2);
                    lp2.gravity = 16;
                    textView2.setLayoutParams(lp2);
                    addView(textView2);
                    this.mTextView = textView2;
                }
                this.mTextView.setText(text);
                this.mTextView.setVisibility(0);
            } else {
                TextView textView3 = this.mTextView;
                if (textView3 != null) {
                    textView3.setVisibility(8);
                    this.mTextView.setText(null);
                }
            }
            ImageView imageView2 = this.mIconView;
            if (imageView2 != null) {
                imageView2.setContentDescription(tab.getContentDescription());
            }
            if (!hasText) {
                charSequence = tab.getContentDescription();
            }
            TooltipCompat.setTooltipText(this, charSequence);
        }

        public Tab getTab() {
            return this.mTab;
        }
    }

    protected class VisibilityAnimListener extends AnimatorListenerAdapter {
        private boolean mCanceled = false;
        private int mFinalVisibility;

        protected VisibilityAnimListener() {
        }

        public VisibilityAnimListener withFinalVisibility(ViewPropertyAnimator animation, int visibility) {
            this.mFinalVisibility = visibility;
            ScrollingTabContainerView.this.mVisibilityAnim = animation;
            return this;
        }

        public void onAnimationStart(Animator animator) {
            ScrollingTabContainerView.this.setVisibility(0);
            this.mCanceled = false;
        }

        public void onAnimationEnd(Animator animator) {
            if (!this.mCanceled) {
                ScrollingTabContainerView scrollingTabContainerView = ScrollingTabContainerView.this;
                scrollingTabContainerView.mVisibilityAnim = null;
                scrollingTabContainerView.setVisibility(this.mFinalVisibility);
            }
        }

        public void onAnimationCancel(Animator animator) {
            this.mCanceled = true;
        }
    }

    public ScrollingTabContainerView(Context context) {
        super(context);
        setHorizontalScrollBarEnabled(false);
        ActionBarPolicy abp = ActionBarPolicy.get(context);
        setContentHeight(abp.getTabContainerHeight());
        this.mStackedTabMaxWidth = abp.getStackedTabMaxWidth();
        this.mTabLayout = createTabLayout();
        addView(this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        boolean canCollapse = true;
        boolean lockedExpanded = widthMode == 1073741824;
        setFillViewport(lockedExpanded);
        int childCount = this.mTabLayout.getChildCount();
        if (childCount <= 1 || !(widthMode == 1073741824 || widthMode == Integer.MIN_VALUE)) {
            this.mMaxTabWidth = -1;
        } else {
            if (childCount > 2) {
                this.mMaxTabWidth = (int) (((float) MeasureSpec.getSize(widthMeasureSpec)) * 0.4f);
            } else {
                this.mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
            this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
        if (lockedExpanded || !this.mAllowCollapse) {
            canCollapse = false;
        }
        if (canCollapse) {
            this.mTabLayout.measure(0, heightMeasureSpec);
            if (this.mTabLayout.getMeasuredWidth() > MeasureSpec.getSize(widthMeasureSpec)) {
                performCollapse();
            } else {
                performExpand();
            }
        } else {
            performExpand();
        }
        int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth = getMeasuredWidth();
        if (lockedExpanded && oldWidth != newWidth) {
            setTabSelected(this.mSelectedTabIndex);
        }
    }

    private boolean isCollapsed() {
        Spinner spinner = this.mTabSpinner;
        return spinner != null && spinner.getParent() == this;
    }

    public void setAllowCollapse(boolean allowCollapse) {
        this.mAllowCollapse = allowCollapse;
    }

    private void performCollapse() {
        if (!isCollapsed()) {
            if (this.mTabSpinner == null) {
                this.mTabSpinner = createSpinner();
            }
            removeView(this.mTabLayout);
            addView(this.mTabSpinner, new ViewGroup.LayoutParams(-2, -1));
            if (this.mTabSpinner.getAdapter() == null) {
                this.mTabSpinner.setAdapter(new TabAdapter());
            }
            Runnable runnable = this.mTabSelector;
            if (runnable != null) {
                removeCallbacks(runnable);
                this.mTabSelector = null;
            }
            this.mTabSpinner.setSelection(this.mSelectedTabIndex);
        }
    }

    private boolean performExpand() {
        if (!isCollapsed()) {
            return false;
        }
        removeView(this.mTabSpinner);
        addView(this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
        setTabSelected(this.mTabSpinner.getSelectedItemPosition());
        return false;
    }

    public void setTabSelected(int position) {
        this.mSelectedTabIndex = position;
        int tabCount = this.mTabLayout.getChildCount();
        int i = 0;
        while (i < tabCount) {
            View child = this.mTabLayout.getChildAt(i);
            boolean isSelected = i == position;
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(position);
            }
            i++;
        }
        Spinner spinner = this.mTabSpinner;
        if (spinner != null && position >= 0) {
            spinner.setSelection(position);
        }
    }

    public void setContentHeight(int contentHeight) {
        this.mContentHeight = contentHeight;
        requestLayout();
    }

    private LinearLayoutCompat createTabLayout() {
        LinearLayoutCompat tabLayout = new LinearLayoutCompat(getContext(), null, C0185R.attr.actionBarTabBarStyle);
        tabLayout.setMeasureWithLargestChildEnabled(true);
        tabLayout.setGravity(17);
        tabLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(-2, -1));
        return tabLayout;
    }

    private Spinner createSpinner() {
        Spinner spinner = new AppCompatSpinner(getContext(), null, C0185R.attr.actionDropDownStyle);
        spinner.setLayoutParams(new LinearLayoutCompat.LayoutParams(-2, -1));
        spinner.setOnItemSelectedListener(this);
        return spinner;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ActionBarPolicy abp = ActionBarPolicy.get(getContext());
        setContentHeight(abp.getTabContainerHeight());
        this.mStackedTabMaxWidth = abp.getStackedTabMaxWidth();
    }

    public void animateToVisibility(int visibility) {
        ViewPropertyAnimator viewPropertyAnimator = this.mVisibilityAnim;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        if (visibility == 0) {
            if (getVisibility() != 0) {
                setAlpha(0.0f);
            }
            ViewPropertyAnimator anim = animate().alpha(1.0f);
            anim.setDuration(200);
            anim.setInterpolator(sAlphaInterpolator);
            anim.setListener(this.mVisAnimListener.withFinalVisibility(anim, visibility));
            anim.start();
            return;
        }
        anim = animate().alpha(0.0f);
        anim.setDuration(200);
        anim.setInterpolator(sAlphaInterpolator);
        anim.setListener(this.mVisAnimListener.withFinalVisibility(anim, visibility));
        anim.start();
    }

    public void animateToTab(int position) {
        final View tabView = this.mTabLayout.getChildAt(position);
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        this.mTabSelector = new Runnable() {
            public void run() {
                ScrollingTabContainerView.this.smoothScrollTo(tabView.getLeft() - ((ScrollingTabContainerView.this.getWidth() - tabView.getWidth()) / 2), 0);
                ScrollingTabContainerView.this.mTabSelector = null;
            }
        };
        post(this.mTabSelector);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            post(runnable);
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
    }

    TabView createTabView(Tab tab, boolean forAdapter) {
        TabView tabView = new TabView(getContext(), tab, forAdapter);
        if (forAdapter) {
            tabView.setBackgroundDrawable(null);
            tabView.setLayoutParams(new AbsListView.LayoutParams(-1, this.mContentHeight));
        } else {
            tabView.setFocusable(true);
            if (this.mTabClickListener == null) {
                this.mTabClickListener = new TabClickListener();
            }
            tabView.setOnClickListener(this.mTabClickListener);
        }
        return tabView;
    }

    public void addTab(Tab tab, boolean setSelected) {
        TabView tabView = createTabView(tab, false);
        this.mTabLayout.addView(tabView, new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (setSelected) {
            tabView.setSelected(true);
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void addTab(Tab tab, int position, boolean setSelected) {
        TabView tabView = createTabView(tab, false);
        this.mTabLayout.addView(tabView, position, new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (setSelected) {
            tabView.setSelected(true);
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void updateTab(int position) {
        ((TabView) this.mTabLayout.getChildAt(position)).update();
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void removeTabAt(int position) {
        this.mTabLayout.removeViewAt(position);
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void removeAllTabs() {
        this.mTabLayout.removeAllViews();
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        ((TabView) view).getTab().select();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
