package com.google.android.gms.common.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.data.DataHolder.Builder;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

@KeepForSdk
public class DataBufferSafeParcelable<T extends SafeParcelable> extends AbstractDataBuffer<T> {
    private static final String[] zaln = new String[]{"data"};
    private final Creator<T> zalo;

    @KeepForSdk
    public DataBufferSafeParcelable(DataHolder dataHolder, Creator<T> creator) {
        super(dataHolder);
        this.zalo = creator;
    }

    @KeepForSdk
    public static Builder buildDataHolder() {
        return DataHolder.builder(zaln);
    }

    @KeepForSdk
    public static <T extends SafeParcelable> void addValue(Builder builder, T t) {
        Parcel obtain = Parcel.obtain();
        t.writeToParcel(obtain, 0);
        t = new ContentValues();
        t.put("data", obtain.marshall());
        builder.withRow(t);
        obtain.recycle();
    }

    @KeepForSdk
    public T get(int i) {
        i = this.mDataHolder.getByteArray("data", i, this.mDataHolder.getWindowIndex(i));
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(i, 0, i.length);
        obtain.setDataPosition(0);
        SafeParcelable safeParcelable = (SafeParcelable) this.zalo.createFromParcel(obtain);
        obtain.recycle();
        return safeParcelable;
    }
}
