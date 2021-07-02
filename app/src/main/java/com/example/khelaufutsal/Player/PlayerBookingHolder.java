package com.example.khelaufutsal.Player;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

public class PlayerBookingHolder extends RecyclerView.ViewHolder{

    public TextView futsalName,futsalDate, futsalTime, futsalDisplayImageLink,futsalId;
    public Button manageButton;
    public ImageView futsalDisplayImage;


    public PlayerBookingHolder(@NonNull View itemView) {
        super(itemView);

        futsalName = itemView.findViewById(R.id.futsalName_playerBookingList);
        futsalDate = itemView.findViewById(R.id.futsalDate_playerBookingList);
        futsalTime = itemView.findViewById(R.id.futsalTime_playerBookingList);
        futsalDisplayImageLink = itemView.findViewById(R.id.futsalDisplayImageLink_playerBooking);
        manageButton = itemView.findViewById(R.id.manageButton_playerBookingList);
        futsalDisplayImage = itemView.findViewById(R.id.imageView_playerBookingList);
        futsalId = itemView.findViewById(R.id.futsalIdplayerBooking);

        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(v.getContext(),Player_ManageBookingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FutsalName",futsalName.getText().toString());
                bundle.putString("Date",futsalDate.getText().toString());
                bundle.putString("Time",futsalTime.getText().toString());
                bundle.putString("DisplayImageLink",futsalDisplayImageLink.getText().toString());
                bundle.putString("FutsalId",futsalId.getText().toString());
                newIntent.putExtras(bundle);
                v.getContext().startActivity(newIntent);
            }
        });


    }
}
