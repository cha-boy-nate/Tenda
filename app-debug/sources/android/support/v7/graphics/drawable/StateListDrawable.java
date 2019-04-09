package android.support.v7.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.appcompat.C0185R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.StateSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP})
class StateListDrawable extends DrawableContainer {
    private static final boolean DEBUG = false;
    private static final String TAG = "StateListDrawable";
    private boolean mMutated;
    private StateListState mStateListState;

    static class StateListState extends DrawableContainerState {
        int[][] mStateSets;

        StateListState(StateListState orig, StateListDrawable owner, Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                this.mStateSets = orig.mStateSets;
            } else {
                this.mStateSets = new int[getCapacity()][];
            }
        }

        void mutate() {
            int[][] iArr = this.mStateSets;
            int[][] stateSets = new int[iArr.length][];
            for (int i = iArr.length - 1; i >= 0; i--) {
                int[][] iArr2 = this.mStateSets;
                stateSets[i] = iArr2[i] != null ? (int[]) iArr2[i].clone() : null;
            }
            this.mStateSets = stateSets;
        }

        int addStateSet(int[] stateSet, Drawable drawable) {
            int pos = addChild(drawable);
            this.mStateSets[pos] = stateSet;
            return pos;
        }

        int indexOfStateSet(int[] stateSet) {
            int[][] stateSets = this.mStateSets;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                if (StateSet.stateSetMatches(stateSets[i], stateSet)) {
                    return i;
                }
            }
            return -1;
        }

        @NonNull
        public Drawable newDrawable() {
            return new StateListDrawable(this, null);
        }

        @NonNull
        public Drawable newDrawable(Resources res) {
            return new StateListDrawable(this, res);
        }

        public void growArray(int oldSize, int newSize) {
            super.growArray(oldSize, newSize);
            int[][] newStateSets = new int[newSize][];
            System.arraycopy(this.mStateSets, 0, newStateSets, 0, oldSize);
            this.mStateSets = newStateSets;
        }
    }

    StateListDrawable() {
        this(null, null);
    }

    public void addState(int[] stateSet, Drawable drawable) {
        if (drawable != null) {
            this.mStateListState.addStateSet(stateSet, drawable);
            onStateChange(getState());
        }
    }

    public boolean isStateful() {
        return true;
    }

    protected boolean onStateChange(int[] stateSet) {
        boolean changed = super.onStateChange(stateSet);
        int idx = this.mStateListState.indexOfStateSet(stateSet);
        if (idx < 0) {
            idx = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
        }
        if (!selectDrawable(idx)) {
            if (!changed) {
                return false;
            }
        }
        return true;
    }

    public void inflate(@NonNull Context context, @NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        TypedArray a = TypedArrayUtils.obtainAttributes(r, theme, attrs, C0185R.styleable.StateListDrawable);
        setVisible(a.getBoolean(C0185R.styleable.StateListDrawable_android_visible, true), true);
        updateStateFromTypedArray(a);
        updateDensity(r);
        a.recycle();
        inflateChildElements(context, r, parser, attrs, theme);
        onStateChange(getState());
    }

    private void updateStateFromTypedArray(TypedArray a) {
        StateListState state = this.mStateListState;
        if (VERSION.SDK_INT >= 21) {
            state.mChangingConfigurations |= a.getChangingConfigurations();
        }
        state.mVariablePadding = a.getBoolean(C0185R.styleable.StateListDrawable_android_variablePadding, state.mVariablePadding);
        state.mConstantSize = a.getBoolean(C0185R.styleable.StateListDrawable_android_constantSize, state.mConstantSize);
        state.mEnterFadeDuration = a.getInt(C0185R.styleable.StateListDrawable_android_enterFadeDuration, state.mEnterFadeDuration);
        state.mExitFadeDuration = a.getInt(C0185R.styleable.StateListDrawable_android_exitFadeDuration, state.mExitFadeDuration);
        state.mDither = a.getBoolean(C0185R.styleable.StateListDrawable_android_dither, state.mDither);
    }

    private void inflateChildElements(Context context, Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        AttributeSet attributeSet = attrs;
        StateListState state = this.mStateListState;
        int i = 1;
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            int next = parser.next();
            int type = next;
            Context context2;
            Resources resources;
            Theme theme2;
            if (next != i) {
                next = parser.getDepth();
                int depth = next;
                if (next < innerDepth) {
                    if (type == 3) {
                        context2 = context;
                        resources = r;
                        theme2 = theme;
                        return;
                    }
                }
                if (type == 2) {
                    if (depth > innerDepth) {
                        context2 = context;
                        resources = r;
                        theme2 = theme;
                        i = 1;
                    } else if (parser.getName().equals("item")) {
                        TypedArray a = TypedArrayUtils.obtainAttributes(r, theme, attributeSet, C0185R.styleable.StateListDrawableItem);
                        Drawable dr = null;
                        int drawableId = a.getResourceId(C0185R.styleable.StateListDrawableItem_android_drawable, -1);
                        if (drawableId > 0) {
                            dr = AppCompatResources.getDrawable(context, drawableId);
                        } else {
                            context2 = context;
                        }
                        a.recycle();
                        int[] states = extractStateSet(attributeSet);
                        if (dr == null) {
                            while (true) {
                                int next2 = parser.next();
                                type = next2;
                                if (next2 != 4) {
                                    break;
                                }
                            }
                            if (type != 2) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(parser.getPositionDescription());
                                stringBuilder.append(": <item> tag requires a 'drawable' attribute or ");
                                stringBuilder.append("child tag defining a drawable");
                                throw new XmlPullParserException(stringBuilder.toString());
                            } else if (VERSION.SDK_INT >= 21) {
                                dr = Drawable.createFromXmlInner(r, parser, attrs, theme);
                            } else {
                                dr = Drawable.createFromXmlInner(r, parser, attrs);
                            }
                        }
                        state.addStateSet(states, dr);
                        i = 1;
                    }
                }
            } else {
                context2 = context;
                resources = r;
                theme2 = theme;
                return;
            }
        }
    }

    int[] extractStateSet(AttributeSet attrs) {
        int j = 0;
        int numAttrs = attrs.getAttributeCount();
        int[] states = new int[numAttrs];
        for (int i = 0; i < numAttrs; i++) {
            int stateResId = attrs.getAttributeNameResource(i);
            if (stateResId != 0) {
                if (stateResId != 16842960 && stateResId != 16843161) {
                    int j2 = j + 1;
                    states[j] = attrs.getAttributeBooleanValue(i, false) ? stateResId : -stateResId;
                    j = j2;
                }
            }
        }
        return StateSet.trimStateSet(states, j);
    }

    StateListState getStateListState() {
        return this.mStateListState;
    }

    int getStateCount() {
        return this.mStateListState.getChildCount();
    }

    int[] getStateSet(int index) {
        return this.mStateListState.mStateSets[index];
    }

    Drawable getStateDrawable(int index) {
        return this.mStateListState.getChild(index);
    }

    int getStateDrawableIndex(int[] stateSet) {
        return this.mStateListState.indexOfStateSet(stateSet);
    }

    @NonNull
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mStateListState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    StateListState cloneConstantState() {
        return new StateListState(this.mStateListState, this, null);
    }

    void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @RequiresApi(21)
    public void applyTheme(@NonNull Theme theme) {
        super.applyTheme(theme);
        onStateChange(getState());
    }

    protected void setConstantState(@NonNull DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof StateListState) {
            this.mStateListState = (StateListState) state;
        }
    }

    StateListDrawable(StateListState state, Resources res) {
        setConstantState(new StateListState(state, this, res));
        onStateChange(getState());
    }

    StateListDrawable(@Nullable StateListState state) {
        if (state != null) {
            setConstantState(state);
        }
    }
}
