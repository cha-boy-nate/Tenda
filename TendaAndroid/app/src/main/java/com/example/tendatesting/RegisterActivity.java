package com.example.tendatesting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button button= (Button)findViewById(R.id.registerButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                CharSequence text = "Account successfully created";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    public void ClickToLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //Function will be called when the button is clicked.
    public void createAccount(View view) throws IOException, JSONException {
        EditText name = (EditText)findViewById(R.id.name);
        EditText email = (EditText)findViewById(R.id.name);
        EditText password = (EditText)findViewById(R.id.password);
        final String emailString = email.getText().toString();
        final String nameString = name.getText().toString();
        final String passwordString = password.getText().toString();

        final String requestString = emailString + " " + nameString + " " + passwordString;

        /*
        final String jsonString = new JSONObject()
                .put("name", nameString
                .put("email", emailString)
                .put("password", passwordString).toString();
        */

        Log.d("RegisterLog", requestString);

        final Intent homeIntent = new Intent(this, HomeActivity.class);
        RequestQueue queue = Volley.newRequestQueue(this);
        String serverURL = "http://ec2-54-200-106-244.us-west-2.compute.amazonaws.com";
        String url = serverURL + "/createAccount";

        //Create request
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response.toString());
                    String result = jsonObj.getString("result");
                    Log.d("RegisterLog", result);
                    boolean success = result.toLowerCase().contains("200");
                    if(success == true){
                        ClickToLoginPage();
                    }
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
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("key", requestString);
                return MyData;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void ClickToLoginPage(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
