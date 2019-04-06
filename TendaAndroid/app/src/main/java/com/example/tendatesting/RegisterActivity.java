package com.example.tendatesting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password, confirmPassword, contactNumber;
    private Button btn_regist;
    private ProgressBar loading;
    private static String URL_REGIST = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        loading = findViewById(R.id.loading);
//        name = findViewById(R.id.name);
//        email = findViewById(R.id.email);
//        password = findViewById(R.id.email);
//        confirmPassword = findViewById(R.id.confirmPassword);
//        contactNumber = findViewById(R.id.contactNumber);
//
//        btn_regist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //
//            }
//        });

    }

    public void ClickToLoginPage(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

//    private void Regist(){
//        loading.setVisibility(View.VISIBLE);
//        btn_regist.setVisibility(View.GONE);
//
//        final String name = this.name.getText().toString().trim();
//        final String email = this.email.getText().toString().trim();
//        final String password = this.password.getText().toString().trim();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
//            }
//        };
//
//    }
}
