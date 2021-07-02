package com.example.khelaufutsal.Player;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khelaufutsal.R;
import com.example.khelaufutsal.RegisterDetailHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class PlayerEnterDetails_SecondFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    Button fourthContinue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player_enter_details__second, container, false);

        final TextInputEditText phoneNumber = view.findViewById(R.id.registerPlayer_contactNumber);
        progressBar = view.findViewById(R.id.progressBar_playerSecond);
        final TextInputEditText password = view.findViewById(R.id.registerPlayer_password);
        final TextInputEditText verifyPassword = view.findViewById(R.id.registerPlayer_verifyPassword);
        Button thirdContinue = view.findViewById(R.id.thirdContinueButton_forPlayers);
        fourthContinue = view.findViewById(R.id.fourthContinueButton_forPlayers);
        final LinearLayout linearLayout = view.findViewById(R.id.secondLinearLayout_registerPlayerSecond);
        linearLayout.setVisibility(View.GONE);
        thirdContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String takenPhoneNumber = phoneNumber.getText().toString().trim();

                if(takenPhoneNumber.isEmpty()){
                    phoneNumber.setError("You can't leave this empty.");
                    phoneNumber.requestFocus();
                }
                else if(!takenPhoneNumber.matches("[0-9]+")){
                    phoneNumber.setError("Enter valid phone Number");
                    phoneNumber.requestFocus();
                }
                else {
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }
        });
        fourthContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String takenPassword = password.getText().toString().trim();
                String takenVerifyPassword = verifyPassword.getText().toString().trim();
                if(takenPassword.isEmpty() || takenPassword.length()<7){
                    password.requestFocus();
                    Toast.makeText(getContext(),"Enter valid password.",Toast.LENGTH_LONG).show();
                }
                else if(takenVerifyPassword.isEmpty()){
                    verifyPassword.requestFocus();
                    Toast.makeText(getContext(),"Enter something to verify password.",Toast.LENGTH_LONG).show();
                }
                else if(!takenPassword.equals(takenVerifyPassword)){
                    verifyPassword.requestFocus();
                    Toast.makeText(getContext(),"Your passwords do not match.",Toast.LENGTH_LONG).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    fourthContinue.setEnabled(false);
                    RegisterDetailHolder.keepDetails(phoneNumber.getText().toString());
                    firebaseAuth = FirebaseAuth.getInstance();
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    String userId = user.getUid();
                    DatabaseReference currentUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(userId);
                    Map newUser = new HashMap();
                    LinkedList details = RegisterDetailHolder.getAllDetails();

                    newUser.put("Name",details.get(0));
                    newUser.put("DisplayName",details.get(1));
                    newUser.put("EmailAddress",user.getEmail());
                    newUser.put("ContactNumber",details.get(2));

                    currentUserDatabase.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(),"You have been registered successfully.",Toast.LENGTH_LONG).show();
                            }
                            else {
                                makeInvisible();
                                Toast.makeText(getContext(),"Cannot register.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),"tempPassword");
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updatePassword(takenPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            firebaseAuth.signOut();
                                            firebaseAuth.signInWithEmailAndPassword(user.getEmail(),takenPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful()){
                                                        Intent newIntent = new Intent(getActivity(),PlayersMainActivity.class);
                                                        startActivity(newIntent);
                                                        getActivity().finish();
                                                    }
                                                    else {
                                                        makeInvisible();
                                                    }
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(getContext(),"Can't Register at this moment,",Toast.LENGTH_LONG).show();
                                            makeInvisible();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(getContext(),"Authentication Failed.",Toast.LENGTH_LONG).show();
                                makeInvisible();
                            }
                        }
                    });

                }
            }
        });

        return view;
    }

    private void makeInvisible(){
        progressBar.setVisibility(View.GONE);
        fourthContinue.setEnabled(true);
    }


}
