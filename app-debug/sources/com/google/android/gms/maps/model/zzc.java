package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.List;

public final class zzc implements Creator<CircleOptions> {
    public final /* synthetic */ Object[] newArray(int i) {
        return new CircleOptions[i];
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        LatLng latLng = null;
        List list = latLng;
        double d = 0.0d;
        float f = 0.0f;
        int i = 0;
        int i2 = 0;
        float f2 = 0.0f;
        boolean z = false;
        boolean z2 = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    latLng = (LatLng) SafeParcelReader.createParcelable(parcel2, readHeader, LatLng.CREATOR);
                    break;
                case 3:
                    d = SafeParcelReader.readDouble(parcel2, readHeader);
                    break;
                case 4:
                    f = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 5:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 6:
                    i2 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 7:
                    f2 = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 8:
                    z = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 9:
                    z2 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 10:
                    list = SafeParcelReader.createTypedList(parcel2, readHeader, PatternItem.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new CircleOptions(latLng, d, f, i, i2, f2, z, z2, list);
    }
}
