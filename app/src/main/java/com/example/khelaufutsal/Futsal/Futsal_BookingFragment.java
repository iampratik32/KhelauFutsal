package com.example.khelaufutsal.Futsal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class Futsal_BookingFragment extends Fragment {

    LinkedList<BookingClass> bookingList;
    LinearLayout mainLinearLayout;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_futsal__booking, container, false);

        mainLinearLayout = view.findViewById(R.id.mainLinearLayout_futsal_Booking);
        progressBar = view.findViewById(R.id.progressBar_futsalBooking);

        readData(new onGetDataListener() {
            @Override
            public void onSuccess(FutsalClass futsalClass) {
                mainLinearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                final CardView pendingCardView = view.findViewById(R.id.pendingCardView_futsalBooking);
                final CardView bookedCardView = view.findViewById(R.id.bookedCardView_futsalBooking);
                final CardView allCardView = view.findViewById(R.id.allCardView_futsalBooking);

                final ActivityOptions options =ActivityOptions.makeCustomAnimation(getContext(), R.anim.right_to_left, R.anim.exit_right);


                pendingCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookingIntent = new Intent(getActivity(),Futsal_BookingActivity.class);
                        bookingIntent.putExtra("Open","Pending");
                        startActivity(bookingIntent,options.toBundle());

                    }
                });

                bookedCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookingIntent = new Intent(getActivity(),Futsal_BookingActivity.class);
                        bookingIntent.putExtra("Open","Booked");
                        startActivity(bookingIntent,options.toBundle());
                    }
                });

                allCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
            }

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
                mainLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure() {

            }
        });

        return view;
    }

    public interface onGetDataListener{
        void onSuccess(FutsalClass futsalClass);
        void onStart();
        void onFailure();
    }

    public void readData(final Futsal_BookingFragment.onGetDataListener listener){
        listener.onStart();
        if(FutsalHolder.getFutsal().getFutsalBooking()==null|| FutsalHolder.getFutsal().getFutsalBooking().size()==0){
            listener.onSuccess(FutsalHolder.getFutsal());
            return;
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    try{
                        if(FutsalHolder.getFutsal().getFutsalBooking().size()!=dataSnapshot.getChildrenCount()){
                            bookingList = new LinkedList<>();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                for (DataSnapshot post : postSnapshot.getChildren()) {
                                    BookingClass booking = new BookingClass(FutsalHolder.getFutsal().getUserId(), postSnapshot.getKey(), post.getKey(), post.child("Booked").getValue().toString()
                                            , post.child("Confirmed").getValue().toString(), post.child("Taken").getValue().toString());
                                    bookingList.add(booking);

                                }
                            }
                            FutsalHolder.getFutsal().setFutsalBooking(bookingList);
                            listener.onSuccess(FutsalHolder.getFutsal());
                        }
                        else{
                            listener.onSuccess(FutsalHolder.getFutsal());
                        }
                    }
                    catch (NullPointerException e){

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
