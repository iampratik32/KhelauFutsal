package com.example.khelaufutsal.Futsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.khelaufutsal.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Futsal_StatsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal__stats);

        ImageView closeButton = findViewById(R.id.closeButton_futsalStats);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bottomNavigationView = findViewById(R.id.navigationBar_futsalStats);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
        bottomNavigationView.setSelectedItemId(R.id.monthly_FutsalStats);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.monthly_FutsalStats:
                    selectedFragment = new MonthlyFutsalStatsFragment();
                    break;


                case R.id.topPlayers_FutsalStats:
                    selectedFragment = new TopPlayersStatsFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_futsalStats,selectedFragment).commit();
            return true;
        }
    };

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
