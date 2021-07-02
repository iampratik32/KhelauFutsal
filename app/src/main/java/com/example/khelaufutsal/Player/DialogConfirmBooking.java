package com.example.khelaufutsal.Player;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.khelaufutsal.Futsal.BookingClass;
import com.example.khelaufutsal.Player.Player_FutsalClass;
import com.example.khelaufutsal.Player.Player_FutsalClassHolder;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DialogConfirmBooking extends AppCompatDialogFragment {

    Button yesButton, noButton;
    ProgressBar progressBar;
    String time, date;

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

    public DialogConfirmBooking(String time, String date) {
        this.time = time;
        this.date =date;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirm_booking,null);
        builder.setView(view);

        yesButton = view.findViewById(R.id.yesButton_confirmBookingDialog);
        noButton = view.findViewById(R.id.noButton_confirmBookingDialog);
        progressBar = view.findViewById(R.id.progressBar_confirmBooking);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                yesButton.setEnabled(false);
                noButton.setEnabled(false);

                final Player_FutsalClass futsal = Player_FutsalClassHolder.getFutsal();
                DatabaseReference bookingDatabase = FirebaseDatabase.getInstance().getReference().child("Booking").child(futsal.getFutsalId()).child(date).child(time);
                final Map newMap = new HashMap();
                newMap.put("Taken",PlayerHolder.getPlayer().getUserId());
                newMap.put("Confirmed","False");
                newMap.put("Booked","False");
                bookingDatabase.setValue(newMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId()).child("Bookings");
                            Map anotherMap = new HashMap();
                            anotherMap.put("Futsal",futsal.getFutsalId());
                            anotherMap.put("Date",date);
                            anotherMap.put("Time",time);
                            databaseReference.push().setValue(anotherMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(),"Your Booking Request is sent out to the Futsal",Toast.LENGTH_LONG).show();
                                        try {
                                            dismiss();
                                            PlayerHolder.setPlayer(null);
                                            Intent intent = new Intent(getContext(),PlayersMainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        catch (IllegalStateException e){
                                        }
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(getContext(),"Unexpected Error Occurred!!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            yesButton.setEnabled(true);
                            noButton.setEnabled(true);
                        }
                    }
                });
            }
        });

        return builder.create();
    }
}
