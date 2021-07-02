package com.example.khelaufutsal.Player;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khelaufutsal.Futsal.BookingClass;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Player_BookingFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    TextView empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player__booking, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_playerBooking);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayerBookingAdapter(getMatches(), getContext());
        recyclerView.setAdapter(adapter);
        empty = view.findViewById(R.id.bookingIsEmpty);

        if(PlayerHolder.getPlayer().getPlayerBookings()==null || PlayerHolder.getPlayer().getPlayerBookings().size()==0){
            empty.setVisibility(View.VISIBLE);
        }

        return view;
    }
    private List<BookingClass> getMatches(){

        return PlayerHolder.getPlayer().getPlayerBookings();

    }


}
