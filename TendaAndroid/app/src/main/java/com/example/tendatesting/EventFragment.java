package com.example.tendatesting;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EventFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Manage Event");

        View v = inflater.inflate(R.layout.fragment_event, container, false);

        Button buttonLocation = v.findViewById(R.id.button_location);
        buttonLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(getActivity(), LocationActivity.class);
                startActivity(myIntent);
            }
        });

        FloatingActionButton buttonEvent = v.findViewById(R.id.floatEventButton);
        buttonEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(myIntent);
            }
        });
        return v;

    }


}
