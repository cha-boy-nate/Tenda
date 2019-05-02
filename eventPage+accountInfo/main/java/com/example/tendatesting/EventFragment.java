package com.example.tendatesting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class EventFragment extends Fragment {

    //RecyclerView Initialization
    private RecyclerView recyclerView;
    //private RecyclerView.Adapter adapter;
    private EventAdapter adapter;
    private static ArrayList<Event> eventArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Manage Event");

        View v = inflater.inflate(R.layout.fragment_event, container, false);


        FloatingActionButton buttonEvent = v.findViewById(R.id.floatEventButton);
        buttonEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(myIntent);
            }
        });

        //Recycler view Implementation
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventArrayList = new ArrayList<>();
        adapter = new EventAdapter(getActivity(),eventArrayList);
        adapter = new EventAdapter(getActivity(),eventArrayList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent myIntent = new Intent();
                myIntent.setClass(getActivity(), EventPageActivity.class);
                myIntent.putExtra("position", position);
                startActivity(myIntent);
            }
        });
        adapter.setOnItemLongClickListener(new EventAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                //Toast.makeText(MainActivity.this,"long click "+mData.get(position),Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
        createListData();




        return v;

    }

    private void createListData() {
        Event event = new Event("Team Chan1", "Weekly Meeting", "10:30", "30/04/2019");
        eventArrayList.add(event);
        Event event1 = new Event("Team Chan2", "Weekly Meeting", "10:30", "30/04/2019");
        eventArrayList.add(event1);
        Event event2 = new Event("Team Chan3", "Weekly Meeting", "10:30", "30/04/2019");
        eventArrayList.add(event2);
        adapter.notifyDataSetChanged();
    }

    public static Event getevent(int i){
        Event event = eventArrayList.get(i);
        return event;

    }


}
