package com.example.tendatesting;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.AttendeeHolder> {



    private ArrayList<Attendee> attendees;
    private OnNoteListener mOnNoteListener;


    public AttendeeAdapter(ArrayList<Attendee> attendees, OnNoteListener onNoteListener){

        this.attendees = attendees;
        this.mOnNoteListener = onNoteListener;

    }

    @NonNull
    @Override
    public AttendeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendee_row,parent,false);
        return new AttendeeHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeHolder attendeeHolder, int position) {
        Attendee attendee = attendees.get(position);
        attendeeHolder.setAttendeeDetails(attendee);
    }



    @Override
    public int getItemCount() {
        return attendees.size();
    }

    public class AttendeeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnNoteListener onNoteListener;
        private TextView textName, textReply, textDuration;
        private ImageView isPresent;
        public AttendeeHolder(@Nullable View itemView, OnNoteListener onNoteListener){
            super(itemView);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }

        public void setAttendeeDetails(Attendee attendee) {
            textName = itemView.findViewById(R.id.page_name1);
            textReply = itemView.findViewById(R.id.page_name2);
            textDuration = itemView.findViewById(R.id.dur1);
            isPresent = itemView.findViewById(R.id.image1);

            textName.setText(attendee.getAttendeeName());
            textReply.setText(attendee.getAttendeeLastName());
            textDuration.setText(attendee.getAttendeeDuration());
            Date date = new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);
            Log.d("endtime",attendee.getAttendeeEndTime() );
            Timestamp endTime = java.sql.Timestamp.valueOf(attendee.getAttendeeEndTime());
            if(ts.after(endTime)){
                isPresent.setImageResource(R.drawable.ic_cancel);
            }else{
                isPresent.setImageResource(R.drawable.ic_check_circle);
            }

        }
        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

}

