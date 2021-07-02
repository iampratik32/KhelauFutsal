package com.example.khelaufutsal.Futsal;

import android.app.Dialog;
import android.content.Context;
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

public class DialogDeleteFutsalAccount extends AppCompatDialogFragment {

    private Context context;
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

    public DialogDeleteFutsalAccount(Context context) {
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
        final DatabaseReference bookingsDb = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId());
        final DatabaseReference futsalDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId());

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
                deleteButton.setEnabled(false);
                cancelButton.setEnabled(false);
                user.reauthenticate(FutsalHolder.getFutsal().getCredential()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        futsalDb.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                bookingsDb.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        FirebaseAuth.getInstance().signOut();
                                                        Toast.makeText(getContext(),FutsalHolder.getFutsal().getFutsalName()+" Deleted From Our Record.",Toast.LENGTH_LONG).show();
                                                        FutsalHolder.setFutsal(null);
                                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(context,"Couldn't Delete at this moment. Try Again.",Toast.LENGTH_LONG).show();
                                        deleteButton.setEnabled(true);
                                        cancelButton.setEnabled(true);
                                    }

                                }
                            });
                        }
                        else {
                            Toast.makeText(getContext(),"Can't validate you at this moment.",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            deleteButton.setEnabled(true);
                            cancelButton.setEnabled(true);

                        }
                    }
                });
            }
        });



        return builder.create();
    }
}
