package com.google.android.gms.common.internal.safeparcel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.Serializable;
import java.util.ArrayList;

@KeepForSdk
@VisibleForTesting
public final class SafeParcelableSerializer {
    @KeepForSdk
    public static <T extends SafeParcelable> byte[] serializeToBytes(T t) {
        Parcel obtain = Parcel.obtain();
        t.writeToParcel(obtain, 0);
        t = obtain.marshall();
        obtain.recycle();
        return t;
    }

    @KeepForSdk
    public static <T extends SafeParcelable> T deserializeFromBytes(byte[] bArr, Creator<T> creator) {
        Preconditions.checkNotNull(creator);
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        SafeParcelable safeParcelable = (SafeParcelable) creator.createFromParcel(obtain);
        obtain.recycle();
        return safeParcelable;
    }

    public static <T extends SafeParcelable> void serializeIterableToBundle(Iterable<T> iterable, Bundle bundle, String str) {
        Serializable arrayList = new ArrayList();
        for (T serializeToBytes : iterable) {
            arrayList.add(serializeToBytes(serializeToBytes));
        }
        bundle.putSerializable(str, arrayList);
    }

    public static <T extends SafeParcelable> ArrayList<T> deserializeIterableFromBundle(Bundle bundle, String str, Creator<T> creator) {
        ArrayList arrayList = (ArrayList) bundle.getSerializable(str);
        if (arrayList == null) {
            return null;
        }
        str = new ArrayList(arrayList.size());
        arrayList = arrayList;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            str.add(deserializeFromBytes((byte[]) obj, creator));
        }
        return str;
    }

    public static <T extends SafeParcelable> String serializeToString(T t) {
        return Base64Utils.encodeUrlSafe(serializeToBytes(t));
    }

    public static <T extends SafeParcelable> T deserializeFromString(String str, Creator<T> creator) {
        return deserializeFromBytes(Base64Utils.decodeUrlSafe(str), creator);
    }

    @KeepForSdk
    public static <T extends SafeParcelable> void serializeToIntentExtra(T t, Intent intent, String str) {
        intent.putExtra(str, serializeToBytes(t));
    }

    @KeepForSdk
    public static <T extends SafeParcelable> T deserializeFromIntentExtra(Intent intent, String str, Creator<T> creator) {
        intent = intent.getByteArrayExtra(str);
        if (intent == null) {
            return null;
        }
        return deserializeFromBytes(intent, creator);
    }

    @KeepForSdk
    public static <T extends SafeParcelable> void serializeIterableToIntentExtra(Iterable<T> iterable, Intent intent, String str) {
        Serializable arrayList = new ArrayList();
        for (T serializeToBytes : iterable) {
            arrayList.add(serializeToBytes(serializeToBytes));
        }
        intent.putExtra(str, arrayList);
    }

    @KeepForSdk
    public static <T extends SafeParcelable> ArrayList<T> deserializeIterableFromIntentExtra(Intent intent, String str, Creator<T> creator) {
        ArrayList arrayList = (ArrayList) intent.getSerializableExtra(str);
        if (arrayList == null) {
            return null;
        }
        str = new ArrayList(arrayList.size());
        arrayList = arrayList;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            str.add(deserializeFromBytes((byte[]) obj, creator));
        }
        return str;
    }
}
