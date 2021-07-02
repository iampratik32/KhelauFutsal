package com.example.khelaufutsal.Player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.khelaufutsal.Futsal.BookingClass;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PlayersMainActivity extends AppCompatActivity {

    private boolean backPressedOnce = false;
    CountDownTimer countDownTimer;
    LinkedList<BookingClass> listOfBooking;
    LinkedList<BookingClass> listOfHistory;
    LinkedList<BookingClass> toAddHistory;
    ProgressDialog dialog ;
    SimpleDateFormat date;
    SimpleDateFormat toLongDate;
    Date checkingDate;
    BottomNavigationView bottomNavigationView;
    DatabaseReference playerDatabaseReference;


    @Override
    public void onBackPressed() {
        if(bottomNavigationView.getSelectedItemId()==R.id.mainDashboard_PlayerDashboard){
            if (backPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.backPressedOnce = true;
            Toast.makeText(this, "Press back again to quit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedOnce=false;
                }
            }, 2000);
        }
        else {
            bottomNavigationView.setSelectedItemId(R.id.mainDashboard_PlayerDashboard);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));

        bottomNavigationView = findViewById(R.id.player_bottomNavigator);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        if(PlayerHolder.getPlayer()==null){
            readData(new onGetDataListener() {
                @Override
                public void onSuccess(PlayerClass playerClass) {
                    readBookings(new onGetDataListener() {
                        @Override
                        public void onSuccess(PlayerClass playerClass) {
                            readCompleteBooking(new onGetDataListener() {
                                @Override
                                public void onSuccess(PlayerClass playerClass) {
                                    updateBookings(new onGetDataListener() {
                                        @Override
                                        public void onSuccess(PlayerClass playerClass) {
                                            readFutsalInfo(new onGetDataListener() {
                                                @Override
                                                public void onSuccess(PlayerClass playerClass) {
                                                    readHistory(new onGetDataListener() {
                                                        @Override
                                                        public void onSuccess(PlayerClass playerClass) {
                                                            Player_LoadInformation.loadChatData();
                                                            countDownTimer = new CountDownTimer(2500,1000) {
                                                                @Override
                                                                public void onTick(long millisUntilFinished) {
                                                                }

                                                                @Override
                                                                public void onFinish() {
                                                                    dialog.dismiss();
                                                                    bottomNavigationView.setSelectedItemId(R.id.mainDashboard_PlayerDashboard);
                                                                }
                                                            }.start();
                                                        }

                                                        @Override
                                                        public void onStart() {

                                                        }

                                                        @Override
                                                        public void onFailure() {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onStart() {

                                                }

                                                @Override
                                                public void onFailure() {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onFailure() {

                                        }
                                    });
                                }

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onFailure() {

                                }
                            });
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }

                @Override
                public void onStart() {
                    dialog = ProgressDialog.show(PlayersMainActivity.this, "","Please wait...", true);
                }

                @Override
                public void onFailure() {
                    dialog.dismiss();
                }
            });
        }



        FloatingActionButton floatingActionButton = findViewById(R.id.floatingButton_Player);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Player_SettingsActivity.class));
            }
        });

    }

    public BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.bookingItem_PlayerDashboard:
                    selectedFragment = new Player_BookingFragment();
                    break;


                case R.id.group_PlayerDashboard:
                    selectedFragment = new Player_GroupFramgment();
                    break;

                case R.id.mainDashboard_PlayerDashboard:
                    selectedFragment = new Player_MainFragment();
                    break;

                case R.id.ground_PlayerDashboard:
                    selectedFragment = new Player_GroundFragment();
                    break;

                case R.id.history_PlayerDashboard:
                    selectedFragment = new Player_HistoryFragment();
                    break;
            }
            try {
                getSupportFragmentManager().beginTransaction().replace(R.id.playerMainActivityFrameLayout,selectedFragment).commit();
            }
            catch (IllegalStateException e){
            }
            return true;
        }
    };

    public interface onGetDataListener{
        void onSuccess(PlayerClass playerClass);
        void onStart();
        void onFailure();
    }

    public void readData(final PlayersMainActivity.onGetDataListener listener) {
        listener.onStart();
        Log.d("Inhere","ReadDate");
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        playerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(userId);
        playerDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    PlayerClass playerClass = new PlayerClass(userId,FirebaseAuth.getInstance().getCurrentUser().getEmail(),dataSnapshot.child("ContactNumber").getValue().toString()
                            ,dataSnapshot.child("DisplayName").getValue().toString(),dataSnapshot.child("Name").getValue().toString());
                    if(dataSnapshot.child("DisplayPicture").exists()){
                        playerClass.setDisplayPicture(dataSnapshot.child("DisplayPicture").getValue().toString());
                    }
                    PlayerHolder.setPlayer(playerClass);
                    listener.onSuccess(playerClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void readBookings(final PlayersMainActivity.onGetDataListener listener){
        listener.onStart();
        Log.d("Inhere","ReadBookings");
        try {
            date = new SimpleDateFormat("dd-MMMM");
            toLongDate =new SimpleDateFormat("MM/dd/yyyy");
            checkingDate = date.parse(date.format(Calendar.getInstance().getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        listOfBooking = new LinkedList<>();
        listOfHistory = new LinkedList<>();
        playerDatabaseReference.child("Bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        BookingClass booking = new BookingClass(postSnapshot.child("Futsal").getValue().toString(),postSnapshot.child("Date").getValue().toString()
                        ,postSnapshot.child("Time").getValue().toString(),"Taken","",PlayerHolder.getPlayer().getUserId());

                        try {
                            Date dDate = date.parse(booking.getDate());
                            Log.d("HK",checkingDate.toString());
                            Log.d("HK",dDate.toString());
                            Log.d("HK",String.valueOf(checkingDate.compareTo(dDate)));
                            if(checkingDate.compareTo(dDate)==1){

                                listOfHistory.add(booking);
                                playerDatabaseReference.child("Bookings").child(postSnapshot.getKey()).removeValue();
                            }
                            else if(checkingDate.compareTo(dDate)==0){
                                String hour = booking.getTime();
                                String futTime ="";
                                for(int i=0;i<hour.length();i++){
                                    if(String.valueOf(hour.charAt(i)).equals(":")){
                                        break;
                                    }
                                    futTime = futTime+hour.charAt(i);
                                }
                                DateFormat hourFormat = new SimpleDateFormat("HH");
                                String thisHour = hourFormat.format(Calendar.getInstance().getTime());
                                Log.d("QK",thisHour);
                                Log.d("QK",futTime);
                                if(Integer.parseInt(thisHour)>=Integer.parseInt(futTime)){
                                    listOfHistory.add(booking);
                                }
                                else {
                                    listOfBooking.add(booking);
                                }
                            }
                            else {
                                listOfBooking.add(booking);
                            }
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    PlayerHolder.getPlayer().setPlayerBookings(listOfBooking);
                    PlayerHolder.getPlayer().setPlayerHistory(listOfHistory);
                    listener.onSuccess(PlayerHolder.getPlayer());
                }
                else {
                    listener.onSuccess(PlayerHolder.getPlayer());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readCompleteBooking(final PlayersMainActivity.onGetDataListener listener){
        listener.onStart();
        Log.d("INhere","ReadComplete");
        if(listOfBooking.size()==0 || listOfBooking==null){
            listener.onSuccess(PlayerHolder.getPlayer());
        }
        else {
            for(int i=0;i<listOfBooking.size();i++){
                final int tempVar = i;
                DatabaseReference bookings = FirebaseDatabase.getInstance().getReference().child("Booking").child(listOfBooking.get(i).getFutsalId()).child(listOfBooking.get(i).getDate()).child(listOfBooking.get(i).getTime());
                bookings.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            try{
                                listOfBooking.get(tempVar).setBooked(dataSnapshot.child("Booked").getValue().toString());
                                listOfBooking.get(tempVar).setConfirmed(dataSnapshot.child("Confirmed").getValue().toString());
                            }
                            catch (IndexOutOfBoundsException e){
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onFailure();
                    }
                });
                PlayerHolder.getPlayer().setPlayerBookings(listOfBooking);
                listener.onSuccess(PlayerHolder.getPlayer());
            }
        }
    }

    public void updateBookings(final PlayersMainActivity.onGetDataListener listener){
        listener.onStart();
        Log.d("InHere","UpdateBooking");
        if(listOfBooking.size()==0){
            listener.onSuccess(PlayerHolder.getPlayer());
        }
        for(int i=0;i<listOfBooking.size();i++){
            final int tempV = i;
            DatabaseReference bookings = FirebaseDatabase.getInstance().getReference().child("Bookings").child(listOfBooking.get(i).getFutsalId()).child(listOfBooking.get(i).getDate()).child(listOfBooking.get(i).getTime());
            bookings.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map newMap = new HashMap();
                    try{
                        newMap.put("Booked",listOfBooking.get(tempV).getBooked());
                        newMap.put("Confirmed",listOfBooking.get(tempV).getConfirmed());
                        newMap.put("Taken",listOfBooking.get(tempV).getUserId());
                    }
                    catch (IndexOutOfBoundsException e){
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            listener.onSuccess(PlayerHolder.getPlayer());
        }
    }

    public void readHistory(final PlayersMainActivity.onGetDataListener listener){
        listener.onStart();
        Log.d("History","Inhere");
        if(listOfHistory.size()==0){
            listener.onSuccess(PlayerHolder.getPlayer());
        }
        else {
            for(int i=0;i<listOfHistory.size();i++){
                final DatabaseReference removeBookings = FirebaseDatabase.getInstance().getReference().child("Booking").child(listOfHistory.get(i).getFutsalId()).child(listOfHistory.get(i).getDate());
                final int tempVar = i;
                removeBookings.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            removeBookings.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference().child("History").child(listOfHistory.get(tempVar).getFutsalId())
                                                .child(listOfHistory.get(tempVar).getDate()).child(listOfHistory.get(tempVar).getTime());
                                        Map historyMap = new HashMap();
                                        historyMap.put("Booked",listOfHistory.get(tempVar).getBooked());
                                        historyMap.put("Taken",listOfHistory.get(tempVar).getUserId());
                                        historyReference.setValue(historyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                DatabaseReference toRate = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId());
                                                toRate.child("ToRate").setValue(listOfHistory.get(tempVar).getFutsalId());
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            listener.onSuccess(PlayerHolder.getPlayer());
        }
    }

    public void readFutsalInfo(final PlayersMainActivity.onGetDataListener listener){
        listener.onStart();
        Log.d("Inhere","Fut INFO");
        if(listOfBooking.size()==0){
            listener.onSuccess(PlayerHolder.getPlayer());
        }
        else {
            for(int i=0;i<listOfBooking.size();i++){
                final int tempV = i;
                final DatabaseReference futsalInfo = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(listOfBooking.get(i).getFutsalId());
                futsalInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                            if(dataSnapshot.exists()){
                                PlayerHolder.getPlayer().getPlayerBookings().get(tempV).setFutsalName(dataSnapshot.child("FutsalName").getValue().toString());
                                if(dataSnapshot.child("DisplayPicture").exists()){
                                    PlayerHolder.getPlayer().getPlayerBookings().get(tempV).setFutsalDisplayImage(dataSnapshot.child("DisplayPicture").getValue().toString());
                                }
                            }
                        }
                        catch (NullPointerException e){
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                listener.onSuccess(PlayerHolder.getPlayer());
            }
        }

    }
}
