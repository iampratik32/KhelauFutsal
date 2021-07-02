package com.example.khelaufutsal.Futsal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.khelaufutsal.DialogVerifyPassword;
import com.example.khelaufutsal.LoginActivity;
import com.example.khelaufutsal.Player.PlayerHolder;
import com.example.khelaufutsal.Player.Player_FutsalClassHolder;
import com.example.khelaufutsal.Player.Player_HistoryHolder;
import com.example.khelaufutsal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Futsal_SettingsFragment extends Fragment {

    private Switch showUs;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_futsal__settings, container, false);

         TextView showUsText = view.findViewById(R.id.showUsText_settingsPageFutsalActivity);
         showUsText.setText("Show "+ FutsalHolder.getFutsal().getFutsalName()+" to your potential futsal players. Check this if you want to be visible with the rest of the Futsal world.");

         ImageView backButton = view.findViewById(R.id.backButton_futsalSettingsPage);
         backButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getActivity().finish();
             }
         });

         showUs = view.findViewById(R.id.showUsSwitch_futsalSettingsActivity);
         if(FutsalHolder.getFutsal().getShowFutsal().equals("true")){
             showUs.setChecked(true);
         }
         else {
             showUs.setChecked(false);
         }

         final CardView verifyAccount = view.findViewById(R.id.verifyCardView_futsalSettingsActivity);
         CardView accountSettings = view.findViewById(R.id.accountSettingsCardView_futsalSettingsActivity);
         CardView locationSettings = view.findViewById(R.id.locationCardView_futsalSettingsActivity);
         CardView customizeFutsal = view.findViewById(R.id.customizeCardView_futsalSettingsActivity);
         CardView logOut = view.findViewById(R.id.logOutCardView_futsalSettingsActivity);

         logOut.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FirebaseAuth auth = FirebaseAuth.getInstance();
                 auth.signOut();
                 FutsalHolder.setFutsal(null);
                 PlayerHolder.setPlayer(null);
                 Player_FutsalClassHolder.setFutsal(null);
                 Intent intent = new Intent(getActivity(), LoginActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                         | Intent.FLAG_ACTIVITY_CLEAR_TOP
                         | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
                 getActivity().finish();
             }
         });

         TextView verifyText = view.findViewById(R.id.verifyText_SettingsFragment);
         TextView learnMoreText = view.findViewById(R.id.learnMoreText_SettingsFragment);

         if(FutsalHolder.getFutsal().getVerified().equals("true")){
            verifyText.setText("Verified");
            learnMoreText.setText("");
            verifyAccount.setEnabled(false);
            verifyAccount.setCardBackgroundColor(Color.DKGRAY);
         }

         verifyAccount.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Fragment newFragment = new Futsal_VerifyAccountFragment();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();
                 transaction.replace(R.id.frameLayout_FutsalSettings,newFragment);
                 transaction.addToBackStack(null);
                 transaction.commit();
             }
         });

         accountSettings.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(FutsalHolder.getFutsal().getCredential()==null){
                     DialogVerifyPassword verifyPassword = new DialogVerifyPassword(getContext());
                     verifyPassword.show(getFragmentManager(),"VerifyPassword");
                 }
                 else {
                     Fragment newFragment = new Futsal_AccountSettingsFragment();
                     FragmentTransaction transaction = getFragmentManager().beginTransaction();
                     transaction.replace(R.id.frameLayout_FutsalSettings,newFragment);
                     transaction.addToBackStack(null);
                     transaction.commit();
                 }
             }
         });

         locationSettings.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent location = new Intent(getActivity(),Futsal_MapsActivity.class);
                 startActivity(location);
             }
         });

         customizeFutsal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Fragment newFragment = new Futsal_CustomizeFutsalFragment();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();
                 transaction.replace(R.id.frameLayout_FutsalSettings,newFragment);
                 transaction.addToBackStack(null);
                 transaction.commit();
             }
         });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            DatabaseReference changeShowStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("ShowUs");
            if(showUs.isChecked() && FutsalHolder.getFutsal().getShowFutsal().equals("false")){
                changeShowStatus.setValue("true");
                FutsalHolder.getFutsal().setShowFutsal("true");
            }
            else if(!showUs.isChecked() && FutsalHolder.getFutsal().getShowFutsal().equals("true")){
                changeShowStatus.setValue("false");
                FutsalHolder.getFutsal().setShowFutsal("false");
            }
        }
        catch (NullPointerException e){

        }

    }
}
