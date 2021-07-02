package com.example.khelaufutsal.Futsal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

public class Futsal_PlayerPendingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView userName;
    public TextView userId;
    public TextView userDate;
    public TextView userTime;
    public TextView imageLink;
    LinearLayout toHide;
    public Button button;
    public CardView cardView;

    public Futsal_PlayerPendingHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        userName = itemView.findViewById(R.id.userName_futsalPlayerBookingList);
        userId = itemView.findViewById(R.id.playerId_pendingList);
        userDate = itemView.findViewById(R.id.userDate_futsalPlayerBookingList);
        userTime = itemView.findViewById(R.id.userTime_futsalPlayerBookingList);
        imageLink=itemView.findViewById(R.id.displayPicture_futsalPlayerBookingList);
        button = itemView.findViewById(R.id.dealWithButton_futsalPlayerBookingList);
        toHide = itemView.findViewById(R.id.toHide_pendingList);
        toHide.setVisibility(View.GONE);
        cardView = itemView.findViewById(R.id.cardView_pendingList);
    }


    @Override
    public void onClick(View v) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toHide.getVisibility()==View.GONE){
                    toHide.setVisibility(View.VISIBLE);
                    cardView.getLayoutParams().height=450;
                }
                else {
                    toHide.setVisibility(View.GONE);
                    cardView.getLayoutParams().height=100;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options =ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.right_to_left, R.anim.exit_right);
                Intent pendingIntent = new Intent(v.getContext(), Futsal_ConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Date",userDate.getText().toString());
                bundle.putString("Time",userTime.getText().toString());
                bundle.putString("UserId",userId.getText().toString());
                bundle.putString("UserName",userName.getText().toString());
                bundle.putString("DisplayPictureLink",imageLink.getText().toString());
                bundle.putString("Booked","False");
                pendingIntent.putExtras(bundle);
                v.getContext().startActivity(pendingIntent,options.toBundle());
            }
        });

    }

}
