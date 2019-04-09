package android.support.v4.text;

import android.os.Build.VERSION;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.UiThread;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.util.Preconditions;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class PrecomputedTextCompat implements Spannable {
    private static final char LINE_FEED = '\n';
    @GuardedBy("sLock")
    @NonNull
    private static Executor sExecutor = null;
    private static final Object sLock = new Object();
    @NonNull
    private final int[] mParagraphEnds;
    @NonNull
    private final Params mParams;
    @NonNull
    private final Spannable mText;
    @Nullable
    private final PrecomputedText mWrapped;

    public static final class Params {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        @NonNull
        private final TextPaint mPaint;
        @Nullable
        private final TextDirectionHeuristic mTextDir;
        final android.text.PrecomputedText.Params mWrapped;

        public static class Builder {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            @NonNull
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;

            public Builder(@NonNull TextPaint paint) {
                this.mPaint = paint;
                if (VERSION.SDK_INT >= 23) {
                    this.mBreakStrategy = 1;
                    this.mHyphenationFrequency = 1;
                } else {
                    this.mHyphenationFrequency = 0;
                    this.mBreakStrategy = 0;
                }
                if (VERSION.SDK_INT >= 18) {
                    this.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                } else {
                    this.mTextDir = null;
                }
            }

            @RequiresApi(23)
            public Builder setBreakStrategy(int strategy) {
                this.mBreakStrategy = strategy;
                return this;
            }

            @RequiresApi(23)
            public Builder setHyphenationFrequency(int frequency) {
                this.mHyphenationFrequency = frequency;
                return this;
            }

            @RequiresApi(18)
            public Builder setTextDirection(@NonNull TextDirectionHeuristic textDir) {
                this.mTextDir = textDir;
                return this;
            }

            @NonNull
            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
        }

        Params(@NonNull TextPaint paint, @NonNull TextDirectionHeuristic textDir, int strategy, int frequency) {
            if (VERSION.SDK_INT >= 28) {
                this.mWrapped = new android.text.PrecomputedText.Params.Builder(paint).setBreakStrategy(strategy).setHyphenationFrequency(frequency).setTextDirection(textDir).build();
            } else {
                this.mWrapped = null;
            }
            this.mPaint = paint;
            this.mTextDir = textDir;
            this.mBreakStrategy = strategy;
            this.mHyphenationFrequency = frequency;
        }

        @RequiresApi(28)
        public Params(@NonNull android.text.PrecomputedText.Params wrapped) {
            this.mPaint = wrapped.getTextPaint();
            this.mTextDir = wrapped.getTextDirection();
            this.mBreakStrategy = wrapped.getBreakStrategy();
            this.mHyphenationFrequency = wrapped.getHyphenationFrequency();
            this.mWrapped = wrapped;
        }

        @NonNull
        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        @Nullable
        @RequiresApi(18)
        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        @RequiresApi(23)
        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }

        @RequiresApi(23)
        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }

        public boolean equals(@Nullable Object o) {
            if (o == this) {
                return true;
            }
            if (o != null) {
                if (o instanceof Params) {
                    Params other = (Params) o;
                    android.text.PrecomputedText.Params params = this.mWrapped;
                    if (params != null) {
                        return params.equals(other.mWrapped);
                    }
                    if (VERSION.SDK_INT >= 23 && (this.mBreakStrategy != other.getBreakStrategy() || this.mHyphenationFrequency != other.getHyphenationFrequency())) {
                        return false;
                    }
                    if ((VERSION.SDK_INT >= 18 && this.mTextDir != other.getTextDirection()) || this.mPaint.getTextSize() != other.getTextPaint().getTextSize() || this.mPaint.getTextScaleX() != other.getTextPaint().getTextScaleX() || this.mPaint.getTextSkewX() != other.getTextPaint().getTextSkewX()) {
                        return false;
                    }
                    if ((VERSION.SDK_INT >= 21 && (this.mPaint.getLetterSpacing() != other.getTextPaint().getLetterSpacing() || !TextUtils.equals(this.mPaint.getFontFeatureSettings(), other.getTextPaint().getFontFeatureSettings()))) || this.mPaint.getFlags() != other.getTextPaint().getFlags()) {
                        return false;
                    }
                    if (VERSION.SDK_INT >= 24) {
                        if (!this.mPaint.getTextLocales().equals(other.getTextPaint().getTextLocales())) {
                            return false;
                        }
                    } else if (VERSION.SDK_INT >= 17 && !this.mPaint.getTextLocale().equals(other.getTextPaint().getTextLocale())) {
                        return false;
                    }
                    if (this.mPaint.getTypeface() == null) {
                        if (other.getTextPaint().getTypeface() != null) {
                            return false;
                        }
                    } else if (!this.mPaint.getTypeface().equals(other.getTextPaint().getTypeface())) {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            if (VERSION.SDK_INT >= 24) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), Boolean.valueOf(this.mPaint.isElegantTextHeight()), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            } else if (VERSION.SDK_INT >= 21) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), Boolean.valueOf(this.mPaint.isElegantTextHeight()), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            } else if (VERSION.SDK_INT >= 18) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            } else if (VERSION.SDK_INT >= 17) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            } else {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("textSize=");
            stringBuilder.append(this.mPaint.getTextSize());
            sb.append(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(", textScaleX=");
            stringBuilder.append(this.mPaint.getTextScaleX());
            sb.append(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(", textSkewX=");
            stringBuilder.append(this.mPaint.getTextSkewX());
            sb.append(stringBuilder.toString());
            if (VERSION.SDK_INT >= 21) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", letterSpacing=");
                stringBuilder.append(this.mPaint.getLetterSpacing());
                sb.append(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append(", elegantTextHeight=");
                stringBuilder.append(this.mPaint.isElegantTextHeight());
                sb.append(stringBuilder.toString());
            }
            if (VERSION.SDK_INT >= 24) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", textLocale=");
                stringBuilder.append(this.mPaint.getTextLocales());
                sb.append(stringBuilder.toString());
            } else if (VERSION.SDK_INT >= 17) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", textLocale=");
                stringBuilder.append(this.mPaint.getTextLocale());
                sb.append(stringBuilder.toString());
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(", typeface=");
            stringBuilder.append(this.mPaint.getTypeface());
            sb.append(stringBuilder.toString());
            if (VERSION.SDK_INT >= 26) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", variationSettings=");
                stringBuilder.append(this.mPaint.getFontVariationSettings());
                sb.append(stringBuilder.toString());
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(", textDir=");
            stringBuilder.append(this.mTextDir);
            sb.append(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(", breakStrategy=");
            stringBuilder.append(this.mBreakStrategy);
            sb.append(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(", hyphenationFrequency=");
            stringBuilder.append(this.mHyphenationFrequency);
            sb.append(stringBuilder.toString());
            sb.append("}");
            return sb.toString();
        }
    }

    private static class PrecomputedTextFutureTask extends FutureTask<PrecomputedTextCompat> {

        private static class PrecomputedTextCallback implements Callable<PrecomputedTextCompat> {
            private Params mParams;
            private CharSequence mText;

            PrecomputedTextCallback(@NonNull Params params, @NonNull CharSequence cs) {
                this.mParams = params;
                this.mText = cs;
            }

            public PrecomputedTextCompat call() throws Exception {
                return PrecomputedTextCompat.create(this.mText, this.mParams);
            }
        }

        PrecomputedTextFutureTask(@NonNull Params params, @NonNull CharSequence text) {
            super(new PrecomputedTextCallback(params, text));
        }
    }

    public static android.support.v4.text.PrecomputedTextCompat create(@android.support.annotation.NonNull java.lang.CharSequence r14, @android.support.annotation.NonNull android.support.v4.text.PrecomputedTextCompat.Params r15) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:34:0x00df in {8, 14, 15, 16, 20, 23, 26, 27, 30, 33} preds:[]
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
        android.support.v4.util.Preconditions.checkNotNull(r14);
        android.support.v4.util.Preconditions.checkNotNull(r15);
        r0 = "PrecomputedText";	 Catch:{ all -> 0x00d9 }
        android.support.v4.os.TraceCompat.beginSection(r0);	 Catch:{ all -> 0x00d9 }
        r0 = android.os.Build.VERSION.SDK_INT;	 Catch:{ all -> 0x00d9 }
        r1 = 28;	 Catch:{ all -> 0x00d9 }
        if (r0 < r1) goto L_0x0024;	 Catch:{ all -> 0x00d9 }
    L_0x0011:
        r0 = r15.mWrapped;	 Catch:{ all -> 0x00d9 }
        if (r0 == 0) goto L_0x0024;	 Catch:{ all -> 0x00d9 }
    L_0x0015:
        r0 = new android.support.v4.text.PrecomputedTextCompat;	 Catch:{ all -> 0x00d9 }
        r1 = r15.mWrapped;	 Catch:{ all -> 0x00d9 }
        r1 = android.text.PrecomputedText.create(r14, r1);	 Catch:{ all -> 0x00d9 }
        r0.<init>(r1, r15);	 Catch:{ all -> 0x00d9 }
        android.support.v4.os.TraceCompat.endSection();
        return r0;
    L_0x0024:
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x00d9 }
        r0.<init>();	 Catch:{ all -> 0x00d9 }
        r1 = 0;	 Catch:{ all -> 0x00d9 }
        r2 = r14.length();	 Catch:{ all -> 0x00d9 }
        r3 = 0;	 Catch:{ all -> 0x00d9 }
    L_0x002f:
        if (r3 >= r2) goto L_0x0047;	 Catch:{ all -> 0x00d9 }
    L_0x0031:
        r4 = 10;	 Catch:{ all -> 0x00d9 }
        r4 = android.text.TextUtils.indexOf(r14, r4, r3, r2);	 Catch:{ all -> 0x00d9 }
        r1 = r4;	 Catch:{ all -> 0x00d9 }
        if (r1 >= 0) goto L_0x003c;	 Catch:{ all -> 0x00d9 }
    L_0x003a:
        r1 = r2;	 Catch:{ all -> 0x00d9 }
        goto L_0x003e;	 Catch:{ all -> 0x00d9 }
    L_0x003c:
        r1 = r1 + 1;	 Catch:{ all -> 0x00d9 }
    L_0x003e:
        r4 = java.lang.Integer.valueOf(r1);	 Catch:{ all -> 0x00d9 }
        r0.add(r4);	 Catch:{ all -> 0x00d9 }
        r3 = r1;	 Catch:{ all -> 0x00d9 }
        goto L_0x002f;	 Catch:{ all -> 0x00d9 }
    L_0x0047:
        r3 = r0.size();	 Catch:{ all -> 0x00d9 }
        r3 = new int[r3];	 Catch:{ all -> 0x00d9 }
        r4 = 0;	 Catch:{ all -> 0x00d9 }
        r5 = r4;	 Catch:{ all -> 0x00d9 }
        r6 = r0.size();	 Catch:{ all -> 0x00d9 }
        if (r5 >= r6) goto L_0x006c;	 Catch:{ all -> 0x00d9 }
        r6 = r0.get(r5);	 Catch:{ all -> 0x00d9 }
        r6 = (java.lang.Integer) r6;	 Catch:{ all -> 0x00d9 }
        r6 = r6.intValue();	 Catch:{ all -> 0x00d9 }
        r3[r5] = r6;	 Catch:{ all -> 0x00d9 }
        r5 = r5 + 1;	 Catch:{ all -> 0x00d9 }
        goto L_0x0050;	 Catch:{ all -> 0x00d9 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ all -> 0x00d9 }
        r6 = 23;	 Catch:{ all -> 0x00d9 }
        if (r5 < r6) goto L_0x00ab;	 Catch:{ all -> 0x00d9 }
        r5 = r14.length();	 Catch:{ all -> 0x00d9 }
        r6 = r15.getTextPaint();	 Catch:{ all -> 0x00d9 }
        r7 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;	 Catch:{ all -> 0x00d9 }
        r4 = android.text.StaticLayout.Builder.obtain(r14, r4, r5, r6, r7);	 Catch:{ all -> 0x00d9 }
        r5 = r15.getBreakStrategy();	 Catch:{ all -> 0x00d9 }
        r4 = r4.setBreakStrategy(r5);	 Catch:{ all -> 0x00d9 }
        r5 = r15.getHyphenationFrequency();	 Catch:{ all -> 0x00d9 }
        r4 = r4.setHyphenationFrequency(r5);	 Catch:{ all -> 0x00d9 }
        r5 = r15.getTextDirection();	 Catch:{ all -> 0x00d9 }
        r4 = r4.setTextDirection(r5);	 Catch:{ all -> 0x00d9 }
        r4.build();	 Catch:{ all -> 0x00d9 }
        goto L_0x00cd;	 Catch:{ all -> 0x00d9 }
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ all -> 0x00d9 }
        r5 = 21;	 Catch:{ all -> 0x00d9 }
        if (r4 < r5) goto L_0x00cc;	 Catch:{ all -> 0x00d9 }
        r6 = new android.text.StaticLayout;	 Catch:{ all -> 0x00d9 }
        r8 = r15.getTextPaint();	 Catch:{ all -> 0x00d9 }
        r9 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;	 Catch:{ all -> 0x00d9 }
        r10 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ all -> 0x00d9 }
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ all -> 0x00d9 }
        r12 = 0;	 Catch:{ all -> 0x00d9 }
        r13 = 0;	 Catch:{ all -> 0x00d9 }
        r7 = r14;	 Catch:{ all -> 0x00d9 }
        r6.<init>(r7, r8, r9, r10, r11, r12, r13);	 Catch:{ all -> 0x00d9 }
        goto L_0x00cd;	 Catch:{ all -> 0x00d9 }
        r4 = new android.support.v4.text.PrecomputedTextCompat;	 Catch:{ all -> 0x00d9 }
        r4.<init>(r14, r15, r3);	 Catch:{ all -> 0x00d9 }
        android.support.v4.os.TraceCompat.endSection();
        return r4;
    L_0x00d9:
        r0 = move-exception;
        android.support.v4.os.TraceCompat.endSection();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.text.PrecomputedTextCompat.create(java.lang.CharSequence, android.support.v4.text.PrecomputedTextCompat$Params):android.support.v4.text.PrecomputedTextCompat");
    }

    private int findParaIndex(@android.support.annotation.IntRange(from = 0) int r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:9:0x0034 in {5, 6, 8} preds:[]
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
        r4 = this;
        r0 = 0;
    L_0x0001:
        r1 = r4.mParagraphEnds;
        r2 = r1.length;
        if (r0 >= r2) goto L_0x000e;
    L_0x0006:
        r1 = r1[r0];
        if (r5 >= r1) goto L_0x000b;
    L_0x000a:
        return r0;
    L_0x000b:
        r0 = r0 + 1;
        goto L_0x0001;
    L_0x000e:
        r0 = new java.lang.IndexOutOfBoundsException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "pos must be less than ";
        r1.append(r2);
        r2 = r4.mParagraphEnds;
        r3 = r2.length;
        r3 = r3 + -1;
        r2 = r2[r3];
        r1.append(r2);
        r2 = ", gave ";
        r1.append(r2);
        r1.append(r5);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.text.PrecomputedTextCompat.findParaIndex(int):int");
    }

    private PrecomputedTextCompat(@NonNull CharSequence text, @NonNull Params params, @NonNull int[] paraEnds) {
        this.mText = new SpannableString(text);
        this.mParams = params;
        this.mParagraphEnds = paraEnds;
        this.mWrapped = null;
    }

    @RequiresApi(28)
    private PrecomputedTextCompat(@NonNull PrecomputedText precomputed, @NonNull Params params) {
        this.mText = precomputed;
        this.mParams = params;
        this.mParagraphEnds = null;
        this.mWrapped = precomputed;
    }

    @Nullable
    @RequiresApi(28)
    @RestrictTo({Scope.LIBRARY_GROUP})
    public PrecomputedText getPrecomputedText() {
        Spannable spannable = this.mText;
        if (spannable instanceof PrecomputedText) {
            return (PrecomputedText) spannable;
        }
        return null;
    }

    @NonNull
    public Params getParams() {
        return this.mParams;
    }

    @IntRange(from = 0)
    public int getParagraphCount() {
        if (VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphCount();
        }
        return this.mParagraphEnds.length;
    }

    @IntRange(from = 0)
    public int getParagraphStart(@IntRange(from = 0) int paraIndex) {
        int i = 0;
        Preconditions.checkArgumentInRange(paraIndex, 0, getParagraphCount(), "paraIndex");
        if (VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphStart(paraIndex);
        }
        if (paraIndex != 0) {
            i = this.mParagraphEnds[paraIndex - 1];
        }
        return i;
    }

    @IntRange(from = 0)
    public int getParagraphEnd(@IntRange(from = 0) int paraIndex) {
        Preconditions.checkArgumentInRange(paraIndex, 0, getParagraphCount(), "paraIndex");
        if (VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphEnd(paraIndex);
        }
        return this.mParagraphEnds[paraIndex];
    }

    @UiThread
    public static Future<PrecomputedTextCompat> getTextFuture(@NonNull CharSequence charSequence, @NonNull Params params, @Nullable Executor executor) {
        PrecomputedTextFutureTask task = new PrecomputedTextFutureTask(params, charSequence);
        if (executor == null) {
            synchronized (sLock) {
                if (sExecutor == null) {
                    sExecutor = Executors.newFixedThreadPool(1);
                }
                executor = sExecutor;
            }
        }
        executor.execute(task);
        return task;
    }

    public void setSpan(Object what, int start, int end, int flags) {
        if (what instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
        } else if (VERSION.SDK_INT >= 28) {
            this.mWrapped.setSpan(what, start, end, flags);
        } else {
            this.mText.setSpan(what, start, end, flags);
        }
    }

    public void removeSpan(Object what) {
        if (what instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
        } else if (VERSION.SDK_INT >= 28) {
            this.mWrapped.removeSpan(what);
        } else {
            this.mText.removeSpan(what);
        }
    }

    public <T> T[] getSpans(int start, int end, Class<T> type) {
        if (VERSION.SDK_INT >= 28) {
            return this.mWrapped.getSpans(start, end, type);
        }
        return this.mText.getSpans(start, end, type);
    }

    public int getSpanStart(Object tag) {
        return this.mText.getSpanStart(tag);
    }

    public int getSpanEnd(Object tag) {
        return this.mText.getSpanEnd(tag);
    }

    public int getSpanFlags(Object tag) {
        return this.mText.getSpanFlags(tag);
    }

    public int nextSpanTransition(int start, int limit, Class type) {
        return this.mText.nextSpanTransition(start, limit, type);
    }

    public int length() {
        return this.mText.length();
    }

    public char charAt(int index) {
        return this.mText.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return this.mText.subSequence(start, end);
    }

    public String toString() {
        return this.mText.toString();
    }
}
