package com.example.tendatesting;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
        public AttendeeHolder(@Nullable View itemView, OnNoteListener onNoteListener){
            super(itemView);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }

        public void setAttendeeDetails(Attendee attendee) {
            textName = itemView.findViewById(R.id.page_name1);
            textReply = itemView.findViewById(R.id.image1);
            textDuration = itemView.findViewById(R.id.dur1);
            textName.setText(attendee.getAttendeeName());
            textReply.setText(attendee.getAttendeeReply());
            textDuration.setText(attendee.getAttendeeDuration());

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

