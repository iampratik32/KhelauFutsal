package com.example.khelaufutsal.Player;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.khelaufutsal.LoginActivity;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DialogDeletePlayerAccount extends AppCompatDialogFragment {
    private Context context;

    public DialogDeletePlayerAccount(Context context) {
        if(context==null){
            throw new NullPointerException();
        }
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete_account,null);
        builder.setView(view);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference playerDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId());

        final Button cancelButton = view.findViewById(R.id.noButton_deleteAccountDialog);
        final Button deleteButton = view.findViewById(R.id.yesButton_deleteAccountDialog);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar_deleteAccount);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                cancelButton.setEnabled(false);
                deleteButton.setEnabled(false);
                user.reauthenticate(PlayerHolder.getPlayer().getCredential()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        playerDb.child("Bookings").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    for(DataSnapshot post:dataSnapshot.getChildren()){
                                                        String date = post.child("Date").getValue().toString();
                                                        String time = post.child("Time").getValue().toString();
                                                        String futsal = post.child("Futsal").getValue().toString();
                                                        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference().child("Booking").child(futsal).child(date).child(time);
                                                        bookingsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                playerDb.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                        FirebaseAuth.getInstance().signOut();
                                                                        Toast.makeText(getContext(), PlayerHolder.getPlayer().getUserName()+" Deleted From Our Record.",Toast.LENGTH_LONG).show();
                                                                        PlayerHolder.setPlayer(null);
                                                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivity(intent);
                                                                    }
                                                                });
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
                                    else {
                                        cancelButton.setEnabled(true);
                                        deleteButton.setEnabled(true);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(getContext(),"Can't validate you at this moment.",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            cancelButton.setEnabled(true);
                            deleteButton.setEnabled(true);
                        }
                    }
                });
            }
        });

        return builder.create();
    }
}
