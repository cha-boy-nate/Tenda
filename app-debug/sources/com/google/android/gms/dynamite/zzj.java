package com.google.android.gms.dynamite;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;
import com.google.android.gms.internal.common.zza;
import com.google.android.gms.internal.common.zzc;

public final class zzj extends zza implements zzi {
    zzj(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.dynamite.IDynamiteLoader");
    }

    public final IObjectWrapper zza(IObjectWrapper iObjectWrapper, String str, int i) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (IInterface) iObjectWrapper);
        zza.writeString(str);
        zza.writeInt(i);
        iObjectWrapper = zza(2, zza);
        str = Stub.asInterface(iObjectWrapper.readStrongBinder());
        iObjectWrapper.recycle();
        return str;
    }

    public final int zza(IObjectWrapper iObjectWrapper, String str, boolean z) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (IInterface) iObjectWrapper);
        zza.writeString(str);
        zzc.writeBoolean(zza, z);
        iObjectWrapper = zza(3, zza);
        str = iObjectWrapper.readInt();
        iObjectWrapper.recycle();
        return str;
    }

    public final IObjectWrapper zzb(IObjectWrapper iObjectWrapper, String str, int i) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (IInterface) iObjectWrapper);
        zza.writeString(str);
        zza.writeInt(i);
        iObjectWrapper = zza(4, zza);
        str = Stub.asInterface(iObjectWrapper.readStrongBinder());
        iObjectWrapper.recycle();
        return str;
    }

    public final int zzb(IObjectWrapper iObjectWrapper, String str, boolean z) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (IInterface) iObjectWrapper);
        zza.writeString(str);
        zzc.writeBoolean(zza, z);
        iObjectWrapper = zza(5, zza);
        str = iObjectWrapper.readInt();
        iObjectWrapper.recycle();
        return str;
    }

    public final int zzaj() throws RemoteException {
        Parcel zza = zza(6, zza());
        int readInt = zza.readInt();
        zza.recycle();
        return readInt;
    }
}
