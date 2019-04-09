package com.google.android.gms.maps;

import android.location.Location;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.internal.zzah;
import com.google.android.gms.maps.model.RuntimeRemoteException;

final class zzm implements OnLocationChangedListener {
    private final /* synthetic */ zzah zzu;

    zzm(zzl zzl, zzah zzah) {
        this.zzu = zzah;
    }

    public final void onLocationChanged(Location location) {
        try {
            this.zzu.zza(location);
        } catch (Location location2) {
            throw new RuntimeRemoteException(location2);
        }
    }
}
