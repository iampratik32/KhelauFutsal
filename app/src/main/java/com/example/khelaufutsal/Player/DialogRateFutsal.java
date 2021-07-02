package com.example.khelaufutsal.Player;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.Futsal.Futsal_SettingsActivity;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DialogRateFutsal extends AppCompatDialogFragment {
    String futsalId,futsalName;
    int totalUserRatings=0;
    float rating=0;
    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    dismiss();
                    return true;
                }
                else{
                    return false;
                }
            }
        });
    }

    public DialogRateFutsal(String futsalId) {
        this.futsalId=futsalId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rate_futsal,null);
        builder.setView(view);

        final TextView futsalNameTextView = view.findViewById(R.id.futsal_notVerified_DialogBox_text);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(futsalId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    futsalNameTextView.setText(dataSnapshot.child("FutsalName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final RatingBar ratingBar = view.findViewById(R.id.ratingBar_rateFutsal);
        final Button rateButton = view.findViewById(R.id.rateButton_rateFutsal);
        final DatabaseReference playerRating = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId()).child("ToRate");
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Ratings").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            totalUserRatings = Integer.parseInt(dataSnapshot.child("TotalUsers").getValue().toString());
                            rating = Float.parseFloat(dataSnapshot.child("Rating").getValue().toString());
                            totalUserRatings++;
                            float totalRating = rating+ratingBar.getRating();

                            Map newRating = new HashMap();
                            newRating.put("Rating",String.valueOf(totalRating));
                            newRating.put("TotalUsers",totalUserRatings);
                            databaseReference.child("Ratings").setValue(newRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        playerRating.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    dismiss();
                                                    return;
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        else {
                            Map newRating = new HashMap();
                            newRating.put("Rating",String.valueOf(ratingBar.getRating()));
                            newRating.put("TotalUsers","1");
                            databaseReference.child("Ratings").setValue(newRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        playerRating.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    dismiss();
                                                    return;
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return builder.create();
    }
}
