package com.example.khelaufutsal;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DialogCancelRegistry extends AppCompatDialogFragment {

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cancel_register_dialog,null);
        builder.setView(view);

        Button yesButton = view.findViewById(R.id.yesButton_dialogBox);
        Button noButton = view.findViewById(R.id.noButton_dialogBox);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            try {
                                throw task.getException();
                            }
                            catch (Exception e) {
                                Log.d("ASD",e.toString());

                                e.printStackTrace();
                            }
                        }
                    }
                });
                Intent newIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(newIntent);
            }
        });

        return builder.create();

    }
}
