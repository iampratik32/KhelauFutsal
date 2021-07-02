package com.example.khelaufutsal.Futsal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

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

import com.example.khelaufutsal.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Futsal_HistoryFragment extends Fragment {

    private LinkedList<BookingClass> newHistory;
    private LinkedList<BookingClass> historyBookings;
    private LinkedList<BookingClass> finalHistory;
    private ImageView sortButton;
    BottomSheetDialog menuBottomSheetDialog;
    private ProgressBar progressBar;
    CountDownTimer countDownTimer;
    LinearLayout dateOption;
    LinearLayout dateOption2;
    private TextView noHistory;
    SimpleDateFormat date;
    SimpleDateFormat toLongDate;
    Date checkingDate;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<BookingClass> arrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment_futsal__history, container, false);

        historyBookings = FutsalHolder.getFutsal().getFutsalHistory();
        sortButton = view.findViewById(R.id.sortButton_futsalHistory);
        sortButton.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progressBar_futsalHistoryFragment);
        recyclerView = view.findViewById(R.id.recyclerView_futsalHistory);
        noHistory = view.findViewById(R.id.noHistory_futsalHistoryFragment);


        newHistory = new LinkedList<>();
        finalHistory = new LinkedList<>();

        final DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference().child("History").child(FutsalHolder.getFutsal().getUserId());
        historyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(historyBookings!=null){
                    readData(dataSnapshot, new onGetDataListener() {
                        @Override
                        public void onSuccess(FutsalClass futsalClass) {
                            finalHistory = FutsalHolder.getFutsal().getFutsalHistory();
                            countDownTimer = new CountDownTimer(1400,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    sortButton.setVisibility(View.VISIBLE);
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
                }
                else {
                    finalHistory = FutsalHolder.getFutsal().getFutsalHistory();
                    if(finalHistory==null){
                        noHistory.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    else {
                        displayRecyclerView(view);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sort_menu,null);
                dateOption = view.findViewById(R.id.dateOptionDialog_sortMenu);
                dateOption2 = view.findViewById(R.id.dateOption2Dialog_sortMenu);
                dateOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sorting("A");
                    }
                });
                dateOption2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sorting("D");
                    }
                });

                menuBottomSheetDialog = new BottomSheetDialog(getActivity());
                menuBottomSheetDialog.setContentView(view);
                menuBottomSheetDialog.show();
            }
        });


        return view;
    }


    private void displayRecyclerView(View view){
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Futsal_HistoryAdapter(getCorrectHistory(),getContext());
        recyclerView.setAdapter(adapter);
    }



    public interface onGetDataListener{
        void onSuccess(FutsalClass futsalClass);
        void onStart();
        void onFailure();
    }

    public void readData(DataSnapshot dataSnapshot, final Futsal_HistoryFragment.onGetDataListener listener){
        listener.onStart();

        FutsalHolder.getFutsal().setFutsalHistory(null);
        if(dataSnapshot.exists()){
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                for (DataSnapshot post : postSnapshot.getChildren()) {
                    BookingClass booking = new BookingClass(FutsalHolder.getFutsal().getUserId(), postSnapshot.getKey(), post.getKey(), post.child("Booked").getValue().toString()
                            , "", post.child("Taken").getValue().toString());
                    newHistory.add(booking);
                }
            }
            FutsalHolder.getFutsal().setFutsalHistory(newHistory);
            listener.onSuccess(FutsalHolder.getFutsal());
        }
        else {
            noHistory.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private List<BookingClass> getCorrectHistory(){
        LinkedList<BookingClass> bookings = finalHistory;
        if(bookings!=null){
            for(int i=0;i<bookings.size();i++){
                arrayList.add(bookings.get(i));
            }
        }
        return arrayList;
    }


    private void sorting(final String type){
        menuBottomSheetDialog.dismiss();
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Collections.sort(arrayList, new Comparator<BookingClass>() {
            @Override
            public int compare(BookingClass o1, BookingClass o2) {
                String o1date = o1.getDate();
                String tempol1 ="";
                String tempol11 = "";
                int q=0;
                for(int i=0;i<o1date.length();i++){
                    if(q!=0){
                        tempol1=tempol1+o1date.charAt(i);
                    }
                    else {
                        tempol11 = tempol11+o1date.charAt(i);
                    }
                    if(String.valueOf(o1date.charAt(i)).equals("-")){
                        q=1;
                    }
                }
                String newD = tempol1+tempol11;

                String o1datee = o2.getDate();
                String qtempol1 ="";
                String qtempol11 = "";
                int w=0;
                for(int i=0;i<o1datee.length();i++){
                    if(w!=0){
                        qtempol1=qtempol1+o1datee.charAt(i);
                    }
                    else {
                        qtempol11 = qtempol11+o1datee.charAt(i);
                    }
                    if(String.valueOf(o1datee.charAt(i)).equals("-")){
                        w=1;
                    }
                }
                String newD2 = qtempol1+qtempol11;
                return newD.compareTo(newD2);
            }
        });
        if(type.equals("D")){
            Collections.reverse(arrayList);
        }
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
