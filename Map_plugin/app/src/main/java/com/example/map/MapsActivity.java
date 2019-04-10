package com.example.map;

import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import butterknife.ButterKnife;
//simport butterknife.InjectView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;




public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private double latitude = 0.0;
    private double longitude = 0.0;
    //private Button info;
    private LocationManager myLocationManager;
    //private Button btRequest;
    //private TextView tv_Info;
    private static final int REQUEST_PERMISSION = 0;

    private static final int PRIVATE_CODE = 1315;//开启GPS权限

    private int GPS_REQUEST_CODE = 10;


    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestPermission();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //LatLng beijing = new LatLng(39, 116);
        //mMap.addMarker(new MarkerOptions().position(beijing).title("Marker in Beijing"));



        //String a;


            getLocation();
            LatLng my_location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(my_location).title("MyLocation"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    my_location, 12));


        TextView tv=(TextView)findViewById(R.id.text_view);


        tv.setText("latitude:\t" + String.format("%.2f", latitude) + "\t\t\tlongitude:\t" +String.format("%.2f", longitude)+"");


        //enableMyLocation();
    }


    /**
     * 申请权限
     */
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSION);
    }


    /**
     * 判断当前位置服务是否打开
     * 如果未打开进行跳转到设置服务界面，然后延迟进行获取经纬度
     * @return
     */
    /**
     private void checkGPSIsStart() {
     boolean isOpen = false;
     isOpen = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
     if (isOpen) {
     getLocation();
     } else {
     //跳转GPS设置界面
     Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
     startActivityForResult(intent, GPS_REQUEST_CODE);
     new Handler() {
     }.postDelayed(new Runnable() {
    @Override
    public void run() {
    getLocation();
    }
    }, 2000);
     }
     }
     */

    /**
     * 获取经纬度进行显示
     */
    public String getLocation() {
        myLocationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return "";
        //}
        if(myLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            //查找到位置服务
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗


            //String provider = myLocationManager.getBestProvider(criteria, true); // 获取GPS信息

            //Location location = myLocationManager.getLastKnownLocation(provider);

            Location location = getLastKnownLocation();
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            } else {
                myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, myLocationListener);
            }


        }
        return "纬度：" + latitude + "\n" + "经度：" + longitude;
    }

    /**
     *获取GPS位置监听器，包含四个不同触发方式
     */
    LocationListener myLocationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // 当位置获取（GPS）打开时调用此方法
        @Override
        public void onProviderEnabled(String provider) {
            Log.d("执行，GPS的打开", provider);
        }

        // 当位置获取（GPS）关闭时调用此方法
        @Override
        public void onProviderDisabled(String provider) {
            Log.d("执行，GPS关闭", provider);
        }

        // 当坐标改变时触发此方法，如果获取到相同坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                latitude = location.getLatitude(); // 经度
                longitude = location.getLongitude(); // 纬度
                Log.d("执行，坐标发生改变", "Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
            }
        }
    };


    private Location getLastKnownLocation() {
        myLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = myLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = myLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }



}



