package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khelaufutsal.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Futsal_BookedBookingFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView empty;
    private ArrayList<BookingClass> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_futsal__booked_booking, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_bookedBookings);
        empty = view.findViewById(R.id.empty_confirmedBooking);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Futsal_BookedAdapter(getCorrectBookings(),getContext());
        recyclerView.setAdapter(adapter);

        ImageView closeButton = view.findViewById(R.id.closeButton_bookedBooking);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    public List<BookingClass> getCorrectBookings(){
        LinkedList<BookingClass> bookings = FutsalHolder.getFutsal().getFutsalBooking();
        if(bookings!=null){
            for(int i=0;i<bookings.size();i++){
                if(bookings.get(i).getBooked().equals("True")){
                    arrayList.add(bookings.get(i));
                }
            }
        }
        if(arrayList.size()==0){
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
        return arrayList;
    }

}
