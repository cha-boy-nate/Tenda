package com.google.android.gms.common.util;

import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Objects;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@KeepForSdk
@VisibleForTesting
public final class ArrayUtils {
    @KeepForSdk
    public static <T> boolean contains(T[] tArr, T t) {
        int length = tArr != null ? tArr.length : 0;
        int i = 0;
        while (i < length) {
            if (Objects.equal(tArr[i], t)) {
                break;
            }
            i++;
        }
        i = -1;
        if (i >= 0) {
            return 1;
        }
        return false;
    }

    @KeepForSdk
    public static boolean contains(int[] iArr, int i) {
        if (iArr == null) {
            return false;
        }
        for (int i2 : iArr) {
            if (i2 == i) {
                return 1;
            }
        }
        return false;
    }

    @KeepForSdk
    public static Integer[] toWrapperArray(int[] iArr) {
        if (iArr == null) {
            return null;
        }
        int length = iArr.length;
        Integer[] numArr = new Integer[length];
        for (int i = 0; i < length; i++) {
            numArr[i] = Integer.valueOf(iArr[i]);
        }
        return numArr;
    }

    private ArrayUtils() {
    }

    @KeepForSdk
    public static <T> void writeArray(StringBuilder stringBuilder, T[] tArr) {
        int length = tArr.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(tArr[i].toString());
        }
    }

    @KeepForSdk
    public static void writeArray(StringBuilder stringBuilder, int[] iArr) {
        int length = iArr.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Integer.toString(iArr[i]));
        }
    }

    @KeepForSdk
    public static void writeArray(StringBuilder stringBuilder, long[] jArr) {
        int length = jArr.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Long.toString(jArr[i]));
        }
    }

    @KeepForSdk
    public static void writeArray(StringBuilder stringBuilder, float[] fArr) {
        int length = fArr.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Float.toString(fArr[i]));
        }
    }

    @KeepForSdk
    public static void writeArray(StringBuilder stringBuilder, double[] dArr) {
        int length = dArr.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Double.toString(dArr[i]));
        }
    }

    @KeepForSdk
    public static void writeArray(StringBuilder stringBuilder, boolean[] zArr) {
        int length = zArr.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(Boolean.toString(zArr[i]));
        }
    }

    @KeepForSdk
    public static void writeStringArray(StringBuilder stringBuilder, String[] strArr) {
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\"");
            stringBuilder.append(strArr[i]);
            stringBuilder.append("\"");
        }
    }

    @KeepForSdk
    public static <T> T[] concat(T[]... tArr) {
        if (tArr.length == 0) {
            return (Object[]) Array.newInstance(tArr.getClass(), 0);
        }
        int i = 0;
        for (T[] length : tArr) {
            i += length.length;
        }
        Object copyOf = Arrays.copyOf(tArr[0], i);
        i = tArr[0].length;
        for (int i2 = 1; i2 < tArr.length; i2++) {
            Object obj = tArr[i2];
            System.arraycopy(obj, 0, copyOf, i, obj.length);
            i += obj.length;
        }
        return copyOf;
    }

    @KeepForSdk
    public static byte[] concatByteArrays(byte[]... bArr) {
        if (bArr.length == 0) {
            return new byte[0];
        }
        int i = 0;
        for (byte[] length : bArr) {
            i += length.length;
        }
        Object copyOf = Arrays.copyOf(bArr[0], i);
        i = bArr[0].length;
        for (int i2 = 1; i2 < bArr.length; i2++) {
            Object obj = bArr[i2];
            System.arraycopy(obj, 0, copyOf, i, obj.length);
            i += obj.length;
        }
        return copyOf;
    }

    @KeepForSdk
    public static <T> T[] appendToArray(T[] tArr, T t) {
        if (tArr == null) {
            if (t == null) {
                throw new IllegalArgumentException("Cannot generate array of generic type w/o class info");
            }
        }
        if (tArr == null) {
            tArr = (Object[]) Array.newInstance(t.getClass(), 1);
        } else {
            tArr = Arrays.copyOf(tArr, tArr.length + 1);
        }
        tArr[tArr.length - 1] = t;
        return tArr;
    }

    @KeepForSdk
    public static <T> T[] removeAll(T[] tArr, T... tArr2) {
        if (tArr == null) {
            return null;
        }
        if (tArr2 != null) {
            if (tArr2.length != 0) {
                int i;
                T[] tArr3 = (Object[]) Array.newInstance(tArr2.getClass().getComponentType(), tArr.length);
                int i2 = 0;
                int length;
                if (tArr2.length == 1) {
                    i = 0;
                    for (Object obj : tArr) {
                        if (!Objects.equal(tArr2[0], obj)) {
                            int i3 = i + 1;
                            tArr3[i] = obj;
                            i = i3;
                        }
                    }
                } else {
                    length = tArr.length;
                    i = 0;
                    while (i2 < length) {
                        Object obj2 = tArr[i2];
                        if (!contains((Object[]) tArr2, obj2)) {
                            int i4 = i + 1;
                            tArr3[i] = obj2;
                            i = i4;
                        }
                        i2++;
                    }
                }
                if (tArr3 == null) {
                    return null;
                }
                if (i != tArr3.length) {
                    tArr3 = Arrays.copyOf(tArr3, i);
                }
                return tArr3;
            }
        }
        return Arrays.copyOf(tArr, tArr.length);
    }

    @KeepForSdk
    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList();
    }

    @KeepForSdk
    public static <T> ArrayList<T> toArrayList(T[] tArr) {
        ArrayList<T> arrayList = new ArrayList(r0);
        for (Object add : tArr) {
            arrayList.add(add);
        }
        return arrayList;
    }

    @KeepForSdk
    public static int[] toPrimitiveArray(Collection<Integer> collection) {
        int i = 0;
        if (collection != null) {
            if (collection.size() != 0) {
                int[] iArr = new int[collection.size()];
                for (Integer intValue : collection) {
                    int i2 = i + 1;
                    iArr[i] = intValue.intValue();
                    i = i2;
                }
                return iArr;
            }
        }
        return new int[0];
    }
}
