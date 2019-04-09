package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.OverScroller;
import android.widget.ScrollView;
import java.util.List;

public class NestedScrollView extends FrameLayout implements NestedScrollingParent2, NestedScrollingChild2, ScrollingView {
    private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final int[] SCROLLVIEW_STYLEABLE = new int[]{16843130};
    private static final String TAG = "NestedScrollView";
    private int mActivePointerId;
    private final NestedScrollingChildHelper mChildHelper;
    private View mChildToScrollTo;
    private EdgeEffect mEdgeGlowBottom;
    private EdgeEffect mEdgeGlowTop;
    private boolean mFillViewport;
    private boolean mIsBeingDragged;
    private boolean mIsLaidOut;
    private boolean mIsLayoutDirty;
    private int mLastMotionY;
    private long mLastScroll;
    private int mLastScrollerY;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mNestedYOffset;
    private OnScrollChangeListener mOnScrollChangeListener;
    private final NestedScrollingParentHelper mParentHelper;
    private SavedState mSavedState;
    private final int[] mScrollConsumed;
    private final int[] mScrollOffset;
    private OverScroller mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private float mVerticalScrollFactor;

    public interface OnScrollChangeListener {
        void onScrollChange(NestedScrollView nestedScrollView, int i, int i2, int i3, int i4);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C01581();
        public int scrollPosition;

        /* renamed from: android.support.v4.widget.NestedScrollView$SavedState$1 */
        static class C01581 implements Creator<SavedState> {
            C01581() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel source) {
            super(source);
            this.scrollPosition = source.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.scrollPosition);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HorizontalScrollView.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" scrollPosition=");
            stringBuilder.append(this.scrollPosition);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    static class AccessibilityDelegate extends AccessibilityDelegateCompat {
        AccessibilityDelegate() {
        }

        public boolean performAccessibilityAction(View host, int action, Bundle arguments) {
            if (super.performAccessibilityAction(host, action, arguments)) {
                return true;
            }
            NestedScrollView nsvHost = (NestedScrollView) host;
            if (!nsvHost.isEnabled()) {
                return false;
            }
            int targetScrollY;
            if (action == 4096) {
                targetScrollY = Math.min(nsvHost.getScrollY() + ((nsvHost.getHeight() - nsvHost.getPaddingBottom()) - nsvHost.getPaddingTop()), nsvHost.getScrollRange());
                if (targetScrollY == nsvHost.getScrollY()) {
                    return false;
                }
                nsvHost.smoothScrollTo(0, targetScrollY);
                return true;
            } else if (action != 8192) {
                return false;
            } else {
                targetScrollY = Math.max(nsvHost.getScrollY() - ((nsvHost.getHeight() - nsvHost.getPaddingBottom()) - nsvHost.getPaddingTop()), 0);
                if (targetScrollY == nsvHost.getScrollY()) {
                    return false;
                }
                nsvHost.smoothScrollTo(0, targetScrollY);
                return true;
            }
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            NestedScrollView nsvHost = (NestedScrollView) host;
            info.setClassName(ScrollView.class.getName());
            if (nsvHost.isEnabled()) {
                int scrollRange = nsvHost.getScrollRange();
                if (scrollRange > 0) {
                    info.setScrollable(true);
                    if (nsvHost.getScrollY() > 0) {
                        info.addAction(8192);
                    }
                    if (nsvHost.getScrollY() < scrollRange) {
                        info.addAction(4096);
                    }
                }
            }
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            NestedScrollView nsvHost = (NestedScrollView) host;
            event.setClassName(ScrollView.class.getName());
            event.setScrollable(nsvHost.getScrollRange() > 0);
            event.setScrollX(nsvHost.getScrollX());
            event.setScrollY(nsvHost.getScrollY());
            AccessibilityRecordCompat.setMaxScrollX(event, nsvHost.getScrollX());
            AccessibilityRecordCompat.setMaxScrollY(event, nsvHost.getScrollRange());
        }
    }

    public NestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public NestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mTempRect = new Rect();
        this.mIsLayoutDirty = true;
        this.mIsLaidOut = false;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        initScrollView();
        TypedArray a = context.obtainStyledAttributes(attrs, SCROLLVIEW_STYLEABLE, defStyleAttr, 0);
        setFillViewport(a.getBoolean(0, false));
        a.recycle();
        this.mParentHelper = new NestedScrollingParentHelper(this);
        this.mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        ViewCompat.setAccessibilityDelegate(this, ACCESSIBILITY_DELEGATE);
    }

    public boolean startNestedScroll(int axes, int type) {
        return this.mChildHelper.startNestedScroll(axes, type);
    }

    public void stopNestedScroll(int type) {
        this.mChildHelper.stopNestedScroll(type);
    }

    public boolean hasNestedScrollingParent(int type) {
        return this.mChildHelper.hasNestedScrollingParent(type);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        return this.mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        return this.mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        this.mChildHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        return startNestedScroll(axes, 0);
    }

    public void stopNestedScroll() {
        stopNestedScroll(0);
    }

    public boolean hasNestedScrollingParent() {
        return hasNestedScrollingParent(0);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, 0);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, 0);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return this.mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return this.mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & 2) != 0;
    }

    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        this.mParentHelper.onNestedScrollAccepted(child, target, axes, type);
        startNestedScroll(2, type);
    }

    public void onStopNestedScroll(@NonNull View target, int type) {
        this.mParentHelper.onStopNestedScroll(target, type);
        stopNestedScroll(type);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        int i = dyUnconsumed;
        int oldScrollY = getScrollY();
        NestedScrollView nestedScrollView = this;
        scrollBy(0, i);
        int myConsumed = getScrollY() - oldScrollY;
        dispatchNestedScroll(0, myConsumed, 0, i - myConsumed, null, type);
    }

    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        dispatchNestedPreScroll(dx, dy, consumed, null, type);
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return onStartNestedScroll(child, target, nestedScrollAxes, 0);
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        onNestedScrollAccepted(child, target, nestedScrollAxes, 0);
    }

    public void onStopNestedScroll(View target) {
        onStopNestedScroll(target, 0);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, 0);
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        onNestedPreScroll(target, dx, dy, consumed, 0);
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (consumed) {
            return false;
        }
        flingWithNestedDispatch((int) velocityY);
        return true;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getVerticalFadingEdgeLength();
        int scrollY = getScrollY();
        if (scrollY < length) {
            return ((float) scrollY) / ((float) length);
        }
        return 1.0f;
    }

    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        View child = getChildAt(null);
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int length = getVerticalFadingEdgeLength();
        int span = ((child.getBottom() + lp.bottomMargin) - getScrollY()) - (getHeight() - getPaddingBottom());
        if (span < length) {
            return ((float) span) / ((float) length);
        }
        return 1.0f;
    }

    public int getMaxScrollAmount() {
        return (int) (((float) getHeight()) * MAX_SCROLL_FACTOR);
    }

    private void initScrollView() {
        this.mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(262144);
        setWillNotDraw(false);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public void addView(View child) {
        if (getChildCount() <= 0) {
            super.addView(child);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void addView(View child, int index) {
        if (getChildCount() <= 0) {
            super.addView(child, index);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() <= 0) {
            super.addView(child, params);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() <= 0) {
            super.addView(child, index, params);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void setOnScrollChangeListener(@Nullable OnScrollChangeListener l) {
        this.mOnScrollChangeListener = l;
    }

    private boolean canScroll() {
        boolean z = false;
        if (getChildCount() <= 0) {
            return false;
        }
        View child = getChildAt(0);
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if ((child.getHeight() + lp.topMargin) + lp.bottomMargin > (getHeight() - getPaddingTop()) - getPaddingBottom()) {
            z = true;
        }
        return z;
    }

    public boolean isFillViewport() {
        return this.mFillViewport;
    }

    public void setFillViewport(boolean fillViewport) {
        if (fillViewport != this.mFillViewport) {
            this.mFillViewport = fillViewport;
            requestLayout();
        }
    }

    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }

    public void setSmoothScrollingEnabled(boolean smoothScrollingEnabled) {
        this.mSmoothScrollingEnabled = smoothScrollingEnabled;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        OnScrollChangeListener onScrollChangeListener = this.mOnScrollChangeListener;
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mFillViewport && MeasureSpec.getMode(heightMeasureSpec) != 0 && getChildCount() > 0) {
            View child = getChildAt(null);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int parentSpace = (((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) - lp.topMargin) - lp.bottomMargin;
            if (child.getMeasuredHeight() < parentSpace) {
                child.measure(getChildMeasureSpec(widthMeasureSpec, ((getPaddingLeft() + getPaddingRight()) + lp.leftMargin) + lp.rightMargin, lp.width), MeasureSpec.makeMeasureSpec(parentSpace, 1073741824));
            }
        }
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
        this.mTempRect.setEmpty();
        int i = 130;
        if (canScroll()) {
            boolean handled = false;
            if (event.getAction() == 0) {
                int keyCode = event.getKeyCode();
                if (keyCode != 62) {
                    switch (keyCode) {
                        case 19:
                            if (!event.isAltPressed()) {
                                handled = arrowScroll(33);
                                break;
                            }
                            handled = fullScroll(33);
                            break;
                        case 20:
                            if (!event.isAltPressed()) {
                                handled = arrowScroll(130);
                                break;
                            }
                            handled = fullScroll(130);
                            break;
                        default:
                            break;
                    }
                }
                if (event.isShiftPressed()) {
                    i = 33;
                }
                pageScroll(i);
            }
            return handled;
        }
        boolean z = false;
        if (!isFocused() || event.getKeyCode() == 4) {
            return false;
        }
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, 130);
        if (nextFocused != null && nextFocused != this && nextFocused.requestFocus(130)) {
            z = true;
        }
        return z;
    }

    private boolean inChild(int x, int y) {
        boolean z = false;
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollY = getScrollY();
        View child = getChildAt(0);
        if (y >= child.getTop() - scrollY && y < child.getBottom() - scrollY && x >= child.getLeft() && x < child.getRight()) {
            z = true;
        }
        return z;
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        NestedScrollView nestedScrollView = this;
        MotionEvent motionEvent = ev;
        int action = ev.getAction();
        if (action == 2 && nestedScrollView.mIsBeingDragged) {
            return true;
        }
        int i = action & 255;
        if (i != 6) {
            switch (i) {
                case 0:
                    i = (int) ev.getY();
                    if (!inChild((int) ev.getX(), i)) {
                        nestedScrollView.mIsBeingDragged = false;
                        recycleVelocityTracker();
                        break;
                    }
                    nestedScrollView.mLastMotionY = i;
                    nestedScrollView.mActivePointerId = motionEvent.getPointerId(0);
                    initOrResetVelocityTracker();
                    nestedScrollView.mVelocityTracker.addMovement(motionEvent);
                    nestedScrollView.mScroller.computeScrollOffset();
                    nestedScrollView.mIsBeingDragged = true ^ nestedScrollView.mScroller.isFinished();
                    startNestedScroll(2, 0);
                    break;
                case 1:
                case 3:
                    nestedScrollView.mIsBeingDragged = false;
                    nestedScrollView.mActivePointerId = -1;
                    recycleVelocityTracker();
                    if (nestedScrollView.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                    stopNestedScroll(0);
                    break;
                case 2:
                    i = nestedScrollView.mActivePointerId;
                    if (i != -1) {
                        int pointerIndex = motionEvent.findPointerIndex(i);
                        if (pointerIndex != -1) {
                            int y = (int) motionEvent.getY(pointerIndex);
                            if (Math.abs(y - nestedScrollView.mLastMotionY) > nestedScrollView.mTouchSlop && (2 & getNestedScrollAxes()) == 0) {
                                nestedScrollView.mIsBeingDragged = true;
                                nestedScrollView.mLastMotionY = y;
                                initVelocityTrackerIfNotExists();
                                nestedScrollView.mVelocityTracker.addMovement(motionEvent);
                                nestedScrollView.mNestedYOffset = 0;
                                ViewParent parent = getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                                break;
                            }
                        }
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid pointerId=");
                        stringBuilder.append(i);
                        stringBuilder.append(" in onInterceptTouchEvent");
                        Log.e(str, stringBuilder.toString());
                        break;
                    }
                    break;
                default:
                    break;
            }
        }
        onSecondaryPointerUp(ev);
        return nestedScrollView.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean z;
        NestedScrollView nestedScrollView = this;
        MotionEvent motionEvent = ev;
        initVelocityTrackerIfNotExists();
        MotionEvent vtev = MotionEvent.obtain(ev);
        int actionMasked = ev.getActionMasked();
        if (actionMasked == 0) {
            nestedScrollView.mNestedYOffset = 0;
        }
        vtev.offsetLocation(0.0f, (float) nestedScrollView.mNestedYOffset);
        boolean isFinished;
        ViewParent parent;
        int initialVelocity;
        int i;
        switch (actionMasked) {
            case 0:
                z = true;
                if (getChildCount() != 0) {
                    isFinished = nestedScrollView.mScroller.isFinished() ^ true;
                    nestedScrollView.mIsBeingDragged = isFinished;
                    if (isFinished) {
                        parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    if (!nestedScrollView.mScroller.isFinished()) {
                        nestedScrollView.mScroller.abortAnimation();
                    }
                    nestedScrollView.mLastMotionY = (int) ev.getY();
                    nestedScrollView.mActivePointerId = motionEvent.getPointerId(0);
                    startNestedScroll(2, 0);
                    break;
                }
                return false;
            case 1:
                z = true;
                VelocityTracker velocityTracker = nestedScrollView.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) nestedScrollView.mMaximumVelocity);
                initialVelocity = (int) velocityTracker.getYVelocity(nestedScrollView.mActivePointerId);
                if (Math.abs(initialVelocity) > nestedScrollView.mMinimumVelocity) {
                    flingWithNestedDispatch(-initialVelocity);
                } else if (nestedScrollView.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                nestedScrollView.mActivePointerId = -1;
                endDrag();
                break;
            case 2:
                int activePointerIndex = motionEvent.findPointerIndex(nestedScrollView.mActivePointerId);
                if (activePointerIndex != -1) {
                    int y = (int) motionEvent.getY(activePointerIndex);
                    int deltaY = nestedScrollView.mLastMotionY - y;
                    if (dispatchNestedPreScroll(0, deltaY, nestedScrollView.mScrollConsumed, nestedScrollView.mScrollOffset, 0)) {
                        deltaY -= nestedScrollView.mScrollConsumed[1];
                        vtev.offsetLocation(0.0f, (float) nestedScrollView.mScrollOffset[1]);
                        nestedScrollView.mNestedYOffset += nestedScrollView.mScrollOffset[1];
                    }
                    if (!nestedScrollView.mIsBeingDragged && Math.abs(deltaY) > nestedScrollView.mTouchSlop) {
                        parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        nestedScrollView.mIsBeingDragged = true;
                        if (deltaY > 0) {
                            deltaY -= nestedScrollView.mTouchSlop;
                        } else {
                            deltaY += nestedScrollView.mTouchSlop;
                        }
                    }
                    if (!nestedScrollView.mIsBeingDragged) {
                        int i2 = y;
                        initialVelocity = activePointerIndex;
                        z = true;
                        break;
                    }
                    boolean canOverscroll;
                    int range;
                    int deltaY2;
                    int activePointerIndex2;
                    EdgeEffect edgeEffect;
                    int[] iArr;
                    nestedScrollView.mLastMotionY = y - nestedScrollView.mScrollOffset[1];
                    int oldY = getScrollY();
                    int range2 = getScrollRange();
                    int overscrollMode = getOverScrollMode();
                    if (overscrollMode != 0) {
                        if (overscrollMode != 1 || range2 <= 0) {
                            isFinished = false;
                            canOverscroll = isFinished;
                            range = range2;
                            deltaY2 = deltaY;
                            activePointerIndex2 = activePointerIndex;
                            if (overScrollByCompat(0, deltaY, 0, getScrollY(), 0, range, 0, 0, true) && !hasNestedScrollingParent(0)) {
                                nestedScrollView.mVelocityTracker.clear();
                            }
                            y = getScrollY() - oldY;
                            if (dispatchNestedScroll(0, y, 0, deltaY2 - y, nestedScrollView.mScrollOffset, 0)) {
                                z = true;
                                if (!canOverscroll) {
                                    initialVelocity = activePointerIndex2;
                                } else {
                                    ensureGlows();
                                    deltaY = oldY + deltaY2;
                                    if (deltaY >= 0) {
                                        EdgeEffectCompat.onPull(nestedScrollView.mEdgeGlowTop, ((float) deltaY2) / ((float) getHeight()), motionEvent.getX(activePointerIndex2) / ((float) getWidth()));
                                        if (nestedScrollView.mEdgeGlowBottom.isFinished()) {
                                            nestedScrollView.mEdgeGlowBottom.onRelease();
                                            i = range;
                                        } else {
                                            i = range;
                                        }
                                    } else {
                                        initialVelocity = activePointerIndex2;
                                        if (deltaY > range) {
                                            EdgeEffectCompat.onPull(nestedScrollView.mEdgeGlowBottom, ((float) deltaY2) / ((float) getHeight()), 1.0f - (motionEvent.getX(initialVelocity) / ((float) getWidth())));
                                            if (!nestedScrollView.mEdgeGlowTop.isFinished()) {
                                                nestedScrollView.mEdgeGlowTop.onRelease();
                                            }
                                        }
                                    }
                                    edgeEffect = nestedScrollView.mEdgeGlowTop;
                                    if (!(edgeEffect == null || (edgeEffect.isFinished() && nestedScrollView.mEdgeGlowBottom.isFinished()))) {
                                        ViewCompat.postInvalidateOnAnimation(this);
                                    }
                                }
                            } else {
                                i = nestedScrollView.mLastMotionY;
                                iArr = nestedScrollView.mScrollOffset;
                                z = true;
                                nestedScrollView.mLastMotionY = i - iArr[1];
                                vtev.offsetLocation(0.0f, (float) iArr[1]);
                                nestedScrollView.mNestedYOffset += nestedScrollView.mScrollOffset[1];
                                initialVelocity = activePointerIndex2;
                            }
                            break;
                        }
                    }
                    isFinished = true;
                    canOverscroll = isFinished;
                    range = range2;
                    deltaY2 = deltaY;
                    activePointerIndex2 = activePointerIndex;
                    nestedScrollView.mVelocityTracker.clear();
                    y = getScrollY() - oldY;
                    if (dispatchNestedScroll(0, y, 0, deltaY2 - y, nestedScrollView.mScrollOffset, 0)) {
                        z = true;
                        if (!canOverscroll) {
                            ensureGlows();
                            deltaY = oldY + deltaY2;
                            if (deltaY >= 0) {
                                initialVelocity = activePointerIndex2;
                                if (deltaY > range) {
                                    EdgeEffectCompat.onPull(nestedScrollView.mEdgeGlowBottom, ((float) deltaY2) / ((float) getHeight()), 1.0f - (motionEvent.getX(initialVelocity) / ((float) getWidth())));
                                    if (nestedScrollView.mEdgeGlowTop.isFinished()) {
                                        nestedScrollView.mEdgeGlowTop.onRelease();
                                    }
                                }
                            } else {
                                EdgeEffectCompat.onPull(nestedScrollView.mEdgeGlowTop, ((float) deltaY2) / ((float) getHeight()), motionEvent.getX(activePointerIndex2) / ((float) getWidth()));
                                if (nestedScrollView.mEdgeGlowBottom.isFinished()) {
                                    i = range;
                                } else {
                                    nestedScrollView.mEdgeGlowBottom.onRelease();
                                    i = range;
                                }
                            }
                            edgeEffect = nestedScrollView.mEdgeGlowTop;
                            ViewCompat.postInvalidateOnAnimation(this);
                            break;
                        }
                        initialVelocity = activePointerIndex2;
                    } else {
                        i = nestedScrollView.mLastMotionY;
                        iArr = nestedScrollView.mScrollOffset;
                        z = true;
                        nestedScrollView.mLastMotionY = i - iArr[1];
                        vtev.offsetLocation(0.0f, (float) iArr[1]);
                        nestedScrollView.mNestedYOffset += nestedScrollView.mScrollOffset[1];
                        initialVelocity = activePointerIndex2;
                    }
                    break;
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid pointerId=");
                stringBuilder.append(nestedScrollView.mActivePointerId);
                stringBuilder.append(" in onTouchEvent");
                Log.e(str, stringBuilder.toString());
                z = true;
                break;
                break;
            case 3:
                if (nestedScrollView.mIsBeingDragged && getChildCount() > 0 && nestedScrollView.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                nestedScrollView.mActivePointerId = -1;
                endDrag();
                z = true;
                break;
            case 5:
                i = ev.getActionIndex();
                nestedScrollView.mLastMotionY = (int) motionEvent.getY(i);
                nestedScrollView.mActivePointerId = motionEvent.getPointerId(i);
                z = true;
                break;
            case 6:
                onSecondaryPointerUp(ev);
                nestedScrollView.mLastMotionY = (int) motionEvent.getY(motionEvent.findPointerIndex(nestedScrollView.mActivePointerId));
                z = true;
                break;
            default:
                z = true;
                break;
        }
        VelocityTracker velocityTracker2 = nestedScrollView.mVelocityTracker;
        if (velocityTracker2 != null) {
            velocityTracker2.addMovement(vtev);
        }
        vtev.recycle();
        return z;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionY = (int) ev.getY(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & 2) != 0) {
            if (event.getAction() == 8) {
                if (!this.mIsBeingDragged) {
                    float vscroll = event.getAxisValue(1.3E-44f);
                    if (vscroll != 0.0f) {
                        int delta = (int) (getVerticalScrollFactorCompat() * vscroll);
                        int range = getScrollRange();
                        int oldScrollY = getScrollY();
                        int newScrollY = oldScrollY - delta;
                        if (newScrollY < 0) {
                            newScrollY = 0;
                        } else if (newScrollY > range) {
                            newScrollY = range;
                        }
                        if (newScrollY != oldScrollY) {
                            super.scrollTo(getScrollX(), newScrollY);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private float getVerticalScrollFactorCompat() {
        if (this.mVerticalScrollFactor == 0.0f) {
            TypedValue outValue = new TypedValue();
            Context context = getContext();
            if (context.getTheme().resolveAttribute(16842829, outValue, true)) {
                this.mVerticalScrollFactor = outValue.getDimension(context.getResources().getDisplayMetrics());
            } else {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
        }
        return this.mVerticalScrollFactor;
    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, scrollY);
    }

    boolean overScrollByCompat(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        boolean overScrollHorizontal;
        boolean overScrollVertical;
        int newScrollX;
        int maxOverScrollX2;
        int newScrollY;
        int maxOverScrollY2;
        int left;
        int right;
        int top;
        int bottom;
        boolean clampedX;
        boolean clampedY;
        NestedScrollView nestedScrollView = this;
        int overScrollMode = getOverScrollMode();
        boolean canScrollHorizontal = computeHorizontalScrollRange() > computeHorizontalScrollExtent();
        boolean canScrollVertical = computeVerticalScrollRange() > computeVerticalScrollExtent();
        if (overScrollMode != 0) {
            if (overScrollMode != 1 || !canScrollHorizontal) {
                overScrollHorizontal = false;
                if (overScrollMode != 0) {
                    if (overScrollMode == 1 || !canScrollVertical) {
                        overScrollVertical = false;
                        newScrollX = scrollX + deltaX;
                        if (overScrollHorizontal) {
                            maxOverScrollX2 = 0;
                        } else {
                            maxOverScrollX2 = maxOverScrollX;
                        }
                        newScrollY = scrollY + deltaY;
                        if (overScrollVertical) {
                            maxOverScrollY2 = 0;
                        } else {
                            maxOverScrollY2 = maxOverScrollY;
                        }
                        left = -maxOverScrollX2;
                        right = maxOverScrollX2 + scrollRangeX;
                        top = -maxOverScrollY2;
                        bottom = maxOverScrollY2 + scrollRangeY;
                        if (newScrollX <= right) {
                            newScrollX = right;
                            clampedX = true;
                        } else if (newScrollX >= left) {
                            newScrollX = left;
                            clampedX = true;
                        } else {
                            clampedX = false;
                        }
                        if (newScrollY <= bottom) {
                            maxOverScrollX = bottom;
                            clampedY = true;
                        } else if (newScrollY >= top) {
                            maxOverScrollX = top;
                            clampedY = true;
                        } else {
                            maxOverScrollX = newScrollY;
                            clampedY = false;
                        }
                        if (clampedY && !hasNestedScrollingParent(1)) {
                            nestedScrollView.mScroller.springBack(newScrollX, maxOverScrollX, 0, 0, 0, getScrollRange());
                        }
                        onOverScrolled(newScrollX, maxOverScrollX, clampedX, clampedY);
                        if (!clampedX) {
                            if (!clampedY) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                overScrollVertical = true;
                newScrollX = scrollX + deltaX;
                if (overScrollHorizontal) {
                    maxOverScrollX2 = maxOverScrollX;
                } else {
                    maxOverScrollX2 = 0;
                }
                newScrollY = scrollY + deltaY;
                if (overScrollVertical) {
                    maxOverScrollY2 = maxOverScrollY;
                } else {
                    maxOverScrollY2 = 0;
                }
                left = -maxOverScrollX2;
                right = maxOverScrollX2 + scrollRangeX;
                top = -maxOverScrollY2;
                bottom = maxOverScrollY2 + scrollRangeY;
                if (newScrollX <= right) {
                    newScrollX = right;
                    clampedX = true;
                } else if (newScrollX >= left) {
                    clampedX = false;
                } else {
                    newScrollX = left;
                    clampedX = true;
                }
                if (newScrollY <= bottom) {
                    maxOverScrollX = bottom;
                    clampedY = true;
                } else if (newScrollY >= top) {
                    maxOverScrollX = newScrollY;
                    clampedY = false;
                } else {
                    maxOverScrollX = top;
                    clampedY = true;
                }
                nestedScrollView.mScroller.springBack(newScrollX, maxOverScrollX, 0, 0, 0, getScrollRange());
                onOverScrolled(newScrollX, maxOverScrollX, clampedX, clampedY);
                if (clampedX) {
                    if (!clampedY) {
                        return false;
                    }
                }
                return true;
            }
        }
        overScrollHorizontal = true;
        if (overScrollMode != 0) {
            if (overScrollMode == 1) {
            }
            overScrollVertical = false;
            newScrollX = scrollX + deltaX;
            if (overScrollHorizontal) {
                maxOverScrollX2 = 0;
            } else {
                maxOverScrollX2 = maxOverScrollX;
            }
            newScrollY = scrollY + deltaY;
            if (overScrollVertical) {
                maxOverScrollY2 = 0;
            } else {
                maxOverScrollY2 = maxOverScrollY;
            }
            left = -maxOverScrollX2;
            right = maxOverScrollX2 + scrollRangeX;
            top = -maxOverScrollY2;
            bottom = maxOverScrollY2 + scrollRangeY;
            if (newScrollX <= right) {
                newScrollX = right;
                clampedX = true;
            } else if (newScrollX >= left) {
                newScrollX = left;
                clampedX = true;
            } else {
                clampedX = false;
            }
            if (newScrollY <= bottom) {
                maxOverScrollX = bottom;
                clampedY = true;
            } else if (newScrollY >= top) {
                maxOverScrollX = top;
                clampedY = true;
            } else {
                maxOverScrollX = newScrollY;
                clampedY = false;
            }
            nestedScrollView.mScroller.springBack(newScrollX, maxOverScrollX, 0, 0, 0, getScrollRange());
            onOverScrolled(newScrollX, maxOverScrollX, clampedX, clampedY);
            if (clampedX) {
                if (!clampedY) {
                    return false;
                }
            }
            return true;
        }
        overScrollVertical = true;
        newScrollX = scrollX + deltaX;
        if (overScrollHorizontal) {
            maxOverScrollX2 = maxOverScrollX;
        } else {
            maxOverScrollX2 = 0;
        }
        newScrollY = scrollY + deltaY;
        if (overScrollVertical) {
            maxOverScrollY2 = maxOverScrollY;
        } else {
            maxOverScrollY2 = 0;
        }
        left = -maxOverScrollX2;
        right = maxOverScrollX2 + scrollRangeX;
        top = -maxOverScrollY2;
        bottom = maxOverScrollY2 + scrollRangeY;
        if (newScrollX <= right) {
            newScrollX = right;
            clampedX = true;
        } else if (newScrollX >= left) {
            clampedX = false;
        } else {
            newScrollX = left;
            clampedX = true;
        }
        if (newScrollY <= bottom) {
            maxOverScrollX = bottom;
            clampedY = true;
        } else if (newScrollY >= top) {
            maxOverScrollX = newScrollY;
            clampedY = false;
        } else {
            maxOverScrollX = top;
            clampedY = true;
        }
        nestedScrollView.mScroller.springBack(newScrollX, maxOverScrollX, 0, 0, 0, getScrollRange());
        onOverScrolled(newScrollX, maxOverScrollX, clampedX, clampedY);
        if (clampedX) {
            if (!clampedY) {
                return false;
            }
        }
        return true;
    }

    int getScrollRange() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View child = getChildAt(0);
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return Math.max(0, ((child.getHeight() + lp.topMargin) + lp.bottomMargin) - ((getHeight() - getPaddingTop()) - getPaddingBottom()));
    }

    private View findFocusableViewInBounds(boolean topFocus, int top, int bottom) {
        List<View> focusables = getFocusables(2);
        View focusCandidate = null;
        boolean foundFullyContainedFocusable = false;
        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = (View) focusables.get(i);
            int viewTop = view.getTop();
            int viewBottom = view.getBottom();
            if (top < viewBottom && viewTop < bottom) {
                boolean viewIsCloserToBoundary = false;
                boolean viewIsFullyContained = top < viewTop && viewBottom < bottom;
                if (focusCandidate == null) {
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    if ((topFocus && viewTop < focusCandidate.getTop()) || (!topFocus && viewBottom > focusCandidate.getBottom())) {
                        viewIsCloserToBoundary = true;
                    }
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            focusCandidate = view;
                        }
                    } else if (viewIsFullyContained) {
                        focusCandidate = view;
                        foundFullyContainedFocusable = true;
                    } else if (viewIsCloserToBoundary) {
                        focusCandidate = view;
                    }
                }
            }
        }
        return focusCandidate;
    }

    public boolean pageScroll(int direction) {
        boolean down = direction == 130;
        int height = getHeight();
        if (down) {
            this.mTempRect.top = getScrollY() + height;
            int count = getChildCount();
            if (count > 0) {
                View view = getChildAt(count - 1);
                int bottom = (view.getBottom() + ((LayoutParams) view.getLayoutParams()).bottomMargin) + getPaddingBottom();
                if (this.mTempRect.top + height > bottom) {
                    this.mTempRect.top = bottom - height;
                }
            }
        } else {
            this.mTempRect.top = getScrollY() - height;
            if (this.mTempRect.top < 0) {
                this.mTempRect.top = 0;
            }
        }
        Rect rect = this.mTempRect;
        rect.bottom = rect.top + height;
        return scrollAndFocus(direction, this.mTempRect.top, this.mTempRect.bottom);
    }

    public boolean fullScroll(int direction) {
        boolean down = direction == 130;
        int height = getHeight();
        Rect rect = this.mTempRect;
        rect.top = 0;
        rect.bottom = height;
        if (down) {
            int count = getChildCount();
            if (count > 0) {
                View view = getChildAt(count - 1);
                this.mTempRect.bottom = (view.getBottom() + ((LayoutParams) view.getLayoutParams()).bottomMargin) + getPaddingBottom();
                Rect rect2 = this.mTempRect;
                rect2.top = rect2.bottom - height;
            }
        }
        return scrollAndFocus(direction, this.mTempRect.top, this.mTempRect.bottom);
    }

    private boolean scrollAndFocus(int direction, int top, int bottom) {
        boolean handled = true;
        int height = getHeight();
        int containerTop = getScrollY();
        int containerBottom = containerTop + height;
        boolean up = direction == 33;
        View newFocused = findFocusableViewInBounds(up, top, bottom);
        if (newFocused == null) {
            newFocused = this;
        }
        if (top < containerTop || bottom > containerBottom) {
            doScrollY(up ? top - containerTop : bottom - containerBottom);
        } else {
            handled = false;
        }
        if (newFocused != findFocus()) {
            newFocused.requestFocus(direction);
        }
        return handled;
    }

    public boolean arrowScroll(int direction) {
        int scrollDelta;
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        int maxJump = getMaxScrollAmount();
        if (nextFocused == null || !isWithinDeltaOfScreen(nextFocused, maxJump, getHeight())) {
            scrollDelta = maxJump;
            if (direction == 33 && getScrollY() < scrollDelta) {
                scrollDelta = getScrollY();
            } else if (direction == 130 && getChildCount() > 0) {
                View child = getChildAt(0);
                scrollDelta = Math.min((child.getBottom() + ((LayoutParams) child.getLayoutParams()).bottomMargin) - ((getScrollY() + getHeight()) - getPaddingBottom()), maxJump);
            }
            if (scrollDelta == 0) {
                return false;
            }
            doScrollY(direction == 130 ? scrollDelta : -scrollDelta);
        } else {
            nextFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
            doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            nextFocused.requestFocus(direction);
        }
        if (currentFocused != null && currentFocused.isFocused() && isOffScreen(currentFocused)) {
            scrollDelta = getDescendantFocusability();
            setDescendantFocusability(131072);
            requestFocus();
            setDescendantFocusability(scrollDelta);
        }
        return true;
    }

    private boolean isOffScreen(View descendant) {
        return isWithinDeltaOfScreen(descendant, 0, getHeight()) ^ 1;
    }

    private boolean isWithinDeltaOfScreen(View descendant, int delta, int height) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        return this.mTempRect.bottom + delta >= getScrollY() && this.mTempRect.top - delta <= getScrollY() + height;
    }

    private void doScrollY(int delta) {
        if (delta == 0) {
            return;
        }
        if (this.mSmoothScrollingEnabled) {
            smoothScrollBy(0, delta);
        } else {
            scrollBy(0, delta);
        }
    }

    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() != 0) {
            if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
                View child = getChildAt(0);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childSize = (child.getHeight() + lp.topMargin) + lp.bottomMargin;
                int parentSpace = (getHeight() - getPaddingTop()) - getPaddingBottom();
                int scrollY = getScrollY();
                dy = Math.max(0, Math.min(scrollY + dy, Math.max(0, childSize - parentSpace))) - scrollY;
                this.mLastScrollerY = getScrollY();
                this.mScroller.startScroll(getScrollX(), scrollY, 0, dy);
                ViewCompat.postInvalidateOnAnimation(this);
            } else {
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                scrollBy(dx, dy);
            }
            this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public final void smoothScrollTo(int x, int y) {
        smoothScrollBy(x - getScrollX(), y - getScrollY());
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int computeVerticalScrollRange() {
        int parentSpace = (getHeight() - getPaddingBottom()) - getPaddingTop();
        if (getChildCount() == 0) {
            return parentSpace;
        }
        View child = getChildAt(0);
        int scrollRange = child.getBottom() + ((LayoutParams) child.getLayoutParams()).bottomMargin;
        int scrollY = getScrollY();
        int overscrollBottom = Math.max(0, scrollRange - parentSpace);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }
        return scrollRange;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        child.measure(getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), child.getLayoutParams().width), MeasureSpec.makeMeasureSpec(0, 0));
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        child.measure(getChildMeasureSpec(parentWidthMeasureSpec, (((getPaddingLeft() + getPaddingRight()) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width), MeasureSpec.makeMeasureSpec(lp.topMargin + lp.bottomMargin, 0));
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int dy;
            int x = r10.mScroller.getCurrX();
            int y = r10.mScroller.getCurrY();
            int dy2 = y - r10.mLastScrollerY;
            if (dispatchNestedPreScroll(0, dy2, r10.mScrollConsumed, null, 1)) {
                dy = dy2 - r10.mScrollConsumed[1];
            } else {
                dy = dy2;
            }
            if (dy != 0) {
                int range = getScrollRange();
                int oldScrollY = getScrollY();
                int oldScrollY2 = oldScrollY;
                int range2 = range;
                overScrollByCompat(0, dy, getScrollX(), oldScrollY, 0, range, 0, 0, false);
                int scrolledDeltaY = getScrollY() - oldScrollY2;
                if (dispatchNestedScroll(0, scrolledDeltaY, 0, dy - scrolledDeltaY, null, 1)) {
                } else {
                    boolean z;
                    int mode = getOverScrollMode();
                    int range3;
                    if (mode != 0) {
                        if (mode == 1) {
                            range3 = range2;
                            if (range3 > 0) {
                            }
                        } else {
                            range3 = range2;
                        }
                        z = false;
                        if (z) {
                            ensureGlows();
                            if (y > 0 && oldScrollY2 > 0) {
                                r10.mEdgeGlowTop.onAbsorb((int) r10.mScroller.getCurrVelocity());
                            } else if (y >= range3 && oldScrollY2 < range3) {
                                r10.mEdgeGlowBottom.onAbsorb((int) r10.mScroller.getCurrVelocity());
                            }
                        }
                    } else {
                        range3 = range2;
                    }
                    z = true;
                    if (z) {
                        ensureGlows();
                        if (y > 0) {
                        }
                        r10.mEdgeGlowBottom.onAbsorb((int) r10.mScroller.getCurrVelocity());
                    }
                }
            }
            r10.mLastScrollerY = y;
            ViewCompat.postInvalidateOnAnimation(this);
            return;
        }
        if (hasNestedScrollingParent(1)) {
            stopNestedScroll(1);
        }
        r10.mLastScrollerY = 0;
    }

    private void scrollToChild(View child) {
        child.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(child, this.mTempRect);
        int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (scrollDelta != 0) {
            scrollBy(0, scrollDelta);
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        int delta = computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean scroll = delta != 0;
        if (scroll) {
            if (immediate) {
                scrollBy(0, delta);
            } else {
                smoothScrollBy(0, delta);
            }
        }
        return scroll;
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        int actualScreenBottom = screenBottom;
        int fadingEdge = getVerticalFadingEdgeLength();
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }
        View child = getChildAt(0);
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (rect.bottom < (child.getHeight() + lp.topMargin) + lp.bottomMargin) {
            screenBottom -= fadingEdge;
        }
        int scrollYDelta = 0;
        if (rect.bottom > screenBottom && rect.top > screenTop) {
            if (rect.height() > height) {
                scrollYDelta = 0 + (rect.top - screenTop);
            } else {
                scrollYDelta = 0 + (rect.bottom - screenBottom);
            }
            scrollYDelta = Math.min(scrollYDelta, (child.getBottom() + lp.bottomMargin) - actualScreenBottom);
        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            if (rect.height() > height) {
                scrollYDelta = 0 - (screenBottom - rect.bottom);
            } else {
                scrollYDelta = 0 - (screenTop - rect.top);
            }
            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
            return scrollYDelta;
        }
        return scrollYDelta;
    }

    public void requestChildFocus(View child, View focused) {
        if (this.mIsLayoutDirty) {
            this.mChildToScrollTo = focused;
        } else {
            scrollToChild(focused);
        }
        super.requestChildFocus(child, focused);
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        View nextFocus;
        if (direction == 2) {
            direction = 130;
        } else if (direction == 1) {
            direction = 33;
        }
        if (previouslyFocusedRect == null) {
            nextFocus = FocusFinder.getInstance().findNextFocus(this, null, direction);
        } else {
            nextFocus = FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
        }
        if (nextFocus == null || isOffScreen(nextFocus)) {
            return false;
        }
        return nextFocus.requestFocus(direction, previouslyFocusedRect);
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        return scrollToChildRect(rectangle, immediate);
    }

    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mIsLayoutDirty = false;
        View view = this.mChildToScrollTo;
        if (view != null && isViewDescendantOf(view, this)) {
            scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!this.mIsLaidOut) {
            if (this.mSavedState != null) {
                scrollTo(getScrollX(), this.mSavedState.scrollPosition);
                this.mSavedState = null;
            }
            int childSize = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                childSize = (child.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin;
            }
            int parentSpace = ((b - t) - getPaddingTop()) - getPaddingBottom();
            int currentScrollY = getScrollY();
            int newScrollY = clamp(currentScrollY, parentSpace, childSize);
            if (newScrollY != currentScrollY) {
                scrollTo(getScrollX(), newScrollY);
            }
        }
        scrollTo(getScrollX(), getScrollY());
        this.mIsLaidOut = true;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsLaidOut = false;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View currentFocused = findFocus();
        if (currentFocused != null) {
            if (this != currentFocused) {
                if (isWithinDeltaOfScreen(currentFocused, 0, oldh)) {
                    currentFocused.getDrawingRect(this.mTempRect);
                    offsetDescendantRectToMyCoords(currentFocused, this.mTempRect);
                    doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
                }
            }
        }
    }

    private static boolean isViewDescendantOf(View child, View parent) {
        boolean z = true;
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        if (!(theParent instanceof ViewGroup) || !isViewDescendantOf((View) theParent, parent)) {
            z = false;
        }
        return z;
    }

    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            startNestedScroll(2, 1);
            this.mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            this.mLastScrollerY = getScrollY();
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void flingWithNestedDispatch(int velocityY) {
        boolean canFling;
        int scrollY = getScrollY();
        if (scrollY > 0 || velocityY > 0) {
            if (scrollY >= getScrollRange()) {
                if (velocityY < 0) {
                }
            }
            canFling = true;
            if (!dispatchNestedPreFling(0.0f, (float) velocityY)) {
                dispatchNestedFling(0.0f, (float) velocityY, canFling);
                fling(velocityY);
            }
        }
        canFling = false;
        if (!dispatchNestedPreFling(0.0f, (float) velocityY)) {
            dispatchNestedFling(0.0f, (float) velocityY, canFling);
            fling(velocityY);
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        recycleVelocityTracker();
        stopNestedScroll(0);
        EdgeEffect edgeEffect = this.mEdgeGlowTop;
        if (edgeEffect != null) {
            edgeEffect.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
    }

    public void scrollTo(int x, int y) {
        if (getChildCount() > 0) {
            View child = getChildAt(null);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int parentSpaceVertical = (getHeight() - getPaddingTop()) - getPaddingBottom();
            int childSizeVertical = (child.getHeight() + lp.topMargin) + lp.bottomMargin;
            x = clamp(x, (getWidth() - getPaddingLeft()) - getPaddingRight(), (child.getWidth() + lp.leftMargin) + lp.rightMargin);
            y = clamp(y, parentSpaceVertical, childSizeVertical);
            if (x != getScrollX() || y != getScrollY()) {
                super.scrollTo(x, y);
            }
        }
    }

    private void ensureGlows() {
        if (getOverScrollMode() == 2) {
            this.mEdgeGlowTop = null;
            this.mEdgeGlowBottom = null;
        } else if (this.mEdgeGlowTop == null) {
            Context context = getContext();
            this.mEdgeGlowTop = new EdgeEffect(context);
            this.mEdgeGlowBottom = new EdgeEffect(context);
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mEdgeGlowTop != null) {
            int restoreCount;
            int width;
            int height;
            int xTranslation;
            int yTranslation;
            int scrollY = getScrollY();
            if (!this.mEdgeGlowTop.isFinished()) {
                restoreCount = canvas.save();
                width = getWidth();
                height = getHeight();
                xTranslation = 0;
                yTranslation = Math.min(0, scrollY);
                if (VERSION.SDK_INT < 21 || getClipToPadding()) {
                    width -= getPaddingLeft() + getPaddingRight();
                    xTranslation = 0 + getPaddingLeft();
                }
                if (VERSION.SDK_INT >= 21 && getClipToPadding()) {
                    height -= getPaddingTop() + getPaddingBottom();
                    yTranslation += getPaddingTop();
                }
                canvas.translate((float) xTranslation, (float) yTranslation);
                this.mEdgeGlowTop.setSize(width, height);
                if (this.mEdgeGlowTop.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                restoreCount = canvas.save();
                width = getWidth();
                height = getHeight();
                xTranslation = 0;
                yTranslation = Math.max(getScrollRange(), scrollY) + height;
                if (VERSION.SDK_INT < 21 || getClipToPadding()) {
                    width -= getPaddingLeft() + getPaddingRight();
                    xTranslation = 0 + getPaddingLeft();
                }
                if (VERSION.SDK_INT >= 21 && getClipToPadding()) {
                    height -= getPaddingTop() + getPaddingBottom();
                    yTranslation -= getPaddingBottom();
                }
                canvas.translate((float) (xTranslation - width), (float) yTranslation);
                canvas.rotate(180.0f, (float) width, 0.0f);
                this.mEdgeGlowBottom.setSize(width, height);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    private static int clamp(int n, int my, int child) {
        if (my < child) {
            if (n >= 0) {
                if (my + n > child) {
                    return child - my;
                }
                return n;
            }
        }
        return 0;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            this.mSavedState = ss;
            requestLayout();
            return;
        }
        super.onRestoreInstanceState(state);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.scrollPosition = getScrollY();
        return ss;
    }
}
