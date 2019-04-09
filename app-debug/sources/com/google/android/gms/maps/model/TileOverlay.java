package com.google.android.gms.maps.model;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.maps.zzac;

public final class TileOverlay {
    private final zzac zzeh;

    public TileOverlay(zzac zzac) {
        this.zzeh = (zzac) Preconditions.checkNotNull(zzac);
    }

    public final void remove() {
        try {
            this.zzeh.remove();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void clearTileCache() {
        try {
            this.zzeh.clearTileCache();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getId() {
        try {
            return this.zzeh.getId();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setZIndex(float f) {
        try {
            this.zzeh.setZIndex(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getZIndex() {
        try {
            return this.zzeh.getZIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setVisible(boolean z) {
        try {
            this.zzeh.setVisible(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isVisible() {
        try {
            return this.zzeh.isVisible();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setFadeIn(boolean z) {
        try {
            this.zzeh.setFadeIn(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean getFadeIn() {
        try {
            return this.zzeh.getFadeIn();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTransparency(float f) {
        try {
            this.zzeh.setTransparency(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getTransparency() {
        try {
            return this.zzeh.getTransparency();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof TileOverlay)) {
            return null;
        }
        try {
            return this.zzeh.zza(((TileOverlay) obj).zzeh);
        } catch (Object obj2) {
            throw new RuntimeRemoteException(obj2);
        }
    }

    public final int hashCode() {
        try {
            return this.zzeh.zzj();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
