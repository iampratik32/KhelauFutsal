package com.example.khelaufutsal.Futsal;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class LoadCustomizableData {

    public static void loadTiming(DatabaseReference futsalDatabase){
        futsalDatabase.child("Timing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("OpeningTime").exists() && dataSnapshot.child("ClosingTime").exists()) {
                        String openTime = dataSnapshot.child("OpeningTime").getValue().toString();
                        String closeTime = dataSnapshot.child("ClosingTime").getValue().toString();
                        keepTiming(openTime,closeTime);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void loadOpenDays(DatabaseReference futsalDatabase){
        final LinkedList tempOpenDays = new LinkedList();
        futsalDatabase.child("Timing").child("OpenDays").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        tempOpenDays.add(postSnapshot.getKey());
                    }
                }
                FutsalHolder.getFutsal().setFutsalOpenDays(tempOpenDays);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void loadImages(DatabaseReference futsalDatabase){
        final LinkedList keepImages;
        keepImages = new LinkedList();
        DatabaseReference images = futsalDatabase.child("Images");
        images.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.child("Link").exists() && postSnapshot.child("Link") != null) {
                            String imageName = postSnapshot.child("Link").getValue().toString();
                            keepImages.add(imageName);
                        }
                    }
                    FutsalHolder.getFutsal().setFutsalImages(keepImages);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void keepTiming(String openTime, String closeTime){
        String futsalOpeningTime = "";
        for (int i = 0; i < openTime.length(); i++) {
            if (String.valueOf(openTime.charAt(i)).equals(":")) {
                break;
            }
            futsalOpeningTime = futsalOpeningTime + openTime.charAt(i);
        }
        String futsalClosingTime = "";
        for (int i = 0; i < closeTime.length(); i++) {
            if (String.valueOf(closeTime.charAt(i)).equals(":")) {
                break;
            }
            futsalClosingTime = futsalClosingTime + closeTime.charAt(i);
        }
        int openingTime = Integer.parseInt(futsalOpeningTime);
        int closingTime = Integer.parseInt(futsalClosingTime);
        LinkedList<String> tempLinkedList = new LinkedList<>();
        for (int i = openingTime; i <= closingTime; i++) {
            tempLinkedList.add(i + ":00");
        }
        FutsalHolder.getFutsal().setFutsalOpenTime(tempLinkedList);
    }

}
