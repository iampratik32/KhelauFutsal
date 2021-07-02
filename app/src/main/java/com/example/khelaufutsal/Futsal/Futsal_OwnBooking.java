package com.example.khelaufutsal.Futsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.Player.PlayerHolder;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Futsal_OwnBooking extends AppCompatActivity {
    String time;
    String date;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal__own_booking);

        if(getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            time = bundle.getString("Time");
            date = bundle.getString("Date");
        }

        progressBar = findViewById(R.id.progressBar_ownBooking);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));

        TextView futsalName = findViewById(R.id.futsalName_ownBooking);
        ImageView closeButton = findViewById(R.id.closeButton_ownBooking);
        ImageView futsalDisplayImage = findViewById(R.id.futsalDisplayPicture_ownBooking);
        TextView futsalDate = findViewById(R.id.bookingDate_ownBooking);
        TextView futsalTime = findViewById(R.id.bookingTime_ownBooking);
        Button confirmBooking = findViewById(R.id.confirmBooking_ownBooking);
        TextView futsalPrice = findViewById(R.id.futsalPrice_ownBooking);

        futsalName.setText(FutsalHolder.getFutsal().getFutsalName());
        Glide.with(getApplicationContext()).load(Uri.parse(FutsalHolder.getFutsal().getDisplayPicture())).into(futsalDisplayImage);
        futsalDate.setText(date);
        futsalTime.setText(time);
        futsalPrice.setText(FutsalHolder.getFutsal().getFutsalPrice());
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference bookingDatabase = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId()).child(date).child(time);
                final Map newMap = new HashMap();
                newMap.put("Taken", FutsalHolder.getFutsal().getUserId());
                newMap.put("Confirmed","True");
                newMap.put("Booked","True");
                bookingDatabase.setValue(newMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"The Booking Is Completed.",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),FutsalMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Unexpected Error Occurred!!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
