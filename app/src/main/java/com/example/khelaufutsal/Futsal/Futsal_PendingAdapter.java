package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Futsal_PendingAdapter extends RecyclerView.Adapter<Futsal_PlayerPendingHolder> {
    private List<BookingClass> bookingList;
    String userId = "";
    private Context context;

    public Futsal_PendingAdapter(List<BookingClass> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public Futsal_PlayerPendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.futsal_player_pending_list,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        Futsal_PlayerPendingHolder holder = new Futsal_PlayerPendingHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Futsal_PlayerPendingHolder holder, final int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(bookingList.get(position).getUserId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    holder.userName.setText(dataSnapshot.child("DisplayName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        holder.userDate.setText(bookingList.get(position).getDate());
        holder.userTime.setText(bookingList.get(position).getTime());
        holder.userId.setText(bookingList.get(position).getUserId());
        holder.imageLink.setText(bookingList.get(position).getFutsalDisplayImage());

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }



}
