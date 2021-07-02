package com.example.khelaufutsal.Futsal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.khelaufutsal.R;
import com.example.khelaufutsal.RegisterDetailHolder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.LinkedList;


public class FutsalEnterDetails_FirstFragment extends Fragment {
    RegisterDetailHolder registerDetailHolder;
    TextInputEditText locatoinTextField;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_futsal_enter_details__first, container, false);


        final LinearLayout secondLinearLayout = view.findViewById(R.id.secondLinearLayout_registerFutsal);
        secondLinearLayout.setVisibility(View.GONE);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);


        Button firstContinue = view.findViewById(R.id.firstContinueButton_forFutsal);
        Button chooseLocationButton = view.findViewById(R.id.chooseLocationButton_forFutsal);
        locatoinTextField = view.findViewById(R.id.registerFutsal_location);
        final TextInputEditText futsalNameTextField = view.findViewById(R.id.registerFutsal_futsalName);
        firstContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(secondLinearLayout.getVisibility()==View.GONE){
                    getActivity().getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    String takenFutsalName = futsalNameTextField.getText().toString().trim();
                    if(takenFutsalName.isEmpty()){
                        futsalNameTextField.setError("You must enter Futsal Name to continue");
                        futsalNameTextField.requestFocus();
                    }
                    else{
                        RegisterDetailHolder.setAllDetails(new LinkedList());
                        RegisterDetailHolder.keepDetails(futsalNameTextField.getText().toString());
                        secondLinearLayout.setVisibility(View.VISIBLE);
                        futsalNameTextField.clearFocus();
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                }
            }
        });

        Button secondContinue = view.findViewById(R.id.secondContinueButton_forFutsal);
        secondContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String takenLocation = locatoinTextField.getText().toString().trim();
                if(takenLocation.isEmpty()){
                    Toast.makeText(getContext(),"Choose Futsal Location to Continue.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(RegisterDetailHolder.getAllDetails().size()<2){
                        RegisterDetailHolder.keepDetails(futsalNameTextField.getText().toString());
                    }
                    Fragment newFragment = new FutsalEnterDetails_SecondFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.right_to_left, R.anim.exit_right);
                    transaction.replace(R.id.firstLoginFrameLayout,newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        chooseLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent location = new Intent(getActivity(),Futsal_MapsActivity.class);
                startActivityForResult(location, 5574);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 5574:{
                if(resultCode==Activity.RESULT_OK){
                    String locationText = data.getStringExtra("5575");
                    String[] latlong =  locationText.split(",");
                    String ans = ChangeCoordinates.changeToText(latlong,getContext());
                    locatoinTextField.setText(ans);
                }
                break;
            }
        }
    }
}
