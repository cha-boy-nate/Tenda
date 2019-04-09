package android.support.v4.util;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.text.TextUtils;
import java.util.Collection;
import java.util.Locale;

@RestrictTo({Scope.LIBRARY_GROUP})
public class Preconditions {
    public static <T> T[] checkArrayElementsNotNull(T[] r6, java.lang.String r7) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x0040 in {6, 8, 9, 11} preds:[]
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
        if (r6 == 0) goto L_0x0029;
    L_0x0002:
        r0 = 0;
    L_0x0003:
        r1 = r6.length;
        if (r0 >= r1) goto L_0x0028;
    L_0x0006:
        r1 = r6[r0];
        if (r1 == 0) goto L_0x000d;
    L_0x000a:
        r0 = r0 + 1;
        goto L_0x0003;
    L_0x000d:
        r1 = new java.lang.NullPointerException;
        r2 = java.util.Locale.US;
        r3 = 2;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r3[r4] = r7;
        r4 = java.lang.Integer.valueOf(r0);
        r5 = 1;
        r3[r5] = r4;
        r4 = "%s[%d] must not be null";
        r2 = java.lang.String.format(r2, r4, r3);
        r1.<init>(r2);
        throw r1;
    L_0x0028:
        return r6;
    L_0x0029:
        r0 = new java.lang.NullPointerException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r7);
        r2 = " must not be null";
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.Preconditions.checkArrayElementsNotNull(java.lang.Object[], java.lang.String):T[]");
    }

    @android.support.annotation.NonNull
    public static <C extends java.util.Collection<T>, T> C checkCollectionElementsNotNull(C r8, java.lang.String r9) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x004b in {6, 8, 9, 11} preds:[]
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
        if (r8 == 0) goto L_0x0034;
    L_0x0002:
        r0 = 0;
        r2 = r8.iterator();
    L_0x0008:
        r3 = r2.hasNext();
        if (r3 == 0) goto L_0x0033;
    L_0x000e:
        r3 = r2.next();
        if (r3 == 0) goto L_0x0018;
    L_0x0014:
        r4 = 1;
        r0 = r0 + r4;
        goto L_0x0008;
    L_0x0018:
        r2 = new java.lang.NullPointerException;
        r4 = java.util.Locale.US;
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r9;
        r6 = 1;
        r7 = java.lang.Long.valueOf(r0);
        r5[r6] = r7;
        r6 = "%s[%d] must not be null";
        r4 = java.lang.String.format(r4, r6, r5);
        r2.<init>(r4);
        throw r2;
    L_0x0033:
        return r8;
    L_0x0034:
        r0 = new java.lang.NullPointerException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r9);
        r2 = " must not be null";
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.Preconditions.checkCollectionElementsNotNull(java.util.Collection, java.lang.String):C");
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    @NonNull
    public static <T extends CharSequence> T checkStringNotEmpty(T string) {
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        throw new IllegalArgumentException();
    }

    @NonNull
    public static <T extends CharSequence> T checkStringNotEmpty(T string, Object errorMessage) {
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        throw new IllegalArgumentException(String.valueOf(errorMessage));
    }

    @NonNull
    public static <T> T checkNotNull(T reference) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException();
    }

    @NonNull
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException(String.valueOf(errorMessage));
    }

    public static void checkState(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void checkState(boolean expression) {
        checkState(expression, null);
    }

    public static int checkFlagsArgument(int requestedFlags, int allowedFlags) {
        if ((requestedFlags & allowedFlags) == requestedFlags) {
            return requestedFlags;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Requested flags 0x");
        stringBuilder.append(Integer.toHexString(requestedFlags));
        stringBuilder.append(", but only 0x");
        stringBuilder.append(Integer.toHexString(allowedFlags));
        stringBuilder.append(" are allowed");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @IntRange(from = 0)
    public static int checkArgumentNonnegative(int value, String errorMessage) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException(errorMessage);
    }

    @IntRange(from = 0)
    public static int checkArgumentNonnegative(int value) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException();
    }

    public static long checkArgumentNonnegative(long value) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException();
    }

    public static long checkArgumentNonnegative(long value, String errorMessage) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException(errorMessage);
    }

    public static int checkArgumentPositive(int value, String errorMessage) {
        if (value > 0) {
            return value;
        }
        throw new IllegalArgumentException(errorMessage);
    }

    public static float checkArgumentFinite(float value, String valueName) {
        StringBuilder stringBuilder;
        if (Float.isNaN(value)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(valueName);
            stringBuilder.append(" must not be NaN");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (!Float.isInfinite(value)) {
            return value;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(valueName);
            stringBuilder.append(" must not be infinite");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static float checkArgumentInRange(float value, float lower, float upper, String valueName) {
        if (Float.isNaN(value)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(valueName);
            stringBuilder.append(" must not be NaN");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (value < lower) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", new Object[]{valueName, Float.valueOf(lower), Float.valueOf(upper)}));
        } else if (value <= upper) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", new Object[]{valueName, Float.valueOf(lower), Float.valueOf(upper)}));
        }
    }

    public static int checkArgumentInRange(int value, int lower, int upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[]{valueName, Integer.valueOf(lower), Integer.valueOf(upper)}));
        } else if (value <= upper) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[]{valueName, Integer.valueOf(lower), Integer.valueOf(upper)}));
        }
    }

    public static long checkArgumentInRange(long value, long lower, long upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[]{valueName, Long.valueOf(lower), Long.valueOf(upper)}));
        } else if (value <= upper) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[]{valueName, Long.valueOf(lower), Long.valueOf(upper)}));
        }
    }

    public static <T> Collection<T> checkCollectionNotEmpty(Collection<T> value, String valueName) {
        StringBuilder stringBuilder;
        if (value == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(valueName);
            stringBuilder.append(" must not be null");
            throw new NullPointerException(stringBuilder.toString());
        } else if (!value.isEmpty()) {
            return value;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(valueName);
            stringBuilder.append(" is empty");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static float[] checkArrayElementsInRange(float[] value, float lower, float upper, String valueName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(valueName);
        stringBuilder.append(" must not be null");
        checkNotNull(value, stringBuilder.toString());
        int i = 0;
        while (i < value.length) {
            float v = value[i];
            if (Float.isNaN(v)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(valueName);
                stringBuilder2.append("[");
                stringBuilder2.append(i);
                stringBuilder2.append("] must not be NaN");
                throw new IllegalArgumentException(stringBuilder2.toString());
            } else if (v < lower) {
                throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too low)", new Object[]{valueName, Integer.valueOf(i), Float.valueOf(lower), Float.valueOf(upper)}));
            } else if (v <= upper) {
                i++;
            } else {
                throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too high)", new Object[]{valueName, Integer.valueOf(i), Float.valueOf(lower), Float.valueOf(upper)}));
            }
        }
        return value;
    }

    private Preconditions() {
    }
}
