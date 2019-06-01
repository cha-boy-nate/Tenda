
package com.example.tendatesting;
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
        import java.util.HashMap;
        import java.util.Map;
public class EventJoinFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Search / Join Event");
        final View v = inflater.inflate(R.layout.fragment_event_join, container, false);
        final String[] event_id = new String[1];
        Button eventButton = v.findViewById(R.id.search_button);
        Button joinButton = v.findViewById(R.id.join_button);
        final EditText search = (EditText)v.findViewById(R.id.search);
        joinButton.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(final View v) {
                Log.d("JoinLog", "Join Process Started");
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url ="http://34.217.162.221:8000/createResponse";
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
//                        String userID = getArguments().getString("userID");
                        MyData.put("userID", "1");
                        MyData.put("eventID", event_id[0]);
                        MyData.put("response", "yes");
                        return MyData;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
        eventButton.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(final View v) {
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
                            jsonObj = new JSONObject(result);
                            Log.d("JoinLog", result);
                            String name = jsonObj.getString("name");
                            String description = jsonObj.getString("description");
                            String date = jsonObj.getString("date");
                            String duration = jsonObj.getString("duration");
                            String time = jsonObj.getString("time");
                            event_id[0] = jsonObj.getString("event_id");
                            TextView nameLabel = getView().findViewById(R.id.first_name);
                            nameLabel.setText(name);
                            TextView descriptionLabel = getView().findViewById(R.id.description);
                            descriptionLabel.setText(description);
                            TextView dateLabel = getView().findViewById(R.id.date);
                            dateLabel.setText(date);
                            TextView timeLabel = getView().findViewById(R.id.time);
                            timeLabel.setText(time);
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