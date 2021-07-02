package com.example.khelaufutsal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.Futsal.Futsal_SettingsActivity;

public class DialogNotVerifiedFutsal extends AppCompatDialogFragment {

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
        View view = inflater.inflate(R.layout.dialog_not_verified_futsal,null);
        builder.setView(view);

        Button yesButton = view.findViewById(R.id.okButton_NotVerifiedDialog);
        TextView futsal  = view.findViewById(R.id.futsal_notVerified_DialogBox_text);
        futsal.setText(FutsalHolder.getFutsal().getFutsalName()+" is not verified.");
        Button noButton = view.findViewById(R.id.noButton_NotVerifiedDialog);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity(), Futsal_SettingsActivity.class);
                startActivity(newIntent);
                dismiss();
            }
        });

        return builder.create();

    }

}
