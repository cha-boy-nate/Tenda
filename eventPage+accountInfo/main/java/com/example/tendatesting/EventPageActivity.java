package com.example.tendatesting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EventPageActivity extends AppCompatActivity {

    private TextView textTitle, textDescription, textTime, textDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        Intent intent = getIntent();
        int index = intent.getIntExtra("position", 0);
        Event event = EventFragment.getevent(index);

        textTitle = findViewById(R.id.page_textTitle);
        textDescription = findViewById(R.id.page_textDescription);
        textTime = findViewById(R.id.page_textTime);
        textDate = findViewById(R.id.page_textDate);

        textTitle.setText("Title:  " + event.getEventTitle());
        textDescription.setText("Description:  " + event.getEventDescription());
        textTime.setText("Time:  " + event.getEventTime());
        textDate.setText("Date:  " + event.getEventDate());
    }
}
