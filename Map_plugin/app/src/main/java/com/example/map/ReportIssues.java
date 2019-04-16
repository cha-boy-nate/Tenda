package com.example.map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;

public class ReportIssues extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issues);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String str1="";
                EditText editText1 =(EditText)findViewById(R.id.editText1);
                str1=editText1.getText().toString();

                //将文本框1的文本赋给文本框2
                EditText editText2 =(EditText)findViewById(R.id.editText2);
                editText2.setText(str1.toCharArray(), 0, str1.length());

            }
        });

    }
}
