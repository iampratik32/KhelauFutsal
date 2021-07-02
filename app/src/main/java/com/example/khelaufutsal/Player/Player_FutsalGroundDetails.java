package com.example.khelaufutsal.Player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khelaufutsal.R;

public class Player_FutsalGroundDetails extends AppCompatActivity {

    String futsalEmail = "",futsalId, futsalName,futsalLocation,futsalContact,futsalDispalyImage,futsalPrice,futsalLatLan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player__futsal_ground_details);

        Bundle getBundle = getIntent().getExtras();

        ImageView backButton = findViewById(R.id.backButton_bookFutsalPage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getBundle!=null){
            futsalName = getBundle.getString("futsalName");
            futsalLocation = getBundle.getString("futsalLocation");
            futsalId = getBundle.getString("futsalId");
            futsalContact = getBundle.getString("futsalNumber");
            futsalEmail = getBundle.getString("futsalEmail");
            futsalDispalyImage =getBundle.getString("futsalDisplayImage");
            futsalPrice = getBundle.getString("futsalPrice");
            futsalLatLan = getBundle.getString("futsalLatLan");
        }

        Player_FutsalClass player_futsalClass = new Player_FutsalClass(futsalContact,futsalEmail,futsalName,"",futsalLocation,futsalId,"true");
        player_futsalClass.setFutsalPrice(futsalPrice);
        player_futsalClass.setFutsalDisplayImageLink(futsalDispalyImage);
        player_futsalClass.setFutsalLatLan(futsalLatLan);
        Player_FutsalClassHolder.setFutsal(player_futsalClass);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));


        TextView futsalNameOnTop = findViewById(R.id.futsalName_bookFutsalPage);
        futsalNameOnTop.setText(futsalName);


        androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_Player_FutsalGroundDetail,new Player_GroundDetailFragment());
        fragmentTransaction.commit();



    }


}
