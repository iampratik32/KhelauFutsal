package com.example.khelaufutsal.Futsal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DialogFutsalRejectBooking extends AppCompatDialogFragment {
    Button yesButton, noButton;
    ProgressBar progressBar;
    String time, date, userId;

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

    public DialogFutsalRejectBooking(String time, String date, String userId){
        this.time = time;
        this.date =date;
        this.userId = userId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reject_booking,null);
        builder.setView(view);

        yesButton = view.findViewById(R.id.yesButton_rejectBookingDialog);
        noButton = view.findViewById(R.id.noButton_rejectBookingDialog);
        progressBar = view.findViewById(R.id.progressBar_rejectBooking);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Log.d("UserId",userId);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                yesButton.setEnabled(false);
                noButton.setEnabled(false);

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId()).child(date).child(time);
                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            final DatabaseReference playerDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(userId).child("Bookings");
                            playerDb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for (DataSnapshot post:dataSnapshot.getChildren()){

                                            if(post.child("Date").getValue().toString().equals(date) && post.child("Time").getValue().toString().equals(time) && post.child("Futsal").getValue().toString().equals(FutsalHolder.getFutsal().getUserId())){
                                                DatabaseReference toDelete = playerDb.child(post.getKey());
                                                final String date = post.child("Date").getValue().toString();
                                                final String time = post.child("Time").getValue().toString();
                                                toDelete.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            dismiss();
                                                            for(int i=0;i<FutsalHolder.getFutsal().getFutsalBooking().size();i++){
                                                                BookingClass prev = FutsalHolder.getFutsal().getFutsalBooking().get(i);
                                                                if(prev.getDate().equals(date) && prev.getTime().equals(time)){
                                                                    FutsalHolder.getFutsal().getFutsalBooking().remove(i);
                                                                    break;
                                                                }
                                                            }
                                                            Intent bookingIntent = new Intent(getActivity(),Futsal_BookingActivity.class);
                                                            bookingIntent.putExtra("Open","Pending");
                                                            bookingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(bookingIntent);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
            }
        });
        return builder.create();
    }
}
