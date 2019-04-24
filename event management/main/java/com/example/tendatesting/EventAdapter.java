package com.example.tendatesting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventHolder> {

    private Context context;
    private ArrayList<Event> events;

    public EventAdapter(Context context, ArrayList<Event> events){
        this.context = context;
        this.events = events;
    }
    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_row,parent,false);
        return new EventHolder(view);
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
}
