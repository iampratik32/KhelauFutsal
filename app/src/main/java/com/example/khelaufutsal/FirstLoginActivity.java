package com.example.khelaufutsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.khelaufutsal.Futsal.FutsalEnterDetails_FirstFragment;
import com.example.khelaufutsal.Player.PlayerEnterDetails_FirstFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FirstLoginActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        DialogCancelRegistry cancelRegistryDialog = new DialogCancelRegistry();
        cancelRegistryDialog.show(getSupportFragmentManager(),"Dialog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.loginsBar));

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.firstLoginFrameLayout,new PlayerEnterDetails_FirstFragment());
        fragmentTransaction.commit();

        ImageView closeButton = findViewById(R.id.firstLoginCloseButton);
        ImageView backButton = findViewById(R.id.firstLoginBackButton);
        backButton.setEnabled(false);
        backButton.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCancelRegistry cancelRegistryDialog = new DialogCancelRegistry();
                cancelRegistryDialog.show(getSupportFragmentManager(),"Dialog");
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationBar_RegisterPage);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            RegisterDetailHolder.setAllDetails(null);
            switch (menuItem.getItemId()){
                case R.id.registerAsPlayer_menuItem:
                    selectedFragment = new PlayerEnterDetails_FirstFragment();
                    break;

                case R.id.registerAsFutsal_menuItem:
                    selectedFragment = new FutsalEnterDetails_FirstFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.firstLoginFrameLayout,selectedFragment).commit();
            return true;
        }
    };
}
