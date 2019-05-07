package com.example.tendatesting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EventJoinFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Search / Join Event");
        final View v = inflater.inflate(R.layout.fragment_event_join, container, false);

        Button eventButton = v.findViewById(R.id.search_button);
        final EditText search = (EditText)v.findViewById(R.id.search);
        final TextView requestResult = v.findViewById(R.id.responseData);

        eventButton.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(View v) {
                //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String eventID = search.getText().toString();
                String url ="http://34.217.162.221:8000/event/"+eventID+"/";
                //Create request
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    //When the request is recieved:
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Convert response to a json
                            JSONObject jsonObj = new JSONObject(response.toString());
                            String result = jsonObj.getString("result");
                            Log.d("JoinLog", result);
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
            }
        });



        return v;
    }
}