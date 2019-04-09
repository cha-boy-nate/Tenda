package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR = new C01451();
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    static final int[] LAYOUT_ATTRS = new int[]{16842931};
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new C01462();
    private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
    private int mActivePointerId = -1;
    PagerAdapter mAdapter;
    private List<OnAdapterChangeListener> mAdapterChangeListeners;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable = new C01473();
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = Float.MAX_VALUE;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private int mPageTransformerLayerType;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffect mRightEdge;
    private int mScrollState = 0;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    /* renamed from: android.support.v4.view.ViewPager$1 */
    static class C01451 implements Comparator<ItemInfo> {
        C01451() {
        }

        public int compare(ItemInfo lhs, ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$2 */
    static class C01462 implements Interpolator {
        C01462() {
        }

        public float getInterpolation(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$3 */
    class C01473 implements Runnable {
        C01473() {
        }

        public void run() {
            ViewPager.this.setScrollState(0);
            ViewPager.this.populate();
        }
    }

    @Inherited
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DecorView {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, ViewPager.LAYOUT_ATTRS);
            this.gravity = a.getInteger(0, 48);
            a.recycle();
        }
    }

    public interface OnAdapterChangeListener {
        void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter pagerAdapter, @Nullable PagerAdapter pagerAdapter2);
    }

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, @Px int i2);

        void onPageSelected(int i);
    }

    public interface PageTransformer {
        void transformPage(@NonNull View view, float f);
    }

    private class PagerObserver extends DataSetObserver {
        PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View lhs, View rhs) {
            LayoutParams llp = (LayoutParams) lhs.getLayoutParams();
            LayoutParams rlp = (LayoutParams) rhs.getLayoutParams();
            if (llp.isDecor == rlp.isDecor) {
                return llp.position - rlp.position;
            }
            return llp.isDecor ? 1 : -1;
        }
    }

    /* renamed from: android.support.v4.view.ViewPager$4 */
    class C02684 implements OnApplyWindowInsetsListener {
        private final Rect mTempRect = new Rect();

        C02684() {
        }

        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat originalInsets) {
            WindowInsetsCompat applied = ViewCompat.onApplyWindowInsets(v, originalInsets);
            if (applied.isConsumed()) {
                return applied;
            }
            Rect res = this.mTempRect;
            res.left = applied.getSystemWindowInsetLeft();
            res.top = applied.getSystemWindowInsetTop();
            res.right = applied.getSystemWindowInsetRight();
            res.bottom = applied.getSystemWindowInsetBottom();
            int count = ViewPager.this.getChildCount();
            for (int i = 0; i < count; i++) {
                WindowInsetsCompat childInsets = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(i), applied);
                res.left = Math.min(childInsets.getSystemWindowInsetLeft(), res.left);
                res.top = Math.min(childInsets.getSystemWindowInsetTop(), res.top);
                res.right = Math.min(childInsets.getSystemWindowInsetRight(), res.right);
                res.bottom = Math.min(childInsets.getSystemWindowInsetBottom(), res.bottom);
            }
            return applied.replaceSystemWindowInsets(res.left, res.top, res.right, res.bottom);
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPager.class.getName());
            event.setScrollable(canScroll());
            if (event.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
                event.setItemCount(ViewPager.this.mAdapter.getCount());
                event.setFromIndex(ViewPager.this.mCurItem);
                event.setToIndex(ViewPager.this.mCurItem);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPager.class.getName());
            info.setScrollable(canScroll());
            if (ViewPager.this.canScrollHorizontally(1)) {
                info.addAction(4096);
            }
            if (ViewPager.this.canScrollHorizontally(-1)) {
                info.addAction(8192);
            }
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            ViewPager viewPager;
            if (action != 4096) {
                if (action != 8192 || !ViewPager.this.canScrollHorizontally(-1)) {
                    return false;
                }
                viewPager = ViewPager.this;
                viewPager.setCurrentItem(viewPager.mCurItem - 1);
                return true;
            } else if (!ViewPager.this.canScrollHorizontally(1)) {
                return false;
            } else {
                viewPager = ViewPager.this;
                viewPager.setCurrentItem(viewPager.mCurItem + 1);
                return true;
            }
        }

        private boolean canScroll() {
            return ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1;
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new C01481();
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        /* renamed from: android.support.v4.view.ViewPager$SavedState$1 */
        static class C01481 implements ClassLoaderCreator<SavedState> {
            C01481() {
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

        public SavedState(@NonNull Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FragmentPager.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" position=");
            stringBuilder.append(this.position);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    void populate(int r20) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:154:0x02b4 in {2, 3, 7, 11, 14, 24, 25, 28, 32, 33, 36, 37, 44, 51, 52, 53, 59, 60, 61, 64, 65, 66, 67, 72, 73, 75, 76, 83, 90, 91, 92, 93, 100, 101, 102, 105, 106, 107, 108, 109, 110, 111, 112, 121, 122, 123, 128, 129, 133, 143, 144, 145, 149, 151, 153} preds:[]
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
        r19 = this;
        r1 = r19;
        r2 = r20;
        r0 = 0;
        r3 = r1.mCurItem;
        if (r3 == r2) goto L_0x0011;
    L_0x0009:
        r0 = r1.infoForPosition(r3);
        r1.mCurItem = r2;
        r3 = r0;
        goto L_0x0012;
    L_0x0011:
        r3 = r0;
    L_0x0012:
        r0 = r1.mAdapter;
        if (r0 != 0) goto L_0x001a;
    L_0x0016:
        r19.sortChildDrawingOrder();
        return;
    L_0x001a:
        r0 = r1.mPopulatePending;
        if (r0 == 0) goto L_0x0022;
    L_0x001e:
        r19.sortChildDrawingOrder();
        return;
    L_0x0022:
        r0 = r19.getWindowToken();
        if (r0 != 0) goto L_0x0029;
    L_0x0028:
        return;
    L_0x0029:
        r0 = r1.mAdapter;
        r0.startUpdate(r1);
        r4 = r1.mOffscreenPageLimit;
        r0 = 0;
        r5 = r1.mCurItem;
        r5 = r5 - r4;
        r5 = java.lang.Math.max(r0, r5);
        r0 = r1.mAdapter;
        r6 = r0.getCount();
        r0 = r6 + -1;
        r7 = r1.mCurItem;
        r7 = r7 + r4;
        r7 = java.lang.Math.min(r0, r7);
        r0 = r1.mExpectedAdapterCount;
        if (r6 != r0) goto L_0x0257;
    L_0x004b:
        r0 = -1;
        r8 = 0;
        r0 = 0;
    L_0x004e:
        r9 = r1.mItems;
        r9 = r9.size();
        if (r0 >= r9) goto L_0x006f;
    L_0x0056:
        r9 = r1.mItems;
        r9 = r9.get(r0);
        r9 = (android.support.v4.view.ViewPager.ItemInfo) r9;
        r10 = r9.position;
        r11 = r1.mCurItem;
        if (r10 < r11) goto L_0x006c;
    L_0x0064:
        r10 = r9.position;
        r11 = r1.mCurItem;
        if (r10 != r11) goto L_0x006f;
    L_0x006a:
        r8 = r9;
        goto L_0x006f;
    L_0x006c:
        r0 = r0 + 1;
        goto L_0x004e;
    L_0x006f:
        if (r8 != 0) goto L_0x0079;
    L_0x0071:
        if (r6 <= 0) goto L_0x0079;
    L_0x0073:
        r9 = r1.mCurItem;
        r8 = r1.addNewItem(r9, r0);
    L_0x0079:
        if (r8 == 0) goto L_0x01d8;
    L_0x007b:
        r11 = 0;
        r12 = r0 + -1;
        if (r12 < 0) goto L_0x0089;
    L_0x0080:
        r13 = r1.mItems;
        r13 = r13.get(r12);
        r13 = (android.support.v4.view.ViewPager.ItemInfo) r13;
        goto L_0x008a;
    L_0x0089:
        r13 = 0;
    L_0x008a:
        r14 = r19.getClientWidth();
        r15 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r14 > 0) goto L_0x0094;
    L_0x0092:
        r9 = 0;
        goto L_0x00a0;
    L_0x0094:
        r10 = r8.widthFactor;
        r10 = r15 - r10;
        r9 = r19.getPaddingLeft();
        r9 = (float) r9;
        r15 = (float) r14;
        r9 = r9 / r15;
        r9 = r9 + r10;
        r10 = r1.mCurItem;
        r10 = r10 + -1;
    L_0x00a5:
        if (r10 < 0) goto L_0x010c;
    L_0x00a7:
        r15 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1));
        if (r15 < 0) goto L_0x00d6;
    L_0x00ab:
        if (r10 >= r5) goto L_0x00d6;
    L_0x00ad:
        if (r13 != 0) goto L_0x00b0;
    L_0x00af:
        goto L_0x010c;
    L_0x00b0:
        r15 = r13.position;
        if (r10 != r15) goto L_0x0107;
    L_0x00b4:
        r15 = r13.scrolling;
        if (r15 != 0) goto L_0x0107;
    L_0x00b8:
        r15 = r1.mItems;
        r15.remove(r12);
        r15 = r1.mAdapter;
        r2 = r13.object;
        r15.destroyItem(r1, r10, r2);
        r12 = r12 + -1;
        r0 = r0 + -1;
        if (r12 < 0) goto L_0x00d3;
    L_0x00ca:
        r2 = r1.mItems;
        r2 = r2.get(r12);
        r2 = (android.support.v4.view.ViewPager.ItemInfo) r2;
        goto L_0x00d4;
    L_0x00d3:
        r2 = 0;
    L_0x00d4:
        r13 = r2;
        goto L_0x0107;
    L_0x00d6:
        if (r13 == 0) goto L_0x00ef;
    L_0x00d8:
        r2 = r13.position;
        if (r10 != r2) goto L_0x00ef;
    L_0x00dc:
        r2 = r13.widthFactor;
        r11 = r11 + r2;
        r12 = r12 + -1;
        if (r12 < 0) goto L_0x00ec;
    L_0x00e3:
        r2 = r1.mItems;
        r2 = r2.get(r12);
        r2 = (android.support.v4.view.ViewPager.ItemInfo) r2;
        goto L_0x00ed;
    L_0x00ec:
        r2 = 0;
    L_0x00ed:
        r13 = r2;
        goto L_0x0107;
    L_0x00ef:
        r2 = r12 + 1;
        r2 = r1.addNewItem(r10, r2);
        r13 = r2.widthFactor;
        r11 = r11 + r13;
        r0 = r0 + 1;
        if (r12 < 0) goto L_0x0105;
    L_0x00fc:
        r13 = r1.mItems;
        r13 = r13.get(r12);
        r13 = (android.support.v4.view.ViewPager.ItemInfo) r13;
        goto L_0x0106;
    L_0x0105:
        r13 = 0;
    L_0x0106:
        r2 = r13;
    L_0x0107:
        r10 = r10 + -1;
        r2 = r20;
        goto L_0x00a5;
    L_0x010c:
        r2 = r8.widthFactor;
        r10 = r0 + 1;
        r12 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r15 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1));
        if (r15 >= 0) goto L_0x01c6;
    L_0x0116:
        r12 = r1.mItems;
        r12 = r12.size();
        if (r10 >= r12) goto L_0x0127;
    L_0x011e:
        r12 = r1.mItems;
        r12 = r12.get(r10);
        r12 = (android.support.v4.view.ViewPager.ItemInfo) r12;
        goto L_0x0128;
    L_0x0127:
        r12 = 0;
    L_0x0128:
        if (r14 > 0) goto L_0x012c;
    L_0x012a:
        r13 = 0;
        goto L_0x0136;
    L_0x012c:
        r13 = r19.getPaddingRight();
        r13 = (float) r13;
        r15 = (float) r14;
        r13 = r13 / r15;
        r15 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r13 = r13 + r15;
        r15 = r1.mCurItem;
        r15 = r15 + 1;
    L_0x013b:
        if (r15 >= r6) goto L_0x01c1;
    L_0x013d:
        r17 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1));
        if (r17 < 0) goto L_0x017a;
    L_0x0141:
        if (r15 <= r7) goto L_0x017a;
    L_0x0143:
        if (r12 != 0) goto L_0x014b;
    L_0x0145:
        r17 = r4;
        r18 = r5;
        goto L_0x01cb;
    L_0x014b:
        r17 = r4;
        r4 = r12.position;
        if (r15 != r4) goto L_0x0177;
    L_0x0151:
        r4 = r12.scrolling;
        if (r4 != 0) goto L_0x0177;
    L_0x0155:
        r4 = r1.mItems;
        r4.remove(r10);
        r4 = r1.mAdapter;
        r18 = r5;
        r5 = r12.object;
        r4.destroyItem(r1, r15, r5);
        r4 = r1.mItems;
        r4 = r4.size();
        if (r10 >= r4) goto L_0x0174;
    L_0x016b:
        r4 = r1.mItems;
        r4 = r4.get(r10);
        r4 = (android.support.v4.view.ViewPager.ItemInfo) r4;
        goto L_0x0175;
    L_0x0174:
        r4 = 0;
    L_0x0175:
        r12 = r4;
        goto L_0x01b9;
    L_0x0177:
        r18 = r5;
        goto L_0x01b9;
    L_0x017a:
        r17 = r4;
        r18 = r5;
        if (r12 == 0) goto L_0x019d;
    L_0x0180:
        r4 = r12.position;
        if (r15 != r4) goto L_0x019d;
    L_0x0184:
        r4 = r12.widthFactor;
        r2 = r2 + r4;
        r10 = r10 + 1;
        r4 = r1.mItems;
        r4 = r4.size();
        if (r10 >= r4) goto L_0x019a;
    L_0x0191:
        r4 = r1.mItems;
        r4 = r4.get(r10);
        r4 = (android.support.v4.view.ViewPager.ItemInfo) r4;
        goto L_0x019b;
    L_0x019a:
        r4 = 0;
    L_0x019b:
        r12 = r4;
        goto L_0x01b9;
    L_0x019d:
        r4 = r1.addNewItem(r15, r10);
        r10 = r10 + 1;
        r5 = r4.widthFactor;
        r2 = r2 + r5;
        r5 = r1.mItems;
        r5 = r5.size();
        if (r10 >= r5) goto L_0x01b7;
    L_0x01ae:
        r5 = r1.mItems;
        r5 = r5.get(r10);
        r5 = (android.support.v4.view.ViewPager.ItemInfo) r5;
        goto L_0x01b8;
    L_0x01b7:
        r5 = 0;
    L_0x01b8:
        r12 = r5;
    L_0x01b9:
        r15 = r15 + 1;
        r4 = r17;
        r5 = r18;
        goto L_0x013b;
    L_0x01c1:
        r17 = r4;
        r18 = r5;
        goto L_0x01cb;
    L_0x01c6:
        r17 = r4;
        r18 = r5;
        r12 = r13;
    L_0x01cb:
        r1.calculatePageOffsets(r8, r0, r3);
        r4 = r1.mAdapter;
        r5 = r1.mCurItem;
        r13 = r8.object;
        r4.setPrimaryItem(r1, r5, r13);
        goto L_0x01dc;
    L_0x01d8:
        r17 = r4;
        r18 = r5;
    L_0x01dc:
        r2 = r1.mAdapter;
        r2.finishUpdate(r1);
        r2 = r19.getChildCount();
        r4 = 0;
    L_0x01e6:
        if (r4 >= r2) goto L_0x0212;
    L_0x01e8:
        r5 = r1.getChildAt(r4);
        r9 = r5.getLayoutParams();
        r9 = (android.support.v4.view.ViewPager.LayoutParams) r9;
        r9.childIndex = r4;
        r10 = r9.isDecor;
        if (r10 != 0) goto L_0x020e;
    L_0x01f8:
        r10 = r9.widthFactor;
        r11 = 0;
        r10 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1));
        if (r10 != 0) goto L_0x020f;
    L_0x01ff:
        r10 = r1.infoForChild(r5);
        if (r10 == 0) goto L_0x020f;
    L_0x0205:
        r12 = r10.widthFactor;
        r9.widthFactor = r12;
        r12 = r10.position;
        r9.position = r12;
        goto L_0x020f;
    L_0x020e:
        r11 = 0;
    L_0x020f:
        r4 = r4 + 1;
        goto L_0x01e6;
    L_0x0212:
        r19.sortChildDrawingOrder();
        r4 = r19.hasFocus();
        if (r4 == 0) goto L_0x0256;
    L_0x021b:
        r4 = r19.findFocus();
        if (r4 == 0) goto L_0x0228;
    L_0x0221:
        r10 = r1.infoForAnyChild(r4);
        r16 = r10;
        goto L_0x022a;
    L_0x0228:
        r16 = 0;
    L_0x022a:
        r5 = r16;
        if (r5 == 0) goto L_0x0234;
    L_0x022e:
        r9 = r5.position;
        r10 = r1.mCurItem;
        if (r9 == r10) goto L_0x0256;
    L_0x0234:
        r9 = 0;
    L_0x0235:
        r10 = r19.getChildCount();
        if (r9 >= r10) goto L_0x0256;
    L_0x023b:
        r10 = r1.getChildAt(r9);
        r5 = r1.infoForChild(r10);
        if (r5 == 0) goto L_0x0253;
    L_0x0245:
        r11 = r5.position;
        r12 = r1.mCurItem;
        if (r11 != r12) goto L_0x0253;
    L_0x024b:
        r11 = 2;
        r11 = r10.requestFocus(r11);
        if (r11 == 0) goto L_0x0253;
    L_0x0252:
        goto L_0x0256;
    L_0x0253:
        r9 = r9 + 1;
        goto L_0x0235;
    L_0x0256:
        return;
    L_0x0257:
        r17 = r4;
        r18 = r5;
        r0 = r19.getResources();	 Catch:{ NotFoundException -> 0x0268 }
        r2 = r19.getId();	 Catch:{ NotFoundException -> 0x0268 }
        r0 = r0.getResourceName(r2);	 Catch:{ NotFoundException -> 0x0268 }
        goto L_0x0271;
    L_0x0268:
        r0 = move-exception;
        r2 = r19.getId();
        r0 = java.lang.Integer.toHexString(r2);
    L_0x0271:
        r2 = new java.lang.IllegalStateException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ";
        r4.append(r5);
        r5 = r1.mExpectedAdapterCount;
        r4.append(r5);
        r5 = ", found: ";
        r4.append(r5);
        r4.append(r6);
        r5 = " Pager id: ";
        r4.append(r5);
        r4.append(r0);
        r5 = " Pager class: ";
        r4.append(r5);
        r5 = r19.getClass();
        r4.append(r5);
        r5 = " Problematic adapter: ";
        r4.append(r5);
        r5 = r1.mAdapter;
        r5 = r5.getClass();
        r4.append(r5);
        r4 = r4.toString();
        r2.<init>(r4);
        throw r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.view.ViewPager.populate(int):void");
    }

    public ViewPager(@NonNull Context context) {
        super(context);
        initViewPager();
    }

    public ViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = configuration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int) (400.0f * density);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        this.mFlingDistance = (int) (25.0f * density);
        this.mCloseEnough = (int) (2.0f * density);
        this.mDefaultGutterSize = (int) (16.0f * density);
        ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        ViewCompat.setOnApplyWindowInsetsListener(this, new C02684());
    }

    protected void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        Scroller scroller = this.mScroller;
        if (!(scroller == null || scroller.isFinished())) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }

    void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mPageTransformer != null) {
                enableLayers(newState != 0);
            }
            dispatchOnScrollStateChanged(newState);
        }
    }

    public void setAdapter(@Nullable PagerAdapter adapter) {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            pagerAdapter.setViewPagerObserver(null);
            this.mAdapter.startUpdate((ViewGroup) this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo ii = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        pagerAdapter = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.setViewPagerObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean wasFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (wasFirstLayout) {
                requestLayout();
            } else {
                populate();
            }
        }
        List list = this.mAdapterChangeListeners;
        if (list != null && !list.isEmpty()) {
            int count = this.mAdapterChangeListeners.size();
            for (int i2 = 0; i2 < count; i2++) {
                ((OnAdapterChangeListener) this.mAdapterChangeListeners.get(i2)).onAdapterChanged(this, pagerAdapter, adapter);
            }
        }
    }

    private void removeNonDecorViews() {
        int i = 0;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    @Nullable
    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public void addOnAdapterChangeListener(@NonNull OnAdapterChangeListener listener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList();
        }
        this.mAdapterChangeListeners.add(listener);
    }

    public void removeOnAdapterChangeListener(@NonNull OnAdapterChangeListener listener) {
        List list = this.mAdapterChangeListeners;
        if (list != null) {
            list.remove(listener);
        }
    }

    private int getClientWidth() {
        return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, this.mFirstLayout ^ 1, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            if (pagerAdapter.getCount() > 0) {
                if (always || this.mCurItem != item || this.mItems.size() == 0) {
                    boolean dispatchSelected = true;
                    if (item < 0) {
                        item = 0;
                    } else if (item >= this.mAdapter.getCount()) {
                        item = this.mAdapter.getCount() - 1;
                    }
                    int pageLimit = this.mOffscreenPageLimit;
                    int i = this.mCurItem;
                    if (item > i + pageLimit || item < i - pageLimit) {
                        for (i = 0; i < this.mItems.size(); i++) {
                            ((ItemInfo) this.mItems.get(i)).scrolling = true;
                        }
                    }
                    if (this.mCurItem == item) {
                        dispatchSelected = false;
                    }
                    if (this.mFirstLayout) {
                        this.mCurItem = item;
                        if (dispatchSelected) {
                            dispatchOnPageSelected(item);
                        }
                        requestLayout();
                    } else {
                        populate(item);
                        scrollToItem(item, smoothScroll, velocity, dispatchSelected);
                    }
                    return;
                }
                setScrollingCacheEnabled(false);
                return;
            }
        }
        setScrollingCacheEnabled(false);
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ItemInfo curInfo = infoForPosition(item);
        int destX = 0;
        if (curInfo != null) {
            destX = (int) (((float) getClientWidth()) * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
        }
        if (smoothScroll) {
            smoothScrollTo(destX, 0, velocity);
            if (dispatchSelected) {
                dispatchOnPageSelected(item);
                return;
            }
            return;
        }
        if (dispatchSelected) {
            dispatchOnPageSelected(item);
        }
        completeScroll(false);
        scrollTo(destX, 0);
        pageScrolled(destX);
    }

    @Deprecated
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void addOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList();
        }
        this.mOnPageChangeListeners.add(listener);
    }

    public void removeOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        List list = this.mOnPageChangeListeners;
        if (list != null) {
            list.remove(listener);
        }
    }

    public void clearOnPageChangeListeners() {
        List list = this.mOnPageChangeListeners;
        if (list != null) {
            list.clear();
        }
    }

    public void setPageTransformer(boolean reverseDrawingOrder, @Nullable PageTransformer transformer) {
        setPageTransformer(reverseDrawingOrder, transformer, 2);
    }

    public void setPageTransformer(boolean reverseDrawingOrder, @Nullable PageTransformer transformer, int pageLayerType) {
        int i = 1;
        boolean hasTransformer = transformer != null;
        boolean needsPopulate = hasTransformer != (this.mPageTransformer != null);
        this.mPageTransformer = transformer;
        setChildrenDrawingOrderEnabled(hasTransformer);
        if (hasTransformer) {
            if (reverseDrawingOrder) {
                i = 2;
            }
            this.mDrawingOrder = i;
            this.mPageTransformerLayerType = pageLayerType;
        } else {
            this.mDrawingOrder = 0;
        }
        if (needsPopulate) {
            populate();
        }
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        return ((LayoutParams) ((View) this.mDrawingOrderedChildren.get(this.mDrawingOrder == 2 ? (childCount - 1) - i : i)).getLayoutParams()).childIndex;
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
        OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested offscreen page limit ");
            stringBuilder.append(limit);
            stringBuilder.append(" too small; defaulting to ");
            stringBuilder.append(1);
            Log.w(str, stringBuilder.toString());
            limit = 1;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            populate();
        }
    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = getWidth();
        recomputeScrollPosition(width, width, marginPixels, oldMargin);
        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(@Nullable Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            refreshDrawableState();
        }
        setWillNotDraw(d == null);
        invalidate();
    }

    public void setPageMarginDrawable(@DrawableRes int resId) {
        setPageMarginDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    protected boolean verifyDrawable(Drawable who) {
        if (!super.verifyDrawable(who)) {
            if (who != this.mMarginDrawable) {
                return false;
            }
        }
        return true;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((f - 0.5f) * 0.47123894f));
    }

    void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        ViewPager viewPager = this;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int sx;
        Scroller scroller = viewPager.mScroller;
        boolean wasScrolling = (scroller == null || scroller.isFinished()) ? false : true;
        if (wasScrolling) {
            sx = viewPager.mIsScrollStarted ? viewPager.mScroller.getCurrX() : viewPager.mScroller.getStartX();
            viewPager.mScroller.abortAnimation();
            setScrollingCacheEnabled(false);
        } else {
            sx = getScrollX();
        }
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if (dx == 0 && dy == 0) {
            completeScroll(false);
            populate();
            setScrollState(0);
            return;
        }
        int duration;
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int width = getClientWidth();
        int halfWidth = width / 2;
        float distance = ((float) halfWidth) + (((float) halfWidth) * distanceInfluenceForSnapDuration(Math.min(1.0f, (((float) Math.abs(dx)) * 1.0f) / ((float) width))));
        int velocity2 = Math.abs(velocity);
        if (velocity2 > 0) {
            duration = Math.round(Math.abs(distance / ((float) velocity2)) * 1000.0f) * 4;
        } else {
            duration = (int) ((1.0f + (((float) Math.abs(dx)) / (((float) viewPager.mPageMargin) + (((float) width) * viewPager.mAdapter.getPageWidth(viewPager.mCurItem))))) * 100.0f);
        }
        int duration2 = Math.min(duration, MAX_SETTLE_DURATION);
        viewPager.mIsScrollStarted = false;
        viewPager.mScroller.startScroll(sx, sy, dx, dy, duration2);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    ItemInfo addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem((ViewGroup) this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if (index >= 0) {
            if (index < this.mItems.size()) {
                this.mItems.add(index, ii);
                return ii;
            }
        }
        this.mItems.add(ii);
        return ii;
    }

    void dataSetChanged() {
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        boolean needPopulate = this.mItems.size() < (this.mOffscreenPageLimit * 2) + 1 && this.mItems.size() < adapterCount;
        int newCurrItem = this.mCurItem;
        boolean isUpdating = false;
        int i = 0;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            int newPos = this.mAdapter.getItemPosition(ii.object);
            if (newPos != -1) {
                if (newPos == -2) {
                    this.mItems.remove(i);
                    i--;
                    if (!isUpdating) {
                        this.mAdapter.startUpdate((ViewGroup) this);
                        isUpdating = true;
                    }
                    this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
                    needPopulate = true;
                    if (this.mCurItem == ii.position) {
                        newCurrItem = Math.max(0, Math.min(this.mCurItem, adapterCount - 1));
                        needPopulate = true;
                    }
                } else if (ii.position != newPos) {
                    if (ii.position == this.mCurItem) {
                        newCurrItem = newPos;
                    }
                    ii.position = newPos;
                    needPopulate = true;
                }
            }
            i++;
        }
        if (isUpdating) {
            this.mAdapter.finishUpdate((ViewGroup) this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (needPopulate) {
            i = getChildCount();
            for (int i2 = 0; i2 < i; i2++) {
                LayoutParams lp = (LayoutParams) getChildAt(i2).getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(newCurrItem, false, true);
            requestLayout();
        }
    }

    void populate() {
        populate(this.mCurItem);
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            ArrayList arrayList = this.mDrawingOrderedChildren;
            if (arrayList == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                arrayList.clear();
            }
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                this.mDrawingOrderedChildren.add(getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }
    }

    private void calculatePageOffsets(ItemInfo curItem, int curIndex, ItemInfo oldCurInfo) {
        int oldCurPosition;
        int itemIndex;
        float offset;
        int N = this.mAdapter.getCount();
        int width = getClientWidth();
        float marginOffset = width > 0 ? ((float) this.mPageMargin) / ((float) width) : 0.0f;
        if (oldCurInfo != null) {
            oldCurPosition = oldCurInfo.position;
            int pos;
            ItemInfo ii;
            if (oldCurPosition < curItem.position) {
                itemIndex = 0;
                offset = (oldCurInfo.offset + oldCurInfo.widthFactor) + marginOffset;
                pos = oldCurPosition + 1;
                while (pos <= curItem.position && itemIndex < this.mItems.size()) {
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                    while (pos > ii.position && itemIndex < this.mItems.size() - 1) {
                        itemIndex++;
                        ii = (ItemInfo) this.mItems.get(itemIndex);
                    }
                    while (pos < ii.position) {
                        offset += this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos++;
                    }
                    ii.offset = offset;
                    offset += ii.widthFactor + marginOffset;
                    pos++;
                }
            } else if (oldCurPosition > curItem.position) {
                itemIndex = this.mItems.size() - 1;
                offset = oldCurInfo.offset;
                pos = oldCurPosition - 1;
                while (pos >= curItem.position && itemIndex >= 0) {
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                    while (pos < ii.position && itemIndex > 0) {
                        itemIndex--;
                        ii = (ItemInfo) this.mItems.get(itemIndex);
                    }
                    while (pos > ii.position) {
                        offset -= this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos--;
                    }
                    offset -= ii.widthFactor + marginOffset;
                    ii.offset = offset;
                    pos--;
                }
            }
        }
        oldCurPosition = this.mItems.size();
        float offset2 = curItem.offset;
        int pos2 = curItem.position - 1;
        this.mFirstOffset = curItem.position == 0 ? curItem.offset : -3.4028235E38f;
        this.mLastOffset = curItem.position == N + -1 ? (curItem.offset + curItem.widthFactor) - 1.0f : Float.MAX_VALUE;
        int i = curIndex - 1;
        while (i >= 0) {
            ItemInfo ii2 = (ItemInfo) this.mItems.get(i);
            while (pos2 > ii2.position) {
                offset2 -= this.mAdapter.getPageWidth(pos2) + marginOffset;
                pos2--;
            }
            offset2 -= ii2.widthFactor + marginOffset;
            ii2.offset = offset2;
            if (ii2.position == 0) {
                this.mFirstOffset = offset2;
            }
            i--;
            pos2--;
        }
        offset = (curItem.offset + curItem.widthFactor) + marginOffset;
        itemIndex = curItem.position + 1;
        pos2 = curIndex + 1;
        while (pos2 < oldCurPosition) {
            ii2 = (ItemInfo) this.mItems.get(pos2);
            while (itemIndex < ii2.position) {
                offset += this.mAdapter.getPageWidth(itemIndex) + marginOffset;
                itemIndex++;
            }
            if (ii2.position == N - 1) {
                this.mLastOffset = (ii2.widthFactor + offset) - 1.0f;
            }
            ii2.offset = offset;
            offset += ii2.widthFactor + marginOffset;
            pos2++;
            itemIndex++;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.position = this.mCurItem;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            ss.adapterState = pagerAdapter.saveState();
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            PagerAdapter pagerAdapter = this.mAdapter;
            if (pagerAdapter != null) {
                pagerAdapter.restoreState(ss.adapterState, ss.loader);
                setCurrentItemInternal(ss.position, false, true);
            } else {
                this.mRestoredCurItem = ss.position;
                this.mRestoredAdapterState = ss.adapterState;
                this.mRestoredClassLoader = ss.loader;
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        LayoutParams lp = (LayoutParams) params;
        lp.isDecor |= isDecorView(child);
        if (this.mInLayout) {
            if (lp != null) {
                if (lp.isDecor) {
                    throw new IllegalStateException("Cannot add pager decor view during layout");
                }
            }
            lp.needsMeasure = true;
            addViewInLayout(child, index, params);
            return;
        }
        super.addView(child, index, params);
    }

    private static boolean isDecorView(@NonNull View view) {
        return view.getClass().getAnnotation(DecorView.class) != null;
    }

    public void removeView(View view) {
        if (this.mInLayout) {
            removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }
        return null;
    }

    ItemInfo infoForAnyChild(View child) {
        while (true) {
            Object parent = child.getParent();
            ViewParent parent2 = parent;
            if (parent == this) {
                return infoForChild(child);
            }
            if (parent2 == null) {
                break;
            } else if (!(parent2 instanceof View)) {
                break;
            } else {
                child = (View) parent2;
            }
        }
        return null;
    }

    ItemInfo infoForPosition(int position) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
        }
        return null;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxGutterSize;
        int widthSize;
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = getMeasuredWidth();
        int maxGutterSize2 = measuredWidth / 10;
        this.mGutterSize = Math.min(maxGutterSize2, this.mDefaultGutterSize);
        int childWidthSize = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int childHeightSize = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int size = getChildCount();
        int i = 0;
        while (i < size) {
            int measuredWidth2;
            int heightMode;
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    measuredWidth2 = measuredWidth;
                    maxGutterSize = maxGutterSize2;
                } else {
                    boolean consumeVertical;
                    boolean z;
                    boolean consumeHorizontal;
                    int widthSize2;
                    int heightSize;
                    int hgrav = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int widthMode = Integer.MIN_VALUE;
                    maxGutterSize = Integer.MIN_VALUE;
                    if (vgrav != 48) {
                        if (vgrav != 80) {
                            consumeVertical = false;
                            if (hgrav != 3) {
                                if (hgrav == 5) {
                                    z = false;
                                    consumeHorizontal = z;
                                    if (!consumeVertical) {
                                        widthMode = 1073741824;
                                    } else if (consumeHorizontal) {
                                        maxGutterSize = 1073741824;
                                    }
                                    widthSize2 = childWidthSize;
                                    heightSize = childHeightSize;
                                    measuredWidth2 = measuredWidth;
                                    if (lp.width == -2) {
                                        widthMode = 1073741824;
                                        if (lp.width == -1) {
                                            widthSize = lp.width;
                                        } else {
                                            widthSize = widthSize2;
                                        }
                                    } else {
                                        widthSize = widthSize2;
                                    }
                                    if (lp.height == -2) {
                                        heightMode = maxGutterSize;
                                        measuredWidth = heightSize;
                                    } else if (lp.height == -1) {
                                        measuredWidth = lp.height;
                                        heightMode = 1073741824;
                                    } else {
                                        heightMode = 1073741824;
                                        measuredWidth = heightSize;
                                    }
                                    maxGutterSize = maxGutterSize2;
                                    widthSize2 = widthSize;
                                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                    if (!consumeVertical) {
                                        childHeightSize -= child.getMeasuredHeight();
                                    } else if (!consumeHorizontal) {
                                        childWidthSize -= child.getMeasuredWidth();
                                    }
                                }
                            }
                            z = true;
                            consumeHorizontal = z;
                            if (!consumeVertical) {
                                widthMode = 1073741824;
                            } else if (consumeHorizontal) {
                                maxGutterSize = 1073741824;
                            }
                            widthSize2 = childWidthSize;
                            heightSize = childHeightSize;
                            measuredWidth2 = measuredWidth;
                            if (lp.width == -2) {
                                widthSize = widthSize2;
                            } else {
                                widthMode = 1073741824;
                                if (lp.width == -1) {
                                    widthSize = widthSize2;
                                } else {
                                    widthSize = lp.width;
                                }
                            }
                            if (lp.height == -2) {
                                heightMode = maxGutterSize;
                                measuredWidth = heightSize;
                            } else if (lp.height == -1) {
                                heightMode = 1073741824;
                                measuredWidth = heightSize;
                            } else {
                                measuredWidth = lp.height;
                                heightMode = 1073741824;
                            }
                            maxGutterSize = maxGutterSize2;
                            widthSize2 = widthSize;
                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                            if (!consumeVertical) {
                                childHeightSize -= child.getMeasuredHeight();
                            } else if (!consumeHorizontal) {
                                childWidthSize -= child.getMeasuredWidth();
                            }
                        }
                    }
                    consumeVertical = true;
                    if (hgrav != 3) {
                        if (hgrav == 5) {
                            z = false;
                            consumeHorizontal = z;
                            if (!consumeVertical) {
                                widthMode = 1073741824;
                            } else if (consumeHorizontal) {
                                maxGutterSize = 1073741824;
                            }
                            widthSize2 = childWidthSize;
                            heightSize = childHeightSize;
                            measuredWidth2 = measuredWidth;
                            if (lp.width == -2) {
                                widthMode = 1073741824;
                                if (lp.width == -1) {
                                    widthSize = lp.width;
                                } else {
                                    widthSize = widthSize2;
                                }
                            } else {
                                widthSize = widthSize2;
                            }
                            if (lp.height == -2) {
                                heightMode = maxGutterSize;
                                measuredWidth = heightSize;
                            } else if (lp.height == -1) {
                                measuredWidth = lp.height;
                                heightMode = 1073741824;
                            } else {
                                heightMode = 1073741824;
                                measuredWidth = heightSize;
                            }
                            maxGutterSize = maxGutterSize2;
                            widthSize2 = widthSize;
                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                            if (!consumeVertical) {
                                childHeightSize -= child.getMeasuredHeight();
                            } else if (!consumeHorizontal) {
                                childWidthSize -= child.getMeasuredWidth();
                            }
                        }
                    }
                    z = true;
                    consumeHorizontal = z;
                    if (!consumeVertical) {
                        widthMode = 1073741824;
                    } else if (consumeHorizontal) {
                        maxGutterSize = 1073741824;
                    }
                    widthSize2 = childWidthSize;
                    heightSize = childHeightSize;
                    measuredWidth2 = measuredWidth;
                    if (lp.width == -2) {
                        widthSize = widthSize2;
                    } else {
                        widthMode = 1073741824;
                        if (lp.width == -1) {
                            widthSize = widthSize2;
                        } else {
                            widthSize = lp.width;
                        }
                    }
                    if (lp.height == -2) {
                        heightMode = maxGutterSize;
                        measuredWidth = heightSize;
                    } else if (lp.height == -1) {
                        heightMode = 1073741824;
                        measuredWidth = heightSize;
                    } else {
                        measuredWidth = lp.height;
                        heightMode = 1073741824;
                    }
                    maxGutterSize = maxGutterSize2;
                    widthSize2 = widthSize;
                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                    if (!consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (!consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            } else {
                measuredWidth2 = measuredWidth;
                maxGutterSize = maxGutterSize2;
            }
            i++;
            maxGutterSize2 = maxGutterSize;
            measuredWidth = measuredWidth2;
            widthSize = widthMeasureSpec;
            heightMode = heightMeasureSpec;
        }
        maxGutterSize = maxGutterSize2;
        r0.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, 1073741824);
        r0.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);
        r0.mInLayout = true;
        populate();
        r0.mInLayout = false;
        widthSize = getChildCount();
        for (measuredWidth = 0; measuredWidth < widthSize; measuredWidth++) {
            View child2 = getChildAt(measuredWidth);
            if (child2.getVisibility() != 8) {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (lp2 == null || !lp2.isDecor) {
                    child2.measure(MeasureSpec.makeMeasureSpec((int) (((float) childWidthSize) * lp2.widthFactor), 1073741824), r0.mChildHeightMeasureSpec);
                }
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            int i = this.mPageMargin;
            recomputeScrollPosition(w, oldw, i, i);
        }
    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        if (oldWidth <= 0 || this.mItems.isEmpty()) {
            ItemInfo ii = infoForPosition(this.mCurItem);
            int scrollPos = (int) (((float) ((width - getPaddingLeft()) - getPaddingRight())) * (ii != null ? Math.min(ii.offset, this.mLastOffset) : 0.0f));
            if (scrollPos != getScrollX()) {
                completeScroll(false);
                scrollTo(scrollPos, getScrollY());
            }
        } else if (this.mScroller.isFinished()) {
            scrollTo((int) (((float) (((width - getPaddingLeft()) - getPaddingRight()) + margin)) * (((float) getScrollX()) / ((float) (((oldWidth - getPaddingLeft()) - getPaddingRight()) + oldMargin)))), getScrollY());
        } else {
            this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width;
        int paddingLeft;
        boolean z;
        ViewPager viewPager = this;
        int count = getChildCount();
        int width2 = r - l;
        int height = b - t;
        int paddingLeft2 = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int decorCount = 0;
        int i = 0;
        while (true) {
            int i2 = 8;
            if (i >= count) {
                break;
            }
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childLeft;
                if (lp.isDecor) {
                    int hgrav = lp.gravity & 7;
                    childLeft = 0;
                    int childLeft2 = lp.gravity & 112;
                    if (hgrav == 1) {
                        lp = Math.max((width2 - child.getMeasuredWidth()) / 2, paddingLeft2);
                    } else if (hgrav == 3) {
                        lp = paddingLeft2;
                        paddingLeft2 += child.getMeasuredWidth();
                    } else if (hgrav != 5) {
                        lp = paddingLeft2;
                    } else {
                        lp = (width2 - paddingRight) - child.getMeasuredWidth();
                        paddingRight += child.getMeasuredWidth();
                    }
                    childLeft = paddingLeft2;
                    if (childLeft2 == 16) {
                        paddingLeft2 = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);
                    } else if (childLeft2 == 48) {
                        paddingLeft2 = paddingTop;
                        paddingTop += child.getMeasuredHeight();
                    } else if (childLeft2 != 80) {
                        paddingLeft2 = paddingTop;
                    } else {
                        paddingLeft2 = (height - paddingBottom) - child.getMeasuredHeight();
                        paddingBottom += child.getMeasuredHeight();
                    }
                    lp += scrollX;
                    int paddingTop2 = paddingTop;
                    child.layout(lp, paddingLeft2, child.getMeasuredWidth() + lp, paddingLeft2 + child.getMeasuredHeight());
                    decorCount++;
                    paddingLeft2 = childLeft;
                    paddingTop = paddingTop2;
                } else {
                    childLeft = 0;
                }
            }
            i++;
        }
        i = (width2 - paddingLeft2) - paddingRight;
        int i3 = 0;
        while (i3 < count) {
            int count2;
            View child2 = getChildAt(i3);
            if (child2.getVisibility() != i2) {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (lp2.isDecor) {
                    count2 = count;
                    width = width2;
                    paddingLeft = paddingLeft2;
                } else {
                    ItemInfo infoForChild = infoForChild(child2);
                    ItemInfo ii = infoForChild;
                    ItemInfo ii2;
                    if (infoForChild != null) {
                        ii2 = ii;
                        count2 = count;
                        count = (int) (((float) i) * ii2.offset);
                        i2 = paddingLeft2 + count;
                        paddingTop2 = paddingTop;
                        if (lp2.needsMeasure) {
                            lp2.needsMeasure = false;
                            width = width2;
                            paddingLeft = paddingLeft2;
                            child2.measure(MeasureSpec.makeMeasureSpec((int) (((float) i) * lp2.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec((height - paddingTop) - paddingBottom, 1073741824));
                        } else {
                            width = width2;
                            paddingLeft = paddingLeft2;
                        }
                        paddingLeft2 = paddingTop2;
                        child2.layout(i2, paddingLeft2, child2.getMeasuredWidth() + i2, child2.getMeasuredHeight() + paddingLeft2);
                    } else {
                        width = width2;
                        paddingLeft = paddingLeft2;
                        ii2 = ii;
                        count2 = count;
                    }
                }
            } else {
                count2 = count;
                width = width2;
                paddingLeft = paddingLeft2;
            }
            i3++;
            count = count2;
            width2 = width;
            paddingLeft2 = paddingLeft;
            i2 = 8;
        }
        width = width2;
        paddingLeft = paddingLeft2;
        viewPager.mTopPageBounds = paddingTop;
        viewPager.mBottomPageBounds = height - paddingBottom;
        viewPager.mDecorChildCount = decorCount;
        if (viewPager.mFirstLayout) {
            z = false;
            scrollToItem(viewPager.mCurItem, false, 0, false);
        } else {
            z = false;
        }
        viewPager.mFirstLayout = z;
    }

    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll(true);
            return;
        }
        int oldX = getScrollX();
        int oldY = getScrollY();
        int x = this.mScroller.getCurrX();
        int y = this.mScroller.getCurrY();
        if (!(oldX == x && oldY == y)) {
            scrollTo(x, y);
            if (!pageScrolled(x)) {
                this.mScroller.abortAnimation();
                scrollTo(0, y);
            }
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() != 0) {
            ItemInfo ii = infoForCurrentScrollPosition();
            int width = getClientWidth();
            int i = this.mPageMargin;
            int widthWithMargin = width + i;
            float marginOffset = ((float) i) / ((float) width);
            int currentPage = ii.position;
            float pageOffset = ((((float) xpos) / ((float) width)) - ii.offset) / (ii.widthFactor + marginOffset);
            int offsetPixels = (int) (((float) widthWithMargin) * pageOffset);
            this.mCalledSuper = false;
            onPageScrolled(currentPage, pageOffset, offsetPixels);
            if (this.mCalledSuper) {
                return true;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        } else if (this.mFirstLayout) {
            return false;
        } else {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
    }

    @CallSuper
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        int scrollX;
        int paddingLeft;
        int paddingRight;
        if (this.mDecorChildCount > 0) {
            scrollX = getScrollX();
            paddingLeft = getPaddingLeft();
            paddingRight = getPaddingRight();
            int width = getWidth();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int childLeft;
                    int hgrav = lp.gravity & 7;
                    if (hgrav == 1) {
                        childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                    } else if (hgrav == 3) {
                        childLeft = paddingLeft;
                        paddingLeft += child.getWidth();
                    } else if (hgrav != 5) {
                        childLeft = paddingLeft;
                    } else {
                        childLeft = (width - paddingRight) - child.getMeasuredWidth();
                        paddingRight += child.getMeasuredWidth();
                    }
                    int childOffset = (childLeft + scrollX) - child.getLeft();
                    if (childOffset != 0) {
                        child.offsetLeftAndRight(childOffset);
                    }
                }
            }
        }
        dispatchOnPageScrolled(position, offset, offsetPixels);
        if (r0.mPageTransformer != null) {
            scrollX = getScrollX();
            paddingLeft = getChildCount();
            for (paddingRight = 0; paddingRight < paddingLeft; paddingRight++) {
                View child2 = getChildAt(paddingRight);
                if (!((LayoutParams) child2.getLayoutParams()).isDecor) {
                    r0.mPageTransformer.transformPage(child2, ((float) (child2.getLeft() - scrollX)) / ((float) getClientWidth()));
                }
            }
        }
        r0.mCalledSuper = true;
    }

    private void dispatchOnPageScrolled(int position, float offset, int offsetPixels) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        int z = this.mOnPageChangeListeners;
        if (z != 0) {
            z = z.size();
            for (int i = 0; i < z; i++) {
                OnPageChangeListener listener = (OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrolled(position, offset, offsetPixels);
                }
            }
        }
        onPageChangeListener = this.mInternalPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
    }

    private void dispatchOnPageSelected(int position) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
        int z = this.mOnPageChangeListeners;
        if (z != 0) {
            z = z.size();
            for (int i = 0; i < z; i++) {
                OnPageChangeListener listener = (OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageSelected(position);
                }
            }
        }
        onPageChangeListener = this.mInternalPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
    }

    private void dispatchOnScrollStateChanged(int state) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
        int z = this.mOnPageChangeListeners;
        if (z != 0) {
            z = z.size();
            for (int i = 0; i < z; i++) {
                OnPageChangeListener listener = (OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrollStateChanged(state);
                }
            }
        }
        onPageChangeListener = this.mInternalPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate = this.mScrollState == 2;
        if (needPopulate) {
            setScrollingCacheEnabled(false);
            if (true ^ this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                int oldX = getScrollX();
                int oldY = getScrollY();
                int x = this.mScroller.getCurrX();
                int y = this.mScroller.getCurrY();
                if (!(oldX == x && oldY == y)) {
                    scrollTo(x, y);
                    if (x != oldX) {
                        pageScrolled(x);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.scrolling) {
                needPopulate = true;
                ii.scrolling = false;
            }
        }
        if (!needPopulate) {
            return;
        }
        if (postEvents) {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
        } else {
            this.mEndScrollRunnable.run();
        }
    }

    private boolean isGutterDrag(float x, float dx) {
        return (x < ((float) this.mGutterSize) && dx > 0.0f) || (x > ((float) (getWidth() - this.mGutterSize)) && dx < 0.0f);
    }

    private void enableLayers(boolean enable) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setLayerType(enable ? this.mPageTransformerLayerType : 0, null);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ViewPager viewPager = this;
        MotionEvent motionEvent = ev;
        int action = ev.getAction() & 255;
        if (action != 3) {
            if (action != 1) {
                if (action != 0) {
                    if (viewPager.mIsBeingDragged) {
                        return true;
                    }
                    if (viewPager.mIsUnableToDrag) {
                        return false;
                    }
                }
                if (action == 0) {
                    float x = ev.getX();
                    viewPager.mInitialMotionX = x;
                    viewPager.mLastMotionX = x;
                    x = ev.getY();
                    viewPager.mInitialMotionY = x;
                    viewPager.mLastMotionY = x;
                    viewPager.mActivePointerId = motionEvent.getPointerId(0);
                    viewPager.mIsUnableToDrag = false;
                    viewPager.mIsScrollStarted = true;
                    viewPager.mScroller.computeScrollOffset();
                    if (viewPager.mScrollState != 2 || Math.abs(viewPager.mScroller.getFinalX() - viewPager.mScroller.getCurrX()) <= viewPager.mCloseEnough) {
                        completeScroll(false);
                        viewPager.mIsBeingDragged = false;
                    } else {
                        viewPager.mScroller.abortAnimation();
                        viewPager.mPopulatePending = false;
                        populate();
                        viewPager.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        setScrollState(1);
                    }
                } else if (action == 2) {
                    int activePointerId = viewPager.mActivePointerId;
                    if (activePointerId != -1) {
                        float y;
                        int pointerIndex = motionEvent.findPointerIndex(activePointerId);
                        float x2 = motionEvent.getX(pointerIndex);
                        float dx = x2 - viewPager.mLastMotionX;
                        float xDiff = Math.abs(dx);
                        float y2 = motionEvent.getY(pointerIndex);
                        float yDiff = Math.abs(y2 - viewPager.mInitialMotionY);
                        if (dx == 0.0f || isGutterDrag(viewPager.mLastMotionX, dx)) {
                            y = y2;
                        } else {
                            y = y2;
                            if (canScroll(this, false, (int) dx, (int) x2, (int) y2)) {
                                viewPager.mLastMotionX = x2;
                                viewPager.mLastMotionY = y;
                                viewPager.mIsUnableToDrag = true;
                                return false;
                            }
                        }
                        if (xDiff > ((float) viewPager.mTouchSlop) && 0.5f * xDiff > yDiff) {
                            viewPager.mIsBeingDragged = true;
                            requestParentDisallowInterceptTouchEvent(true);
                            setScrollState(1);
                            viewPager.mLastMotionX = dx > 0.0f ? viewPager.mInitialMotionX + ((float) viewPager.mTouchSlop) : viewPager.mInitialMotionX - ((float) viewPager.mTouchSlop);
                            viewPager.mLastMotionY = y;
                            setScrollingCacheEnabled(true);
                        } else if (yDiff > ((float) viewPager.mTouchSlop)) {
                            viewPager.mIsUnableToDrag = true;
                        }
                        if (viewPager.mIsBeingDragged && performDrag(x2)) {
                            ViewCompat.postInvalidateOnAnimation(this);
                        }
                    }
                } else if (action == 6) {
                    onSecondaryPointerUp(ev);
                }
                if (viewPager.mVelocityTracker == null) {
                    viewPager.mVelocityTracker = VelocityTracker.obtain();
                }
                viewPager.mVelocityTracker.addMovement(motionEvent);
                return viewPager.mIsBeingDragged;
            }
        }
        resetTouch();
        return false;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        MotionEvent motionEvent = ev;
        if (this.mFakeDragging) {
            return true;
        }
        if (ev.getAction() == 0 && ev.getEdgeFlags() != 0) {
            return false;
        }
        PagerAdapter pagerAdapter = r0.mAdapter;
        if (pagerAdapter != null) {
            if (pagerAdapter.getCount() != 0) {
                if (r0.mVelocityTracker == null) {
                    r0.mVelocityTracker = VelocityTracker.obtain();
                }
                r0.mVelocityTracker.addMovement(motionEvent);
                int action = ev.getAction();
                boolean needsInvalidate = false;
                int i;
                int pointerIndex;
                switch (action & 255) {
                    case 0:
                        r0.mScroller.abortAnimation();
                        r0.mPopulatePending = false;
                        populate();
                        float x = ev.getX();
                        r0.mInitialMotionX = x;
                        r0.mLastMotionX = x;
                        x = ev.getY();
                        r0.mInitialMotionY = x;
                        r0.mLastMotionY = x;
                        r0.mActivePointerId = motionEvent.getPointerId(0);
                        break;
                    case 1:
                        if (!r0.mIsBeingDragged) {
                            break;
                        }
                        VelocityTracker velocityTracker = r0.mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, (float) r0.mMaximumVelocity);
                        int initialVelocity = (int) velocityTracker.getXVelocity(r0.mActivePointerId);
                        r0.mPopulatePending = true;
                        int width = getClientWidth();
                        int scrollX = getScrollX();
                        ItemInfo ii = infoForCurrentScrollPosition();
                        i = action;
                        setCurrentItemInternal(determineTargetPage(ii.position, ((((float) scrollX) / ((float) width)) - ii.offset) / (ii.widthFactor + (((float) r0.mPageMargin) / ((float) width))), initialVelocity, (int) (motionEvent.getX(motionEvent.findPointerIndex(r0.mActivePointerId)) - r0.mInitialMotionX)), true, true, initialVelocity);
                        needsInvalidate = resetTouch();
                        break;
                    case 2:
                        if (!r0.mIsBeingDragged) {
                            pointerIndex = motionEvent.findPointerIndex(r0.mActivePointerId);
                            if (pointerIndex == -1) {
                                needsInvalidate = resetTouch();
                                i = action;
                                break;
                            }
                            float x2 = motionEvent.getX(pointerIndex);
                            float xDiff = Math.abs(x2 - r0.mLastMotionX);
                            float y = motionEvent.getY(pointerIndex);
                            float yDiff = Math.abs(y - r0.mLastMotionY);
                            if (xDiff > ((float) r0.mTouchSlop) && xDiff > yDiff) {
                                r0.mIsBeingDragged = true;
                                requestParentDisallowInterceptTouchEvent(true);
                                float f = r0.mInitialMotionX;
                                r0.mLastMotionX = x2 - f > 0.0f ? f + ((float) r0.mTouchSlop) : f - ((float) r0.mTouchSlop);
                                r0.mLastMotionY = y;
                                setScrollState(1);
                                setScrollingCacheEnabled(true);
                                ViewParent parent = getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                        }
                        if (!r0.mIsBeingDragged) {
                            i = action;
                            break;
                        }
                        needsInvalidate = false | performDrag(motionEvent.getX(motionEvent.findPointerIndex(r0.mActivePointerId)));
                        i = action;
                        break;
                    case 3:
                        if (!r0.mIsBeingDragged) {
                            i = action;
                            break;
                        }
                        scrollToItem(r0.mCurItem, true, 0, false);
                        needsInvalidate = resetTouch();
                        i = action;
                        break;
                    case 5:
                        pointerIndex = ev.getActionIndex();
                        r0.mLastMotionX = motionEvent.getX(pointerIndex);
                        r0.mActivePointerId = motionEvent.getPointerId(pointerIndex);
                        i = action;
                        break;
                    case 6:
                        onSecondaryPointerUp(ev);
                        r0.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(r0.mActivePointerId));
                        i = action;
                        break;
                    default:
                        break;
                }
                if (needsInvalidate) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                return true;
            }
        }
        return false;
    }

    private boolean resetTouch() {
        this.mActivePointerId = -1;
        endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        if (!this.mLeftEdge.isFinished()) {
            if (!this.mRightEdge.isFinished()) {
                return false;
            }
        }
        return true;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private boolean performDrag(float x) {
        float f = x;
        boolean needsInvalidate = false;
        float deltaX = this.mLastMotionX - f;
        this.mLastMotionX = f;
        float scrollX = ((float) getScrollX()) + deltaX;
        int width = getClientWidth();
        float leftBound = ((float) width) * this.mFirstOffset;
        float rightBound = ((float) width) * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;
        ItemInfo firstItem = (ItemInfo) this.mItems.get(0);
        ArrayList arrayList = this.mItems;
        ItemInfo lastItem = (ItemInfo) arrayList.get(arrayList.size() - 1);
        if (firstItem.position != 0) {
            leftAbsolute = false;
            leftBound = firstItem.offset * ((float) width);
        }
        if (lastItem.position != r0.mAdapter.getCount() - 1) {
            rightAbsolute = false;
            rightBound = lastItem.offset * ((float) width);
        }
        if (scrollX < leftBound) {
            if (leftAbsolute) {
                r0.mLeftEdge.onPull(Math.abs(leftBound - scrollX) / ((float) width));
                needsInvalidate = true;
            }
            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                r0.mRightEdge.onPull(Math.abs(scrollX - rightBound) / ((float) width));
                needsInvalidate = true;
            }
            scrollX = rightBound;
        }
        r0.mLastMotionX += scrollX - ((float) ((int) scrollX));
        scrollTo((int) scrollX, getScrollY());
        pageScrolled((int) scrollX);
        return needsInvalidate;
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int width = getClientWidth();
        float marginOffset = 0.0f;
        float scrollOffset = width > 0 ? ((float) getScrollX()) / ((float) width) : 0.0f;
        if (width > 0) {
            marginOffset = ((float) this.mPageMargin) / ((float) width);
        }
        int lastPos = -1;
        float lastOffset = 0.0f;
        float lastWidth = 0.0f;
        boolean first = true;
        ItemInfo lastItem = null;
        int i = 0;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (!(first || ii.position == lastPos + 1)) {
                ii = this.mTempItem;
                ii.offset = (lastOffset + lastWidth) + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                i--;
            }
            float offset = ii.offset;
            float leftBound = offset;
            float rightBound = (ii.widthFactor + offset) + marginOffset;
            if (!first) {
                if (scrollOffset < leftBound) {
                    return lastItem;
                }
            }
            if (scrollOffset >= rightBound) {
                if (i != this.mItems.size() - 1) {
                    first = false;
                    lastPos = ii.position;
                    lastOffset = offset;
                    lastWidth = ii.widthFactor;
                    lastItem = ii;
                    i++;
                }
            }
            return ii;
        }
        return lastItem;
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) <= this.mFlingDistance || Math.abs(velocity) <= this.mMinimumVelocity) {
            targetPage = currentPage + ((int) (pageOffset + (currentPage >= this.mCurItem ? 0.4f : 0.6f)));
        } else {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        }
        if (this.mItems.size() <= 0) {
            return targetPage;
        }
        ItemInfo firstItem = (ItemInfo) this.mItems.get(0);
        ArrayList arrayList = this.mItems;
        return Math.max(firstItem.position, Math.min(targetPage, ((ItemInfo) arrayList.get(arrayList.size() - 1)).position));
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = false;
        int overScrollMode = getOverScrollMode();
        if (overScrollMode != 0) {
            if (overScrollMode == 1) {
                PagerAdapter pagerAdapter = this.mAdapter;
                if (pagerAdapter != null && pagerAdapter.getCount() > 1) {
                }
            }
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
            if (needsInvalidate) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
        if (!this.mLeftEdge.isFinished()) {
            int restoreCount = canvas.save();
            int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
            int width = getWidth();
            canvas.rotate(270.0f);
            canvas.translate((float) ((-height) + getPaddingTop()), this.mFirstOffset * ((float) width));
            this.mLeftEdge.setSize(height, width);
            needsInvalidate = false | this.mLeftEdge.draw(canvas);
            canvas.restoreToCount(restoreCount);
        }
        if (!this.mRightEdge.isFinished()) {
            restoreCount = canvas.save();
            height = getWidth();
            width = (getHeight() - getPaddingTop()) - getPaddingBottom();
            canvas.rotate(90.0f);
            canvas.translate((float) (-getPaddingTop()), (-(this.mLastOffset + 1.0f)) * ((float) height));
            this.mRightEdge.setSize(width, height);
            needsInvalidate |= this.mRightEdge.draw(canvas);
            canvas.restoreToCount(restoreCount);
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin <= 0 || r0.mMarginDrawable == null || r0.mItems.size() <= 0 || r0.mAdapter == null) {
            Canvas canvas2 = canvas;
            return;
        }
        float marginOffset;
        int scrollX = getScrollX();
        int width = getWidth();
        float marginOffset2 = ((float) r0.mPageMargin) / ((float) width);
        int itemIndex = 0;
        ItemInfo ii = (ItemInfo) r0.mItems.get(0);
        float offset = ii.offset;
        int itemCount = r0.mItems.size();
        int firstPos = ii.position;
        int lastPos = ((ItemInfo) r0.mItems.get(itemCount - 1)).position;
        int pos = firstPos;
        while (pos < lastPos) {
            float drawAt;
            while (pos > ii.position && itemIndex < itemCount) {
                itemIndex++;
                ii = (ItemInfo) r0.mItems.get(itemIndex);
            }
            if (pos == ii.position) {
                drawAt = (ii.offset + ii.widthFactor) * ((float) width);
                offset = (ii.offset + ii.widthFactor) + marginOffset2;
            } else {
                drawAt = r0.mAdapter.getPageWidth(pos);
                float drawAt2 = (offset + drawAt) * ((float) width);
                offset += drawAt + marginOffset2;
                drawAt = drawAt2;
            }
            if (((float) r0.mPageMargin) + drawAt > ((float) scrollX)) {
                marginOffset = marginOffset2;
                r0.mMarginDrawable.setBounds(Math.round(drawAt), r0.mTopPageBounds, Math.round(((float) r0.mPageMargin) + drawAt), r0.mBottomPageBounds);
                r0.mMarginDrawable.draw(canvas);
            } else {
                canvas2 = canvas;
                marginOffset = marginOffset2;
            }
            if (drawAt <= ((float) (scrollX + width))) {
                pos++;
                marginOffset2 = marginOffset;
            } else {
                return;
            }
        }
        canvas2 = canvas;
        marginOffset = marginOffset2;
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
        long time = SystemClock.uptimeMillis();
        MotionEvent ev = MotionEvent.obtain(time, time, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(ev);
        ev.recycle();
        this.mFakeDragBeginTime = time;
        return true;
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            if (this.mAdapter != null) {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                this.mPopulatePending = true;
                int width = getClientWidth();
                int scrollX = getScrollX();
                ItemInfo ii = infoForCurrentScrollPosition();
                setCurrentItemInternal(determineTargetPage(ii.position, ((((float) scrollX) / ((float) width)) - ii.offset) / ii.widthFactor, initialVelocity, (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, initialVelocity);
            }
            endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public void fakeDragBy(float xOffset) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else if (r0.mAdapter != null) {
            r0.mLastMotionX += xOffset;
            float scrollX = ((float) getScrollX()) - xOffset;
            int width = getClientWidth();
            float leftBound = ((float) width) * r0.mFirstOffset;
            float rightBound = ((float) width) * r0.mLastOffset;
            ItemInfo firstItem = (ItemInfo) r0.mItems.get(0);
            ArrayList arrayList = r0.mItems;
            ItemInfo lastItem = (ItemInfo) arrayList.get(arrayList.size() - 1);
            if (firstItem.position != 0) {
                leftBound = firstItem.offset * ((float) width);
            }
            if (lastItem.position != r0.mAdapter.getCount() - 1) {
                rightBound = lastItem.offset * ((float) width);
            }
            if (scrollX < leftBound) {
                scrollX = leftBound;
            } else if (scrollX > rightBound) {
                scrollX = rightBound;
            }
            r0.mLastMotionX += scrollX - ((float) ((int) scrollX));
            scrollTo((int) scrollX, getScrollY());
            pageScrolled((int) scrollX);
            MotionEvent ev = MotionEvent.obtain(r0.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, r0.mLastMotionX, 0.0f, 0);
            r0.mVelocityTracker.addMovement(ev);
            ev.recycle();
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = ev.getX(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }
    }

    public boolean canScrollHorizontally(int direction) {
        boolean z = false;
        if (this.mAdapter == null) {
            return false;
        }
        int width = getClientWidth();
        int scrollX = getScrollX();
        if (direction < 0) {
            if (scrollX > ((int) (((float) width) * this.mFirstOffset))) {
                z = true;
            }
            return z;
        } else if (direction <= 0) {
            return false;
        } else {
            if (scrollX < ((int) (((float) width) * this.mLastOffset))) {
                z = true;
            }
            return z;
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        View view = v;
        boolean z = true;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            for (int i = group.getChildCount() - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()) {
                    if (canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                        return true;
                    }
                }
            }
        }
        if (!checkV) {
            int i2 = dx;
        } else if (v.canScrollHorizontally(-dx)) {
            return z;
        }
        z = false;
        return z;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!super.dispatchKeyEvent(event)) {
            if (!executeKeyEvent(event)) {
                return false;
            }
        }
        return true;
    }

    public boolean executeKeyEvent(@NonNull KeyEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        int keyCode = event.getKeyCode();
        if (keyCode != 61) {
            switch (keyCode) {
                case 21:
                    if (event.hasModifiers(2)) {
                        return pageLeft();
                    }
                    return arrowScroll(17);
                case 22:
                    if (event.hasModifiers(2)) {
                        return pageRight();
                    }
                    return arrowScroll(66);
                default:
                    return false;
            }
        } else if (event.hasNoModifiers()) {
            return arrowScroll(2);
        } else {
            if (event.hasModifiers(1)) {
                return arrowScroll(1);
            }
            return false;
        }
    }

    public boolean arrowScroll(int direction) {
        boolean isChild;
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            isChild = false;
            for (ViewPager parent = currentFocused.getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
                if (parent == this) {
                    isChild = true;
                    break;
                }
            }
            if (!isChild) {
                StringBuilder sb = new StringBuilder();
                sb.append(currentFocused.getClass().getSimpleName());
                for (ViewParent parent2 = currentFocused.getParent(); parent2 instanceof ViewGroup; parent2 = parent2.getParent()) {
                    sb.append(" => ");
                    sb.append(parent2.getClass().getSimpleName());
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("arrowScroll tried to find focus based on non-child current focused view ");
                stringBuilder.append(sb.toString());
                Log.e(str, stringBuilder.toString());
                currentFocused = null;
            }
        }
        isChild = false;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused == null || nextFocused == currentFocused) {
            if (direction != 17) {
                if (direction != 1) {
                    if (direction == 66 || direction == 2) {
                        isChild = pageRight();
                    }
                }
            }
            isChild = pageLeft();
        } else if (direction == 17) {
            nextLeft = getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
            currLeft = getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
            if (currentFocused == null || nextLeft < currLeft) {
                isChild = nextFocused.requestFocus();
            } else {
                isChild = pageLeft();
            }
        } else if (direction == 66) {
            nextLeft = getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
            currLeft = getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
            if (currentFocused == null || nextLeft > currLeft) {
                isChild = nextFocused.requestFocus();
            } else {
                isChild = pageRight();
            }
        }
        if (isChild) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return isChild;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        }
        outRect.left = child.getLeft();
        outRect.right = child.getRight();
        outRect.top = child.getTop();
        outRect.bottom = child.getBottom();
        ViewGroup parent = child.getParent();
        while ((parent instanceof ViewGroup) && parent != this) {
            ViewGroup group = parent;
            outRect.left += group.getLeft();
            outRect.right += group.getRight();
            outRect.top += group.getTop();
            outRect.bottom += group.getBottom();
            parent = group.getParent();
        }
        return outRect;
    }

    boolean pageLeft() {
        int i = this.mCurItem;
        if (i <= 0) {
            return false;
        }
        setCurrentItem(i - 1, true);
        return true;
    }

    boolean pageRight() {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || this.mCurItem >= pagerAdapter.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == 0) {
                    ItemInfo ii = infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }
        if ((descendantFocusability == 262144 && focusableCount != views.size()) || !isFocusable()) {
            return;
        }
        if (!(((focusableMode & 1) == 1 && isInTouchMode() && !isFocusableInTouchMode()) || views == null)) {
            views.add(this);
        }
    }

    public void addTouchables(ArrayList<View> views) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = getChildCount();
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(event);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
}
