package android.support.v7.graphics.drawable;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.appcompat.C0185R;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedStateListDrawableCompat extends StateListDrawable {
    private static final String ELEMENT_ITEM = "item";
    private static final String ELEMENT_TRANSITION = "transition";
    private static final String ITEM_MISSING_DRAWABLE_ERROR = ": <item> tag requires a 'drawable' attribute or child tag defining a drawable";
    private static final String LOGTAG = AnimatedStateListDrawableCompat.class.getSimpleName();
    private static final String TRANSITION_MISSING_DRAWABLE_ERROR = ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable";
    private static final String TRANSITION_MISSING_FROM_TO_ID = ": <transition> tag requires 'fromId' & 'toId' attributes";
    private boolean mMutated;
    private AnimatedStateListState mState;
    private Transition mTransition;
    private int mTransitionFromIndex;
    private int mTransitionToIndex;

    private static class FrameInterpolator implements TimeInterpolator {
        private int[] mFrameTimes;
        private int mFrames;
        private int mTotalDuration;

        FrameInterpolator(AnimationDrawable d, boolean reversed) {
            updateFrames(d, reversed);
        }

        int updateFrames(AnimationDrawable d, boolean reversed) {
            int frameCount = d.getNumberOfFrames();
            this.mFrames = frameCount;
            int[] iArr = this.mFrameTimes;
            if (iArr == null || iArr.length < frameCount) {
                this.mFrameTimes = new int[frameCount];
            }
            iArr = this.mFrameTimes;
            int totalDuration = 0;
            for (int i = 0; i < frameCount; i++) {
                int duration = d.getDuration(reversed ? (frameCount - i) - 1 : i);
                iArr[i] = duration;
                totalDuration += duration;
            }
            this.mTotalDuration = totalDuration;
            return totalDuration;
        }

        int getTotalDuration() {
            return this.mTotalDuration;
        }

        public float getInterpolation(float input) {
            float frameElapsed;
            int elapsed = (int) ((((float) this.mTotalDuration) * input) + 1056964608);
            int frameCount = this.mFrames;
            int[] frameTimes = this.mFrameTimes;
            int remaining = elapsed;
            int i = 0;
            while (i < frameCount && remaining >= frameTimes[i]) {
                remaining -= frameTimes[i];
                i++;
            }
            if (i < frameCount) {
                frameElapsed = ((float) remaining) / ((float) this.mTotalDuration);
            } else {
                frameElapsed = 0.0f;
            }
            return (((float) i) / ((float) frameCount)) + frameElapsed;
        }
    }

    private static abstract class Transition {
        public abstract void start();

        public abstract void stop();

        private Transition() {
        }

        public void reverse() {
        }

        public boolean canReverse() {
            return false;
        }
    }

    private static class AnimatableTransition extends Transition {
        private final Animatable mA;

        AnimatableTransition(Animatable a) {
            super();
            this.mA = a;
        }

        public void start() {
            this.mA.start();
        }

        public void stop() {
            this.mA.stop();
        }
    }

    private static class AnimatedVectorDrawableTransition extends Transition {
        private final AnimatedVectorDrawableCompat mAvd;

        AnimatedVectorDrawableTransition(AnimatedVectorDrawableCompat avd) {
            super();
            this.mAvd = avd;
        }

        public void start() {
            this.mAvd.start();
        }

        public void stop() {
            this.mAvd.stop();
        }
    }

    private static class AnimationDrawableTransition extends Transition {
        private final ObjectAnimator mAnim;
        private final boolean mHasReversibleFlag;

        AnimationDrawableTransition(AnimationDrawable ad, boolean reversed, boolean hasReversibleFlag) {
            super();
            int frameCount = ad.getNumberOfFrames();
            int fromFrame = reversed ? frameCount - 1 : 0;
            int toFrame = reversed ? 0 : frameCount - 1;
            FrameInterpolator interp = new FrameInterpolator(ad, reversed);
            ObjectAnimator anim = ObjectAnimator.ofInt(ad, "currentIndex", new int[]{fromFrame, toFrame});
            if (VERSION.SDK_INT >= 18) {
                anim.setAutoCancel(true);
            }
            anim.setDuration((long) interp.getTotalDuration());
            anim.setInterpolator(interp);
            this.mHasReversibleFlag = hasReversibleFlag;
            this.mAnim = anim;
        }

        public boolean canReverse() {
            return this.mHasReversibleFlag;
        }

        public void start() {
            this.mAnim.start();
        }

        public void reverse() {
            this.mAnim.reverse();
        }

        public void stop() {
            this.mAnim.cancel();
        }
    }

    static class AnimatedStateListState extends StateListState {
        private static final long REVERSED_BIT = 4294967296L;
        private static final long REVERSIBLE_FLAG_BIT = 8589934592L;
        SparseArrayCompat<Integer> mStateIds;
        LongSparseArray<Long> mTransitions;

        AnimatedStateListState(@Nullable AnimatedStateListState orig, @NonNull AnimatedStateListDrawableCompat owner, @Nullable Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                this.mTransitions = orig.mTransitions;
                this.mStateIds = orig.mStateIds;
                return;
            }
            this.mTransitions = new LongSparseArray();
            this.mStateIds = new SparseArrayCompat();
        }

        void mutate() {
            this.mTransitions = this.mTransitions.clone();
            this.mStateIds = this.mStateIds.clone();
        }

        int addTransition(int fromId, int toId, @NonNull Drawable anim, boolean reversible) {
            int pos = super.addChild(anim);
            long keyFromTo = generateTransitionKey(fromId, toId);
            long reversibleBit = 0;
            if (reversible) {
                reversibleBit = REVERSIBLE_FLAG_BIT;
            }
            r0.mTransitions.append(keyFromTo, Long.valueOf(((long) pos) | reversibleBit));
            if (reversible) {
                r0.mTransitions.append(generateTransitionKey(toId, fromId), Long.valueOf((((long) pos) | REVERSED_BIT) | reversibleBit));
            } else {
                int i = fromId;
                int i2 = toId;
            }
            return pos;
        }

        int addStateSet(@NonNull int[] stateSet, @NonNull Drawable drawable, int id) {
            int index = super.addStateSet(stateSet, drawable);
            this.mStateIds.put(index, Integer.valueOf(id));
            return index;
        }

        int indexOfKeyframe(@NonNull int[] stateSet) {
            int index = super.indexOfStateSet(stateSet);
            if (index >= 0) {
                return index;
            }
            return super.indexOfStateSet(StateSet.WILD_CARD);
        }

        int getKeyframeIdAt(int index) {
            return index < 0 ? 0 : ((Integer) this.mStateIds.get(index, Integer.valueOf(0))).intValue();
        }

        int indexOfTransition(int fromId, int toId) {
            return (int) ((Long) this.mTransitions.get(generateTransitionKey(fromId, toId), Long.valueOf(-1))).longValue();
        }

        boolean isTransitionReversed(int fromId, int toId) {
            return (((Long) this.mTransitions.get(generateTransitionKey(fromId, toId), Long.valueOf(-1))).longValue() & REVERSED_BIT) != 0;
        }

        boolean transitionHasReversibleFlag(int fromId, int toId) {
            return (((Long) this.mTransitions.get(generateTransitionKey(fromId, toId), Long.valueOf(-1))).longValue() & REVERSIBLE_FLAG_BIT) != 0;
        }

        @NonNull
        public Drawable newDrawable() {
            return new AnimatedStateListDrawableCompat(this, null);
        }

        @NonNull
        public Drawable newDrawable(Resources res) {
            return new AnimatedStateListDrawableCompat(this, res);
        }

        private static long generateTransitionKey(int fromId, int toId) {
            return (((long) fromId) << 32) | ((long) toId);
        }
    }

    private int parseItem(@android.support.annotation.NonNull android.content.Context r10, @android.support.annotation.NonNull android.content.res.Resources r11, @android.support.annotation.NonNull org.xmlpull.v1.XmlPullParser r12, @android.support.annotation.NonNull android.util.AttributeSet r13, @android.support.annotation.Nullable android.content.res.Resources.Theme r14) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:24:0x0090 in {2, 7, 12, 15, 16, 18, 21, 23} preds:[]
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
        r9 = this;
        r0 = android.support.v7.appcompat.C0185R.styleable.AnimatedStateListDrawableItem;
        r0 = android.support.v4.content.res.TypedArrayUtils.obtainAttributes(r11, r14, r13, r0);
        r1 = android.support.v7.appcompat.C0185R.styleable.AnimatedStateListDrawableItem_android_id;
        r2 = 0;
        r1 = r0.getResourceId(r1, r2);
        r2 = 0;
        r3 = android.support.v7.appcompat.C0185R.styleable.AnimatedStateListDrawableItem_android_drawable;
        r4 = -1;
        r3 = r0.getResourceId(r3, r4);
        if (r3 <= 0) goto L_0x001b;
    L_0x0017:
        r2 = android.support.v7.content.res.AppCompatResources.getDrawable(r10, r3);
    L_0x001b:
        r0.recycle();
        r4 = r9.extractStateSet(r13);
        if (r2 != 0) goto L_0x006c;
    L_0x0024:
        r5 = r12.next();
        r6 = r5;
        r7 = 4;
        if (r5 != r7) goto L_0x002d;
    L_0x002c:
        goto L_0x0024;
    L_0x002d:
        r5 = 2;
        if (r6 != r5) goto L_0x0051;
    L_0x0030:
        r5 = r12.getName();
        r7 = "vector";
        r5 = r5.equals(r7);
        if (r5 == 0) goto L_0x0041;
    L_0x003c:
        r2 = android.support.graphics.drawable.VectorDrawableCompat.createFromXmlInner(r11, r12, r13, r14);
        goto L_0x006c;
    L_0x0041:
        r5 = android.os.Build.VERSION.SDK_INT;
        r7 = 21;
        if (r5 < r7) goto L_0x004c;
    L_0x0047:
        r2 = android.graphics.drawable.Drawable.createFromXmlInner(r11, r12, r13, r14);
        goto L_0x006c;
    L_0x004c:
        r2 = android.graphics.drawable.Drawable.createFromXmlInner(r11, r12, r13);
        goto L_0x006c;
    L_0x0051:
        r5 = new org.xmlpull.v1.XmlPullParserException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r12.getPositionDescription();
        r7.append(r8);
        r8 = ": <item> tag requires a 'drawable' attribute or child tag defining a drawable";
        r7.append(r8);
        r7 = r7.toString();
        r5.<init>(r7);
        throw r5;
    L_0x006c:
        if (r2 == 0) goto L_0x0075;
    L_0x006e:
        r5 = r9.mState;
        r5 = r5.addStateSet(r4, r2, r1);
        return r5;
    L_0x0075:
        r5 = new org.xmlpull.v1.XmlPullParserException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = r12.getPositionDescription();
        r6.append(r7);
        r7 = ": <item> tag requires a 'drawable' attribute or child tag defining a drawable";
        r6.append(r7);
        r6 = r6.toString();
        r5.<init>(r6);
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.graphics.drawable.AnimatedStateListDrawableCompat.parseItem(android.content.Context, android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):int");
    }

    private int parseTransition(@android.support.annotation.NonNull android.content.Context r11, @android.support.annotation.NonNull android.content.res.Resources r12, @android.support.annotation.NonNull org.xmlpull.v1.XmlPullParser r13, @android.support.annotation.NonNull android.util.AttributeSet r14, @android.support.annotation.Nullable android.content.res.Resources.Theme r15) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:28:0x00b7 in {2, 7, 12, 15, 16, 18, 23, 25, 27} preds:[]
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
        r10 = this;
        r0 = android.support.v7.appcompat.C0185R.styleable.AnimatedStateListDrawableTransition;
        r0 = android.support.v4.content.res.TypedArrayUtils.obtainAttributes(r12, r15, r14, r0);
        r1 = android.support.v7.appcompat.C0185R.styleable.AnimatedStateListDrawableTransition_android_fromId;
        r2 = -1;
        r1 = r0.getResourceId(r1, r2);
        r3 = android.support.v7.appcompat.C0185R.styleable.AnimatedStateListDrawableTransition_android_toId;
        r3 = r0.getResourceId(r3, r2);
        r4 = 0;
        r5 = android.support.v7.appcompat.C0185R.styleable.AnimatedStateListDrawableTransition_android_drawable;
        r5 = r0.getResourceId(r5, r2);
        if (r5 <= 0) goto L_0x0020;
    L_0x001c:
        r4 = android.support.v7.content.res.AppCompatResources.getDrawable(r11, r5);
    L_0x0020:
        r6 = android.support.v7.appcompat.C0185R.styleable.AnimatedStateListDrawableTransition_android_reversible;
        r7 = 0;
        r6 = r0.getBoolean(r6, r7);
        r0.recycle();
        if (r4 != 0) goto L_0x0074;
    L_0x002c:
        r7 = r13.next();
        r8 = r7;
        r9 = 4;
        if (r7 != r9) goto L_0x0035;
    L_0x0034:
        goto L_0x002c;
    L_0x0035:
        r7 = 2;
        if (r8 != r7) goto L_0x0059;
    L_0x0038:
        r7 = r13.getName();
        r9 = "animated-vector";
        r7 = r7.equals(r9);
        if (r7 == 0) goto L_0x0049;
    L_0x0044:
        r4 = android.support.graphics.drawable.AnimatedVectorDrawableCompat.createFromXmlInner(r11, r12, r13, r14, r15);
        goto L_0x0074;
    L_0x0049:
        r7 = android.os.Build.VERSION.SDK_INT;
        r9 = 21;
        if (r7 < r9) goto L_0x0054;
    L_0x004f:
        r4 = android.graphics.drawable.Drawable.createFromXmlInner(r12, r13, r14, r15);
        goto L_0x0074;
    L_0x0054:
        r4 = android.graphics.drawable.Drawable.createFromXmlInner(r12, r13, r14);
        goto L_0x0074;
    L_0x0059:
        r2 = new org.xmlpull.v1.XmlPullParserException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r9 = r13.getPositionDescription();
        r7.append(r9);
        r9 = ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable";
        r7.append(r9);
        r7 = r7.toString();
        r2.<init>(r7);
        throw r2;
    L_0x0074:
        if (r4 == 0) goto L_0x009c;
    L_0x0076:
        if (r1 == r2) goto L_0x0081;
    L_0x0078:
        if (r3 == r2) goto L_0x0081;
    L_0x007a:
        r2 = r10.mState;
        r2 = r2.addTransition(r1, r3, r4, r6);
        return r2;
    L_0x0081:
        r2 = new org.xmlpull.v1.XmlPullParserException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r13.getPositionDescription();
        r7.append(r8);
        r8 = ": <transition> tag requires 'fromId' & 'toId' attributes";
        r7.append(r8);
        r7 = r7.toString();
        r2.<init>(r7);
        throw r2;
    L_0x009c:
        r2 = new org.xmlpull.v1.XmlPullParserException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r13.getPositionDescription();
        r7.append(r8);
        r8 = ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable";
        r7.append(r8);
        r7 = r7.toString();
        r2.<init>(r7);
        throw r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.graphics.drawable.AnimatedStateListDrawableCompat.parseTransition(android.content.Context, android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):int");
    }

    public /* bridge */ /* synthetic */ void addState(int[] iArr, Drawable drawable) {
        super.addState(iArr, drawable);
    }

    @RequiresApi(21)
    public /* bridge */ /* synthetic */ void applyTheme(@NonNull Theme theme) {
        super.applyTheme(theme);
    }

    @RequiresApi(21)
    public /* bridge */ /* synthetic */ boolean canApplyTheme() {
        return super.canApplyTheme();
    }

    public /* bridge */ /* synthetic */ void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
    }

    public /* bridge */ /* synthetic */ int getAlpha() {
        return super.getAlpha();
    }

    public /* bridge */ /* synthetic */ int getChangingConfigurations() {
        return super.getChangingConfigurations();
    }

    @NonNull
    public /* bridge */ /* synthetic */ Drawable getCurrent() {
        return super.getCurrent();
    }

    public /* bridge */ /* synthetic */ void getHotspotBounds(@NonNull Rect rect) {
        super.getHotspotBounds(rect);
    }

    public /* bridge */ /* synthetic */ int getIntrinsicHeight() {
        return super.getIntrinsicHeight();
    }

    public /* bridge */ /* synthetic */ int getIntrinsicWidth() {
        return super.getIntrinsicWidth();
    }

    public /* bridge */ /* synthetic */ int getMinimumHeight() {
        return super.getMinimumHeight();
    }

    public /* bridge */ /* synthetic */ int getMinimumWidth() {
        return super.getMinimumWidth();
    }

    public /* bridge */ /* synthetic */ int getOpacity() {
        return super.getOpacity();
    }

    @RequiresApi(21)
    public /* bridge */ /* synthetic */ void getOutline(@NonNull Outline outline) {
        super.getOutline(outline);
    }

    public /* bridge */ /* synthetic */ boolean getPadding(@NonNull Rect rect) {
        return super.getPadding(rect);
    }

    public /* bridge */ /* synthetic */ void invalidateDrawable(@NonNull Drawable drawable) {
        super.invalidateDrawable(drawable);
    }

    public /* bridge */ /* synthetic */ boolean isAutoMirrored() {
        return super.isAutoMirrored();
    }

    public /* bridge */ /* synthetic */ boolean onLayoutDirectionChanged(int i) {
        return super.onLayoutDirectionChanged(i);
    }

    public /* bridge */ /* synthetic */ void scheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable, long j) {
        super.scheduleDrawable(drawable, runnable, j);
    }

    public /* bridge */ /* synthetic */ void setAlpha(int i) {
        super.setAlpha(i);
    }

    public /* bridge */ /* synthetic */ void setAutoMirrored(boolean z) {
        super.setAutoMirrored(z);
    }

    public /* bridge */ /* synthetic */ void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
    }

    public /* bridge */ /* synthetic */ void setDither(boolean z) {
        super.setDither(z);
    }

    public /* bridge */ /* synthetic */ void setEnterFadeDuration(int i) {
        super.setEnterFadeDuration(i);
    }

    public /* bridge */ /* synthetic */ void setExitFadeDuration(int i) {
        super.setExitFadeDuration(i);
    }

    public /* bridge */ /* synthetic */ void setHotspot(float f, float f2) {
        super.setHotspot(f, f2);
    }

    public /* bridge */ /* synthetic */ void setHotspotBounds(int i, int i2, int i3, int i4) {
        super.setHotspotBounds(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ void setTintList(ColorStateList colorStateList) {
        super.setTintList(colorStateList);
    }

    public /* bridge */ /* synthetic */ void setTintMode(@NonNull Mode mode) {
        super.setTintMode(mode);
    }

    public /* bridge */ /* synthetic */ void unscheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable) {
        super.unscheduleDrawable(drawable, runnable);
    }

    public AnimatedStateListDrawableCompat() {
        this(null, null);
    }

    AnimatedStateListDrawableCompat(@Nullable AnimatedStateListState state, @Nullable Resources res) {
        super(null);
        this.mTransitionToIndex = -1;
        this.mTransitionFromIndex = -1;
        setConstantState(new AnimatedStateListState(state, this, res));
        onStateChange(getState());
        jumpToCurrentState();
    }

    @Nullable
    public static AnimatedStateListDrawableCompat create(@NonNull Context context, @DrawableRes int resId, @Nullable Theme theme) {
        try {
            int type;
            Resources res = context.getResources();
            XmlPullParser parser = res.getXml(resId);
            AttributeSet attrs = Xml.asAttributeSet(parser);
            while (true) {
                int next = parser.next();
                type = next;
                if (next == 2 || type == 1) {
                    if (type == 2) {
                        return createFromXmlInner(context, res, parser, attrs, theme);
                    }
                    throw new XmlPullParserException("No start tag found");
                }
            }
            if (type == 2) {
                return createFromXmlInner(context, res, parser, attrs, theme);
            }
            throw new XmlPullParserException("No start tag found");
        } catch (XmlPullParserException e) {
            Log.e(LOGTAG, "parser error", e);
            return null;
        } catch (IOException e2) {
            Log.e(LOGTAG, "parser error", e2);
            return null;
        }
    }

    public static AnimatedStateListDrawableCompat createFromXmlInner(@NonNull Context context, @NonNull Resources resources, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws IOException, XmlPullParserException {
        String name = parser.getName();
        if (name.equals("animated-selector")) {
            AnimatedStateListDrawableCompat asl = new AnimatedStateListDrawableCompat();
            asl.inflate(context, resources, parser, attrs, theme);
            return asl;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parser.getPositionDescription());
        stringBuilder.append(": invalid animated-selector tag ");
        stringBuilder.append(name);
        throw new XmlPullParserException(stringBuilder.toString());
    }

    public void inflate(@NonNull Context context, @NonNull Resources resources, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        TypedArray a = TypedArrayUtils.obtainAttributes(resources, theme, attrs, C0185R.styleable.AnimatedStateListDrawableCompat);
        setVisible(a.getBoolean(C0185R.styleable.AnimatedStateListDrawableCompat_android_visible, true), true);
        updateStateFromTypedArray(a);
        updateDensity(resources);
        a.recycle();
        inflateChildElements(context, resources, parser, attrs, theme);
        init();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (this.mTransition != null && (changed || restart)) {
            if (visible) {
                this.mTransition.start();
            } else {
                jumpToCurrentState();
            }
        }
        return changed;
    }

    public void addState(@NonNull int[] stateSet, @NonNull Drawable drawable, int id) {
        if (drawable != null) {
            this.mState.addStateSet(stateSet, drawable, id);
            onStateChange(getState());
            return;
        }
        throw new IllegalArgumentException("Drawable must not be null");
    }

    public <T extends Drawable & Animatable> void addTransition(int fromId, int toId, @NonNull T transition, boolean reversible) {
        if (transition != null) {
            this.mState.addTransition(fromId, toId, transition, reversible);
            return;
        }
        throw new IllegalArgumentException("Transition drawable must not be null");
    }

    public boolean isStateful() {
        return true;
    }

    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        Transition transition = this.mTransition;
        if (transition != null) {
            transition.stop();
            this.mTransition = null;
            selectDrawable(this.mTransitionToIndex);
            this.mTransitionToIndex = -1;
            this.mTransitionFromIndex = -1;
        }
    }

    protected boolean onStateChange(int[] stateSet) {
        int targetIndex = this.mState.indexOfKeyframe(stateSet);
        boolean changed = targetIndex != getCurrentIndex() && (selectTransition(targetIndex) || selectDrawable(targetIndex));
        Drawable current = getCurrent();
        if (current != null) {
            return changed | current.setState(stateSet);
        }
        return changed;
    }

    private boolean selectTransition(int toIndex) {
        int fromIndex;
        Transition currentTransition = this.mTransition;
        if (currentTransition == null) {
            fromIndex = getCurrentIndex();
        } else if (toIndex == this.mTransitionToIndex) {
            return true;
        } else {
            if (toIndex == this.mTransitionFromIndex && currentTransition.canReverse()) {
                currentTransition.reverse();
                this.mTransitionToIndex = this.mTransitionFromIndex;
                this.mTransitionFromIndex = toIndex;
                return true;
            }
            fromIndex = this.mTransitionToIndex;
            currentTransition.stop();
        }
        this.mTransition = null;
        this.mTransitionFromIndex = -1;
        this.mTransitionToIndex = -1;
        AnimatedStateListState state = this.mState;
        int fromId = state.getKeyframeIdAt(fromIndex);
        int toId = state.getKeyframeIdAt(toIndex);
        if (toId != 0) {
            if (fromId != 0) {
                int transitionIndex = state.indexOfTransition(fromId, toId);
                if (transitionIndex < 0) {
                    return false;
                }
                Transition transition;
                boolean hasReversibleFlag = state.transitionHasReversibleFlag(fromId, toId);
                selectDrawable(transitionIndex);
                Drawable d = getCurrent();
                if (d instanceof AnimationDrawable) {
                    transition = new AnimationDrawableTransition((AnimationDrawable) d, state.isTransitionReversed(fromId, toId), hasReversibleFlag);
                } else if (d instanceof AnimatedVectorDrawableCompat) {
                    transition = new AnimatedVectorDrawableTransition((AnimatedVectorDrawableCompat) d);
                } else if (!(d instanceof Animatable)) {
                    return false;
                } else {
                    transition = new AnimatableTransition((Animatable) d);
                }
                transition.start();
                this.mTransition = transition;
                this.mTransitionFromIndex = fromIndex;
                this.mTransitionToIndex = toIndex;
                return true;
            }
        }
        return false;
    }

    private void updateStateFromTypedArray(TypedArray a) {
        AnimatedStateListState state = this.mState;
        if (VERSION.SDK_INT >= 21) {
            state.mChangingConfigurations |= a.getChangingConfigurations();
        }
        state.setVariablePadding(a.getBoolean(C0185R.styleable.AnimatedStateListDrawableCompat_android_variablePadding, state.mVariablePadding));
        state.setConstantSize(a.getBoolean(C0185R.styleable.AnimatedStateListDrawableCompat_android_constantSize, state.mConstantSize));
        state.setEnterFadeDuration(a.getInt(C0185R.styleable.AnimatedStateListDrawableCompat_android_enterFadeDuration, state.mEnterFadeDuration));
        state.setExitFadeDuration(a.getInt(C0185R.styleable.AnimatedStateListDrawableCompat_android_exitFadeDuration, state.mExitFadeDuration));
        setDither(a.getBoolean(C0185R.styleable.AnimatedStateListDrawableCompat_android_dither, state.mDither));
    }

    private void init() {
        onStateChange(getState());
    }

    private void inflateChildElements(@NonNull Context context, @NonNull Resources resources, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            int next = parser.next();
            int type = next;
            if (next != 1) {
                next = parser.getDepth();
                int depth = next;
                if (next < innerDepth && type == 3) {
                    return;
                }
                if (type == 2) {
                    if (depth <= innerDepth) {
                        if (parser.getName().equals(ELEMENT_ITEM)) {
                            parseItem(context, resources, parser, attrs, theme);
                        } else if (parser.getName().equals(ELEMENT_TRANSITION)) {
                            parseTransition(context, resources, parser, attrs, theme);
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    AnimatedStateListState cloneConstantState() {
        return new AnimatedStateListState(this.mState, this, null);
    }

    void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    protected void setConstantState(@NonNull DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof AnimatedStateListState) {
            this.mState = (AnimatedStateListState) state;
        }
    }
}
