package com.example.khelaufutsal.Player;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.khelaufutsal.R;
import com.example.khelaufutsal.RegisterDetailHolder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.LinkedList;


public class PlayerEnterDetails_FirstFragment extends Fragment {
    RegisterDetailHolder registerDetailHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player_enter_details__first, container, false);


        final LinearLayout secondLinearLayout = view.findViewById(R.id.secondLinearLayout_registerPlayer);
        secondLinearLayout.setVisibility(View.GONE);

        Button firstContinue = view.findViewById(R.id.firstContinueButton_forPlayers);
        final TextInputEditText fullNameTextfield = view.findViewById(R.id.registerPlayer_fullName);
        firstContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                String takenName = fullNameTextfield.getText().toString().trim();
                if(takenName.isEmpty()){
                    fullNameTextfield.setError("You must enter your name to continue");
                    fullNameTextfield.requestFocus();
                }
                else if(takenName.matches(".*\\\\d.*")){
                    fullNameTextfield.setError("Enter Valid Name");
                    fullNameTextfield.requestFocus();
                }
                else{
                    secondLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        Button secondContinue = view.findViewById(R.id.secondContinueButton_forPlayers);
        final TextInputEditText displayName = view.findViewById(R.id.registerPlayer_displayName);
        secondContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                String takenDisplayName = displayName.getText().toString().trim();
                if(takenDisplayName.isEmpty()){
                    displayName.setError("You can't leave this empty.");
                    displayName.requestFocus();
                }
                else{
                    if(RegisterDetailHolder.getAllDetails()!=null){
                        LinkedList tempLinkedList = new LinkedList();
                        RegisterDetailHolder.setAllDetails(tempLinkedList);
                    }
                    RegisterDetailHolder.keepDetails(fullNameTextfield.getText().toString());
                    RegisterDetailHolder.keepDetails(displayName.getText().toString());

                    Fragment newFragment = new PlayerEnterDetails_SecondFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.right_to_left, R.anim.exit_right);
                    transaction.replace(R.id.firstLoginFrameLayout,newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });


        return view;
    }

}
