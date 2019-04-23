package com.example.reportissues;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import com.example.map.R;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.report_issues_toolbar);
        //toolbar.setTitle("Event");//标题
        setSupportActionBar(toolbar);
        //getActionBar.setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.shape_corner);//设置Navigation 图标
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        finish();
        //    }
        //});
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button     显示返回箭头的按钮
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
}
