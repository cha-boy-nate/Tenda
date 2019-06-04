package com.example.tendatesting;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.android.gms.maps.OnMapReadyCallback;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnMapReadyCallback {
    String eventDate;
    String eventTime;
    String latitude;
    String longitude;
    String radius;

    Button dateAndTimePicker;
    TextView dateTimeResult;
    EditText eventTitle, eventDescription;

    private MapView mMapView; //Map view api variable

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"; //Bundle key if bundle is empty
    AutocompleteSupportFragment placeAutoComplete; //Places api variable

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dateAndTimePicker = findViewById(R.id.dateAndTimePicker);
        dateTimeResult = findViewById(R.id.dateTimeResult);

        dateAndTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                        CreateEventActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

        //If the bundle is empty then it si set to the mapview bundle key
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        //Google maps api view set to display to the correct view
        //Then calling the api onMapReady
        mMapView = (MapView) findViewById(R.id.map_View);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        Button createEventButton = (Button) findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CreateEventLog", "Button clicked");
                Context context = getApplicationContext();
                createEvent(view);
                CharSequence text = "Event successfully created";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }


    public void createEvent(View view) {
//Get event title and description values into string
        eventTitle = findViewById(R.id.eventTitle);
        eventDescription = findViewById(R.id.eventDescription);
        final String eventTitleString = eventTitle.getText().toString();
        final String eventDescriptionString = eventDescription.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String serverURL = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com";
        String url = serverURL + "/createEvent";

        //Create request
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            //When the request is recieved:
            @Override
            public void onResponse(String response) {
                try {
                    //Convert response to a json and check if the response what 200 (which means password is valid)
                    JSONObject jsonObj = new JSONObject(response.toString());
                    String result = jsonObj.getString("result");
                    Log.d("CreateEventLog", result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CreateEventLog", "Error with request response.");
            }
        }) {
            protected Map<String, String> getParams() {
                //Format data that will make up the body of the post request (email and password)
                Map<String, String> MyData = new HashMap<String, String>();
                Bundle extras = getIntent().getExtras();
                String user_id = extras.getString("user_id");
                MyData.put("userID", user_id);
                MyData.put("eventTitle", eventTitleString);
                MyData.put("eventDescription", eventDescriptionString);
                MyData.put("eventDate", eventDate);
                MyData.put("eventTime", eventTime);
                MyData.put("Latitude", latitude);
                MyData.put("Longitude", longitude);
                MyData.put("Radius", radius);
                MyData.put("Duration", "00:00:00");

                return MyData;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }




    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this,
                CreateEventActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;

        String twentyFourTime = String.format("%02d:%02d", hourFinal, minuteFinal);
        SimpleDateFormat twentyFourTimeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat tweleveHourFormat = new SimpleDateFormat("hh:mm a");
        Date twentyFourHourDate = null;
        try {
            twentyFourHourDate = twentyFourTimeFormat.parse(twentyFourTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        eventTime = twentyFourTimeFormat.format(twentyFourHourDate);
        eventDate = yearFinal+"-"+monthFinal + "-" + dayFinal;
        dateTimeResult.setText("Date: " + eventDate + "\n" +
                "Time: " + eventTime);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    /*
    Name: onMapReady
    Description: This function handles the maps api, it sets the marker, radius and takes the user's location
    Input: Google map
    Output: None
    */
    @Override
    public void onMapReady(final GoogleMap map) {

        LatLng SPU = new LatLng(47.6496, -122.3615); //setting the lat and long for SPU

        final Marker loc = map.addMarker(new MarkerOptions().position(SPU).title("Set Location").draggable(true)); //Setting the map marker to SPU
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc.getPosition(), 17.0f)); //Moving the camera to SPU
        final Circle cir = map.addCircle(new CircleOptions().center(loc.getPosition()).radius(10).strokeColor(Color.GREEN).fillColor(0x2290EE90)); //Creating the circle that will represent the radius

        //Getting the user's permission for google to read their location
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
        map.setMyLocationEnabled(true); //Showing the user's location on the map

        /*
        Name: onMarkerDragListener
        Description: This function listens if the marker has been moved, then moves the circle and camera accordingly
        Input: Google map marker listener
        Output: None
        */
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            /*
            Name: onMarkerDragEndListener
            Description: This function listens if the marker has finished being dragged, then moves the circle and camera accordingly
            Input: Marker
            Output: None
            */
            @Override
            public void onMarkerDragEnd(Marker marker) {

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), map.getCameraPosition().zoom)); //moving the camera to where the marker is set
                cir.setCenter(marker.getPosition()); //Redrawing the circle to where the marker has been moved to


                //getting the user's permission if it already hasn't been acepted
                if (ActivityCompat.checkSelfPermission(CreateEventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        });


        final SeekBar seekBarDis = findViewById(R.id.seekBarEvent); //Variable to get the view for the seekbar
        seekBarDis.setProgress(35); //Setting the seekbar progress to 35

        /*
        Name: onSeekBarChangeListener
        Description: This function listens to whether the seekbar has been changed and shows the radius and draws the circle accordingly
        Input: seekbar change listener
        Output: None
        */
        seekBarDis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            /*
            Name: onProgressChanged listener
            Description: This function listens to whether the seekbar has been changed and shows the radius and draws the circle accordingly
            Input: seekbar, the progress of the seekbar and whether is user has moved it or not
            Output: None
            */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //Checking if the user is moving the seekbar
                if (fromUser) {
                    if(progress==0){ //if the seekbar is set to 0 then the radius is set to 10
                        cir.setRadius(1 * 10);

                    }else {
                        cir.setRadius(progress); //The circle radius is set to the amount of progress on the seekbar
                        TextView showRadius = findViewById(R.id.Radius); //getting the view to display the radius
                        showRadius.setText(progress +" meters"); //Displaying the radius in the view
                    }
                    if (ActivityCompat.checkSelfPermission(CreateEventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        //Checking if placed has been initialized if not then I have hard coded the key to initialize the API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        }

        //Getting the fragment from the layout to manipulate the API
        placeAutoComplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        //Getting the list of the Name of the places and their location and placing then into an array list
        placeAutoComplete.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));

        /*
        Name: onPlacesSelected Listener
        Description: This function listens to whether a place has been searched for and moves the marker and circle to that location
        Input: Place selection listener
        Output: None
        */
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            /*
            Name: onPlacesSelected
            Description: This function listens to whether a place has been searched for and moves the marker and circle to that location
            Input: Place
            Output: None
            */
            @Override
            public void onPlaceSelected(Place place) {

                Log.i("PLACE SELECTED", "Place: " + place.getLatLng() + ", " + place.getId());

                //setting the location the place searched
                loc.setPosition(place.getLatLng());

                //Moving the marker to the place searched
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc.getPosition(), map.getCameraPosition().zoom));

                //Setting the circle to the place searched
                cir.setCenter(loc.getPosition());

                //Getting the user's permission if it hasn't already been granted
                if (ActivityCompat.checkSelfPermission(CreateEventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            //Logging any errors
            @Override
            public void onError(Status status) {
                Log.i("MAPS", "An error occurred: " + status);

            }


        });


        LatLng coord = loc.getPosition();
        latitude = Double.toString(coord.latitude);
        longitude = Double.toString(coord.longitude);
        radius = String.valueOf(seekBarDis.getProgress());











    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
