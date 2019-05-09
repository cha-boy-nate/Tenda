package com.example.tendatesting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class
ManageEvent extends AppCompatActivity {


    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_M_container,
                new AttendanceFragment()).commit();
        Toolbar barTitle = findViewById(R.id.messageToolbar);
        barTitle.setTitle("Event Management");
        //getSupportActionBar().setTitle("Event Management");


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {




        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            toolbar = (Toolbar) findViewById(R.id.messageToolbar);
            setSupportActionBar(toolbar);

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
