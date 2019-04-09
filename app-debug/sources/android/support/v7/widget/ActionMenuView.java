package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuBuilder.ItemInvoker;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter.Callback;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView extends LinearLayoutCompat implements ItemInvoker, MenuView {
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    @RestrictTo({Scope.LIBRARY_GROUP})
    public interface ActionMenuChildView {
        boolean needsDividerAfter();

        boolean needsDividerBefore();
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    private static class ActionMenuPresenterCallback implements Callback {
        ActionMenuPresenterCallback() {
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            return false;
        }
    }

    public static class LayoutParams extends android.support.v7.widget.LinearLayoutCompat.LayoutParams {
        @ExportedProperty
        public int cellsUsed;
        @ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ExportedProperty
        public int extraPixels;
        @ExportedProperty
        public boolean isOverflowButton;
        @ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams other) {
            super(other);
        }

        public LayoutParams(LayoutParams other) {
            super((android.view.ViewGroup.LayoutParams) other);
            this.isOverflowButton = other.isOverflowButton;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.isOverflowButton = false;
        }

        LayoutParams(int width, int height, boolean isOverflowButton) {
            super(width, height);
            this.isOverflowButton = isOverflowButton;
        }
    }

    private class MenuBuilderCallback implements MenuBuilder.Callback {
        MenuBuilderCallback() {
        }

        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(item);
        }

        public void onMenuModeChange(MenuBuilder menu) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menu);
            }
        }
    }

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBaselineAligned(false);
        float density = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int) (56.0f * density);
        this.mGeneratedItemPadding = (int) (4.0f * density);
        this.mPopupContext = context;
        this.mPopupTheme = 0;
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

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setPresenter(ActionMenuPresenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.setMenuView(this);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean wasFormatted = this.mFormatItems;
        this.mFormatItems = MeasureSpec.getMode(widthMeasureSpec) == 1073741824;
        if (wasFormatted != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (this.mFormatItems) {
            MenuBuilder menuBuilder = this.mMenu;
            if (!(menuBuilder == null || widthSize == this.mFormatItemsWidth)) {
                this.mFormatItemsWidth = widthSize;
                menuBuilder.onItemsChanged(true);
            }
        }
        int childCount = getChildCount();
        if (!this.mFormatItems || childCount <= 0) {
            for (int i = 0; i < childCount; i++) {
                LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
                lp.rightMargin = 0;
                lp.leftMargin = 0;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        onMeasureExactFormat(widthMeasureSpec, heightMeasureSpec);
    }

    private void onMeasureExactFormat(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthPadding = getPaddingLeft() + getPaddingRight();
        int heightPadding = getPaddingTop() + getPaddingBottom();
        int itemHeightSpec = getChildMeasureSpec(heightMeasureSpec, heightPadding, -2);
        widthSize -= widthPadding;
        int cellSize = this.mMinCellSize;
        int cellCount = widthSize / cellSize;
        int cellSizeRemaining = widthSize % cellSize;
        if (cellCount == 0) {
            setMeasuredDimension(widthSize, 0);
            return;
        }
        int cellCount2;
        int cellSizeRemaining2;
        boolean isGeneratedItem;
        boolean z;
        boolean needsExpansion;
        int widthSize2;
        int maxChildHeight;
        cellSize += cellSizeRemaining / cellCount;
        int cellsRemaining = cellCount;
        boolean hasOverflow = false;
        boolean childCount = getChildCount();
        int heightSize2 = heightSize;
        heightSize = 0;
        int visibleItemCount = 0;
        int expandableItemCount = 0;
        int maxCellsUsed = 0;
        int cellsRemaining2 = cellsRemaining;
        boolean i = false;
        long smallestItemsAt = 0;
        while (true) {
            int widthPadding2 = widthPadding;
            if (i >= childCount) {
                break;
            }
            int heightPadding2;
            int cellsUsed;
            View child = getChildAt(i);
            cellCount2 = cellCount;
            if (child.getVisibility() == 8) {
                heightPadding2 = heightPadding;
                cellSizeRemaining2 = cellSizeRemaining;
            } else {
                isGeneratedItem = child instanceof ActionMenuItemView;
                visibleItemCount++;
                if (isGeneratedItem) {
                    cellCount = r0.mGeneratedItemPadding;
                    cellSizeRemaining2 = cellSizeRemaining;
                    z = false;
                    child.setPadding(cellCount, 0, cellCount, 0);
                } else {
                    cellSizeRemaining2 = cellSizeRemaining;
                    z = false;
                }
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.expanded = z;
                lp.extraPixels = z;
                lp.cellsUsed = z;
                lp.expandable = z;
                lp.leftMargin = z;
                lp.rightMargin = z;
                z = isGeneratedItem && ((ActionMenuItemView) child).hasText();
                lp.preventEdgeOffset = z;
                cellsUsed = measureChildForCells(child, cellSize, lp.isOverflowButton ? 1 : cellsRemaining2, itemHeightSpec, heightPadding);
                maxCellsUsed = Math.max(maxCellsUsed, cellsUsed);
                heightPadding2 = heightPadding;
                if (lp.expandable) {
                    expandableItemCount++;
                }
                if (lp.isOverflowButton) {
                    hasOverflow = true;
                }
                cellsRemaining2 -= cellsUsed;
                heightSize = Math.max(heightSize, child.getMeasuredHeight());
                if (cellsUsed == 1) {
                    smallestItemsAt |= (long) (1 << i);
                    heightSize = heightSize;
                } else {
                    View view = child;
                }
            }
            i++;
            widthPadding = widthPadding2;
            cellCount = cellCount2;
            cellSizeRemaining = cellSizeRemaining2;
            heightPadding = heightPadding2;
            cellsUsed = heightMeasureSpec;
        }
        cellCount2 = cellCount;
        cellSizeRemaining2 = cellSizeRemaining;
        boolean centerSingleExpandedItem = hasOverflow && visibleItemCount == 2;
        isGeneratedItem = false;
        while (expandableItemCount > 0 && cellsRemaining2 > 0) {
            long minCellsAt = 0;
            cellCount = Integer.MAX_VALUE;
            widthPadding = 0;
            i = false;
            while (i < childCount) {
                View child2 = getChildAt(i);
                needsExpansion = isGeneratedItem;
                LayoutParams needsExpansion2 = (LayoutParams) child2.getLayoutParams();
                if (needsExpansion2.expandable) {
                    if (needsExpansion2.cellsUsed < cellCount) {
                        cellCount = needsExpansion2.cellsUsed;
                        minCellsAt = 1 << i;
                        widthPadding = 1;
                    } else if (needsExpansion2.cellsUsed == cellCount) {
                        minCellsAt |= 1 << i;
                        widthPadding++;
                    }
                }
                i++;
                isGeneratedItem = needsExpansion;
            }
            needsExpansion = isGeneratedItem;
            smallestItemsAt |= minCellsAt;
            if (widthPadding > cellsRemaining2) {
                widthSize2 = widthSize;
                maxChildHeight = heightSize;
                break;
            }
            int minCellsItemCount;
            cellCount++;
            isGeneratedItem = false;
            while (isGeneratedItem < childCount) {
                child2 = getChildAt(isGeneratedItem);
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                minCellsItemCount = widthPadding;
                widthSize2 = widthSize;
                maxChildHeight = heightSize;
                if ((minCellsAt & ((long) (1 << isGeneratedItem))) != 0) {
                    if (centerSingleExpandedItem && lp2.preventEdgeOffset && cellsRemaining2 == 1) {
                        widthSize = r0.mGeneratedItemPadding;
                        child2.setPadding(widthSize + cellSize, 0, widthSize, 0);
                    }
                    lp2.cellsUsed++;
                    lp2.expanded = true;
                    cellsRemaining2--;
                } else if (lp2.cellsUsed == cellCount) {
                    smallestItemsAt |= (long) (1 << isGeneratedItem);
                }
                isGeneratedItem++;
                widthPadding = minCellsItemCount;
                widthSize = widthSize2;
                heightSize = maxChildHeight;
            }
            maxChildHeight = heightSize;
            minCellsItemCount = widthPadding;
            isGeneratedItem = true;
        }
        widthSize2 = widthSize;
        maxChildHeight = heightSize;
        needsExpansion = isGeneratedItem;
        boolean singleItem = !hasOverflow && visibleItemCount == 1;
        boolean z2;
        if (cellsRemaining2 <= 0 || smallestItemsAt == 0) {
            z2 = centerSingleExpandedItem;
        } else {
            float expandCount;
            if (cellsRemaining2 >= visibleItemCount - 1 && !singleItem) {
                if (maxCellsUsed <= 1) {
                    z = singleItem;
                    z2 = centerSingleExpandedItem;
                }
            }
            float expandCount2 = (float) Long.bitCount(smallestItemsAt);
            if (singleItem) {
            } else {
                if ((smallestItemsAt & 1) != 0) {
                    if (!((LayoutParams) getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                        expandCount2 -= 0.5f;
                    }
                }
                if (!((smallestItemsAt & ((long) (1 << (childCount - 1)))) == 0 || ((LayoutParams) getChildAt(childCount - 1).getLayoutParams()).preventEdgeOffset)) {
                    expandCount2 -= 0.5f;
                }
            }
            widthPadding = expandCount2 > 0.0f ? (int) (((float) (cellsRemaining2 * cellSize)) / expandCount2) : 0;
            centerSingleExpandedItem = false;
            isGeneratedItem = needsExpansion;
            while (centerSingleExpandedItem < childCount) {
                z = singleItem;
                expandCount = expandCount2;
                if (smallestItemsAt & ((long) (1 << centerSingleExpandedItem))) {
                    singleItem = getChildAt(centerSingleExpandedItem);
                    LayoutParams lp3 = (LayoutParams) singleItem.getLayoutParams();
                    if (singleItem instanceof ActionMenuItemView) {
                        lp3.extraPixels = widthPadding;
                        lp3.expanded = true;
                        if (!(centerSingleExpandedItem || lp3.preventEdgeOffset)) {
                            lp3.leftMargin = (-widthPadding) / 2;
                        }
                        isGeneratedItem = true;
                    } else if (lp3.isOverflowButton) {
                        lp3.extraPixels = widthPadding;
                        lp3.expanded = true;
                        lp3.rightMargin = (-widthPadding) / 2;
                        isGeneratedItem = true;
                    } else {
                        if (centerSingleExpandedItem) {
                            lp3.leftMargin = widthPadding / 2;
                        }
                        if (centerSingleExpandedItem != childCount - 1) {
                            lp3.rightMargin = widthPadding / 2;
                        }
                    }
                }
                centerSingleExpandedItem++;
                singleItem = z;
                expandCount2 = expandCount;
            }
            expandCount = expandCount2;
            needsExpansion = isGeneratedItem;
        }
        if (needsExpansion) {
            for (boolean i2 = false; i2 < childCount; i2++) {
                child = getChildAt(i2);
                LayoutParams lp4 = (LayoutParams) child.getLayoutParams();
                if (lp4.expanded) {
                    child.measure(MeasureSpec.makeMeasureSpec((lp4.cellsUsed * cellSize) + lp4.extraPixels, 1073741824), itemHeightSpec);
                }
            }
        }
        if (heightMode != 1073741824) {
            heightSize = maxChildHeight;
        } else {
            heightSize = heightSize2;
        }
        setMeasuredDimension(widthSize2, heightSize);
    }

    static int measureChildForCells(View child, int cellSize, int cellsRemaining, int parentHeightMeasureSpec, int parentHeightPadding) {
        View view = child;
        int i = cellsRemaining;
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int childHeightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(parentHeightMeasureSpec) - parentHeightPadding, MeasureSpec.getMode(parentHeightMeasureSpec));
        ActionMenuItemView itemView = view instanceof ActionMenuItemView ? (ActionMenuItemView) view : null;
        boolean expandable = false;
        boolean hasText = itemView != null && itemView.hasText();
        int cellsUsed = 0;
        if (i > 0 && (!hasText || i >= 2)) {
            child.measure(MeasureSpec.makeMeasureSpec(cellSize * i, Integer.MIN_VALUE), childHeightSpec);
            int measuredWidth = child.getMeasuredWidth();
            cellsUsed = measuredWidth / cellSize;
            if (measuredWidth % cellSize != 0) {
                cellsUsed++;
            }
            if (hasText && cellsUsed < 2) {
                cellsUsed = 2;
            }
        }
        if (!lp.isOverflowButton && hasText) {
            expandable = true;
        }
        lp.expandable = expandable;
        lp.cellsUsed = cellsUsed;
        child.measure(MeasureSpec.makeMeasureSpec(cellsUsed * cellSize, 1073741824), childHeightSpec);
        return cellsUsed;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.mFormatItems) {
            int i;
            ActionMenuView actionMenuView;
            int midVertical;
            boolean isLayoutRtl;
            LayoutParams p;
            int height;
            int l;
            int r;
            int childCount = getChildCount();
            int midVertical2 = (bottom - top) / 2;
            int dividerWidth = getDividerWidth();
            int overflowWidth = 0;
            int nonOverflowWidth = 0;
            int nonOverflowCount = 0;
            int widthRemaining = ((right - left) - getPaddingRight()) - getPaddingLeft();
            boolean hasOverflow = false;
            boolean isLayoutRtl2 = ViewUtils.isLayoutRtl(this);
            int i2 = 0;
            while (true) {
                i = 8;
                if (i2 >= childCount) {
                    break;
                }
                View v = getChildAt(i2);
                if (v.getVisibility() == 8) {
                    midVertical = midVertical2;
                    isLayoutRtl = isLayoutRtl2;
                } else {
                    p = (LayoutParams) v.getLayoutParams();
                    if (p.isOverflowButton) {
                        overflowWidth = v.getMeasuredWidth();
                        if (hasSupportDividerBeforeChildAt(i2)) {
                            overflowWidth += dividerWidth;
                        }
                        height = v.getMeasuredHeight();
                        if (isLayoutRtl2) {
                            l = getPaddingLeft() + p.leftMargin;
                            r = l + overflowWidth;
                        } else {
                            r = (getWidth() - getPaddingRight()) - p.rightMargin;
                            l = r - overflowWidth;
                        }
                        isLayoutRtl = isLayoutRtl2;
                        isLayoutRtl2 = midVertical2 - (height / 2);
                        midVertical = midVertical2;
                        v.layout(l, isLayoutRtl2, r, isLayoutRtl2 + height);
                        widthRemaining -= overflowWidth;
                        hasOverflow = true;
                    } else {
                        midVertical = midVertical2;
                        isLayoutRtl = isLayoutRtl2;
                        midVertical2 = (v.getMeasuredWidth() + p.leftMargin) + p.rightMargin;
                        nonOverflowWidth += midVertical2;
                        widthRemaining -= midVertical2;
                        if (hasSupportDividerBeforeChildAt(i2)) {
                            nonOverflowWidth += dividerWidth;
                        }
                        nonOverflowCount++;
                    }
                }
                i2++;
                midVertical2 = midVertical;
                isLayoutRtl2 = isLayoutRtl;
            }
            midVertical = midVertical2;
            isLayoutRtl = isLayoutRtl2;
            int spacerCount = 1;
            int i3;
            if (childCount != 1 || hasOverflow) {
                if (hasOverflow) {
                    spacerCount = 0;
                }
                spacerCount = nonOverflowCount - spacerCount;
                midVertical2 = Math.max(0, spacerCount > 0 ? widthRemaining / spacerCount : 0);
                int overflowWidth2;
                if (isLayoutRtl) {
                    i2 = getWidth() - getPaddingRight();
                    i3 = 0;
                    while (i3 < childCount) {
                        int dividerWidth2;
                        View v2 = getChildAt(i3);
                        LayoutParams lp = (LayoutParams) v2.getLayoutParams();
                        if (v2.getVisibility() == i) {
                            dividerWidth2 = dividerWidth;
                            overflowWidth2 = overflowWidth;
                        } else if (lp.isOverflowButton) {
                            dividerWidth2 = dividerWidth;
                            overflowWidth2 = overflowWidth;
                        } else {
                            i2 -= lp.rightMargin;
                            r = v2.getMeasuredWidth();
                            int height2 = v2.getMeasuredHeight();
                            i = midVertical - (height2 / 2);
                            dividerWidth2 = dividerWidth;
                            overflowWidth2 = overflowWidth;
                            v2.layout(i2 - r, i, i2, i + height2);
                            i2 -= (lp.leftMargin + r) + midVertical2;
                        }
                        i3++;
                        dividerWidth = dividerWidth2;
                        overflowWidth = overflowWidth2;
                        i = 8;
                    }
                    overflowWidth2 = overflowWidth;
                } else {
                    overflowWidth2 = overflowWidth;
                    dividerWidth = getPaddingLeft();
                    overflowWidth = 0;
                    while (overflowWidth < childCount) {
                        View v3 = actionMenuView.getChildAt(overflowWidth);
                        p = (LayoutParams) v3.getLayoutParams();
                        if (v3.getVisibility() != 8) {
                            if (!p.isOverflowButton) {
                                dividerWidth += p.leftMargin;
                                i3 = v3.getMeasuredWidth();
                                l = v3.getMeasuredHeight();
                                r = midVertical - (l / 2);
                                v3.layout(dividerWidth, r, dividerWidth + i3, r + l);
                                dividerWidth += (p.rightMargin + i3) + midVertical2;
                            }
                        }
                        overflowWidth++;
                        actionMenuView = this;
                    }
                }
                return;
            }
            View v4 = getChildAt(0);
            spacerCount = v4.getMeasuredWidth();
            i2 = v4.getMeasuredHeight();
            i3 = ((right - left) / 2) - (spacerCount / 2);
            height = midVertical - (i2 / 2);
            v4.layout(i3, height, i3 + spacerCount, height + i2);
            return;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissPopupMenus();
    }

    public void setOverflowIcon(@Nullable Drawable icon) {
        getMenu();
        this.mPresenter.setOverflowIcon(icon);
    }

    @Nullable
    public Drawable getOverflowIcon() {
        getMenu();
        return this.mPresenter.getOverflowIcon();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setOverflowReserved(boolean reserveOverflow) {
        this.mReserveOverflow = reserveOverflow;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams params = new LayoutParams(-2, -2);
        params.gravity = 16;
        return params;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p == null) {
            return generateDefaultLayoutParams();
        }
        LayoutParams result = p instanceof LayoutParams ? new LayoutParams((LayoutParams) p) : new LayoutParams(p);
        if (result.gravity <= 0) {
            result.gravity = 16;
        }
        return result;
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p != null && (p instanceof LayoutParams);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams result = generateDefaultLayoutParams();
        result.isOverflowButton = true;
        return result;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean invokeItem(MenuItemImpl item) {
        return this.mMenu.performItemAction(item, 0);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public int getWindowAnimations() {
        return 0;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void initialize(MenuBuilder menu) {
        this.mMenu = menu;
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            Context context = getContext();
            this.mMenu = new MenuBuilder(context);
            this.mMenu.setCallback(new MenuBuilderCallback());
            this.mPresenter = new ActionMenuPresenter(context);
            this.mPresenter.setReserveOverflow(true);
            ActionMenuPresenter actionMenuPresenter = this.mPresenter;
            Callback callback = this.mActionMenuPresenterCallback;
            if (callback == null) {
                callback = new ActionMenuPresenterCallback();
            }
            actionMenuPresenter.setCallback(callback);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return this.mMenu;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setMenuCallbacks(Callback pcb, MenuBuilder.Callback mcb) {
        this.mActionMenuPresenterCallback = pcb;
        this.mMenuBuilderCallback = mcb;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu();
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isOverflowMenuShowPending() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending();
    }

    public void dismissPopupMenus() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    protected boolean hasSupportDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0) {
            return false;
        }
        View childBefore = getChildAt(childIndex - 1);
        View child = getChildAt(childIndex);
        boolean result = false;
        if (childIndex < getChildCount() && (childBefore instanceof ActionMenuChildView)) {
            result = false | ((ActionMenuChildView) childBefore).needsDividerAfter();
        }
        if (childIndex > 0 && (child instanceof ActionMenuChildView)) {
            result |= ((ActionMenuChildView) child).needsDividerBefore();
        }
        return result;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return false;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setExpandedActionViewsExclusive(boolean exclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(exclusive);
    }
}
