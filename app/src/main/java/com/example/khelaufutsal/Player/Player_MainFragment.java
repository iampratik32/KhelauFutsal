package com.example.khelaufutsal.Player;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.DialogNotVerifiedFutsal;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Player_MainFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player__main, container, false);

        PlayerClass player = PlayerHolder.getPlayer();

        CircleImageView displayPicture = view.findViewById(R.id.displayPicture_playerMainFragment);
        TextView displayName = view.findViewById(R.id.displayName_playerMainFragment);

        displayName.setText(player.getDisplayName());
        if(PlayerHolder.getPlayer().getDisplayPicture()!=null){
            Glide.with(getContext()).load(Uri.parse(PlayerHolder.getPlayer().getDisplayPicture())).into(displayPicture);
        }


        CardView exploreBookings = view.findViewById(R.id.exploreBookings);
        CardView exploreGrounds = view.findViewById(R.id.exploreGrounds);

        final PlayersMainActivity ac = (PlayersMainActivity) getActivity();
        exploreGrounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.bottomNavigationView.setSelectedItemId(R.id.ground_PlayerDashboard);
            }
        });

        exploreBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.bottomNavigationView.setSelectedItemId(R.id.bookingItem_PlayerDashboard);
            }
        });

        return view;
    }


}
