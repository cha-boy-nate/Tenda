package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.database.DataSetObserver;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0185R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView {
    private static final int[] ATTRS_ANDROID_SPINNERMODE = new int[]{16843505};
    private static final int MAX_ITEMS_MEASURED = 15;
    private static final int MODE_DIALOG = 0;
    private static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "AppCompatSpinner";
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    int mDropDownWidth;
    private ForwardingListener mForwardingListener;
    DropdownPopup mPopup;
    private final Context mPopupContext;
    private final boolean mPopupSet;
    private SpinnerAdapter mTempAdapter;
    final Rect mTempRect;

    private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;

        public DropDownAdapter(@Nullable SpinnerAdapter adapter, @Nullable Theme dropDownTheme) {
            this.mAdapter = adapter;
            if (adapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter) adapter;
            }
            if (dropDownTheme == null) {
                return;
            }
            if (VERSION.SDK_INT >= 23 && (adapter instanceof ThemedSpinnerAdapter)) {
                ThemedSpinnerAdapter themedAdapter = (ThemedSpinnerAdapter) adapter;
                if (themedAdapter.getDropDownViewTheme() != dropDownTheme) {
                    themedAdapter.setDropDownViewTheme(dropDownTheme);
                }
            } else if (adapter instanceof ThemedSpinnerAdapter) {
                ThemedSpinnerAdapter themedAdapter2 = (ThemedSpinnerAdapter) adapter;
                if (themedAdapter2.getDropDownViewTheme() == null) {
                    themedAdapter2.setDropDownViewTheme(dropDownTheme);
                }
            }
        }

        public int getCount() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter == null ? 0 : spinnerAdapter.getCount();
        }

        public Object getItem(int position) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter == null ? null : spinnerAdapter.getItem(position);
        }

        public long getItemId(int position) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter == null ? -1 : spinnerAdapter.getItemId(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return getDropDownView(position, convertView, parent);
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getDropDownView(position, convertView, parent);
        }

        public boolean hasStableIds() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter != null && spinnerAdapter.hasStableIds();
        }

        public void registerDataSetObserver(DataSetObserver observer) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.registerDataSetObserver(observer);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.unregisterDataSetObserver(observer);
            }
        }

        public boolean areAllItemsEnabled() {
            ListAdapter adapter = this.mListAdapter;
            if (adapter != null) {
                return adapter.areAllItemsEnabled();
            }
            return true;
        }

        public boolean isEnabled(int position) {
            ListAdapter adapter = this.mListAdapter;
            if (adapter != null) {
                return adapter.isEnabled(position);
            }
            return true;
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean isEmpty() {
            return getCount() == 0;
        }
    }

    private class DropdownPopup extends ListPopupWindow {
        ListAdapter mAdapter;
        private CharSequence mHintText;
        private final Rect mVisibleRect = new Rect();

        /* renamed from: android.support.v7.widget.AppCompatSpinner$DropdownPopup$2 */
        class C02062 implements OnGlobalLayoutListener {
            C02062() {
            }

            public void onGlobalLayout() {
                DropdownPopup dropdownPopup = DropdownPopup.this;
                if (dropdownPopup.isVisibleToUser(AppCompatSpinner.this)) {
                    DropdownPopup.this.computeContentWidth();
                    super.show();
                    return;
                }
                DropdownPopup.this.dismiss();
            }
        }

        public DropdownPopup(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setAnchorView(AppCompatSpinner.this);
            setModal(true);
            setPromptPosition(0);
            setOnItemClickListener(new OnItemClickListener(AppCompatSpinner.this) {
                public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                    AppCompatSpinner.this.setSelection(position);
                    if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                        AppCompatSpinner.this.performItemClick(v, position, DropdownPopup.this.mAdapter.getItemId(position));
                    }
                    DropdownPopup.this.dismiss();
                }
            });
        }

        public void setAdapter(ListAdapter adapter) {
            super.setAdapter(adapter);
            this.mAdapter = adapter;
        }

        public CharSequence getHintText() {
            return this.mHintText;
        }

        public void setPromptText(CharSequence hintText) {
            this.mHintText = hintText;
        }

        void computeContentWidth() {
            Drawable background = getBackground();
            int hOffset = 0;
            if (background != null) {
                background.getPadding(AppCompatSpinner.this.mTempRect);
                hOffset = ViewUtils.isLayoutRtl(AppCompatSpinner.this) ? AppCompatSpinner.this.mTempRect.right : -AppCompatSpinner.this.mTempRect.left;
            } else {
                Rect rect = AppCompatSpinner.this.mTempRect;
                AppCompatSpinner.this.mTempRect.right = 0;
                rect.left = 0;
            }
            int spinnerPaddingLeft = AppCompatSpinner.this.getPaddingLeft();
            int spinnerPaddingRight = AppCompatSpinner.this.getPaddingRight();
            int spinnerWidth = AppCompatSpinner.this.getWidth();
            if (AppCompatSpinner.this.mDropDownWidth == -2) {
                int contentWidth = AppCompatSpinner.this.compatMeasureContentWidth((SpinnerAdapter) this.mAdapter, getBackground());
                int contentWidthLimit = (AppCompatSpinner.this.getContext().getResources().getDisplayMetrics().widthPixels - AppCompatSpinner.this.mTempRect.left) - AppCompatSpinner.this.mTempRect.right;
                if (contentWidth > contentWidthLimit) {
                    contentWidth = contentWidthLimit;
                }
                setContentWidth(Math.max(contentWidth, (spinnerWidth - spinnerPaddingLeft) - spinnerPaddingRight));
            } else if (AppCompatSpinner.this.mDropDownWidth == -1) {
                setContentWidth((spinnerWidth - spinnerPaddingLeft) - spinnerPaddingRight);
            } else {
                setContentWidth(AppCompatSpinner.this.mDropDownWidth);
            }
            if (ViewUtils.isLayoutRtl(AppCompatSpinner.this)) {
                hOffset += (spinnerWidth - spinnerPaddingRight) - getWidth();
            } else {
                hOffset += spinnerPaddingLeft;
            }
            setHorizontalOffset(hOffset);
        }

        public void show() {
            boolean wasShowing = isShowing();
            computeContentWidth();
            setInputMethodMode(2);
            super.show();
            getListView().setChoiceMode(1);
            setSelection(AppCompatSpinner.this.getSelectedItemPosition());
            if (!wasShowing) {
                ViewTreeObserver vto = AppCompatSpinner.this.getViewTreeObserver();
                if (vto != null) {
                    final OnGlobalLayoutListener layoutListener = new C02062();
                    vto.addOnGlobalLayoutListener(layoutListener);
                    setOnDismissListener(new OnDismissListener() {
                        public void onDismiss() {
                            ViewTreeObserver vto = AppCompatSpinner.this.getViewTreeObserver();
                            if (vto != null) {
                                vto.removeGlobalOnLayoutListener(layoutListener);
                            }
                        }
                    });
                }
            }
        }

        boolean isVisibleToUser(View view) {
            return ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(this.mVisibleRect);
        }
    }

    public AppCompatSpinner(Context context) {
        this(context, null);
    }

    public AppCompatSpinner(Context context, int mode) {
        this(context, null, C0185R.attr.spinnerStyle, mode);
    }

    public AppCompatSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, C0185R.attr.spinnerStyle);
    }

    public AppCompatSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public AppCompatSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        this(context, attrs, defStyleAttr, mode, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AppCompatSpinner(android.content.Context r9, android.util.AttributeSet r10, int r11, int r12, android.content.res.Resources.Theme r13) {
        /*
        r8 = this;
        r8.<init>(r9, r10, r11);
        r0 = new android.graphics.Rect;
        r0.<init>();
        r8.mTempRect = r0;
        r0 = android.support.v7.appcompat.C0185R.styleable.Spinner;
        r1 = 0;
        r0 = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(r9, r10, r0, r11, r1);
        r2 = new android.support.v7.widget.AppCompatBackgroundHelper;
        r2.<init>(r8);
        r8.mBackgroundTintHelper = r2;
        r2 = 0;
        if (r13 == 0) goto L_0x0023;
    L_0x001b:
        r3 = new android.support.v7.view.ContextThemeWrapper;
        r3.<init>(r9, r13);
        r8.mPopupContext = r3;
        goto L_0x003e;
    L_0x0023:
        r3 = android.support.v7.appcompat.C0185R.styleable.Spinner_popupTheme;
        r3 = r0.getResourceId(r3, r1);
        if (r3 == 0) goto L_0x0033;
    L_0x002b:
        r4 = new android.support.v7.view.ContextThemeWrapper;
        r4.<init>(r9, r3);
        r8.mPopupContext = r4;
        goto L_0x003e;
    L_0x0033:
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 23;
        if (r4 >= r5) goto L_0x003b;
    L_0x0039:
        r4 = r9;
        goto L_0x003c;
    L_0x003b:
        r4 = r2;
    L_0x003c:
        r8.mPopupContext = r4;
    L_0x003e:
        r3 = r8.mPopupContext;
        r4 = 1;
        if (r3 == 0) goto L_0x00ab;
    L_0x0043:
        r3 = -1;
        if (r12 != r3) goto L_0x0073;
    L_0x0046:
        r3 = 0;
        r5 = ATTRS_ANDROID_SPINNERMODE;	 Catch:{ Exception -> 0x0061 }
        r5 = r9.obtainStyledAttributes(r10, r5, r11, r1);	 Catch:{ Exception -> 0x0061 }
        r3 = r5;
        r5 = r3.hasValue(r1);	 Catch:{ Exception -> 0x0061 }
        if (r5 == 0) goto L_0x0059;
    L_0x0054:
        r5 = r3.getInt(r1, r1);	 Catch:{ Exception -> 0x0061 }
        r12 = r5;
    L_0x0059:
        if (r3 == 0) goto L_0x0073;
    L_0x005b:
        r3.recycle();
        goto L_0x0073;
    L_0x005f:
        r1 = move-exception;
        goto L_0x006d;
    L_0x0061:
        r5 = move-exception;
        r6 = "AppCompatSpinner";
        r7 = "Could not read android:spinnerMode";
        android.util.Log.i(r6, r7, r5);	 Catch:{ all -> 0x005f }
        if (r3 == 0) goto L_0x0073;
    L_0x006c:
        goto L_0x005b;
    L_0x006d:
        if (r3 == 0) goto L_0x0072;
    L_0x006f:
        r3.recycle();
    L_0x0072:
        throw r1;
    L_0x0073:
        if (r12 != r4) goto L_0x00ab;
    L_0x0075:
        r3 = new android.support.v7.widget.AppCompatSpinner$DropdownPopup;
        r5 = r8.mPopupContext;
        r3.<init>(r5, r10, r11);
        r5 = r8.mPopupContext;
        r6 = android.support.v7.appcompat.C0185R.styleable.Spinner;
        r1 = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(r5, r10, r6, r11, r1);
        r5 = android.support.v7.appcompat.C0185R.styleable.Spinner_android_dropDownWidth;
        r6 = -2;
        r5 = r1.getLayoutDimension(r5, r6);
        r8.mDropDownWidth = r5;
        r5 = android.support.v7.appcompat.C0185R.styleable.Spinner_android_popupBackground;
        r5 = r1.getDrawable(r5);
        r3.setBackgroundDrawable(r5);
        r5 = android.support.v7.appcompat.C0185R.styleable.Spinner_android_prompt;
        r5 = r0.getString(r5);
        r3.setPromptText(r5);
        r1.recycle();
        r8.mPopup = r3;
        r5 = new android.support.v7.widget.AppCompatSpinner$1;
        r5.<init>(r8, r3);
        r8.mForwardingListener = r5;
    L_0x00ab:
        r1 = android.support.v7.appcompat.C0185R.styleable.Spinner_android_entries;
        r1 = r0.getTextArray(r1);
        if (r1 == 0) goto L_0x00c3;
    L_0x00b3:
        r3 = new android.widget.ArrayAdapter;
        r5 = 17367048; // 0x1090008 float:2.5162948E-38 double:8.580462E-317;
        r3.<init>(r9, r5, r1);
        r5 = android.support.v7.appcompat.C0185R.layout.support_simple_spinner_dropdown_item;
        r3.setDropDownViewResource(r5);
        r8.setAdapter(r3);
    L_0x00c3:
        r0.recycle();
        r8.mPopupSet = r4;
        r3 = r8.mTempAdapter;
        if (r3 == 0) goto L_0x00d1;
    L_0x00cc:
        r8.setAdapter(r3);
        r8.mTempAdapter = r2;
    L_0x00d1:
        r2 = r8.mBackgroundTintHelper;
        r2.loadFromAttributes(r10, r11);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AppCompatSpinner.<init>(android.content.Context, android.util.AttributeSet, int, int, android.content.res.Resources$Theme):void");
    }

    public Context getPopupContext() {
        if (this.mPopup != null) {
            return this.mPopupContext;
        }
        if (VERSION.SDK_INT >= 23) {
            return super.getPopupContext();
        }
        return null;
    }

    public void setPopupBackgroundDrawable(Drawable background) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setBackgroundDrawable(background);
        } else if (VERSION.SDK_INT >= 16) {
            super.setPopupBackgroundDrawable(background);
        }
    }

    public void setPopupBackgroundResource(@DrawableRes int resId) {
        setPopupBackgroundDrawable(AppCompatResources.getDrawable(getPopupContext(), resId));
    }

    public Drawable getPopupBackground() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            return dropdownPopup.getBackground();
        }
        if (VERSION.SDK_INT >= 16) {
            return super.getPopupBackground();
        }
        return null;
    }

    public void setDropDownVerticalOffset(int pixels) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setVerticalOffset(pixels);
        } else if (VERSION.SDK_INT >= 16) {
            super.setDropDownVerticalOffset(pixels);
        }
    }

    public int getDropDownVerticalOffset() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            return dropdownPopup.getVerticalOffset();
        }
        if (VERSION.SDK_INT >= 16) {
            return super.getDropDownVerticalOffset();
        }
        return 0;
    }

    public void setDropDownHorizontalOffset(int pixels) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setHorizontalOffset(pixels);
        } else if (VERSION.SDK_INT >= 16) {
            super.setDropDownHorizontalOffset(pixels);
        }
    }

    public int getDropDownHorizontalOffset() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            return dropdownPopup.getHorizontalOffset();
        }
        if (VERSION.SDK_INT >= 16) {
            return super.getDropDownHorizontalOffset();
        }
        return 0;
    }

    public void setDropDownWidth(int pixels) {
        if (this.mPopup != null) {
            this.mDropDownWidth = pixels;
        } else if (VERSION.SDK_INT >= 16) {
            super.setDropDownWidth(pixels);
        }
    }

    public int getDropDownWidth() {
        if (this.mPopup != null) {
            return this.mDropDownWidth;
        }
        if (VERSION.SDK_INT >= 16) {
            return super.getDropDownWidth();
        }
        return 0;
    }

    public void setAdapter(SpinnerAdapter adapter) {
        if (this.mPopupSet) {
            super.setAdapter(adapter);
            if (this.mPopup != null) {
                Context popupContext = this.mPopupContext;
                if (popupContext == null) {
                    popupContext = getContext();
                }
                this.mPopup.setAdapter(new DropDownAdapter(adapter, popupContext.getTheme()));
            }
            return;
        }
        this.mTempAdapter = adapter;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null && dropdownPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        ForwardingListener forwardingListener = this.mForwardingListener;
        if (forwardingListener == null || !forwardingListener.onTouch(this, event)) {
            return super.onTouchEvent(event);
        }
        return true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mPopup != null && MeasureSpec.getMode(widthMeasureSpec) == Integer.MIN_VALUE) {
            setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), compatMeasureContentWidth(getAdapter(), getBackground())), MeasureSpec.getSize(widthMeasureSpec)), getMeasuredHeight());
        }
    }

    public boolean performClick() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup == null) {
            return super.performClick();
        }
        if (!dropdownPopup.isShowing()) {
            this.mPopup.show();
        }
        return true;
    }

    public void setPrompt(CharSequence prompt) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setPromptText(prompt);
        } else {
            super.setPrompt(prompt);
        }
    }

    public CharSequence getPrompt() {
        DropdownPopup dropdownPopup = this.mPopup;
        return dropdownPopup != null ? dropdownPopup.getHintText() : super.getPrompt();
    }

    public void setBackgroundResource(@DrawableRes int resId) {
        super.setBackgroundResource(resId);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(resId);
        }
    }

    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable(background);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setSupportBackgroundTintList(@Nullable ColorStateList tint) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintList(tint);
        }
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP})
    public ColorStateList getSupportBackgroundTintList() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        return appCompatBackgroundHelper != null ? appCompatBackgroundHelper.getSupportBackgroundTintList() : null;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setSupportBackgroundTintMode(@Nullable Mode tintMode) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintMode(tintMode);
        }
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP})
    public Mode getSupportBackgroundTintMode() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        return appCompatBackgroundHelper != null ? appCompatBackgroundHelper.getSupportBackgroundTintMode() : null;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.applySupportBackgroundTint();
        }
    }

    int compatMeasureContentWidth(SpinnerAdapter adapter, Drawable background) {
        if (adapter == null) {
            return 0;
        }
        int width = 0;
        View itemView = null;
        int itemType = 0;
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
        int start = Math.max(0, getSelectedItemPosition());
        int end = Math.min(adapter.getCount(), start + 15);
        for (start = Math.max(0, start - (15 - (end - start))); start < end; start++) {
            int positionType = adapter.getItemViewType(start);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            itemView = adapter.getView(start, itemView, this);
            if (itemView.getLayoutParams() == null) {
                itemView.setLayoutParams(new LayoutParams(-2, -2));
            }
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            width = Math.max(width, itemView.getMeasuredWidth());
        }
        if (background != null) {
            background.getPadding(this.mTempRect);
            width += this.mTempRect.left + this.mTempRect.right;
        }
        return width;
    }
}
