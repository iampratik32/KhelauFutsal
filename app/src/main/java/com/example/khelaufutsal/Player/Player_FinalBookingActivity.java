package com.example.khelaufutsal.Player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player_FinalBookingActivity extends AppCompatActivity {

    String timing = "";
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player__final_booking);

        Bundle getBundle = getIntent().getExtras();

        if(getBundle!=null){
            timing = getBundle.getString("Timing");
            date = getBundle.getString("Date");
        }

        TextView futsalName = findViewById(R.id.futsalName_finalBookingPage);
        ImageView closeButton = findViewById(R.id.closeButton_finalBookingPage);
        ImageView futsalDisplayImage = findViewById(R.id.futsalDisplayPicture_finalBookingPage);
        TextView futsalDate = findViewById(R.id.bookingDate_finalBookingPage);
        TextView futsalTime = findViewById(R.id.bookingTime_finalBookingPage);
        Button confirmBooking = findViewById(R.id.confirmBooking_finalBookingPage);
        TextView futsalPrice = findViewById(R.id.futsalPrice_finalBookingPage);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));

        Player_FutsalClass futsal = Player_FutsalClassHolder.getFutsal();

        futsalName.setText(futsal.getFutsalName());
        futsalDate.setText(date);
        futsalTime.setText(timing);
        futsalPrice.setText(futsal.getFutsalPrice());
        Glide.with(getApplicationContext()).load(Player_FutsalClassHolder.getFutsal().getFutsalDisplayImageLink()).into(futsalDisplayImage);


        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogConfirmBooking dialog = new DialogConfirmBooking(timing,date);
                dialog.show(getSupportFragmentManager(),"ConfirmBooking");
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right,R.anim.exit_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_to_right,R.anim.exit_left);
    }
}
