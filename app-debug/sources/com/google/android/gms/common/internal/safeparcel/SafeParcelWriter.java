package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class SafeParcelWriter {
    private SafeParcelWriter() {
    }

    private static void zzb(Parcel parcel, int i, int i2) {
        if (i2 >= SupportMenu.USER_MASK) {
            parcel.writeInt(i | SupportMenu.CATEGORY_MASK);
            parcel.writeInt(i2);
            return;
        }
        parcel.writeInt(i | (i2 << 16));
    }

    private static int zza(Parcel parcel, int i) {
        parcel.writeInt(i | SupportMenu.CATEGORY_MASK);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }

    private static void zzb(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        int i2 = dataPosition - i;
        parcel.setDataPosition(i - 4);
        parcel.writeInt(i2);
        parcel.setDataPosition(dataPosition);
    }

    public static int beginObjectHeader(Parcel parcel) {
        return zza(parcel, 20293);
    }

    public static void finishObjectHeader(Parcel parcel, int i) {
        zzb(parcel, i);
    }

    public static void writeBoolean(Parcel parcel, int i, boolean z) {
        zzb(parcel, i, 4);
        parcel.writeInt(z);
    }

    public static void writeBooleanObject(Parcel parcel, int i, Boolean bool, boolean z) {
        if (bool == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        zzb(parcel, i, true);
        parcel.writeInt(bool.booleanValue());
    }

    public static void writeByte(Parcel parcel, int i, byte b) {
        zzb(parcel, i, 4);
        parcel.writeInt(b);
    }

    public static void writeChar(Parcel parcel, int i, char c) {
        zzb(parcel, i, 4);
        parcel.writeInt(c);
    }

    public static void writeShort(Parcel parcel, int i, short s) {
        zzb(parcel, i, 4);
        parcel.writeInt(s);
    }

    public static void writeInt(Parcel parcel, int i, int i2) {
        zzb(parcel, i, 4);
        parcel.writeInt(i2);
    }

    public static void writeIntegerObject(Parcel parcel, int i, Integer num, boolean z) {
        if (num == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        zzb(parcel, i, true);
        parcel.writeInt(num.intValue());
    }

    public static void writeLong(Parcel parcel, int i, long j) {
        zzb(parcel, i, 8);
        parcel.writeLong(j);
    }

    public static void writeLongObject(Parcel parcel, int i, Long l, boolean z) {
        if (l == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        zzb(parcel, i, true);
        parcel.writeLong(l.longValue());
    }

    public static void writeBigInteger(Parcel parcel, int i, BigInteger bigInteger, boolean z) {
        if (bigInteger == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeByteArray(bigInteger.toByteArray());
        zzb(parcel, i);
    }

    public static void writeFloat(Parcel parcel, int i, float f) {
        zzb(parcel, i, 4);
        parcel.writeFloat(f);
    }

    public static void writeFloatObject(Parcel parcel, int i, Float f, boolean z) {
        if (f == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        zzb(parcel, i, true);
        parcel.writeFloat(f.floatValue());
    }

    public static void writeDouble(Parcel parcel, int i, double d) {
        zzb(parcel, i, 8);
        parcel.writeDouble(d);
    }

    public static void writeDoubleObject(Parcel parcel, int i, Double d, boolean z) {
        if (d == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        zzb(parcel, i, true);
        parcel.writeDouble(d.doubleValue());
    }

    public static void writeBigDecimal(Parcel parcel, int i, BigDecimal bigDecimal, boolean z) {
        if (bigDecimal == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeByteArray(bigDecimal.unscaledValue().toByteArray());
        parcel.writeInt(bigDecimal.scale());
        zzb(parcel, i);
    }

    public static void writeString(Parcel parcel, int i, String str, boolean z) {
        if (str == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeString(str);
        zzb(parcel, i);
    }

    public static void writeIBinder(Parcel parcel, int i, IBinder iBinder, boolean z) {
        if (iBinder == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeStrongBinder(iBinder);
        zzb(parcel, i);
    }

    public static void writeParcelable(Parcel parcel, int i, Parcelable parcelable, int i2, boolean z) {
        if (parcelable == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcelable.writeToParcel(parcel, i2);
        zzb(parcel, i);
    }

    public static void writeBundle(Parcel parcel, int i, Bundle bundle, boolean z) {
        if (bundle == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeBundle(bundle);
        zzb(parcel, i);
    }

    public static void writeByteArray(Parcel parcel, int i, byte[] bArr, boolean z) {
        if (bArr == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeByteArray(bArr);
        zzb(parcel, i);
    }

    public static void writeByteArrayArray(Parcel parcel, int i, byte[][] bArr, boolean z) {
        boolean z2 = false;
        if (bArr == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = bArr.length;
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeByteArray(bArr[z2]);
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeBooleanArray(Parcel parcel, int i, boolean[] zArr, boolean z) {
        if (zArr == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeBooleanArray(zArr);
        zzb(parcel, i);
    }

    public static void writeCharArray(Parcel parcel, int i, char[] cArr, boolean z) {
        if (cArr == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeCharArray(cArr);
        zzb(parcel, i);
    }

    public static void writeIntArray(Parcel parcel, int i, int[] iArr, boolean z) {
        if (iArr == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeIntArray(iArr);
        zzb(parcel, i);
    }

    public static void writeLongArray(Parcel parcel, int i, long[] jArr, boolean z) {
        if (jArr == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeLongArray(jArr);
        zzb(parcel, i);
    }

    public static void writeBigIntegerArray(Parcel parcel, int i, BigInteger[] bigIntegerArr, boolean z) {
        boolean z2 = false;
        if (bigIntegerArr == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = bigIntegerArr.length;
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeByteArray(bigIntegerArr[z2].toByteArray());
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeFloatArray(Parcel parcel, int i, float[] fArr, boolean z) {
        if (fArr == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeFloatArray(fArr);
        zzb(parcel, i);
    }

    public static void writeDoubleArray(Parcel parcel, int i, double[] dArr, boolean z) {
        if (dArr == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeDoubleArray(dArr);
        zzb(parcel, i);
    }

    public static void writeBigDecimalArray(Parcel parcel, int i, BigDecimal[] bigDecimalArr, boolean z) {
        boolean z2 = false;
        if (bigDecimalArr == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = bigDecimalArr.length;
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeByteArray(bigDecimalArr[z2].unscaledValue().toByteArray());
            parcel.writeInt(bigDecimalArr[z2].scale());
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeStringArray(Parcel parcel, int i, String[] strArr, boolean z) {
        if (strArr == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeStringArray(strArr);
        zzb(parcel, i);
    }

    public static void writeIBinderArray(Parcel parcel, int i, IBinder[] iBinderArr, boolean z) {
        if (iBinderArr == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeBinderArray(iBinderArr);
        zzb(parcel, i);
    }

    public static void writeBooleanList(Parcel parcel, int i, List<Boolean> list, boolean z) {
        boolean z2 = false;
        if (list == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = list.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeInt(((Boolean) list.get(z2)).booleanValue());
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeIntegerList(Parcel parcel, int i, List<Integer> list, boolean z) {
        boolean z2 = false;
        if (list == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = list.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeInt(((Integer) list.get(z2)).intValue());
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeLongList(Parcel parcel, int i, List<Long> list, boolean z) {
        boolean z2 = false;
        if (list == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = list.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeLong(((Long) list.get(z2)).longValue());
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeFloatList(Parcel parcel, int i, List<Float> list, boolean z) {
        boolean z2 = false;
        if (list == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = list.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeFloat(((Float) list.get(z2)).floatValue());
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeDoubleList(Parcel parcel, int i, List<Double> list, boolean z) {
        boolean z2 = false;
        if (list == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = list.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeDouble(((Double) list.get(z2)).doubleValue());
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeStringList(Parcel parcel, int i, List<String> list, boolean z) {
        if (list == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeStringList(list);
        zzb(parcel, i);
    }

    public static void writeIBinderList(Parcel parcel, int i, List<IBinder> list, boolean z) {
        if (list == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeBinderList(list);
        zzb(parcel, i);
    }

    public static <T extends Parcelable> void writeTypedArray(Parcel parcel, int i, T[] tArr, int i2, boolean z) {
        if (tArr == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeInt(z);
        for (Parcelable parcelable : tArr) {
            if (parcelable == null) {
                parcel.writeInt(0);
            } else {
                zza(parcel, parcelable, i2);
            }
        }
        zzb(parcel, i);
    }

    public static <T extends Parcelable> void writeTypedList(Parcel parcel, int i, List<T> list, boolean z) {
        if (list == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = list.size();
        parcel.writeInt(z);
        for (boolean z2 = false; z2 < z; z2++) {
            Parcelable parcelable = (Parcelable) list.get(z2);
            if (parcelable == null) {
                parcel.writeInt(0);
            } else {
                zza(parcel, parcelable, 0);
            }
        }
        zzb(parcel, i);
    }

    private static <T extends Parcelable> void zza(Parcel parcel, T t, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(1);
        int dataPosition2 = parcel.dataPosition();
        t.writeToParcel(parcel, i);
        t = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(t - dataPosition2);
        parcel.setDataPosition(t);
    }

    public static void writeParcel(Parcel parcel, int i, Parcel parcel2, boolean z) {
        if (parcel2 == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.appendFrom(parcel2, 0, parcel2.dataSize());
        zzb(parcel, i);
    }

    public static void writeParcelArray(Parcel parcel, int i, Parcel[] parcelArr, boolean z) {
        if (parcelArr == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeInt(z);
        for (Parcel parcel2 : parcelArr) {
            if (parcel2 != null) {
                parcel.writeInt(parcel2.dataSize());
                parcel.appendFrom(parcel2, 0, parcel2.dataSize());
            } else {
                parcel.writeInt(0);
            }
        }
        zzb(parcel, i);
    }

    public static void writeParcelList(Parcel parcel, int i, List<Parcel> list, boolean z) {
        if (list == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = list.size();
        parcel.writeInt(z);
        for (boolean z2 = false; z2 < z; z2++) {
            Parcel parcel2 = (Parcel) list.get(z2);
            if (parcel2 != null) {
                parcel.writeInt(parcel2.dataSize());
                parcel.appendFrom(parcel2, 0, parcel2.dataSize());
            } else {
                parcel.writeInt(0);
            }
        }
        zzb(parcel, i);
    }

    public static void writeList(Parcel parcel, int i, List list, boolean z) {
        if (list == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeList(list);
        zzb(parcel, i);
    }

    public static void writeSparseBooleanArray(Parcel parcel, int i, SparseBooleanArray sparseBooleanArray, boolean z) {
        if (sparseBooleanArray == null) {
            if (z) {
                zzb(parcel, i, null);
            }
            return;
        }
        i = zza(parcel, i);
        parcel.writeSparseBooleanArray(sparseBooleanArray);
        zzb(parcel, i);
    }

    public static void writeDoubleSparseArray(Parcel parcel, int i, SparseArray<Double> sparseArray, boolean z) {
        boolean z2 = false;
        if (sparseArray == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = sparseArray.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeInt(sparseArray.keyAt(z2));
            parcel.writeDouble(((Double) sparseArray.valueAt(z2)).doubleValue());
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeFloatSparseArray(Parcel parcel, int i, SparseArray<Float> sparseArray, boolean z) {
        boolean z2 = false;
        if (sparseArray == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = sparseArray.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeInt(sparseArray.keyAt(z2));
            parcel.writeFloat(((Float) sparseArray.valueAt(z2)).floatValue());
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeSparseIntArray(Parcel parcel, int i, SparseIntArray sparseIntArray, boolean z) {
        boolean z2 = false;
        if (sparseIntArray == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = sparseIntArray.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeInt(sparseIntArray.keyAt(z2));
            parcel.writeInt(sparseIntArray.valueAt(z2));
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeSparseLongArray(Parcel parcel, int i, SparseLongArray sparseLongArray, boolean z) {
        boolean z2 = false;
        if (sparseLongArray == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = sparseLongArray.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeInt(sparseLongArray.keyAt(z2));
            parcel.writeLong(sparseLongArray.valueAt(z2));
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeStringSparseArray(Parcel parcel, int i, SparseArray<String> sparseArray, boolean z) {
        boolean z2 = false;
        if (sparseArray == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = sparseArray.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeInt(sparseArray.keyAt(z2));
            parcel.writeString((String) sparseArray.valueAt(z2));
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeParcelSparseArray(Parcel parcel, int i, SparseArray<Parcel> sparseArray, boolean z) {
        if (sparseArray == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = sparseArray.size();
        parcel.writeInt(z);
        for (boolean z2 = false; z2 < z; z2++) {
            parcel.writeInt(sparseArray.keyAt(z2));
            Parcel parcel2 = (Parcel) sparseArray.valueAt(z2);
            if (parcel2 != null) {
                parcel.writeInt(parcel2.dataSize());
                parcel.appendFrom(parcel2, 0, parcel2.dataSize());
            } else {
                parcel.writeInt(0);
            }
        }
        zzb(parcel, i);
    }

    public static <T extends Parcelable> void writeTypedSparseArray(Parcel parcel, int i, SparseArray<T> sparseArray, boolean z) {
        if (sparseArray == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = sparseArray.size();
        parcel.writeInt(z);
        for (boolean z2 = false; z2 < z; z2++) {
            parcel.writeInt(sparseArray.keyAt(z2));
            Parcelable parcelable = (Parcelable) sparseArray.valueAt(z2);
            if (parcelable == null) {
                parcel.writeInt(0);
            } else {
                zza(parcel, parcelable, 0);
            }
        }
        zzb(parcel, i);
    }

    public static void writeIBinderSparseArray(Parcel parcel, int i, SparseArray<IBinder> sparseArray, boolean z) {
        boolean z2 = false;
        if (sparseArray == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = sparseArray.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeInt(sparseArray.keyAt(z2));
            parcel.writeStrongBinder((IBinder) sparseArray.valueAt(z2));
            z2++;
        }
        zzb(parcel, i);
    }

    public static void writeByteArraySparseArray(Parcel parcel, int i, SparseArray<byte[]> sparseArray, boolean z) {
        boolean z2 = false;
        if (sparseArray == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
            return;
        }
        i = zza(parcel, i);
        z = sparseArray.size();
        parcel.writeInt(z);
        while (z2 < z) {
            parcel.writeInt(sparseArray.keyAt(z2));
            parcel.writeByteArray((byte[]) sparseArray.valueAt(z2));
            z2++;
        }
        zzb(parcel, i);
    }
}
