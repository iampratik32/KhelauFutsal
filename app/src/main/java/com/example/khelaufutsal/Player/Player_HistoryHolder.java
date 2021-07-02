package com.example.khelaufutsal.Player;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

public class Player_HistoryHolder extends RecyclerView.ViewHolder{
    public TextView userName;
    public TextView userDate;
    public TextView userTime;
    LinearLayout toHide;
    public CardView cardView;

    public Player_HistoryHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.userName_futsalHistoryList);
        userDate = itemView.findViewById(R.id.userDate_futsalHistoryList);
        userTime = itemView.findViewById(R.id.userTime_futsalHistoryList);
        toHide = itemView.findViewById(R.id.toHide_futsalHistoryList);
        cardView = itemView.findViewById(R.id.cardView_futsalHistoryList);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toHide.getVisibility()==View.GONE){
                    toHide.setVisibility(View.VISIBLE);
                    cardView.getLayoutParams().height=350;
                }
                else {
                    toHide.setVisibility(View.GONE);
                    cardView.getLayoutParams().height=100;
                }
            }
        });


    }


}
