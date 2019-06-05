package com.example.tendatesting;

import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

        String userID = getActivity().getIntent().getExtras().getString("user_id");
        Log.d("UserID_for_session", "from event: " + userID);
        Log.d("TimelineLog", "from event: "+userID);
        //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverURL = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com";
        String url = serverURL + "/timeline/"+userID+"/";
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
                Log.d("ERROR", "Error with request response.");
                Context context = getContext();
                CharSequence text = "Couldn't Load Events Refresh Tab";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
                //////FORMAT DATE////
                SimpleDateFormat twentyFourTimeFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat twelveHourFormat = new SimpleDateFormat("hh:mm a");
                Date twentyFourHourDate = null;
                try {
                    twentyFourHourDate = twentyFourTimeFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String eventTimeAMPM = twelveHourFormat.format(twentyFourHourDate);
                ///////FORMAT DATE/////

                //////FORMAT END TIME////
                SimpleDateFormat twentyFourTimeFormatE = new SimpleDateFormat("HH:mm");
                SimpleDateFormat twelveHourFormatE = new SimpleDateFormat("hh:mm a");
                Date twentyFourHourDateE = null;
                try {
                    twentyFourHourDateE = twentyFourTimeFormatE.parse(duration);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String eventETimeAMPM = twelveHourFormatE.format(twentyFourHourDateE);
                ///////FORMAT END TIME/////

                //FORMAT DATE///
                LocalDate eventDate = LocalDate.parse(date);
                String dateFormatted = eventDate.format(DateTimeFormatter.ofPattern( "MMM d yyyy"));
                //FORMAT DATE//

                Event event = new Event(name, description, eventTimeAMPM, dateFormatted,eventETimeAMPM,"");
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
            String user_id = getActivity().getIntent().getExtras().getString("user_id");
            intent.putExtra("user_id", user_id);
            intent.putExtra("event_id", event_id);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
