package android.support.v4.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.FocusStrategy.BoundsAdapter;
import android.support.v4.widget.FocusStrategy.CollectionAdapter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.ArrayList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
    private static final String DEFAULT_CLASS_NAME = "android.view.View";
    public static final int HOST_ID = -1;
    public static final int INVALID_ID = Integer.MIN_VALUE;
    private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private static final BoundsAdapter<AccessibilityNodeInfoCompat> NODE_ADAPTER = new C02691();
    private static final CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> SPARSE_VALUES_ADAPTER = new C02702();
    int mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
    private final View mHost;
    private int mHoveredVirtualViewId = Integer.MIN_VALUE;
    int mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
    private final AccessibilityManager mManager;
    private MyNodeProvider mNodeProvider;
    private final int[] mTempGlobalRect = new int[2];
    private final Rect mTempParentRect = new Rect();
    private final Rect mTempScreenRect = new Rect();
    private final Rect mTempVisibleRect = new Rect();

    /* renamed from: android.support.v4.widget.ExploreByTouchHelper$1 */
    static class C02691 implements BoundsAdapter<AccessibilityNodeInfoCompat> {
        C02691() {
        }

        public void obtainBounds(AccessibilityNodeInfoCompat node, Rect outBounds) {
            node.getBoundsInParent(outBounds);
        }
    }

    /* renamed from: android.support.v4.widget.ExploreByTouchHelper$2 */
    static class C02702 implements CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> {
        C02702() {
        }

        public AccessibilityNodeInfoCompat get(SparseArrayCompat<AccessibilityNodeInfoCompat> collection, int index) {
            return (AccessibilityNodeInfoCompat) collection.valueAt(index);
        }

        public int size(SparseArrayCompat<AccessibilityNodeInfoCompat> collection) {
            return collection.size();
        }
    }

    private class MyNodeProvider extends AccessibilityNodeProviderCompat {
        MyNodeProvider() {
        }

        public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int virtualViewId) {
            return AccessibilityNodeInfoCompat.obtain(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(virtualViewId));
        }

        public boolean performAction(int virtualViewId, int action, Bundle arguments) {
            return ExploreByTouchHelper.this.performAction(virtualViewId, action, arguments);
        }

        public AccessibilityNodeInfoCompat findFocus(int focusType) {
            int focusedId = focusType == 2 ? ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId : ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId;
            if (focusedId == Integer.MIN_VALUE) {
                return null;
            }
            return createAccessibilityNodeInfo(focusedId);
        }
    }

    @android.support.annotation.NonNull
    private android.support.v4.view.accessibility.AccessibilityNodeInfoCompat createNodeForChild(int r12) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:49:0x0154 in {4, 6, 15, 16, 19, 20, 22, 25, 32, 33, 34, 41, 42, 44, 46, 48} preds:[]
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
        r11 = this;
        r0 = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.obtain();
        r1 = 1;
        r0.setEnabled(r1);
        r0.setFocusable(r1);
        r2 = "android.view.View";
        r0.setClassName(r2);
        r2 = INVALID_PARENT_BOUNDS;
        r0.setBoundsInParent(r2);
        r2 = INVALID_PARENT_BOUNDS;
        r0.setBoundsInScreen(r2);
        r2 = r11.mHost;
        r0.setParent(r2);
        r11.onPopulateNodeForVirtualView(r12, r0);
        r2 = r0.getText();
        if (r2 != 0) goto L_0x0037;
    L_0x0028:
        r2 = r0.getContentDescription();
        if (r2 == 0) goto L_0x002f;
    L_0x002e:
        goto L_0x0037;
    L_0x002f:
        r1 = new java.lang.RuntimeException;
        r2 = "Callbacks must add text or a content description in populateNodeForVirtualViewId()";
        r1.<init>(r2);
        throw r1;
    L_0x0037:
        r2 = r11.mTempParentRect;
        r0.getBoundsInParent(r2);
        r2 = r11.mTempParentRect;
        r3 = INVALID_PARENT_BOUNDS;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x014c;
    L_0x0046:
        r2 = r0.getActions();
        r3 = r2 & 64;
        if (r3 != 0) goto L_0x0144;
    L_0x004e:
        r3 = r2 & 128;
        if (r3 != 0) goto L_0x013c;
    L_0x0052:
        r3 = r11.mHost;
        r3 = r3.getContext();
        r3 = r3.getPackageName();
        r0.setPackageName(r3);
        r3 = r11.mHost;
        r0.setSource(r3, r12);
        r3 = r11.mAccessibilityFocusedVirtualViewId;
        r4 = 0;
        if (r3 != r12) goto L_0x0072;
    L_0x0069:
        r0.setAccessibilityFocused(r1);
        r3 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0.addAction(r3);
        goto L_0x007a;
    L_0x0072:
        r0.setAccessibilityFocused(r4);
        r3 = 64;
        r0.addAction(r3);
    L_0x007a:
        r3 = r11.mKeyboardFocusedVirtualViewId;
        if (r3 != r12) goto L_0x0080;
    L_0x007e:
        r3 = 1;
        goto L_0x0081;
    L_0x0080:
        r3 = 0;
    L_0x0081:
        if (r3 == 0) goto L_0x0088;
    L_0x0083:
        r5 = 2;
        r0.addAction(r5);
        goto L_0x0091;
    L_0x0088:
        r5 = r0.isFocusable();
        if (r5 == 0) goto L_0x0091;
    L_0x008e:
        r0.addAction(r1);
    L_0x0091:
        r0.setFocused(r3);
        r5 = r11.mHost;
        r6 = r11.mTempGlobalRect;
        r5.getLocationOnScreen(r6);
        r5 = r11.mTempScreenRect;
        r0.getBoundsInScreen(r5);
        r5 = r11.mTempScreenRect;
        r6 = INVALID_PARENT_BOUNDS;
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x00fc;
    L_0x00aa:
        r5 = r11.mTempScreenRect;
        r0.getBoundsInParent(r5);
        r5 = r0.mParentVirtualDescendantId;
        r6 = -1;
        if (r5 == r6) goto L_0x00e1;
    L_0x00b4:
        r5 = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.obtain();
        r7 = r0.mParentVirtualDescendantId;
    L_0x00ba:
        if (r7 == r6) goto L_0x00de;
    L_0x00bc:
        r8 = r11.mHost;
        r5.setParent(r8, r6);
        r8 = INVALID_PARENT_BOUNDS;
        r5.setBoundsInParent(r8);
        r11.onPopulateNodeForVirtualView(r7, r5);
        r8 = r11.mTempParentRect;
        r5.getBoundsInParent(r8);
        r8 = r11.mTempScreenRect;
        r9 = r11.mTempParentRect;
        r9 = r9.left;
        r10 = r11.mTempParentRect;
        r10 = r10.top;
        r8.offset(r9, r10);
        r7 = r5.mParentVirtualDescendantId;
        goto L_0x00ba;
    L_0x00de:
        r5.recycle();
    L_0x00e1:
        r5 = r11.mTempScreenRect;
        r6 = r11.mTempGlobalRect;
        r6 = r6[r4];
        r7 = r11.mHost;
        r7 = r7.getScrollX();
        r6 = r6 - r7;
        r7 = r11.mTempGlobalRect;
        r7 = r7[r1];
        r8 = r11.mHost;
        r8 = r8.getScrollY();
        r7 = r7 - r8;
        r5.offset(r6, r7);
    L_0x00fc:
        r5 = r11.mHost;
        r6 = r11.mTempVisibleRect;
        r5 = r5.getLocalVisibleRect(r6);
        if (r5 == 0) goto L_0x013b;
    L_0x0106:
        r5 = r11.mTempVisibleRect;
        r6 = r11.mTempGlobalRect;
        r4 = r6[r4];
        r6 = r11.mHost;
        r6 = r6.getScrollX();
        r4 = r4 - r6;
        r6 = r11.mTempGlobalRect;
        r6 = r6[r1];
        r7 = r11.mHost;
        r7 = r7.getScrollY();
        r6 = r6 - r7;
        r5.offset(r4, r6);
        r4 = r11.mTempScreenRect;
        r5 = r11.mTempVisibleRect;
        r4 = r4.intersect(r5);
        if (r4 == 0) goto L_0x013b;
    L_0x012b:
        r5 = r11.mTempScreenRect;
        r0.setBoundsInScreen(r5);
        r5 = r11.mTempScreenRect;
        r5 = r11.isVisibleToUser(r5);
        if (r5 == 0) goto L_0x013b;
    L_0x0138:
        r0.setVisibleToUser(r1);
    L_0x013b:
        return r0;
    L_0x013c:
        r1 = new java.lang.RuntimeException;
        r3 = "Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()";
        r1.<init>(r3);
        throw r1;
    L_0x0144:
        r1 = new java.lang.RuntimeException;
        r3 = "Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()";
        r1.<init>(r3);
        throw r1;
    L_0x014c:
        r1 = new java.lang.RuntimeException;
        r2 = "Callbacks must set parent bounds in populateNodeForVirtualViewId()";
        r1.<init>(r2);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.ExploreByTouchHelper.createNodeForChild(int):android.support.v4.view.accessibility.AccessibilityNodeInfoCompat");
    }

    protected abstract int getVirtualViewAt(float f, float f2);

    protected abstract void getVisibleVirtualViews(List<Integer> list);

    protected abstract boolean onPerformActionForVirtualView(int i, int i2, @Nullable Bundle bundle);

    protected abstract void onPopulateNodeForVirtualView(int i, @NonNull AccessibilityNodeInfoCompat accessibilityNodeInfoCompat);

    public ExploreByTouchHelper(@NonNull View host) {
        if (host != null) {
            this.mHost = host;
            this.mManager = (AccessibilityManager) host.getContext().getSystemService("accessibility");
            host.setFocusable(true);
            if (ViewCompat.getImportantForAccessibility(host) == 0) {
                ViewCompat.setImportantForAccessibility(host, 1);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("View may not be null");
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View host) {
        if (this.mNodeProvider == null) {
            this.mNodeProvider = new MyNodeProvider();
        }
        return this.mNodeProvider;
    }

    public final boolean dispatchHoverEvent(@NonNull MotionEvent event) {
        boolean z = false;
        if (this.mManager.isEnabled()) {
            if (this.mManager.isTouchExplorationEnabled()) {
                int action = event.getAction();
                if (action != 7) {
                    switch (action) {
                        case 9:
                            break;
                        case 10:
                            if (this.mHoveredVirtualViewId == Integer.MIN_VALUE) {
                                return false;
                            }
                            updateHoveredVirtualView(Integer.MIN_VALUE);
                            return true;
                        default:
                            return false;
                    }
                }
                action = getVirtualViewAt(event.getX(), event.getY());
                updateHoveredVirtualView(action);
                if (action != Integer.MIN_VALUE) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    public final boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        boolean handled = false;
        if (event.getAction() == 1) {
            return false;
        }
        int keyCode = event.getKeyCode();
        if (keyCode != 61) {
            if (keyCode != 66) {
                switch (keyCode) {
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                        if (!event.hasNoModifiers()) {
                            return false;
                        }
                        int direction = keyToDirection(keyCode);
                        int count = event.getRepeatCount() + 1;
                        for (int i = 0; i < count && moveFocus(direction, null); i++) {
                            handled = true;
                        }
                        return handled;
                    case 23:
                        break;
                    default:
                        return false;
                }
            }
            if (!event.hasNoModifiers() || event.getRepeatCount() != 0) {
                return false;
            }
            clickKeyboardFocusedVirtualView();
            return true;
        } else if (event.hasNoModifiers()) {
            return moveFocus(2, null);
        } else {
            if (event.hasModifiers(1)) {
                return moveFocus(1, null);
            }
            return false;
        }
    }

    public final void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        int i = this.mKeyboardFocusedVirtualViewId;
        if (i != Integer.MIN_VALUE) {
            clearKeyboardFocusForVirtualView(i);
        }
        if (gainFocus) {
            moveFocus(direction, previouslyFocusedRect);
        }
    }

    public final int getAccessibilityFocusedVirtualViewId() {
        return this.mAccessibilityFocusedVirtualViewId;
    }

    public final int getKeyboardFocusedVirtualViewId() {
        return this.mKeyboardFocusedVirtualViewId;
    }

    private static int keyToDirection(int keyCode) {
        switch (keyCode) {
            case 19:
                return 33;
            case 21:
                return 17;
            case 22:
                return 66;
            default:
                return 130;
        }
    }

    private void getBoundsInParent(int virtualViewId, Rect outBounds) {
        obtainAccessibilityNodeInfo(virtualViewId).getBoundsInParent(outBounds);
    }

    private boolean moveFocus(int direction, @Nullable Rect previouslyFocusedRect) {
        AccessibilityNodeInfoCompat focusedNode;
        AccessibilityNodeInfoCompat nextFocusedNode;
        int nextFocusedNodeId;
        SparseArrayCompat<AccessibilityNodeInfoCompat> allNodes = getAllNodes();
        int focusedNodeId = this.mKeyboardFocusedVirtualViewId;
        if (focusedNodeId == Integer.MIN_VALUE) {
            focusedNode = null;
        } else {
            focusedNode = (AccessibilityNodeInfoCompat) allNodes.get(focusedNodeId);
        }
        if (direction == 17 || direction == 33 || direction == 66 || direction == 130) {
            Rect selectedRect = new Rect();
            int i = this.mKeyboardFocusedVirtualViewId;
            if (i != Integer.MIN_VALUE) {
                getBoundsInParent(i, selectedRect);
            } else if (previouslyFocusedRect != null) {
                selectedRect.set(previouslyFocusedRect);
            } else {
                guessPreviouslyFocusedRect(this.mHost, direction, selectedRect);
            }
            nextFocusedNode = (AccessibilityNodeInfoCompat) FocusStrategy.findNextFocusInAbsoluteDirection(allNodes, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, focusedNode, selectedRect, direction);
        } else {
            switch (direction) {
                case 1:
                case 2:
                    nextFocusedNode = (AccessibilityNodeInfoCompat) FocusStrategy.findNextFocusInRelativeDirection(allNodes, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, focusedNode, direction, ViewCompat.getLayoutDirection(this.mHost) == 1, false);
                    break;
                default:
                    throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
        }
        if (nextFocusedNode == null) {
            nextFocusedNodeId = Integer.MIN_VALUE;
        } else {
            nextFocusedNodeId = allNodes.keyAt(allNodes.indexOfValue(nextFocusedNode));
        }
        return requestKeyboardFocusForVirtualView(nextFocusedNodeId);
    }

    private SparseArrayCompat<AccessibilityNodeInfoCompat> getAllNodes() {
        List<Integer> virtualViewIds = new ArrayList();
        getVisibleVirtualViews(virtualViewIds);
        SparseArrayCompat<AccessibilityNodeInfoCompat> allNodes = new SparseArrayCompat();
        for (int virtualViewId = 0; virtualViewId < virtualViewIds.size(); virtualViewId++) {
            allNodes.put(virtualViewId, createNodeForChild(virtualViewId));
        }
        return allNodes;
    }

    private static Rect guessPreviouslyFocusedRect(@NonNull View host, int direction, @NonNull Rect outBounds) {
        int w = host.getWidth();
        int h = host.getHeight();
        if (direction == 17) {
            outBounds.set(w, 0, w, h);
        } else if (direction == 33) {
            outBounds.set(0, h, w, h);
        } else if (direction == 66) {
            outBounds.set(-1, 0, -1, h);
        } else if (direction == 130) {
            outBounds.set(0, -1, w, -1);
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        return outBounds;
    }

    private boolean clickKeyboardFocusedVirtualView() {
        int i = this.mKeyboardFocusedVirtualViewId;
        return i != Integer.MIN_VALUE && onPerformActionForVirtualView(i, 16, null);
    }

    public final boolean sendEventForVirtualView(int virtualViewId, int eventType) {
        if (virtualViewId != Integer.MIN_VALUE) {
            if (this.mManager.isEnabled()) {
                ViewParent parent = this.mHost.getParent();
                if (parent == null) {
                    return false;
                }
                return ViewParentCompat.requestSendAccessibilityEvent(parent, this.mHost, createEvent(virtualViewId, eventType));
            }
        }
        return false;
    }

    public final void invalidateRoot() {
        invalidateVirtualView(-1, 1);
    }

    public final void invalidateVirtualView(int virtualViewId) {
        invalidateVirtualView(virtualViewId, 0);
    }

    public final void invalidateVirtualView(int virtualViewId, int changeTypes) {
        if (virtualViewId != Integer.MIN_VALUE && this.mManager.isEnabled()) {
            ViewParent parent = this.mHost.getParent();
            if (parent != null) {
                AccessibilityEvent event = createEvent(virtualViewId, 2048);
                AccessibilityEventCompat.setContentChangeTypes(event, changeTypes);
                ViewParentCompat.requestSendAccessibilityEvent(parent, this.mHost, event);
            }
        }
    }

    @Deprecated
    public int getFocusedVirtualView() {
        return getAccessibilityFocusedVirtualViewId();
    }

    protected void onVirtualViewKeyboardFocusChanged(int virtualViewId, boolean hasFocus) {
    }

    private void updateHoveredVirtualView(int virtualViewId) {
        if (this.mHoveredVirtualViewId != virtualViewId) {
            int previousVirtualViewId = this.mHoveredVirtualViewId;
            this.mHoveredVirtualViewId = virtualViewId;
            sendEventForVirtualView(virtualViewId, 128);
            sendEventForVirtualView(previousVirtualViewId, 256);
        }
    }

    private AccessibilityEvent createEvent(int virtualViewId, int eventType) {
        if (virtualViewId != -1) {
            return createEventForChild(virtualViewId, eventType);
        }
        return createEventForHost(eventType);
    }

    private AccessibilityEvent createEventForHost(int eventType) {
        AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
        this.mHost.onInitializeAccessibilityEvent(event);
        return event;
    }

    public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(host, event);
        onPopulateEventForHost(event);
    }

    private AccessibilityEvent createEventForChild(int virtualViewId, int eventType) {
        AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
        AccessibilityNodeInfoCompat node = obtainAccessibilityNodeInfo(virtualViewId);
        event.getText().add(node.getText());
        event.setContentDescription(node.getContentDescription());
        event.setScrollable(node.isScrollable());
        event.setPassword(node.isPassword());
        event.setEnabled(node.isEnabled());
        event.setChecked(node.isChecked());
        onPopulateEventForVirtualView(virtualViewId, event);
        if (event.getText().isEmpty()) {
            if (event.getContentDescription() == null) {
                throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
            }
        }
        event.setClassName(node.getClassName());
        AccessibilityRecordCompat.setSource(event, this.mHost, virtualViewId);
        event.setPackageName(this.mHost.getContext().getPackageName());
        return event;
    }

    @NonNull
    AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int virtualViewId) {
        if (virtualViewId == -1) {
            return createNodeForHost();
        }
        return createNodeForChild(virtualViewId);
    }

    @NonNull
    private AccessibilityNodeInfoCompat createNodeForHost() {
        AccessibilityNodeInfoCompat info = AccessibilityNodeInfoCompat.obtain(this.mHost);
        ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, info);
        ArrayList<Integer> virtualViewIds = new ArrayList();
        getVisibleVirtualViews(virtualViewIds);
        if (info.getChildCount() > 0) {
            if (virtualViewIds.size() > 0) {
                throw new RuntimeException("Views cannot have both real and virtual children");
            }
        }
        int count = virtualViewIds.size();
        for (int i = 0; i < count; i++) {
            info.addChild(this.mHost, ((Integer) virtualViewIds.get(i)).intValue());
        }
        return info;
    }

    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
        super.onInitializeAccessibilityNodeInfo(host, info);
        onPopulateNodeForHost(info);
    }

    boolean performAction(int virtualViewId, int action, Bundle arguments) {
        if (virtualViewId != -1) {
            return performActionForChild(virtualViewId, action, arguments);
        }
        return performActionForHost(action, arguments);
    }

    private boolean performActionForHost(int action, Bundle arguments) {
        return ViewCompat.performAccessibilityAction(this.mHost, action, arguments);
    }

    private boolean performActionForChild(int virtualViewId, int action, Bundle arguments) {
        if (action == 64) {
            return requestAccessibilityFocus(virtualViewId);
        }
        if (action == 128) {
            return clearAccessibilityFocus(virtualViewId);
        }
        switch (action) {
            case 1:
                return requestKeyboardFocusForVirtualView(virtualViewId);
            case 2:
                return clearKeyboardFocusForVirtualView(virtualViewId);
            default:
                return onPerformActionForVirtualView(virtualViewId, action, arguments);
        }
    }

    private boolean isVisibleToUser(Rect localRect) {
        boolean z = false;
        if (localRect != null) {
            if (!localRect.isEmpty()) {
                if (this.mHost.getWindowVisibility() != 0) {
                    return false;
                }
                ViewParent viewParent = this.mHost.getParent();
                while (viewParent instanceof View) {
                    View view = (View) viewParent;
                    if (view.getAlpha() > 0.0f) {
                        if (view.getVisibility() == 0) {
                            viewParent = view.getParent();
                        }
                    }
                    return false;
                }
                if (viewParent != null) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    private boolean requestAccessibilityFocus(int virtualViewId) {
        if (this.mManager.isEnabled()) {
            if (this.mManager.isTouchExplorationEnabled()) {
                int i = this.mAccessibilityFocusedVirtualViewId;
                if (i == virtualViewId) {
                    return false;
                }
                if (i != Integer.MIN_VALUE) {
                    clearAccessibilityFocus(i);
                }
                this.mAccessibilityFocusedVirtualViewId = virtualViewId;
                this.mHost.invalidate();
                sendEventForVirtualView(virtualViewId, 32768);
                return true;
            }
        }
        return false;
    }

    private boolean clearAccessibilityFocus(int virtualViewId) {
        if (this.mAccessibilityFocusedVirtualViewId != virtualViewId) {
            return false;
        }
        this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mHost.invalidate();
        sendEventForVirtualView(virtualViewId, 65536);
        return true;
    }

    public final boolean requestKeyboardFocusForVirtualView(int virtualViewId) {
        if (!this.mHost.isFocused() && !this.mHost.requestFocus()) {
            return false;
        }
        int i = this.mKeyboardFocusedVirtualViewId;
        if (i == virtualViewId) {
            return false;
        }
        if (i != Integer.MIN_VALUE) {
            clearKeyboardFocusForVirtualView(i);
        }
        this.mKeyboardFocusedVirtualViewId = virtualViewId;
        onVirtualViewKeyboardFocusChanged(virtualViewId, true);
        sendEventForVirtualView(virtualViewId, 8);
        return true;
    }

    public final boolean clearKeyboardFocusForVirtualView(int virtualViewId) {
        if (this.mKeyboardFocusedVirtualViewId != virtualViewId) {
            return false;
        }
        this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
        onVirtualViewKeyboardFocusChanged(virtualViewId, false);
        sendEventForVirtualView(virtualViewId, 8);
        return true;
    }

    protected void onPopulateEventForVirtualView(int virtualViewId, @NonNull AccessibilityEvent event) {
    }

    protected void onPopulateEventForHost(@NonNull AccessibilityEvent event) {
    }

    protected void onPopulateNodeForHost(@NonNull AccessibilityNodeInfoCompat node) {
    }
}
