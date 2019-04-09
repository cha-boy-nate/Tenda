package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.List;

public final class zza implements Creator<WakeLockEvent> {
    public final /* synthetic */ Object[] newArray(int i) {
        return new WakeLockEvent[i];
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        long j = 0;
        long j2 = j;
        long j3 = j2;
        String str = null;
        List list = str;
        String str2 = list;
        String str3 = str2;
        String str4 = str3;
        String str5 = str4;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        float f = 0.0f;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 1:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 2:
                    j = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 4:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 5:
                    i3 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 6:
                    list = SafeParcelReader.createStringList(parcel2, readHeader);
                    break;
                case 8:
                    j2 = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 10:
                    str3 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 11:
                    i2 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 12:
                    str2 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 13:
                    str4 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 14:
                    i4 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 15:
                    f = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 16:
                    j3 = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 17:
                    str5 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new WakeLockEvent(i, j, i2, str, i3, list, str2, j2, i4, str3, str4, f, j3, str5);
    }
}
