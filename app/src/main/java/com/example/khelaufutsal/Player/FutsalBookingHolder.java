package com.example.khelaufutsal.Player;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

public class FutsalBookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView timeTextView;
    public TextView statusTextView;
    public TextView dateTextView;
    public CardView cardView;

    public FutsalBookingHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        timeTextView = itemView.findViewById(R.id.futsalTime_bookingList);
        statusTextView = itemView.findViewById(R.id.bookingStatus_bookingList);
        dateTextView = itemView.findViewById(R.id.futsalDate_bookingList);
        cardView = itemView.findViewById(R.id.cardView_bookingList);
    }

    @Override
    public void onClick(View v) {
        if(statusTextView.getText().toString().equals("Free")){
            ActivityOptions options =ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.right_to_left, R.anim.exit_right);
            Intent intent = new Intent(v.getContext(), Player_FinalBookingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Timing",timeTextView.getText().toString());
            bundle.putString("Date",dateTextView.getText().toString());
            intent.putExtras(bundle);
            v.getContext().startActivity(intent,options.toBundle());
        }
    }
}
