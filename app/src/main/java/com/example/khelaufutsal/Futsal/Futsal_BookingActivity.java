package com.example.khelaufutsal.Futsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.khelaufutsal.R;

public class Futsal_BookingActivity extends AppCompatActivity {


    String takenValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal__booking);

        if(getIntent().getExtras()!=null){
            takenValue = getIntent().getStringExtra("Open");
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));

        Fragment newFragment = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(takenValue.equals("Pending")){
            newFragment = new Futsal_PendingBookingFragment();
        }
        else if(takenValue.equals("Booked")){
            newFragment = new Futsal_BookedBookingFragment();
        }
        else if (takenValue.equals("All")){
            newFragment = new Futsal_AllBookedBookingFragment();
        }

        transaction.replace(R.id.frameLayout_FutsalBooking,newFragment);
        transaction.commit();



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
