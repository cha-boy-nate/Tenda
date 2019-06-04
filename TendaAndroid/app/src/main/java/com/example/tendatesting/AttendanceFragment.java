package com.example.tendatesting;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

public class AttendanceFragment extends Fragment implements AttendeeAdapter.OnNoteListener {
    private Button mButton;
    private JSONObject eventString;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Attendee> attendanceArrayList;
    private JSONArray attendeeArray;
    String fragementIdentifier = "AttendeeLog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        final View v = inflater.inflate(R.layout.fragment_attendance, container, false);
        mButton = (Button) v.findViewById(R.id.sendAlert);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(v.getContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.send_alert_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(v.getContext(), R.style.sendAlertDialog);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                Context context = v.getContext();
                                CharSequence text = "Alert Sent";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,300);
                                toast.show();
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });

        Bundle extras = getActivity().getIntent().getExtras();
        String eventID = extras.getString("event_id");
        String user_id = extras.getString("user_id");
        Log.d("UserID_for_session", "event_id: " + eventID + ", user_id: " + user_id);

        //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
        RequestQueue queueDet = Volley.newRequestQueue(v.getContext());
        String serverURL = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com";
        String urlDet =  serverURL + "/event/" + eventID + "/";
        //Create request
        final StringRequest stringRequestDet = new StringRequest(Request.Method.GET, urlDet, new Response.Listener<String>() {
            //When the request is recieved:
            @Override
            public void onResponse(String response) {
                try {
                    //Convert response to a json
                    JSONObject jsonObj = new JSONObject(response.toString());
                    String result = jsonObj.getString("result");
                    jsonObj = new JSONObject(result);

                    Log.d(fragementIdentifier, result);

                    String name = jsonObj.getString("name");
                    String description = jsonObj.getString("description");
                    String time = jsonObj.getString("time");
                    String date = jsonObj.getString("date");


                    TextView eventTitle = v.findViewById(R.id.page_textTitle);
                    TextView eventDescription = v.findViewById(R.id.page_textDescription);
                    TextView eventTime = v.findViewById(R.id.page_textTime);
                    TextView eventDate = v.findViewById(R.id.page_textDate);

                    eventTitle.setText(name);
                    eventDescription.setText(description);
                    eventTime.setText(time);
                    eventDate.setText(date);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(fragementIdentifier, "Error with request response.");
            }
        });

        // Add the request to the RequestQueue.
        queueDet.add(stringRequestDet);

        //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        String url = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com/attendenceData/"+eventID+"/";
        //Create request
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            //When the request is recieved:
            @Override
            public void onResponse(String response) {
                try {
                    //Convert response to a json
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String result = jsonObject.getString("result");
                    Log.d(fragementIdentifier, "RESULT" + result);

                    attendeeArray = new JSONArray(result);
                    Log.d(fragementIdentifier,result);
                    createListData(attendeeArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(fragementIdentifier, "Error with request response.");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        recyclerView = v.findViewById(R.id.recyclerViewAttend);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        attendanceArrayList = new ArrayList<>();
        adapter = new AttendeeAdapter(attendanceArrayList,this);
        recyclerView.setAdapter(adapter);

        return v;
    }
    private void createListData(JSONArray attendeeArray) {
        int length = attendeeArray.length();
        for(int i = 0; i < length; i++) {
            try {
                JSONObject full = (JSONObject) attendeeArray.get(i);
                JSONObject test = new JSONObject(full.getString("user"));
                String firstName = test.getString("firstName");
                String lastName = test.getString("lastName");
                String duration = full.getString("difference");
                Attendee attendee = new Attendee(firstName,lastName, duration);
                Log.d(fragementIdentifier, "Adding user");
                attendanceArrayList.add(attendee);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int position) {

    }
}
