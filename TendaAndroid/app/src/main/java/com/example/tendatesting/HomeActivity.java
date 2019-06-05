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
import android.view.View;
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
        String value = extras.getString("user_id");
        userIDVal = value;
        Log.d("UserID_for_session", "from home: " + value);

        //Getting the view for the toolbar and then setting the support action bar to the tool bar to change the title according to the fragment
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Getting the view for the drawer and the view for the navigation view and then setting the navigation listener to the view
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting the toggle for the action bar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); //The drawer listener will listen for any toggle changes
        toggle.syncState(); //The toggle syncs its current state depending on whether the drawer is open or closed

        String eventFrag = getIntent().getStringExtra("frag");
        Log.d("eventFrag","value: "+eventFrag);
        if(eventFrag != null && eventFrag.equals("myEvent")){
            Log.d("eventFrag","value: "+"yes");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyEvents()).commit();}
        else if (savedInstanceState == null){
            Bundle bundle = new Bundle();
            bundle.putString("user_id", userIDVal);
            EventFragment timeline_frag = new EventFragment();
            timeline_frag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, timeline_frag).commit();
            navigationView.setCheckedItem(R.id.nav_timeline);
        }
        requestPermission();

    }
    @Override
    public void onStart(){
        super.onStart();
        View load =  findViewById(R.id.loadingPanel);
        load.setVisibility(View.GONE);


    }
    /*
    name: onNavigationItemSelected
    description: When a navigation item is selected then the appropriate fragment is opened
    input: MenuItem
    output:
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Bundle bundle = new Bundle();
        bundle.putString("userID", userIDVal);

        //A switch statement that selects which fragment to open depending which item is clicked on the menu drawer
        switch(menuItem.getItemId()){

            //The same process is repeated for all items selected: getting the support fragment and selecting to show that fragment
            case  R.id.nav_timeline:
                EventFragment timeline_frag = new EventFragment();
                timeline_frag.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, timeline_frag).commit();
                break;
            case  R.id.nav_my_event:
                MyEvents my_event_frag = new MyEvents();
                my_event_frag.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, my_event_frag).commit();
                break;
            case  R.id.nav_event_join:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EventJoinFragment()).commit();
                break;
            case  R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccountFragment()).commit();
                break;
            case  R.id.nav_report:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReportFragment()).commit();
                break;
            case  R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;

        }
        if (menuItem.getItemId() == R.id.nav_exit){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,R.style.AlertDialog);
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

    //Closes the drawer if the back button is pressed
    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Requesting permission from the user to access their location
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }

}
