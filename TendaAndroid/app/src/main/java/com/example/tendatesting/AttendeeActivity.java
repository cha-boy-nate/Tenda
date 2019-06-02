package com.example.tendatesting;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AttendeeActivity extends AppCompatActivity implements OnMapReadyCallback{
    String fragementIdentifier = "AttendeeLog";
    Timer timer;
    String isPresent;
    String inRadius;
    private FusedLocationProviderClient fusedLocationClient;




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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onStop() {
        super.onStop();
        if(timer != null){
            timer.cancel();
            timer.purge();
            //cancel timer task and assign null
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee);

        Toolbar barTitle = findViewById(R.id.messageToolbar);
        barTitle.setTitle("Event Details");
        setSupportActionBar(barTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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


//        recyclerView = findViewById(R.id.recyclerViewAttend);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setHasFixedSize(true);
//        attendanceArrayList = new ArrayList<>();
//
//        recyclerView.setAdapter(new AttendeeAdapter(attendanceArrayList, this));
//


    }


    /*
    Name: inRadius
    Description: This function calculates the distance from the user to the marker in degress then converts it to meters
    Input: The latitude, longitude of the user and the latitude and longitue of the marker
    Output: THe distance between the user and the marker in meters
    */
    public void inRadius(double markerLattitude, double markerLongitude, double locLatitude, double locLongtitude, double radius) {
        //distance calculation that returns the distance in degrees
        double val = Math.sqrt(Math.pow((markerLattitude - locLatitude), 2) + Math.pow((markerLongitude - locLongtitude), 2));
        val = val*78710; //converting the degrees to meters
        if(radius > val){
            isPresent = "yes";
            inRadius = "You are marked as present";
        }else{
            isPresent = "no";
            inRadius = "You are " + (int)(val-radius) +" meters from the radius";}
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

        /*******************************************************/
        // THIS IS THE START OF THE REQUEST TO GET EVENT DATA  //
        /*******************************************************/
        Bundle extras = getIntent().getExtras();
        String eventID = extras.getString("event_id");
        Log.d(fragementIdentifier, eventID);

        //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://34.217.162.221:8000/event/" + eventID + "/";
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
                    String duration = jsonObj.getString("duration");
                    final int radius = (int) Double.parseDouble(jsonObj.getString("radius"));

//                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
//                    Date firstParsedDate = dateFormat.parse(time);
//                    Log.d("total time date", Long.toString(firstParsedDate.getTime()));
//                    Timestamp tsD = new Timestamp(firstParsedDate.getTime());
//                    Log.d("in timestamp date", tsD.toString());
//                    Date secondParsedDate = dateFormat.parse(duration);
//                    Log.d("total time duration", Long.toString(secondParsedDate.getTime()));
//                    Timestamp tsDu = new Timestamp(secondParsedDate.getTime());
//                    Log.d("in timestamp duration", tsDu.toString());
//                    long diff = secondParsedDate.getTime() + firstParsedDate.getTime();
//                    Log.d("total time", Long.toString(diff));
//                    Timestamp ts = new Timestamp(diff);
//                    Log.d("in timestamp", ts.toString());

                    ///Event start time format/////
                    SimpleDateFormat twentyFourTimeFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat twelveHourFormat = new SimpleDateFormat("hh:mm a");
                    Date twentyFourHourDate = null;
                    try {
                        twentyFourHourDate = twentyFourTimeFormat.parse(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String eventTimeAMPM = twelveHourFormat.format(twentyFourHourDate);
                    ///Event end time format///
                    //////FORMAT END TIME////
                    SimpleDateFormat twentyFourTimeFormatE = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat twelveHourFormatE = new SimpleDateFormat("hh:mm a");
                    Date twentyFourHourDateE = null;
                    try {
                        twentyFourHourDateE = twentyFourTimeFormatE.parse(duration);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String eventETimeAMPM = twelveHourFormatE.format(twentyFourHourDateE);
                    ///////FORMAT END TIME/////
                    //FORMAT DATE///
                    LocalDate eventDateF = LocalDate.parse(date);
                    String dateFormatted = eventDateF.format(DateTimeFormatter.ofPattern( "MMM d yyyy"));
                    //FORMAT DATE//


                    double latitude;
                    double longitude;
                    latitude = Double.parseDouble(jsonObj.getString("latitude"));
                    longitude = Double.parseDouble(jsonObj.getString("longitude"));

                    //





                    Log.d(fragementIdentifier, "Longitude: " + longitude + " - Latitude: " + latitude);

                    TextView eventTitle = findViewById(R.id.page_textTitle);
                    TextView eventDescription = findViewById(R.id.page_textDescription);
                    TextView eventTime = findViewById(R.id.page_textTime);
                    TextView eventDate = findViewById(R.id.page_textDate);
                    //TextView eventDuration = findViewById(R.id.page_textDuration);

                    eventTitle.setText(name);
                    eventDescription.setText(description);
                    eventTime.setText(eventTimeAMPM + " - " + eventETimeAMPM);
                    eventDate.setText(dateFormatted);

                    //eventDuration.setText(duration);

                    if(latitude == longitude){
                        latitude = 47.6496;
                        longitude = 122.3615;

                    }

                    final LatLng eventLocation = new LatLng(latitude, longitude);
                    Log.d("AttendeeLog", "Lat: " + latitude + " Long: " + longitude);


                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(AttendeeActivity.this, Locale.getDefault());

                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    //Adding the location marker for the event
                    final Marker loc = map.addMarker(new MarkerOptions().position(eventLocation).title(address).draggable(false).snippet("Radius: "+radius+" meters"));
                    loc.showInfoWindow();
                    loc.getPosition().toString();
                    //Moving the camera to where the marker is
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc.getPosition(), 17.0f));

                    //Setting the radius for the event by drawing a circle
                    final Circle cir = map.addCircle(new CircleOptions().center(loc.getPosition()).radius(radius).strokeColor(Color.GREEN).fillColor(0x2290EE90));

                    //getting the view to show how far the user is from the radius once the event starts
                    final TextView radTextView = findViewById(R.id.distanceFromEvent);
                    radTextView.setText("The Event has not started");


                    if (ActivityCompat.checkSelfPermission(AttendeeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }







                    ;

                    String startTimeTest = date + " " + time;
                    String endTimeTest = date + " " + duration;
                    Log.d("startTimeLog", startTimeTest);

                    //Setting the start time of the event and the end time of the event
                    Timestamp startTime = java.sql.Timestamp.valueOf(startTimeTest);
                    final Timestamp endTime = java.sql.Timestamp.valueOf(endTimeTest);
                    Timestamp curTime = new Timestamp(System.currentTimeMillis());
                    if(curTime.getTime()>=endTime.getTime()){
                        radTextView.setText("The event has ended");
                        map.setMyLocationEnabled(false);
                    }else{
                        radTextView.setText("The event has not started");
                    }







                    //creating a timer for a scheduled task
                    timer = new Timer("LocationTimer");


                    //creating the task that will run once the time has started
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Log.d("startTimeLog", "test");
                            //getting the user's permission before getting the location
                            if (ActivityCompat.checkSelfPermission(AttendeeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }


 //                           map.setMyLocationEnabled(true);
                            ///////////////////////////GET LOCATION AND SEND YES IF WITHIN RADIUS//////////////////

                            fusedLocationClient = LocationServices.getFusedLocationProviderClient(AttendeeActivity.this);
                            fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(AttendeeActivity.this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            // Got last known location. In some rare situations this can be null.

                                            if (location != null) {
                                                inRadius(eventLocation.latitude,eventLocation.longitude,location.getLatitude(),location.getLongitude(),radius);
                                                radTextView.setText(inRadius);
                                                if(isPresent.equals("yes")){
                                                    sendRequest(isPresent);
                                                    Log.d("Request Sent",isPresent);
                                                }
                                                else{
                                                    Log.d("Request Sent",isPresent);
                                                }




                                            }
                                        }
                                    });
                            ////////////////////////////GET LOCATION AND SEND YES IF WITHIN RADIUS/////////



                            //End getting location

                            //getting the current timestamp to send to the database
                            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            //Getting the current time and then checking if the time has passed the end time
                            //if so then the task is cancelled
                            Timestamp curTime = new Timestamp(System.currentTimeMillis());
                            if(curTime.getTime()>=endTime.getTime()){
                                radTextView.setText("The event has ended");
                                map.setMyLocationEnabled(false);
                                timer.cancel();
                                timer.purge();
                               // map.setMyLocationEnabled(false);
                            }

                        }
                    };

                    //Getting the start date from the start time and scheduling the task.
                    Date startdate = new Date(startTime.getTime());
                    Log.d("start date", startdate.toString());
                    if((curTime.getTime()<=endTime.getTime())&&(curTime.getTime()>=startTime.getTime())){
                        map.setMyLocationEnabled(true);
                        timer.schedule(task,startdate,3000L);

                    }







                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (IOException e) {
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






        //Getting the user's location
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



    }




        public void sendRequest(final String presence) {

    RequestQueue queue = Volley.newRequestQueue(this);
    String url ="http://34.217.162.221:8000/createAttendenceRecord";
    //Create request
    final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        //When the request is recieved:
        @Override
        public void onResponse(String response) {
            try {
                //Convert response to a json and check if the response what 200 (which means password is valid)
                JSONObject jsonObj = new JSONObject(response.toString());
                String result = jsonObj.getString("result");
                Log.d("JoinLog", result);
                Log.d("check presence", presence);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("ERROR", "Error with request response.");
        }
    }) {
        protected Map<String, String> getParams() {
            //Format data that will make up the body of the post request (email and password)
            Map<String, String> MyData = new HashMap<String, String>();
            // String userID = getArguments().getString("userID");
            MyData.put("user_id", "1");
            MyData.put("event_id", "1");
            MyData.put("present", presence);
            return MyData;
        }
    };
    // Add the request to the RequestQueue.
    queue.add(stringRequest);

}



}



