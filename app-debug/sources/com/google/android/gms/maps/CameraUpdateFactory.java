package com.google.android.gms.maps;

import android.graphics.Point;
import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public final class CameraUpdateFactory {
    private static ICameraUpdateFactoryDelegate zzf;

    private CameraUpdateFactory() {
    }

    private static ICameraUpdateFactoryDelegate zzc() {
        return (ICameraUpdateFactoryDelegate) Preconditions.checkNotNull(zzf, "CameraUpdateFactory is not initialized");
    }

    public static void zza(ICameraUpdateFactoryDelegate iCameraUpdateFactoryDelegate) {
        zzf = (ICameraUpdateFactoryDelegate) Preconditions.checkNotNull(iCameraUpdateFactoryDelegate);
    }

    public static CameraUpdate zoomIn() {
        try {
            return new CameraUpdate(zzc().zoomIn());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static CameraUpdate zoomOut() {
        try {
            return new CameraUpdate(zzc().zoomOut());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static CameraUpdate scrollBy(float f, float f2) {
        try {
            return new CameraUpdate(zzc().scrollBy(f, f2));
        } catch (float f3) {
            throw new RuntimeRemoteException(f3);
        }
    }

    public static CameraUpdate zoomTo(float f) {
        try {
            return new CameraUpdate(zzc().zoomTo(f));
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public static CameraUpdate zoomBy(float f) {
        try {
            return new CameraUpdate(zzc().zoomBy(f));
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public static CameraUpdate zoomBy(float f, Point point) {
        try {
            return new CameraUpdate(zzc().zoomByWithFocus(f, point.x, point.y));
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public static CameraUpdate newCameraPosition(CameraPosition cameraPosition) {
        try {
            return new CameraUpdate(zzc().newCameraPosition(cameraPosition));
        } catch (CameraPosition cameraPosition2) {
            throw new RuntimeRemoteException(cameraPosition2);
        }
    }

    public static CameraUpdate newLatLng(LatLng latLng) {
        try {
            return new CameraUpdate(zzc().newLatLng(latLng));
        } catch (LatLng latLng2) {
            throw new RuntimeRemoteException(latLng2);
        }
    }

    public static CameraUpdate newLatLngZoom(LatLng latLng, float f) {
        try {
            return new CameraUpdate(zzc().newLatLngZoom(latLng, f));
        } catch (LatLng latLng2) {
            throw new RuntimeRemoteException(latLng2);
        }
    }

    public static CameraUpdate newLatLngBounds(LatLngBounds latLngBounds, int i) {
        try {
            return new CameraUpdate(zzc().newLatLngBounds(latLngBounds, i));
        } catch (LatLngBounds latLngBounds2) {
            throw new RuntimeRemoteException(latLngBounds2);
        }
    }

    public static CameraUpdate newLatLngBounds(LatLngBounds latLngBounds, int i, int i2, int i3) {
        try {
            return new CameraUpdate(zzc().newLatLngBoundsWithSize(latLngBounds, i, i2, i3));
        } catch (LatLngBounds latLngBounds2) {
            throw new RuntimeRemoteException(latLngBounds2);
        }
    }
}
