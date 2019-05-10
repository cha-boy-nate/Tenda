package com.example.tendatesting;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventFragment extends Fragment implements EventAdapter.OnNoteListener {

    //RecyclerView Initialization
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Event> eventArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Event Timeline");
        final View v = inflater.inflate(R.layout.fragment_event, container, false);
        String userID = getArguments().getString("userID");
        Log.d("TimelineLog", userID);
        //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="http://34.217.162.221:8000/timeline/"+userID+"/";
        //Create request
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            //When the request is recieved:
            @Override
            public void onResponse(String response) {
                try {
                    //Convert response to a json
                    JSONObject jsonObj = new JSONObject(response.toString());
                    String result = jsonObj.getString("result");
                    Log.d("TimelineLog", result);
                    TextView requestResult = v.findViewById(R.id.eventDescription);
                    requestResult.setText(result);
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

        //Recycler view Implementation
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventArrayList = new ArrayList<>();
        adapter = new EventAdapter(eventArrayList,this);
        recyclerView.setAdapter(adapter);
        createListData();
        return v;
    }
    private void createListData() {
        Event event = new Event("Team Chan", "Weekly Meeting", "10:30", "30/04/2019");
        eventArrayList.add(event);
        Event event1 = new Event("Comic Conference", "Multi-genre entertainment and comic convention", "2:30", "4/04/2019");
        eventArrayList.add(event1);
        Event event2 = new Event("Team Chan", "Weekly Meeting", "10:30", "30/04/2019");
        eventArrayList.add(event2);
        Event event3 = new Event("Team Chan", "Weekly Meeting", "10:30", "30/04/2019");
        eventArrayList.add(event3);
        Event event4 = new Event("Team Chan", "Weekly Meeting", "10:30", "30/04/2019");
        eventArrayList.add(event4);
        Event event5 = new Event("Team Chan", "Weekly Meeting", "10:30", "30/04/2019");
        eventArrayList.add(event5);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int position) {
        eventArrayList.get(position);
        if(position < 1){
        Intent intent = new Intent(getActivity(), AttendeeActivity.class);
        startActivity(intent);}
        else{        Intent intent = new Intent(getActivity(),  ManageEvent.class);
            startActivity(intent);}
    }
}
