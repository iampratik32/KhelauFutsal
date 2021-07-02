package com.example.khelaufutsal.Futsal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.khelaufutsal.R;

public class Futsal_SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal__settings);

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_FutsalSettings,new Futsal_SettingsFragment());
        fragmentTransaction.commit();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));

        if(getIntent().getExtras()!=null){
            String toLoad = getIntent().getExtras().getString("Fragment");
            if(toLoad.equals("Settings")){
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.add(R.id.frameLayout_FutsalSettings,new Futsal_AccountSettingsFragment());
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            }
        }
    }
}
