package com.example.tendatesting;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.os.health.TimerStat;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Time;
import java.sql.Timestamp;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AttendeeActivity extends AppCompatActivity implements OnMapReadyCallback{
    String fragementIdentifier = "AttendeeLog";
    private MapView mMapView; //variable used for map view

    /*
    Name: Bottom navigation listener for the user event reply
    Description: This function listens to whether or not the user has clicked one of the three responses and changes the color accordingly
    Input: None
    Output: Bool to whether an item was clicked
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //Getting the bottom nav view
            BottomNavigationView navView = findViewById(R.id.nav_view);

            //Switch statement will set the background color depending on the item clicked
            //set checkable makes sure that the item is clickable
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

        //getting the view for the bottom nav view
        BottomNavigationView navView = findViewById(R.id.nav_view);

        //setting the checkable to false so that when the user opens the attendee activity no response is selected
        navView.getMenu().getItem(0).setCheckable(false);

        //Creating the listener for the nav view
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mMapView = (MapView) findViewById(R.id.map_ViewAttendee); //getting the view for where the map will be displayed
        mMapView.onCreate(savedInstanceState); //creating the bundle for when the event starts
        mMapView.onResume(); //Showing the map when acticity starts
        mMapView.getMapAsync(this); //The API callback to show the map

        /*******************************************************/
        // THIS IS THE START OF THE REQUEST TO GET EVENT DATA  //
        /*******************************************************/
        //String eventID="1";
        //Log.d(fragementIdentifier, eventID);

        Bundle extras = getIntent().getExtras();
        String eventID = extras.getString("event_id");
        String user_id = extras.getString("user_id");
        Log.d("UserID_for_session", "event_id: " + eventID + ", user_id: " + user_id);

        RequestQueue queue = Volley.newRequestQueue(this);
        //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
        String serverURL = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com";
        String url = serverURL + "/event/" + eventID + "/";
        //Create request
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            //When the request is recieved:
            @Override
            public void onResponse(String response) {
                try {
                    //Convert response to a json
                    JSONObject jsonObj = new JSONObject(response.toString());
                    String result = jsonObj.getString("result");
                    jsonObj = new JSONObject(result);

                    Log.d(fragementIdentifier, result);

                    String name = jsonObj.getString("name");
                    String description = jsonObj.getString("description");
                    String time = jsonObj.getString("time");
                    String date = jsonObj.getString("date");

                    Log.d(fragementIdentifier, name);
                    Log.d(fragementIdentifier, description);
                    Log.d(fragementIdentifier, time);
                    Log.d(fragementIdentifier, date);

                    TextView eventTitle = findViewById(R.id.page_textTitle);
                    TextView eventDescription = findViewById(R.id.page_textDescription);
                    TextView eventTime = findViewById(R.id.page_textTime);
                    TextView eventDate = findViewById(R.id.page_textDate);

                    eventTitle.setText(name);
                    eventDescription.setText(description);
                    eventTime.setText(time);
                    eventDate.setText(date);

                    Log.d(fragementIdentifier, "get request complete");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(fragementIdentifier, "Error with request response.");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        /*******************************************************/
        // THIS IS THE END OF THE REQUEST TO GET EVENT DATA    //
        /*******************************************************/
    }

    /*
    Name: inRadius
    Description: This function calculates the distance from the user to the marker in degress then converts it to meters
    Input: The latitude, longitude of the user and the latitude and longitue of the marker
    Output: THe distance between the user and the marker in meters
    */
    public double inRadius(double markerLattitude, double markerLongitude, double locLatitude, double locLongtitude, double radius) {
        //distance calculation that returns the distance in degrees
        double val = Math.sqrt(Math.pow((markerLattitude - locLatitude), 2) + Math.pow((markerLongitude - locLongtitude), 2));
        val = val*78710; //converting the degrees to meters
        return val;
    }

    /*
    Name: onMapReady
    Description: This function handles the maps api, it sets the marker, radius and takes the user's location
    Input: Google map
    Output: None
    */
    @Override
    public void onMapReady(final GoogleMap map) {
        //Setting the lat and long for SPU
        LatLng SPU = new LatLng(47.6496, -122.3615);

        //Adding the location marker for the event
        final Marker loc = map.addMarker(new MarkerOptions().position(SPU).title("Set Location").draggable(true));
        loc.getPosition().toString();
        //Moving the camera to where the marker is
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc.getPosition(), 20.0f));

        //Setting the radius for the event by drawing a circle
        final Circle cir = map.addCircle(new CircleOptions().center(loc.getPosition()).radius(10).strokeColor(Color.GREEN).fillColor(0x2290EE90));



        //Getting the user's location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }

        //getting the view to show how far the user is from the radius once the event starts
        final TextView radTextView = findViewById(R.id.distanceFromEvent);
        radTextView.setText("You're location will be taken once the event starts");

        //Setting the start time of the event and the end time of the event
        Timestamp startTime = java.sql.Timestamp.valueOf("2019-05-04 00:00:00.0");
        final Timestamp endTime = java.sql.Timestamp.valueOf("2019-05-05 16:51:00.0");

        //creating a timer for a scheduled task
        Timer timer = new Timer("LocationTimer");
        final RequestQueue queue = Volley.newRequestQueue(this);

        //creating the task that will run once the time has started
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(fragementIdentifier, "Timer running");
                String url = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com/createAttendenceRecord";
                //Create request
                final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    //When the request is recieved:
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Convert response to a json and check if the response what 200 (which means password is valid)
                            JSONObject jsonObj = new JSONObject(response.toString());
                            String result = jsonObj.getString("result");
                            Log.d(fragementIdentifier, "post request complete");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(fragementIdentifier, "Error with request response.");
                    }
                }) {
                    protected Map<String, String> getParams() {
                        //Format data that will make up the body of the post request (email and password)
                        Map<String, String> MyData = new HashMap<String, String>();
                        Bundle extras = getIntent().getExtras();
                        String event_id = extras.getString("event_id");
                        String user_id = extras.getString("user_id");
                        MyData.put("userID", user_id);
                        MyData.put("eventID", event_id);
                        MyData.put("response", "yes");
                        return MyData;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                //getting the user's permission before getting the location
                if (ActivityCompat.checkSelfPermission(AttendeeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    return;
                }

                //map.setMyLocationEnabled(true);

                //creating the cliend for getting the user's location using the Fused Location Provier Client
                final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(AttendeeActivity.this);

                /*
                name: Location listener
                description: This function gets the last locaiton of the user and if it was success full then the function
                calls the inradius function to check if the user is within the location
                input: None
                output: None
                 */
                client.getLastLocation().addOnSuccessListener(AttendeeActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location myLocation) {

                        //bool value to send to the database that represents whether the user is within the radius or not
                        Boolean presence = null;

                        //calling the inRadius to get the distance between the marker and the user
                        double val = inRadius(loc.getPosition().latitude, loc.getPosition().longitude, myLocation.getLatitude(), myLocation.getLongitude(), cir.getRadius());

                        //checking if the user is within radius or not and setting the bool and
                        //textview appropriately
                        if (val < cir.getRadius()) {
                            radTextView.setText("You Are Within The Event Radius");
                            presence = true;
                        } else {
                            radTextView.setText(((int) val) + " Meters From The Event");
                            presence = false;
                        }
                    }
                });
                //End getting location

                //getting the current timestamp to send to the database
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                //Getting the current time and then checking if the time has passed the end time
                //if so then the task is cancelled
                Timestamp curTime = new Timestamp(System.currentTimeMillis());
                if(curTime.getTime()>=endTime.getTime()){
                    cancel();
                }

            }
        };

        //Getting the start date from the start time and scheduling the task.
        Date startdate = new Date(startTime.getTime());
        timer.scheduleAtFixedRate(task,startdate,3000L);



    }


}
