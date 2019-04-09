package com.google.android.gms.maps.model;

import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.maps.zzh;
import java.util.List;

public final class Circle {
    private final zzh zzco;

    public Circle(zzh zzh) {
        this.zzco = (zzh) Preconditions.checkNotNull(zzh);
    }

    public final void remove() {
        try {
            this.zzco.remove();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getId() {
        try {
            return this.zzco.getId();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setCenter(LatLng latLng) {
        try {
            this.zzco.setCenter(latLng);
        } catch (LatLng latLng2) {
            throw new RuntimeRemoteException(latLng2);
        }
    }

    public final LatLng getCenter() {
        try {
            return this.zzco.getCenter();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setRadius(double d) {
        try {
            this.zzco.setRadius(d);
        } catch (double d2) {
            throw new RuntimeRemoteException(d2);
        }
    }

    public final double getRadius() {
        try {
            return this.zzco.getRadius();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setStrokeWidth(float f) {
        try {
            this.zzco.setStrokeWidth(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getStrokeWidth() {
        try {
            return this.zzco.getStrokeWidth();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setStrokeColor(int i) {
        try {
            this.zzco.setStrokeColor(i);
        } catch (int i2) {
            throw new RuntimeRemoteException(i2);
        }
    }

    public final int getStrokeColor() {
        try {
            return this.zzco.getStrokeColor();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setStrokePattern(@Nullable List<PatternItem> list) {
        try {
            this.zzco.setStrokePattern(list);
        } catch (List<PatternItem> list2) {
            throw new RuntimeRemoteException(list2);
        }
    }

    @Nullable
    public final List<PatternItem> getStrokePattern() {
        try {
            return PatternItem.zza(this.zzco.getStrokePattern());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setFillColor(int i) {
        try {
            this.zzco.setFillColor(i);
        } catch (int i2) {
            throw new RuntimeRemoteException(i2);
        }
    }

    public final int getFillColor() {
        try {
            return this.zzco.getFillColor();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setZIndex(float f) {
        try {
            this.zzco.setZIndex(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final float getZIndex() {
        try {
            return this.zzco.getZIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setVisible(boolean z) {
        try {
            this.zzco.setVisible(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isVisible() {
        try {
            return this.zzco.isVisible();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setClickable(boolean z) {
        try {
            this.zzco.setClickable(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isClickable() {
        try {
            return this.zzco.isClickable();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTag(@Nullable Object obj) {
        try {
            this.zzco.zze(ObjectWrapper.wrap(obj));
        } catch (Object obj2) {
            throw new RuntimeRemoteException(obj2);
        }
    }

    @Nullable
    public final Object getTag() {
        try {
            return ObjectWrapper.unwrap(this.zzco.zzk());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Circle)) {
            return null;
        }
        try {
            return this.zzco.zzb(((Circle) obj).zzco);
        } catch (Object obj2) {
            throw new RuntimeRemoteException(obj2);
        }
    }

    public final int hashCode() {
        try {
            return this.zzco.zzj();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
