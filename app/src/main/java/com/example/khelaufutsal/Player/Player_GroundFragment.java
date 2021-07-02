package com.example.khelaufutsal.Player;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khelaufutsal.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Player_GroundFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView futsalGroundRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter anotherAdapter;
    CountDownTimer countDownTimer;
    EditText searchField;
    CountDownTimer countDownTimer2;
    ProgressBar progressBar;
    private ArrayList<Player_FutsalClass> futsalGrounds = new ArrayList<>();
    private ArrayList<Player_FutsalClass> groundsSearching = new ArrayList<>();
    private ArrayList<Player_FutsalClass> secondaryGrounds = new ArrayList<>();
    private int currentSize=0;
    boolean isLoading = false;
    int forDupCounter=0;
    DatabaseReference futsalDatabase;
    private int totalShowingFutsal;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_player__ground, container, false);

        futsalDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal");
        searchField = view.findViewById(R.id.futsalSearchField);
        futsalGroundRecyclerView = view.findViewById(R.id.searchFutsalRecyclerView);

        progressBar = view.findViewById(R.id.progressBar_futsalGroundFragment);
        layoutManager = new LinearLayoutManager(getContext());
        futsalGroundRecyclerView.setLayoutManager(layoutManager);
        adapter = new FutsalGroundAdapter(getMatches(), getContext());
        futsalGroundRecyclerView.setAdapter(adapter);

        fillData();

        scrollListener();

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0 || s.toString().equals("")){
                    futsalGroundRecyclerView.setAdapter(adapter);
                }
                else {
                    groundsSearching.clear();
                    anotherAdapter = new FutsalGroundAdapter(groundsSearching,getContext());
                    searchFutsals(s.toString());
                }
            }
        });

        return view;
    }

    private void searchFutsals(String toString) {
        Query query = futsalDatabase.orderByChild("FutsalName").startAt(toString).endAt(toString+"\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot post:dataSnapshot.getChildren()){
                    if(post.child("ShowUs").getValue().toString().equals("true") && post.child("Verified").getValue().toString().equals("true")){
                        if(!post.child("BlockList").child(PlayerHolder.getPlayer().getUserId()).exists()){
                            final Player_FutsalClass futsalClass = new Player_FutsalClass(post.child("FutsalContact").getValue().toString(),post.child("PersonalEmail").getValue().toString()
                                    ,post.child("FutsalName").getValue().toString(),post.child("OwnerName").getValue().toString(),post.child("FutsalLocation").getValue().toString(),post.getKey()
                                    ,post.child("Verified").getValue().toString());
                            if(post.child("DisplayPicture").exists()){
                                futsalClass.setFutsalDisplayImageLink(post.child("DisplayPicture").getValue().toString());
                            }
                            if(post.child("Ratings").exists()){
                                float totalRating = Float.parseFloat(post.child("Ratings").child("Rating").getValue().toString());
                                int totalUsers = Integer.parseInt(post.child("Ratings").child("TotalUsers").getValue().toString());
                                futsalClass.setFutsalRating(totalRating/totalUsers);
                            }
                            if(post.child("Price").exists()){
                                futsalClass.setFutsalPrice(post.child("Price").getValue().toString());
                            }
                            groundsSearching.add(futsalClass);
                        }
                    }
                }
                anotherAdapter = new FutsalGroundAdapter(groundsSearching,getContext());
                futsalGroundRecyclerView.setAdapter(anotherAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void scrollListener() {
        futsalGroundRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) futsalGroundRecyclerView.getLayoutManager();

                if(!isLoading){
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == futsalGrounds.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {

        futsalGrounds.add(null);
        adapter.notifyItemInserted(futsalGrounds.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                futsalGrounds.remove(futsalGrounds.size() - 1);
                int scrollPosition = futsalGrounds.size();
                adapter.notifyItemRemoved(scrollPosition);
                currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                readMoreData(nextLimit, new onGetDataListener() {
                    @Override
                    public void onSuccess(ArrayList<Player_FutsalClass> grounds) {
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        }, 2000);
    }

    private List<Player_FutsalClass> getMatches(){
        return futsalGrounds;

    }

    public void readMoreData(final int nextLimit, final Player_GroundFragment.onGetDataListener listener){
        listener.onStart();
        futsalDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        futsalDatabase.child(postSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(futsalGrounds.size()<totalShowingFutsal){
                                        if(dataSnapshot.child("ShowUs").getValue().toString().equals("true") && dataSnapshot.child("Verified").getValue().toString().equals("true")){
                                            if(!dataSnapshot.child("BlockList").child(PlayerHolder.getPlayer().getUserId()).exists()){
                                                final Player_FutsalClass futsalClass = new Player_FutsalClass(dataSnapshot.child("FutsalContact").getValue().toString(),
                                                        dataSnapshot.child("FutsalEmail").getValue().toString()
                                                        ,dataSnapshot.child("FutsalName").getValue().toString(),
                                                        dataSnapshot.child("OwnerName").getValue().toString(),dataSnapshot.child("FutsalLocation").getValue().toString(),postSnapshot.getKey()
                                                        ,dataSnapshot.child("Verified").getValue().toString());
                                                if(dataSnapshot.child("DisplayPicture").exists()){
                                                    futsalClass.setFutsalDisplayImageLink(dataSnapshot.child("DisplayPicture").getValue().toString());
                                                }
                                                if(dataSnapshot.child("Ratings").exists()){
                                                    float totalRating = Float.parseFloat(dataSnapshot.child("Ratings").child("Rating").getValue().toString());
                                                    int totalUsers = Integer.parseInt(dataSnapshot.child("Ratings").child("TotalUsers").getValue().toString());
                                                    futsalClass.setFutsalRating(totalRating/totalUsers);
                                                }
                                                if(dataSnapshot.child("Price").exists()){
                                                    futsalClass.setFutsalPrice(dataSnapshot.child("Price").getValue().toString());
                                                }
                                                if(currentSize-1<nextLimit){
                                                    try {
                                                        if(futsalClass.getFutsalId().equals(futsalGrounds.get(forDupCounter).getFutsalId())){
                                                            //
                                                        }
                                                    }
                                                    catch (IndexOutOfBoundsException e){
                                                        futsalGrounds.add(futsalClass);
                                                        currentSize++;
                                                    }
                                                    forDupCounter++;

                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        CountDownTimer anotherCounter = new CountDownTimer(1500,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                listener.onSuccess(futsalGrounds);
                            }
                        };
                        anotherCounter.start();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void fillData(){
        readData(new onGetDataListener() {
            @Override
            public void onSuccess(final ArrayList<Player_FutsalClass> grounds) {
                countDownTimer = new CountDownTimer(2000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        futsalGroundRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                }.start();
            }

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
                futsalGroundRecyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public interface onGetDataListener{
        void onSuccess(ArrayList<Player_FutsalClass> grounds);
        void onStart();
        void onFailure();
    }

    public void readData(final Player_GroundFragment.onGetDataListener listener) {
        listener.onStart();
        final DatabaseReference futsalDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal");
        futsalDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    futsalDatabase.child(postSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                if(dataSnapshot.child("ShowUs").getValue().toString().equals("true") && dataSnapshot.child("Verified").getValue().toString().equals("true")){
                                    if(!dataSnapshot.child("BlockList").child(PlayerHolder.getPlayer().getUserId()).exists()){
                                        final Player_FutsalClass futsalClass = new Player_FutsalClass(dataSnapshot.child("FutsalContact").getValue().toString(),dataSnapshot.child("PersonalEmail").getValue().toString()
                                                ,dataSnapshot.child("FutsalName").getValue().toString(),dataSnapshot.child("OwnerName").getValue().toString(),dataSnapshot.child("FutsalLocation").getValue().toString(),postSnapshot.getKey()
                                                ,dataSnapshot.child("Verified").getValue().toString());
                                        if(dataSnapshot.child("DisplayPicture").exists()){
                                            futsalClass.setFutsalDisplayImageLink(dataSnapshot.child("DisplayPicture").getValue().toString());
                                        }
                                        if(dataSnapshot.child("Ratings").exists()){
                                            float totalRating = Float.parseFloat(dataSnapshot.child("Ratings").child("Rating").getValue().toString());
                                            int totalUsers = Integer.parseInt(dataSnapshot.child("Ratings").child("TotalUsers").getValue().toString());
                                            futsalClass.setFutsalRating(totalRating/totalUsers);
                                        }
                                        if(dataSnapshot.child("Price").exists()){
                                            futsalClass.setFutsalPrice(dataSnapshot.child("Price").getValue().toString());
                                        }
                                        if(futsalGrounds.size()<10){
                                            futsalGrounds.add(futsalClass);
                                        }
                                        totalShowingFutsal++;
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    listener.onSuccess(futsalGrounds);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databalistseError) {

            }
        });
    }


}
