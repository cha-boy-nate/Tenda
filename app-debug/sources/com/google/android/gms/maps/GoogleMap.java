package com.google.android.gms.maps;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.view.View;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.maps.zzn;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.zzd;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

public final class GoogleMap {
    public static final int MAP_TYPE_HYBRID = 4;
    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    private final IGoogleMapDelegate zzg;
    private UiSettings zzh;

    public interface CancelableCallback {
        void onCancel();

        void onFinish();
    }

    public interface InfoWindowAdapter {
        View getInfoContents(Marker marker);

        View getInfoWindow(Marker marker);
    }

    @Deprecated
    public interface OnCameraChangeListener {
        void onCameraChange(CameraPosition cameraPosition);
    }

    public interface OnCameraIdleListener {
        void onCameraIdle();
    }

    public interface OnCameraMoveCanceledListener {
        void onCameraMoveCanceled();
    }

    public interface OnCameraMoveListener {
        void onCameraMove();
    }

    public interface OnCameraMoveStartedListener {
        public static final int REASON_API_ANIMATION = 2;
        public static final int REASON_DEVELOPER_ANIMATION = 3;
        public static final int REASON_GESTURE = 1;

        void onCameraMoveStarted(int i);
    }

    public interface OnCircleClickListener {
        void onCircleClick(Circle circle);
    }

    public interface OnGroundOverlayClickListener {
        void onGroundOverlayClick(GroundOverlay groundOverlay);
    }

    public interface OnIndoorStateChangeListener {
        void onIndoorBuildingFocused();

        void onIndoorLevelActivated(IndoorBuilding indoorBuilding);
    }

    public interface OnInfoWindowClickListener {
        void onInfoWindowClick(Marker marker);
    }

    public interface OnInfoWindowCloseListener {
        void onInfoWindowClose(Marker marker);
    }

    public interface OnInfoWindowLongClickListener {
        void onInfoWindowLongClick(Marker marker);
    }

    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);
    }

    public interface OnMapLoadedCallback {
        void onMapLoaded();
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(LatLng latLng);
    }

    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker);
    }

    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    public interface OnMyLocationButtonClickListener {
        boolean onMyLocationButtonClick();
    }

    @Deprecated
    public interface OnMyLocationChangeListener {
        void onMyLocationChange(Location location);
    }

    public interface OnMyLocationClickListener {
        void onMyLocationClick(@NonNull Location location);
    }

    public interface OnPoiClickListener {
        void onPoiClick(PointOfInterest pointOfInterest);
    }

    public interface OnPolygonClickListener {
        void onPolygonClick(Polygon polygon);
    }

    public interface OnPolylineClickListener {
        void onPolylineClick(Polyline polyline);
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    private static final class zza extends zzd {
        private final CancelableCallback zzai;

        zza(CancelableCallback cancelableCallback) {
            this.zzai = cancelableCallback;
        }

        public final void onFinish() {
            this.zzai.onFinish();
        }

        public final void onCancel() {
            this.zzai.onCancel();
        }
    }

    public GoogleMap(IGoogleMapDelegate iGoogleMapDelegate) {
        this.zzg = (IGoogleMapDelegate) Preconditions.checkNotNull(iGoogleMapDelegate);
    }

    public final CameraPosition getCameraPosition() {
        try {
            return this.zzg.getCameraPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMaxZoomLevel() {
        try {
            return this.zzg.getMaxZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMinZoomLevel() {
        try {
            return this.zzg.getMinZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void moveCamera(CameraUpdate cameraUpdate) {
        try {
            this.zzg.moveCamera(cameraUpdate.zzb());
        } catch (CameraUpdate cameraUpdate2) {
            throw new RuntimeRemoteException(cameraUpdate2);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate) {
        try {
            this.zzg.animateCamera(cameraUpdate.zzb());
        } catch (CameraUpdate cameraUpdate2) {
            throw new RuntimeRemoteException(cameraUpdate2);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate, CancelableCallback cancelableCallback) {
        try {
            this.zzg.animateCameraWithCallback(cameraUpdate.zzb(), cancelableCallback == null ? null : new zza(cancelableCallback));
        } catch (CameraUpdate cameraUpdate2) {
            throw new RuntimeRemoteException(cameraUpdate2);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate, int i, CancelableCallback cancelableCallback) {
        try {
            this.zzg.animateCameraWithDurationAndCallback(cameraUpdate.zzb(), i, cancelableCallback == null ? null : new zza(cancelableCallback));
        } catch (CameraUpdate cameraUpdate2) {
            throw new RuntimeRemoteException(cameraUpdate2);
        }
    }

    public final void stopAnimation() {
        try {
            this.zzg.stopAnimation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polyline addPolyline(PolylineOptions polylineOptions) {
        try {
            return new Polyline(this.zzg.addPolyline(polylineOptions));
        } catch (PolylineOptions polylineOptions2) {
            throw new RuntimeRemoteException(polylineOptions2);
        }
    }

    public final Polygon addPolygon(PolygonOptions polygonOptions) {
        try {
            return new Polygon(this.zzg.addPolygon(polygonOptions));
        } catch (PolygonOptions polygonOptions2) {
            throw new RuntimeRemoteException(polygonOptions2);
        }
    }

    public final Circle addCircle(CircleOptions circleOptions) {
        try {
            return new Circle(this.zzg.addCircle(circleOptions));
        } catch (CircleOptions circleOptions2) {
            throw new RuntimeRemoteException(circleOptions2);
        }
    }

    public final Marker addMarker(MarkerOptions markerOptions) {
        try {
            markerOptions = this.zzg.addMarker(markerOptions);
            if (markerOptions != null) {
                return new Marker(markerOptions);
            }
            return null;
        } catch (MarkerOptions markerOptions2) {
            throw new RuntimeRemoteException(markerOptions2);
        }
    }

    public final GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions) {
        try {
            groundOverlayOptions = this.zzg.addGroundOverlay(groundOverlayOptions);
            if (groundOverlayOptions != null) {
                return new GroundOverlay(groundOverlayOptions);
            }
            return null;
        } catch (GroundOverlayOptions groundOverlayOptions2) {
            throw new RuntimeRemoteException(groundOverlayOptions2);
        }
    }

    public final TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) {
        try {
            tileOverlayOptions = this.zzg.addTileOverlay(tileOverlayOptions);
            if (tileOverlayOptions != null) {
                return new TileOverlay(tileOverlayOptions);
            }
            return null;
        } catch (TileOverlayOptions tileOverlayOptions2) {
            throw new RuntimeRemoteException(tileOverlayOptions2);
        }
    }

    public final void clear() {
        try {
            this.zzg.clear();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final IndoorBuilding getFocusedBuilding() {
        try {
            zzn focusedBuilding = this.zzg.getFocusedBuilding();
            if (focusedBuilding != null) {
                return new IndoorBuilding(focusedBuilding);
            }
            return null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnIndoorStateChangeListener(OnIndoorStateChangeListener onIndoorStateChangeListener) {
        if (onIndoorStateChangeListener == null) {
            try {
                this.zzg.setOnIndoorStateChangeListener(null);
                return;
            } catch (OnIndoorStateChangeListener onIndoorStateChangeListener2) {
                throw new RuntimeRemoteException(onIndoorStateChangeListener2);
            }
        }
        this.zzg.setOnIndoorStateChangeListener(new zza(this, onIndoorStateChangeListener2));
    }

    public final int getMapType() {
        try {
            return this.zzg.getMapType();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMapType(int i) {
        try {
            this.zzg.setMapType(i);
        } catch (int i2) {
            throw new RuntimeRemoteException(i2);
        }
    }

    public final boolean isTrafficEnabled() {
        try {
            return this.zzg.isTrafficEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTrafficEnabled(boolean z) {
        try {
            this.zzg.setTrafficEnabled(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isIndoorEnabled() {
        try {
            return this.zzg.isIndoorEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean setIndoorEnabled(boolean z) {
        try {
            return this.zzg.setIndoorEnabled(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isBuildingsEnabled() {
        try {
            return this.zzg.isBuildingsEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setBuildingsEnabled(boolean z) {
        try {
            this.zzg.setBuildingsEnabled(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    public final boolean isMyLocationEnabled() {
        try {
            return this.zzg.isMyLocationEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public final void setMyLocationEnabled(boolean z) {
        try {
            this.zzg.setMyLocationEnabled(z);
        } catch (boolean z2) {
            throw new RuntimeRemoteException(z2);
        }
    }

    @Deprecated
    public final Location getMyLocation() {
        try {
            return this.zzg.getMyLocation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setLocationSource(LocationSource locationSource) {
        if (locationSource == null) {
            try {
                this.zzg.setLocationSource(null);
                return;
            } catch (LocationSource locationSource2) {
                throw new RuntimeRemoteException(locationSource2);
            }
        }
        this.zzg.setLocationSource(new zzl(this, locationSource2));
    }

    public final UiSettings getUiSettings() {
        try {
            if (this.zzh == null) {
                this.zzh = new UiSettings(this.zzg.getUiSettings());
            }
            return this.zzh;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Projection getProjection() {
        try {
            return new Projection(this.zzg.getProjection());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Deprecated
    public final void setOnCameraChangeListener(@Nullable OnCameraChangeListener onCameraChangeListener) {
        if (onCameraChangeListener == null) {
            try {
                this.zzg.setOnCameraChangeListener(null);
                return;
            } catch (OnCameraChangeListener onCameraChangeListener2) {
                throw new RuntimeRemoteException(onCameraChangeListener2);
            }
        }
        this.zzg.setOnCameraChangeListener(new zzt(this, onCameraChangeListener2));
    }

    public final void setOnCameraMoveStartedListener(@Nullable OnCameraMoveStartedListener onCameraMoveStartedListener) {
        if (onCameraMoveStartedListener == null) {
            try {
                this.zzg.setOnCameraMoveStartedListener(null);
                return;
            } catch (OnCameraMoveStartedListener onCameraMoveStartedListener2) {
                throw new RuntimeRemoteException(onCameraMoveStartedListener2);
            }
        }
        this.zzg.setOnCameraMoveStartedListener(new zzu(this, onCameraMoveStartedListener2));
    }

    public final void setOnCameraMoveListener(@Nullable OnCameraMoveListener onCameraMoveListener) {
        if (onCameraMoveListener == null) {
            try {
                this.zzg.setOnCameraMoveListener(null);
                return;
            } catch (OnCameraMoveListener onCameraMoveListener2) {
                throw new RuntimeRemoteException(onCameraMoveListener2);
            }
        }
        this.zzg.setOnCameraMoveListener(new zzv(this, onCameraMoveListener2));
    }

    public final void setOnCameraMoveCanceledListener(@Nullable OnCameraMoveCanceledListener onCameraMoveCanceledListener) {
        if (onCameraMoveCanceledListener == null) {
            try {
                this.zzg.setOnCameraMoveCanceledListener(null);
                return;
            } catch (OnCameraMoveCanceledListener onCameraMoveCanceledListener2) {
                throw new RuntimeRemoteException(onCameraMoveCanceledListener2);
            }
        }
        this.zzg.setOnCameraMoveCanceledListener(new zzw(this, onCameraMoveCanceledListener2));
    }

    public final void setOnCameraIdleListener(@Nullable OnCameraIdleListener onCameraIdleListener) {
        if (onCameraIdleListener == null) {
            try {
                this.zzg.setOnCameraIdleListener(null);
                return;
            } catch (OnCameraIdleListener onCameraIdleListener2) {
                throw new RuntimeRemoteException(onCameraIdleListener2);
            }
        }
        this.zzg.setOnCameraIdleListener(new zzx(this, onCameraIdleListener2));
    }

    public final void setOnMapClickListener(@Nullable OnMapClickListener onMapClickListener) {
        if (onMapClickListener == null) {
            try {
                this.zzg.setOnMapClickListener(null);
                return;
            } catch (OnMapClickListener onMapClickListener2) {
                throw new RuntimeRemoteException(onMapClickListener2);
            }
        }
        this.zzg.setOnMapClickListener(new zzy(this, onMapClickListener2));
    }

    public final void setOnMapLongClickListener(@Nullable OnMapLongClickListener onMapLongClickListener) {
        if (onMapLongClickListener == null) {
            try {
                this.zzg.setOnMapLongClickListener(null);
                return;
            } catch (OnMapLongClickListener onMapLongClickListener2) {
                throw new RuntimeRemoteException(onMapLongClickListener2);
            }
        }
        this.zzg.setOnMapLongClickListener(new zzz(this, onMapLongClickListener2));
    }

    public final void setOnMarkerClickListener(@Nullable OnMarkerClickListener onMarkerClickListener) {
        if (onMarkerClickListener == null) {
            try {
                this.zzg.setOnMarkerClickListener(null);
                return;
            } catch (OnMarkerClickListener onMarkerClickListener2) {
                throw new RuntimeRemoteException(onMarkerClickListener2);
            }
        }
        this.zzg.setOnMarkerClickListener(new zzb(this, onMarkerClickListener2));
    }

    public final void setOnMarkerDragListener(@Nullable OnMarkerDragListener onMarkerDragListener) {
        if (onMarkerDragListener == null) {
            try {
                this.zzg.setOnMarkerDragListener(null);
                return;
            } catch (OnMarkerDragListener onMarkerDragListener2) {
                throw new RuntimeRemoteException(onMarkerDragListener2);
            }
        }
        this.zzg.setOnMarkerDragListener(new zzc(this, onMarkerDragListener2));
    }

    public final void setOnInfoWindowClickListener(@Nullable OnInfoWindowClickListener onInfoWindowClickListener) {
        if (onInfoWindowClickListener == null) {
            try {
                this.zzg.setOnInfoWindowClickListener(null);
                return;
            } catch (OnInfoWindowClickListener onInfoWindowClickListener2) {
                throw new RuntimeRemoteException(onInfoWindowClickListener2);
            }
        }
        this.zzg.setOnInfoWindowClickListener(new zzd(this, onInfoWindowClickListener2));
    }

    public final void setOnInfoWindowLongClickListener(@Nullable OnInfoWindowLongClickListener onInfoWindowLongClickListener) {
        if (onInfoWindowLongClickListener == null) {
            try {
                this.zzg.setOnInfoWindowLongClickListener(null);
                return;
            } catch (OnInfoWindowLongClickListener onInfoWindowLongClickListener2) {
                throw new RuntimeRemoteException(onInfoWindowLongClickListener2);
            }
        }
        this.zzg.setOnInfoWindowLongClickListener(new zze(this, onInfoWindowLongClickListener2));
    }

    public final void setOnInfoWindowCloseListener(@Nullable OnInfoWindowCloseListener onInfoWindowCloseListener) {
        if (onInfoWindowCloseListener == null) {
            try {
                this.zzg.setOnInfoWindowCloseListener(null);
                return;
            } catch (OnInfoWindowCloseListener onInfoWindowCloseListener2) {
                throw new RuntimeRemoteException(onInfoWindowCloseListener2);
            }
        }
        this.zzg.setOnInfoWindowCloseListener(new zzf(this, onInfoWindowCloseListener2));
    }

    public final void setInfoWindowAdapter(InfoWindowAdapter infoWindowAdapter) {
        if (infoWindowAdapter == null) {
            try {
                this.zzg.setInfoWindowAdapter(null);
                return;
            } catch (InfoWindowAdapter infoWindowAdapter2) {
                throw new RuntimeRemoteException(infoWindowAdapter2);
            }
        }
        this.zzg.setInfoWindowAdapter(new zzg(this, infoWindowAdapter2));
    }

    @Deprecated
    public final void setOnMyLocationChangeListener(@Nullable OnMyLocationChangeListener onMyLocationChangeListener) {
        if (onMyLocationChangeListener == null) {
            try {
                this.zzg.setOnMyLocationChangeListener(null);
                return;
            } catch (OnMyLocationChangeListener onMyLocationChangeListener2) {
                throw new RuntimeRemoteException(onMyLocationChangeListener2);
            }
        }
        this.zzg.setOnMyLocationChangeListener(new zzh(this, onMyLocationChangeListener2));
    }

    public final void setOnMyLocationButtonClickListener(@Nullable OnMyLocationButtonClickListener onMyLocationButtonClickListener) {
        if (onMyLocationButtonClickListener == null) {
            try {
                this.zzg.setOnMyLocationButtonClickListener(null);
                return;
            } catch (OnMyLocationButtonClickListener onMyLocationButtonClickListener2) {
                throw new RuntimeRemoteException(onMyLocationButtonClickListener2);
            }
        }
        this.zzg.setOnMyLocationButtonClickListener(new zzi(this, onMyLocationButtonClickListener2));
    }

    public final void setOnMyLocationClickListener(@Nullable OnMyLocationClickListener onMyLocationClickListener) {
        if (onMyLocationClickListener == null) {
            try {
                this.zzg.setOnMyLocationClickListener(null);
                return;
            } catch (OnMyLocationClickListener onMyLocationClickListener2) {
                throw new RuntimeRemoteException(onMyLocationClickListener2);
            }
        }
        this.zzg.setOnMyLocationClickListener(new zzj(this, onMyLocationClickListener2));
    }

    public final void setOnMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback) {
        if (onMapLoadedCallback == null) {
            try {
                this.zzg.setOnMapLoadedCallback(null);
                return;
            } catch (OnMapLoadedCallback onMapLoadedCallback2) {
                throw new RuntimeRemoteException(onMapLoadedCallback2);
            }
        }
        this.zzg.setOnMapLoadedCallback(new zzk(this, onMapLoadedCallback2));
    }

    public final void setOnGroundOverlayClickListener(OnGroundOverlayClickListener onGroundOverlayClickListener) {
        if (onGroundOverlayClickListener == null) {
            try {
                this.zzg.setOnGroundOverlayClickListener(null);
                return;
            } catch (OnGroundOverlayClickListener onGroundOverlayClickListener2) {
                throw new RuntimeRemoteException(onGroundOverlayClickListener2);
            }
        }
        this.zzg.setOnGroundOverlayClickListener(new zzn(this, onGroundOverlayClickListener2));
    }

    public final void setOnCircleClickListener(OnCircleClickListener onCircleClickListener) {
        if (onCircleClickListener == null) {
            try {
                this.zzg.setOnCircleClickListener(null);
                return;
            } catch (OnCircleClickListener onCircleClickListener2) {
                throw new RuntimeRemoteException(onCircleClickListener2);
            }
        }
        this.zzg.setOnCircleClickListener(new zzo(this, onCircleClickListener2));
    }

    public final void setOnPolygonClickListener(OnPolygonClickListener onPolygonClickListener) {
        if (onPolygonClickListener == null) {
            try {
                this.zzg.setOnPolygonClickListener(null);
                return;
            } catch (OnPolygonClickListener onPolygonClickListener2) {
                throw new RuntimeRemoteException(onPolygonClickListener2);
            }
        }
        this.zzg.setOnPolygonClickListener(new zzp(this, onPolygonClickListener2));
    }

    public final void setOnPolylineClickListener(OnPolylineClickListener onPolylineClickListener) {
        if (onPolylineClickListener == null) {
            try {
                this.zzg.setOnPolylineClickListener(null);
                return;
            } catch (OnPolylineClickListener onPolylineClickListener2) {
                throw new RuntimeRemoteException(onPolylineClickListener2);
            }
        }
        this.zzg.setOnPolylineClickListener(new zzq(this, onPolylineClickListener2));
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback) {
        snapshot(snapshotReadyCallback, null);
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback, Bitmap bitmap) {
        try {
            this.zzg.snapshot(new zzr(this, snapshotReadyCallback), (ObjectWrapper) (bitmap != null ? ObjectWrapper.wrap(bitmap) : null));
        } catch (SnapshotReadyCallback snapshotReadyCallback2) {
            throw new RuntimeRemoteException(snapshotReadyCallback2);
        }
    }

    public final void setPadding(int i, int i2, int i3, int i4) {
        try {
            this.zzg.setPadding(i, i2, i3, i4);
        } catch (int i5) {
            throw new RuntimeRemoteException(i5);
        }
    }

    public final void setContentDescription(String str) {
        try {
            this.zzg.setContentDescription(str);
        } catch (String str2) {
            throw new RuntimeRemoteException(str2);
        }
    }

    public final void setOnPoiClickListener(OnPoiClickListener onPoiClickListener) {
        if (onPoiClickListener == null) {
            try {
                this.zzg.setOnPoiClickListener(null);
                return;
            } catch (OnPoiClickListener onPoiClickListener2) {
                throw new RuntimeRemoteException(onPoiClickListener2);
            }
        }
        this.zzg.setOnPoiClickListener(new zzs(this, onPoiClickListener2));
    }

    public final boolean setMapStyle(@Nullable MapStyleOptions mapStyleOptions) {
        try {
            return this.zzg.setMapStyle(mapStyleOptions);
        } catch (MapStyleOptions mapStyleOptions2) {
            throw new RuntimeRemoteException(mapStyleOptions2);
        }
    }

    public final void setMinZoomPreference(float f) {
        try {
            this.zzg.setMinZoomPreference(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final void setMaxZoomPreference(float f) {
        try {
            this.zzg.setMaxZoomPreference(f);
        } catch (float f2) {
            throw new RuntimeRemoteException(f2);
        }
    }

    public final void resetMinMaxZoomPreference() {
        try {
            this.zzg.resetMinMaxZoomPreference();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setLatLngBoundsForCameraTarget(LatLngBounds latLngBounds) {
        try {
            this.zzg.setLatLngBoundsForCameraTarget(latLngBounds);
        } catch (LatLngBounds latLngBounds2) {
            throw new RuntimeRemoteException(latLngBounds2);
        }
    }
}
