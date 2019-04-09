package com.google.android.gms.maps.internal;

import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.maps.zzb;

public interface ILocationSourceDelegate extends IInterface {

    public static abstract class zza extends zzb implements ILocationSourceDelegate {
        public zza() {
            super("com.google.android.gms.maps.internal.ILocationSourceDelegate");
        }

        protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    i = parcel.readStrongBinder();
                    if (i == 0) {
                        i = 0;
                    } else {
                        parcel = i.queryLocalInterface("com.google.android.gms.maps.internal.IOnLocationChangeListener");
                        if ((parcel instanceof zzah) != 0) {
                            i = (zzah) parcel;
                        } else {
                            i = new zzai(i);
                        }
                    }
                    activate(i);
                    break;
                case 2:
                    deactivate();
                    break;
                default:
                    return false;
            }
            parcel2.writeNoException();
            return true;
        }
    }

    void activate(zzah zzah) throws RemoteException;

    void deactivate() throws RemoteException;
}
