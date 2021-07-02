package com.example.khelaufutsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.khelaufutsal.Futsal.FutsalMainActivity;
import com.example.khelaufutsal.Player.PlayersMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.splashBar));

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            final DatabaseReference playerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Players");
            playerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(mAuth.getUid()).exists()){
                        if(dataSnapshot.child(mAuth.getUid()).child("AdminBlock").exists()){
                            blocked();
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), PlayersMainActivity.class));
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DatabaseReference futsalDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal");
            futsalDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        if(dataSnapshot.child(mAuth.getUid()).exists()){

                            if(dataSnapshot.child(mAuth.getUid()).child("AdminBlock").exists()){
                                blocked();
                            }
                            else {
                                startActivity(new Intent(getApplicationContext(), FutsalMainActivity.class));
                                finish();
                            }
                        }
                    }
                    catch (NullPointerException e){
                        blocked();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            openLogin();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }

    private void openLogin(){
        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        finish();
    }

    public void blocked(){
        mAuth.signOut();
        Toast.makeText(getApplicationContext(),"You have been blocked by the Admin.",Toast.LENGTH_LONG).show();
        openLogin();
    }
}
