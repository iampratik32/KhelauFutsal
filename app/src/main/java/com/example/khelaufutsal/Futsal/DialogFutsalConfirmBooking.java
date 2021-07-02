package com.example.khelaufutsal.Futsal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DialogFutsalConfirmBooking extends AppCompatDialogFragment {
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

    public DialogFutsalConfirmBooking(String time, String date, String userId) {
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

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId()).child(date).child(time);
                Map newMap = new HashMap();
                newMap.put("Booked","True");
                newMap.put("Confirmed","True");
                newMap.put("Taken",userId);
                databaseReference.setValue(newMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dismiss();
                            //TODO REMOVE BOOKING
                            Intent bookingIntent = new Intent(getActivity(),Futsal_BookingActivity.class);
                            bookingIntent.putExtra("Open","Pending");
                            bookingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(bookingIntent);
                        }
                        else {
                            Toast.makeText(getContext(),"Can't Confirm At This Moment.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return builder.create();
    }
}
