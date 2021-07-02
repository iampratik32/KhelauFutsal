package com.example.khelaufutsal.Futsal;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.khelaufutsal.R;


public class Futsal_VerifyAccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_futsal__verify_account, container, false);

        CardView getStared = view.findViewById(R.id.getStartedCardView_verifyFutsalFragment);
        getStared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new Futsal_StartingVerificationFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_FutsalSettings,newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ImageView backButton = view.findViewById(R.id.backButton_verifyFragmentFutsalPage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        return view;
    }

}
