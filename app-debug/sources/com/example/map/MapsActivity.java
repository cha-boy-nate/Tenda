package com.example.map;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int PRIVATE_CODE = 1315;
    private static final int REQUEST_PERMISSION = 0;
    private int GPS_REQUEST_CODE = 10;
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private GoogleMap mMap;
    LocationListener myLocationListener = new C02331();
    private LocationManager myLocationManager;

    /* renamed from: com.example.map.MapsActivity$1 */
    class C02331 implements LocationListener {
        C02331() {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
            Log.d("执行，GPS的打开", provider);
        }

        public void onProviderDisabled(String provider) {
            Log.d("执行，GPS关闭", provider);
        }

        public void onLocationChanged(Location location) {
            if (location != null) {
                MapsActivity.this.latitude = location.getLatitude();
                MapsActivity.this.longitude = location.getLongitude();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Lat: ");
                stringBuilder.append(location.getLatitude());
                stringBuilder.append(" Lng: ");
                stringBuilder.append(location.getLongitude());
                Log.d("执行，坐标发生改变", stringBuilder.toString());
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0234R.layout.activity_maps);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(C0234R.id.map)).getMapAsync(this);
        requestPermission();
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        getLocation();
        LatLng my_location = new LatLng(this.latitude, this.longitude);
        this.mMap.addMarker(new MarkerOptions().position(my_location).title("MyLocation"));
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_location, 12.0f));
        TextView tv = (TextView) findViewById(C0234R.id.text_view);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("latitude:\t");
        stringBuilder.append(String.format("%.2f", new Object[]{Double.valueOf(this.latitude)}));
        stringBuilder.append("\t\t\tlongitude:\t");
        stringBuilder.append(String.format("%.2f", new Object[]{Double.valueOf(this.longitude)}));
        stringBuilder.append("");
        tv.setText(stringBuilder.toString());
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_FINE_LOCATION"}, 0);
    }

    public String getLocation() {
        this.myLocationManager = (LocationManager) getSystemService("location");
        if (this.myLocationManager.isProviderEnabled("gps")) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(1);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(1);
            Location location = getLastKnownLocation();
            if (location != null) {
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
            } else {
                this.myLocationManager.requestLocationUpdates("gps", 1000, 0.0f, this.myLocationListener);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("纬度：");
        stringBuilder.append(this.latitude);
        stringBuilder.append("\n经度：");
        stringBuilder.append(this.longitude);
        return stringBuilder.toString();
    }

    private Location getLastKnownLocation() {
        this.myLocationManager = (LocationManager) getApplicationContext().getSystemService("location");
        Location bestLocation = null;
        for (String provider : this.myLocationManager.getProviders(true)) {
            Location l = this.myLocationManager.getLastKnownLocation(provider);
            if (l != null) {
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }
}
