package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v7.appcompat.C0185R;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class AppCompatTextViewAutoSizeHelper {
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
    private static final String TAG = "ACTVAutoSizeHelper";
    private static final RectF TEMP_RECTF = new RectF();
    static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0f;
    private static final int VERY_WIDE = 1048576;
    private static ConcurrentHashMap<String, Method> sTextViewMethodByNameCache = new ConcurrentHashMap();
    private float mAutoSizeMaxTextSizeInPx = -1.0f;
    private float mAutoSizeMinTextSizeInPx = -1.0f;
    private float mAutoSizeStepGranularityInPx = -1.0f;
    private int[] mAutoSizeTextSizesInPx = new int[0];
    private int mAutoSizeTextType = 0;
    private final Context mContext;
    private boolean mHasPresetAutoSizeValues = false;
    private boolean mNeedsAutoSizeText = false;
    private TextPaint mTempTextPaint;
    private final TextView mTextView;

    private int findLargestTextSizeWhichFits(android.graphics.RectF r7) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x002f in {6, 7, 9, 11} preds:[]
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
        r6 = this;
        r0 = r6.mAutoSizeTextSizesInPx;
        r0 = r0.length;
        if (r0 == 0) goto L_0x0027;
    L_0x0005:
        r1 = 0;
        r2 = r1 + 1;
        r3 = r0 + -1;
    L_0x000a:
        if (r2 > r3) goto L_0x0022;
    L_0x000c:
        r4 = r2 + r3;
        r4 = r4 / 2;
        r5 = r6.mAutoSizeTextSizesInPx;
        r5 = r5[r4];
        r5 = r6.suggestedSizeFitsInSpace(r5, r7);
        if (r5 == 0) goto L_0x001e;
    L_0x001a:
        r1 = r2;
        r2 = r4 + 1;
        goto L_0x000a;
    L_0x001e:
        r3 = r4 + -1;
        r1 = r3;
        goto L_0x000a;
    L_0x0022:
        r4 = r6.mAutoSizeTextSizesInPx;
        r4 = r4[r1];
        return r4;
    L_0x0027:
        r1 = new java.lang.IllegalStateException;
        r2 = "No available text sizes to choose from.";
        r1.<init>(r2);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AppCompatTextViewAutoSizeHelper.findLargestTextSizeWhichFits(android.graphics.RectF):int");
    }

    private <T> T invokeAndReturnWithDefault(@android.support.annotation.NonNull java.lang.Object r7, @android.support.annotation.NonNull java.lang.String r8, @android.support.annotation.NonNull T r9) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:22:0x0040 in {5, 6, 16, 17, 20, 21} preds:[]
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
        r6 = this;
        r0 = 0;
        r1 = 0;
        r2 = r6.getTextViewMethod(r8);	 Catch:{ Exception -> 0x0016 }
        r3 = 0;	 Catch:{ Exception -> 0x0016 }
        r3 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0016 }
        r3 = r2.invoke(r7, r3);	 Catch:{ Exception -> 0x0016 }
        r0 = r3;
        if (r0 != 0) goto L_0x0039;
    L_0x0010:
        if (r1 == 0) goto L_0x0039;
    L_0x0012:
        r0 = r9;
        goto L_0x0039;
    L_0x0014:
        r2 = move-exception;
        goto L_0x003a;
    L_0x0016:
        r2 = move-exception;
        r1 = 1;
        r3 = "ACTVAutoSizeHelper";	 Catch:{ all -> 0x0014 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0014 }
        r4.<init>();	 Catch:{ all -> 0x0014 }
        r5 = "Failed to invoke TextView#";	 Catch:{ all -> 0x0014 }
        r4.append(r5);	 Catch:{ all -> 0x0014 }
        r4.append(r8);	 Catch:{ all -> 0x0014 }
        r5 = "() method";	 Catch:{ all -> 0x0014 }
        r4.append(r5);	 Catch:{ all -> 0x0014 }
        r4 = r4.toString();	 Catch:{ all -> 0x0014 }
        android.util.Log.w(r3, r4, r2);	 Catch:{ all -> 0x0014 }
        if (r0 != 0) goto L_0x0039;
    L_0x0036:
        if (r1 == 0) goto L_0x0039;
    L_0x0038:
        goto L_0x0012;
    L_0x0039:
        return r0;
    L_0x003a:
        if (r0 != 0) goto L_0x003f;
    L_0x003c:
        if (r1 == 0) goto L_0x003f;
    L_0x003e:
        r0 = r9;
    L_0x003f:
        throw r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AppCompatTextViewAutoSizeHelper.invokeAndReturnWithDefault(java.lang.Object, java.lang.String, java.lang.Object):T");
    }

    AppCompatTextViewAutoSizeHelper(TextView textView) {
        this.mTextView = textView;
        this.mContext = this.mTextView.getContext();
    }

    void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        float autoSizeMinTextSizeInPx = -1.0f;
        float autoSizeMaxTextSizeInPx = -1.0f;
        float autoSizeStepGranularityInPx = -1.0f;
        TypedArray a = this.mContext.obtainStyledAttributes(attrs, C0185R.styleable.AppCompatTextView, defStyleAttr, 0);
        if (a.hasValue(C0185R.styleable.AppCompatTextView_autoSizeTextType)) {
            this.mAutoSizeTextType = a.getInt(C0185R.styleable.AppCompatTextView_autoSizeTextType, 0);
        }
        if (a.hasValue(C0185R.styleable.AppCompatTextView_autoSizeStepGranularity)) {
            autoSizeStepGranularityInPx = a.getDimension(C0185R.styleable.AppCompatTextView_autoSizeStepGranularity, -1.0f);
        }
        if (a.hasValue(C0185R.styleable.AppCompatTextView_autoSizeMinTextSize)) {
            autoSizeMinTextSizeInPx = a.getDimension(C0185R.styleable.AppCompatTextView_autoSizeMinTextSize, -1.0f);
        }
        if (a.hasValue(C0185R.styleable.AppCompatTextView_autoSizeMaxTextSize)) {
            autoSizeMaxTextSizeInPx = a.getDimension(C0185R.styleable.AppCompatTextView_autoSizeMaxTextSize, -1.0f);
        }
        if (a.hasValue(C0185R.styleable.AppCompatTextView_autoSizePresetSizes)) {
            int autoSizeStepSizeArrayResId = a.getResourceId(C0185R.styleable.AppCompatTextView_autoSizePresetSizes, 0);
            if (autoSizeStepSizeArrayResId > 0) {
                TypedArray autoSizePreDefTextSizes = a.getResources().obtainTypedArray(autoSizeStepSizeArrayResId);
                setupAutoSizeUniformPresetSizes(autoSizePreDefTextSizes);
                autoSizePreDefTextSizes.recycle();
            }
        }
        a.recycle();
        if (!supportsAutoSizeText()) {
            this.mAutoSizeTextType = 0;
        } else if (this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues) {
                DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                if (autoSizeMinTextSizeInPx == -1.0f) {
                    autoSizeMinTextSizeInPx = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                }
                if (autoSizeMaxTextSizeInPx == -1.0f) {
                    autoSizeMaxTextSizeInPx = TypedValue.applyDimension(2, 112.0f, displayMetrics);
                }
                if (autoSizeStepGranularityInPx == -1.0f) {
                    autoSizeStepGranularityInPx = 1.0f;
                }
                validateAndSetAutoSizeTextTypeUniformConfiguration(autoSizeMinTextSizeInPx, autoSizeMaxTextSizeInPx, autoSizeStepGranularityInPx);
            }
            setupAutoSizeText();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void setAutoSizeTextTypeWithDefaults(int autoSizeTextType) {
        if (supportsAutoSizeText()) {
            switch (autoSizeTextType) {
                case 0:
                    clearAutoSizeConfiguration();
                    return;
                case 1:
                    DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(2, 12.0f, displayMetrics), TypedValue.applyDimension(2, 112.0f, displayMetrics), 1.0f);
                    if (setupAutoSizeText()) {
                        autoSizeText();
                        return;
                    }
                    return;
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown auto-size text type: ");
                    stringBuilder.append(autoSizeTextType);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void setAutoSizeTextTypeUniformWithConfiguration(int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) throws IllegalArgumentException {
        if (supportsAutoSizeText()) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(unit, (float) autoSizeMinTextSize, displayMetrics), TypedValue.applyDimension(unit, (float) autoSizeMaxTextSize, displayMetrics), TypedValue.applyDimension(unit, (float) autoSizeStepGranularity, displayMetrics));
            if (setupAutoSizeText()) {
                autoSizeText();
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] presetSizes, int unit) throws IllegalArgumentException {
        if (supportsAutoSizeText()) {
            int presetSizesLength = presetSizes.length;
            if (presetSizesLength > 0) {
                int[] presetSizesInPx = new int[presetSizesLength];
                if (unit == 0) {
                    presetSizesInPx = Arrays.copyOf(presetSizes, presetSizesLength);
                } else {
                    DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    for (int i = 0; i < presetSizesLength; i++) {
                        presetSizesInPx[i] = Math.round(TypedValue.applyDimension(unit, (float) presetSizes[i], displayMetrics));
                    }
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(presetSizesInPx);
                if (!setupAutoSizeUniformPresetSizesConfiguration()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("None of the preset sizes is valid: ");
                    stringBuilder.append(Arrays.toString(presetSizes));
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            } else {
                this.mHasPresetAutoSizeValues = false;
            }
            if (setupAutoSizeText()) {
                autoSizeText();
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getAutoSizeStepGranularity() {
        return Math.round(this.mAutoSizeStepGranularityInPx);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getAutoSizeMinTextSize() {
        return Math.round(this.mAutoSizeMinTextSizeInPx);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int getAutoSizeMaxTextSize() {
        return Math.round(this.mAutoSizeMaxTextSizeInPx);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }

    private void setupAutoSizeUniformPresetSizes(TypedArray textSizes) {
        int textSizesLength = textSizes.length();
        int[] parsedSizes = new int[textSizesLength];
        if (textSizesLength > 0) {
            for (int i = 0; i < textSizesLength; i++) {
                parsedSizes[i] = textSizes.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(parsedSizes);
            setupAutoSizeUniformPresetSizesConfiguration();
        }
    }

    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        int sizesLength = this.mAutoSizeTextSizesInPx.length;
        this.mHasPresetAutoSizeValues = sizesLength > 0;
        if (this.mHasPresetAutoSizeValues) {
            this.mAutoSizeTextType = 1;
            int[] iArr = this.mAutoSizeTextSizesInPx;
            this.mAutoSizeMinTextSizeInPx = (float) iArr[0];
            this.mAutoSizeMaxTextSizeInPx = (float) iArr[sizesLength - 1];
            this.mAutoSizeStepGranularityInPx = -1.0f;
        }
        return this.mHasPresetAutoSizeValues;
    }

    private int[] cleanupAutoSizePresetSizes(int[] presetValues) {
        if (presetValuesLength == 0) {
            return presetValues;
        }
        int i;
        Arrays.sort(presetValues);
        List<Integer> uniqueValidSizes = new ArrayList();
        for (int currentPresetValue : presetValues) {
            if (currentPresetValue > 0 && Collections.binarySearch(uniqueValidSizes, Integer.valueOf(currentPresetValue)) < 0) {
                uniqueValidSizes.add(Integer.valueOf(currentPresetValue));
            }
        }
        if (presetValuesLength == uniqueValidSizes.size()) {
            return presetValues;
        }
        i = uniqueValidSizes.size();
        int[] cleanedUpSizes = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            cleanedUpSizes[i2] = ((Integer) uniqueValidSizes.get(i2)).intValue();
        }
        return cleanedUpSizes;
    }

    private void validateAndSetAutoSizeTextTypeUniformConfiguration(float autoSizeMinTextSizeInPx, float autoSizeMaxTextSizeInPx, float autoSizeStepGranularityInPx) throws IllegalArgumentException {
        StringBuilder stringBuilder;
        if (autoSizeMinTextSizeInPx <= 0.0f) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Minimum auto-size text size (");
            stringBuilder.append(autoSizeMinTextSizeInPx);
            stringBuilder.append("px) is less or equal to (0px)");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (autoSizeMaxTextSizeInPx <= autoSizeMinTextSizeInPx) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Maximum auto-size text size (");
            stringBuilder.append(autoSizeMaxTextSizeInPx);
            stringBuilder.append("px) is less or equal to minimum auto-size ");
            stringBuilder.append("text size (");
            stringBuilder.append(autoSizeMinTextSizeInPx);
            stringBuilder.append("px)");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (autoSizeStepGranularityInPx > 0.0f) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = autoSizeMinTextSizeInPx;
            this.mAutoSizeMaxTextSizeInPx = autoSizeMaxTextSizeInPx;
            this.mAutoSizeStepGranularityInPx = autoSizeStepGranularityInPx;
            this.mHasPresetAutoSizeValues = false;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("The auto-size step granularity (");
            stringBuilder.append(autoSizeStepGranularityInPx);
            stringBuilder.append("px) is less or equal to (0px)");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private boolean setupAutoSizeText() {
        if (supportsAutoSizeText() && this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                int autoSizeValuesLength = 1;
                float currentSize = (float) Math.round(this.mAutoSizeMinTextSizeInPx);
                while (Math.round(this.mAutoSizeStepGranularityInPx + currentSize) <= Math.round(this.mAutoSizeMaxTextSizeInPx)) {
                    autoSizeValuesLength++;
                    currentSize += this.mAutoSizeStepGranularityInPx;
                }
                int[] autoSizeTextSizesInPx = new int[autoSizeValuesLength];
                float sizeToAdd = this.mAutoSizeMinTextSizeInPx;
                for (int i = 0; i < autoSizeValuesLength; i++) {
                    autoSizeTextSizesInPx[i] = Math.round(sizeToAdd);
                    sizeToAdd += this.mAutoSizeStepGranularityInPx;
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(autoSizeTextSizesInPx);
            }
            this.mNeedsAutoSizeText = true;
        } else {
            this.mNeedsAutoSizeText = false;
        }
        return this.mNeedsAutoSizeText;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void autoSizeText() {
        if (isAutoSizeEnabled()) {
            if (this.mNeedsAutoSizeText) {
                if (this.mTextView.getMeasuredHeight() > 0) {
                    if (this.mTextView.getMeasuredWidth() > 0) {
                        int availableWidth;
                        if (((Boolean) invokeAndReturnWithDefault(this.mTextView, "getHorizontallyScrolling", Boolean.valueOf(false))).booleanValue()) {
                            availableWidth = 1048576;
                        } else {
                            availableWidth = (this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft()) - this.mTextView.getTotalPaddingRight();
                        }
                        int availableHeight = (this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom()) - this.mTextView.getCompoundPaddingTop();
                        if (availableWidth > 0) {
                            if (availableHeight > 0) {
                                synchronized (TEMP_RECTF) {
                                    TEMP_RECTF.setEmpty();
                                    TEMP_RECTF.right = (float) availableWidth;
                                    TEMP_RECTF.bottom = (float) availableHeight;
                                    float optimalTextSize = (float) findLargestTextSizeWhichFits(TEMP_RECTF);
                                    if (optimalTextSize != this.mTextView.getTextSize()) {
                                        setTextSizeInternal(0, optimalTextSize);
                                    }
                                }
                            }
                        }
                        return;
                    }
                }
                return;
            }
            this.mNeedsAutoSizeText = true;
        }
    }

    private void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = -1.0f;
        this.mAutoSizeMaxTextSizeInPx = -1.0f;
        this.mAutoSizeStepGranularityInPx = -1.0f;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mNeedsAutoSizeText = false;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    void setTextSizeInternal(int unit, float size) {
        Resources res;
        Context context = this.mContext;
        if (context == null) {
            res = Resources.getSystem();
        } else {
            res = context.getResources();
        }
        setRawTextSize(TypedValue.applyDimension(unit, size, res.getDisplayMetrics()));
    }

    private void setRawTextSize(float size) {
        if (size != this.mTextView.getPaint().getTextSize()) {
            this.mTextView.getPaint().setTextSize(size);
            boolean isInLayout = false;
            if (VERSION.SDK_INT >= 18) {
                isInLayout = this.mTextView.isInLayout();
            }
            if (this.mTextView.getLayout() != null) {
                this.mNeedsAutoSizeText = false;
                String methodName = "nullLayouts";
                try {
                    Method method = getTextViewMethod("nullLayouts");
                    if (method != null) {
                        method.invoke(this.mTextView, new Object[0]);
                    }
                } catch (Exception ex) {
                    Log.w(TAG, "Failed to invoke TextView#nullLayouts() method", ex);
                }
                if (isInLayout) {
                    this.mTextView.forceLayout();
                } else {
                    this.mTextView.requestLayout();
                }
                this.mTextView.invalidate();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean suggestedSizeFitsInSpace(int r10, android.graphics.RectF r11) {
        /*
        r9 = this;
        r0 = r9.mTextView;
        r0 = r0.getText();
        r1 = r9.mTextView;
        r1 = r1.getTransformationMethod();
        if (r1 == 0) goto L_0x0017;
    L_0x000e:
        r2 = r9.mTextView;
        r2 = r1.getTransformation(r0, r2);
        if (r2 == 0) goto L_0x0017;
    L_0x0016:
        r0 = r2;
    L_0x0017:
        r2 = android.os.Build.VERSION.SDK_INT;
        r3 = 16;
        r4 = -1;
        if (r2 < r3) goto L_0x0025;
    L_0x001e:
        r2 = r9.mTextView;
        r2 = r2.getMaxLines();
        goto L_0x0026;
    L_0x0025:
        r2 = -1;
    L_0x0026:
        r3 = r9.mTempTextPaint;
        if (r3 != 0) goto L_0x0032;
    L_0x002a:
        r3 = new android.text.TextPaint;
        r3.<init>();
        r9.mTempTextPaint = r3;
        goto L_0x0035;
    L_0x0032:
        r3.reset();
    L_0x0035:
        r3 = r9.mTempTextPaint;
        r5 = r9.mTextView;
        r5 = r5.getPaint();
        r3.set(r5);
        r3 = r9.mTempTextPaint;
        r5 = (float) r10;
        r3.setTextSize(r5);
        r3 = r9.mTextView;
        r5 = "getLayoutAlignment";
        r6 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r3 = r9.invokeAndReturnWithDefault(r3, r5, r6);
        r3 = (android.text.Layout.Alignment) r3;
        r5 = android.os.Build.VERSION.SDK_INT;
        r6 = 23;
        if (r5 < r6) goto L_0x0063;
    L_0x0058:
        r5 = r11.right;
        r5 = java.lang.Math.round(r5);
        r5 = r9.createStaticLayoutForMeasuring(r0, r3, r5, r2);
        goto L_0x006d;
    L_0x0063:
        r5 = r11.right;
        r5 = java.lang.Math.round(r5);
        r5 = r9.createStaticLayoutForMeasuringPre23(r0, r3, r5);
        r6 = 0;
        r7 = 1;
        if (r2 == r4) goto L_0x0088;
    L_0x0072:
        r4 = r5.getLineCount();
        if (r4 > r2) goto L_0x0087;
    L_0x0078:
        r4 = r5.getLineCount();
        r4 = r4 - r7;
        r4 = r5.getLineEnd(r4);
        r8 = r0.length();
        if (r4 == r8) goto L_0x0088;
    L_0x0087:
        return r6;
    L_0x0088:
        r4 = r5.getHeight();
        r4 = (float) r4;
        r8 = r11.bottom;
        r4 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r4 <= 0) goto L_0x0094;
    L_0x0093:
        return r6;
    L_0x0094:
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AppCompatTextViewAutoSizeHelper.suggestedSizeFitsInSpace(int, android.graphics.RectF):boolean");
    }

    @RequiresApi(23)
    private StaticLayout createStaticLayoutForMeasuring(CharSequence text, Alignment alignment, int availableWidth, int maxLines) {
        return Builder.obtain(text, 0, text.length(), this.mTempTextPaint, availableWidth).setAlignment(alignment).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency()).setMaxLines(maxLines == -1 ? Integer.MAX_VALUE : maxLines).setTextDirection((TextDirectionHeuristic) invokeAndReturnWithDefault(this.mTextView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR)).build();
    }

    private StaticLayout createStaticLayoutForMeasuringPre23(CharSequence text, Alignment alignment, int availableWidth) {
        float lineSpacingMultiplier;
        float lineSpacingAdd;
        boolean includePad;
        if (VERSION.SDK_INT >= 16) {
            lineSpacingMultiplier = this.mTextView.getLineSpacingMultiplier();
            lineSpacingAdd = this.mTextView.getLineSpacingExtra();
            includePad = this.mTextView.getIncludeFontPadding();
        } else {
            lineSpacingMultiplier = ((Float) invokeAndReturnWithDefault(this.mTextView, "getLineSpacingMultiplier", Float.valueOf(1.0f))).floatValue();
            lineSpacingAdd = ((Float) invokeAndReturnWithDefault(this.mTextView, "getLineSpacingExtra", Float.valueOf(0.0f))).floatValue();
            includePad = ((Boolean) invokeAndReturnWithDefault(this.mTextView, "getIncludeFontPadding", Boolean.valueOf(true))).booleanValue();
        }
        return new StaticLayout(text, this.mTempTextPaint, availableWidth, alignment, lineSpacingMultiplier, lineSpacingAdd, includePad);
    }

    @Nullable
    private Method getTextViewMethod(@NonNull String methodName) {
        try {
            Method method = (Method) sTextViewMethodByNameCache.get(methodName);
            if (method == null) {
                method = TextView.class.getDeclaredMethod(methodName, new Class[0]);
                if (method != null) {
                    method.setAccessible(true);
                    sTextViewMethodByNameCache.put(methodName, method);
                }
            }
            return method;
        } catch (Exception ex) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to retrieve TextView#");
            stringBuilder.append(methodName);
            stringBuilder.append("() method");
            Log.w(str, stringBuilder.toString(), ex);
            return null;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    boolean isAutoSizeEnabled() {
        return supportsAutoSizeText() && this.mAutoSizeTextType != 0;
    }

    private boolean supportsAutoSizeText() {
        return !(this.mTextView instanceof AppCompatEditText);
    }
}
