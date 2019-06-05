
package com.example.tendatesting;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class EventJoinFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Search / Join Event");
        final View v = inflater.inflate(R.layout.fragment_event_join, container, false);

        String user_id = getActivity().getIntent().getExtras().getString("user_id");
        Log.d("UserID_for_session", "from join: " + user_id);

        final String[] event_id = new String[1];
        Button eventButton = v.findViewById(R.id.search_button);
        Button joinButton = v.findViewById(R.id.join_button);
        final EditText search = (EditText)v.findViewById(R.id.search);
        joinButton.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(final View v) {
                Log.d("JoinLog", "Join Process Started");
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String serverURL = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com";
                String url = serverURL + "/createResponse";
                //Create request
                final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    //When the request is recieved:
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Convert response to a json and check if the response what 200 (which means password is valid)
                            JSONObject jsonObj = new JSONObject(response.toString());
                            String result = jsonObj.getString("result");
                            Log.d("JoinLog", result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error with request response.");
                    }
                }) {
                    protected Map<String, String> getParams() {
                        //Format data that will make up the body of the post request (email and password)
                        Map<String, String> MyData = new HashMap<String, String>();
                        String user_id = getActivity().getIntent().getExtras().getString("user_id");
                        MyData.put("userID", user_id);
                        MyData.put("eventID", event_id[0]);
                        MyData.put("response", "yes");
                        return MyData;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                Context context = getContext();
                CharSequence text = "Joined Event Successfully";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Fragment fragment = new EventFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        eventButton.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(final View v) {
                //Format what is needed for request: place to go if verified, a request queue to send a request to the server, and url for server.
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String eventID = search.getText().toString();
                String serverURL = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com";
                String url = serverURL + "/event/"+eventID+"/";
                //Create request
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    //When the request is recieved:
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Convert response to a json
                            JSONObject jsonObj = new JSONObject(response.toString());
                            String result = jsonObj.getString("result");
                            jsonObj = new JSONObject(result);
                            Log.d("JoinLog", result);
                            String name = jsonObj.getString("name");
                            String description = jsonObj.getString("description");
                            String date = jsonObj.getString("date");
                            String duration = jsonObj.getString("duration");
                            String time = jsonObj.getString("time");

                            event_id[0] = jsonObj.getString("event_id");


                            ///Event start time format/////
                            SimpleDateFormat twentyFourTimeFormat = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat twelveHourFormat = new SimpleDateFormat("hh:mm a");
                            Date twentyFourHourDate = null;
                            try {
                                twentyFourHourDate = twentyFourTimeFormat.parse(time);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String eventTimeAMPM = twelveHourFormat.format(twentyFourHourDate);
                            ///Event end time format///
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
                            LocalDate eventDateF = LocalDate.parse(date);
                            String dateFormatted = eventDateF.format(DateTimeFormatter.ofPattern( "MMM d yyyy"));
                            //FORMAT DATE//


                            double latitude;
                            double longitude;
                            latitude = Double.parseDouble(jsonObj.getString("latitude"));
                            longitude = Double.parseDouble(jsonObj.getString("longitude"));

                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getActivity(), Locale.getDefault());

                            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                            TextView nameLabel = getView().findViewById(R.id.name);
                            nameLabel.setText(name);
                            TextView location = getView().findViewById(R.id.location);
                            location.setText(address);
                            TextView descriptionLabel = getView().findViewById(R.id.description);
                            descriptionLabel.setText(description);
                            TextView dateLabel = getView().findViewById(R.id.date);
                            dateLabel.setText(dateFormatted);
                            TextView timeLabel = getView().findViewById(R.id.time);
                            timeLabel.setText(eventTimeAMPM + " - " + eventETimeAMPM);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (IOException e) {
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