package com.example.khelaufutsal.Player;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.Futsal.BookingClass;
import com.example.khelaufutsal.R;

import java.util.List;

public class PlayerBookingAdapter extends RecyclerView.Adapter<PlayerBookingHolder> {
    public PlayerBookingAdapter(List<BookingClass> groundList, Context context) {
        this.groundList = groundList;
        this.context = context;
    }

    private List<BookingClass> groundList;
    private Context context;

    @NonNull
    @Override
    public PlayerBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_booking_list,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        PlayerBookingHolder holder = new PlayerBookingHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerBookingHolder holder, int position) {
        holder.futsalName.setText(groundList.get(position).getFutsalName());
        holder.futsalDate.setText(groundList.get(position).getDate());
        holder.futsalTime.setText(groundList.get(position).getTime());
        holder.futsalDisplayImageLink.setText(groundList.get(position).getFutsalDisplayImage());
        Glide.with(context).load(groundList.get(position).getFutsalDisplayImage()).into(holder.futsalDisplayImage);
        holder.futsalId.setText(groundList.get(position).getFutsalId());

    }

    @Override
    public int getItemCount() {
        try {
            return groundList.size();
        }
        catch (NullPointerException e){
            return 0;
        }
    }
}
