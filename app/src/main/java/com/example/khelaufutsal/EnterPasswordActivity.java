package com.example.khelaufutsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khelaufutsal.Futsal.FutsalMainActivity;
import com.example.khelaufutsal.Player.PlayersMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EnterPasswordActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button loginButton;
    ProgressBar progressBar;
    ActivityOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.loginsBar));

        progressBar = findViewById(R.id.progressBar_enterPassword);

         mAuth = FirebaseAuth.getInstance();

         String takenEmail = "";

        final Bundle email = getIntent().getExtras();
        if(email!=null){
            takenEmail=email.getString("Email");
        }
        options =ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.right_to_left, R.anim.exit_right);
        final String finalEmail = takenEmail;
        final TextInputEditText password= findViewById(R.id.loginUserPassword);
        loginButton = findViewById(R.id.loginButton_password);
        ImageView backButton = findViewById(R.id.closeButton_enterPassword);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                final String takenPassword = password.getText().toString();
                mAuth.signInWithEmailAndPassword(finalEmail,takenPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final DatabaseReference futsalDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal");
                            futsalDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()){
                                            if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("AdminBlock").exists()){
                                                blocked();
                                            }
                                            else {
                                                Intent intent = new Intent(getApplicationContext(), FutsalMainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent,options.toBundle());
                                                finish();
                                            }
                                        }
                                        else {
                                            enableStuff();
                                        }
                                    }
                                    else {
                                        enableStuff();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    startActivity(new Intent(getApplicationContext(), PlayersMainActivity.class));
                                    finish();
                                }
                            });

                            final DatabaseReference playersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players");
                            playersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        try{
                                            if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()){
                                                if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("AdminBlock").exists()){
                                                    blocked();
                                                }
                                                else {
                                                    Intent intent = new Intent(getApplicationContext(),PlayersMainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent,options.toBundle());
                                                    finish();
                                                }
                                            }
                                            else {
                                                enableStuff();
                                            }
                                        }
                                        catch (NullPointerException e){
                                            blocked();
                                        }
                                    }
                                    else {
                                        enableStuff();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else {
                            try {
                                enableStuff();
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidCredentialsException invalidCredential){
                                Toast.makeText(getApplicationContext(),"Invalid Password.",Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Can't Log You In At This Moment.",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });
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

    private void enableStuff(){
        loginButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    private void openLogin(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent,options.toBundle());
        finish();
    }

    private void blocked(){
        mAuth.signOut();
        Toast.makeText(getApplicationContext(),"You have been blocked by the Admin.",Toast.LENGTH_LONG).show();
        openLogin();
    }

}
