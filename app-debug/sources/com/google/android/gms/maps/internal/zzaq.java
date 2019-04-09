package com.google.android.gms.maps.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.maps.zzb;

public abstract class zzaq extends zzb implements zzap {
    public zzaq() {
        super("com.google.android.gms.maps.internal.IOnMapReadyCallback");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (i != 1) {
            return false;
        }
        i = parcel.readStrongBinder();
        if (i == 0) {
            i = 0;
        } else {
            parcel = i.queryLocalInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
            if (parcel instanceof IGoogleMapDelegate) {
                i = (IGoogleMapDelegate) parcel;
            } else {
                i = new zzg(i);
            }
        }
        zza(i);
        parcel2.writeNoException();
        return true;
    }
}
