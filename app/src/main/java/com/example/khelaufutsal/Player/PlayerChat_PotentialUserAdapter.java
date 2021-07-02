package com.example.khelaufutsal.Player;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.Futsal.Chat_PotentialChatUser;
import com.example.khelaufutsal.Futsal.Chat_PotentialUserHolder;
import com.example.khelaufutsal.R;

import java.util.List;

public class PlayerChat_PotentialUserAdapter extends RecyclerView.Adapter<PlayerChat_PotentialUserHolder> {

    private List<Chat_PotentialChatUser> userList;
    private Context context;

    public PlayerChat_PotentialUserAdapter(List<Chat_PotentialChatUser> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlayerChat_PotentialUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_potentail_user_holder, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        PlayerChat_PotentialUserHolder holder = new PlayerChat_PotentialUserHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerChat_PotentialUserHolder holder, int position) {
        holder.userName.setText(userList.get(position).getDisplayName());
        holder.userId.setText(userList.get(position).getUserId());
        if (userList.get(position).getProfileImage() != null) {
            Glide.with(context).load(Uri.parse(userList.get(position).getProfileImage())).into(holder.userDisplayPicture);
            holder.imageLink.setText(userList.get(position).getProfileImage());
        }
        holder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}