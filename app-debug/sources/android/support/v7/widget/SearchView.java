package android.support.v7.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.C0185R;
import android.support.v7.view.CollapsibleActionView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.google.android.gms.actions.SearchIntents;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class SearchView extends LinearLayoutCompat implements CollapsibleActionView {
    static final boolean DBG = false;
    static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();
    private static final String IME_OPTION_NO_MICROPHONE = "nm";
    static final String LOG_TAG = "SearchView";
    private Bundle mAppSearchData;
    private boolean mClearingFocus;
    final ImageView mCloseButton;
    private final ImageView mCollapsedIcon;
    private int mCollapsedImeOptions;
    private final CharSequence mDefaultQueryHint;
    private final View mDropDownAnchor;
    private boolean mExpandedInActionView;
    final ImageView mGoButton;
    private boolean mIconified;
    private boolean mIconifiedByDefault;
    private int mMaxWidth;
    private CharSequence mOldQueryText;
    private final OnClickListener mOnClickListener;
    private OnCloseListener mOnCloseListener;
    private final OnEditorActionListener mOnEditorActionListener;
    private final OnItemClickListener mOnItemClickListener;
    private final OnItemSelectedListener mOnItemSelectedListener;
    private OnQueryTextListener mOnQueryChangeListener;
    OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private OnClickListener mOnSearchClickListener;
    private OnSuggestionListener mOnSuggestionListener;
    private final WeakHashMap<String, ConstantState> mOutsideDrawablesCache;
    private CharSequence mQueryHint;
    private boolean mQueryRefinement;
    private Runnable mReleaseCursorRunnable;
    final ImageView mSearchButton;
    private final View mSearchEditFrame;
    private final Drawable mSearchHintIcon;
    private final View mSearchPlate;
    final SearchAutoComplete mSearchSrcTextView;
    private Rect mSearchSrcTextViewBounds;
    private Rect mSearchSrtTextViewBoundsExpanded;
    SearchableInfo mSearchable;
    private final View mSubmitArea;
    private boolean mSubmitButtonEnabled;
    private final int mSuggestionCommitIconResId;
    private final int mSuggestionRowLayout;
    CursorAdapter mSuggestionsAdapter;
    private int[] mTemp;
    private int[] mTemp2;
    OnKeyListener mTextKeyListener;
    private TextWatcher mTextWatcher;
    private UpdatableTouchDelegate mTouchDelegate;
    private final Runnable mUpdateDrawableStateRunnable;
    private CharSequence mUserQuery;
    private final Intent mVoiceAppSearchIntent;
    final ImageView mVoiceButton;
    private boolean mVoiceButtonEnabled;
    private final Intent mVoiceWebSearchIntent;

    /* renamed from: android.support.v7.widget.SearchView$1 */
    class C02121 implements Runnable {
        C02121() {
        }

        public void run() {
            SearchView.this.updateFocusedState();
        }
    }

    /* renamed from: android.support.v7.widget.SearchView$2 */
    class C02132 implements Runnable {
        C02132() {
        }

        public void run() {
            if (SearchView.this.mSuggestionsAdapter != null && (SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter)) {
                SearchView.this.mSuggestionsAdapter.changeCursor(null);
            }
        }
    }

    /* renamed from: android.support.v7.widget.SearchView$3 */
    class C02143 implements OnFocusChangeListener {
        C02143() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (SearchView.this.mOnQueryTextFocusChangeListener != null) {
                SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange(SearchView.this, hasFocus);
            }
        }
    }

    /* renamed from: android.support.v7.widget.SearchView$4 */
    class C02154 implements OnLayoutChangeListener {
        C02154() {
        }

        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            SearchView.this.adjustDropDownSizeAndPosition();
        }
    }

    /* renamed from: android.support.v7.widget.SearchView$5 */
    class C02165 implements OnClickListener {
        C02165() {
        }

        public void onClick(View v) {
            if (v == SearchView.this.mSearchButton) {
                SearchView.this.onSearchClicked();
            } else if (v == SearchView.this.mCloseButton) {
                SearchView.this.onCloseClicked();
            } else if (v == SearchView.this.mGoButton) {
                SearchView.this.onSubmitQuery();
            } else if (v == SearchView.this.mVoiceButton) {
                SearchView.this.onVoiceClicked();
            } else if (v == SearchView.this.mSearchSrcTextView) {
                SearchView.this.forceSuggestionQuery();
            }
        }
    }

    /* renamed from: android.support.v7.widget.SearchView$6 */
    class C02176 implements OnKeyListener {
        C02176() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (SearchView.this.mSearchable == null) {
                return false;
            }
            if (SearchView.this.mSearchSrcTextView.isPopupShowing() && SearchView.this.mSearchSrcTextView.getListSelection() != -1) {
                return SearchView.this.onSuggestionsKey(v, keyCode, event);
            }
            if (SearchView.this.mSearchSrcTextView.isEmpty() || !event.hasNoModifiers() || event.getAction() != 1 || keyCode != 66) {
                return false;
            }
            v.cancelLongPress();
            SearchView searchView = SearchView.this;
            searchView.launchQuerySearch(0, null, searchView.mSearchSrcTextView.getText().toString());
            return true;
        }
    }

    /* renamed from: android.support.v7.widget.SearchView$7 */
    class C02187 implements OnEditorActionListener {
        C02187() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            SearchView.this.onSubmitQuery();
            return true;
        }
    }

    /* renamed from: android.support.v7.widget.SearchView$8 */
    class C02198 implements OnItemClickListener {
        C02198() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            SearchView.this.onItemClicked(position, 0, null);
        }
    }

    /* renamed from: android.support.v7.widget.SearchView$9 */
    class C02209 implements OnItemSelectedListener {
        C02209() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            SearchView.this.onItemSelected(position);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private static class AutoCompleteTextViewReflector {
        private Method doAfterTextChanged;
        private Method doBeforeTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;

        AutoCompleteTextViewReflector() {
            try {
                this.doBeforeTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
                this.doBeforeTextChanged.setAccessible(true);
            } catch (NoSuchMethodException e) {
            }
            try {
                this.doAfterTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
                this.doAfterTextChanged.setAccessible(true);
            } catch (NoSuchMethodException e2) {
            }
            try {
                this.ensureImeVisible = AutoCompleteTextView.class.getMethod("ensureImeVisible", new Class[]{Boolean.TYPE});
                this.ensureImeVisible.setAccessible(true);
            } catch (NoSuchMethodException e3) {
            }
        }

        void doBeforeTextChanged(AutoCompleteTextView view) {
            Method method = this.doBeforeTextChanged;
            if (method != null) {
                try {
                    method.invoke(view, new Object[0]);
                } catch (Exception e) {
                }
            }
        }

        void doAfterTextChanged(AutoCompleteTextView view) {
            Method method = this.doAfterTextChanged;
            if (method != null) {
                try {
                    method.invoke(view, new Object[0]);
                } catch (Exception e) {
                }
            }
        }

        void ensureImeVisible(AutoCompleteTextView view, boolean visible) {
            Method method = this.ensureImeVisible;
            if (method != null) {
                try {
                    method.invoke(view, new Object[]{Boolean.valueOf(visible)});
                } catch (Exception e) {
                }
            }
        }
    }

    public interface OnCloseListener {
        boolean onClose();
    }

    public interface OnQueryTextListener {
        boolean onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    public interface OnSuggestionListener {
        boolean onSuggestionClick(int i);

        boolean onSuggestionSelect(int i);
    }

    private static class UpdatableTouchDelegate extends TouchDelegate {
        private final Rect mActualBounds = new Rect();
        private boolean mDelegateTargeted;
        private final View mDelegateView;
        private final int mSlop;
        private final Rect mSlopBounds = new Rect();
        private final Rect mTargetBounds = new Rect();

        public UpdatableTouchDelegate(Rect targetBounds, Rect actualBounds, View delegateView) {
            super(targetBounds, delegateView);
            this.mSlop = ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
            setBounds(targetBounds, actualBounds);
            this.mDelegateView = delegateView;
        }

        public void setBounds(Rect desiredBounds, Rect actualBounds) {
            this.mTargetBounds.set(desiredBounds);
            this.mSlopBounds.set(desiredBounds);
            Rect rect = this.mSlopBounds;
            int i = this.mSlop;
            rect.inset(-i, -i);
            this.mActualBounds.set(actualBounds);
        }

        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            boolean sendToDelegate = false;
            boolean hit = true;
            switch (event.getAction()) {
                case 0:
                    if (this.mTargetBounds.contains(x, y)) {
                        this.mDelegateTargeted = true;
                        sendToDelegate = true;
                        break;
                    }
                    break;
                case 1:
                case 2:
                    sendToDelegate = this.mDelegateTargeted;
                    if (sendToDelegate && !this.mSlopBounds.contains(x, y)) {
                        hit = false;
                        break;
                    }
                case 3:
                    sendToDelegate = this.mDelegateTargeted;
                    this.mDelegateTargeted = false;
                    break;
                default:
                    break;
            }
            if (!sendToDelegate) {
                return false;
            }
            if (!hit || this.mActualBounds.contains(x, y)) {
                event.setLocation((float) (x - this.mActualBounds.left), (float) (y - this.mActualBounds.top));
            } else {
                event.setLocation((float) (this.mDelegateView.getWidth() / 2), (float) (this.mDelegateView.getHeight() / 2));
            }
            return this.mDelegateView.dispatchTouchEvent(event);
        }
    }

    static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new C02211();
        boolean isIconified;

        /* renamed from: android.support.v7.widget.SearchView$SavedState$1 */
        static class C02211 implements ClassLoaderCreator<SavedState> {
            C02211() {
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

        SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.isIconified = ((Boolean) source.readValue(null)).booleanValue();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeValue(Boolean.valueOf(this.isIconified));
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SearchView.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" isIconified=");
            stringBuilder.append(this.isIconified);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static class SearchAutoComplete extends AppCompatAutoCompleteTextView {
        private boolean mHasPendingShowSoftInputRequest;
        final Runnable mRunShowSoftInputIfNecessary;
        private SearchView mSearchView;
        private int mThreshold;

        /* renamed from: android.support.v7.widget.SearchView$SearchAutoComplete$1 */
        class C02221 implements Runnable {
            C02221() {
            }

            public void run() {
                SearchAutoComplete.this.showSoftInputIfNecessary();
            }
        }

        public SearchAutoComplete(Context context) {
            this(context, null);
        }

        public SearchAutoComplete(Context context, AttributeSet attrs) {
            this(context, attrs, C0185R.attr.autoCompleteTextViewStyle);
        }

        public SearchAutoComplete(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.mRunShowSoftInputIfNecessary = new C02221();
            this.mThreshold = getThreshold();
        }

        protected void onFinishInflate() {
            super.onFinishInflate();
            setMinWidth((int) TypedValue.applyDimension(1, (float) getSearchViewTextMinWidthDp(), getResources().getDisplayMetrics()));
        }

        void setSearchView(SearchView searchView) {
            this.mSearchView = searchView;
        }

        public void setThreshold(int threshold) {
            super.setThreshold(threshold);
            this.mThreshold = threshold;
        }

        boolean isEmpty() {
            return TextUtils.getTrimmedLength(getText()) == 0;
        }

        protected void replaceText(CharSequence text) {
        }

        public void performCompletion() {
        }

        public void onWindowFocusChanged(boolean hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
            if (hasWindowFocus && this.mSearchView.hasFocus() && getVisibility() == 0) {
                this.mHasPendingShowSoftInputRequest = true;
                if (SearchView.isLandscapeMode(getContext())) {
                    SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
                }
            }
        }

        protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            this.mSearchView.onTextFocusChanged();
        }

        public boolean enoughToFilter() {
            if (this.mThreshold > 0) {
                if (!super.enoughToFilter()) {
                    return false;
                }
            }
            return true;
        }

        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            if (keyCode == 4) {
                DispatcherState state;
                if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                    state = getKeyDispatcherState();
                    if (state != null) {
                        state.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == 1) {
                    state = getKeyDispatcherState();
                    if (state != null) {
                        state.handleUpEvent(event);
                    }
                    if (event.isTracking() && !event.isCanceled()) {
                        this.mSearchView.clearFocus();
                        setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(keyCode, event);
        }

        private int getSearchViewTextMinWidthDp() {
            Configuration config = getResources().getConfiguration();
            int widthDp = config.screenWidthDp;
            int heightDp = config.screenHeightDp;
            if (widthDp >= 960 && heightDp >= 720 && config.orientation == 2) {
                return 256;
            }
            if (widthDp < 600) {
                if (widthDp < 640 || heightDp < 480) {
                    return 160;
                }
            }
            return 192;
        }

        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            InputConnection ic = super.onCreateInputConnection(editorInfo);
            if (this.mHasPendingShowSoftInputRequest) {
                removeCallbacks(this.mRunShowSoftInputIfNecessary);
                post(this.mRunShowSoftInputIfNecessary);
            }
            return ic;
        }

        void showSoftInputIfNecessary() {
            if (this.mHasPendingShowSoftInputRequest) {
                ((InputMethodManager) getContext().getSystemService("input_method")).showSoftInput(this, 0);
                this.mHasPendingShowSoftInputRequest = false;
            }
        }

        void setImeVisibility(boolean visible) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService("input_method");
            if (!visible) {
                this.mHasPendingShowSoftInputRequest = false;
                removeCallbacks(this.mRunShowSoftInputIfNecessary);
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (imm.isActive(this)) {
                this.mHasPendingShowSoftInputRequest = false;
                removeCallbacks(this.mRunShowSoftInputIfNecessary);
                imm.showSoftInput(this, 0);
            } else {
                this.mHasPendingShowSoftInputRequest = true;
            }
        }
    }

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, C0185R.attr.searchViewStyle);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mSearchSrcTextViewBounds = new Rect();
        this.mSearchSrtTextViewBoundsExpanded = new Rect();
        this.mTemp = new int[2];
        this.mTemp2 = new int[2];
        this.mUpdateDrawableStateRunnable = new C02121();
        this.mReleaseCursorRunnable = new C02132();
        this.mOutsideDrawablesCache = new WeakHashMap();
        this.mOnClickListener = new C02165();
        this.mTextKeyListener = new C02176();
        this.mOnEditorActionListener = new C02187();
        this.mOnItemClickListener = new C02198();
        this.mOnItemSelectedListener = new C02209();
        this.mTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int before, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int after) {
                SearchView.this.onTextChanged(s);
            }

            public void afterTextChanged(Editable s) {
            }
        };
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, C0185R.styleable.SearchView, defStyleAttr, 0);
        LayoutInflater.from(context).inflate(a.getResourceId(C0185R.styleable.SearchView_layout, C0185R.layout.abc_search_view), this, true);
        this.mSearchSrcTextView = (SearchAutoComplete) findViewById(C0185R.id.search_src_text);
        this.mSearchSrcTextView.setSearchView(this);
        this.mSearchEditFrame = findViewById(C0185R.id.search_edit_frame);
        this.mSearchPlate = findViewById(C0185R.id.search_plate);
        this.mSubmitArea = findViewById(C0185R.id.submit_area);
        this.mSearchButton = (ImageView) findViewById(C0185R.id.search_button);
        this.mGoButton = (ImageView) findViewById(C0185R.id.search_go_btn);
        this.mCloseButton = (ImageView) findViewById(C0185R.id.search_close_btn);
        this.mVoiceButton = (ImageView) findViewById(C0185R.id.search_voice_btn);
        this.mCollapsedIcon = (ImageView) findViewById(C0185R.id.search_mag_icon);
        ViewCompat.setBackground(this.mSearchPlate, a.getDrawable(C0185R.styleable.SearchView_queryBackground));
        ViewCompat.setBackground(this.mSubmitArea, a.getDrawable(C0185R.styleable.SearchView_submitBackground));
        this.mSearchButton.setImageDrawable(a.getDrawable(C0185R.styleable.SearchView_searchIcon));
        this.mGoButton.setImageDrawable(a.getDrawable(C0185R.styleable.SearchView_goIcon));
        this.mCloseButton.setImageDrawable(a.getDrawable(C0185R.styleable.SearchView_closeIcon));
        this.mVoiceButton.setImageDrawable(a.getDrawable(C0185R.styleable.SearchView_voiceIcon));
        this.mCollapsedIcon.setImageDrawable(a.getDrawable(C0185R.styleable.SearchView_searchIcon));
        this.mSearchHintIcon = a.getDrawable(C0185R.styleable.SearchView_searchHintIcon);
        TooltipCompat.setTooltipText(this.mSearchButton, getResources().getString(C0185R.string.abc_searchview_description_search));
        this.mSuggestionRowLayout = a.getResourceId(C0185R.styleable.SearchView_suggestionRowLayout, C0185R.layout.abc_search_dropdown_item_icons_2line);
        this.mSuggestionCommitIconResId = a.getResourceId(C0185R.styleable.SearchView_commitIcon, 0);
        this.mSearchButton.setOnClickListener(this.mOnClickListener);
        this.mCloseButton.setOnClickListener(this.mOnClickListener);
        this.mGoButton.setOnClickListener(this.mOnClickListener);
        this.mVoiceButton.setOnClickListener(this.mOnClickListener);
        this.mSearchSrcTextView.setOnClickListener(this.mOnClickListener);
        this.mSearchSrcTextView.addTextChangedListener(this.mTextWatcher);
        this.mSearchSrcTextView.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mSearchSrcTextView.setOnItemClickListener(this.mOnItemClickListener);
        this.mSearchSrcTextView.setOnItemSelectedListener(this.mOnItemSelectedListener);
        this.mSearchSrcTextView.setOnKeyListener(this.mTextKeyListener);
        this.mSearchSrcTextView.setOnFocusChangeListener(new C02143());
        setIconifiedByDefault(a.getBoolean(C0185R.styleable.SearchView_iconifiedByDefault, true));
        int maxWidth = a.getDimensionPixelSize(C0185R.styleable.SearchView_android_maxWidth, -1);
        if (maxWidth != -1) {
            setMaxWidth(maxWidth);
        }
        this.mDefaultQueryHint = a.getText(C0185R.styleable.SearchView_defaultQueryHint);
        this.mQueryHint = a.getText(C0185R.styleable.SearchView_queryHint);
        int imeOptions = a.getInt(C0185R.styleable.SearchView_android_imeOptions, -1);
        if (imeOptions != -1) {
            setImeOptions(imeOptions);
        }
        int inputType = a.getInt(C0185R.styleable.SearchView_android_inputType, -1);
        if (inputType != -1) {
            setInputType(inputType);
        }
        setFocusable(a.getBoolean(C0185R.styleable.SearchView_android_focusable, true));
        a.recycle();
        this.mVoiceWebSearchIntent = new Intent("android.speech.action.WEB_SEARCH");
        this.mVoiceWebSearchIntent.addFlags(268435456);
        this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        this.mVoiceAppSearchIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.mVoiceAppSearchIntent.addFlags(268435456);
        this.mDropDownAnchor = findViewById(this.mSearchSrcTextView.getDropDownAnchor());
        View view = this.mDropDownAnchor;
        if (view != null) {
            view.addOnLayoutChangeListener(new C02154());
        }
        updateViewsVisibility(this.mIconifiedByDefault);
        updateQueryHint();
    }

    int getSuggestionRowLayout() {
        return this.mSuggestionRowLayout;
    }

    int getSuggestionCommitIconResId() {
        return this.mSuggestionCommitIconResId;
    }

    public void setSearchableInfo(SearchableInfo searchable) {
        this.mSearchable = searchable;
        if (this.mSearchable != null) {
            updateSearchAutoComplete();
            updateQueryHint();
        }
        this.mVoiceButtonEnabled = hasVoiceSearch();
        if (this.mVoiceButtonEnabled) {
            this.mSearchSrcTextView.setPrivateImeOptions(IME_OPTION_NO_MICROPHONE);
        }
        updateViewsVisibility(isIconified());
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setAppSearchData(Bundle appSearchData) {
        this.mAppSearchData = appSearchData;
    }

    public void setImeOptions(int imeOptions) {
        this.mSearchSrcTextView.setImeOptions(imeOptions);
    }

    public int getImeOptions() {
        return this.mSearchSrcTextView.getImeOptions();
    }

    public void setInputType(int inputType) {
        this.mSearchSrcTextView.setInputType(inputType);
    }

    public int getInputType() {
        return this.mSearchSrcTextView.getInputType();
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (this.mClearingFocus || !isFocusable()) {
            return false;
        }
        if (isIconified()) {
            return super.requestFocus(direction, previouslyFocusedRect);
        }
        boolean result = this.mSearchSrcTextView.requestFocus(direction, previouslyFocusedRect);
        if (result) {
            updateViewsVisibility(false);
        }
        return result;
    }

    public void clearFocus() {
        this.mClearingFocus = true;
        super.clearFocus();
        this.mSearchSrcTextView.clearFocus();
        this.mSearchSrcTextView.setImeVisibility(false);
        this.mClearingFocus = false;
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        this.mOnQueryChangeListener = listener;
    }

    public void setOnCloseListener(OnCloseListener listener) {
        this.mOnCloseListener = listener;
    }

    public void setOnQueryTextFocusChangeListener(OnFocusChangeListener listener) {
        this.mOnQueryTextFocusChangeListener = listener;
    }

    public void setOnSuggestionListener(OnSuggestionListener listener) {
        this.mOnSuggestionListener = listener;
    }

    public void setOnSearchClickListener(OnClickListener listener) {
        this.mOnSearchClickListener = listener;
    }

    public CharSequence getQuery() {
        return this.mSearchSrcTextView.getText();
    }

    public void setQuery(CharSequence query, boolean submit) {
        this.mSearchSrcTextView.setText(query);
        if (query != null) {
            SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
            searchAutoComplete.setSelection(searchAutoComplete.length());
            this.mUserQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    public void setQueryHint(@Nullable CharSequence hint) {
        this.mQueryHint = hint;
        updateQueryHint();
    }

    @Nullable
    public CharSequence getQueryHint() {
        if (this.mQueryHint != null) {
            return this.mQueryHint;
        }
        SearchableInfo searchableInfo = this.mSearchable;
        if (searchableInfo == null || searchableInfo.getHintId() == 0) {
            return this.mDefaultQueryHint;
        }
        return getContext().getText(this.mSearchable.getHintId());
    }

    public void setIconifiedByDefault(boolean iconified) {
        if (this.mIconifiedByDefault != iconified) {
            this.mIconifiedByDefault = iconified;
            updateViewsVisibility(iconified);
            updateQueryHint();
        }
    }

    public boolean isIconfiedByDefault() {
        return this.mIconifiedByDefault;
    }

    public void setIconified(boolean iconify) {
        if (iconify) {
            onCloseClicked();
        } else {
            onSearchClicked();
        }
    }

    public boolean isIconified() {
        return this.mIconified;
    }

    public void setSubmitButtonEnabled(boolean enabled) {
        this.mSubmitButtonEnabled = enabled;
        updateViewsVisibility(isIconified());
    }

    public boolean isSubmitButtonEnabled() {
        return this.mSubmitButtonEnabled;
    }

    public void setQueryRefinementEnabled(boolean enable) {
        this.mQueryRefinement = enable;
        CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
        if (cursorAdapter instanceof SuggestionsAdapter) {
            ((SuggestionsAdapter) cursorAdapter).setQueryRefinement(enable ? 2 : 1);
        }
    }

    public boolean isQueryRefinementEnabled() {
        return this.mQueryRefinement;
    }

    public void setSuggestionsAdapter(CursorAdapter adapter) {
        this.mSuggestionsAdapter = adapter;
        this.mSearchSrcTextView.setAdapter(this.mSuggestionsAdapter);
    }

    public CursorAdapter getSuggestionsAdapter() {
        return this.mSuggestionsAdapter;
    }

    public void setMaxWidth(int maxpixels) {
        this.mMaxWidth = maxpixels;
        requestLayout();
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isIconified()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int i;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == Integer.MIN_VALUE) {
            i = this.mMaxWidth;
            width = i > 0 ? Math.min(i, width) : Math.min(getPreferredWidth(), width);
        } else if (widthMode == 0) {
            i = this.mMaxWidth;
            if (i <= 0) {
                i = getPreferredWidth();
            }
            width = i;
        } else if (widthMode == 1073741824) {
            i = this.mMaxWidth;
            if (i > 0) {
                width = Math.min(i, width);
            }
        }
        i = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (i == Integer.MIN_VALUE) {
            height = Math.min(getPreferredHeight(), height);
        } else if (i == 0) {
            height = getPreferredHeight();
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            getChildBoundsWithinSearchView(this.mSearchSrcTextView, this.mSearchSrcTextViewBounds);
            this.mSearchSrtTextViewBoundsExpanded.set(this.mSearchSrcTextViewBounds.left, 0, this.mSearchSrcTextViewBounds.right, bottom - top);
            UpdatableTouchDelegate updatableTouchDelegate = this.mTouchDelegate;
            if (updatableTouchDelegate == null) {
                this.mTouchDelegate = new UpdatableTouchDelegate(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds, this.mSearchSrcTextView);
                setTouchDelegate(this.mTouchDelegate);
                return;
            }
            updatableTouchDelegate.setBounds(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds);
        }
    }

    private void getChildBoundsWithinSearchView(View view, Rect rect) {
        view.getLocationInWindow(this.mTemp);
        getLocationInWindow(this.mTemp2);
        int[] iArr = this.mTemp;
        int top = iArr[1];
        int[] iArr2 = this.mTemp2;
        top -= iArr2[1];
        int left = iArr[0] - iArr2[0];
        rect.set(left, top, view.getWidth() + left, view.getHeight() + top);
    }

    private int getPreferredWidth() {
        return getContext().getResources().getDimensionPixelSize(C0185R.dimen.abc_search_view_preferred_width);
    }

    private int getPreferredHeight() {
        return getContext().getResources().getDimensionPixelSize(C0185R.dimen.abc_search_view_preferred_height);
    }

    private void updateViewsVisibility(boolean collapsed) {
        this.mIconified = collapsed;
        int i = 8;
        boolean z = false;
        boolean hasText = TextUtils.isEmpty(this.mSearchSrcTextView.getText()) ^ true;
        this.mSearchButton.setVisibility(collapsed ? 0 : 8);
        updateSubmitButton(hasText);
        View view = this.mSearchEditFrame;
        if (!collapsed) {
            i = 0;
        }
        view.setVisibility(i);
        if (this.mCollapsedIcon.getDrawable() != null) {
            if (!this.mIconifiedByDefault) {
                i = 0;
                this.mCollapsedIcon.setVisibility(i);
                updateCloseButton();
                if (!hasText) {
                    z = true;
                }
                updateVoiceButton(z);
                updateSubmitArea();
            }
        }
        i = 8;
        this.mCollapsedIcon.setVisibility(i);
        updateCloseButton();
        if (hasText) {
            z = true;
        }
        updateVoiceButton(z);
        updateSubmitArea();
    }

    private boolean hasVoiceSearch() {
        SearchableInfo searchableInfo = this.mSearchable;
        boolean z = false;
        if (searchableInfo != null && searchableInfo.getVoiceSearchEnabled()) {
            Intent testIntent = null;
            if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
                testIntent = this.mVoiceWebSearchIntent;
            } else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
                testIntent = this.mVoiceAppSearchIntent;
            }
            if (testIntent != null) {
                if (getContext().getPackageManager().resolveActivity(testIntent, 65536) != null) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    private boolean isSubmitAreaEnabled() {
        return (this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !isIconified();
    }

    private void updateSubmitButton(boolean hasText) {
        int visibility = 8;
        if (this.mSubmitButtonEnabled && isSubmitAreaEnabled() && hasFocus() && (hasText || !this.mVoiceButtonEnabled)) {
            visibility = 0;
        }
        this.mGoButton.setVisibility(visibility);
    }

    private void updateSubmitArea() {
        int visibility = 8;
        if (isSubmitAreaEnabled() && (this.mGoButton.getVisibility() == 0 || this.mVoiceButton.getVisibility() == 0)) {
            visibility = 0;
        }
        this.mSubmitArea.setVisibility(visibility);
    }

    private void updateCloseButton() {
        boolean showClose = true;
        boolean hasText = TextUtils.isEmpty(this.mSearchSrcTextView.getText()) ^ true;
        int i = 0;
        if (!hasText) {
            if (!this.mIconifiedByDefault || this.mExpandedInActionView) {
                showClose = false;
            }
        }
        ImageView imageView = this.mCloseButton;
        if (!showClose) {
            i = 8;
        }
        imageView.setVisibility(i);
        Drawable closeButtonImg = this.mCloseButton.getDrawable();
        if (closeButtonImg != null) {
            closeButtonImg.setState(hasText ? ENABLED_STATE_SET : EMPTY_STATE_SET);
        }
    }

    private void postUpdateFocusedState() {
        post(this.mUpdateDrawableStateRunnable);
    }

    void updateFocusedState() {
        int[] stateSet = this.mSearchSrcTextView.hasFocus() ? FOCUSED_STATE_SET : EMPTY_STATE_SET;
        Drawable searchPlateBg = this.mSearchPlate.getBackground();
        if (searchPlateBg != null) {
            searchPlateBg.setState(stateSet);
        }
        Drawable submitAreaBg = this.mSubmitArea.getBackground();
        if (submitAreaBg != null) {
            submitAreaBg.setState(stateSet);
        }
        invalidate();
    }

    protected void onDetachedFromWindow() {
        removeCallbacks(this.mUpdateDrawableStateRunnable);
        post(this.mReleaseCursorRunnable);
        super.onDetachedFromWindow();
    }

    void onQueryRefine(CharSequence queryText) {
        setQuery(queryText);
    }

    boolean onSuggestionsKey(View v, int keyCode, KeyEvent event) {
        if (this.mSearchable != null && this.mSuggestionsAdapter != null && event.getAction() == 0 && event.hasNoModifiers()) {
            if (!(keyCode == 66 || keyCode == 84)) {
                if (keyCode != 61) {
                    int selPoint;
                    if (keyCode != 21) {
                        if (keyCode != 22) {
                            if (keyCode != 19 || this.mSearchSrcTextView.getListSelection() == 0) {
                                return false;
                            }
                        }
                    }
                    if (keyCode == 21) {
                        selPoint = 0;
                    } else {
                        selPoint = this.mSearchSrcTextView.length();
                    }
                    this.mSearchSrcTextView.setSelection(selPoint);
                    this.mSearchSrcTextView.setListSelection(0);
                    this.mSearchSrcTextView.clearListSelection();
                    HIDDEN_METHOD_INVOKER.ensureImeVisible(this.mSearchSrcTextView, true);
                    return true;
                }
            }
            return onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, null);
        }
        return false;
    }

    private CharSequence getDecoratedHint(CharSequence hintText) {
        if (this.mIconifiedByDefault) {
            if (this.mSearchHintIcon != null) {
                double textSize = (double) this.mSearchSrcTextView.getTextSize();
                Double.isNaN(textSize);
                int textSize2 = (int) (textSize * 1.25d);
                this.mSearchHintIcon.setBounds(0, 0, textSize2, textSize2);
                SpannableStringBuilder ssb = new SpannableStringBuilder("   ");
                ssb.setSpan(new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
                ssb.append(hintText);
                return ssb;
            }
        }
        return hintText;
    }

    private void updateQueryHint() {
        CharSequence hint = getQueryHint();
        this.mSearchSrcTextView.setHint(getDecoratedHint(hint == null ? "" : hint));
    }

    private void updateSearchAutoComplete() {
        this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
        this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
        int inputType = this.mSearchable.getInputType();
        int i = 1;
        if ((inputType & 15) == 1) {
            inputType &= -65537;
            if (this.mSearchable.getSuggestAuthority() != null) {
                inputType = (inputType | 65536) | 524288;
            }
        }
        this.mSearchSrcTextView.setInputType(inputType);
        CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
        if (cursorAdapter != null) {
            cursorAdapter.changeCursor(null);
        }
        if (this.mSearchable.getSuggestAuthority() != null) {
            this.mSuggestionsAdapter = new SuggestionsAdapter(getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
            this.mSearchSrcTextView.setAdapter(this.mSuggestionsAdapter);
            SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter) this.mSuggestionsAdapter;
            if (this.mQueryRefinement) {
                i = 2;
            }
            suggestionsAdapter.setQueryRefinement(i);
        }
    }

    private void updateVoiceButton(boolean empty) {
        int visibility = 8;
        if (this.mVoiceButtonEnabled && !isIconified() && empty) {
            visibility = 0;
            this.mGoButton.setVisibility(8);
        }
        this.mVoiceButton.setVisibility(visibility);
    }

    void onTextChanged(CharSequence newText) {
        CharSequence text = this.mSearchSrcTextView.getText();
        this.mUserQuery = text;
        boolean z = true;
        boolean hasText = TextUtils.isEmpty(text) ^ true;
        updateSubmitButton(hasText);
        if (hasText) {
            z = false;
        }
        updateVoiceButton(z);
        updateCloseButton();
        updateSubmitArea();
        if (!(this.mOnQueryChangeListener == null || TextUtils.equals(newText, this.mOldQueryText))) {
            this.mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        this.mOldQueryText = newText.toString();
    }

    void onSubmitQuery() {
        CharSequence query = this.mSearchSrcTextView.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            OnQueryTextListener onQueryTextListener = this.mOnQueryChangeListener;
            if (onQueryTextListener == null || !onQueryTextListener.onQueryTextSubmit(query.toString())) {
                if (this.mSearchable != null) {
                    launchQuerySearch(0, null, query.toString());
                }
                this.mSearchSrcTextView.setImeVisibility(false);
                dismissSuggestions();
            }
        }
    }

    private void dismissSuggestions() {
        this.mSearchSrcTextView.dismissDropDown();
    }

    void onCloseClicked() {
        if (!TextUtils.isEmpty(this.mSearchSrcTextView.getText())) {
            this.mSearchSrcTextView.setText("");
            this.mSearchSrcTextView.requestFocus();
            this.mSearchSrcTextView.setImeVisibility(true);
        } else if (this.mIconifiedByDefault) {
            OnCloseListener onCloseListener = this.mOnCloseListener;
            if (onCloseListener == null || !onCloseListener.onClose()) {
                clearFocus();
                updateViewsVisibility(true);
            }
        }
    }

    void onSearchClicked() {
        updateViewsVisibility(false);
        this.mSearchSrcTextView.requestFocus();
        this.mSearchSrcTextView.setImeVisibility(true);
        OnClickListener onClickListener = this.mOnSearchClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    void onVoiceClicked() {
        if (this.mSearchable != null) {
            SearchableInfo searchable = this.mSearchable;
            try {
                if (searchable.getVoiceSearchLaunchWebSearch()) {
                    getContext().startActivity(createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, searchable));
                } else if (searchable.getVoiceSearchLaunchRecognizer()) {
                    getContext().startActivity(createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, searchable));
                }
            } catch (ActivityNotFoundException e) {
                Log.w(LOG_TAG, "Could not find voice search activity");
            }
        }
    }

    void onTextFocusChanged() {
        updateViewsVisibility(isIconified());
        postUpdateFocusedState();
        if (this.mSearchSrcTextView.hasFocus()) {
            forceSuggestionQuery();
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        postUpdateFocusedState();
    }

    public void onActionViewCollapsed() {
        setQuery("", false);
        clearFocus();
        updateViewsVisibility(true);
        this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
        this.mExpandedInActionView = false;
    }

    public void onActionViewExpanded() {
        if (!this.mExpandedInActionView) {
            this.mExpandedInActionView = true;
            this.mCollapsedImeOptions = this.mSearchSrcTextView.getImeOptions();
            this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions | 33554432);
            this.mSearchSrcTextView.setText("");
            setIconified(false);
        }
    }

    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.isIconified = isIconified();
        return ss;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            updateViewsVisibility(ss.isIconified);
            requestLayout();
            return;
        }
        super.onRestoreInstanceState(state);
    }

    void adjustDropDownSizeAndPosition() {
        if (this.mDropDownAnchor.getWidth() > 1) {
            int offset;
            Resources res = getContext().getResources();
            int anchorPadding = this.mSearchPlate.getPaddingLeft();
            Rect dropDownPadding = new Rect();
            boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
            int iconOffset = this.mIconifiedByDefault ? res.getDimensionPixelSize(C0185R.dimen.abc_dropdownitem_icon_width) + res.getDimensionPixelSize(C0185R.dimen.abc_dropdownitem_text_padding_left) : 0;
            this.mSearchSrcTextView.getDropDownBackground().getPadding(dropDownPadding);
            if (isLayoutRtl) {
                offset = -dropDownPadding.left;
            } else {
                offset = anchorPadding - (dropDownPadding.left + iconOffset);
            }
            this.mSearchSrcTextView.setDropDownHorizontalOffset(offset);
            this.mSearchSrcTextView.setDropDownWidth((((this.mDropDownAnchor.getWidth() + dropDownPadding.left) + dropDownPadding.right) + iconOffset) - anchorPadding);
        }
    }

    boolean onItemClicked(int position, int actionKey, String actionMsg) {
        OnSuggestionListener onSuggestionListener = this.mOnSuggestionListener;
        if (onSuggestionListener != null) {
            if (onSuggestionListener.onSuggestionClick(position)) {
                return false;
            }
        }
        launchSuggestion(position, 0, null);
        this.mSearchSrcTextView.setImeVisibility(false);
        dismissSuggestions();
        return true;
    }

    boolean onItemSelected(int position) {
        OnSuggestionListener onSuggestionListener = this.mOnSuggestionListener;
        if (onSuggestionListener != null) {
            if (onSuggestionListener.onSuggestionSelect(position)) {
                return false;
            }
        }
        rewriteQueryFromSuggestion(position);
        return true;
    }

    private void rewriteQueryFromSuggestion(int position) {
        CharSequence oldQuery = this.mSearchSrcTextView.getText();
        Cursor c = this.mSuggestionsAdapter.getCursor();
        if (c != null) {
            if (c.moveToPosition(position)) {
                CharSequence newQuery = this.mSuggestionsAdapter.convertToString(c);
                if (newQuery != null) {
                    setQuery(newQuery);
                } else {
                    setQuery(oldQuery);
                }
            } else {
                setQuery(oldQuery);
            }
        }
    }

    private boolean launchSuggestion(int position, int actionKey, String actionMsg) {
        Cursor c = this.mSuggestionsAdapter.getCursor();
        if (c == null || !c.moveToPosition(position)) {
            return false;
        }
        launchIntent(createIntentFromSuggestion(c, actionKey, actionMsg));
        return true;
    }

    private void launchIntent(Intent intent) {
        if (intent != null) {
            try {
                getContext().startActivity(intent);
            } catch (RuntimeException ex) {
                String str = LOG_TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed launch activity: ");
                stringBuilder.append(intent);
                Log.e(str, stringBuilder.toString(), ex);
            }
        }
    }

    private void setQuery(CharSequence query) {
        this.mSearchSrcTextView.setText(query);
        this.mSearchSrcTextView.setSelection(TextUtils.isEmpty(query) ? 0 : query.length());
    }

    void launchQuerySearch(int actionKey, String actionMsg, String query) {
        getContext().startActivity(createIntent("android.intent.action.SEARCH", null, null, query, actionKey, actionMsg));
    }

    private Intent createIntent(String action, Uri data, String extraData, String query, int actionKey, String actionMsg) {
        Intent intent = new Intent(action);
        intent.addFlags(268435456);
        if (data != null) {
            intent.setData(data);
        }
        intent.putExtra("user_query", this.mUserQuery);
        if (query != null) {
            intent.putExtra(SearchIntents.EXTRA_QUERY, query);
        }
        if (extraData != null) {
            intent.putExtra("intent_extra_data_key", extraData);
        }
        Bundle bundle = this.mAppSearchData;
        if (bundle != null) {
            intent.putExtra("app_data", bundle);
        }
        if (actionKey != 0) {
            intent.putExtra("action_key", actionKey);
            intent.putExtra("action_msg", actionMsg);
        }
        intent.setComponent(this.mSearchable.getSearchActivity());
        return intent;
    }

    private Intent createVoiceWebSearchIntent(Intent baseIntent, SearchableInfo searchable) {
        String str;
        Intent voiceIntent = new Intent(baseIntent);
        ComponentName searchActivity = searchable.getSearchActivity();
        String str2 = "calling_package";
        if (searchActivity == null) {
            str = null;
        } else {
            str = searchActivity.flattenToShortString();
        }
        voiceIntent.putExtra(str2, str);
        return voiceIntent;
    }

    private Intent createVoiceAppSearchIntent(Intent baseIntent, SearchableInfo searchable) {
        String str;
        ComponentName searchActivity = searchable.getSearchActivity();
        Intent queryIntent = new Intent("android.intent.action.SEARCH");
        queryIntent.setComponent(searchActivity);
        PendingIntent pending = PendingIntent.getActivity(getContext(), 0, queryIntent, 1073741824);
        Bundle queryExtras = new Bundle();
        Parcelable parcelable = this.mAppSearchData;
        if (parcelable != null) {
            queryExtras.putParcelable("app_data", parcelable);
        }
        Intent voiceIntent = new Intent(baseIntent);
        String languageModel = "free_form";
        String prompt = null;
        String language = null;
        int maxResults = 1;
        Resources resources = getResources();
        if (searchable.getVoiceLanguageModeId() != 0) {
            languageModel = resources.getString(searchable.getVoiceLanguageModeId());
        }
        if (searchable.getVoicePromptTextId() != 0) {
            prompt = resources.getString(searchable.getVoicePromptTextId());
        }
        if (searchable.getVoiceLanguageId() != 0) {
            language = resources.getString(searchable.getVoiceLanguageId());
        }
        if (searchable.getVoiceMaxResults() != 0) {
            maxResults = searchable.getVoiceMaxResults();
        }
        voiceIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", languageModel);
        voiceIntent.putExtra("android.speech.extra.PROMPT", prompt);
        voiceIntent.putExtra("android.speech.extra.LANGUAGE", language);
        voiceIntent.putExtra("android.speech.extra.MAX_RESULTS", maxResults);
        String str2 = "calling_package";
        if (searchActivity == null) {
            str = null;
        } else {
            str = searchActivity.flattenToShortString();
        }
        voiceIntent.putExtra(str2, str);
        voiceIntent.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", pending);
        voiceIntent.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", queryExtras);
        return voiceIntent;
    }

    private Intent createIntentFromSuggestion(Cursor c, int actionKey, String actionMsg) {
        String id;
        StringBuilder stringBuilder;
        try {
            String data;
            String action = SuggestionsAdapter.getColumnString(c, "suggest_intent_action");
            if (action == null) {
                action = this.mSearchable.getSuggestIntentAction();
            }
            if (action == null) {
                action = "android.intent.action.SEARCH";
            }
            String data2 = SuggestionsAdapter.getColumnString(c, "suggest_intent_data");
            if (data2 == null) {
                data2 = this.mSearchable.getSuggestIntentData();
            }
            if (data2 != null) {
                id = SuggestionsAdapter.getColumnString(c, "suggest_intent_data_id");
                if (id != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(data2);
                    stringBuilder.append("/");
                    stringBuilder.append(Uri.encode(id));
                    data = stringBuilder.toString();
                    return createIntent(action, data != null ? null : Uri.parse(data), SuggestionsAdapter.getColumnString(c, "suggest_intent_extra_data"), SuggestionsAdapter.getColumnString(c, "suggest_intent_query"), actionKey, actionMsg);
                }
            }
            data = data2;
            if (data != null) {
            }
            return createIntent(action, data != null ? null : Uri.parse(data), SuggestionsAdapter.getColumnString(c, "suggest_intent_extra_data"), SuggestionsAdapter.getColumnString(c, "suggest_intent_query"), actionKey, actionMsg);
        } catch (RuntimeException e) {
            int rowNum;
            try {
                rowNum = c.getPosition();
            } catch (RuntimeException e2) {
                rowNum = -1;
            }
            id = LOG_TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Search suggestions cursor at row ");
            stringBuilder.append(rowNum);
            stringBuilder.append(" returned exception.");
            Log.w(id, stringBuilder.toString(), e);
            return null;
        }
    }

    void forceSuggestionQuery() {
        HIDDEN_METHOD_INVOKER.doBeforeTextChanged(this.mSearchSrcTextView);
        HIDDEN_METHOD_INVOKER.doAfterTextChanged(this.mSearchSrcTextView);
    }

    static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }
}
