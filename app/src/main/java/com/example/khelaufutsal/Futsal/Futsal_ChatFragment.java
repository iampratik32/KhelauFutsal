package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.khelaufutsal.Notification.Token;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Futsal_ChatFragment extends Fragment {

    private RecyclerView chatRecylerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Chat_PotentialChatUser> chatUsers = new ArrayList<>();
    private EditText searchTextField;
    private LinkedList<String> userIds = new LinkedList<>();
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_futsal__chat, container, false);


        chatRecylerView = view.findViewById(R.id.messagesRecyclerView_chatFragment);
        chatRecylerView.setNestedScrollingEnabled(true);
        TextView emptyChat = view.findViewById(R.id.chatIsEmpty);
        chatRecylerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        chatRecylerView.setLayoutManager(layoutManager);
        adapter = new Chat_PotentialUserAdapter(getMatches(), getContext());
        chatRecylerView.setAdapter(adapter);

        if (FutsalHolder.getFutsal().getChatUsers() != null) {
            chatRecylerView.setVisibility(View.VISIBLE);
            emptyChat.setVisibility(View.GONE);
            for (int i = 0; i < FutsalHolder.getFutsal().getChatUsers().size(); i++) {
                if(!userIds.contains(FutsalHolder.getFutsal().getChatUsers().get(i).getUserId())){
                    chatUsers.add(FutsalHolder.getFutsal().getChatUsers().get(i));
                    adapter.notifyDataSetChanged();
                    updateToken(FirebaseInstanceId.getInstance().getToken());
                    userIds.add(FutsalHolder.getFutsal().getChatUsers().get(i).getUserId());
                }

            }
        } else {
            chatRecylerView.setVisibility(View.GONE);
            emptyChat.setVisibility(View.VISIBLE);
        }

        androidx.appcompat.widget.SearchView searchView  = view.findViewById(R.id.searchView_chatUsers);
        searchView.setQueryHint("Search Chat Users");
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((Chat_PotentialUserAdapter) adapter).filter(newText);
                return true;
            }
        });

        return view;
    }


    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(FutsalHolder.getFutsal().getUserId()).setValue(token1);
    }

    private List<Chat_PotentialChatUser> getMatches() {
        return chatUsers;
    }

}
