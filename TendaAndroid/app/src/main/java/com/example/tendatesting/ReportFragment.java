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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Report");
        final View v = inflater.inflate(R.layout.fragment_report, container, false);

        final EditText reportName = (EditText)v.findViewById(R.id.name);
        final EditText reportDescription = (EditText)v.findViewById(R.id.description);

        Button submitButton = v.findViewById(R.id.submit_button);

        submitButton.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(View v) {
                String nameString = reportName.getText().toString();
                String descriptionString = reportDescription.getText().toString();
                String values = nameString + " --- "+descriptionString;
                Log.d("ReportLog", values);

            }
        });


        return v;
    }
}
