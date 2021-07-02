package com.example.khelaufutsal.Futsal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

public class Futsal_AllBookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView timeTextView;
    public TextView statusTextView;
    public TextView dateTextView;
    public CardView cardView;

    public Futsal_AllBookingHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        timeTextView = itemView.findViewById(R.id.futsalTime_allBookingList);
        statusTextView = itemView.findViewById(R.id.bookingStatus_allBookingList);
        dateTextView = itemView.findViewById(R.id.futsalDate_allBookingList);
        cardView = itemView.findViewById(R.id.cardView_allBookingList);
    }

    @Override
    public void onClick(View v) {
        if(statusTextView.getText().toString().equals("Not Taken")){
            ActivityOptions options =ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.right_to_left, R.anim.exit_right);
            Intent intent = new Intent(itemView.getContext(),Futsal_OwnBooking.class);
            Bundle bundle = new Bundle();
            bundle.putString("Time",timeTextView.getText().toString());
            bundle.putString("Date",dateTextView.getText().toString());
            intent.putExtras(bundle);
            itemView.getContext().startActivity(intent,options.toBundle());
        }
    }
}
