package com.example.khelaufutsal.Player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

public class FutsalGroundHolder extends RecyclerView.ViewHolder{
    public TextView futsalName;
    public TextView futsalLocation;
    public RatingBar ratingBar;
    public TextView futsalId;
    public TextView futsalEmail;
    public TextView futsalNumber;
    public TextView futsalDisplayImage;
    public ImageView displayImage;
    public TextView numberOfImages;
    public TextView futsalPrice;
    public LinearLayout toHideLayout;
    public Button button;
    public TextView latLan;
    public ImageView verificationIcon;

    public FutsalGroundHolder(@NonNull  View itemView){
        super(itemView);

        futsalName = itemView.findViewById(R.id.futsalName_futsalGroundList);
        futsalLocation = itemView.findViewById(R.id.futsalLocation_futsalGroundList);
        ratingBar = itemView.findViewById(R.id.ratingBar_groundList);
        ratingBar.setIsIndicator(true);
        latLan = itemView.findViewById(R.id.latLan_groundList);
        ratingBar.setEnabled(false);
        futsalId = itemView.findViewById(R.id.futsalId_groundList);
        futsalNumber = itemView.findViewById(R.id.contactNumber_groundList);
        futsalEmail = itemView.findViewById(R.id.email_groundList);
        futsalDisplayImage = itemView.findViewById(R.id.futsalDisplayPictureLink);
        displayImage = itemView.findViewById(R.id.futsalImage_futsalGroundList);
        numberOfImages = itemView.findViewById(R.id.noOfImagesForFutsal);
        futsalPrice = itemView.findViewById(R.id.futsalPrice_futsalGroundList);
        toHideLayout = itemView.findViewById(R.id.toHideLayout_groundList);
        verificationIcon = itemView.findViewById(R.id.verificationIcon_groundList);
        button = itemView.findViewById(R.id.viewButton_groundList);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toHideLayout.getVisibility()==View.GONE){
                    toHideLayout.setVisibility(View.VISIBLE);
                }
                else {
                    toHideLayout.setVisibility(View.GONE);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(v.getContext(), Player_FutsalGroundDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("futsalName",futsalName.getText().toString());
                bundle.putString("futsalLocation",futsalLocation.getText().toString());
                bundle.putString("futsalId",futsalId.getText().toString());
                bundle.putString("futsalEmail",futsalEmail.getText().toString());
                bundle.putString("futsalNumber",futsalNumber.getText().toString());
                bundle.putString("futsalDisplayImage",futsalDisplayImage.getText().toString());
                bundle.putString("futsalNoOfImages",numberOfImages.getText().toString());
                bundle.putString("futsalPrice",futsalPrice.getText().toString());
                bundle.putString("futsalLatLan",latLan.getText().toString());
                chatIntent.putExtras(bundle);
                v.getContext().startActivity(chatIntent);
            }
        });



    }

}
