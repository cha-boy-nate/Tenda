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
                try {
                    createAccount(view);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void ClickToLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //Function will be called when the button is clicked.
    public void createAccount(View view) throws IOException, JSONException {
        EditText first_name = (EditText)findViewById(R.id.first_name);
        EditText last_name = (EditText)findViewById(R.id.last_name);
        EditText email = (EditText)findViewById(R.id.email);
        EditText password = (EditText)findViewById(R.id.password);
        final String emailString = email.getText().toString();
        final String firstNameString = first_name.getText().toString();
        final String lastNameString = last_name.getText().toString();
        final String passwordString = password.getText().toString();

        Log.d("RegisterLog", firstNameString + " : " + lastNameString + " : " + emailString + " : " + passwordString);

        final Intent homeIntent = new Intent(this, HomeActivity.class);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://34.217.162.221:8000/createAccount";

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
                MyData.put("first_name", firstNameString);
                MyData.put("last_name", lastNameString);
                MyData.put("email", emailString);
                MyData.put("password", passwordString);
                return MyData;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
