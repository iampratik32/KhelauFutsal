package com.example.khelaufutsal.Player;

import androidx.annotation.NonNull;

import com.example.khelaufutsal.Futsal.Chat_PotentialChatUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Player_LoadInformation {

    public static void loadChatData(){
        final ArrayList<Chat_PotentialChatUser> chatUsers = new ArrayList<>();
        DatabaseReference playerBooking = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId()).child("Bookings");
        playerBooking.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot post: dataSnapshot.getChildren()){
                        DatabaseReference bookingsDb = FirebaseDatabase.getInstance().getReference().child("Booking").child(post.child("Futsal").getValue().toString()).child(post.child("Date").getValue().toString()).child(post.child("Time").getValue().toString());
                        bookingsDb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    final Chat_PotentialChatUser chatUser = new Chat_PotentialChatUser(post.child("Futsal").getValue().toString(),"");
                                    DatabaseReference futsal = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(post.child("Futsal").getValue().toString());
                                    futsal.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                chatUser.setDisplayName(dataSnapshot.child("FutsalName").getValue().toString());
                                                chatUser.setProfileImage(dataSnapshot.child("DisplayPicture").getValue().toString());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    chatUsers.add(chatUser);
                                    PlayerHolder.getPlayer().setChatUsers(chatUsers);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
