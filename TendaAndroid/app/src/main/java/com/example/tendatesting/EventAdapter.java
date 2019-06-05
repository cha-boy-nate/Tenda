package com.example.tendatesting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {


    private ArrayList<Event> events;
    private OnNoteListener mOnNoteListener;


    public EventAdapter(ArrayList<Event> events, OnNoteListener onNoteListener){

        this.events = events;
        this.mOnNoteListener = onNoteListener;

    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row,parent,false);
        return new EventHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position){
        Event event = events.get(position);
        holder.setDetails(event);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnNoteListener onNoteListener;
        public EventHolder(@Nullable View itemView, OnNoteListener onNoteListener){
            super(itemView);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }
        public void setDetails(Event event)
        {
            TextView title = itemView.findViewById(R.id.textTitle);
            TextView description = itemView.findViewById(R.id.textDescription);
            TextView time = itemView.findViewById(R.id.textTime);
            TextView date = itemView.findViewById(R.id.textDate);
            TextView id = itemView.findViewById(R.id.textID);
            //TextView duration = itemView.findViewById(R.id.textDuration);

            title.setText(event.getEventTitle());
            description.setText(event.getEventDescription());
            time.setText(event.getEventTime() + " - " + event.getEventDuration());
            date.setText(event.getEventDate());
            id.setText(event.getEventID());
            //duration.setText(event.getEventDuration());


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
