package com.google.android.gms.maps.model;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.maps.zzq;

public final class IndoorLevel {
    private final zzq zzdg;

    public IndoorLevel(zzq zzq) {
        this.zzdg = (zzq) Preconditions.checkNotNull(zzq);
    }

    @NonNull
    public final String getName() {
        try {
            return this.zzdg.getName();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @NonNull
    public final String getShortName() {
        try {
            return this.zzdg.getShortName();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void activate() {
        try {
            this.zzdg.activate();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof IndoorLevel)) {
            return null;
        }
        try {
            return this.zzdg.zzb(((IndoorLevel) obj).zzdg);
        } catch (Object obj2) {
            throw new RuntimeRemoteException(obj2);
        }
    }

    public final int hashCode() {
        try {
            return this.zzdg.zzj();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
