package com.example.khelaufutsal.Player;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.khelaufutsal.DialogVerifyPassword;
import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.Futsal.Futsal_AccountSettingsFragment;
import com.example.khelaufutsal.LoginActivity;
import com.example.khelaufutsal.R;
import com.google.firebase.auth.FirebaseAuth;


public class PlayerSettings_Fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player_settings_, container, false);

        ImageView backButton = view.findViewById(R.id.backButton_playerSettingsPage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        CardView logOut = view.findViewById(R.id.logOutCardView_playerSettingsActivity);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                FutsalHolder.setFutsal(null);
                PlayerHolder.setPlayer(null);
                Player_FutsalClassHolder.setFutsal(null);
                PlayerHolder.setPlayer(null);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        CardView accountSettings = view.findViewById(R.id.accountSettingsCardView_playerSettingsActivity);
        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlayerHolder.getPlayer().getCredential()==null){
                    DialogVerifyPassword verifyPassword = new DialogVerifyPassword(getContext());
                    verifyPassword.show(getFragmentManager(),"VerifyPassword");
                }
                else {
                    Fragment newFragment = new Player_AccountSettingsFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout_PlayerSettings,newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        CardView changePersonalInfo = view.findViewById(R.id.changePersonalInformation_playerSettings);
        changePersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new Player_PersonalInformationFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_PlayerSettings,newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }


}
