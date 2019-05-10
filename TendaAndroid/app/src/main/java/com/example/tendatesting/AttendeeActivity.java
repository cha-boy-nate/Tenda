package com.example.tendatesting;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class AttendeeActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView mTextMessage;
    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            BottomNavigationView navView = findViewById(R.id.nav_view);
            switch (item.getItemId()) {
                case R.id.going:
                    item.setCheckable(true);
                    navView.setBackgroundColor(Color.parseColor("#66B266"));

                    return true;
                case R.id.not_going:
                    item.setCheckable(true);

                    navView.setBackgroundColor(Color.RED);
                    return true;
                case R.id.not_sure:
                    item.setCheckable(true);
                    navView.setBackgroundColor(Color.parseColor("#77AAFF"));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        //navView.setItemIconTintList(null);
        navView.getMenu().getItem(0).setCheckable(false);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }


        mMapView = (MapView) findViewById(R.id.map_ViewAttendee);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);



    }


    public double inRadius(double markerLattitude, double markerLongitude, double locLatitude, double locLongtitude, double radius) {
        double val = Math.sqrt(Math.pow((markerLattitude - locLatitude), 2) + Math.pow((markerLongitude - locLongtitude), 2));
        val = val*78710;
        return val;
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        LatLng SPU = new LatLng(47.6496, -122.3615);


        final Marker loc = map.addMarker(new MarkerOptions().position(SPU).title("Set Location").draggable(true));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc.getPosition(), 20.0f));
        final Circle cir = map.addCircle(new CircleOptions().center(loc.getPosition()).radius(10).strokeColor(Color.GREEN).fillColor(0x2290EE90));
        map.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        TextView radTextView = findViewById(R.id.in_location);
        final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location myLocation) {

                TextView radTextView = findViewById(R.id.distanceFromEvent);
                double val = inRadius(loc.getPosition().latitude, loc.getPosition().longitude, myLocation.getLatitude(), myLocation.getLongitude(), cir.getRadius());
                if(val<cir.getRadius()) {
                    radTextView.setText("You Are Within The Event Radius");
                }else{
                    radTextView.setText(((int)val)+" Meters From The Event");
                }
            }
        });



    }

}
