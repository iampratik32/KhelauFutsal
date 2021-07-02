package com.example.khelaufutsal.Futsal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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


public class FutsalEnterDetails_SecondFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    Button sixthContinue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view  = inflater.inflate(R.layout.fragment_futsal_enter_details__second, container, false);

        final TextInputEditText ownerName = view.findViewById(R.id.registerFutsal_ownerName);
        final TextInputEditText personalContactNumber = view.findViewById(R.id.registerFutsal_personalContactNumber);
        final TextInputEditText futsalContactNumber = view.findViewById(R.id.registerFutsal_futsalContactNumber);
        final TextInputEditText personalEmail = view.findViewById(R.id.registerFutsal_emailAddress);
        final TextInputEditText password = view.findViewById(R.id.registerFutsal_password);
        progressBar = view.findViewById(R.id.progressBar_futsalSecond);
        final TextInputEditText verifyPassword = view.findViewById(R.id.registerFutsal_verifyPassword);
        final ScrollView scrollView = view.findViewById(R.id.scrollView_futsalEnterSecondDetail);
        ImageView backButton = view.findViewById(R.id.backButton_futsalSecond);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        Button thirdContinue = view.findViewById(R.id.thirdContinueButton_forFutsal);
        Button fourthContinue = view.findViewById(R.id.fourthContinueButton_forFutsal);
        Button fifthContinue = view.findViewById(R.id.fifthContinueButton_forFutsal);
        sixthContinue = view.findViewById(R.id.sixthContinueButton_forFutsal);

        final LinearLayout fourthLinearLayout = view.findViewById(R.id.fourthLinearLayout_registerFutsal);
        final LinearLayout fifthLinearLayout = view.findViewById(R.id.fifthLinearLayout_registerFutsal);
        final LinearLayout sixthLinearLayout = view.findViewById(R.id.sixthLinearLayout_registerFutsal);
        fourthLinearLayout.setVisibility(View.GONE);
        fifthLinearLayout.setVisibility(View.GONE);
        sixthLinearLayout.setVisibility(View.GONE);

        thirdContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String takenOwnerName = ownerName.getText().toString().trim();

                if(takenOwnerName.isEmpty()){
                    ownerName.setError("You must enter a Name to continue");
                    ownerName.requestFocus();
                }
                else if(takenOwnerName.matches(".*\\\\d.*")){
                    ownerName.setError("Enter Valid Name");
                    ownerName.requestFocus();
                }
                else{
                    fourthLinearLayout.setVisibility(View.VISIBLE);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

            }
        });

        fourthContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String takenPersonalPhoneNumber = personalContactNumber.getText().toString().trim();
                String takenFutsalPhoneNumber = futsalContactNumber.getText().toString().trim();
                if(takenPersonalPhoneNumber.isEmpty()){
                    personalContactNumber.setError("You can't leave this empty.");
                    personalContactNumber.requestFocus();
                }
                else if(!takenPersonalPhoneNumber.matches("[0-9]+")){
                    personalContactNumber.setError("Enter valid phone Number");
                    personalContactNumber.requestFocus();
                }

                else if(takenFutsalPhoneNumber.isEmpty()){
                    futsalContactNumber.setError("You can't leave this empty.");
                    futsalContactNumber.requestFocus();
                }
                else if(!takenPersonalPhoneNumber.matches("[0-9]+")){
                    futsalContactNumber.setError("Enter valid phone Number");
                    futsalContactNumber.requestFocus();
                }
                else {
                    fifthLinearLayout.setVisibility(View.VISIBLE);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        });

        fifthContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String takenEmail = personalEmail.getText().toString().trim();
                if(takenEmail.isEmpty()){
                    personalEmail.setError("You can't leave this empty.");
                    personalEmail.requestFocus();
                }

                else {
                    sixthLinearLayout.setVisibility(View.VISIBLE);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                }
            }
        });

        sixthContinue.setOnClickListener(new View.OnClickListener() {
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
                    sixthContinue.setEnabled(false);
                    RegisterDetailHolder.keepDetails(ownerName.getText().toString());
                    RegisterDetailHolder.keepDetails(personalContactNumber.getText().toString());
                    RegisterDetailHolder.keepDetails(futsalContactNumber.getText().toString());
                    RegisterDetailHolder.keepDetails(personalEmail.getText().toString());
                    firebaseAuth = FirebaseAuth.getInstance();
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    String userId = user.getUid();
                    DatabaseReference currentUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(userId);
                    Map newUser = new HashMap();
                    LinkedList details = RegisterDetailHolder.getAllDetails();

                    newUser.put("FutsalName",details.get(0));
                    newUser.put("FutsalLocation",details.get(1));
                    newUser.put("OwnerName",details.get(2));
                    newUser.put("PersonalContact",details.get(3));
                    newUser.put("FutsalContact",details.get(4));
                    newUser.put("PersonalEmail",details.get(5));
                    newUser.put("FutsalEmail",user.getEmail());
                    newUser.put("Verified","false");
                    newUser.put("ShowUs","true");

                    currentUserDatabase.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(),"You Futsal been registered successfully.",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getContext(),"Cannot register.",Toast.LENGTH_LONG).show();
                                makeInvisible();
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
                                                        Intent newIntent = new Intent(getActivity(),FutsalMainActivity.class);
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
        sixthContinue.setEnabled(true);
    }

}
