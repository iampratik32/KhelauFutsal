package com.example.khelaufutsal.Player;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.ChooseMediaType;
import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class Player_PersonalInformationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_personal_information, container, false);

        ImageView backButton = view.findViewById(R.id.backButton_personalInfo);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        CircleImageView displayPicture = view.findViewById(R.id.displayPicture_playerPersonalInfo);
        if(PlayerHolder.getPlayer().getDisplayPicture()!=null){
            Glide.with(getActivity()).load(Uri.parse(PlayerHolder.getPlayer().getDisplayPicture())).into(displayPicture);
        }
        else {
            displayPicture.setImageResource(R.drawable.football_player_icon);
        }
        displayPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChooseMediaType.class));
            }
        });

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId());

        final TextView playerName = view.findViewById(R.id.playerName_accountSettingsFragment);
        playerName.setText(PlayerHolder.getPlayer().getUserName());

        final TextView displayName = view.findViewById(R.id.displayName_accountSettingsFragment);
        displayName.setText(PlayerHolder.getPlayer().getDisplayName());

        final TextView phoneNumber = view.findViewById(R.id.phoneNumber_accountSettingsFragment);
        phoneNumber.setText(PlayerHolder.getPlayer().getContactNumber());

        final LinearLayout playerNameLayout = view.findViewById(R.id.toHide_enterPlayerNameAccountSettings);
        final LinearLayout displayNameLayout = view.findViewById(R.id.toHide_enterDisplayNameAccountSettings);
        final LinearLayout phoneNumberNameLayout = view.findViewById(R.id.toHide_enterPhoneNumberAccountSettings);
        playerNameLayout.setVisibility(View.GONE);
        displayNameLayout.setVisibility(View.GONE);
        phoneNumberNameLayout.setVisibility(View.GONE);

        final TextInputEditText newPlayerName = view.findViewById(R.id.enterNewPlayerName_AccountSettings);
        final TextInputEditText newDisplayName = view.findViewById(R.id.enterNewDisplayName_AccountSettings);
        final TextInputEditText newPhoneNumber = view.findViewById(R.id.enterNewPhoneNumber_AccountSettings);

        Button changePlayerNameButton = view.findViewById(R.id.newPlayerNameDoneButton_AccountSettings);
        Button changeDisplayNameButton = view.findViewById(R.id.newDisplayNameDoneButton_AccountSettings);
        Button changePhoneNumberButton = view.findViewById(R.id.newPhoneNumberDoneButton_AccountSettings);

        CardView playerDisplayNameCardView = view.findViewById(R.id.displayNameCardView_AccountSettings);
        playerDisplayNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(displayNameLayout.getVisibility()==View.GONE){
                    displayNameLayout.setVisibility(View.VISIBLE);
                    newDisplayName.requestFocus();
                }
                else {
                    displayNameLayout.setVisibility(View.GONE);
                }
            }
        });
        CardView playerNameCardView = view.findViewById(R.id.playerNameCardView_AccountSettings);
        playerNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerNameLayout.getVisibility()==View.GONE){
                    playerNameLayout.setVisibility(View.VISIBLE);
                    newPlayerName.requestFocus();
                }
                else {
                    playerNameLayout.setVisibility(View.GONE);
                }
            }
        });
        CardView phoneNumberCardView = view.findViewById(R.id.phoneNumberCardView_AccountSettings);
        phoneNumberCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneNumberNameLayout.getVisibility()==View.GONE){
                    phoneNumberNameLayout.setVisibility(View.VISIBLE);
                    newPhoneNumber.requestFocus();
                }
                else {
                    phoneNumberNameLayout.setVisibility(View.GONE);
                }
            }
        });

        changePlayerNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            databaseReference.child("Name").setValue(newPlayerName.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        PlayerHolder.getPlayer().setUserName(newPlayerName.getText().toString().trim());
                                        playerName.setText(newPlayerName.getText().toString().trim());
                                        newPlayerName.setText("");
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                playerNameLayout.setVisibility(View.GONE);
            }
        });

        changeDisplayNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            databaseReference.child("DisplayName").setValue(newDisplayName.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        PlayerHolder.getPlayer().setDisplayName(newDisplayName.getText().toString().trim());
                                        displayName.setText(newDisplayName.getText().toString().trim());
                                        newDisplayName.setText("");
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                displayNameLayout.setVisibility(View.GONE);
            }
        });

        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            databaseReference.child("ContactNumber").setValue(newPhoneNumber.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        PlayerHolder.getPlayer().setContactNumber(newPhoneNumber.getText().toString().trim());
                                        phoneNumber.setText(newPhoneNumber.getText().toString().trim());
                                        newPhoneNumber.setText("");
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                phoneNumberNameLayout.setVisibility(View.GONE);
            }
        });

        return view;
    }


}
