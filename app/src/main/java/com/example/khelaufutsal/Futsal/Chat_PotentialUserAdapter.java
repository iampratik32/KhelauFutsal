package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ConcurrentModificationException;
import java.util.List;

public class Chat_PotentialUserAdapter extends RecyclerView.Adapter<Chat_PotentialUserHolder> {

    private List<Chat_PotentialChatUser> userList;
    private List<Chat_PotentialChatUser> copyList;
    private Context context;

    public Chat_PotentialUserAdapter(List<Chat_PotentialChatUser> userList, Context context) {
        this.userList = userList;
        this.context = context;
        this.copyList=FutsalHolder.getFutsal().getChatUsers();
    }

    @NonNull
    @Override
    public Chat_PotentialUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_potentail_user_holder, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        Chat_PotentialUserHolder holder = new Chat_PotentialUserHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_PotentialUserHolder holder, int position) {
        holder.userName.setText(userList.get(position).getDisplayName());
        holder.userId.setText(userList.get(position).getUserId());
        if (userList.get(position).getProfileImage() != null) {
            Glide.with(context).load(Uri.parse(userList.get(position).getProfileImage())).into(holder.userDisplayPicture);
            holder.imageLink.setText(userList.get(position).getProfileImage());
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void filter(String constraint){
        try {
            userList.clear();
            if(constraint.length()==0 || constraint.trim().isEmpty()){
                userList.addAll(copyList);
                notifyDataSetChanged();
            }
            else {
                for(Chat_PotentialChatUser c:copyList){
                    if(c.getDisplayName().toLowerCase().contains(constraint)){
                        userList.add(c);
                    }
                }
                notifyDataSetChanged();
            }
        }
        catch (ConcurrentModificationException exception){
            //filter(constraint);
        }

    }

}