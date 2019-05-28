package com.example.tendatesting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class
ManageEvent extends AppCompatActivity {
    String fragementIdentifier = "ManageEventLog";

    Toolbar toolbar; //Creating the variable for the toolbar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);


        //Getting the view for the bottom navigation from the layout
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener); //Default listener for the bottom navigation

        //Setting the default page to the attendance page
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_M_container,
                new AttendanceFragment()).commit();
        Toolbar barTitle = findViewById(R.id.messageToolbar);
        barTitle.setTitle("Event Management");

        Bundle extras = getIntent().getExtras();
        String eventID = extras.getString("event_id");
        Log.d(fragementIdentifier, eventID);

        //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://34.217.162.221:8000/attendenceData/"+eventID+"/";
        //Create request
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            //When the request is recieved:
            @Override
            public void onResponse(String response) {
                try {
                    //Convert response to a json
                    JSONObject jsonObj = new JSONObject(response.toString());
                    String result = jsonObj.getString("result");
                    Log.d(fragementIdentifier, result);
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





    }

    /*
    name: onNavigationItemSelectedListener
    description: Listens for which item is pressed and shows the fragment accordingly
    input: menuItem
    output: none
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {




        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            toolbar = (Toolbar) findViewById(R.id.messageToolbar);
            setSupportActionBar(toolbar);

            //This switch statement get the id of the item clicked and displays the appropriate fragment and changes the title of toolbar
            switch (item.getItemId()) {
                case R.id.navigation_attendance:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_M_container,
                            new AttendanceFragment()).commit();
                    getSupportActionBar().setTitle("Event Management");
                    return true;
                case R.id.navigation_map:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_M_container,
                            new LocationFragment()).commit();
                    getSupportActionBar().setTitle("Event Location");
                    return true;
                case R.id.navigation_stats:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_M_container,
                            new StatsFragment()).commit();
                    getSupportActionBar().setTitle("Event Statistics");
                    return true;
            }
            return false;
        }
    };



}
