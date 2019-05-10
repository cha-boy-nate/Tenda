package com.example.tendatesting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    public String userIDVal = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Basically signing into system
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("User");
            RequestQueue queue = Volley.newRequestQueue(this);
            String urlUserID ="http://34.217.162.221:8000/getUserIDNum/"+value+"/";
            //Create request
            final StringRequest stringRequestUserID = new StringRequest(Request.Method.GET, urlUserID, new Response.Listener<String>() {
                //When the request is recieved:
                @Override
                public void onResponse(String response) {
                    try {
                        //Convert response to a json
                        JSONObject jsonObj = new JSONObject(response.toString());
                        String result = jsonObj.getJSONObject("result").getString("user_id");
                        userIDVal = result;
                        Log.d("HomeLog", userIDVal);
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
            queue.add(stringRequestUserID );

        } else {
            Log.d("HomeLog", "getting value unsuccessful");
        }

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            Bundle bundle = new Bundle();
            bundle.putString("userID", userIDVal);
            EventFragment timeline_frag = new EventFragment();
            timeline_frag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, timeline_frag).commit();
            navigationView.setCheckedItem(R.id.nav_timeline);
        }
        requestPermission();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Bundle bundle = new Bundle();
        bundle.putString("userID", userIDVal);

        switch(menuItem.getItemId()) {
            case R.id.nav_timeline:
                EventFragment timeline_frag = new EventFragment();
                timeline_frag.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, timeline_frag).commit();
                break;
            case R.id.nav_my_event:
                MyEvents my_event_frag = new MyEvents();
                my_event_frag.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, my_event_frag).commit();
                break;
            case R.id.nav_event_join:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EventJoinFragment()).commit();
                break;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccountFragment()).commit();
                break;
            case R.id.nav_report:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReportFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;
//            case R.id.nav_exit:
//                //Setup alert dialog when user log out
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//                alertDialog.setTitle("Confirm Logout");
//                alertDialog.setMessage("Are you sure you want to log out?");
//                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(this, LoginActivity.class);
//                        startActivity(intent);
//                    }
//                });
//                alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // User pressed Cancel button. Write Logic Here
//                        Toast.makeText(getApplicationContext(), "You clicked on Cancel",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                alertDialog.show();
//                break;
        }
        if (menuItem.getItemId() == R.id.nav_exit){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Are you sure you wish to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            alertDialog.show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }

}
