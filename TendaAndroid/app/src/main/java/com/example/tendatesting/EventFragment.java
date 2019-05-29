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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class EventFragment extends Fragment implements EventAdapter.OnNoteListener {


    //RecyclerView Initialization
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Event> eventArrayList;
    private JSONArray eventArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Event Timeline");
        final View v = inflater.inflate(R.layout.fragment_event, container, false);
        //String userID = getArguments().getString("userID");
        String userID="1";
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
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String result = jsonObject.getString("result");
                    eventArray = new JSONArray(result);
                    createListData(eventArray);
                } catch (JSONException e) {
                    Log.d("TimelineLog", "json conversion didn't work");
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

        return v;
    }
    private void createListData(JSONArray eventArray) {
        int length = eventArray.length();
        for(int i = 0; i < length; i++) {
            try {
                JSONObject full = (JSONObject) eventArray.get(i);
                JSONObject test = new JSONObject(full.getString("event"));
                String name = test.getString("name");
                String event_id = test.getString("event_id");
                String description = test.getString("description");
                String duration = test.getString("duration");
                String radius = test.getString("radius");
                String time = test.getString("time");
                String date = test.getString("date");

                Event event = new Event(name, description, time, date);
                eventArrayList.add(event);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int position) {
        JSONObject full = null;
        try {
            full = (JSONObject) eventArray.get(position);
            JSONObject test = new JSONObject(full.getString("event"));
            String event_id = test.getString("event_id");
            Intent intent = new Intent(getActivity(), AttendeeActivity.class);
            intent.putExtra("event_id", event_id);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
