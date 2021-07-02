package com.example.khelaufutsal.Futsal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.ChooseMediaType;
import com.example.khelaufutsal.DialogChangePassword;
import com.example.khelaufutsal.EmailValidator;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class Futsal_AccountSettingsFragment extends Fragment {
    ProgressBar futsalEmailProgressBar;
    Button changeFutsalEmailButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_futsal__account_setttings, container, false);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId());

        final TextView futsalName = view.findViewById(R.id.futsalName_accountSettingsFragment);
        futsalName.setText(FutsalHolder.getFutsal().getFutsalName());

        final TextView ownerName = view.findViewById(R.id.ownerName_accountSettingsFragment);
        ownerName.setText(FutsalHolder.getFutsal().getOwnerName());

        final TextView futsalEmail = view.findViewById(R.id.futsalEmail_accountSettingsFragment);
        futsalEmail.setText(FutsalHolder.getFutsal().getFutsalEmail());

        final TextView futsalContactNumber = view.findViewById(R.id.futsalContactNumber_accountSettingsFragment);
        futsalContactNumber.setText(FutsalHolder.getFutsal().getFutsalContact());

        final TextView personalContact = view.findViewById(R.id.personalContact_accountSettingsFragment);
        personalContact.setText(FutsalHolder.getFutsal().getPersonalContact());

        final TextView personalEmail = view.findViewById(R.id.personalEmail_accountSettingsFragment);
        personalEmail.setText(FutsalHolder.getFutsal().getPersonalEmail());

        ImageView backButton = view.findViewById(R.id.backButton_futsalAccountSettingsPage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });

        final LinearLayout futsalNameLayout = view.findViewById(R.id.toHide_enterFutsalNameAccountSettings);
        final LinearLayout futsalOwnerNameLayout = view.findViewById(R.id.toHide_enterOwnerNameAccountSettings);
        final LinearLayout futsalEmailLayout = view.findViewById(R.id.toHide_enterFutsalEmailAccountSettings);
        final LinearLayout futsalContactNumberLayout = view.findViewById(R.id.toHide_enterFutsalContactNumberAccountSettings);
        final LinearLayout personalContactLayout = view.findViewById(R.id.toHide_enterPersonalContactAccountSettings);
        final LinearLayout personalEmailLayout = view.findViewById(R.id.toHide_enterPersonalEmailAccountSettings);
        futsalNameLayout.setVisibility(View.GONE);
        futsalOwnerNameLayout.setVisibility(View.GONE);
        futsalEmailLayout.setVisibility(View.GONE);
        futsalContactNumberLayout.setVisibility(View.GONE);
        personalContactLayout.setVisibility(View.GONE);
        personalEmailLayout.setVisibility(View.GONE);

        final TextInputEditText newFutsalName = view.findViewById(R.id.enterNewFutsalName_AccountSettings);
        final TextInputEditText newOwnerName = view.findViewById(R.id.enterNewOwnerName_AccountSettings);
        final TextInputEditText newFutsalEmail = view.findViewById(R.id.enterFutsalEmail_AccountSettings);
        final TextInputEditText newFutsalContactNumber = view.findViewById(R.id.enterNewFutsalContactNumber_AccountSettings);
        final TextInputEditText newPersonalContactNumber = view.findViewById(R.id.enterNewPersonalContact_AccountSettings);
        final TextInputEditText newPersonalEmail = view.findViewById(R.id.enterNewPersonalEmail_AccountSettings);


        Button changeFutsalNameButton = view.findViewById(R.id.newFutsalNameDoneButton_AccountSettings);
        Button changeFutsalOwnerNameButton = view.findViewById(R.id.newFutsalOwnerNameDoneButton_AccountSettings);
        changeFutsalEmailButton = view.findViewById(R.id.newFutsalEmailDoneButton_AccountSettings);
        Button changeFutsalContactNumberButton = view.findViewById(R.id.newContactNumberNameDoneButton_AccountSettings);
        Button changePersonalContactNumberButton = view.findViewById(R.id.newPersonalContactDoneButton_AccountSettings);
        Button changePersonalEmailButton = view.findViewById(R.id.newPersonalEmailDoneButton_AccountSettings);

        CardView futsalOwnerNameCardView = view.findViewById(R.id.futsalOwnerNameCardView_AccountSettings);
        futsalOwnerNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(futsalOwnerNameLayout.getVisibility()==View.GONE){
                    futsalOwnerNameLayout.setVisibility(View.VISIBLE);
                    newOwnerName.requestFocus();
                }
                else {
                    futsalOwnerNameLayout.setVisibility(View.GONE);
                }
            }
        });
        CardView futsalNameCardView = view.findViewById(R.id.futsalNameCardView_AccountSettings);
        futsalNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(futsalNameLayout.getVisibility()==View.GONE){
                    futsalNameLayout.setVisibility(View.VISIBLE);
                    newFutsalName.requestFocus();
                }
                else {
                    futsalNameLayout.setVisibility(View.GONE);
                }
            }
        });
        CardView futsalEmailCardView = view.findViewById(R.id.futsalEmailCardView_AccountSettings);
        futsalEmailCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(futsalEmailLayout.getVisibility() == View.GONE){
                    futsalEmailLayout.setVisibility(View.VISIBLE);
                    newFutsalEmail.requestFocus();
                }
                else {
                    futsalEmailLayout.setVisibility(View.GONE);
                }
            }
        });
        CardView futsalContactCardView = view.findViewById(R.id.futsalContactNumberCardView_AccountSettings);
        futsalContactCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(futsalContactNumberLayout.getVisibility() == View.GONE){
                    futsalContactNumberLayout.setVisibility(View.VISIBLE);
                    newFutsalContactNumber.requestFocus();
                }
                else {
                    futsalContactNumberLayout.setVisibility(View.GONE);
                }
            }
        });

        CardView personalContactCardView = view.findViewById(R.id.personalContactCardView_AccountSettings);
        personalContactCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(personalContactLayout.getVisibility() == View.GONE){
                    personalContactLayout.setVisibility(View.VISIBLE);
                    newPersonalContactNumber.requestFocus();
                }
                else {
                    personalContactLayout.setVisibility(View.GONE);
                }
            }
        });

        CardView personalEmailCardView = view.findViewById(R.id.personalEmailCardView_AccountSettings);
        personalEmailCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(personalEmailLayout.getVisibility() == View.GONE){
                    personalEmailLayout.setVisibility(View.VISIBLE);
                    newPersonalEmail.requestFocus();
                }
                else {
                    personalEmailLayout.setVisibility(View.GONE);
                }
            }
        });

        changeFutsalNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            databaseReference.child("FutsalName").setValue(newFutsalName.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FutsalHolder.getFutsal().setFutsalName(newFutsalName.getText().toString().trim());
                                        futsalName.setText(newFutsalName.getText().toString().trim());
                                        newFutsalName.setText("");
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                futsalNameLayout.setVisibility(View.GONE);
            }
        });

        changeFutsalOwnerNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            databaseReference.child("OwnerName").setValue(newOwnerName.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FutsalHolder.getFutsal().setOwnerName(newOwnerName.getText().toString().trim());
                                        ownerName.setText(newOwnerName.getText().toString().trim());
                                        newOwnerName.setText("");
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                futsalOwnerNameLayout.setVisibility(View.GONE);
            }
        });

        futsalEmailProgressBar = view.findViewById(R.id.progressBar_emailAccountSettings);

        changeFutsalEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EmailValidator.emailValidator(newFutsalEmail.getText().toString().trim())){
                    newFutsalEmail.setError("Enter Valid Email");
                    newFutsalEmail.requestFocus();
                    return;
                }
                futsalEmailProgressBar.setVisibility(View.VISIBLE);
                changeFutsalEmailButton.setVisibility(View.GONE);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        databaseReference.child("FutsalEmail").setValue(newFutsalEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.reauthenticate(FutsalHolder.getFutsal().getCredential()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            user.updateEmail(newFutsalEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    futsalEmailLayout.setVisibility(View.GONE);
                                                    futsalEmailProgressBar.setVisibility(View.GONE);
                                                    changeFutsalEmailButton.setVisibility(View.VISIBLE);
                                                    if(task.isSuccessful()){
                                                        FutsalHolder.getFutsal().setFutsalEmail(newFutsalEmail.getText().toString().trim());
                                                        futsalEmail.setText(newFutsalEmail.getText().toString().trim());
                                                        Toast.makeText(getContext(),"Email Change Successfully",Toast.LENGTH_LONG).show();
                                                        FutsalHolder.getFutsal().setCredential(null);
                                                        backPressed();
                                                    }
                                                    else {
                                                        changeEmailButtons();
                                                        try {
                                                            throw task.getException();
                                                        }
                                                        catch (FirebaseAuthUserCollisionException e){
                                                            Toast.makeText(getContext(),"This email is already taken.",Toast.LENGTH_LONG).show();
                                                        }
                                                        catch (Exception e){
                                                            Toast.makeText(getContext(),"Cant Update Your Email At This Moment.",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                        else {
                                            changeEmailButtons();
                                        }
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        changeFutsalContactNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            databaseReference.child("FutsalContact").setValue(newFutsalContactNumber.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FutsalHolder.getFutsal().setFutsalContact(newFutsalContactNumber.getText().toString().trim());
                                        futsalContactNumber.setText(newFutsalContactNumber.getText().toString().trim());
                                        newFutsalContactNumber.setText("");
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                futsalContactNumberLayout.setVisibility(View.GONE);
            }
        });

        changePersonalContactNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            databaseReference.child("PersonalContact").setValue(newPersonalContactNumber.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FutsalHolder.getFutsal().setPersonalContact(newPersonalContactNumber.getText().toString().trim());
                                        personalContact.setText(newPersonalContactNumber.getText().toString().trim());
                                        newPersonalContactNumber.setText("");
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                personalContactLayout.setVisibility(View.GONE);
            }
        });

        changePersonalEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EmailValidator.emailValidator(newPersonalEmail.getText().toString().trim())){
                    newPersonalEmail.setError("Enter Valid Email");
                    newPersonalEmail.requestFocus();
                    return;
                }
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            databaseReference.child("PersonalEmail").setValue(newPersonalEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FutsalHolder.getFutsal().setPersonalEmail(newPersonalEmail.getText().toString().trim());
                                        personalEmail.setText(newPersonalEmail.getText().toString().trim());
                                        newPersonalEmail.setText("");
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                personalEmailLayout.setVisibility(View.GONE);
            }
        });

        CircleImageView displayPicture = view.findViewById(R.id.displayPicture_accountSettings);
        if(FutsalHolder.getFutsal().getDisplayPicture()!=null){
            Glide.with(getActivity()).load(Uri.parse(FutsalHolder.getFutsal().getDisplayPicture())).into(displayPicture);
        }
        else {
            displayPicture.setImageResource(R.drawable.futsal_icon);
        }
        displayPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChooseMediaType.class));
            }
        });

        CardView changePasswordCardView = view.findViewById(R.id.changePasswordCardView_futsalAccountSettings);
        changePasswordCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChangePassword dialogChangePassword = new DialogChangePassword(getContext());
                dialogChangePassword.show(getFragmentManager(),"ChangePassword");
            }
        });

        CardView deleteAccountCardView = view.findViewById(R.id.deleteAccountCardView_futsalSettingsActivity);
        deleteAccountCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDeleteFutsalAccount deleteFutsalAccount = new DialogDeleteFutsalAccount(getContext());
                deleteFutsalAccount.show(getFragmentManager(),"DeleteAccount");
            }
        });

        return view;
    }

    private void backPressed(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStackImmediate();
        }
    }

    private void changeEmailButtons(){
        Toast.makeText(getContext(),"Cant Validate At This Moment.",Toast.LENGTH_LONG).show();
        futsalEmailProgressBar.setVisibility(View.GONE);
        changeFutsalEmailButton.setVisibility(View.VISIBLE);
    }

}
