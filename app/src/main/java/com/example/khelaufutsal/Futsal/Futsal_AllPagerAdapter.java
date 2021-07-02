package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.khelaufutsal.R;

import java.util.LinkedList;

public class Futsal_AllPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private TextView noBooking;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private LinkedList dates;
    private LinkedList days;

    public Futsal_AllPagerAdapter(Context context, LinkedList dates, LinkedList days) {
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
        View view = layoutInflater.inflate(R.layout.futsal_pager_adapter_layout,null,false);
        noBooking = view.findViewById(R.id.noOne_futsalAllPagerAdapter);
        recyclerView = view.findViewById(R.id.recyclerView_futsalAllPagerAdapter);
        layoutManager = new LinearLayoutManager(context);

        if(!FutsalHolder.getFutsal().getFutsalOpenDays().contains(days.get(position))){
            recyclerView.setVisibility(View.GONE);
            noBooking.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setLayoutManager(layoutManager);
            adapter = new Futsal_AllBookingRecyclerAdapter(context,String.valueOf(dates.get(position)));
            recyclerView.setAdapter(adapter);
        }

        container.addView(view);
        return view;

    }
}
