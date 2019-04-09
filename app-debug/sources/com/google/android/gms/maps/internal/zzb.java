package com.google.android.gms.maps.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;
import com.google.android.gms.internal.maps.zza;
import com.google.android.gms.internal.maps.zzc;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public final class zzb extends zza implements ICameraUpdateFactoryDelegate {
    zzb(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
    }

    public final IObjectWrapper zoomIn() throws RemoteException {
        Parcel zza = zza(1, zza());
        IObjectWrapper asInterface = Stub.asInterface(zza.readStrongBinder());
        zza.recycle();
        return asInterface;
    }

    public final IObjectWrapper zoomOut() throws RemoteException {
        Parcel zza = zza(2, zza());
        IObjectWrapper asInterface = Stub.asInterface(zza.readStrongBinder());
        zza.recycle();
        return asInterface;
    }

    public final IObjectWrapper scrollBy(float f, float f2) throws RemoteException {
        Parcel zza = zza();
        zza.writeFloat(f);
        zza.writeFloat(f2);
        f = zza(4.2E-45f, zza);
        f2 = Stub.asInterface(f.readStrongBinder());
        f.recycle();
        return f2;
    }

    public final IObjectWrapper zoomTo(float f) throws RemoteException {
        Parcel zza = zza();
        zza.writeFloat(f);
        f = zza(5.6E-45f, zza);
        IObjectWrapper asInterface = Stub.asInterface(f.readStrongBinder());
        f.recycle();
        return asInterface;
    }

    public final IObjectWrapper zoomBy(float f) throws RemoteException {
        Parcel zza = zza();
        zza.writeFloat(f);
        f = zza(7.0E-45f, zza);
        IObjectWrapper asInterface = Stub.asInterface(f.readStrongBinder());
        f.recycle();
        return asInterface;
    }

    public final IObjectWrapper zoomByWithFocus(float f, int i, int i2) throws RemoteException {
        Parcel zza = zza();
        zza.writeFloat(f);
        zza.writeInt(i);
        zza.writeInt(i2);
        f = zza(8.4E-45f, zza);
        i = Stub.asInterface(f.readStrongBinder());
        f.recycle();
        return i;
    }

    public final IObjectWrapper newCameraPosition(CameraPosition cameraPosition) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (Parcelable) cameraPosition);
        cameraPosition = zza(7, zza);
        IObjectWrapper asInterface = Stub.asInterface(cameraPosition.readStrongBinder());
        cameraPosition.recycle();
        return asInterface;
    }

    public final IObjectWrapper newLatLng(LatLng latLng) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (Parcelable) latLng);
        latLng = zza(8, zza);
        IObjectWrapper asInterface = Stub.asInterface(latLng.readStrongBinder());
        latLng.recycle();
        return asInterface;
    }

    public final IObjectWrapper newLatLngZoom(LatLng latLng, float f) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (Parcelable) latLng);
        zza.writeFloat(f);
        latLng = zza(9, zza);
        f = Stub.asInterface(latLng.readStrongBinder());
        latLng.recycle();
        return f;
    }

    public final IObjectWrapper newLatLngBounds(LatLngBounds latLngBounds, int i) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (Parcelable) latLngBounds);
        zza.writeInt(i);
        latLngBounds = zza(10, zza);
        i = Stub.asInterface(latLngBounds.readStrongBinder());
        latLngBounds.recycle();
        return i;
    }

    public final IObjectWrapper newLatLngBoundsWithSize(LatLngBounds latLngBounds, int i, int i2, int i3) throws RemoteException {
        Parcel zza = zza();
        zzc.zza(zza, (Parcelable) latLngBounds);
        zza.writeInt(i);
        zza.writeInt(i2);
        zza.writeInt(i3);
        latLngBounds = zza(11, zza);
        i = Stub.asInterface(latLngBounds.readStrongBinder());
        latLngBounds.recycle();
        return i;
    }
}
