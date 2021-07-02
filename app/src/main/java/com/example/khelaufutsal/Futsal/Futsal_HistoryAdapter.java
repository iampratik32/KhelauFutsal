package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.Player.Futsal_HistoryHolder;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Futsal_HistoryAdapter extends RecyclerView.Adapter<Futsal_HistoryHolder> {
    private List<BookingClass> historyList;
    String userId = "";
    private Context context;

    public Futsal_HistoryAdapter(List<BookingClass> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public Futsal_HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.futsal_history_list,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        Futsal_HistoryHolder holder = new Futsal_HistoryHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Futsal_HistoryHolder holder, int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(historyList.get(position).getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
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
        if(holder.userName.getText().toString().isEmpty()){
            holder.userName.setText(FutsalHolder.getFutsal().getFutsalName());
            holder.thisUser.setText("Booked this at");
        }
        holder.userDate.setText(historyList.get(position).getDate());
        holder.userTime.setText(historyList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


}
