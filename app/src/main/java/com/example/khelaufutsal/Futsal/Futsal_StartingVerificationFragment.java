package com.example.khelaufutsal.Futsal;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khelaufutsal.DialogVerifyPassword;
import com.example.khelaufutsal.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Futsal_StartingVerificationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_futsal__starting_verification, container, false);

        ImageView backButton = view.findViewById(R.id.backButton_futsalFinalVerificationPage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        final Button sendVerificationButtonFinal = view.findViewById(R.id.sendVerificationButton_finalVerification);

        TextView futsalName = view.findViewById(R.id.futsalName_endingVerification);
        futsalName.setText(FutsalHolder.getFutsal().getFutsalName());

        TextView futsalEmail = view.findViewById(R.id.futsalEmail_endingVerification);
        futsalEmail.setText(FutsalHolder.getFutsal().getFutsalEmail());

        TextView ownerName = view.findViewById(R.id.futsalOwnerName_endingVerification);
        ownerName.setText(FutsalHolder.getFutsal().getOwnerName());

        TextView futsalContact = view.findViewById(R.id.futsalContact_endingVerification);
        futsalContact.setText(FutsalHolder.getFutsal().getFutsalContact());

        TextView futsalLocation = view.findViewById(R.id.futsalLocation_endingVerification);
        String[] latlong =  FutsalHolder.getFutsal().getFutsalLocation().split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            futsalLocation.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView personalEmail = view.findViewById(R.id.personalEmail_endingVerification);
        personalEmail.setText(FutsalHolder.getFutsal().getPersonalEmail());

        TextView personalContactNumber = view.findViewById(R.id.personalContact_endingVerification);
        personalContactNumber.setText(FutsalHolder.getFutsal().getPersonalContact());

        Button changeFutsalInformation = view.findViewById(R.id.changeFutsalInformation_endingVerification);
        changeFutsalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FutsalHolder.getFutsal().getCredential()==null){
                    DialogVerifyPassword dialogVerifyPassword = new DialogVerifyPassword(getContext());
                    dialogVerifyPassword.show(getFragmentManager(),"VerifyPassword");
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

        Button changeLocation = view.findViewById(R.id.changeLocation_endingVerification);
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent location = new Intent(getActivity(),Futsal_MapsActivity.class);
                startActivity(location);
            }
        });

        Button changePersonalInformation = view.findViewById(R.id.changePersonalInfo_endingVerification);
        changePersonalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FutsalHolder.getFutsal().getCredential()==null){
                    DialogVerifyPassword dialogVerifyPassword = new DialogVerifyPassword(getContext());
                    dialogVerifyPassword.show(getFragmentManager(),"VerifyPassword");
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

        Button addAdditionalInformation = view.findViewById(R.id.changeCustomize_endingVerification);
        addAdditionalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new Futsal_CustomizeFutsalFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_FutsalSettings,newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        sendVerificationButtonFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FutsalHolder.getFutsal().getVerificationRequest()!=null) {
                    if (FutsalHolder.getFutsal().getVerificationRequest().equals("true")) {
                        Toast.makeText(getContext(),"Your new request is not sent. If rejected you will be able to send a new request again.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                final DatabaseReference verifyReq = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("VerificationRequest");
                verifyReq.setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),"Your request has been sent out.",Toast.LENGTH_LONG).show();
                            FutsalHolder.getFutsal().setVerificationRequest("true");
                        }
                    }
                });
            }
        });

        return view;
    }




}
