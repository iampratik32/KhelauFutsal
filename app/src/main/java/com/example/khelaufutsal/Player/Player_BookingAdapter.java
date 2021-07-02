package com.example.khelaufutsal.Player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.khelaufutsal.R;

import java.util.LinkedList;

public class Player_BookingAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private TextView closedFutsal;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private LinkedList dates;
    private LinkedList days;


    public Player_BookingAdapter(Context context, LinkedList dates, LinkedList days) {
        this.context = context;
        this.dates= dates;
        this.days =days;
    }

    @Override
    public int getCount() {
        return 15;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.player_booking_adapter_layout,null,false);
        closedFutsal = view.findViewById(R.id.closedFutsal_groundBookingAdapter);
        recyclerView = view.findViewById(R.id.recyclerView_groundBookingAdapter);
        layoutManager = new LinearLayoutManager(context);

        if(Player_FutsalClassHolder.getFutsal().getFutsalOpenDays().contains(days.get(position))){
            recyclerView.setVisibility(View.VISIBLE);
            closedFutsal.setVisibility(View.GONE);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new FutsalBookingRecyclerAdapter(context,String.valueOf(dates.get(position)));
            recyclerView.setAdapter(adapter);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            closedFutsal.setVisibility(View.VISIBLE);
        }

        container.addView(view);

        return view;
    }


}
