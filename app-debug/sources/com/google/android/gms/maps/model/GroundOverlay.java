package com.google.android.gms.maps.model;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.maps.zzk;

public final class GroundOverlay {
    private final zzk zzcw;

    public GroundOverlay(zzk zzk) {
        this.zzcw = (zzk) Preconditions.checkNotNull(zzk);
    }

    public final void remove() {
        try {
            this.zzcw.remove();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getId() {
        try {
            return this.zzcw.getId();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPosition(LatLng latLng) {
        try {
            this.zzcw.setPosition(latLng);
        } catch (LatLng latLng2) {
            throw new RuntimeRemoteException(latLng2);
        }
    }

    public final LatLng getPosition() {
        try {
            return this.zzcw.getPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setImage(@NonNull BitmapDescriptor bitmapDescriptor) {
        Preconditions.checkNotNull(bitmapDescriptor, "imageDescriptor must not be null");
        try {
            this.zzcw.zzf(bitmapDescriptor.zzb());
        } catch (BitmapDescriptor bitmapDescriptor2) {
            throw new RuntimeRemoteException(bitmapDescriptor2);
        }
    }

    public final void setDimensions(float f) {
        try {
            this.zzcw.setDimensions(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final void setDimensions(float f, float f2) {
        try {
            this.zzcw.zza(f, f2);
        } catch (float f3) {
            throw new RuntimeRemoteException(f3);
        }
    }

    public final float getWidth() {
        try {
            return this.zzcw.getWidth();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getHeight() {
        try {
            return this.zzcw.getHeight();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPositionFromBounds(LatLngBounds latLngBounds) {
        try {
            this.zzcw.setPositionFromBounds(latLngBounds);
        } catch (LatLngBounds latLngBounds2) {
            throw new RuntimeRemoteException(latLngBounds2);
        }
    }

    public final LatLngBounds getBounds() {
        try {
            return this.zzcw.getBounds();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setBearing(float f) {
        try {
            this.zzcw.setBearing(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getBearing() {
        try {
            return this.zzcw.getBearing();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setZIndex(float f) {
        try {
            this.zzcw.setZIndex(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getZIndex() {
        try {
            return this.zzcw.getZIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setVisible(boolean z) {
        try {
            this.zzcw.setVisible(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isVisible() {
        try {
            return this.zzcw.isVisible();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setClickable(boolean z) {
        try {
            this.zzcw.setClickable(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isClickable() {
        try {
            return this.zzcw.isClickable();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTransparency(float f) {
        try {
            this.zzcw.setTransparency(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getTransparency() {
        try {
            return this.zzcw.getTransparency();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTag(@Nullable Object obj) {
        try {
            this.zzcw.zze(ObjectWrapper.wrap(obj));
        } catch (Object obj2) {
            throw new RuntimeRemoteException(obj2);
        }
    }

    @Nullable
    public final Object getTag() {
        try {
            return ObjectWrapper.unwrap(this.zzcw.zzk());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof GroundOverlay)) {
            return null;
        }
        try {
            return this.zzcw.zzb(((GroundOverlay) obj).zzcw);
        } catch (Object obj2) {
            throw new RuntimeRemoteException(obj2);
        }
    }

    public final int hashCode() {
        try {
            return this.zzcw.zzj();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
