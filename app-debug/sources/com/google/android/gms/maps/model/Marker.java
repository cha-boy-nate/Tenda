package com.google.android.gms.maps.model;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.maps.zzt;

public final class Marker {
    private final zzt zzdm;

    public Marker(zzt zzt) {
        this.zzdm = (zzt) Preconditions.checkNotNull(zzt);
    }

    public final void remove() {
        try {
            this.zzdm.remove();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getId() {
        try {
            return this.zzdm.getId();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPosition(@NonNull LatLng latLng) {
        if (latLng != null) {
            try {
                this.zzdm.setPosition(latLng);
                return;
            } catch (LatLng latLng2) {
                throw new RuntimeRemoteException(latLng2);
            }
        }
        throw new IllegalArgumentException("latlng cannot be null - a position is required.");
    }

    public final LatLng getPosition() {
        try {
            return this.zzdm.getPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setZIndex(float f) {
        try {
            this.zzdm.setZIndex(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getZIndex() {
        try {
            return this.zzdm.getZIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setIcon(@Nullable BitmapDescriptor bitmapDescriptor) {
        if (bitmapDescriptor == null) {
            try {
                this.zzdm.zzg(null);
                return;
            } catch (BitmapDescriptor bitmapDescriptor2) {
                throw new RuntimeRemoteException(bitmapDescriptor2);
            }
        }
        this.zzdm.zzg(bitmapDescriptor2.zzb());
    }

    public final void setAnchor(float f, float f2) {
        try {
            this.zzdm.setAnchor(f, f2);
        } catch (float f3) {
            throw new RuntimeRemoteException(f3);
        }
    }

    public final void setInfoWindowAnchor(float f, float f2) {
        try {
            this.zzdm.setInfoWindowAnchor(f, f2);
        } catch (float f3) {
            throw new RuntimeRemoteException(f3);
        }
    }

    public final void setTitle(@Nullable String str) {
        try {
            this.zzdm.setTitle(str);
        } catch (String str2) {
            throw new RuntimeRemoteException(str2);
        }
    }

    public final String getTitle() {
        try {
            return this.zzdm.getTitle();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setSnippet(@Nullable String str) {
        try {
            this.zzdm.setSnippet(str);
        } catch (String str2) {
            throw new RuntimeRemoteException(str2);
        }
    }

    public final String getSnippet() {
        try {
            return this.zzdm.getSnippet();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setDraggable(boolean z) {
        try {
            this.zzdm.setDraggable(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isDraggable() {
        try {
            return this.zzdm.isDraggable();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void showInfoWindow() {
        try {
            this.zzdm.showInfoWindow();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void hideInfoWindow() {
        try {
            this.zzdm.hideInfoWindow();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isInfoWindowShown() {
        try {
            return this.zzdm.isInfoWindowShown();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setVisible(boolean z) {
        try {
            this.zzdm.setVisible(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isVisible() {
        try {
            return this.zzdm.isVisible();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setFlat(boolean z) {
        try {
            this.zzdm.setFlat(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isFlat() {
        try {
            return this.zzdm.isFlat();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setRotation(float f) {
        try {
            this.zzdm.setRotation(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getRotation() {
        try {
            return this.zzdm.getRotation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setAlpha(float f) {
        try {
            this.zzdm.setAlpha(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getAlpha() {
        try {
            return this.zzdm.getAlpha();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTag(@Nullable Object obj) {
        try {
            this.zzdm.zze(ObjectWrapper.wrap(obj));
        } catch (Object obj2) {
            throw new RuntimeRemoteException(obj2);
        }
    }

    @Nullable
    public final Object getTag() {
        try {
            return ObjectWrapper.unwrap(this.zzdm.zzk());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Marker)) {
            return null;
        }
        try {
            return this.zzdm.zzj(((Marker) obj).zzdm);
        } catch (Object obj2) {
            throw new RuntimeRemoteException(obj2);
        }
    }

    public final int hashCode() {
        try {
            return this.zzdm.zzj();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
