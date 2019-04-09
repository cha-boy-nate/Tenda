package com.google.android.gms.maps;

import android.graphics.Point;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;
import com.google.android.gms.maps.model.StreetViewSource;

public class StreetViewPanorama {
    private final IStreetViewPanoramaDelegate zzbo;

    public interface OnStreetViewPanoramaCameraChangeListener {
        void onStreetViewPanoramaCameraChange(StreetViewPanoramaCamera streetViewPanoramaCamera);
    }

    public interface OnStreetViewPanoramaChangeListener {
        void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation);
    }

    public interface OnStreetViewPanoramaClickListener {
        void onStreetViewPanoramaClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation);
    }

    public interface OnStreetViewPanoramaLongClickListener {
        void onStreetViewPanoramaLongClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation);
    }

    public StreetViewPanorama(@NonNull IStreetViewPanoramaDelegate iStreetViewPanoramaDelegate) {
        this.zzbo = (IStreetViewPanoramaDelegate) Preconditions.checkNotNull(iStreetViewPanoramaDelegate, "delegate");
    }

    public boolean isZoomGesturesEnabled() {
        try {
            return this.zzbo.isZoomGesturesEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setZoomGesturesEnabled(boolean z) {
        try {
            this.zzbo.enableZoom(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public boolean isPanningGesturesEnabled() {
        try {
            return this.zzbo.isPanningGesturesEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setPanningGesturesEnabled(boolean z) {
        try {
            this.zzbo.enablePanning(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public boolean isUserNavigationEnabled() {
        try {
            return this.zzbo.isUserNavigationEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setUserNavigationEnabled(boolean z) {
        try {
            this.zzbo.enableUserNavigation(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public boolean isStreetNamesEnabled() {
        try {
            return this.zzbo.isStreetNamesEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setStreetNamesEnabled(boolean z) {
        try {
            this.zzbo.enableStreetNames(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public void animateTo(StreetViewPanoramaCamera streetViewPanoramaCamera, long j) {
        try {
            this.zzbo.animateTo(streetViewPanoramaCamera, j);
        } catch (StreetViewPanoramaCamera streetViewPanoramaCamera2) {
            throw new RuntimeRemoteException(streetViewPanoramaCamera2);
        }
    }

    public StreetViewPanoramaCamera getPanoramaCamera() {
        try {
            return this.zzbo.getPanoramaCamera();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setPosition(String str) {
        try {
            this.zzbo.setPositionWithID(str);
        } catch (String str2) {
            throw new RuntimeRemoteException(str2);
        }
    }

    public void setPosition(LatLng latLng) {
        try {
            this.zzbo.setPosition(latLng);
        } catch (LatLng latLng2) {
            throw new RuntimeRemoteException(latLng2);
        }
    }

    public void setPosition(LatLng latLng, int i) {
        try {
            this.zzbo.setPositionWithRadius(latLng, i);
        } catch (LatLng latLng2) {
            throw new RuntimeRemoteException(latLng2);
        }
    }

    public void setPosition(LatLng latLng, StreetViewSource streetViewSource) {
        try {
            this.zzbo.setPositionWithSource(latLng, streetViewSource);
        } catch (LatLng latLng2) {
            throw new RuntimeRemoteException(latLng2);
        }
    }

    public void setPosition(LatLng latLng, int i, StreetViewSource streetViewSource) {
        try {
            this.zzbo.setPositionWithRadiusAndSource(latLng, i, streetViewSource);
        } catch (LatLng latLng2) {
            throw new RuntimeRemoteException(latLng2);
        }
    }

    public StreetViewPanoramaLocation getLocation() {
        try {
            return this.zzbo.getStreetViewPanoramaLocation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public StreetViewPanoramaOrientation pointToOrientation(Point point) {
        try {
            return this.zzbo.pointToOrientation(ObjectWrapper.wrap(point));
        } catch (Point point2) {
            throw new RuntimeRemoteException(point2);
        }
    }

    public Point orientationToPoint(StreetViewPanoramaOrientation streetViewPanoramaOrientation) {
        try {
            streetViewPanoramaOrientation = this.zzbo.orientationToPoint(streetViewPanoramaOrientation);
            if (streetViewPanoramaOrientation == null) {
                return null;
            }
            return (Point) ObjectWrapper.unwrap(streetViewPanoramaOrientation);
        } catch (StreetViewPanoramaOrientation streetViewPanoramaOrientation2) {
            throw new RuntimeRemoteException(streetViewPanoramaOrientation2);
        }
    }

    public final void setOnStreetViewPanoramaChangeListener(OnStreetViewPanoramaChangeListener onStreetViewPanoramaChangeListener) {
        if (onStreetViewPanoramaChangeListener == null) {
            try {
                this.zzbo.setOnStreetViewPanoramaChangeListener(null);
                return;
            } catch (OnStreetViewPanoramaChangeListener onStreetViewPanoramaChangeListener2) {
                throw new RuntimeRemoteException(onStreetViewPanoramaChangeListener2);
            }
        }
        this.zzbo.setOnStreetViewPanoramaChangeListener(new zzad(this, onStreetViewPanoramaChangeListener2));
    }

    public final void setOnStreetViewPanoramaCameraChangeListener(OnStreetViewPanoramaCameraChangeListener onStreetViewPanoramaCameraChangeListener) {
        if (onStreetViewPanoramaCameraChangeListener == null) {
            try {
                this.zzbo.setOnStreetViewPanoramaCameraChangeListener(null);
                return;
            } catch (OnStreetViewPanoramaCameraChangeListener onStreetViewPanoramaCameraChangeListener2) {
                throw new RuntimeRemoteException(onStreetViewPanoramaCameraChangeListener2);
            }
        }
        this.zzbo.setOnStreetViewPanoramaCameraChangeListener(new zzae(this, onStreetViewPanoramaCameraChangeListener2));
    }

    public final void setOnStreetViewPanoramaClickListener(OnStreetViewPanoramaClickListener onStreetViewPanoramaClickListener) {
        if (onStreetViewPanoramaClickListener == null) {
            try {
                this.zzbo.setOnStreetViewPanoramaClickListener(null);
                return;
            } catch (OnStreetViewPanoramaClickListener onStreetViewPanoramaClickListener2) {
                throw new RuntimeRemoteException(onStreetViewPanoramaClickListener2);
            }
        }
        this.zzbo.setOnStreetViewPanoramaClickListener(new zzaf(this, onStreetViewPanoramaClickListener2));
    }

    public final void setOnStreetViewPanoramaLongClickListener(OnStreetViewPanoramaLongClickListener onStreetViewPanoramaLongClickListener) {
        if (onStreetViewPanoramaLongClickListener == null) {
            try {
                this.zzbo.setOnStreetViewPanoramaLongClickListener(null);
                return;
            } catch (OnStreetViewPanoramaLongClickListener onStreetViewPanoramaLongClickListener2) {
                throw new RuntimeRemoteException(onStreetViewPanoramaLongClickListener2);
            }
        }
        this.zzbo.setOnStreetViewPanoramaLongClickListener(new zzag(this, onStreetViewPanoramaLongClickListener2));
    }
}
