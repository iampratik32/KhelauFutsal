package com.example.khelaufutsal.Player;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khelaufutsal.Futsal.BookingClass;
import com.example.khelaufutsal.Futsal.FutsalClass;
import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.Futsal.Futsal_HistoryAdapter;
import com.example.khelaufutsal.Futsal.Futsal_HistoryFragment;
import com.example.khelaufutsal.Futsal.Player_HistoryAdapter;
import com.example.khelaufutsal.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Player_HistoryFragment extends Fragment {

    private LinkedList<BookingClass> newHistory;
    private LinkedList<BookingClass> historyBookings;
    private LinkedList<BookingClass> finalHistory;
    private ProgressBar progressBar;
    CountDownTimer countDownTimer;
    private TextView noHistory;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<BookingClass> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_player__history, container, false);

        DatabaseReference toRate = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId()).child("ToRate");
        toRate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    try {
                        DialogRateFutsal dialogRateFutsal = new DialogRateFutsal(dataSnapshot.getValue().toString());
                        dialogRateFutsal.show(getFragmentManager(),"Dialog");
                    }
                    catch (NullPointerException e){}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        historyBookings = PlayerHolder.getPlayer().getPlayerHistory();
        newHistory = new LinkedList<>();
        progressBar = view.findViewById(R.id.progressBar_playerHistory);
        recyclerView = view.findViewById(R.id.recyclerView_playerHistory);
        noHistory = view.findViewById(R.id.noHistory_playerHistory);

        newHistory = new LinkedList<>();
        finalHistory = new LinkedList<>();

        readData(new onGetDataListener() {
            @Override
            public void onSuccess(PlayerClass playerClass) {
                countDownTimer = new CountDownTimer(1400,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        displayRecyclerView(view);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }.start();
            }

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Error Loading Data",Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }


    public interface onGetDataListener{
        void onSuccess(PlayerClass playerClass);
        void onStart();
        void onFailure();
    }

    public void readData(final Player_HistoryFragment.onGetDataListener listener){

        DatabaseReference readHistory = FirebaseDatabase.getInstance().getReference().child("History");
        readHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        for(DataSnapshot post:postSnapshot.getChildren()){
                            for(DataSnapshot p:post.getChildren()){
                                if(p.child("Taken").getValue().toString().equals(PlayerHolder.getPlayer().getUserId())){
                                    BookingClass bookingClass = new BookingClass(postSnapshot.getKey(),post.getKey(),p.getKey(),"Taken","",PlayerHolder.getPlayer().getUserId());
                                    newHistory.add(bookingClass);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listener.onSuccess(PlayerHolder.getPlayer());
    }

    private void displayRecyclerView(View view){
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Player_HistoryAdapter(getCorrectHistory(),getContext());
        recyclerView.setAdapter(adapter);
    }

    private List<BookingClass> getCorrectHistory(){
        LinkedList<BookingClass> bookings = finalHistory;
        if(newHistory!=null){
            for(int i=0;i<newHistory.size();i++){
                arrayList.add(newHistory.get(i));
            }
        }
        if(arrayList==null || arrayList.size()==0){
            noHistory.setVisibility(View.VISIBLE);

        }
        return arrayList;
    }

}
