package com.google.android.gms.internal.maps;

import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;

public final class zzg extends zza implements zze {
    zzg(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
    }

    public final IObjectWrapper zza(int i) throws RemoteException {
        Parcel zza = zza();
        zza.writeInt(i);
        i = zza(1, zza);
        IObjectWrapper asInterface = Stub.asInterface(i.readStrongBinder());
        i.recycle();
        return asInterface;
    }

    public final IObjectWrapper zza(String str) throws RemoteException {
        Parcel zza = zza();
        zza.writeString(str);
        str = zza(2, zza);
        IObjectWrapper asInterface = Stub.asInterface(str.readStrongBinder());
        str.recycle();
        return asInterface;
    }

    public final IObjectWrapper zzb(String str) throws RemoteException {
        Parcel zza = zza();
        zza.writeString(str);
        str = zza(3, zza);
        IObjectWrapper asInterface = Stub.asInterface(str.readStrongBinder());
        str.recycle();
        return asInterface;
    }

    public final IObjectWrapper zzi() throws RemoteException {
        Parcel zza = zza(4, zza());
        IObjectWrapper asInterface = Stub.asInterface(zza.readStrongBinder());
        zza.recycle();
        return asInterface;
    }

    public final IObjectWrapper zza(float f) throws RemoteException {
        Parcel zza = zza();
        zza.writeFloat(f);
        f = zza(7.0E-45f, zza);
        IObjectWrapper asInterface = Stub.asInterface(f.readStrongBinder());
        f.recycle();
        return asInterface;
    }

    public final IObjectWrapper zza(Bitmap bitmap) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (Parcelable) bitmap);
        bitmap = zza(6, zza);
        IObjectWrapper asInterface = Stub.asInterface(bitmap.readStrongBinder());
        bitmap.recycle();
        return asInterface;
    }

    public final IObjectWrapper zzc(String str) throws RemoteException {
        Parcel zza = zza();
        zza.writeString(str);
        str = zza(7, zza);
        IObjectWrapper asInterface = Stub.asInterface(str.readStrongBinder());
        str.recycle();
        return asInterface;
    }
}
