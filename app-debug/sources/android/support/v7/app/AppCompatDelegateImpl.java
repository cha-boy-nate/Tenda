package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.NavUtils;
import android.support.v4.view.KeyEventDispatcher;
import android.support.v4.view.KeyEventDispatcher.Component;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.ActionBarDrawerToggle.Delegate;
import android.support.v7.appcompat.C0185R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.view.SupportActionModeWrapper.CallbackWrapper;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuBuilder.Callback;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.ContentFrameLayout.OnAttachListener;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.FitWindowsViewGroup.OnFitSystemWindowsListener;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

class AppCompatDelegateImpl extends AppCompatDelegate implements Callback, Factory2 {
    private static final boolean DEBUG = false;
    static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
    private static final boolean IS_PRE_LOLLIPOP = (VERSION.SDK_INT < 21);
    private static final String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";
    private static boolean sInstalledExceptionHandler = true;
    private static final int[] sWindowBackgroundStyleable = new int[]{16842836};
    ActionBar mActionBar;
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    ActionMode mActionMode;
    PopupWindow mActionModePopup;
    ActionBarContextView mActionModeView;
    final AppCompatCallback mAppCompatCallback;
    private AppCompatViewInflater mAppCompatViewInflater;
    final Window.Callback mAppCompatWindowCallback;
    private boolean mApplyDayNightCalled;
    private AutoNightModeManager mAutoNightModeManager;
    private boolean mClosingActionMenu;
    final Context mContext;
    private DecorContentParent mDecorContentParent;
    private boolean mEnableDefaultActionBarUp;
    ViewPropertyAnimatorCompat mFadeAnim = null;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    private boolean mHandleNativeActionModes = true;
    boolean mHasActionBar;
    int mInvalidatePanelMenuFeatures;
    boolean mInvalidatePanelMenuPosted;
    private final Runnable mInvalidatePanelMenuRunnable = new C01802();
    boolean mIsDestroyed;
    boolean mIsFloating;
    private int mLocalNightMode = -100;
    private boolean mLongPressBackDown;
    MenuInflater mMenuInflater;
    final Window.Callback mOriginalWindowCallback;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    Runnable mShowActionModePopup;
    private View mStatusGuard;
    private ViewGroup mSubDecor;
    private boolean mSubDecorInstalled;
    private Rect mTempRect1;
    private Rect mTempRect2;
    private CharSequence mTitle;
    private TextView mTitleView;
    final Window mWindow;
    boolean mWindowNoTitle;

    /* renamed from: android.support.v7.app.AppCompatDelegateImpl$2 */
    class C01802 implements Runnable {
        C01802() {
        }

        public void run() {
            if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 1) != 0) {
                AppCompatDelegateImpl.this.doInvalidatePanelMenu(0);
            }
            if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 4096) != 0) {
                AppCompatDelegateImpl.this.doInvalidatePanelMenu(108);
            }
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            appCompatDelegateImpl.mInvalidatePanelMenuPosted = false;
            appCompatDelegateImpl.mInvalidatePanelMenuFeatures = 0;
        }
    }

    /* renamed from: android.support.v7.app.AppCompatDelegateImpl$6 */
    class C01816 implements Runnable {

        /* renamed from: android.support.v7.app.AppCompatDelegateImpl$6$1 */
        class C02881 extends ViewPropertyAnimatorListenerAdapter {
            C02881() {
            }

            public void onAnimationStart(View view) {
                AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
            }

            public void onAnimationEnd(View view) {
                AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                AppCompatDelegateImpl.this.mFadeAnim = null;
            }
        }

        C01816() {
        }

        public void run() {
            AppCompatDelegateImpl.this.mActionModePopup.showAtLocation(AppCompatDelegateImpl.this.mActionModeView, 55, 0, 0);
            AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
            if (AppCompatDelegateImpl.this.shouldAnimateActionModeView()) {
                AppCompatDelegateImpl.this.mActionModeView.setAlpha(0.0f);
                AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                appCompatDelegateImpl.mFadeAnim = ViewCompat.animate(appCompatDelegateImpl.mActionModeView).alpha(1.0f);
                AppCompatDelegateImpl.this.mFadeAnim.setListener(new C02881());
                return;
            }
            AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
            AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
        }
    }

    @VisibleForTesting
    final class AutoNightModeManager {
        private BroadcastReceiver mAutoTimeChangeReceiver;
        private IntentFilter mAutoTimeChangeReceiverFilter;
        private boolean mIsNight;
        private TwilightManager mTwilightManager;

        /* renamed from: android.support.v7.app.AppCompatDelegateImpl$AutoNightModeManager$1 */
        class C01821 extends BroadcastReceiver {
            C01821() {
            }

            public void onReceive(Context context, Intent intent) {
                AutoNightModeManager.this.dispatchTimeChanged();
            }
        }

        AutoNightModeManager(@NonNull TwilightManager twilightManager) {
            this.mTwilightManager = twilightManager;
            this.mIsNight = twilightManager.isNight();
        }

        int getApplyableNightMode() {
            this.mIsNight = this.mTwilightManager.isNight();
            return this.mIsNight ? 2 : 1;
        }

        void dispatchTimeChanged() {
            boolean isNight = this.mTwilightManager.isNight();
            if (isNight != this.mIsNight) {
                this.mIsNight = isNight;
                AppCompatDelegateImpl.this.applyDayNight();
            }
        }

        void setup() {
            cleanup();
            if (this.mAutoTimeChangeReceiver == null) {
                this.mAutoTimeChangeReceiver = new C01821();
            }
            if (this.mAutoTimeChangeReceiverFilter == null) {
                this.mAutoTimeChangeReceiverFilter = new IntentFilter();
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_SET");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_TICK");
            }
            AppCompatDelegateImpl.this.mContext.registerReceiver(this.mAutoTimeChangeReceiver, this.mAutoTimeChangeReceiverFilter);
        }

        void cleanup() {
            if (this.mAutoTimeChangeReceiver != null) {
                AppCompatDelegateImpl.this.mContext.unregisterReceiver(this.mAutoTimeChangeReceiver);
                this.mAutoTimeChangeReceiver = null;
            }
        }
    }

    protected static final class PanelFeatureState {
        int background;
        View createdPanelView;
        ViewGroup decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int gravity;
        boolean isHandled;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        Context listPresenterContext;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView = false;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastOpen;
        int windowAnimations;
        /* renamed from: x */
        int f1x;
        /* renamed from: y */
        int f2y;

        private static class SavedState implements Parcelable {
            public static final Creator<SavedState> CREATOR = new C01831();
            int featureId;
            boolean isOpen;
            Bundle menuState;

            /* renamed from: android.support.v7.app.AppCompatDelegateImpl$PanelFeatureState$SavedState$1 */
            static class C01831 implements ClassLoaderCreator<SavedState> {
                C01831() {
                }

                public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                    return SavedState.readFromParcel(in, loader);
                }

                public SavedState createFromParcel(Parcel in) {
                    return SavedState.readFromParcel(in, null);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            }

            SavedState() {
            }

            public int describeContents() {
                return 0;
            }

            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.featureId);
                dest.writeInt(this.isOpen);
                if (this.isOpen) {
                    dest.writeBundle(this.menuState);
                }
            }

            static SavedState readFromParcel(Parcel source, ClassLoader loader) {
                SavedState savedState = new SavedState();
                savedState.featureId = source.readInt();
                boolean z = true;
                if (source.readInt() != 1) {
                    z = false;
                }
                savedState.isOpen = z;
                if (savedState.isOpen) {
                    savedState.menuState = source.readBundle(loader);
                }
                return savedState;
            }
        }

        PanelFeatureState(int featureId) {
            this.featureId = featureId;
        }

        public boolean hasPanelItems() {
            boolean z = false;
            if (this.shownPanelView == null) {
                return false;
            }
            if (this.createdPanelView != null) {
                return true;
            }
            if (this.listMenuPresenter.getAdapter().getCount() > 0) {
                z = true;
            }
            return z;
        }

        public void clearMenuPresenters() {
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder != null) {
                menuBuilder.removeMenuPresenter(this.listMenuPresenter);
            }
            this.listMenuPresenter = null;
        }

        void setStyle(Context context) {
            TypedValue outValue = new TypedValue();
            Theme widgetTheme = context.getResources().newTheme();
            widgetTheme.setTo(context.getTheme());
            widgetTheme.resolveAttribute(C0185R.attr.actionBarPopupTheme, outValue, true);
            if (outValue.resourceId != 0) {
                widgetTheme.applyStyle(outValue.resourceId, true);
            }
            widgetTheme.resolveAttribute(C0185R.attr.panelMenuListTheme, outValue, true);
            if (outValue.resourceId != 0) {
                widgetTheme.applyStyle(outValue.resourceId, true);
            } else {
                widgetTheme.applyStyle(C0185R.style.Theme_AppCompat_CompactMenu, true);
            }
            context = new ContextThemeWrapper(context, 0);
            context.getTheme().setTo(widgetTheme);
            this.listPresenterContext = context;
            TypedArray a = context.obtainStyledAttributes(C0185R.styleable.AppCompatTheme);
            this.background = a.getResourceId(C0185R.styleable.AppCompatTheme_panelBackground, 0);
            this.windowAnimations = a.getResourceId(C0185R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            a.recycle();
        }

        void setMenu(MenuBuilder menu) {
            MenuBuilder menuBuilder = this.menu;
            if (menu != menuBuilder) {
                if (menuBuilder != null) {
                    menuBuilder.removeMenuPresenter(this.listMenuPresenter);
                }
                this.menu = menu;
                if (menu != null) {
                    MenuPresenter menuPresenter = this.listMenuPresenter;
                    if (menuPresenter != null) {
                        menu.addMenuPresenter(menuPresenter);
                    }
                }
            }
        }

        MenuView getListMenuView(MenuPresenter.Callback cb) {
            if (this.menu == null) {
                return null;
            }
            if (this.listMenuPresenter == null) {
                this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, C0185R.layout.abc_list_menu_item_layout);
                this.listMenuPresenter.setCallback(cb);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }

        Parcelable onSaveInstanceState() {
            SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return savedState;
        }

        void onRestoreInstanceState(Parcelable state) {
            SavedState savedState = (SavedState) state;
            this.featureId = savedState.featureId;
            this.wasLastOpen = savedState.isOpen;
            this.frozenMenuState = savedState.menuState;
            this.shownPanelView = null;
            this.decorView = null;
        }

        void applyFrozenState() {
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder != null) {
                Bundle bundle = this.frozenMenuState;
                if (bundle != null) {
                    menuBuilder.restorePresenterStates(bundle);
                    this.frozenMenuState = null;
                }
            }
        }
    }

    /* renamed from: android.support.v7.app.AppCompatDelegateImpl$3 */
    class C02723 implements OnApplyWindowInsetsListener {
        C02723() {
        }

        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
            int top = insets.getSystemWindowInsetTop();
            int newTop = AppCompatDelegateImpl.this.updateStatusGuard(top);
            if (top != newTop) {
                insets = insets.replaceSystemWindowInsets(insets.getSystemWindowInsetLeft(), newTop, insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            }
            return ViewCompat.onApplyWindowInsets(v, insets);
        }
    }

    /* renamed from: android.support.v7.app.AppCompatDelegateImpl$4 */
    class C02734 implements OnFitSystemWindowsListener {
        C02734() {
        }

        public void onFitSystemWindows(Rect insets) {
            insets.top = AppCompatDelegateImpl.this.updateStatusGuard(insets.top);
        }
    }

    /* renamed from: android.support.v7.app.AppCompatDelegateImpl$5 */
    class C02745 implements OnAttachListener {
        C02745() {
        }

        public void onAttachedFromWindow() {
        }

        public void onDetachedFromWindow() {
            AppCompatDelegateImpl.this.dismissPopups();
        }
    }

    private class ActionBarDrawableToggleImpl implements Delegate {
        ActionBarDrawableToggleImpl() {
        }

        public Drawable getThemeUpIndicator() {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(getActionBarThemedContext(), null, new int[]{C0185R.attr.homeAsUpIndicator});
            Drawable result = a.getDrawable(0);
            a.recycle();
            return result;
        }

        public Context getActionBarThemedContext() {
            return AppCompatDelegateImpl.this.getActionBarThemedContext();
        }

        public boolean isNavigationVisible() {
            ActionBar ab = AppCompatDelegateImpl.this.getSupportActionBar();
            return (ab == null || (ab.getDisplayOptions() & 4) == 0) ? false : true;
        }

        public void setActionBarUpIndicator(Drawable upDrawable, int contentDescRes) {
            ActionBar ab = AppCompatDelegateImpl.this.getSupportActionBar();
            if (ab != null) {
                ab.setHomeAsUpIndicator(upDrawable);
                ab.setHomeActionContentDescription(contentDescRes);
            }
        }

        public void setActionBarDescription(int contentDescRes) {
            ActionBar ab = AppCompatDelegateImpl.this.getSupportActionBar();
            if (ab != null) {
                ab.setHomeActionContentDescription(contentDescRes);
            }
        }
    }

    private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
        ActionMenuPresenterCallback() {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            Window.Callback cb = AppCompatDelegateImpl.this.getWindowCallback();
            if (cb != null) {
                cb.onMenuOpened(108, subMenu);
            }
            return true;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            AppCompatDelegateImpl.this.checkCloseActionMenu(menu);
        }
    }

    class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
        private ActionMode.Callback mWrapped;

        /* renamed from: android.support.v7.app.AppCompatDelegateImpl$ActionModeCallbackWrapperV9$1 */
        class C02901 extends ViewPropertyAnimatorListenerAdapter {
            C02901() {
            }

            public void onAnimationEnd(View view) {
                AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                    AppCompatDelegateImpl.this.mActionModePopup.dismiss();
                } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                    ViewCompat.requestApplyInsets((View) AppCompatDelegateImpl.this.mActionModeView.getParent());
                }
                AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
                AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                AppCompatDelegateImpl.this.mFadeAnim = null;
            }
        }

        public ActionModeCallbackWrapperV9(ActionMode.Callback wrapped) {
            this.mWrapped = wrapped;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return this.mWrapped.onCreateActionMode(mode, menu);
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(mode, menu);
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return this.mWrapped.onActionItemClicked(mode, item);
        }

        public void onDestroyActionMode(ActionMode mode) {
            this.mWrapped.onDestroyActionMode(mode);
            if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                AppCompatDelegateImpl.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup);
            }
            if (AppCompatDelegateImpl.this.mActionModeView != null) {
                AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                appCompatDelegateImpl.mFadeAnim = ViewCompat.animate(appCompatDelegateImpl.mActionModeView).alpha(0.0f);
                AppCompatDelegateImpl.this.mFadeAnim.setListener(new C02901());
            }
            if (AppCompatDelegateImpl.this.mAppCompatCallback != null) {
                AppCompatDelegateImpl.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImpl.this.mActionMode);
            }
            AppCompatDelegateImpl.this.mActionMode = null;
        }
    }

    class AppCompatWindowCallback extends WindowCallbackWrapper {
        AppCompatWindowCallback(Window.Callback callback) {
            super(callback);
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            if (!AppCompatDelegateImpl.this.dispatchKeyEvent(event)) {
                if (!super.dispatchKeyEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        public boolean dispatchKeyShortcutEvent(KeyEvent event) {
            if (!super.dispatchKeyShortcutEvent(event)) {
                if (!AppCompatDelegateImpl.this.onKeyShortcut(event.getKeyCode(), event)) {
                    return false;
                }
            }
            return true;
        }

        public boolean onCreatePanelMenu(int featureId, Menu menu) {
            if (featureId != 0 || (menu instanceof MenuBuilder)) {
                return super.onCreatePanelMenu(featureId, menu);
            }
            return false;
        }

        public void onContentChanged() {
        }

        public boolean onPreparePanel(int featureId, View view, Menu menu) {
            MenuBuilder mb = menu instanceof MenuBuilder ? (MenuBuilder) menu : null;
            if (featureId == 0 && mb == null) {
                return false;
            }
            if (mb != null) {
                mb.setOverrideVisibleItems(true);
            }
            boolean handled = super.onPreparePanel(featureId, view, menu);
            if (mb != null) {
                mb.setOverrideVisibleItems(false);
            }
            return handled;
        }

        public boolean onMenuOpened(int featureId, Menu menu) {
            super.onMenuOpened(featureId, menu);
            AppCompatDelegateImpl.this.onMenuOpened(featureId);
            return true;
        }

        public void onPanelClosed(int featureId, Menu menu) {
            super.onPanelClosed(featureId, menu);
            AppCompatDelegateImpl.this.onPanelClosed(featureId);
        }

        public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback) {
            if (VERSION.SDK_INT >= 23) {
                return null;
            }
            if (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled()) {
                return startAsSupportActionMode(callback);
            }
            return super.onWindowStartingActionMode(callback);
        }

        final android.view.ActionMode startAsSupportActionMode(android.view.ActionMode.Callback callback) {
            CallbackWrapper callbackWrapper = new CallbackWrapper(AppCompatDelegateImpl.this.mContext, callback);
            ActionMode supportActionMode = AppCompatDelegateImpl.this.startSupportActionMode(callbackWrapper);
            if (supportActionMode != null) {
                return callbackWrapper.getActionModeWrapper(supportActionMode);
            }
            return null;
        }

        @RequiresApi(23)
        public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback, int type) {
            if (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled()) {
                if (type == 0) {
                    return startAsSupportActionMode(callback);
                }
            }
            return super.onWindowStartingActionMode(callback, type);
        }

        @RequiresApi(24)
        public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {
            PanelFeatureState panel = AppCompatDelegateImpl.this.getPanelState(0, true);
            if (panel == null || panel.menu == null) {
                super.onProvideKeyboardShortcuts(data, menu, deviceId);
            } else {
                super.onProvideKeyboardShortcuts(data, panel.menu, deviceId);
            }
        }
    }

    private class ListMenuDecorView extends ContentFrameLayout {
        public ListMenuDecorView(Context context) {
            super(context);
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            if (!AppCompatDelegateImpl.this.dispatchKeyEvent(event)) {
                if (!super.dispatchKeyEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        public boolean onInterceptTouchEvent(MotionEvent event) {
            if (event.getAction() != 0 || !isOutOfBounds((int) event.getX(), (int) event.getY())) {
                return super.onInterceptTouchEvent(event);
            }
            AppCompatDelegateImpl.this.closePanel(0);
            return true;
        }

        public void setBackgroundResource(int resid) {
            setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), resid));
        }

        private boolean isOutOfBounds(int x, int y) {
            if (x >= -5 && y >= -5 && x <= getWidth() + 5) {
                if (y <= getHeight() + 5) {
                    return false;
                }
            }
            return true;
        }
    }

    private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
        PanelMenuPresenterCallback() {
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            Menu parentMenu = menu.getRootMenu();
            boolean isSubMenu = parentMenu != menu;
            PanelFeatureState panel = AppCompatDelegateImpl.this.findMenuPanel(isSubMenu ? parentMenu : menu);
            if (panel == null) {
                return;
            }
            if (isSubMenu) {
                AppCompatDelegateImpl.this.callOnPanelClosed(panel.featureId, panel, parentMenu);
                AppCompatDelegateImpl.this.closePanel(panel, true);
                return;
            }
            AppCompatDelegateImpl.this.closePanel(panel, allMenusAreClosing);
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            if (subMenu == null && AppCompatDelegateImpl.this.mHasActionBar) {
                Window.Callback cb = AppCompatDelegateImpl.this.getWindowCallback();
                if (!(cb == null || AppCompatDelegateImpl.this.mIsDestroyed)) {
                    cb.onMenuOpened(108, subMenu);
                }
            }
            return true;
        }
    }

    /* renamed from: android.support.v7.app.AppCompatDelegateImpl$7 */
    class C02897 extends ViewPropertyAnimatorListenerAdapter {
        C02897() {
        }

        public void onAnimationStart(View view) {
            AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
            AppCompatDelegateImpl.this.mActionModeView.sendAccessibilityEvent(32);
            if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                ViewCompat.requestApplyInsets((View) AppCompatDelegateImpl.this.mActionModeView.getParent());
            }
        }

        public void onAnimationEnd(View view) {
            AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
            AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
            AppCompatDelegateImpl.this.mFadeAnim = null;
        }
    }

    private android.view.ViewGroup createSubDecor() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:61:0x01b0 in {4, 7, 10, 13, 18, 23, 24, 27, 30, 33, 34, 37, 38, 41, 42, 46, 51, 54, 56, 58, 60} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/112302969.run(Unknown Source)
*/
        /*
        r10 = this;
        r0 = r10.mContext;
        r1 = android.support.v7.appcompat.C0185R.styleable.AppCompatTheme;
        r0 = r0.obtainStyledAttributes(r1);
        r1 = android.support.v7.appcompat.C0185R.styleable.AppCompatTheme_windowActionBar;
        r1 = r0.hasValue(r1);
        if (r1 == 0) goto L_0x01a5;
    L_0x0010:
        r1 = android.support.v7.appcompat.C0185R.styleable.AppCompatTheme_windowNoTitle;
        r2 = 0;
        r1 = r0.getBoolean(r1, r2);
        r3 = 1;
        if (r1 == 0) goto L_0x001e;
    L_0x001a:
        r10.requestWindowFeature(r3);
        goto L_0x002b;
    L_0x001e:
        r1 = android.support.v7.appcompat.C0185R.styleable.AppCompatTheme_windowActionBar;
        r1 = r0.getBoolean(r1, r2);
        if (r1 == 0) goto L_0x002b;
    L_0x0026:
        r1 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        r10.requestWindowFeature(r1);
    L_0x002b:
        r1 = android.support.v7.appcompat.C0185R.styleable.AppCompatTheme_windowActionBarOverlay;
        r1 = r0.getBoolean(r1, r2);
        r4 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        if (r1 == 0) goto L_0x0038;
    L_0x0035:
        r10.requestWindowFeature(r4);
    L_0x0038:
        r1 = android.support.v7.appcompat.C0185R.styleable.AppCompatTheme_windowActionModeOverlay;
        r1 = r0.getBoolean(r1, r2);
        if (r1 == 0) goto L_0x0045;
    L_0x0040:
        r1 = 10;
        r10.requestWindowFeature(r1);
    L_0x0045:
        r1 = android.support.v7.appcompat.C0185R.styleable.AppCompatTheme_android_windowIsFloating;
        r1 = r0.getBoolean(r1, r2);
        r10.mIsFloating = r1;
        r0.recycle();
        r1 = r10.mWindow;
        r1.getDecorView();
        r1 = r10.mContext;
        r1 = android.view.LayoutInflater.from(r1);
        r5 = 0;
        r6 = r10.mWindowNoTitle;
        r7 = 0;
        if (r6 != 0) goto L_0x00d6;
    L_0x0061:
        r6 = r10.mIsFloating;
        if (r6 == 0) goto L_0x0074;
    L_0x0065:
        r3 = android.support.v7.appcompat.C0185R.layout.abc_dialog_title_material;
        r3 = r1.inflate(r3, r7);
        r5 = r3;
        r5 = (android.view.ViewGroup) r5;
        r10.mOverlayActionBar = r2;
        r10.mHasActionBar = r2;
        goto L_0x0107;
    L_0x0074:
        r6 = r10.mHasActionBar;
        if (r6 == 0) goto L_0x0107;
    L_0x0078:
        r6 = new android.util.TypedValue;
        r6.<init>();
        r8 = r10.mContext;
        r8 = r8.getTheme();
        r9 = android.support.v7.appcompat.C0185R.attr.actionBarTheme;
        r8.resolveAttribute(r9, r6, r3);
        r3 = r6.resourceId;
        if (r3 == 0) goto L_0x0096;
    L_0x008c:
        r3 = new android.support.v7.view.ContextThemeWrapper;
        r8 = r10.mContext;
        r9 = r6.resourceId;
        r3.<init>(r8, r9);
        goto L_0x0098;
    L_0x0096:
        r3 = r10.mContext;
    L_0x0098:
        r8 = android.view.LayoutInflater.from(r3);
        r9 = android.support.v7.appcompat.C0185R.layout.abc_screen_toolbar;
        r8 = r8.inflate(r9, r7);
        r5 = r8;
        r5 = (android.view.ViewGroup) r5;
        r8 = android.support.v7.appcompat.C0185R.id.decor_content_parent;
        r8 = r5.findViewById(r8);
        r8 = (android.support.v7.widget.DecorContentParent) r8;
        r10.mDecorContentParent = r8;
        r8 = r10.mDecorContentParent;
        r9 = r10.getWindowCallback();
        r8.setWindowCallback(r9);
        r8 = r10.mOverlayActionBar;
        if (r8 == 0) goto L_0x00c1;
    L_0x00bc:
        r8 = r10.mDecorContentParent;
        r8.initFeature(r4);
    L_0x00c1:
        r4 = r10.mFeatureProgress;
        if (r4 == 0) goto L_0x00cb;
    L_0x00c5:
        r4 = r10.mDecorContentParent;
        r8 = 2;
        r4.initFeature(r8);
    L_0x00cb:
        r4 = r10.mFeatureIndeterminateProgress;
        if (r4 == 0) goto L_0x00d5;
    L_0x00cf:
        r4 = r10.mDecorContentParent;
        r8 = 5;
        r4.initFeature(r8);
    L_0x00d5:
        goto L_0x0107;
    L_0x00d6:
        r3 = r10.mOverlayActionMode;
        if (r3 == 0) goto L_0x00e4;
    L_0x00da:
        r3 = android.support.v7.appcompat.C0185R.layout.abc_screen_simple_overlay_action_mode;
        r3 = r1.inflate(r3, r7);
        r3 = (android.view.ViewGroup) r3;
        r5 = r3;
        goto L_0x00ed;
    L_0x00e4:
        r3 = android.support.v7.appcompat.C0185R.layout.abc_screen_simple;
        r3 = r1.inflate(r3, r7);
        r3 = (android.view.ViewGroup) r3;
        r5 = r3;
    L_0x00ed:
        r3 = android.os.Build.VERSION.SDK_INT;
        r4 = 21;
        if (r3 < r4) goto L_0x00fc;
    L_0x00f3:
        r3 = new android.support.v7.app.AppCompatDelegateImpl$3;
        r3.<init>();
        android.support.v4.view.ViewCompat.setOnApplyWindowInsetsListener(r5, r3);
        goto L_0x0107;
    L_0x00fc:
        r3 = r5;
        r3 = (android.support.v7.widget.FitWindowsViewGroup) r3;
        r4 = new android.support.v7.app.AppCompatDelegateImpl$4;
        r4.<init>();
        r3.setOnFitSystemWindowsListener(r4);
    L_0x0107:
        if (r5 == 0) goto L_0x015f;
    L_0x0109:
        r3 = r10.mDecorContentParent;
        if (r3 != 0) goto L_0x0117;
    L_0x010d:
        r3 = android.support.v7.appcompat.C0185R.id.title;
        r3 = r5.findViewById(r3);
        r3 = (android.widget.TextView) r3;
        r10.mTitleView = r3;
    L_0x0117:
        android.support.v7.widget.ViewUtils.makeOptionalFitsSystemWindows(r5);
        r3 = android.support.v7.appcompat.C0185R.id.action_bar_activity_content;
        r3 = r5.findViewById(r3);
        r3 = (android.support.v7.widget.ContentFrameLayout) r3;
        r4 = r10.mWindow;
        r6 = 16908290; // 0x1020002 float:2.3877235E-38 double:8.353805E-317;
        r4 = r4.findViewById(r6);
        r4 = (android.view.ViewGroup) r4;
        if (r4 == 0) goto L_0x0151;
    L_0x012f:
        r8 = r4.getChildCount();
        if (r8 <= 0) goto L_0x0140;
    L_0x0135:
        r8 = r4.getChildAt(r2);
        r4.removeViewAt(r2);
        r3.addView(r8);
        goto L_0x012f;
    L_0x0140:
        r2 = -1;
        r4.setId(r2);
        r3.setId(r6);
        r2 = r4 instanceof android.widget.FrameLayout;
        if (r2 == 0) goto L_0x0151;
    L_0x014b:
        r2 = r4;
        r2 = (android.widget.FrameLayout) r2;
        r2.setForeground(r7);
    L_0x0151:
        r2 = r10.mWindow;
        r2.setContentView(r5);
        r2 = new android.support.v7.app.AppCompatDelegateImpl$5;
        r2.<init>();
        r3.setAttachListener(r2);
        return r5;
    L_0x015f:
        r2 = new java.lang.IllegalArgumentException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "AppCompat does not support the current theme features: { windowActionBar: ";
        r3.append(r4);
        r4 = r10.mHasActionBar;
        r3.append(r4);
        r4 = ", windowActionBarOverlay: ";
        r3.append(r4);
        r4 = r10.mOverlayActionBar;
        r3.append(r4);
        r4 = ", android:windowIsFloating: ";
        r3.append(r4);
        r4 = r10.mIsFloating;
        r3.append(r4);
        r4 = ", windowActionModeOverlay: ";
        r3.append(r4);
        r4 = r10.mOverlayActionMode;
        r3.append(r4);
        r4 = ", windowNoTitle: ";
        r3.append(r4);
        r4 = r10.mWindowNoTitle;
        r3.append(r4);
        r4 = " }";
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x01a5:
        r0.recycle();
        r1 = new java.lang.IllegalStateException;
        r2 = "You need to use a Theme.AppCompat theme (or descendant) with this activity.";
        r1.<init>(r2);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatDelegateImpl.createSubDecor():android.view.ViewGroup");
    }

    static {
        if (IS_PRE_LOLLIPOP && !sInstalledExceptionHandler) {
            final UncaughtExceptionHandler defHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                public void uncaughtException(Thread thread, Throwable thowable) {
                    if (shouldWrapException(thowable)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(thowable.getMessage());
                        stringBuilder.append(AppCompatDelegateImpl.EXCEPTION_HANDLER_MESSAGE_SUFFIX);
                        Throwable wrapped = new NotFoundException(stringBuilder.toString());
                        wrapped.initCause(thowable.getCause());
                        wrapped.setStackTrace(thowable.getStackTrace());
                        defHandler.uncaughtException(thread, wrapped);
                        return;
                    }
                    defHandler.uncaughtException(thread, thowable);
                }

                private boolean shouldWrapException(Throwable throwable) {
                    boolean z = false;
                    if (!(throwable instanceof NotFoundException)) {
                        return false;
                    }
                    String message = throwable.getMessage();
                    if (message != null && (message.contains("drawable") || message.contains("Drawable"))) {
                        z = true;
                    }
                    return z;
                }
            });
        }
    }

    AppCompatDelegateImpl(Context context, Window window, AppCompatCallback callback) {
        this.mContext = context;
        this.mWindow = window;
        this.mAppCompatCallback = callback;
        this.mOriginalWindowCallback = this.mWindow.getCallback();
        Window.Callback callback2 = this.mOriginalWindowCallback;
        if (callback2 instanceof AppCompatWindowCallback) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        this.mAppCompatWindowCallback = new AppCompatWindowCallback(callback2);
        this.mWindow.setCallback(this.mAppCompatWindowCallback);
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, sWindowBackgroundStyleable);
        Drawable winBg = a.getDrawableIfKnown(null);
        if (winBg != null) {
            this.mWindow.setBackgroundDrawable(winBg);
        }
        a.recycle();
    }

    public void onCreate(Bundle savedInstanceState) {
        Window.Callback callback = this.mOriginalWindowCallback;
        if (callback instanceof Activity) {
            String parentActivityName = null;
            try {
                parentActivityName = NavUtils.getParentActivityName((Activity) callback);
            } catch (IllegalArgumentException e) {
            }
            if (parentActivityName != null) {
                ActionBar ab = peekSupportActionBar();
                if (ab == null) {
                    this.mEnableDefaultActionBarUp = true;
                } else {
                    ab.setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
        }
        if (savedInstanceState != null && this.mLocalNightMode == -100) {
            this.mLocalNightMode = savedInstanceState.getInt(KEY_LOCAL_NIGHT_MODE, -100);
        }
    }

    public void onPostCreate(Bundle savedInstanceState) {
        ensureSubDecor();
    }

    public ActionBar getSupportActionBar() {
        initWindowDecorActionBar();
        return this.mActionBar;
    }

    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }

    final Window.Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }

    private void initWindowDecorActionBar() {
        ensureSubDecor();
        if (this.mHasActionBar) {
            if (this.mActionBar == null) {
                Window.Callback callback = this.mOriginalWindowCallback;
                if (callback instanceof Activity) {
                    this.mActionBar = new WindowDecorActionBar((Activity) callback, this.mOverlayActionBar);
                } else if (callback instanceof Dialog) {
                    this.mActionBar = new WindowDecorActionBar((Dialog) callback);
                }
                ActionBar actionBar = this.mActionBar;
                if (actionBar != null) {
                    actionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
                }
            }
        }
    }

    public void setSupportActionBar(Toolbar toolbar) {
        if (this.mOriginalWindowCallback instanceof Activity) {
            ActionBar ab = getSupportActionBar();
            if (ab instanceof WindowDecorActionBar) {
                throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
            }
            this.mMenuInflater = null;
            if (ab != null) {
                ab.onDestroy();
            }
            if (toolbar != null) {
                ToolbarActionBar tbab = new ToolbarActionBar(toolbar, ((Activity) this.mOriginalWindowCallback).getTitle(), this.mAppCompatWindowCallback);
                this.mActionBar = tbab;
                this.mWindow.setCallback(tbab.getWrappedWindowCallback());
            } else {
                this.mActionBar = null;
                this.mWindow.setCallback(this.mAppCompatWindowCallback);
            }
            invalidateOptionsMenu();
        }
    }

    final Context getActionBarThemedContext() {
        Context context = null;
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            context = ab.getThemedContext();
        }
        if (context == null) {
            return this.mContext;
        }
        return context;
    }

    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            initWindowDecorActionBar();
            ActionBar actionBar = this.mActionBar;
            this.mMenuInflater = new SupportMenuInflater(actionBar != null ? actionBar.getThemedContext() : this.mContext);
        }
        return this.mMenuInflater;
    }

    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        ensureSubDecor();
        return this.mWindow.findViewById(id);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.onConfigurationChanged(newConfig);
            }
        }
        AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
        applyDayNight();
    }

    public void onStart() {
        applyDayNight();
    }

    public void onStop() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
        }
        AutoNightModeManager autoNightModeManager = this.mAutoNightModeManager;
        if (autoNightModeManager != null) {
            autoNightModeManager.cleanup();
        }
    }

    public void onPostResume() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(true);
        }
    }

    public void setContentView(View v) {
        ensureSubDecor();
        ViewGroup contentParent = (ViewGroup) this.mSubDecor.findViewById(16908290);
        contentParent.removeAllViews();
        contentParent.addView(v);
        this.mOriginalWindowCallback.onContentChanged();
    }

    public void setContentView(int resId) {
        ensureSubDecor();
        ViewGroup contentParent = (ViewGroup) this.mSubDecor.findViewById(16908290);
        contentParent.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(resId, contentParent);
        this.mOriginalWindowCallback.onContentChanged();
    }

    public void setContentView(View v, LayoutParams lp) {
        ensureSubDecor();
        ViewGroup contentParent = (ViewGroup) this.mSubDecor.findViewById(16908290);
        contentParent.removeAllViews();
        contentParent.addView(v, lp);
        this.mOriginalWindowCallback.onContentChanged();
    }

    public void addContentView(View v, LayoutParams lp) {
        ensureSubDecor();
        ((ViewGroup) this.mSubDecor.findViewById(16908290)).addView(v, lp);
        this.mOriginalWindowCallback.onContentChanged();
    }

    public void onSaveInstanceState(Bundle outState) {
        int i = this.mLocalNightMode;
        if (i != -100) {
            outState.putInt(KEY_LOCAL_NIGHT_MODE, i);
        }
    }

    public void onDestroy() {
        if (this.mInvalidatePanelMenuPosted) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
        }
        this.mIsDestroyed = true;
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.onDestroy();
        }
        AutoNightModeManager autoNightModeManager = this.mAutoNightModeManager;
        if (autoNightModeManager != null) {
            autoNightModeManager.cleanup();
        }
    }

    private void ensureSubDecor() {
        if (!this.mSubDecorInstalled) {
            this.mSubDecor = createSubDecor();
            CharSequence title = getTitle();
            if (!TextUtils.isEmpty(title)) {
                DecorContentParent decorContentParent = this.mDecorContentParent;
                if (decorContentParent != null) {
                    decorContentParent.setWindowTitle(title);
                } else if (peekSupportActionBar() != null) {
                    peekSupportActionBar().setWindowTitle(title);
                } else {
                    TextView textView = this.mTitleView;
                    if (textView != null) {
                        textView.setText(title);
                    }
                }
            }
            applyFixedSizeWindow();
            onSubDecorInstalled(this.mSubDecor);
            this.mSubDecorInstalled = true;
            PanelFeatureState st = getPanelState(0, false);
            if (!this.mIsDestroyed) {
                if (st == null || st.menu == null) {
                    invalidatePanelMenu(108);
                }
            }
        }
    }

    void onSubDecorInstalled(ViewGroup subDecor) {
    }

    private void applyFixedSizeWindow() {
        ContentFrameLayout cfl = (ContentFrameLayout) this.mSubDecor.findViewById(16908290);
        View windowDecor = this.mWindow.getDecorView();
        cfl.setDecorPadding(windowDecor.getPaddingLeft(), windowDecor.getPaddingTop(), windowDecor.getPaddingRight(), windowDecor.getPaddingBottom());
        TypedArray a = this.mContext.obtainStyledAttributes(C0185R.styleable.AppCompatTheme);
        a.getValue(C0185R.styleable.AppCompatTheme_windowMinWidthMajor, cfl.getMinWidthMajor());
        a.getValue(C0185R.styleable.AppCompatTheme_windowMinWidthMinor, cfl.getMinWidthMinor());
        if (a.hasValue(C0185R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
            a.getValue(C0185R.styleable.AppCompatTheme_windowFixedWidthMajor, cfl.getFixedWidthMajor());
        }
        if (a.hasValue(C0185R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
            a.getValue(C0185R.styleable.AppCompatTheme_windowFixedWidthMinor, cfl.getFixedWidthMinor());
        }
        if (a.hasValue(C0185R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
            a.getValue(C0185R.styleable.AppCompatTheme_windowFixedHeightMajor, cfl.getFixedHeightMajor());
        }
        if (a.hasValue(C0185R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
            a.getValue(C0185R.styleable.AppCompatTheme_windowFixedHeightMinor, cfl.getFixedHeightMinor());
        }
        a.recycle();
        cfl.requestLayout();
    }

    public boolean requestWindowFeature(int featureId) {
        featureId = sanitizeWindowFeatureId(featureId);
        if (this.mWindowNoTitle && featureId == 108) {
            return false;
        }
        if (this.mHasActionBar && featureId == 1) {
            this.mHasActionBar = false;
        }
        switch (featureId) {
            case 1:
                throwFeatureRequestIfSubDecorInstalled();
                this.mWindowNoTitle = true;
                return true;
            case 2:
                throwFeatureRequestIfSubDecorInstalled();
                this.mFeatureProgress = true;
                return true;
            case 5:
                throwFeatureRequestIfSubDecorInstalled();
                this.mFeatureIndeterminateProgress = true;
                return true;
            case 10:
                throwFeatureRequestIfSubDecorInstalled();
                this.mOverlayActionMode = true;
                return true;
            case 108:
                throwFeatureRequestIfSubDecorInstalled();
                this.mHasActionBar = true;
                return true;
            case 109:
                throwFeatureRequestIfSubDecorInstalled();
                this.mOverlayActionBar = true;
                return true;
            default:
                return this.mWindow.requestFeature(featureId);
        }
    }

    public boolean hasWindowFeature(int featureId) {
        boolean result = false;
        switch (sanitizeWindowFeatureId(featureId)) {
            case 1:
                result = this.mWindowNoTitle;
                break;
            case 2:
                result = this.mFeatureProgress;
                break;
            case 5:
                result = this.mFeatureIndeterminateProgress;
                break;
            case 10:
                result = this.mOverlayActionMode;
                break;
            case 108:
                result = this.mHasActionBar;
                break;
            case 109:
                result = this.mOverlayActionBar;
                break;
            default:
                break;
        }
        if (!result) {
            if (!this.mWindow.hasFeature(featureId)) {
                return false;
            }
        }
        return true;
    }

    public final void setTitle(CharSequence title) {
        this.mTitle = title;
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.setWindowTitle(title);
        } else if (peekSupportActionBar() != null) {
            peekSupportActionBar().setWindowTitle(title);
        } else {
            TextView textView = this.mTitleView;
            if (textView != null) {
                textView.setText(title);
            }
        }
    }

    final CharSequence getTitle() {
        Window.Callback callback = this.mOriginalWindowCallback;
        if (callback instanceof Activity) {
            return ((Activity) callback).getTitle();
        }
        return this.mTitle;
    }

    void onPanelClosed(int featureId) {
        if (featureId == 108) {
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.dispatchMenuVisibilityChanged(false);
            }
        } else if (featureId == 0) {
            PanelFeatureState st = getPanelState(featureId, true);
            if (st.isOpen) {
                closePanel(st, false);
            }
        }
    }

    void onMenuOpened(int featureId) {
        if (featureId == 108) {
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.dispatchMenuVisibilityChanged(true);
            }
        }
    }

    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
        Window.Callback cb = getWindowCallback();
        if (!(cb == null || this.mIsDestroyed)) {
            PanelFeatureState panel = findMenuPanel(menu.getRootMenu());
            if (panel != null) {
                return cb.onMenuItemSelected(panel.featureId, item);
            }
        }
        return false;
    }

    public void onMenuModeChange(MenuBuilder menu) {
        reopenMenu(menu, true);
    }

    public ActionMode startSupportActionMode(@NonNull ActionMode.Callback callback) {
        if (callback != null) {
            ActionMode actionMode = this.mActionMode;
            if (actionMode != null) {
                actionMode.finish();
            }
            ActionMode.Callback wrappedCallback = new ActionModeCallbackWrapperV9(callback);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                this.mActionMode = ab.startActionMode(wrappedCallback);
                ActionMode actionMode2 = this.mActionMode;
                if (actionMode2 != null) {
                    AppCompatCallback appCompatCallback = this.mAppCompatCallback;
                    if (appCompatCallback != null) {
                        appCompatCallback.onSupportActionModeStarted(actionMode2);
                    }
                }
            }
            if (this.mActionMode == null) {
                this.mActionMode = startSupportActionModeFromWindow(wrappedCallback);
            }
            return this.mActionMode;
        }
        throw new IllegalArgumentException("ActionMode callback can not be null.");
    }

    public void invalidateOptionsMenu() {
        ActionBar ab = getSupportActionBar();
        if (ab == null || !ab.invalidateOptionsMenu()) {
            invalidatePanelMenu(0);
        }
    }

    ActionMode startSupportActionModeFromWindow(@NonNull ActionMode.Callback callback) {
        endOnGoingFadeAnimation();
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        if (!(callback instanceof ActionModeCallbackWrapperV9)) {
            callback = new ActionModeCallbackWrapperV9(callback);
        }
        actionMode = null;
        AppCompatCallback appCompatCallback = this.mAppCompatCallback;
        if (appCompatCallback != null && !this.mIsDestroyed) {
            try {
                actionMode = appCompatCallback.onWindowStartingSupportActionMode(callback);
            } catch (AbstractMethodError e) {
            }
        }
        if (actionMode != null) {
            this.mActionMode = actionMode;
        } else {
            boolean z = true;
            if (this.mActionModeView == null) {
                if (this.mIsFloating) {
                    Context actionBarContext;
                    TypedValue outValue = new TypedValue();
                    Theme baseTheme = this.mContext.getTheme();
                    baseTheme.resolveAttribute(C0185R.attr.actionBarTheme, outValue, true);
                    if (outValue.resourceId != 0) {
                        Theme actionBarTheme = this.mContext.getResources().newTheme();
                        actionBarTheme.setTo(baseTheme);
                        actionBarTheme.applyStyle(outValue.resourceId, true);
                        actionBarContext = new ContextThemeWrapper(this.mContext, 0);
                        actionBarContext.getTheme().setTo(actionBarTheme);
                    } else {
                        actionBarContext = this.mContext;
                    }
                    this.mActionModeView = new ActionBarContextView(actionBarContext);
                    this.mActionModePopup = new PopupWindow(actionBarContext, null, C0185R.attr.actionModePopupWindowStyle);
                    PopupWindowCompat.setWindowLayoutType(this.mActionModePopup, 2);
                    this.mActionModePopup.setContentView(this.mActionModeView);
                    this.mActionModePopup.setWidth(-1);
                    actionBarContext.getTheme().resolveAttribute(C0185R.attr.actionBarSize, outValue, true);
                    this.mActionModeView.setContentHeight(TypedValue.complexToDimensionPixelSize(outValue.data, actionBarContext.getResources().getDisplayMetrics()));
                    this.mActionModePopup.setHeight(-2);
                    this.mShowActionModePopup = new C01816();
                } else {
                    ViewStubCompat stub = (ViewStubCompat) this.mSubDecor.findViewById(C0185R.id.action_mode_bar_stub);
                    if (stub != null) {
                        stub.setLayoutInflater(LayoutInflater.from(getActionBarThemedContext()));
                        this.mActionModeView = (ActionBarContextView) stub.inflate();
                    }
                }
            }
            if (this.mActionModeView != null) {
                endOnGoingFadeAnimation();
                this.mActionModeView.killMode();
                Context context = this.mActionModeView.getContext();
                ActionBarContextView actionBarContextView = this.mActionModeView;
                if (this.mActionModePopup != null) {
                    z = false;
                }
                actionMode = new StandaloneActionMode(context, actionBarContextView, callback, z);
                if (callback.onCreateActionMode(actionMode, actionMode.getMenu())) {
                    actionMode.invalidate();
                    this.mActionModeView.initForMode(actionMode);
                    this.mActionMode = actionMode;
                    if (shouldAnimateActionModeView()) {
                        this.mActionModeView.setAlpha(0.0f);
                        this.mFadeAnim = ViewCompat.animate(this.mActionModeView).alpha(1.0f);
                        this.mFadeAnim.setListener(new C02897());
                    } else {
                        this.mActionModeView.setAlpha(1.0f);
                        this.mActionModeView.setVisibility(0);
                        this.mActionModeView.sendAccessibilityEvent(32);
                        if (this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View) this.mActionModeView.getParent());
                        }
                    }
                    if (this.mActionModePopup != null) {
                        this.mWindow.getDecorView().post(this.mShowActionModePopup);
                    }
                } else {
                    this.mActionMode = null;
                }
            }
        }
        ActionMode actionMode2 = this.mActionMode;
        if (actionMode2 != null) {
            AppCompatCallback appCompatCallback2 = this.mAppCompatCallback;
            if (appCompatCallback2 != null) {
                appCompatCallback2.onSupportActionModeStarted(actionMode2);
                return this.mActionMode;
            }
        }
        return this.mActionMode;
    }

    final boolean shouldAnimateActionModeView() {
        if (this.mSubDecorInstalled) {
            View view = this.mSubDecor;
            if (view != null && ViewCompat.isLaidOut(view)) {
                return true;
            }
        }
        return false;
    }

    public void setHandleNativeActionModesEnabled(boolean enabled) {
        this.mHandleNativeActionModes = enabled;
    }

    public boolean isHandleNativeActionModesEnabled() {
        return this.mHandleNativeActionModes;
    }

    void endOnGoingFadeAnimation() {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mFadeAnim;
        if (viewPropertyAnimatorCompat != null) {
            viewPropertyAnimatorCompat.cancel();
        }
    }

    boolean onBackPressed() {
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
            return true;
        }
        ActionBar ab = getSupportActionBar();
        if (ab == null || !ab.collapseActionView()) {
            return false;
        }
        return true;
    }

    boolean onKeyShortcut(int keyCode, KeyEvent ev) {
        ActionBar ab = getSupportActionBar();
        if (ab != null && ab.onKeyShortcut(keyCode, ev)) {
            return true;
        }
        boolean handled = this.mPreparedPanel;
        if (handled && performPanelShortcut(handled, ev.getKeyCode(), ev, 1)) {
            PanelFeatureState panelFeatureState = this.mPreparedPanel;
            if (panelFeatureState != null) {
                panelFeatureState.isHandled = true;
            }
            return true;
        }
        if (this.mPreparedPanel == null) {
            PanelFeatureState st = getPanelState(0, true);
            preparePanel(st, ev);
            boolean handled2 = performPanelShortcut(st, ev.getKeyCode(), ev, 1);
            st.isPrepared = false;
            if (handled2) {
                return true;
            }
        }
        return false;
    }

    boolean dispatchKeyEvent(KeyEvent event) {
        Window.Callback callback = this.mOriginalWindowCallback;
        boolean isDown = true;
        if ((callback instanceof Component) || (callback instanceof AppCompatDialog)) {
            View root = this.mWindow.getDecorView();
            if (root != null && KeyEventDispatcher.dispatchBeforeHierarchy(root, event)) {
                return true;
            }
        }
        if (event.getKeyCode() == 82 && this.mOriginalWindowCallback.dispatchKeyEvent(event)) {
            return true;
        }
        int keyCode = event.getKeyCode();
        if (event.getAction() != 0) {
            isDown = false;
        }
        return isDown ? onKeyDown(keyCode, event) : onKeyUp(keyCode, event);
    }

    boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            boolean wasLongPressBackDown = this.mLongPressBackDown;
            this.mLongPressBackDown = false;
            PanelFeatureState st = getPanelState(0, false);
            if (st != null && st.isOpen) {
                if (!wasLongPressBackDown) {
                    closePanel(st, true);
                }
                return true;
            } else if (onBackPressed()) {
                return true;
            }
        } else if (keyCode == 82) {
            onKeyUpPanel(0, event);
            return true;
        }
        return false;
    }

    boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = true;
        if (keyCode == 4) {
            if ((event.getFlags() & 128) == 0) {
                z = false;
            }
            this.mLongPressBackDown = z;
        } else if (keyCode == 82) {
            onKeyDownPanel(0, event);
            return true;
        }
        return false;
    }

    public View createView(View parent, String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        boolean z = false;
        if (this.mAppCompatViewInflater == null) {
            String viewInflaterClassName = this.mContext.obtainStyledAttributes(C0185R.styleable.AppCompatTheme).getString(C0185R.styleable.AppCompatTheme_viewInflaterClass);
            if (viewInflaterClassName != null) {
                if (!AppCompatViewInflater.class.getName().equals(viewInflaterClassName)) {
                    try {
                        this.mAppCompatViewInflater = (AppCompatViewInflater) Class.forName(viewInflaterClassName).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    } catch (Throwable t) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to instantiate custom view inflater ");
                        stringBuilder.append(viewInflaterClassName);
                        stringBuilder.append(". Falling back to default.");
                        Log.i("AppCompatDelegate", stringBuilder.toString(), t);
                        this.mAppCompatViewInflater = new AppCompatViewInflater();
                    }
                }
            }
            this.mAppCompatViewInflater = new AppCompatViewInflater();
        }
        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            if (!(attrs instanceof XmlPullParser)) {
                z = shouldInheritContext((ViewParent) parent);
            } else if (((XmlPullParser) attrs).getDepth() > 1) {
                z = true;
            }
            inheritContext = z;
        }
        return this.mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext, IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            return false;
        }
        ViewParent windowDecor = this.mWindow.getDecorView();
        while (parent != null) {
            if (parent != windowDecor && (parent instanceof View)) {
                if (!ViewCompat.isAttachedToWindow((View) parent)) {
                    parent = parent.getParent();
                }
            }
            return false;
        }
        return true;
    }

    public void installViewFactory() {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        if (layoutInflater.getFactory() == null) {
            LayoutInflaterCompat.setFactory2(layoutInflater, this);
        } else if (!(layoutInflater.getFactory2() instanceof AppCompatDelegateImpl)) {
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    public final View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return createView(parent, name, context, attrs);
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }

    private void openPanel(PanelFeatureState st, KeyEvent event) {
        AppCompatDelegateImpl appCompatDelegateImpl = this;
        PanelFeatureState panelFeatureState = st;
        if (!panelFeatureState.isOpen) {
            if (!appCompatDelegateImpl.mIsDestroyed) {
                if (panelFeatureState.featureId == 0) {
                    if ((appCompatDelegateImpl.mContext.getResources().getConfiguration().screenLayout & 15) == 4) {
                        return;
                    }
                }
                Window.Callback cb = getWindowCallback();
                if (cb == null || cb.onMenuOpened(panelFeatureState.featureId, panelFeatureState.menu)) {
                    WindowManager wm = (WindowManager) appCompatDelegateImpl.mContext.getSystemService("window");
                    if (wm != null && preparePanel(st, event)) {
                        LayoutParams lp;
                        WindowManager.LayoutParams layoutParams;
                        int width = -2;
                        if (panelFeatureState.decorView != null) {
                            if (!panelFeatureState.refreshDecorView) {
                                if (panelFeatureState.createdPanelView != null) {
                                    lp = panelFeatureState.createdPanelView.getLayoutParams();
                                    if (lp != null && lp.width == -1) {
                                        width = -1;
                                    }
                                    panelFeatureState.isHandled = false;
                                    layoutParams = new WindowManager.LayoutParams(width, -2, panelFeatureState.f1x, panelFeatureState.f2y, PointerIconCompat.TYPE_HAND, 8519680, -3);
                                    layoutParams.gravity = panelFeatureState.gravity;
                                    layoutParams.windowAnimations = panelFeatureState.windowAnimations;
                                    wm.addView(panelFeatureState.decorView, layoutParams);
                                    panelFeatureState.isOpen = true;
                                    return;
                                }
                                panelFeatureState.isHandled = false;
                                layoutParams = new WindowManager.LayoutParams(width, -2, panelFeatureState.f1x, panelFeatureState.f2y, PointerIconCompat.TYPE_HAND, 8519680, -3);
                                layoutParams.gravity = panelFeatureState.gravity;
                                layoutParams.windowAnimations = panelFeatureState.windowAnimations;
                                wm.addView(panelFeatureState.decorView, layoutParams);
                                panelFeatureState.isOpen = true;
                                return;
                            }
                        }
                        if (panelFeatureState.decorView == null) {
                            if (!initializePanelDecor(st) || panelFeatureState.decorView == null) {
                                return;
                            }
                        } else if (panelFeatureState.refreshDecorView && panelFeatureState.decorView.getChildCount() > 0) {
                            panelFeatureState.decorView.removeAllViews();
                        }
                        if (initializePanelContent(st)) {
                            if (st.hasPanelItems()) {
                                lp = panelFeatureState.shownPanelView.getLayoutParams();
                                if (lp == null) {
                                    lp = new LayoutParams(-2, -2);
                                }
                                panelFeatureState.decorView.setBackgroundResource(panelFeatureState.background);
                                ViewParent shownPanelParent = panelFeatureState.shownPanelView.getParent();
                                if (shownPanelParent != null && (shownPanelParent instanceof ViewGroup)) {
                                    ((ViewGroup) shownPanelParent).removeView(panelFeatureState.shownPanelView);
                                }
                                panelFeatureState.decorView.addView(panelFeatureState.shownPanelView, lp);
                                if (!panelFeatureState.shownPanelView.hasFocus()) {
                                    panelFeatureState.shownPanelView.requestFocus();
                                }
                                panelFeatureState.isHandled = false;
                                layoutParams = new WindowManager.LayoutParams(width, -2, panelFeatureState.f1x, panelFeatureState.f2y, PointerIconCompat.TYPE_HAND, 8519680, -3);
                                layoutParams.gravity = panelFeatureState.gravity;
                                layoutParams.windowAnimations = panelFeatureState.windowAnimations;
                                wm.addView(panelFeatureState.decorView, layoutParams);
                                panelFeatureState.isOpen = true;
                                return;
                            }
                        }
                        return;
                    }
                    return;
                }
                closePanel(panelFeatureState, true);
            }
        }
    }

    private boolean initializePanelDecor(PanelFeatureState st) {
        st.setStyle(getActionBarThemedContext());
        st.decorView = new ListMenuDecorView(st.listPresenterContext);
        st.gravity = 81;
        return true;
    }

    private void reopenMenu(MenuBuilder menu, boolean toggleMenuMode) {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent == null || !decorContentParent.canShowOverflowMenu() || (ViewConfiguration.get(this.mContext).hasPermanentMenuKey() && !this.mDecorContentParent.isOverflowMenuShowPending())) {
            PanelFeatureState st = getPanelState(0, true);
            st.refreshDecorView = true;
            closePanel(st, false);
            openPanel(st, null);
            return;
        }
        Window.Callback cb = getWindowCallback();
        if (this.mDecorContentParent.isOverflowMenuShowing()) {
            if (toggleMenuMode) {
                this.mDecorContentParent.hideOverflowMenu();
                if (!this.mIsDestroyed) {
                    cb.onPanelClosed(108, getPanelState(0, true).menu);
                }
            }
        }
        if (!(cb == null || this.mIsDestroyed)) {
            if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
                this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
                this.mInvalidatePanelMenuRunnable.run();
            }
            PanelFeatureState st2 = getPanelState(0, true);
            if (!(st2.menu == null || st2.refreshMenuContent || !cb.onPreparePanel(0, st2.createdPanelView, st2.menu))) {
                cb.onMenuOpened(108, st2.menu);
                this.mDecorContentParent.showOverflowMenu();
            }
        }
    }

    private boolean initializePanelMenu(PanelFeatureState st) {
        Context context = this.mContext;
        if ((st.featureId == 0 || st.featureId == 108) && this.mDecorContentParent != null) {
            TypedValue outValue = new TypedValue();
            Theme baseTheme = context.getTheme();
            baseTheme.resolveAttribute(C0185R.attr.actionBarTheme, outValue, true);
            Theme widgetTheme = null;
            if (outValue.resourceId != 0) {
                widgetTheme = context.getResources().newTheme();
                widgetTheme.setTo(baseTheme);
                widgetTheme.applyStyle(outValue.resourceId, true);
                widgetTheme.resolveAttribute(C0185R.attr.actionBarWidgetTheme, outValue, true);
            } else {
                baseTheme.resolveAttribute(C0185R.attr.actionBarWidgetTheme, outValue, true);
            }
            if (outValue.resourceId != 0) {
                if (widgetTheme == null) {
                    widgetTheme = context.getResources().newTheme();
                    widgetTheme.setTo(baseTheme);
                }
                widgetTheme.applyStyle(outValue.resourceId, true);
            }
            if (widgetTheme != null) {
                context = new ContextThemeWrapper(context, 0);
                context.getTheme().setTo(widgetTheme);
            }
        }
        MenuBuilder menu = new MenuBuilder(context);
        menu.setCallback(this);
        st.setMenu(menu);
        return true;
    }

    private boolean initializePanelContent(PanelFeatureState st) {
        boolean z = true;
        if (st.createdPanelView != null) {
            st.shownPanelView = st.createdPanelView;
            return true;
        } else if (st.menu == null) {
            return false;
        } else {
            if (this.mPanelMenuPresenterCallback == null) {
                this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback();
            }
            st.shownPanelView = (View) st.getListMenuView(this.mPanelMenuPresenterCallback);
            if (st.shownPanelView == null) {
                z = false;
            }
            return z;
        }
    }

    private boolean preparePanel(PanelFeatureState st, KeyEvent event) {
        if (this.mIsDestroyed) {
            return false;
        }
        if (st.isPrepared) {
            return true;
        }
        boolean isActionBarMenu;
        DecorContentParent decorContentParent;
        DecorContentParent decorContentParent2;
        PanelFeatureState panelFeatureState = this.mPreparedPanel;
        if (!(panelFeatureState == null || panelFeatureState == st)) {
            closePanel(panelFeatureState, false);
        }
        Window.Callback cb = getWindowCallback();
        if (cb != null) {
            st.createdPanelView = cb.onCreatePanelView(st.featureId);
        }
        if (st.featureId != 0) {
            if (st.featureId != 108) {
                isActionBarMenu = false;
                if (isActionBarMenu) {
                    decorContentParent = this.mDecorContentParent;
                    if (decorContentParent != null) {
                        decorContentParent.setMenuPrepared();
                    }
                }
                if (st.createdPanelView == null && !(isActionBarMenu && (peekSupportActionBar() instanceof ToolbarActionBar))) {
                    if (st.menu == null || st.refreshMenuContent) {
                        if (st.menu != null && (!initializePanelMenu(st) || st.menu == null)) {
                            return false;
                        }
                        if (isActionBarMenu && this.mDecorContentParent != null) {
                            if (this.mActionMenuPresenterCallback == null) {
                                this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
                            }
                            this.mDecorContentParent.setMenu(st.menu, this.mActionMenuPresenterCallback);
                        }
                        st.menu.stopDispatchingItemsChanged();
                        if (cb.onCreatePanelMenu(st.featureId, st.menu)) {
                            st.setMenu(null);
                            if (isActionBarMenu) {
                                decorContentParent2 = this.mDecorContentParent;
                                if (decorContentParent2 != null) {
                                    decorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
                                }
                            }
                            return false;
                        }
                        st.refreshMenuContent = false;
                    }
                    st.menu.stopDispatchingItemsChanged();
                    if (st.frozenActionViewState != null) {
                        st.menu.restoreActionViewStates(st.frozenActionViewState);
                        st.frozenActionViewState = null;
                    }
                    if (cb.onPreparePanel(0, st.createdPanelView, st.menu)) {
                        if (isActionBarMenu) {
                            decorContentParent2 = this.mDecorContentParent;
                            if (decorContentParent2 != null) {
                                decorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
                            }
                        }
                        st.menu.startDispatchingItemsChanged();
                        return false;
                    }
                    st.qwertyMode = KeyCharacterMap.load(event == null ? event.getDeviceId() : -1).getKeyboardType() == 1;
                    st.menu.setQwertyMode(st.qwertyMode);
                    st.menu.startDispatchingItemsChanged();
                }
                st.isPrepared = true;
                st.isHandled = false;
                this.mPreparedPanel = st;
                return true;
            }
        }
        isActionBarMenu = true;
        if (isActionBarMenu) {
            decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null) {
                decorContentParent.setMenuPrepared();
            }
        }
        if (st.menu != null) {
        }
        if (this.mActionMenuPresenterCallback == null) {
            this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
        }
        this.mDecorContentParent.setMenu(st.menu, this.mActionMenuPresenterCallback);
        st.menu.stopDispatchingItemsChanged();
        if (cb.onCreatePanelMenu(st.featureId, st.menu)) {
            st.refreshMenuContent = false;
            st.menu.stopDispatchingItemsChanged();
            if (st.frozenActionViewState != null) {
                st.menu.restoreActionViewStates(st.frozenActionViewState);
                st.frozenActionViewState = null;
            }
            if (cb.onPreparePanel(0, st.createdPanelView, st.menu)) {
                if (event == null) {
                }
                if (KeyCharacterMap.load(event == null ? event.getDeviceId() : -1).getKeyboardType() == 1) {
                }
                st.qwertyMode = KeyCharacterMap.load(event == null ? event.getDeviceId() : -1).getKeyboardType() == 1;
                st.menu.setQwertyMode(st.qwertyMode);
                st.menu.startDispatchingItemsChanged();
                st.isPrepared = true;
                st.isHandled = false;
                this.mPreparedPanel = st;
                return true;
            }
            if (isActionBarMenu) {
                decorContentParent2 = this.mDecorContentParent;
                if (decorContentParent2 != null) {
                    decorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
                }
            }
            st.menu.startDispatchingItemsChanged();
            return false;
        }
        st.setMenu(null);
        if (isActionBarMenu) {
            decorContentParent2 = this.mDecorContentParent;
            if (decorContentParent2 != null) {
                decorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
            }
        }
        return false;
    }

    void checkCloseActionMenu(MenuBuilder menu) {
        if (!this.mClosingActionMenu) {
            this.mClosingActionMenu = true;
            this.mDecorContentParent.dismissPopups();
            Window.Callback cb = getWindowCallback();
            if (!(cb == null || this.mIsDestroyed)) {
                cb.onPanelClosed(108, menu);
            }
            this.mClosingActionMenu = false;
        }
    }

    void closePanel(int featureId) {
        closePanel(getPanelState(featureId, true), true);
    }

    void closePanel(PanelFeatureState st, boolean doCallback) {
        if (doCallback && st.featureId == 0) {
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null && decorContentParent.isOverflowMenuShowing()) {
                checkCloseActionMenu(st.menu);
                return;
            }
        }
        WindowManager wm = (WindowManager) this.mContext.getSystemService("window");
        if (!(wm == null || !st.isOpen || st.decorView == null)) {
            wm.removeView(st.decorView);
            if (doCallback) {
                callOnPanelClosed(st.featureId, st, null);
            }
        }
        st.isPrepared = false;
        st.isHandled = false;
        st.isOpen = false;
        st.shownPanelView = null;
        st.refreshDecorView = true;
        if (this.mPreparedPanel == st) {
            this.mPreparedPanel = null;
        }
    }

    private boolean onKeyDownPanel(int featureId, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            PanelFeatureState st = getPanelState(featureId, true);
            if (!st.isOpen) {
                return preparePanel(st, event);
            }
        }
        return false;
    }

    private boolean onKeyUpPanel(int featureId, KeyEvent event) {
        if (this.mActionMode != null) {
            return false;
        }
        AudioManager audioManager;
        boolean handled = false;
        PanelFeatureState st = getPanelState(featureId, true);
        if (featureId == 0) {
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (!(decorContentParent == null || !decorContentParent.canShowOverflowMenu() || ViewConfiguration.get(this.mContext).hasPermanentMenuKey())) {
                if (this.mDecorContentParent.isOverflowMenuShowing()) {
                    handled = this.mDecorContentParent.hideOverflowMenu();
                } else if (!this.mIsDestroyed && preparePanel(st, event)) {
                    handled = this.mDecorContentParent.showOverflowMenu();
                }
                if (handled) {
                    audioManager = (AudioManager) this.mContext.getSystemService("audio");
                    if (audioManager != null) {
                        audioManager.playSoundEffect(0);
                    } else {
                        Log.w("AppCompatDelegate", "Couldn't get audio manager");
                    }
                }
                return handled;
            }
        }
        if (!st.isOpen) {
            if (!st.isHandled) {
                if (st.isPrepared) {
                    boolean show = true;
                    if (st.refreshMenuContent) {
                        st.isPrepared = false;
                        show = preparePanel(st, event);
                    }
                    if (show) {
                        openPanel(st, event);
                        handled = true;
                    }
                }
                if (handled) {
                    audioManager = (AudioManager) this.mContext.getSystemService("audio");
                    if (audioManager != null) {
                        Log.w("AppCompatDelegate", "Couldn't get audio manager");
                    } else {
                        audioManager.playSoundEffect(0);
                    }
                }
                return handled;
            }
        }
        handled = st.isOpen;
        closePanel(st, true);
        if (handled) {
            audioManager = (AudioManager) this.mContext.getSystemService("audio");
            if (audioManager != null) {
                audioManager.playSoundEffect(0);
            } else {
                Log.w("AppCompatDelegate", "Couldn't get audio manager");
            }
        }
        return handled;
    }

    void callOnPanelClosed(int featureId, PanelFeatureState panel, Menu menu) {
        if (menu == null) {
            if (panel == null && featureId >= 0) {
                PanelFeatureState[] panelFeatureStateArr = this.mPanels;
                if (featureId < panelFeatureStateArr.length) {
                    panel = panelFeatureStateArr[featureId];
                }
            }
            if (panel != null) {
                menu = panel.menu;
            }
        }
        if ((panel == null || panel.isOpen) && !this.mIsDestroyed) {
            this.mOriginalWindowCallback.onPanelClosed(featureId, menu);
        }
    }

    PanelFeatureState findMenuPanel(Menu menu) {
        PanelFeatureState[] panels = this.mPanels;
        int N = panels != null ? panels.length : 0;
        for (int i = 0; i < N; i++) {
            PanelFeatureState panel = panels[i];
            if (panel != null && panel.menu == menu) {
                return panel;
            }
        }
        return null;
    }

    protected PanelFeatureState getPanelState(int featureId, boolean required) {
        PanelFeatureState[] panelFeatureStateArr = this.mPanels;
        PanelFeatureState[] ar = panelFeatureStateArr;
        if (panelFeatureStateArr == null || ar.length <= featureId) {
            panelFeatureStateArr = new PanelFeatureState[(featureId + 1)];
            if (ar != null) {
                System.arraycopy(ar, 0, panelFeatureStateArr, 0, ar.length);
            }
            ar = panelFeatureStateArr;
            this.mPanels = panelFeatureStateArr;
        }
        PanelFeatureState st = ar[featureId];
        if (st != null) {
            return st;
        }
        PanelFeatureState panelFeatureState = new PanelFeatureState(featureId);
        st = panelFeatureState;
        ar[featureId] = panelFeatureState;
        return st;
    }

    private boolean performPanelShortcut(PanelFeatureState st, int keyCode, KeyEvent event, int flags) {
        if (event.isSystem()) {
            return false;
        }
        boolean handled = false;
        if ((st.isPrepared || preparePanel(st, event)) && st.menu != null) {
            handled = st.menu.performShortcut(keyCode, event, flags);
        }
        if (handled && (flags & 1) == 0 && this.mDecorContentParent == null) {
            closePanel(st, true);
        }
        return handled;
    }

    private void invalidatePanelMenu(int featureId) {
        this.mInvalidatePanelMenuFeatures |= 1 << featureId;
        if (!this.mInvalidatePanelMenuPosted) {
            ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuPosted = true;
        }
    }

    void doInvalidatePanelMenu(int featureId) {
        PanelFeatureState st = getPanelState(featureId, true);
        if (st.menu != null) {
            Bundle savedActionViewStates = new Bundle();
            st.menu.saveActionViewStates(savedActionViewStates);
            if (savedActionViewStates.size() > 0) {
                st.frozenActionViewState = savedActionViewStates;
            }
            st.menu.stopDispatchingItemsChanged();
            st.menu.clear();
        }
        st.refreshMenuContent = true;
        st.refreshDecorView = true;
        if ((featureId == 108 || featureId == 0) && this.mDecorContentParent != null) {
            st = getPanelState(0, false);
            if (st != null) {
                st.isPrepared = false;
                preparePanel(st, null);
            }
        }
    }

    int updateStatusGuard(int insetTop) {
        boolean showStatusGuard = false;
        ActionBarContextView actionBarContextView = this.mActionModeView;
        int i = 0;
        if (actionBarContextView != null && (actionBarContextView.getLayoutParams() instanceof MarginLayoutParams)) {
            MarginLayoutParams mlp = (MarginLayoutParams) this.mActionModeView.getLayoutParams();
            boolean mlpChanged = false;
            if (this.mActionModeView.isShown()) {
                if (this.mTempRect1 == null) {
                    this.mTempRect1 = new Rect();
                    this.mTempRect2 = new Rect();
                }
                Rect insets = this.mTempRect1;
                Rect localInsets = this.mTempRect2;
                insets.set(0, insetTop, 0, 0);
                ViewUtils.computeFitSystemWindows(this.mSubDecor, insets, localInsets);
                if (mlp.topMargin != (localInsets.top == 0 ? insetTop : 0)) {
                    mlpChanged = true;
                    mlp.topMargin = insetTop;
                    LayoutParams lp = this.mStatusGuard;
                    if (lp == null) {
                        this.mStatusGuard = new View(this.mContext);
                        this.mStatusGuard.setBackgroundColor(this.mContext.getResources().getColor(C0185R.color.abc_input_method_navigation_guard));
                        this.mSubDecor.addView(this.mStatusGuard, -1, new LayoutParams(-1, insetTop));
                    } else {
                        lp = lp.getLayoutParams();
                        if (lp.height != insetTop) {
                            lp.height = insetTop;
                            this.mStatusGuard.setLayoutParams(lp);
                        }
                    }
                }
                showStatusGuard = this.mStatusGuard != null;
                if (!this.mOverlayActionMode && showStatusGuard) {
                    insetTop = 0;
                }
            } else if (mlp.topMargin != 0) {
                mlpChanged = true;
                mlp.topMargin = 0;
            }
            if (mlpChanged) {
                this.mActionModeView.setLayoutParams(mlp);
            }
        }
        View view = this.mStatusGuard;
        if (view != null) {
            if (!showStatusGuard) {
                i = 8;
            }
            view.setVisibility(i);
        }
        return insetTop;
    }

    private void throwFeatureRequestIfSubDecorInstalled() {
        if (this.mSubDecorInstalled) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }

    private int sanitizeWindowFeatureId(int featureId) {
        if (featureId == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        } else if (featureId != 9) {
            return featureId;
        } else {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
            return 109;
        }
    }

    ViewGroup getSubDecor() {
        return this.mSubDecor;
    }

    void dismissPopups() {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.dismissPopups();
        }
        if (this.mActionModePopup != null) {
            this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
            if (this.mActionModePopup.isShowing()) {
                try {
                    this.mActionModePopup.dismiss();
                } catch (IllegalArgumentException e) {
                }
            }
            this.mActionModePopup = null;
        }
        endOnGoingFadeAnimation();
        PanelFeatureState st = getPanelState(0, false);
        if (st != null && st.menu != null) {
            st.menu.close();
        }
    }

    public boolean applyDayNight() {
        boolean applied = false;
        int nightMode = getNightMode();
        int modeToApply = mapNightMode(nightMode);
        if (modeToApply != -1) {
            applied = updateForNightMode(modeToApply);
        }
        if (nightMode == 0) {
            ensureAutoNightModeManager();
            this.mAutoNightModeManager.setup();
        }
        this.mApplyDayNightCalled = true;
        return applied;
    }

    public void setLocalNightMode(int mode) {
        switch (mode) {
            case -1:
            case 0:
            case 1:
            case 2:
                if (this.mLocalNightMode != mode) {
                    this.mLocalNightMode = mode;
                    if (this.mApplyDayNightCalled) {
                        applyDayNight();
                        return;
                    }
                    return;
                }
                return;
            default:
                Log.i("AppCompatDelegate", "setLocalNightMode() called with an unknown mode");
                return;
        }
    }

    int mapNightMode(int mode) {
        if (mode == -100) {
            return -1;
        }
        if (mode != 0) {
            return mode;
        }
        if (VERSION.SDK_INT >= 23 && ((UiModeManager) this.mContext.getSystemService(UiModeManager.class)).getNightMode() == 0) {
            return -1;
        }
        ensureAutoNightModeManager();
        return this.mAutoNightModeManager.getApplyableNightMode();
    }

    private int getNightMode() {
        int i = this.mLocalNightMode;
        return i != -100 ? i : AppCompatDelegate.getDefaultNightMode();
    }

    private boolean updateForNightMode(int mode) {
        Resources res = this.mContext.getResources();
        Configuration conf = res.getConfiguration();
        int currentNightMode = conf.uiMode & 48;
        int newNightMode = mode == 2 ? 32 : 16;
        if (currentNightMode == newNightMode) {
            return false;
        }
        if (shouldRecreateOnNightModeChange()) {
            this.mContext.recreate();
        } else {
            Configuration config = new Configuration(conf);
            DisplayMetrics metrics = res.getDisplayMetrics();
            config.uiMode = (config.uiMode & -49) | newNightMode;
            res.updateConfiguration(config, metrics);
            if (VERSION.SDK_INT < 26) {
                ResourcesFlusher.flush(res);
            }
        }
        return true;
    }

    private void ensureAutoNightModeManager() {
        if (this.mAutoNightModeManager == null) {
            this.mAutoNightModeManager = new AutoNightModeManager(TwilightManager.getInstance(this.mContext));
        }
    }

    @VisibleForTesting
    final AutoNightModeManager getAutoNightModeManager() {
        ensureAutoNightModeManager();
        return this.mAutoNightModeManager;
    }

    private boolean shouldRecreateOnNightModeChange() {
        boolean e = false;
        if (this.mApplyDayNightCalled) {
            PackageManager pm = this.mContext;
            if (pm instanceof Activity) {
                try {
                    if ((pm.getPackageManager().getActivityInfo(new ComponentName(this.mContext, this.mContext.getClass()), 0).configChanges & 512) == 0) {
                        e = true;
                    }
                    return e;
                } catch (NameNotFoundException e2) {
                    Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", e2);
                    return true;
                }
            }
        }
        return false;
    }

    public final Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl();
    }
}
