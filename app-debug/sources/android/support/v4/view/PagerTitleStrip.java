package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager.DecorView;
import android.support.v4.view.ViewPager.OnAdapterChangeListener;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils.TruncateAt;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Locale;

@DecorView
public class PagerTitleStrip extends ViewGroup {
    private static final int[] ATTRS = new int[]{16842804, 16842901, 16842904, 16842927};
    private static final float SIDE_ALPHA = 0.6f;
    private static final int[] TEXT_ATTRS = new int[]{16843660};
    private static final int TEXT_SPACING = 16;
    TextView mCurrText;
    private int mGravity;
    private int mLastKnownCurrentPage;
    float mLastKnownPositionOffset;
    TextView mNextText;
    private int mNonPrimaryAlpha;
    private final PageListener mPageListener;
    ViewPager mPager;
    TextView mPrevText;
    private int mScaledTextSpacing;
    int mTextColor;
    private boolean mUpdatingPositions;
    private boolean mUpdatingText;
    private WeakReference<PagerAdapter> mWatchingAdapter;

    private static class SingleLineAllCapsTransform extends SingleLineTransformationMethod {
        private Locale mLocale;

        SingleLineAllCapsTransform(Context context) {
            this.mLocale = context.getResources().getConfiguration().locale;
        }

        public CharSequence getTransformation(CharSequence source, View view) {
            source = super.getTransformation(source, view);
            return source != null ? source.toString().toUpperCase(this.mLocale) : null;
        }
    }

    private class PageListener extends DataSetObserver implements OnPageChangeListener, OnAdapterChangeListener {
        private int mScrollState;

        PageListener() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset > 0.5f) {
                position++;
            }
            PagerTitleStrip.this.updateTextPositions(position, positionOffset, false);
        }

        public void onPageSelected(int position) {
            if (this.mScrollState == 0) {
                PagerTitleStrip pagerTitleStrip = PagerTitleStrip.this;
                pagerTitleStrip.updateText(pagerTitleStrip.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
                float f = 0.0f;
                if (PagerTitleStrip.this.mLastKnownPositionOffset >= 0.0f) {
                    f = PagerTitleStrip.this.mLastKnownPositionOffset;
                }
                float offset = f;
                PagerTitleStrip pagerTitleStrip2 = PagerTitleStrip.this;
                pagerTitleStrip2.updateTextPositions(pagerTitleStrip2.mPager.getCurrentItem(), offset, true);
            }
        }

        public void onPageScrollStateChanged(int state) {
            this.mScrollState = state;
        }

        public void onAdapterChanged(ViewPager viewPager, PagerAdapter oldAdapter, PagerAdapter newAdapter) {
            PagerTitleStrip.this.updateAdapter(oldAdapter, newAdapter);
        }

        public void onChanged() {
            PagerTitleStrip pagerTitleStrip = PagerTitleStrip.this;
            pagerTitleStrip.updateText(pagerTitleStrip.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
            float f = 0.0f;
            if (PagerTitleStrip.this.mLastKnownPositionOffset >= 0.0f) {
                f = PagerTitleStrip.this.mLastKnownPositionOffset;
            }
            float offset = f;
            PagerTitleStrip pagerTitleStrip2 = PagerTitleStrip.this;
            pagerTitleStrip2.updateTextPositions(pagerTitleStrip2.mPager.getCurrentItem(), offset, true);
        }
    }

    private static void setSingleLineAllCaps(TextView text) {
        text.setTransformationMethod(new SingleLineAllCapsTransform(text.getContext()));
    }

    public PagerTitleStrip(@NonNull Context context) {
        this(context, null);
    }

    public PagerTitleStrip(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mLastKnownCurrentPage = -1;
        this.mLastKnownPositionOffset = -1.0f;
        this.mPageListener = new PageListener();
        View textView = new TextView(context);
        this.mPrevText = textView;
        addView(textView);
        textView = new TextView(context);
        this.mCurrText = textView;
        addView(textView);
        textView = new TextView(context);
        this.mNextText = textView;
        addView(textView);
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        int textAppearance = a.getResourceId(0, 0);
        if (textAppearance != 0) {
            TextViewCompat.setTextAppearance(this.mPrevText, textAppearance);
            TextViewCompat.setTextAppearance(this.mCurrText, textAppearance);
            TextViewCompat.setTextAppearance(this.mNextText, textAppearance);
        }
        int textSize = a.getDimensionPixelSize(1, 0);
        if (textSize != 0) {
            setTextSize(0, (float) textSize);
        }
        if (a.hasValue(2)) {
            int textColor = a.getColor(2, 0);
            this.mPrevText.setTextColor(textColor);
            this.mCurrText.setTextColor(textColor);
            this.mNextText.setTextColor(textColor);
        }
        this.mGravity = a.getInteger(3, 80);
        a.recycle();
        this.mTextColor = this.mCurrText.getTextColors().getDefaultColor();
        setNonPrimaryAlpha(SIDE_ALPHA);
        this.mPrevText.setEllipsize(TruncateAt.END);
        this.mCurrText.setEllipsize(TruncateAt.END);
        this.mNextText.setEllipsize(TruncateAt.END);
        boolean allCaps = false;
        if (textAppearance != 0) {
            TypedArray ta = context.obtainStyledAttributes(textAppearance, TEXT_ATTRS);
            allCaps = ta.getBoolean(0, false);
            ta.recycle();
        }
        if (allCaps) {
            setSingleLineAllCaps(this.mPrevText);
            setSingleLineAllCaps(this.mCurrText);
            setSingleLineAllCaps(this.mNextText);
        } else {
            this.mPrevText.setSingleLine();
            this.mCurrText.setSingleLine();
            this.mNextText.setSingleLine();
        }
        this.mScaledTextSpacing = (int) (16.0f * context.getResources().getDisplayMetrics().density);
    }

    public void setTextSpacing(int spacingPixels) {
        this.mScaledTextSpacing = spacingPixels;
        requestLayout();
    }

    public int getTextSpacing() {
        return this.mScaledTextSpacing;
    }

    public void setNonPrimaryAlpha(@FloatRange(from = 0.0d, to = 1.0d) float alpha) {
        this.mNonPrimaryAlpha = ((int) (255.0f * alpha)) & 255;
        int transparentColor = (this.mNonPrimaryAlpha << 24) | (this.mTextColor & ViewCompat.MEASURED_SIZE_MASK);
        this.mPrevText.setTextColor(transparentColor);
        this.mNextText.setTextColor(transparentColor);
    }

    public void setTextColor(@ColorInt int color) {
        this.mTextColor = color;
        this.mCurrText.setTextColor(color);
        int transparentColor = (this.mNonPrimaryAlpha << 24) | (this.mTextColor & ViewCompat.MEASURED_SIZE_MASK);
        this.mPrevText.setTextColor(transparentColor);
        this.mNextText.setTextColor(transparentColor);
    }

    public void setTextSize(int unit, float size) {
        this.mPrevText.setTextSize(unit, size);
        this.mCurrText.setTextSize(unit, size);
        this.mNextText.setTextSize(unit, size);
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
        requestLayout();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof ViewPager) {
            ViewPager pager = (ViewPager) parent;
            PagerAdapter adapter = pager.getAdapter();
            pager.setInternalPageChangeListener(this.mPageListener);
            pager.addOnAdapterChangeListener(this.mPageListener);
            this.mPager = pager;
            WeakReference weakReference = this.mWatchingAdapter;
            updateAdapter(weakReference != null ? (PagerAdapter) weakReference.get() : null, adapter);
            return;
        }
        throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewPager viewPager = this.mPager;
        if (viewPager != null) {
            updateAdapter(viewPager.getAdapter(), null);
            this.mPager.setInternalPageChangeListener(null);
            this.mPager.removeOnAdapterChangeListener(this.mPageListener);
            this.mPager = null;
        }
    }

    void updateText(int currentItem, PagerAdapter adapter) {
        int itemCount = adapter != null ? adapter.getCount() : 0;
        this.mUpdatingText = true;
        CharSequence text = null;
        if (currentItem >= 1 && adapter != null) {
            text = adapter.getPageTitle(currentItem - 1);
        }
        this.mPrevText.setText(text);
        TextView textView = this.mCurrText;
        CharSequence pageTitle = (adapter == null || currentItem >= itemCount) ? null : adapter.getPageTitle(currentItem);
        textView.setText(pageTitle);
        CharSequence text2 = null;
        if (currentItem + 1 < itemCount && adapter != null) {
            text2 = adapter.getPageTitle(currentItem + 1);
        }
        this.mNextText.setText(text2);
        int childWidthSpec = MeasureSpec.makeMeasureSpec(Math.max(0, (int) (((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) * 0.8f)), Integer.MIN_VALUE);
        int childHeightSpec = MeasureSpec.makeMeasureSpec(Math.max(0, (getHeight() - getPaddingTop()) - getPaddingBottom()), Integer.MIN_VALUE);
        this.mPrevText.measure(childWidthSpec, childHeightSpec);
        this.mCurrText.measure(childWidthSpec, childHeightSpec);
        this.mNextText.measure(childWidthSpec, childHeightSpec);
        this.mLastKnownCurrentPage = currentItem;
        if (!this.mUpdatingPositions) {
            updateTextPositions(currentItem, this.mLastKnownPositionOffset, false);
        }
        this.mUpdatingText = false;
    }

    public void requestLayout() {
        if (!this.mUpdatingText) {
            super.requestLayout();
        }
    }

    void updateAdapter(PagerAdapter oldAdapter, PagerAdapter newAdapter) {
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(this.mPageListener);
            this.mWatchingAdapter = null;
        }
        if (newAdapter != null) {
            newAdapter.registerDataSetObserver(this.mPageListener);
            this.mWatchingAdapter = new WeakReference(newAdapter);
        }
        ViewPager viewPager = this.mPager;
        if (viewPager != null) {
            this.mLastKnownCurrentPage = -1;
            this.mLastKnownPositionOffset = -1.0f;
            updateText(viewPager.getCurrentItem(), newAdapter);
            requestLayout();
        }
    }

    void updateTextPositions(int position, float positionOffset, boolean force) {
        int i = position;
        float f = positionOffset;
        if (i != this.mLastKnownCurrentPage) {
            updateText(i, r0.mPager.getAdapter());
        } else if (!force && f == r0.mLastKnownPositionOffset) {
            return;
        }
        r0.mUpdatingPositions = true;
        int prevWidth = r0.mPrevText.getMeasuredWidth();
        int currWidth = r0.mCurrText.getMeasuredWidth();
        int nextWidth = r0.mNextText.getMeasuredWidth();
        int halfCurrWidth = currWidth / 2;
        int stripWidth = getWidth();
        int stripHeight = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int textPaddedLeft = paddingLeft + halfCurrWidth;
        int textPaddedRight = paddingRight + halfCurrWidth;
        int contentWidth = (stripWidth - textPaddedLeft) - textPaddedRight;
        float currOffset = f + 0.5f;
        if (currOffset > 1.0f) {
            currOffset -= 1.0f;
        }
        i = ((stripWidth - textPaddedRight) - ((int) (((float) contentWidth) * currOffset))) - (currWidth / 2);
        halfCurrWidth = i + currWidth;
        currWidth = r0.mPrevText.getBaseline();
        textPaddedLeft = r0.mCurrText.getBaseline();
        textPaddedRight = r0.mNextText.getBaseline();
        contentWidth = Math.max(Math.max(currWidth, textPaddedLeft), textPaddedRight);
        int prevTopOffset = contentWidth - currWidth;
        int currTopOffset = contentWidth - textPaddedLeft;
        int nextTopOffset = contentWidth - textPaddedRight;
        currWidth = prevTopOffset + r0.mPrevText.getMeasuredHeight();
        textPaddedLeft = currTopOffset + r0.mCurrText.getMeasuredHeight();
        textPaddedRight = nextTopOffset + r0.mNextText.getMeasuredHeight();
        contentWidth = Math.max(Math.max(currWidth, textPaddedLeft), textPaddedRight);
        currWidth = r0.mGravity & 112;
        int currTop;
        int nextTop;
        int centeredTop;
        if (currWidth == 16) {
            textPaddedLeft = 0;
            currTop = 0;
            nextTop = 0;
            centeredTop = (((stripHeight - paddingTop) - paddingBottom) - contentWidth) / 2;
            textPaddedRight = centeredTop + prevTopOffset;
            textPaddedLeft = centeredTop + currTopOffset;
            currWidth = centeredTop + nextTopOffset;
        } else if (currWidth != 80) {
            textPaddedRight = paddingTop + prevTopOffset;
            textPaddedLeft = paddingTop + currTopOffset;
            nextTop = currWidth;
            currWidth = paddingTop + nextTopOffset;
        } else {
            textPaddedLeft = 0;
            currTop = 0;
            nextTop = 0;
            int currTop2 = 0;
            centeredTop = 0;
            int bottomGravTop = (stripHeight - paddingBottom) - contentWidth;
            textPaddedRight = bottomGravTop + prevTopOffset;
            textPaddedLeft = bottomGravTop + currTopOffset;
            nextTop = currWidth;
            currWidth = bottomGravTop + nextTopOffset;
        }
        TextView textView = r0.mCurrText;
        textView.layout(i, textPaddedLeft, halfCurrWidth, textPaddedLeft + textView.getMeasuredHeight());
        stripHeight = Math.min(paddingLeft, (i - r0.mScaledTextSpacing) - prevWidth);
        TextView textView2 = r0.mPrevText;
        textView2.layout(stripHeight, textPaddedRight, stripHeight + prevWidth, textPaddedRight + textView2.getMeasuredHeight());
        i = Math.max((stripWidth - paddingRight) - nextWidth, r0.mScaledTextSpacing + halfCurrWidth);
        TextView textView3 = r0.mNextText;
        textView3.layout(i, currWidth, i + nextWidth, currWidth + textView3.getMeasuredHeight());
        r0.mLastKnownPositionOffset = f;
        r0.mUpdatingPositions = false;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == 1073741824) {
            int height;
            int heightPadding = getPaddingTop() + getPaddingBottom();
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, heightPadding, -2);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, (int) (((float) widthSize) * 1045220557), -2);
            this.mPrevText.measure(childWidthSpec, childHeightSpec);
            this.mCurrText.measure(childWidthSpec, childHeightSpec);
            this.mNextText.measure(childWidthSpec, childHeightSpec);
            if (MeasureSpec.getMode(heightMeasureSpec) == 1073741824) {
                height = MeasureSpec.getSize(heightMeasureSpec);
            } else {
                height = Math.max(getMinHeight(), this.mCurrText.getMeasuredHeight() + heightPadding);
            }
            setMeasuredDimension(widthSize, View.resolveSizeAndState(height, heightMeasureSpec, this.mCurrText.getMeasuredState() << 16));
            return;
        }
        throw new IllegalStateException("Must measure with an exact width");
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.mPager != null) {
            float offset = this.mLastKnownPositionOffset;
            if (offset < 0.0f) {
                offset = 0.0f;
            }
            updateTextPositions(this.mLastKnownCurrentPage, offset, true);
        }
    }

    int getMinHeight() {
        Drawable bg = getBackground();
        if (bg != null) {
            return bg.getIntrinsicHeight();
        }
        return 0;
    }
}
