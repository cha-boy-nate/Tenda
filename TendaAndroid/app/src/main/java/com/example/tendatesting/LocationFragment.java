package com.example.tendatesting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap googleMap;
    private ArrayList<Event> eventArrayList;
    double latitude, longitude;
    int radius;
    private JSONArray eventArray;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        final View v = inflater.inflate(R.layout.fragment_map, container, false);
        MapsInitializer.initialize(this.getActivity());
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
             mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }


        ////////////////////////////
        String userID="1";
        Log.d("MyEvents", userID);
        //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="http://34.217.162.221:8000/myEvents/"+userID+"/";
        //Create request
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            //When the request is recieved:
            @Override
            public void onResponse(String response) {
                try {
                    //Convert response to a json
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String result = jsonObject.getString("result");
                    eventArray = new JSONArray(result);
                    createListData(eventArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error with request response.");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        ////////////////////////////


        mMapView = (MapView) v.findViewById(R.id.map_ViewAttend);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);


        return v;
    }
    private void createListData(JSONArray eventArray) {
        int length = eventArray.length();
        for(int i = 0; i < length; i++) {
            try {
                JSONObject full = (JSONObject) eventArray.get(i);
                JSONObject test = new JSONObject(full.getString("event"));
                latitude = Double.parseDouble(test.getString("latitude"));
                longitude = Double.parseDouble(test.getString("longitude"));
                radius = (int)(Double.parseDouble(test.getString("radius")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        LatLng eventLocation = new LatLng(latitude,longitude);


        final Marker loc = map.addMarker(new MarkerOptions().position(eventLocation).title("Set Location").draggable(true));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc.getPosition(), 17.0f));
        final Circle cir = map.addCircle(new CircleOptions().center(loc.getPosition()).radius(radius).strokeColor(Color.GREEN).fillColor(0x2290EE90));
        map.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
            }
        });

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //map.setMyLocationEnabled(true);





    }


}
