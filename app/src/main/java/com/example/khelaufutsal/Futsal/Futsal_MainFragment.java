package com.example.khelaufutsal.Futsal;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.DialogNotVerifiedFutsal;
import com.example.khelaufutsal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Futsal_MainFragment extends Fragment {

    CountDownTimer countDownTimer;
    TextView futsalName;
    ImageView futsalDisplayPicture;
    ScrollView scrollView;
    ImageView verifiedIcon;
    Map<String,Integer> countMap = new HashMap<>();
    TextView topUserTextView;
    TextView noOfDeleteReq;
    ActivityOptions options;
    CardView toDelete, pendingBooking, bookedBooking;
    private TextView futsalPlayedTextView, pendingBookingsTextView, bookedBookingsTextView;
    final LinkedList uniqueUsers = new LinkedList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_futsal__main, container, false);
        scrollView = view.findViewById(R.id.scrollView_futsalMainFragment);
        scrollView.setVisibility(View.GONE);
        futsalDisplayPicture = view.findViewById(R.id.futsal_displayPicture_mainFutsalFragment);
        futsalName = view.findViewById(R.id.futsal_displayName_mainFutsalFragment);
        futsalPlayedTextView = view.findViewById(R.id.futsalPlayed);
        pendingBookingsTextView = view.findViewById(R.id.pendingBookings);
        bookedBookingsTextView = view.findViewById(R.id.bookedBookings);
        topUserTextView = view.findViewById(R.id.topUser);
        toDelete = view.findViewById(R.id.deleteReq_cardView);
        pendingBooking = view.findViewById(R.id.pendingBooking_cardView);
        bookedBooking = view.findViewById(R.id.bookedBooking_cardView);
        noOfDeleteReq=view.findViewById(R.id.noOfDeleteRequest);
        final RatingBar ratingBar = view.findViewById(R.id.futsal_ratingBar_mainFutsalFragment);
        ratingBar.setIsIndicator(true);
        options =ActivityOptions.makeCustomAnimation(getContext(), R.anim.right_to_left, R.anim.exit_right);

        ratingBar.setVisibility(View.GONE);

        verifiedIcon = view.findViewById(R.id.verifiedIcon_futsalMainFragment);
        verifiedIcon.setVisibility(View.GONE);

        pendingBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingIntent = new Intent(getActivity(),Futsal_BookingActivity.class);
                bookingIntent.putExtra("Open","Pending");
                startActivity(bookingIntent,options.toBundle());
            }
        });
        bookedBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBooked();
            }
        });
        toDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBooked();
            }
        });


        final ProgressDialog dialog ;
        if(FutsalHolder.getFutsal()==null){
            dialog = ProgressDialog.show(getActivity(), "","Please wait...", true);
            countDownTimer = new CountDownTimer(4000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    DatabaseReference historyDb = FirebaseDatabase.getInstance().getReference().child("History").child(FirebaseAuth.getInstance().getUid());
                    historyDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                    for(DataSnapshot post:postSnapshot.getChildren()){
                                        String userId = post.child("Taken").getValue().toString();
                                        if(!userId.equals(FutsalHolder.getFutsal().getUserId())){
                                            if(!countMap.containsKey(userId)){
                                                countMap.put(userId,1);
                                            }
                                            else {
                                                countMap.put(userId,countMap.get(userId)+1);
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
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                    getInformation(view);
                    return;
                }
            }.start();
        }
        else {
            getInformation(view);
        }



        return view;
    }

    private void openBooked(){
        Intent bookingIntent = new Intent(getActivity(),Futsal_BookingActivity.class);
        bookingIntent.putExtra("Open","Booked");
        startActivity(bookingIntent,options.toBundle());
    }

    private void getInformation(View view){
        if(FutsalHolder.getFutsal().getVerified().equals("false")){
            DialogNotVerifiedFutsal notVerifiedFutsal = new DialogNotVerifiedFutsal();
            notVerifiedFutsal.show(getFragmentManager(),"NotVerified");
        }
        scrollView.setVisibility(View.VISIBLE);
        futsalName.setText(FutsalHolder.getFutsal().getFutsalName());
        if(FutsalHolder.getFutsal().getDisplayPicture()!=null){
            Glide.with(getActivity()).load(Uri.parse(FutsalHolder.getFutsal().getDisplayPicture())).into(futsalDisplayPicture);
        }
        else {
            futsalDisplayPicture.setImageResource(R.drawable.futsal_icon);
        }
        if(FutsalHolder.getFutsal().getVerified().equals("true")){
            verifiedIcon.setVisibility(View.VISIBLE);
        }
        fillInDashboard();

        CardView ownBookingCardView = view.findViewById(R.id.bookOwnFutsalCardView);
        final ActivityOptions options =ActivityOptions.makeCustomAnimation(getContext(), R.anim.right_to_left, R.anim.exit_right);
        ownBookingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ASD",FutsalHolder.getFutsal().getVerified());
                if(FutsalHolder.getFutsal().getVerified().equals("true")){
                    Intent bookingIntent = new Intent(getActivity(),Futsal_BookingActivity.class);
                    bookingIntent.putExtra("Open","All");
                    startActivity(bookingIntent,options.toBundle());
                }
                else {
                    Toast.makeText(getContext(),"You need to be verified to use this functionality",Toast.LENGTH_SHORT).show();
                }
            }
        });

        CardView statsCardView = view.findViewById(R.id.viewFutsalStatsCardView);
        statsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statsIntent = new Intent(getActivity(),Futsal_StatsActivity.class);
                startActivity(statsIntent,options.toBundle());
            }
        });
    }

    private void fillInDashboard(){
        verifiedIcon.setVisibility(View.GONE);
        FutsalClass futsalClass = FutsalHolder.getFutsal();
        if(futsalClass.getFutsalHistory()==null){
            futsalPlayedTextView.setText("0");
        }
        else {
            futsalPlayedTextView.setText(String.valueOf(futsalClass.getFutsalHistory().size()));
        }
        if(futsalClass.getFutsalBooking()==null){
            pendingBookingsTextView.setText("0");
            bookedBookingsTextView.setText("0");
        }
        else {
            int pending=0;
            int booked=0;
            int toCancel=0;
            for(int i=0;i<futsalClass.getFutsalBooking().size();i++){
                if(futsalClass.getFutsalBooking().get(i).getBooked().equals("True") && futsalClass.getFutsalBooking().get(i).getConfirmed().equals("True")){
                    booked++;
                }
                else if(futsalClass.getFutsalBooking().get(i).getBooked().equals("False") && futsalClass.getFutsalBooking().get(i).getConfirmed().equals("False")){
                    pending++;
                }
                else {
                    toCancel++;
                }
            }
            pendingBookingsTextView.setText(String.valueOf(pending));
            bookedBookingsTextView.setText(String.valueOf(booked));
            if(toCancel!=0){
                String verb = " requests";
                if(toCancel==1){
                    verb=" request";
                }
                noOfDeleteReq.setText(toCancel+verb+" to cancel booking.");
                toDelete.setVisibility(View.VISIBLE);
            }
        }

        if(countMap.size()!=0){
            int maxValueInMap = (Collections.max(countMap.values()));
            for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
                if (entry.getValue()==maxValueInMap) {
                    DatabaseReference playerDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(entry.getKey()).child("DisplayName");
                    playerDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                topUserTextView.setText(dataSnapshot.getValue().toString());
                                FutsalHolder.getFutsal().setTopUserName(dataSnapshot.getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        }
        else if(FutsalHolder.getFutsal().getTopUserName()!=null){
            topUserTextView.setText(FutsalHolder.getFutsal().getTopUserName());
        }
        else {
            topUserTextView.setText("N/A");
        }

    }
}
