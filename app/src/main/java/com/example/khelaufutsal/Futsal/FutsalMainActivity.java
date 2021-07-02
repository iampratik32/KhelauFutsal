package com.example.khelaufutsal.Futsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.khelaufutsal.Player.FutsalBookingRecyclerAdapter;
import com.example.khelaufutsal.Player.Player_FutsalClassHolder;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

public class FutsalMainActivity extends AppCompatActivity {

    private ArrayList<Chat_PotentialChatUser> chatUsers = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    private boolean backPressedOnce = false;
    @Override
    public void onBackPressed() {
        if(bottomNavigationView.getSelectedItemId()==R.id.mainDashboard_FutsalDashboard){
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
            bottomNavigationView.setSelectedItemId(R.id.mainDashboard_FutsalDashboard);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal_main);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));


        LoadFutsalInformation loadFutsalInformation = new LoadFutsalInformation();
        loadFutsalInformation.execute();



        FloatingActionButton settingsButton = findViewById(R.id.floatingButton_Futsal);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(),Futsal_SettingsActivity.class);
                startActivity(newIntent);
            }
        });

        bottomNavigationView = findViewById(R.id.futsal_bottomNavigator);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
        bottomNavigationView.setSelectedItemId(R.id.mainDashboard_FutsalDashboard);
        getChatUsers();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.bookingItem_FutsalDashboard:
                    selectedFragment = new Futsal_BookingFragment();
                    break;


                case R.id.mainDashboard_FutsalDashboard:
                    selectedFragment = new Futsal_MainFragment();
                    break;

                case R.id.historyPage_FutsalDashboard:
                    selectedFragment = new Futsal_HistoryFragment();
                    break;
                    case R.id.chatPage_FutsalDashboard:
                    selectedFragment = new Futsal_ChatFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.futsalMainActivityFrameLayout,selectedFragment).commit();
            return true;
        }
    };

    private void getChatUsers(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        for(final DataSnapshot post:postSnapshot.getChildren()){
                            DatabaseReference playerDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(post.child("Taken").getValue().toString());
                            playerDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot ds) {
                                    if(ds.exists()){
                                        Chat_PotentialChatUser chatUser = new Chat_PotentialChatUser(post.child("Taken").getValue().toString(),ds.child("DisplayName").getValue().toString());
                                        if(ds.child("DisplayPicture").exists()){
                                            chatUser.setProfileImage(ds.child("DisplayPicture").getValue().toString());
                                        }
                                        chatUsers.add(chatUser);
                                        FutsalHolder.getFutsal().setChatUsers(chatUsers);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
