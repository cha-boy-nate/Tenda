package com.example.tendatesting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AttendeeHolder extends RecyclerView.ViewHolder {

    private TextView textName, textReply, textDuration;

    public AttendeeHolder(View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.page_name1);
        //textReply = itemView.findViewById(R.id.image1);
        textDuration = itemView.findViewById(R.id.dur1);
    }

    public void setAttendeeDetails(Attendee attendee) {
        textName.setText(attendee.getAttendeeName());
        //textReply.setText(attendee.getAttendeeReply());
        textDuration.setText(attendee.getAttendeeDuration());

    }
}
