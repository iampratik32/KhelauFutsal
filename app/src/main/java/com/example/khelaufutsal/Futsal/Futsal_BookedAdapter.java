package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Futsal_BookedAdapter extends RecyclerView.Adapter<Futsal_PlayerBookedHolder> {
    private List<BookingClass> bookingList;
    private Context context;

    public Futsal_BookedAdapter(List<BookingClass> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public Futsal_PlayerBookedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.futsal_player_booked_list,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        Futsal_PlayerBookedHolder holder = new Futsal_PlayerBookedHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Futsal_PlayerBookedHolder holder, int position) {
        holder.userId.setText(bookingList.get(position).getUserId());
        holder.userDate.setText(bookingList.get(position).getDate());
        holder.userTime.setText(bookingList.get(position).getTime());
        if(bookingList.get(position).getConfirmed().equals("RequestCancel")){
            holder.userDate.setVisibility(View.GONE);
            holder.userTime.setVisibility(View.GONE);
            holder.secondaryUserDate.setVisibility(View.VISIBLE);
            holder.secondaryUserTime.setVisibility(View.VISIBLE);
            holder.secondaryUserTime.setText("Request for Cancellation");
            holder.secondaryUserDate.setText(bookingList.get(position).getDate()+" At "+bookingList.get(position).getTime());
        }
        else {
            holder.userDate.setText(bookingList.get(position).getDate());
            holder.userTime.setText(bookingList.get(position).getTime());
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(bookingList.get(position).getUserId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    holder.userName.setText(dataSnapshot.child("DisplayName").getValue().toString());
                }
                else {
                    holder.userName.setText(FutsalHolder.getFutsal().getFutsalName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
}
