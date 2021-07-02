package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.Player.PlayerHolder;
import com.example.khelaufutsal.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT = 1;

    private Context context;
    private List<Chat> chatList;
    private String imageUrl;
    private String type;

    public ChatAdapter(Context context, List<Chat> chatList, String imageUrl,String type) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==MSG_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_sender_message,parent,false);
            return new ChatAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_receiver_message,parent,false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.ViewHolder holder, int position) {

        Chat chat = chatList.get(position);
        if(chat!=null){
            holder.showMessage.setText(chat.getMessage());
            String type = chat.getType();
            SimpleDateFormat formatter= new SimpleDateFormat("MM/dd 'at' HH:mm");
            Date date = new Date(chat.getMessageTime());
            holder.chatTime.setText(formatter.format(date));
            holder.chatTime.setVisibility(View.GONE);
            if(imageUrl!=null && !imageUrl.isEmpty()){
                Glide.with(context).load(Uri.parse(imageUrl)).into(holder.profilePhoto);
            }
            holder.showMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.chatTime.getVisibility()==View.VISIBLE){
                        holder.chatTime.setVisibility(View.GONE);
                    }
                    else {
                        holder.chatTime.setVisibility(View.VISIBLE);
                    }
                }
            });
            holder.chatPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.chatTime.getVisibility()==View.VISIBLE){
                        holder.chatTime.setVisibility(View.GONE);
                    }
                    else {
                        holder.chatTime.setVisibility(View.VISIBLE);
                    }
                }
            });
            if(type==null){
                holder.showMessage.setVisibility(View.VISIBLE);
                holder.chatPhoto.setVisibility(View.GONE);
            }
            else if(type.equals("image")){
                holder.chatPhoto.setVisibility(View.VISIBLE);
                holder.showMessage.setVisibility(View.GONE);
                Glide.with(context).load(Uri.parse(chat.getMessage())).into(holder.chatPhoto);
            }
            else {
                holder.showMessage.setVisibility(View.VISIBLE);
                holder.chatPhoto.setVisibility(View.GONE);
            }
            try{
                if(position == chatList.size()-1){
                    if(chat.getSeen().equals("true")){
                        holder.seenMessage.setText("Seen");
                    }
                    else {
                        holder.seenMessage.setText("Delivered");
                    }
                }
                else {
                    holder.seenMessage.setVisibility(View.GONE);
                }
            }
            catch (NullPointerException e){

            }
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView showMessage;
        public ImageView profilePhoto;
        public TextView chatTime;
        public TextView userDisplayImage;
        public ImageView chatPhoto;
        public TextView seenMessage;

        public ViewHolder(View itemView){
            super(itemView);

            showMessage = itemView.findViewById(R.id.chat_message);
            chatTime = itemView.findViewById(R.id.chatTime_chatLayout);
            chatPhoto = itemView.findViewById(R.id.chat_sendingImage);
            profilePhoto = itemView.findViewById(R.id.chatReceiver_profileImage);
            userDisplayImage = itemView.findViewById(R.id.userImage_chatLayout);
            seenMessage = itemView.findViewById(R.id.textSeenMessage);


        }
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("Futsal")){
            if(chatList.get(position).getSender().equals(FutsalHolder.getFutsal().getUserId())){
                return MSG_RIGHT;
            }
            else {
                return MSG_LEFT;
            }
        }
        else {
            if(chatList.get(position).getSender().equals(PlayerHolder.getPlayer().getUserId())){
                return MSG_RIGHT;
            }
            else {
                return MSG_LEFT;
            }
        }
    }
}