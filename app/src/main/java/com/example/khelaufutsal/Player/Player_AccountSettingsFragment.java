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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.ChooseMediaType;
import com.example.khelaufutsal.DialogChangePassword;
import com.example.khelaufutsal.EmailValidator;
import com.example.khelaufutsal.Futsal.DialogDeleteFutsalAccount;
import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Player_AccountSettingsFragment extends Fragment {
    ProgressBar playerEmailProgressBar;
    Button changePlayerEmailButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player__account_settings, container, false);


        CircleImageView displayPicture = view.findViewById(R.id.displayPicture_playerAccountSettings);
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

        final TextView futsalEmail = view.findViewById(R.id.userEmail_accountSettingsFragment);
        futsalEmail.setText(PlayerHolder.getPlayer().getUserEmail());

        final LinearLayout playerEmailLayout = view.findViewById(R.id.toHide_enterUserEmailAccountSettings);
        playerEmailLayout.setVisibility(View.GONE);

        final TextInputEditText newPlayerEmail = view.findViewById(R.id.enterUserEmail_AccountSettings);
        changePlayerEmailButton = view.findViewById(R.id.newUserEmailDoneButton_AccountSettings);
        CardView playerEmailCardView = view.findViewById(R.id.userEmailCardView_AccountSettings);
        playerEmailCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerEmailLayout.getVisibility() == View.GONE){
                    playerEmailLayout.setVisibility(View.VISIBLE);
                    newPlayerEmail.requestFocus();
                }
                else {
                    playerEmailLayout.setVisibility(View.GONE);
                }
            }
        });
        playerEmailProgressBar = view.findViewById(R.id.progressBar_PlayerEmailAccountSettings);

        changePlayerEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EmailValidator.emailValidator(newPlayerEmail.getText().toString().trim())){
                    newPlayerEmail.setError("Enter Valid Email");
                    newPlayerEmail.requestFocus();
                    return;
                }
                playerEmailProgressBar.setVisibility(View.VISIBLE);
                changePlayerEmailButton.setVisibility(View.GONE);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.reauthenticate(PlayerHolder.getPlayer().getCredential()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updateEmail(newPlayerEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        databaseReference.child("EmailAddress").setValue(newPlayerEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    playerEmailLayout.setVisibility(View.GONE);
                                                    playerEmailProgressBar.setVisibility(View.GONE);
                                                    changePlayerEmailButton.setVisibility(View.VISIBLE);
                                                    PlayerHolder.getPlayer().setUserEmail(newPlayerEmail.getText().toString().trim());
                                                    futsalEmail.setText(newPlayerEmail.getText().toString().trim());
                                                    Toast.makeText(getContext(),"Email Change Successfully",Toast.LENGTH_LONG).show();
                                                    PlayerHolder.getPlayer().setCredential(null);
                                                    backPressed();
                                                }
                                                else {
                                                    changeEmailLayout();
                                                    Toast.makeText(getContext(),"Cant Update Database. Try Again.",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        changeEmailLayout();
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
                            changeEmailLayout();
                        }
                    }
                });
            }
        });

        CardView changePasswordCardView = view.findViewById(R.id.changePasswordCardView_playerAccountSettings);
        changePasswordCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChangePassword dialogChangePassword = new DialogChangePassword(getContext());
                dialogChangePassword.show(getFragmentManager(),"ChangePassword");
            }
        });

        CardView deleteAccountCardView = view.findViewById(R.id.deleteAccountCardView_playerSettingsActivity);
        deleteAccountCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDeletePlayerAccount deletePlayerAccount = new DialogDeletePlayerAccount(getContext());
                deletePlayerAccount.show(getFragmentManager(),"DeleteAccount");
            }
        });

        ImageView backButton = view.findViewById(R.id.backButton_playerAccountSettingsPage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });

        return view;
    }

    private void backPressed(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStackImmediate();
        }
    }

    private void changeEmailLayout(){
        Toast.makeText(getContext(),"Cant Validate At This Moment.",Toast.LENGTH_LONG).show();
        playerEmailProgressBar.setVisibility(View.GONE);
        changePlayerEmailButton.setVisibility(View.VISIBLE);
    }

}
