package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0185R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuPresenter.Callback;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup {
    private static final String TAG = "Toolbar";
    private Callback mActionMenuPresenterCallback;
    int mButtonGravity;
    ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private int mContentInsetEndWithActions;
    private int mContentInsetStartWithNavigation;
    private RtlSpacingHelper mContentInsets;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final android.support.v7.widget.ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;

    /* renamed from: android.support.v7.widget.Toolbar$2 */
    class C02242 implements Runnable {
        C02242() {
        }

        public void run() {
            Toolbar.this.showOverflowMenu();
        }
    }

    /* renamed from: android.support.v7.widget.Toolbar$3 */
    class C02253 implements OnClickListener {
        C02253() {
        }

        public void onClick(View v) {
            Toolbar.this.collapseActionView();
        }
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    /* renamed from: android.support.v7.widget.Toolbar$1 */
    class C02871 implements android.support.v7.widget.ActionMenuView.OnMenuItemClickListener {
        C02871() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (Toolbar.this.mOnMenuItemClickListener != null) {
                return Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(item);
            }
            return false;
        }
    }

    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        ExpandedActionViewMenuPresenter() {
        }

        public void initForMenu(Context context, MenuBuilder menu) {
            MenuBuilder menuBuilder = this.mMenu;
            if (menuBuilder != null) {
                MenuItemImpl menuItemImpl = this.mCurrentExpandedItem;
                if (menuItemImpl != null) {
                    menuBuilder.collapseItemActionView(menuItemImpl);
                }
            }
            this.mMenu = menu;
        }

        public MenuView getMenuView(ViewGroup root) {
            return null;
        }

        public void updateMenuView(boolean cleared) {
            if (this.mCurrentExpandedItem != null) {
                boolean found = false;
                int count = this.mMenu;
                if (count != 0) {
                    count = count.size();
                    for (int i = 0; i < count; i++) {
                        if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }

        public void setCallback(Callback cb) {
        }

        public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
            return false;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        }

        public boolean flagActionItems() {
            return false;
        }

        public boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
            Toolbar.this.ensureCollapseButtonView();
            ViewParent collapseButtonParent = Toolbar.this.mCollapseButtonView.getParent();
            ViewParent viewParent = Toolbar.this;
            if (collapseButtonParent != viewParent) {
                if (collapseButtonParent instanceof ViewGroup) {
                    ((ViewGroup) collapseButtonParent).removeView(viewParent.mCollapseButtonView);
                }
                Toolbar toolbar = Toolbar.this;
                toolbar.addView(toolbar.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = item.getActionView();
            this.mCurrentExpandedItem = item;
            viewParent = Toolbar.this.mExpandedActionView.getParent();
            ViewParent viewParent2 = Toolbar.this;
            if (viewParent != viewParent2) {
                if (viewParent instanceof ViewGroup) {
                    ((ViewGroup) viewParent).removeView(viewParent2.mExpandedActionView);
                }
                LayoutParams lp = Toolbar.this.generateDefaultLayoutParams();
                lp.gravity = GravityCompat.START | (Toolbar.this.mButtonGravity & 112);
                lp.mViewType = 2;
                Toolbar.this.mExpandedActionView.setLayoutParams(lp);
                Toolbar toolbar2 = Toolbar.this;
                toolbar2.addView(toolbar2.mExpandedActionView);
            }
            Toolbar.this.removeChildrenForExpandedActionView();
            Toolbar.this.requestLayout();
            item.setActionViewExpanded(true);
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }

        public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewCollapsed();
            }
            Toolbar toolbar = Toolbar.this;
            toolbar.removeView(toolbar.mExpandedActionView);
            toolbar = Toolbar.this;
            toolbar.removeView(toolbar.mCollapseButtonView);
            toolbar = Toolbar.this;
            toolbar.mExpandedActionView = null;
            toolbar.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            Toolbar.this.requestLayout();
            item.setActionViewExpanded(false);
            return true;
        }

        public int getId() {
            return 0;
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onRestoreInstanceState(Parcelable state) {
        }
    }

    public static class LayoutParams extends android.support.v7.app.ActionBar.LayoutParams {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType;

        public LayoutParams(@NonNull Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mViewType = 0;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.mViewType = 0;
            this.gravity = 8388627;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.mViewType = 0;
            this.gravity = gravity;
        }

        public LayoutParams(int gravity) {
            this(-2, -1, gravity);
        }

        public LayoutParams(LayoutParams source) {
            super((android.support.v7.app.ActionBar.LayoutParams) source);
            this.mViewType = 0;
            this.mViewType = source.mViewType;
        }

        public LayoutParams(android.support.v7.app.ActionBar.LayoutParams source) {
            super(source);
            this.mViewType = 0;
        }

        public LayoutParams(MarginLayoutParams source) {
            super((android.view.ViewGroup.LayoutParams) source);
            this.mViewType = 0;
            copyMarginsFromCompat(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            this.mViewType = 0;
        }

        void copyMarginsFromCompat(MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new C02261();
        int expandedMenuItemId;
        boolean isOverflowOpen;

        /* renamed from: android.support.v7.widget.Toolbar$SavedState$1 */
        static class C02261 implements ClassLoaderCreator<SavedState> {
            C02261() {
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        public SavedState(Parcel source) {
            this(source, null);
        }

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.expandedMenuItemId = source.readInt();
            this.isOverflowOpen = source.readInt() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.expandedMenuItemId);
            out.writeInt(this.isOverflowOpen);
        }
    }

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, C0185R.attr.toolbarStyle);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        int i;
        super(context, attrs, defStyleAttr);
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList();
        this.mHiddenViews = new ArrayList();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new C02871();
        this.mShowOverflowMenuRunnable = new C02242();
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs, C0185R.styleable.Toolbar, defStyleAttr, 0);
        this.mTitleTextAppearance = a.getResourceId(C0185R.styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = a.getResourceId(C0185R.styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = a.getInteger(C0185R.styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = a.getInteger(C0185R.styleable.Toolbar_buttonGravity, 48);
        int titleMargin = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_titleMargin, 0);
        if (a.hasValue(C0185R.styleable.Toolbar_titleMargins)) {
            titleMargin = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_titleMargins, titleMargin);
        }
        r0.mTitleMarginBottom = titleMargin;
        r0.mTitleMarginTop = titleMargin;
        r0.mTitleMarginEnd = titleMargin;
        r0.mTitleMarginStart = titleMargin;
        int marginStart = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_titleMarginStart, -1);
        if (marginStart >= 0) {
            r0.mTitleMarginStart = marginStart;
        }
        int marginEnd = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_titleMarginEnd, -1);
        if (marginEnd >= 0) {
            r0.mTitleMarginEnd = marginEnd;
        }
        int marginTop = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_titleMarginTop, -1);
        if (marginTop >= 0) {
            r0.mTitleMarginTop = marginTop;
        }
        int marginBottom = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_titleMarginBottom, -1);
        if (marginBottom >= 0) {
            r0.mTitleMarginBottom = marginBottom;
        }
        r0.mMaxButtonHeight = a.getDimensionPixelSize(C0185R.styleable.Toolbar_maxButtonHeight, -1);
        int contentInsetStart = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        int contentInsetEnd = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        int contentInsetLeft = a.getDimensionPixelSize(C0185R.styleable.Toolbar_contentInsetLeft, 0);
        int contentInsetRight = a.getDimensionPixelSize(C0185R.styleable.Toolbar_contentInsetRight, 0);
        ensureContentInsets();
        r0.mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);
        if (!(contentInsetStart == Integer.MIN_VALUE && contentInsetEnd == Integer.MIN_VALUE)) {
            r0.mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
        }
        r0.mContentInsetStartWithNavigation = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
        r0.mContentInsetEndWithActions = a.getDimensionPixelOffset(C0185R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
        r0.mCollapseIcon = a.getDrawable(C0185R.styleable.Toolbar_collapseIcon);
        r0.mCollapseDescription = a.getText(C0185R.styleable.Toolbar_collapseContentDescription);
        CharSequence title = a.getText(C0185R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        CharSequence subtitle = a.getText(C0185R.styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(subtitle)) {
            setSubtitle(subtitle);
        }
        r0.mPopupContext = getContext();
        setPopupTheme(a.getResourceId(C0185R.styleable.Toolbar_popupTheme, 0));
        Drawable navIcon = a.getDrawable(C0185R.styleable.Toolbar_navigationIcon);
        if (navIcon != null) {
            setNavigationIcon(navIcon);
        }
        CharSequence navDesc = a.getText(C0185R.styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(navDesc)) {
            setNavigationContentDescription(navDesc);
        }
        navIcon = a.getDrawable(C0185R.styleable.Toolbar_logo);
        if (navIcon != null) {
            setLogo(navIcon);
        }
        CharSequence logoDesc = a.getText(C0185R.styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(logoDesc)) {
            setLogoDescription(logoDesc);
        }
        if (a.hasValue(C0185R.styleable.Toolbar_titleTextColor)) {
            i = -1;
            setTitleTextColor(a.getColor(C0185R.styleable.Toolbar_titleTextColor, -1));
        } else {
            i = -1;
        }
        if (a.hasValue(C0185R.styleable.Toolbar_subtitleTextColor)) {
            setSubtitleTextColor(a.getColor(C0185R.styleable.Toolbar_subtitleTextColor, i));
        }
        a.recycle();
    }

    public void setPopupTheme(@StyleRes int resId) {
        if (this.mPopupTheme != resId) {
            this.mPopupTheme = resId;
            if (resId == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), resId);
            }
        }
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    public void setTitleMargin(int start, int top, int end, int bottom) {
        this.mTitleMarginStart = start;
        this.mTitleMarginTop = top;
        this.mTitleMarginEnd = end;
        this.mTitleMarginBottom = bottom;
        requestLayout();
    }

    public int getTitleMarginStart() {
        return this.mTitleMarginStart;
    }

    public void setTitleMarginStart(int margin) {
        this.mTitleMarginStart = margin;
        requestLayout();
    }

    public int getTitleMarginTop() {
        return this.mTitleMarginTop;
    }

    public void setTitleMarginTop(int margin) {
        this.mTitleMarginTop = margin;
        requestLayout();
    }

    public int getTitleMarginEnd() {
        return this.mTitleMarginEnd;
    }

    public void setTitleMarginEnd(int margin) {
        this.mTitleMarginEnd = margin;
        requestLayout();
    }

    public int getTitleMarginBottom() {
        return this.mTitleMarginBottom;
    }

    public void setTitleMarginBottom(int margin) {
        this.mTitleMarginBottom = margin;
        requestLayout();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        if (VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(layoutDirection);
        }
        ensureContentInsets();
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        boolean z = true;
        if (layoutDirection != 1) {
            z = false;
        }
        rtlSpacingHelper.setDirection(z);
    }

    public void setLogo(@DrawableRes int resId) {
        setLogo(AppCompatResources.getDrawable(getContext(), resId));
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean canShowOverflowMenu() {
        if (getVisibility() == 0) {
            ActionMenuView actionMenuView = this.mMenuView;
            if (actionMenuView != null && actionMenuView.isOverflowReserved()) {
                return true;
            }
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowing();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isOverflowMenuShowPending() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowPending();
    }

    public boolean showOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.hideOverflowMenu();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setMenu(MenuBuilder menu, ActionMenuPresenter outerPresenter) {
        if (menu != null || this.mMenuView != null) {
            ensureMenuView();
            MenuBuilder oldMenu = this.mMenuView.peekMenu();
            if (oldMenu != menu) {
                if (oldMenu != null) {
                    oldMenu.removeMenuPresenter(this.mOuterActionMenuPresenter);
                    oldMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
                }
                if (this.mExpandedMenuPresenter == null) {
                    this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
                }
                outerPresenter.setExpandedActionViewsExclusive(true);
                if (menu != null) {
                    menu.addMenuPresenter(outerPresenter, this.mPopupContext);
                    menu.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
                } else {
                    outerPresenter.initForMenu(this.mPopupContext, null);
                    this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
                    outerPresenter.updateMenuView(true);
                    this.mExpandedMenuPresenter.updateMenuView(true);
                }
                this.mMenuView.setPopupTheme(this.mPopupTheme);
                this.mMenuView.setPresenter(outerPresenter);
                this.mOuterActionMenuPresenter = outerPresenter;
            }
        }
    }

    public void dismissPopupMenus() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.dismissPopupMenus();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isTitleTruncated() {
        Layout titleLayout = this.mTitleTextView;
        if (titleLayout == null) {
            return false;
        }
        titleLayout = titleLayout.getLayout();
        if (titleLayout == null) {
            return false;
        }
        int lineCount = titleLayout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            if (titleLayout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }

    public void setLogo(Drawable drawable) {
        if (drawable != null) {
            ensureLogoView();
            if (!isChildOrHidden(this.mLogoView)) {
                addSystemView(this.mLogoView, true);
            }
        } else {
            View view = this.mLogoView;
            if (view != null && isChildOrHidden(view)) {
                removeView(this.mLogoView);
                this.mHiddenViews.remove(this.mLogoView);
            }
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    public Drawable getLogo() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getDrawable() : null;
    }

    public void setLogoDescription(@StringRes int resId) {
        setLogoDescription(getContext().getText(resId));
    }

    public void setLogoDescription(CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureLogoView();
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setContentDescription(description);
        }
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getContentDescription() : null;
    }

    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new AppCompatImageView(getContext());
        }
    }

    public boolean hasExpandedActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        return (expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null) ? false : true;
    }

    public void collapseActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl item = expandedActionViewMenuPresenter == null ? null : expandedActionViewMenuPresenter.mCurrentExpandedItem;
        if (item != null) {
            item.collapseActionView();
        }
    }

    public CharSequence getTitle() {
        return this.mTitleText;
    }

    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            View view = this.mTitleTextView;
            if (view != null && isChildOrHidden(view)) {
                removeView(this.mTitleTextView);
                this.mHiddenViews.remove(this.mTitleTextView);
            }
        } else {
            if (this.mTitleTextView == null) {
                Context context = getContext();
                this.mTitleTextView = new AppCompatTextView(context);
                this.mTitleTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TruncateAt.END);
                int i = this.mTitleTextAppearance;
                if (i != 0) {
                    this.mTitleTextView.setTextAppearance(context, i);
                }
                i = this.mTitleTextColor;
                if (i != 0) {
                    this.mTitleTextView.setTextColor(i);
                }
            }
            if (!isChildOrHidden(this.mTitleTextView)) {
                addSystemView(this.mTitleTextView, true);
            }
        }
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setText(title);
        }
        this.mTitleText = title;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }

    public void setSubtitle(@StringRes int resId) {
        setSubtitle(getContext().getText(resId));
    }

    public void setSubtitle(CharSequence subtitle) {
        if (TextUtils.isEmpty(subtitle)) {
            View view = this.mSubtitleTextView;
            if (view != null && isChildOrHidden(view)) {
                removeView(this.mSubtitleTextView);
                this.mHiddenViews.remove(this.mSubtitleTextView);
            }
        } else {
            if (this.mSubtitleTextView == null) {
                Context context = getContext();
                this.mSubtitleTextView = new AppCompatTextView(context);
                this.mSubtitleTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TruncateAt.END);
                int i = this.mSubtitleTextAppearance;
                if (i != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, i);
                }
                i = this.mSubtitleTextColor;
                if (i != 0) {
                    this.mSubtitleTextView.setTextColor(i);
                }
            }
            if (!isChildOrHidden(this.mSubtitleTextView)) {
                addSystemView(this.mSubtitleTextView, true);
            }
        }
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setText(subtitle);
        }
        this.mSubtitleText = subtitle;
    }

    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        this.mTitleTextAppearance = resId;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, resId);
        }
    }

    public void setSubtitleTextAppearance(Context context, @StyleRes int resId) {
        this.mSubtitleTextAppearance = resId;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, resId);
        }
    }

    public void setTitleTextColor(@ColorInt int color) {
        this.mTitleTextColor = color;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public void setSubtitleTextColor(@ColorInt int color) {
        this.mSubtitleTextColor = color;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    @Nullable
    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getContentDescription() : null;
    }

    public void setNavigationContentDescription(@StringRes int resId) {
        setNavigationContentDescription(resId != 0 ? getContext().getText(resId) : null);
    }

    public void setNavigationContentDescription(@Nullable CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureNavButtonView();
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(description);
        }
    }

    public void setNavigationIcon(@DrawableRes int resId) {
        setNavigationIcon(AppCompatResources.getDrawable(getContext(), resId));
    }

    public void setNavigationIcon(@Nullable Drawable icon) {
        if (icon != null) {
            ensureNavButtonView();
            if (!isChildOrHidden(this.mNavButtonView)) {
                addSystemView(this.mNavButtonView, true);
            }
        } else {
            View view = this.mNavButtonView;
            if (view != null && isChildOrHidden(view)) {
                removeView(this.mNavButtonView);
                this.mHiddenViews.remove(this.mNavButtonView);
            }
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setImageDrawable(icon);
        }
    }

    @Nullable
    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getDrawable() : null;
    }

    public void setNavigationOnClickListener(OnClickListener listener) {
        ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(listener);
    }

    public Menu getMenu() {
        ensureMenu();
        return this.mMenuView.getMenu();
    }

    public void setOverflowIcon(@Nullable Drawable icon) {
        ensureMenu();
        this.mMenuView.setOverflowIcon(icon);
    }

    @Nullable
    public Drawable getOverflowIcon() {
        ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }

    private void ensureMenu() {
        ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            MenuBuilder menu = (MenuBuilder) this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menu.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (this.mMenuView == null) {
            this.mMenuView = new ActionMenuView(getContext());
            this.mMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = GravityCompat.END | (this.mButtonGravity & 112);
            this.mMenuView.setLayoutParams(lp);
            addSystemView(this.mMenuView, false);
        }
    }

    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(getContext());
    }

    public void inflateMenu(@MenuRes int resId) {
        getMenuInflater().inflate(resId, getMenu());
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    public void setContentInsetsRelative(int contentInsetStart, int contentInsetEnd) {
        ensureContentInsets();
        this.mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
    }

    public int getContentInsetStart() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getStart() : 0;
    }

    public int getContentInsetEnd() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getEnd() : 0;
    }

    public void setContentInsetsAbsolute(int contentInsetLeft, int contentInsetRight) {
        ensureContentInsets();
        this.mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);
    }

    public int getContentInsetLeft() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getLeft() : 0;
    }

    public int getContentInsetRight() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getRight() : 0;
    }

    public int getContentInsetStartWithNavigation() {
        int i = this.mContentInsetStartWithNavigation;
        return i != Integer.MIN_VALUE ? i : getContentInsetStart();
    }

    public void setContentInsetStartWithNavigation(int insetStartWithNavigation) {
        if (insetStartWithNavigation < 0) {
            insetStartWithNavigation = Integer.MIN_VALUE;
        }
        if (insetStartWithNavigation != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = insetStartWithNavigation;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public int getContentInsetEndWithActions() {
        int i = this.mContentInsetEndWithActions;
        return i != Integer.MIN_VALUE ? i : getContentInsetEnd();
    }

    public void setContentInsetEndWithActions(int insetEndWithActions) {
        if (insetEndWithActions < 0) {
            insetEndWithActions = Integer.MIN_VALUE;
        }
        if (insetEndWithActions != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = insetEndWithActions;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public int getCurrentContentInsetStart() {
        if (getNavigationIcon() != null) {
            return Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
        }
        return getContentInsetStart();
    }

    public int getCurrentContentInsetEnd() {
        boolean hasActions = false;
        MenuBuilder mb = this.mMenuView;
        if (mb != null) {
            mb = mb.peekMenu();
            boolean z = mb != null && mb.hasVisibleItems();
            hasActions = z;
        }
        if (hasActions) {
            return Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
        }
        return getContentInsetEnd();
    }

    public int getCurrentContentInsetLeft() {
        if (ViewCompat.getLayoutDirection(this) == 1) {
            return getCurrentContentInsetEnd();
        }
        return getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        if (ViewCompat.getLayoutDirection(this) == 1) {
            return getCurrentContentInsetStart();
        }
        return getCurrentContentInsetEnd();
    }

    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(getContext(), null, C0185R.attr.toolbarNavigationButtonStyle);
            LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = GravityCompat.START | (this.mButtonGravity & 112);
            this.mNavButtonView.setLayoutParams(lp);
        }
    }

    void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            this.mCollapseButtonView = new AppCompatImageButton(getContext(), null, C0185R.attr.toolbarNavigationButtonStyle);
            this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = GravityCompat.START | (this.mButtonGravity & 112);
            lp.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams(lp);
            this.mCollapseButtonView.setOnClickListener(new C02253());
        }
    }

    private void addSystemView(View v, boolean allowHide) {
        LayoutParams lp;
        android.view.ViewGroup.LayoutParams vlp = v.getLayoutParams();
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (checkLayoutParams(vlp)) {
            lp = (LayoutParams) vlp;
        } else {
            lp = generateLayoutParams(vlp);
        }
        lp.mViewType = 1;
        if (!allowHide || this.mExpandedActionView == null) {
            addView(v, lp);
            return;
        }
        v.setLayoutParams(lp);
        this.mHiddenViews.add(v);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (!(expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null)) {
            state.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        state.isOverflowOpen = isOverflowMenuShowing();
        return state;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            ActionMenuView actionMenuView = this.mMenuView;
            Menu menu = actionMenuView != null ? actionMenuView.peekMenu() : null;
            if (!(ss.expandedMenuItemId == 0 || this.mExpandedMenuPresenter == null || menu == null)) {
                MenuItem item = menu.findItem(ss.expandedMenuItemId);
                if (item != null) {
                    item.expandActionView();
                }
            }
            if (ss.isOverflowOpen) {
                postShowOverflowMenu();
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private void postShowOverflowMenu() {
        removeCallbacks(this.mShowOverflowMenuRunnable);
        post(this.mShowOverflowMenuRunnable);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mShowOverflowMenuRunnable);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            boolean handled = super.onTouchEvent(ev);
            if (action == 0 && !handled) {
                this.mEatingTouch = true;
            }
        }
        if (action == 1 || action == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }

    public boolean onHoverEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            boolean handled = super.onHoverEvent(ev);
            if (action == 9 && !handled) {
                this.mEatingHover = true;
            }
        }
        if (action == 10 || action == 3) {
            this.mEatingHover = false;
        }
        return true;
    }

    private void measureChildConstrained(View child, int parentWidthSpec, int widthUsed, int parentHeightSpec, int heightUsed, int heightConstraint) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int childWidthSpec = getChildMeasureSpec(parentWidthSpec, (((getPaddingLeft() + getPaddingRight()) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width);
        int childHeightSpec = getChildMeasureSpec(parentHeightSpec, (((getPaddingTop() + getPaddingBottom()) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height);
        int childHeightMode = MeasureSpec.getMode(childHeightSpec);
        if (childHeightMode != 1073741824 && heightConstraint >= 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(childHeightMode != 0 ? Math.min(MeasureSpec.getSize(childHeightSpec), heightConstraint) : heightConstraint, 1073741824);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private int measureChildCollapseMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed, int[] collapsingMargins) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int leftDiff = lp.leftMargin - collapsingMargins[0];
        int rightDiff = lp.rightMargin - collapsingMargins[1];
        int hMargins = Math.max(0, leftDiff) + Math.max(0, rightDiff);
        collapsingMargins[0] = Math.max(0, -leftDiff);
        collapsingMargins[1] = Math.max(0, -rightDiff);
        child.measure(getChildMeasureSpec(parentWidthMeasureSpec, ((getPaddingLeft() + getPaddingRight()) + hMargins) + widthUsed, lp.width), getChildMeasureSpec(parentHeightMeasureSpec, (((getPaddingTop() + getPaddingBottom()) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height));
        return child.getMeasuredWidth() + hMargins;
    }

    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (shouldLayout(child) && child.getMeasuredWidth() > 0 && child.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int marginStartIndex;
        int marginEndIndex;
        int childState;
        int menuWidth;
        int titleWidth;
        int height = 0;
        int childState2 = 0;
        int[] collapsingMargins = this.mTempMargins;
        if (ViewUtils.isLayoutRtl(this)) {
            marginStartIndex = 1;
            marginEndIndex = 0;
        } else {
            marginStartIndex = 0;
            marginEndIndex = 1;
        }
        int navWidth = 0;
        if (shouldLayout(r7.mNavButtonView)) {
            measureChildConstrained(r7.mNavButtonView, widthMeasureSpec, 0, heightMeasureSpec, 0, r7.mMaxButtonHeight);
            navWidth = r7.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(r7.mNavButtonView);
            height = Math.max(0, r7.mNavButtonView.getMeasuredHeight() + getVerticalMargins(r7.mNavButtonView));
            childState2 = View.combineMeasuredStates(0, r7.mNavButtonView.getMeasuredState());
        }
        if (shouldLayout(r7.mCollapseButtonView)) {
            measureChildConstrained(r7.mCollapseButtonView, widthMeasureSpec, 0, heightMeasureSpec, 0, r7.mMaxButtonHeight);
            navWidth = r7.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(r7.mCollapseButtonView);
            height = Math.max(height, r7.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(r7.mCollapseButtonView));
            childState2 = View.combineMeasuredStates(childState2, r7.mCollapseButtonView.getMeasuredState());
        }
        int contentInsetStart = getCurrentContentInsetStart();
        int width = 0 + Math.max(contentInsetStart, navWidth);
        collapsingMargins[marginStartIndex] = Math.max(0, contentInsetStart - navWidth);
        if (shouldLayout(r7.mMenuView)) {
            marginStartIndex = 0;
            measureChildConstrained(r7.mMenuView, widthMeasureSpec, width, heightMeasureSpec, 0, r7.mMaxButtonHeight);
            int menuWidth2 = r7.mMenuView.getMeasuredWidth() + getHorizontalMargins(r7.mMenuView);
            height = Math.max(height, r7.mMenuView.getMeasuredHeight() + getVerticalMargins(r7.mMenuView));
            childState = View.combineMeasuredStates(childState2, r7.mMenuView.getMeasuredState());
            childState2 = height;
            height = menuWidth2;
        } else {
            marginStartIndex = 0;
            childState = childState2;
            childState2 = height;
            height = 0;
        }
        int contentInsetEnd = getCurrentContentInsetEnd();
        width += Math.max(contentInsetEnd, height);
        collapsingMargins[marginEndIndex] = Math.max(marginStartIndex, contentInsetEnd - height);
        if (shouldLayout(r7.mExpandedActionView)) {
            marginStartIndex = childState;
            width += measureChildCollapseMargins(r7.mExpandedActionView, widthMeasureSpec, width, heightMeasureSpec, 0, collapsingMargins);
            childState2 = Math.max(childState2, r7.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(r7.mExpandedActionView));
            marginStartIndex = View.combineMeasuredStates(marginStartIndex, r7.mExpandedActionView.getMeasuredState());
        } else {
            marginStartIndex = childState;
        }
        if (shouldLayout(r7.mLogoView)) {
            width += measureChildCollapseMargins(r7.mLogoView, widthMeasureSpec, width, heightMeasureSpec, 0, collapsingMargins);
            childState2 = Math.max(childState2, r7.mLogoView.getMeasuredHeight() + getVerticalMargins(r7.mLogoView));
            marginStartIndex = View.combineMeasuredStates(marginStartIndex, r7.mLogoView.getMeasuredState());
        }
        childState = getChildCount();
        contentInsetEnd = childState2;
        childState2 = width;
        width = 0;
        while (width < childState) {
            int childCount;
            View child = getChildAt(width);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            View view;
            if (lp.mViewType != 0) {
                view = child;
                childCount = childState;
                menuWidth = height;
                height = contentInsetEnd;
            } else if (shouldLayout(child)) {
                View child2 = child;
                menuWidth = height;
                childCount = childState;
                childState2 += measureChildCollapseMargins(child, widthMeasureSpec, childState2, heightMeasureSpec, 0, collapsingMargins);
                view = child2;
                contentInsetEnd = Math.max(contentInsetEnd, child2.getMeasuredHeight() + getVerticalMargins(view));
                marginStartIndex = View.combineMeasuredStates(marginStartIndex, view.getMeasuredState());
                width++;
                childState = childCount;
                height = menuWidth;
            } else {
                childCount = childState;
                menuWidth = height;
                height = contentInsetEnd;
            }
            contentInsetEnd = height;
            width++;
            childState = childCount;
            height = menuWidth;
        }
        menuWidth = height;
        height = contentInsetEnd;
        width = 0;
        int titleHeight = 0;
        int titleVertMargins = r7.mTitleMarginTop + r7.mTitleMarginBottom;
        int titleHorizMargins = r7.mTitleMarginStart + r7.mTitleMarginEnd;
        if (shouldLayout(r7.mTitleTextView)) {
            titleWidth = measureChildCollapseMargins(r7.mTitleTextView, widthMeasureSpec, childState2 + titleHorizMargins, heightMeasureSpec, titleVertMargins, collapsingMargins);
            width = r7.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(r7.mTitleTextView);
            titleHeight = r7.mTitleTextView.getMeasuredHeight() + getVerticalMargins(r7.mTitleTextView);
            marginStartIndex = View.combineMeasuredStates(marginStartIndex, r7.mTitleTextView.getMeasuredState());
        }
        if (shouldLayout(r7.mSubtitleTextView)) {
            width = Math.max(width, measureChildCollapseMargins(r7.mSubtitleTextView, widthMeasureSpec, childState2 + titleHorizMargins, heightMeasureSpec, titleHeight + titleVertMargins, collapsingMargins));
            titleHeight += r7.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(r7.mSubtitleTextView);
            marginStartIndex = View.combineMeasuredStates(marginStartIndex, r7.mSubtitleTextView.getMeasuredState());
            titleWidth = titleHeight;
        } else {
            titleWidth = titleHeight;
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max((childState2 + width) + (getPaddingLeft() + getPaddingRight()), getSuggestedMinimumWidth()), widthMeasureSpec, ViewCompat.MEASURED_STATE_MASK & marginStartIndex), shouldCollapse() ? 0 : View.resolveSizeAndState(Math.max(Math.max(height, titleWidth) + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight()), heightMeasureSpec, marginStartIndex << 16));
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingRight;
        LayoutParams lp;
        int left;
        int width;
        int paddingLeft;
        int alignmentHeight;
        int leftViewsCount;
        boolean titleHasWidth;
        LayoutParams layoutParams;
        Toolbar toolbar = this;
        boolean isRtl = ViewCompat.getLayoutDirection(this) == 1;
        int width2 = getWidth();
        int height = getHeight();
        int paddingLeft2 = getPaddingLeft();
        int paddingRight2 = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int left2 = paddingLeft2;
        int right = width2 - paddingRight2;
        int[] collapsingMargins = toolbar.mTempMargins;
        collapsingMargins[1] = 0;
        collapsingMargins[0] = 0;
        int minHeight = ViewCompat.getMinimumHeight(this);
        int alignmentHeight2 = minHeight >= 0 ? Math.min(minHeight, b - t) : 0;
        if (shouldLayout(toolbar.mNavButtonView)) {
            if (isRtl) {
                right = layoutChildRight(toolbar.mNavButtonView, right, collapsingMargins, alignmentHeight2);
            } else {
                left2 = layoutChildLeft(toolbar.mNavButtonView, left2, collapsingMargins, alignmentHeight2);
            }
        }
        if (shouldLayout(toolbar.mCollapseButtonView)) {
            if (isRtl) {
                right = layoutChildRight(toolbar.mCollapseButtonView, right, collapsingMargins, alignmentHeight2);
            } else {
                left2 = layoutChildLeft(toolbar.mCollapseButtonView, left2, collapsingMargins, alignmentHeight2);
            }
        }
        if (shouldLayout(toolbar.mMenuView)) {
            if (isRtl) {
                left2 = layoutChildLeft(toolbar.mMenuView, left2, collapsingMargins, alignmentHeight2);
            } else {
                right = layoutChildRight(toolbar.mMenuView, right, collapsingMargins, alignmentHeight2);
            }
        }
        int contentInsetLeft = getCurrentContentInsetLeft();
        int contentInsetRight = getCurrentContentInsetRight();
        collapsingMargins[0] = Math.max(0, contentInsetLeft - left2);
        collapsingMargins[1] = Math.max(0, contentInsetRight - ((width2 - paddingRight2) - right));
        int left3 = Math.max(left2, contentInsetLeft);
        left2 = Math.min(right, (width2 - paddingRight2) - contentInsetRight);
        if (shouldLayout(toolbar.mExpandedActionView)) {
            if (isRtl) {
                left2 = layoutChildRight(toolbar.mExpandedActionView, left2, collapsingMargins, alignmentHeight2);
            } else {
                left3 = layoutChildLeft(toolbar.mExpandedActionView, left3, collapsingMargins, alignmentHeight2);
            }
        }
        if (shouldLayout(toolbar.mLogoView)) {
            if (isRtl) {
                left2 = layoutChildRight(toolbar.mLogoView, left2, collapsingMargins, alignmentHeight2);
            } else {
                left3 = layoutChildLeft(toolbar.mLogoView, left3, collapsingMargins, alignmentHeight2);
            }
        }
        boolean layoutTitle = shouldLayout(toolbar.mTitleTextView);
        boolean layoutSubtitle = shouldLayout(toolbar.mSubtitleTextView);
        int titleHeight = 0;
        if (layoutTitle) {
            LayoutParams minHeight2 = (LayoutParams) toolbar.mTitleTextView.getLayoutParams();
            paddingRight = paddingRight2;
            titleHeight = 0 + ((minHeight2.topMargin + toolbar.mTitleTextView.getMeasuredHeight()) + minHeight2.bottomMargin);
        } else {
            paddingRight = paddingRight2;
            int i = minHeight;
            int i2 = contentInsetLeft;
        }
        if (layoutSubtitle) {
            lp = (LayoutParams) toolbar.mSubtitleTextView.getLayoutParams();
            titleHeight += (lp.topMargin + toolbar.mSubtitleTextView.getMeasuredHeight()) + lp.bottomMargin;
        }
        if (!layoutTitle) {
            if (!layoutSubtitle) {
                boolean z = isRtl;
                left = left3;
                width = width2;
                int i3 = height;
                paddingLeft = paddingLeft2;
                alignmentHeight = alignmentHeight2;
                addCustomViewsWithGravity(toolbar.mTempViews, 3);
                leftViewsCount = toolbar.mTempViews.size();
                width2 = left;
                for (left3 = 0; left3 < leftViewsCount; left3++) {
                    width2 = layoutChildLeft((View) toolbar.mTempViews.get(left3), width2, collapsingMargins, alignmentHeight);
                }
                paddingLeft2 = alignmentHeight;
                addCustomViewsWithGravity(toolbar.mTempViews, 5);
                left3 = toolbar.mTempViews.size();
                for (height = 0; height < left3; height++) {
                    left2 = layoutChildRight((View) toolbar.mTempViews.get(height), left2, collapsingMargins, paddingLeft2);
                }
                addCustomViewsWithGravity(toolbar.mTempViews, 1);
                height = getViewListMeasuredWidth(toolbar.mTempViews, collapsingMargins);
                alignmentHeight2 = (paddingLeft + (((width - paddingLeft) - paddingRight) / 2)) - (height / 2);
                contentInsetLeft = alignmentHeight2 + height;
                if (alignmentHeight2 >= width2) {
                    alignmentHeight2 = width2;
                } else if (contentInsetLeft > left2) {
                    alignmentHeight2 -= contentInsetLeft - left2;
                }
                leftViewsCount = toolbar.mTempViews.size();
                left3 = alignmentHeight2;
                alignmentHeight2 = 0;
                while (alignmentHeight2 < leftViewsCount) {
                    int centerViewsCount = leftViewsCount;
                    left3 = layoutChildLeft((View) toolbar.mTempViews.get(alignmentHeight2), left3, collapsingMargins, paddingLeft2);
                    alignmentHeight2++;
                    leftViewsCount = centerViewsCount;
                }
                toolbar.mTempViews.clear();
            }
        }
        View topChild = layoutTitle ? toolbar.mTitleTextView : toolbar.mSubtitleTextView;
        View bottomChild = layoutSubtitle ? toolbar.mSubtitleTextView : toolbar.mTitleTextView;
        LayoutParams toplp = (LayoutParams) topChild.getLayoutParams();
        lp = (LayoutParams) bottomChild.getLayoutParams();
        if (layoutTitle) {
            if (toolbar.mTitleTextView.getMeasuredWidth() <= 0) {
            }
            titleHasWidth = true;
            width = width2;
            width2 = toolbar.mGravity & 112;
            paddingLeft = paddingLeft2;
            if (width2 != 48) {
                left = left3;
                alignmentHeight = alignmentHeight2;
                left3 = (getPaddingTop() + toplp.topMargin) + toolbar.mTitleMarginTop;
            } else if (width2 != 80) {
                width2 = (height - paddingTop) - paddingBottom;
                paddingLeft2 = (width2 - titleHeight) / 2;
                alignmentHeight = alignmentHeight2;
                if (paddingLeft2 < toplp.topMargin + toolbar.mTitleMarginTop) {
                    paddingLeft2 = toplp.topMargin + toolbar.mTitleMarginTop;
                    left = left3;
                } else {
                    width2 = (((height - paddingBottom) - titleHeight) - paddingLeft2) - paddingTop;
                    left = left3;
                    if (width2 >= toplp.bottomMargin + toolbar.mTitleMarginBottom) {
                        paddingLeft2 = Math.max(0, paddingLeft2 - ((lp.bottomMargin + toolbar.mTitleMarginBottom) - width2));
                    }
                }
                left3 = paddingTop + paddingLeft2;
            } else {
                left = left3;
                alignmentHeight = alignmentHeight2;
                width2 = 0;
                paddingLeft2 = 0;
                int i4 = 0;
                left3 = (((height - paddingBottom) - lp.bottomMargin) - toolbar.mTitleMarginBottom) - titleHeight;
            }
            if (isRtl) {
                width2 = (titleHasWidth ? toolbar.mTitleMarginStart : 0) - collapsingMargins[1];
                left2 -= Math.max(0, width2);
                collapsingMargins[1] = Math.max(0, -width2);
                isRtl = left2;
                paddingLeft2 = left2;
                if (layoutTitle) {
                    LayoutParams lp2 = (LayoutParams) toolbar.mTitleTextView.getLayoutParams();
                    width2 = isRtl - toolbar.mTitleTextView.getMeasuredWidth();
                    height = toolbar.mTitleTextView.getMeasuredHeight() + left3;
                    toolbar.mTitleTextView.layout(width2, left3, isRtl, height);
                    isRtl = width2 - toolbar.mTitleMarginEnd;
                    left3 = height + lp2.bottomMargin;
                } else {
                    i3 = height;
                    layoutParams = lp;
                }
                if (layoutSubtitle) {
                    LayoutParams lp3 = (LayoutParams) toolbar.mSubtitleTextView.getLayoutParams();
                    left3 += lp3.topMargin;
                    lp = toolbar.mSubtitleTextView.getMeasuredHeight() + left3;
                    toolbar.mSubtitleTextView.layout(paddingLeft2 - toolbar.mSubtitleTextView.getMeasuredWidth(), left3, paddingLeft2, lp);
                    paddingLeft2 -= toolbar.mTitleMarginEnd;
                    left3 = lp + lp3.bottomMargin;
                }
                if (titleHasWidth) {
                    left2 = Math.min(isRtl, paddingLeft2);
                }
            } else {
                i3 = height;
                layoutParams = lp;
                leftViewsCount = (titleHasWidth ? toolbar.mTitleMarginStart : 0) - collapsingMargins[0];
                height = left + Math.max(0, leftViewsCount);
                collapsingMargins[0] = Math.max(0, -leftViewsCount);
                width2 = height;
                paddingLeft2 = height;
                if (layoutTitle) {
                    lp = (LayoutParams) toolbar.mTitleTextView.getLayoutParams();
                    alignmentHeight2 = toolbar.mTitleTextView.getMeasuredWidth() + width2;
                    leftViewsCount = toolbar.mTitleTextView.getMeasuredHeight() + left3;
                    left = height;
                    toolbar.mTitleTextView.layout(width2, left3, alignmentHeight2, leftViewsCount);
                    width2 = alignmentHeight2 + toolbar.mTitleMarginEnd;
                    left3 = leftViewsCount + lp.bottomMargin;
                } else {
                    left = height;
                }
                if (layoutSubtitle) {
                    LayoutParams lp4 = (LayoutParams) toolbar.mSubtitleTextView.getLayoutParams();
                    left3 += lp4.topMargin;
                    height = toolbar.mSubtitleTextView.getMeasuredWidth() + paddingLeft2;
                    paddingRight2 = toolbar.mSubtitleTextView.getMeasuredHeight() + left3;
                    toolbar.mSubtitleTextView.layout(paddingLeft2, left3, height, paddingRight2);
                    paddingLeft2 = height + toolbar.mTitleMarginEnd;
                    left3 = paddingRight2 + lp4.bottomMargin;
                }
                if (titleHasWidth) {
                    left = Math.max(width2, paddingLeft2);
                }
            }
            addCustomViewsWithGravity(toolbar.mTempViews, 3);
            leftViewsCount = toolbar.mTempViews.size();
            width2 = left;
            for (left3 = 0; left3 < leftViewsCount; left3++) {
                width2 = layoutChildLeft((View) toolbar.mTempViews.get(left3), width2, collapsingMargins, alignmentHeight);
            }
            paddingLeft2 = alignmentHeight;
            addCustomViewsWithGravity(toolbar.mTempViews, 5);
            left3 = toolbar.mTempViews.size();
            for (height = 0; height < left3; height++) {
                left2 = layoutChildRight((View) toolbar.mTempViews.get(height), left2, collapsingMargins, paddingLeft2);
            }
            addCustomViewsWithGravity(toolbar.mTempViews, 1);
            height = getViewListMeasuredWidth(toolbar.mTempViews, collapsingMargins);
            alignmentHeight2 = (paddingLeft + (((width - paddingLeft) - paddingRight) / 2)) - (height / 2);
            contentInsetLeft = alignmentHeight2 + height;
            if (alignmentHeight2 >= width2) {
                alignmentHeight2 = width2;
            } else if (contentInsetLeft > left2) {
                alignmentHeight2 -= contentInsetLeft - left2;
            }
            leftViewsCount = toolbar.mTempViews.size();
            left3 = alignmentHeight2;
            alignmentHeight2 = 0;
            while (alignmentHeight2 < leftViewsCount) {
                int centerViewsCount2 = leftViewsCount;
                left3 = layoutChildLeft((View) toolbar.mTempViews.get(alignmentHeight2), left3, collapsingMargins, paddingLeft2);
                alignmentHeight2++;
                leftViewsCount = centerViewsCount2;
            }
            toolbar.mTempViews.clear();
        }
        if (!layoutSubtitle || toolbar.mSubtitleTextView.getMeasuredWidth() <= 0) {
            titleHasWidth = false;
            width = width2;
            width2 = toolbar.mGravity & 112;
            paddingLeft = paddingLeft2;
            if (width2 != 48) {
                left = left3;
                alignmentHeight = alignmentHeight2;
                left3 = (getPaddingTop() + toplp.topMargin) + toolbar.mTitleMarginTop;
            } else if (width2 != 80) {
                left = left3;
                alignmentHeight = alignmentHeight2;
                width2 = 0;
                paddingLeft2 = 0;
                int i42 = 0;
                left3 = (((height - paddingBottom) - lp.bottomMargin) - toolbar.mTitleMarginBottom) - titleHeight;
            } else {
                width2 = (height - paddingTop) - paddingBottom;
                paddingLeft2 = (width2 - titleHeight) / 2;
                alignmentHeight = alignmentHeight2;
                if (paddingLeft2 < toplp.topMargin + toolbar.mTitleMarginTop) {
                    width2 = (((height - paddingBottom) - titleHeight) - paddingLeft2) - paddingTop;
                    left = left3;
                    if (width2 >= toplp.bottomMargin + toolbar.mTitleMarginBottom) {
                        paddingLeft2 = Math.max(0, paddingLeft2 - ((lp.bottomMargin + toolbar.mTitleMarginBottom) - width2));
                    }
                } else {
                    paddingLeft2 = toplp.topMargin + toolbar.mTitleMarginTop;
                    left = left3;
                }
                left3 = paddingTop + paddingLeft2;
            }
            if (isRtl) {
                i3 = height;
                layoutParams = lp;
                if (titleHasWidth) {
                }
                leftViewsCount = (titleHasWidth ? toolbar.mTitleMarginStart : 0) - collapsingMargins[0];
                height = left + Math.max(0, leftViewsCount);
                collapsingMargins[0] = Math.max(0, -leftViewsCount);
                width2 = height;
                paddingLeft2 = height;
                if (layoutTitle) {
                    left = height;
                } else {
                    lp = (LayoutParams) toolbar.mTitleTextView.getLayoutParams();
                    alignmentHeight2 = toolbar.mTitleTextView.getMeasuredWidth() + width2;
                    leftViewsCount = toolbar.mTitleTextView.getMeasuredHeight() + left3;
                    left = height;
                    toolbar.mTitleTextView.layout(width2, left3, alignmentHeight2, leftViewsCount);
                    width2 = alignmentHeight2 + toolbar.mTitleMarginEnd;
                    left3 = leftViewsCount + lp.bottomMargin;
                }
                if (layoutSubtitle) {
                    LayoutParams lp42 = (LayoutParams) toolbar.mSubtitleTextView.getLayoutParams();
                    left3 += lp42.topMargin;
                    height = toolbar.mSubtitleTextView.getMeasuredWidth() + paddingLeft2;
                    paddingRight2 = toolbar.mSubtitleTextView.getMeasuredHeight() + left3;
                    toolbar.mSubtitleTextView.layout(paddingLeft2, left3, height, paddingRight2);
                    paddingLeft2 = height + toolbar.mTitleMarginEnd;
                    left3 = paddingRight2 + lp42.bottomMargin;
                }
                if (titleHasWidth) {
                    left = Math.max(width2, paddingLeft2);
                }
            } else {
                if (titleHasWidth) {
                }
                width2 = (titleHasWidth ? toolbar.mTitleMarginStart : 0) - collapsingMargins[1];
                left2 -= Math.max(0, width2);
                collapsingMargins[1] = Math.max(0, -width2);
                isRtl = left2;
                paddingLeft2 = left2;
                if (layoutTitle) {
                    i3 = height;
                    layoutParams = lp;
                } else {
                    LayoutParams lp22 = (LayoutParams) toolbar.mTitleTextView.getLayoutParams();
                    width2 = isRtl - toolbar.mTitleTextView.getMeasuredWidth();
                    height = toolbar.mTitleTextView.getMeasuredHeight() + left3;
                    toolbar.mTitleTextView.layout(width2, left3, isRtl, height);
                    isRtl = width2 - toolbar.mTitleMarginEnd;
                    left3 = height + lp22.bottomMargin;
                }
                if (layoutSubtitle) {
                    LayoutParams lp32 = (LayoutParams) toolbar.mSubtitleTextView.getLayoutParams();
                    left3 += lp32.topMargin;
                    lp = toolbar.mSubtitleTextView.getMeasuredHeight() + left3;
                    toolbar.mSubtitleTextView.layout(paddingLeft2 - toolbar.mSubtitleTextView.getMeasuredWidth(), left3, paddingLeft2, lp);
                    paddingLeft2 -= toolbar.mTitleMarginEnd;
                    left3 = lp + lp32.bottomMargin;
                }
                if (titleHasWidth) {
                    left2 = Math.min(isRtl, paddingLeft2);
                }
            }
            addCustomViewsWithGravity(toolbar.mTempViews, 3);
            leftViewsCount = toolbar.mTempViews.size();
            width2 = left;
            for (left3 = 0; left3 < leftViewsCount; left3++) {
                width2 = layoutChildLeft((View) toolbar.mTempViews.get(left3), width2, collapsingMargins, alignmentHeight);
            }
            paddingLeft2 = alignmentHeight;
            addCustomViewsWithGravity(toolbar.mTempViews, 5);
            left3 = toolbar.mTempViews.size();
            for (height = 0; height < left3; height++) {
                left2 = layoutChildRight((View) toolbar.mTempViews.get(height), left2, collapsingMargins, paddingLeft2);
            }
            addCustomViewsWithGravity(toolbar.mTempViews, 1);
            height = getViewListMeasuredWidth(toolbar.mTempViews, collapsingMargins);
            alignmentHeight2 = (paddingLeft + (((width - paddingLeft) - paddingRight) / 2)) - (height / 2);
            contentInsetLeft = alignmentHeight2 + height;
            if (alignmentHeight2 >= width2) {
                alignmentHeight2 = width2;
            } else if (contentInsetLeft > left2) {
                alignmentHeight2 -= contentInsetLeft - left2;
            }
            leftViewsCount = toolbar.mTempViews.size();
            left3 = alignmentHeight2;
            alignmentHeight2 = 0;
            while (alignmentHeight2 < leftViewsCount) {
                int centerViewsCount22 = leftViewsCount;
                left3 = layoutChildLeft((View) toolbar.mTempViews.get(alignmentHeight2), left3, collapsingMargins, paddingLeft2);
                alignmentHeight2++;
                leftViewsCount = centerViewsCount22;
            }
            toolbar.mTempViews.clear();
        }
        titleHasWidth = true;
        width = width2;
        width2 = toolbar.mGravity & 112;
        paddingLeft = paddingLeft2;
        if (width2 != 48) {
            left = left3;
            alignmentHeight = alignmentHeight2;
            left3 = (getPaddingTop() + toplp.topMargin) + toolbar.mTitleMarginTop;
        } else if (width2 != 80) {
            width2 = (height - paddingTop) - paddingBottom;
            paddingLeft2 = (width2 - titleHeight) / 2;
            alignmentHeight = alignmentHeight2;
            if (paddingLeft2 < toplp.topMargin + toolbar.mTitleMarginTop) {
                paddingLeft2 = toplp.topMargin + toolbar.mTitleMarginTop;
                left = left3;
            } else {
                width2 = (((height - paddingBottom) - titleHeight) - paddingLeft2) - paddingTop;
                left = left3;
                if (width2 >= toplp.bottomMargin + toolbar.mTitleMarginBottom) {
                    paddingLeft2 = Math.max(0, paddingLeft2 - ((lp.bottomMargin + toolbar.mTitleMarginBottom) - width2));
                }
            }
            left3 = paddingTop + paddingLeft2;
        } else {
            left = left3;
            alignmentHeight = alignmentHeight2;
            width2 = 0;
            paddingLeft2 = 0;
            int i422 = 0;
            left3 = (((height - paddingBottom) - lp.bottomMargin) - toolbar.mTitleMarginBottom) - titleHeight;
        }
        if (isRtl) {
            if (titleHasWidth) {
            }
            width2 = (titleHasWidth ? toolbar.mTitleMarginStart : 0) - collapsingMargins[1];
            left2 -= Math.max(0, width2);
            collapsingMargins[1] = Math.max(0, -width2);
            isRtl = left2;
            paddingLeft2 = left2;
            if (layoutTitle) {
                LayoutParams lp222 = (LayoutParams) toolbar.mTitleTextView.getLayoutParams();
                width2 = isRtl - toolbar.mTitleTextView.getMeasuredWidth();
                height = toolbar.mTitleTextView.getMeasuredHeight() + left3;
                toolbar.mTitleTextView.layout(width2, left3, isRtl, height);
                isRtl = width2 - toolbar.mTitleMarginEnd;
                left3 = height + lp222.bottomMargin;
            } else {
                i3 = height;
                layoutParams = lp;
            }
            if (layoutSubtitle) {
                LayoutParams lp322 = (LayoutParams) toolbar.mSubtitleTextView.getLayoutParams();
                left3 += lp322.topMargin;
                lp = toolbar.mSubtitleTextView.getMeasuredHeight() + left3;
                toolbar.mSubtitleTextView.layout(paddingLeft2 - toolbar.mSubtitleTextView.getMeasuredWidth(), left3, paddingLeft2, lp);
                paddingLeft2 -= toolbar.mTitleMarginEnd;
                left3 = lp + lp322.bottomMargin;
            }
            if (titleHasWidth) {
                left2 = Math.min(isRtl, paddingLeft2);
            }
        } else {
            i3 = height;
            layoutParams = lp;
            if (titleHasWidth) {
            }
            leftViewsCount = (titleHasWidth ? toolbar.mTitleMarginStart : 0) - collapsingMargins[0];
            height = left + Math.max(0, leftViewsCount);
            collapsingMargins[0] = Math.max(0, -leftViewsCount);
            width2 = height;
            paddingLeft2 = height;
            if (layoutTitle) {
                lp = (LayoutParams) toolbar.mTitleTextView.getLayoutParams();
                alignmentHeight2 = toolbar.mTitleTextView.getMeasuredWidth() + width2;
                leftViewsCount = toolbar.mTitleTextView.getMeasuredHeight() + left3;
                left = height;
                toolbar.mTitleTextView.layout(width2, left3, alignmentHeight2, leftViewsCount);
                width2 = alignmentHeight2 + toolbar.mTitleMarginEnd;
                left3 = leftViewsCount + lp.bottomMargin;
            } else {
                left = height;
            }
            if (layoutSubtitle) {
                LayoutParams lp422 = (LayoutParams) toolbar.mSubtitleTextView.getLayoutParams();
                left3 += lp422.topMargin;
                height = toolbar.mSubtitleTextView.getMeasuredWidth() + paddingLeft2;
                paddingRight2 = toolbar.mSubtitleTextView.getMeasuredHeight() + left3;
                toolbar.mSubtitleTextView.layout(paddingLeft2, left3, height, paddingRight2);
                paddingLeft2 = height + toolbar.mTitleMarginEnd;
                left3 = paddingRight2 + lp422.bottomMargin;
            }
            if (titleHasWidth) {
                left = Math.max(width2, paddingLeft2);
            }
        }
        addCustomViewsWithGravity(toolbar.mTempViews, 3);
        leftViewsCount = toolbar.mTempViews.size();
        width2 = left;
        for (left3 = 0; left3 < leftViewsCount; left3++) {
            width2 = layoutChildLeft((View) toolbar.mTempViews.get(left3), width2, collapsingMargins, alignmentHeight);
        }
        paddingLeft2 = alignmentHeight;
        addCustomViewsWithGravity(toolbar.mTempViews, 5);
        left3 = toolbar.mTempViews.size();
        for (height = 0; height < left3; height++) {
            left2 = layoutChildRight((View) toolbar.mTempViews.get(height), left2, collapsingMargins, paddingLeft2);
        }
        addCustomViewsWithGravity(toolbar.mTempViews, 1);
        height = getViewListMeasuredWidth(toolbar.mTempViews, collapsingMargins);
        alignmentHeight2 = (paddingLeft + (((width - paddingLeft) - paddingRight) / 2)) - (height / 2);
        contentInsetLeft = alignmentHeight2 + height;
        if (alignmentHeight2 >= width2) {
            alignmentHeight2 = width2;
        } else if (contentInsetLeft > left2) {
            alignmentHeight2 -= contentInsetLeft - left2;
        }
        leftViewsCount = toolbar.mTempViews.size();
        left3 = alignmentHeight2;
        alignmentHeight2 = 0;
        while (alignmentHeight2 < leftViewsCount) {
            int centerViewsCount222 = leftViewsCount;
            left3 = layoutChildLeft((View) toolbar.mTempViews.get(alignmentHeight2), left3, collapsingMargins, paddingLeft2);
            alignmentHeight2++;
            leftViewsCount = centerViewsCount222;
        }
        toolbar.mTempViews.clear();
    }

    private int getViewListMeasuredWidth(List<View> views, int[] collapsingMargins) {
        int collapseLeft = collapsingMargins[0];
        int collapseRight = collapsingMargins[1];
        int width = 0;
        int count = views.size();
        for (int i = 0; i < count; i++) {
            View v = (View) views.get(i);
            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            int l = lp.leftMargin - collapseLeft;
            int r = lp.rightMargin - collapseRight;
            int leftMargin = Math.max(0, l);
            int rightMargin = Math.max(0, r);
            collapseLeft = Math.max(0, -l);
            collapseRight = Math.max(0, -r);
            width += (v.getMeasuredWidth() + leftMargin) + rightMargin;
        }
        return width;
    }

    private int layoutChildLeft(View child, int left, int[] collapsingMargins, int alignmentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int l = lp.leftMargin - collapsingMargins[0];
        left += Math.max(0, l);
        collapsingMargins[0] = Math.max(0, -l);
        int top = getChildTop(child, alignmentHeight);
        int childWidth = child.getMeasuredWidth();
        child.layout(left, top, left + childWidth, child.getMeasuredHeight() + top);
        return left + (lp.rightMargin + childWidth);
    }

    private int layoutChildRight(View child, int right, int[] collapsingMargins, int alignmentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int r = lp.rightMargin - collapsingMargins[1];
        right -= Math.max(0, r);
        collapsingMargins[1] = Math.max(0, -r);
        int top = getChildTop(child, alignmentHeight);
        int childWidth = child.getMeasuredWidth();
        child.layout(right - childWidth, top, right, child.getMeasuredHeight() + top);
        return right - (lp.leftMargin + childWidth);
    }

    private int getChildTop(View child, int alignmentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int childHeight = child.getMeasuredHeight();
        int alignmentOffset = alignmentHeight > 0 ? (childHeight - alignmentHeight) / 2 : 0;
        int childVerticalGravity = getChildVerticalGravity(lp.gravity);
        if (childVerticalGravity == 48) {
            return getPaddingTop() - alignmentOffset;
        }
        if (childVerticalGravity == 80) {
            return (((getHeight() - getPaddingBottom()) - childHeight) - lp.bottomMargin) - alignmentOffset;
        }
        childVerticalGravity = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int height = getHeight();
        int spaceAbove = (((height - childVerticalGravity) - paddingBottom) - childHeight) / 2;
        if (spaceAbove < lp.topMargin) {
            spaceAbove = lp.topMargin;
        } else {
            int spaceBelow = (((height - paddingBottom) - childHeight) - spaceAbove) - childVerticalGravity;
            if (spaceBelow < lp.bottomMargin) {
                spaceAbove = Math.max(0, spaceAbove - (lp.bottomMargin - spaceBelow));
            }
        }
        return childVerticalGravity + spaceAbove;
    }

    private int getChildVerticalGravity(int gravity) {
        int vgrav = gravity & 112;
        if (vgrav == 16 || vgrav == 48 || vgrav == 80) {
            return vgrav;
        }
        return this.mGravity & 112;
    }

    private void addCustomViewsWithGravity(List<View> views, int gravity) {
        boolean z = true;
        if (ViewCompat.getLayoutDirection(this) != 1) {
            z = false;
        }
        boolean isRtl = z;
        int childCount = getChildCount();
        int absGrav = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this));
        views.clear();
        int i;
        View child;
        LayoutParams lp;
        if (isRtl) {
            for (i = childCount - 1; i >= 0; i--) {
                child = getChildAt(i);
                lp = (LayoutParams) child.getLayoutParams();
                if (lp.mViewType == 0 && shouldLayout(child) && getChildHorizontalGravity(lp.gravity) == absGrav) {
                    views.add(child);
                }
            }
            return;
        }
        for (i = 0; i < childCount; i++) {
            child = getChildAt(i);
            lp = (LayoutParams) child.getLayoutParams();
            if (lp.mViewType == 0 && shouldLayout(child) && getChildHorizontalGravity(lp.gravity) == absGrav) {
                views.add(child);
            }
        }
    }

    private int getChildHorizontalGravity(int gravity) {
        int ld = ViewCompat.getLayoutDirection(this);
        int hGrav = GravityCompat.getAbsoluteGravity(gravity, ld) & 7;
        if (hGrav != 1) {
            int i = 3;
            if (!(hGrav == 3 || hGrav == 5)) {
                if (ld == 1) {
                    i = 5;
                }
                return i;
            }
        }
        return hGrav;
    }

    private boolean shouldLayout(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    private int getHorizontalMargins(View v) {
        MarginLayoutParams mlp = (MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(mlp) + MarginLayoutParamsCompat.getMarginEnd(mlp);
    }

    private int getVerticalMargins(View v) {
        MarginLayoutParams mlp = (MarginLayoutParams) v.getLayoutParams();
        return mlp.topMargin + mlp.bottomMargin;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) p);
        }
        if (p instanceof android.support.v7.app.ActionBar.LayoutParams) {
            return new LayoutParams((android.support.v7.app.ActionBar.LayoutParams) p);
        }
        if (p instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) p);
        }
        return new LayoutParams(p);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && (p instanceof LayoutParams);
    }

    private static boolean isCustomView(View child) {
        return ((LayoutParams) child.getLayoutParams()).mViewType == 0;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }

    void removeChildrenForExpandedActionView() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (!(((LayoutParams) child.getLayoutParams()).mViewType == 2 || child == this.mMenuView)) {
                removeViewAt(i);
                this.mHiddenViews.add(child);
            }
        }
    }

    void addChildrenForExpandedActionView() {
        for (int i = this.mHiddenViews.size() - 1; i >= 0; i--) {
            addView((View) this.mHiddenViews.get(i));
        }
        this.mHiddenViews.clear();
    }

    private boolean isChildOrHidden(View child) {
        if (child.getParent() != this) {
            if (!this.mHiddenViews.contains(child)) {
                return false;
            }
        }
        return true;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setCollapsible(boolean collapsible) {
        this.mCollapsible = collapsible;
        requestLayout();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setMenuCallbacks(Callback pcb, MenuBuilder.Callback mcb) {
        this.mActionMenuPresenterCallback = pcb;
        this.mMenuBuilderCallback = mcb;
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.setMenuCallbacks(pcb, mcb);
        }
    }

    private void ensureContentInsets() {
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
    }

    ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.mOuterActionMenuPresenter;
    }

    Context getPopupContext() {
        return this.mPopupContext;
    }
}
