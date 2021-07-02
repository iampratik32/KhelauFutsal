package com.example.khelaufutsal.Futsal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_PotentialUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView userName;
    public TextView userId;
    public CircleImageView userDisplayPicture;
    public TextView imageLink;

    public Chat_PotentialUserHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        userName = itemView.findViewById(R.id.chat_potential_userHolderName);
        userId = itemView.findViewById(R.id.chat_potential_userHolderId);
        userDisplayPicture = itemView.findViewById(R.id.chat_potential_userHolderImage);
        userId.setVisibility(View.GONE);
        imageLink = itemView.findViewById(R.id.chat_potential_userHolderLink);

    }

    @Override
    public void onClick(View v) {
        Intent chatIntent = new Intent(v.getContext(),ChatWithUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("UserId",userId.getText().toString());
        bundle.putString("UserName",userName.getText().toString());
        bundle.putString("ImageLink",imageLink.getText().toString());
        bundle.putString("UserType","Futsal");
        chatIntent.putExtras(bundle);
        v.getContext().startActivity(chatIntent);
    }
}
