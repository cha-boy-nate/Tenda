package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzp implements Creator<StreetViewPanoramaOrientation> {
    public final /* synthetic */ Object[] newArray(int i) {
        return new StreetViewPanoramaOrientation[i];
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        float f = 0.0f;
        float f2 = 0.0f;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    f = SafeParcelReader.readFloat(parcel, readHeader);
                    break;
                case 3:
                    f2 = SafeParcelReader.readFloat(parcel, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new StreetViewPanoramaOrientation(f, f2);
    }
}
