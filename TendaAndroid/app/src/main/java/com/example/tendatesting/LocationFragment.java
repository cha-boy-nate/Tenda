package com.example.tendatesting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LocationFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    String fragementIdentifier = "Location Fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.map_ViewAttend);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
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
                //googleMap.setMyLocationEnabled(true);

                Bundle extras = getActivity().getIntent().getExtras();
                String eventID = extras.getString("event_id");
                Log.d(fragementIdentifier, eventID);

                //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String serverURL = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com";
                String url = serverURL + "/event/" + eventID + "/";
                //Create request
                final StringRequest stringRequestDet = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    //When the request is recieved:
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Convert response to a json
                            JSONObject jsonObj = new JSONObject(response.toString());
                            String result = jsonObj.getString("result");
                            jsonObj = new JSONObject(result);

                            Log.d(fragementIdentifier, result);

                            Double  latitude = Double.parseDouble(jsonObj.getString("latitude"));
                            Double  longitude = Double.parseDouble(jsonObj.getString("longitude"));
                            int radius = (int) Double.parseDouble(jsonObj.getString("radius"));

                            if(latitude == longitude){
                                latitude = 47.6496;
                                longitude = 122.3615;

                            }


                            LatLng eventLocation = new LatLng(latitude, longitude);
                            Log.d("AttendeeLog", "Lat: " + latitude + " Long: " + longitude);

                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getContext(), Locale.getDefault());

                            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                            String city = addresses.get(0).getLocality();
//                            String state = addresses.get(0).getAdminArea();
//                            String country = addresses.get(0).getCountryName();
//                            String postalCode = addresses.get(0).getPostalCode();
//                            String knownName = addresses.get(0).getFeatureName();


                            //Adding the location marker for the event
                            Marker loc = googleMap.addMarker(new MarkerOptions().position(eventLocation).title(address).draggable(false).snippet("Radius: "+radius+" meters"));

                            loc.showInfoWindow();

                            // For zooming automatically to the location of the marker
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc.getPosition(), 17.0f));
                            googleMap.addCircle(new CircleOptions().center(loc.getPosition()).radius(radius).strokeColor(Color.GREEN).fillColor(0x2290EE90));

                        } catch (JSONException e) {

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(fragementIdentifier, "Error with request response.");
                        Context context = getContext();
                        CharSequence text = "Event Location Failed Refresh Tab";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequestDet);


            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}