package com.example.khelaufutsal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.Futsal.Futsal_AccountSettingsFragment;
import com.example.khelaufutsal.Player.PlayerHolder;
import com.example.khelaufutsal.Player.Player_AccountSettingsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DialogVerifyPassword extends AppCompatDialogFragment {

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

    public DialogVerifyPassword(Context context) {
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
        View view = inflater.inflate(R.layout.dialog_verify_password,null);
        builder.setView(view);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Button verifyButton = view.findViewById(R.id.verifyButton_VerifyDialog);
        final TextInputEditText passwordEditText = view.findViewById(R.id.enterPassword_verifyPasswordDialog);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar_VerifyPassword);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                verifyButton.setEnabled(false);
                String takenText = passwordEditText.getText().toString().trim();
                if(takenText.isEmpty()){
                    Toast.makeText(context,"Enter Password To Continue.",Toast.LENGTH_SHORT).show();
                    passwordEditText.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                final AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),takenText);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            verifyButton.setEnabled(true);
                            if(FutsalHolder.getFutsal()!=null){
                                FutsalHolder.getFutsal().setCredential(credential);
                                dismiss();
                                Fragment newFragment = new Futsal_AccountSettingsFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayout_FutsalSettings,newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                            else {
                                PlayerHolder.getPlayer().setCredential(credential);
                                dismiss();
                                Fragment newFragment = new Player_AccountSettingsFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayout_PlayerSettings,newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }
                        else {
                            Toast.makeText(context,"Enter Correct Password.",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            verifyButton.setEnabled(true);
                        }
                    }
                });
            }
        });

        return builder.create();
    }
}
