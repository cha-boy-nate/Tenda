package android.support.v4.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewParent;

public class NestedScrollingChildHelper {
    private boolean mIsNestedScrollingEnabled;
    private ViewParent mNestedScrollingParentNonTouch;
    private ViewParent mNestedScrollingParentTouch;
    private int[] mTempNestedScrollConsumed;
    private final View mView;

    public NestedScrollingChildHelper(@NonNull View view) {
        this.mView = view;
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        if (this.mIsNestedScrollingEnabled) {
            ViewCompat.stopNestedScroll(this.mView);
        }
        this.mIsNestedScrollingEnabled = enabled;
    }

    public boolean isNestedScrollingEnabled() {
        return this.mIsNestedScrollingEnabled;
    }

    public boolean hasNestedScrollingParent() {
        return hasNestedScrollingParent(0);
    }

    public boolean hasNestedScrollingParent(int type) {
        return getNestedScrollingParentForType(type) != null;
    }

    public boolean startNestedScroll(int axes) {
        return startNestedScroll(axes, 0);
    }

    public boolean startNestedScroll(int axes, int type) {
        if (hasNestedScrollingParent(type)) {
            return true;
        }
        if (isNestedScrollingEnabled()) {
            View child = this.mView;
            for (ViewParent p = this.mView.getParent(); p != null; p = p.getParent()) {
                if (ViewParentCompat.onStartNestedScroll(p, child, this.mView, axes, type)) {
                    setNestedScrollingParentForType(type, p);
                    ViewParentCompat.onNestedScrollAccepted(p, child, this.mView, axes, type);
                    return true;
                }
                if (p instanceof View) {
                    child = (View) p;
                }
            }
        }
        return false;
    }

    public void stopNestedScroll() {
        stopNestedScroll(0);
    }

    public void stopNestedScroll(int type) {
        ViewParent parent = getNestedScrollingParentForType(type);
        if (parent != null) {
            ViewParentCompat.onStopNestedScroll(parent, this.mView, type);
            setNestedScrollingParentForType(type, null);
        }
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, 0);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        NestedScrollingChildHelper nestedScrollingChildHelper = this;
        int[] iArr = offsetInWindow;
        if (isNestedScrollingEnabled()) {
            ViewParent parent = getNestedScrollingParentForType(type);
            if (parent == null) {
                return false;
            }
            int startX;
            int startY;
            if (dxConsumed == 0 && dyConsumed == 0 && dxUnconsumed == 0) {
                if (dyUnconsumed == 0) {
                    if (iArr != null) {
                        iArr[0] = 0;
                        iArr[1] = 0;
                    }
                }
            }
            if (iArr != null) {
                nestedScrollingChildHelper.mView.getLocationInWindow(iArr);
                startX = iArr[0];
                startY = iArr[1];
            } else {
                startX = 0;
                startY = 0;
            }
            ViewParentCompat.onNestedScroll(parent, nestedScrollingChildHelper.mView, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
            if (iArr != null) {
                nestedScrollingChildHelper.mView.getLocationInWindow(iArr);
                iArr[0] = iArr[0] - startX;
                iArr[1] = iArr[1] - startY;
            }
            return true;
        }
        int i = type;
        return false;
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        return dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, 0);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        NestedScrollingChildHelper nestedScrollingChildHelper = this;
        int[] iArr = offsetInWindow;
        if (isNestedScrollingEnabled()) {
            ViewParent parent = getNestedScrollingParentForType(type);
            if (parent == null) {
                return false;
            }
            int startX;
            int startY;
            int[] consumed2;
            boolean z = true;
            if (dx == 0) {
                if (dy == 0) {
                    if (iArr != null) {
                        iArr[0] = 0;
                        iArr[1] = 0;
                    }
                }
            }
            if (iArr != null) {
                nestedScrollingChildHelper.mView.getLocationInWindow(iArr);
                startX = iArr[0];
                startY = iArr[1];
            } else {
                startX = 0;
                startY = 0;
            }
            if (consumed == null) {
                if (nestedScrollingChildHelper.mTempNestedScrollConsumed == null) {
                    nestedScrollingChildHelper.mTempNestedScrollConsumed = new int[2];
                }
                consumed2 = nestedScrollingChildHelper.mTempNestedScrollConsumed;
            } else {
                consumed2 = consumed;
            }
            consumed2[0] = 0;
            consumed2[1] = 0;
            ViewParentCompat.onNestedPreScroll(parent, nestedScrollingChildHelper.mView, dx, dy, consumed2, type);
            if (iArr != null) {
                nestedScrollingChildHelper.mView.getLocationInWindow(iArr);
                iArr[0] = iArr[0] - startX;
                iArr[1] = iArr[1] - startY;
            }
            if (consumed2[0] == 0) {
                if (consumed2[1] == 0) {
                    z = false;
                }
            }
            return z;
        }
        int i = type;
        return false;
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        if (isNestedScrollingEnabled()) {
            ViewParent parent = getNestedScrollingParentForType(0);
            if (parent != null) {
                return ViewParentCompat.onNestedFling(parent, this.mView, velocityX, velocityY, consumed);
            }
        }
        return false;
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        if (isNestedScrollingEnabled()) {
            ViewParent parent = getNestedScrollingParentForType(0);
            if (parent != null) {
                return ViewParentCompat.onNestedPreFling(parent, this.mView, velocityX, velocityY);
            }
        }
        return false;
    }

    public void onDetachedFromWindow() {
        ViewCompat.stopNestedScroll(this.mView);
    }

    public void onStopNestedScroll(@NonNull View child) {
        ViewCompat.stopNestedScroll(this.mView);
    }

    private ViewParent getNestedScrollingParentForType(int type) {
        switch (type) {
            case 0:
                return this.mNestedScrollingParentTouch;
            case 1:
                return this.mNestedScrollingParentNonTouch;
            default:
                return null;
        }
    }

    private void setNestedScrollingParentForType(int type, ViewParent p) {
        switch (type) {
            case 0:
                this.mNestedScrollingParentTouch = p;
                return;
            case 1:
                this.mNestedScrollingParentNonTouch = p;
                return;
            default:
                return;
        }
    }
}
