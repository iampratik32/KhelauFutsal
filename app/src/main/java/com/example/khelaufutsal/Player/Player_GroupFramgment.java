package com.example.khelaufutsal.Player;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khelaufutsal.Futsal.Chat_PotentialChatUser;
import com.example.khelaufutsal.Futsal.Chat_PotentialUserAdapter;
import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.Notification.Token;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Player_GroupFramgment extends Fragment {

    private RecyclerView chatRecylerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Chat_PotentialChatUser> chatUsers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player__group_framgment, container, false);



        chatRecylerView = view.findViewById(R.id.messagesRecyclerView_chatFragment);
        chatRecylerView.setNestedScrollingEnabled(true);
        TextView emptyChat = view.findViewById(R.id.chatIsEmpty);
        chatRecylerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        chatRecylerView.setLayoutManager(layoutManager);
        adapter = new PlayerChat_PotentialUserAdapter(getMatches(), getContext());
        chatRecylerView.setAdapter(adapter);

        if (PlayerHolder.getPlayer().getChatUsers() != null) {
            chatRecylerView.setVisibility(View.VISIBLE);
            emptyChat.setVisibility(View.GONE);
            LinkedList<String> refinedList = new LinkedList<>();
            for (int i = 0; i < PlayerHolder.getPlayer().getChatUsers().size(); i++) {
                if(!refinedList.contains(PlayerHolder.getPlayer().getChatUsers().get(i).getUserId())){
                    refinedList.add(PlayerHolder.getPlayer().getChatUsers().get(i).getUserId());
                    chatUsers.add(PlayerHolder.getPlayer().getChatUsers().get(i));
                    adapter.notifyDataSetChanged();
                    updateToken(FirebaseInstanceId.getInstance().getToken());
                }
            }

        } else {
            chatRecylerView.setVisibility(View.GONE);
            emptyChat.setVisibility(View.VISIBLE);
        }

        return view;
    }


    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(PlayerHolder.getPlayer().getUserId()).setValue(token1);
    }

    private List<Chat_PotentialChatUser> getMatches() {
        return chatUsers;
    }

}