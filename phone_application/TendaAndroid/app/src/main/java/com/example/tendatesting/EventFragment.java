package com.example.tendatesting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;

public class EventFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Manage Event");

        View v = inflater.inflate(R.layout.fragment_event, container, false);

        FloatingActionButton buttonLocation = v.findViewById(R.id.createEventFab);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(myIntent);
            }
        });
        return v;

    }

}


