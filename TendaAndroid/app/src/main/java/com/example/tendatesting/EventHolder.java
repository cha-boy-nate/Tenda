package com.example.tendatesting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class EventHolder extends RecyclerView.ViewHolder {
    private TextView textTitle, textDescription, textTime, textDate;

    public EventHolder(View itemView) {
        super(itemView);
        textTitle = itemView.findViewById(R.id.textTitle);
        textDescription = itemView.findViewById(R.id.textDescription);
        textTime = itemView.findViewById(R.id.textTime);
        textDate = itemView.findViewById(R.id.textDate);
    }

    public void setDetails(Event event) {
        textTitle.setText(event.getEventTitle());
        textDescription.setText(event.getEventDescription());
        textTime.setText(event.getEventTime());
        textDate.setText(event.getEventDate());
    }
}
