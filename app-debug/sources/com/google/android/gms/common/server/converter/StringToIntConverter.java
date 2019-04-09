package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.server.response.FastJsonResponse.FieldConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@KeepForSdk
@Class(creator = "StringToIntConverterCreator")
public final class StringToIntConverter extends AbstractSafeParcelable implements FieldConverter<String, Integer> {
    public static final Creator<StringToIntConverter> CREATOR = new zac();
    @VersionField(id = 1)
    private final int zale;
    private final HashMap<String, Integer> zapl;
    private final SparseArray<String> zapm;
    @Field(getter = "getSerializedMap", id = 2)
    private final ArrayList<zaa> zapn;

    @Class(creator = "StringToIntConverterEntryCreator")
    public static final class zaa extends AbstractSafeParcelable {
        public static final Creator<zaa> CREATOR = new zad();
        @VersionField(id = 1)
        private final int versionCode;
        @Field(id = 2)
        final String zapo;
        @Field(id = 3)
        final int zapp;

        @Constructor
        zaa(@Param(id = 1) int i, @Param(id = 2) String str, @Param(id = 3) int i2) {
            this.versionCode = i;
            this.zapo = str;
            this.zapp = i2;
        }

        zaa(String str, int i) {
            this.versionCode = 1;
            this.zapo = str;
            this.zapp = i;
        }

        public final void writeToParcel(Parcel parcel, int i) {
            i = SafeParcelWriter.beginObjectHeader(parcel);
            SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
            SafeParcelWriter.writeString(parcel, 2, this.zapo, false);
            SafeParcelWriter.writeInt(parcel, 3, this.zapp);
            SafeParcelWriter.finishObjectHeader(parcel, i);
        }
    }

    @Constructor
    StringToIntConverter(@Param(id = 1) int i, @Param(id = 2) ArrayList<zaa> arrayList) {
        this.zale = i;
        this.zapl = new HashMap();
        this.zapm = new SparseArray();
        this.zapn = 0;
        arrayList = arrayList;
        i = arrayList.size();
        int i2 = 0;
        while (i2 < i) {
            Object obj = arrayList.get(i2);
            i2++;
            zaa zaa = (zaa) obj;
            add(zaa.zapo, zaa.zapp);
        }
    }

    @KeepForSdk
    public StringToIntConverter() {
        this.zale = 1;
        this.zapl = new HashMap();
        this.zapm = new SparseArray();
        this.zapn = null;
    }

    @KeepForSdk
    public final StringToIntConverter add(String str, int i) {
        this.zapl.put(str, Integer.valueOf(i));
        this.zapm.put(i, str);
        return this;
    }

    public final int zacj() {
        return 7;
    }

    public final int zack() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zale);
        List arrayList = new ArrayList();
        for (String str : this.zapl.keySet()) {
            arrayList.add(new zaa(str, ((Integer) this.zapl.get(str)).intValue()));
        }
        SafeParcelWriter.writeTypedList(parcel, 2, arrayList, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }

    public final /* synthetic */ Object convertBack(Object obj) {
        String str = (String) this.zapm.get(((Integer) obj).intValue());
        if (str == null && this.zapl.containsKey("gms_unknown")) {
            return "gms_unknown";
        }
        return str;
    }

    public final /* synthetic */ Object convert(Object obj) {
        obj = (Integer) this.zapl.get((String) obj);
        if (obj == null) {
            obj = (Integer) this.zapl.get("gms_unknown");
        }
        return obj;
    }
}
