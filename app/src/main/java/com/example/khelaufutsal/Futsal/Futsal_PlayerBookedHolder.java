package com.example.khelaufutsal.Futsal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

public class Futsal_PlayerBookedHolder extends RecyclerView.ViewHolder{

    public TextView userName;
    public TextView userDate, userTime, secondaryUserDate, secondaryUserTime;
    LinearLayout toHide;
    public CardView cardView;
    public TextView userId;

    public Button button;

    public Futsal_PlayerBookedHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.userName_futsalPlayerBookedList);
        userDate = itemView.findViewById(R.id.userDate_futsalPlayerBookedList);
        userTime = itemView.findViewById(R.id.userTime_futsalPlayerBookedList);
        button  = itemView.findViewById(R.id.viewButton_futsalPlayerBookedList);
        secondaryUserDate=itemView.findViewById(R.id.secondaryUserDate_futsalPlayerBookedList);
        secondaryUserTime=itemView.findViewById(R.id.secondaryUserTime_futsalPlayerBookedList);
        toHide = itemView.findViewById(R.id.toHide_bookedList);
        toHide.setVisibility(View.GONE);
        cardView = itemView.findViewById(R.id.cardView_BookedList);
        userId = itemView.findViewById(R.id.userId_bookedList);

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
                Intent bookedIntent = new Intent(v.getContext(), Futsal_ConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Date",userDate.getText().toString());
                bundle.putString("Time",userTime.getText().toString());
                bundle.putString("UserId",userId.getText().toString());
                bundle.putString("UserName",userName.getText().toString());
                bundle.putString("Booked","True");
                bookedIntent.putExtras(bundle);
                v.getContext().startActivity(bookedIntent,options.toBundle());
            }
        });
    }
}
