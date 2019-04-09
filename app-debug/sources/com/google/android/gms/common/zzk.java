package com.google.android.gms.common;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.common.zzb;
import javax.annotation.Nullable;

@Class(creator = "GoogleCertificatesQueryCreator")
public final class zzk extends AbstractSafeParcelable {
    public static final Creator<zzk> CREATOR = new zzl();
    @Field(getter = "getAllowTestKeys", id = 3)
    private final boolean zzaa;
    @Field(getter = "getCallingPackage", id = 1)
    private final String zzy;
    @Field(getter = "getCallingCertificateBinder", id = 2, type = "android.os.IBinder")
    @Nullable
    private final zze zzz;

    @Constructor
    zzk(@Param(id = 1) String str, @Param(id = 2) @Nullable IBinder iBinder, @Param(id = 3) boolean z) {
        this.zzy = str;
        this.zzz = zza(iBinder);
        this.zzaa = z;
    }

    zzk(String str, @Nullable zze zze, boolean z) {
        this.zzy = str;
        this.zzz = zze;
        this.zzaa = z;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        IBinder iBinder;
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, this.zzy, false);
        zzb zzb = this.zzz;
        if (zzb == null) {
            Log.w("GoogleCertificatesQuery", "certificate binder is null");
            iBinder = null;
        } else {
            iBinder = zzb.asBinder();
        }
        SafeParcelWriter.writeIBinder(parcel, 2, iBinder, false);
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzaa);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }

    @Nullable
    private static zze zza(@Nullable IBinder iBinder) {
        zze zze = null;
        if (iBinder == null) {
            return null;
        }
        try {
            iBinder = zzj.zzb(iBinder).zzb();
            iBinder = iBinder == null ? null : (byte[]) ObjectWrapper.unwrap(iBinder);
            if (iBinder != null) {
                zze = new zzf(iBinder);
            } else {
                Log.e("GoogleCertificatesQuery", "Could not unwrap certificate");
            }
            return zze;
        } catch (IBinder iBinder2) {
            Log.e("GoogleCertificatesQuery", "Could not unwrap certificate", iBinder2);
            return null;
        }
    }
}
