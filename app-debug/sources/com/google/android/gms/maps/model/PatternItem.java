package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import java.util.ArrayList;
import java.util.List;

@Class(creator = "PatternItemCreator")
@Reserved({1})
public class PatternItem extends AbstractSafeParcelable {
    public static final Creator<PatternItem> CREATOR = new zzi();
    private static final String TAG = PatternItem.class.getSimpleName();
    @Field(getter = "getType", id = 2)
    private final int type;
    @Nullable
    @Field(getter = "getLength", id = 3)
    private final Float zzdv;

    @Constructor
    public PatternItem(@Param(id = 2) int i, @Nullable @Param(id = 3) Float f) {
        String valueOf;
        StringBuilder stringBuilder;
        boolean z = true;
        if (i != 1) {
            if (f == null || f.floatValue() < 0.0f) {
                z = false;
                valueOf = String.valueOf(f);
                stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 45);
                stringBuilder.append("Invalid PatternItem: type=");
                stringBuilder.append(i);
                stringBuilder.append(" length=");
                stringBuilder.append(valueOf);
                Preconditions.checkArgument(z, stringBuilder.toString());
                this.type = i;
                this.zzdv = f;
            }
        }
        valueOf = String.valueOf(f);
        stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 45);
        stringBuilder.append("Invalid PatternItem: type=");
        stringBuilder.append(i);
        stringBuilder.append(" length=");
        stringBuilder.append(valueOf);
        Preconditions.checkArgument(z, stringBuilder.toString());
        this.type = i;
        this.zzdv = f;
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.type);
        SafeParcelWriter.writeFloatObject(parcel, 3, this.zzdv, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.type), this.zzdv);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PatternItem)) {
            return false;
        }
        PatternItem patternItem = (PatternItem) obj;
        if (this.type != patternItem.type || Objects.equal(this.zzdv, patternItem.zzdv) == null) {
            return false;
        }
        return true;
    }

    public String toString() {
        int i = this.type;
        String valueOf = String.valueOf(this.zzdv);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 39);
        stringBuilder.append("[PatternItem: type=");
        stringBuilder.append(i);
        stringBuilder.append(" length=");
        stringBuilder.append(valueOf);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Nullable
    static List<PatternItem> zza(@Nullable List<PatternItem> list) {
        if (list == null) {
            return null;
        }
        List<PatternItem> arrayList = new ArrayList(list.size());
        for (Object obj : list) {
            Object obj2;
            if (obj2 != null) {
                int i = obj2.type;
                switch (i) {
                    case 0:
                        obj2 = new Dash(obj2.zzdv.floatValue());
                        break;
                    case 1:
                        obj2 = new Dot();
                        break;
                    case 2:
                        obj2 = new Gap(obj2.zzdv.floatValue());
                        break;
                    default:
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder(37);
                        stringBuilder.append("Unknown PatternItem type: ");
                        stringBuilder.append(i);
                        Log.w(str, stringBuilder.toString());
                        break;
                }
            }
            obj2 = null;
            arrayList.add(obj2);
        }
        return arrayList;
    }
}
