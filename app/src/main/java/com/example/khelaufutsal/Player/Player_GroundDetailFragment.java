package com.example.khelaufutsal.Player;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khelaufutsal.Player.Player_BookingFragment;
import com.example.khelaufutsal.Player.Player_FutsalClass;
import com.example.khelaufutsal.Player.Player_FutsalClassHolder;
import com.example.khelaufutsal.Player.Player_GroundDetailAdapter;
import com.example.khelaufutsal.Player_GroundBookingFragment;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class Player_GroundDetailFragment extends Fragment {
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    View view;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> futsalAttractions = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_player__ground_detail, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_futsalBookingPage);

        final Player_FutsalClass futsal = Player_FutsalClassHolder.getFutsal();

        TextView futsalMainName = view.findViewById(R.id.futsalMainName_bookFutsalPage);
        futsalMainName.setText(futsal.getFutsalName());

        TextView futsalPrice = view.findViewById(R.id.futsalPrice_groundDetail);
        futsalPrice.setText(futsal.getFutsalPrice());

        TextView futsalLocationOnPage = view.findViewById(R.id.futsalLocation_bookFutsalPage);
        futsalLocationOnPage.setText(futsal.getFutsalLocation());

        CardView callFutsal = view.findViewById(R.id.phoneNumberCard_futsalBookingPage);
        callFutsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+futsal.getFutsalContact()));
                startActivity(intent);
            }
        });

        CardView mailFutsal = view.findViewById(R.id.emailCard_futsalBookingPage);
        mailFutsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        TextView phoneNumber = view.findViewById(R.id.phoneNumber_futsalBookingPage);
        phoneNumber.setText(futsal.getFutsalContact());

        TextView email = view.findViewById(R.id.email_futsalBookingPage);
        email.setText(futsal.getFutsalEmail());

        final ViewPager viewPager = view.findViewById(R.id.viewPager_playerFutsalDetail);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(futsal.getFutsalId());
        readData(databaseReference.child("Images"), new onGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0){
                    viewPager.setVisibility(View.VISIBLE);
                    Player_GroundDetailAdapter player_groundDetailAdapter = new Player_GroundDetailAdapter(getContext(),(int) dataSnapshot.getChildrenCount(),futsal.getFutsalId());
                    viewPager.setAdapter(player_groundDetailAdapter);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });

        Button bookButton = view.findViewById(R.id.bookButton_bookFutsalPage);
        progressBar = view.findViewById(R.id.progressBar_playerGroundDetail);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadFutsalDetailAsync loadFutsalDetailAsync = new LoadFutsalDetailAsync();
                loadFutsalDetailAsync.execute();
            }
        });

        Button viewOnMap = view.findViewById(R.id.viewOnMap_bookFutsalPage);
        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                String[] latlong =  futsal.getFutsalLatLan().split(",");
                double latitude = Double.parseDouble(latlong[0]);
                double longitude = Double.parseDouble(latlong[1]);

                String label = futsal.getFutsalName();
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return view;
    }

    protected void sendEmail() {

        String[] TO = {Player_FutsalClassHolder.getFutsal().getFutsalEmail()};
        String[] CC = {Player_FutsalClassHolder.getFutsal().getFutsalEmail()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail"));
            getActivity().finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(),"There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public interface onGetDataListener{
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }

    public void readData(DatabaseReference databaseReference, final onGetDataListener listener){
        listener.onStart();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    private class LoadFutsalDetailAsync extends AsyncTask<Integer,Integer,Integer>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Fragment firstDetail = new Player_GroundBookingFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.right_to_left,R.anim.exit_right,R.anim.left_to_right,R.anim.exit_left)
                    .replace(R.id.frameLayout_Player_FutsalGroundDetail,firstDetail).addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {

            Player_FutsalClass futsalClass = Player_FutsalClassHolder.getFutsal();

            DatabaseReference futsalDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(futsalClass.getFutsalId());
            futsalDatabase.child("Timing").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String openTime = dataSnapshot.child("OpeningTime").getValue().toString();
                        String futsalOpeningTime = "";
                        for(int i=0;i<openTime.length();i++){
                            if(String.valueOf(openTime.charAt(i)).equals(":")){
                                break;
                            }
                            futsalOpeningTime = futsalOpeningTime + openTime.charAt(i);
                        }
                        String closeTime = dataSnapshot.child("ClosingTime").getValue().toString();
                        String futsalClosingTime = "";
                        for(int i=0; i<closeTime.length();i++){
                            if(String.valueOf(closeTime.charAt(i)).equals(":")){
                                break;
                            }
                            futsalClosingTime = futsalClosingTime + closeTime.charAt(i);
                        }
                        int openingTime = Integer.parseInt(futsalOpeningTime);
                        int closingTime = Integer.parseInt(futsalClosingTime);
                        LinkedList<String> tempLinkedList = new LinkedList<>();
                        for(int i =openingTime; i<=closingTime;i++){
                            tempLinkedList.add(i+":00");
                        }
                        Player_FutsalClassHolder.getFutsal().setFutsalTiming(tempLinkedList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final LinkedList tempOpenDays = new LinkedList();
            futsalDatabase.child("Timing").child("OpenDays").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                            tempOpenDays.add(postSnapshot.getKey());
                        }
                    }
                    Player_FutsalClassHolder.getFutsal().setFutsalOpenDays(tempOpenDays);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            for (int i = 0; i < 1; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }

            return null;
        }
    }

}
