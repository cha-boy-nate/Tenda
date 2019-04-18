package com.example.tendatesting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        Button button = findViewById(R.id.getLocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(LocationActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                client.getLastLocation().addOnSuccessListener(LocationActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if(location!=null){
                            TextView latTextView  = findViewById(R.id.lat_location);
                            TextView longTextView = findViewById(R.id.long_location);
                            latTextView.setText(Double.toString(location.getLatitude()));
                            longTextView.setText(Double.toString(location.getLongitude()));

                        }

                    }
                });
            }
        });
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }
}
