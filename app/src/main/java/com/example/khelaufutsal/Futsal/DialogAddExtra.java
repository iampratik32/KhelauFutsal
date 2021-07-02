package com.example.khelaufutsal.Futsal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DialogAddExtra extends AppCompatDialogFragment {

    private Context context;
    private List<String> extrasList;
    private String todo;
    FutsalExtraAdapter adapter;
    int position;

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

    public DialogAddExtra(Context context, List<String> extrasList, String todo,FutsalExtraAdapter adapter,int position) {
        if(context==null){
            throw new NullPointerException();
        }
        this.context = context;
        this.extrasList=extrasList;
        this.todo = todo;
        this.adapter=adapter;
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_extra,null);
        builder.setView(view);



        final TextInputEditText serviceText = view.findViewById(R.id.enterService_dialogAddExtra);
        ProgressBar progressBar = view.findViewById(R.id.progressBar_dialogAddExtra);
        Button okButton = view.findViewById(R.id.okButton_dialogAddExtra);
        TextView textView = view.findViewById(R.id.addService_dialogAddExtra);
        Button cancelButton = view.findViewById(R.id.cancelButton_dialogAddExtra);
        ImageView closeButton = view.findViewById(R.id.closeButton_dialogAddExtra);

        if (!todo.equals("Add")) {
            textView.setText("Edit Service");
            serviceText.setText(todo);
            cancelButton.setVisibility(View.VISIBLE);
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        final DatabaseReference extraDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Extras");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String taken = serviceText.getText().toString();
                if(todo.equals("Add")){
                    if(!taken.isEmpty()){
                        extraDb.child(taken).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    extrasList.add(0,taken);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                    dismiss();
                }
                else {
                    if(!taken.isEmpty()){
                        extraDb.child(todo).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    extraDb.child(taken).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                extrasList.remove(position-1);
                                                extrasList.add(position-1,taken);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    }
                    dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraDb.child(todo).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            extrasList.remove(position-1);
                            adapter.notifyDataSetChanged();
                            dismiss();
                        }
                    }
                });
            }
        });

        return builder.create();
    }
}
