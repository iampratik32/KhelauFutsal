package com.example.khelaufutsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.loginsBar));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        final ActivityOptions options =ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.right_to_left, R.anim.exit_right);

        Button loginButton = findViewById(R.id.loginButton_login);
        TextView forgotPassword = findViewById(R.id.forgotPassword_loginActivity);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions leftToRight =ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.left_to_right, R.anim.exit_left);
                Intent newIntent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(newIntent,leftToRight.toBundle());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSupportFragmentManager().findFragmentById(R.id.loginFrameLayout) ==null){
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.left_to_right, R.anim.exit_left);
                    fragmentTransaction.replace(R.id.loginFrameLayout,new EmailLoginFragment());
                    fragmentTransaction.commit();
                }
                else {
                    TextInputEditText email = findViewById(R.id.login_page_email);
                    getSupportFragmentManager().popBackStack();
                    final String takenEmail = email.getText().toString().trim();
                    if(takenEmail.isEmpty()){
                        email.setError("Enter your email to continue");
                        email.requestFocus();
                    }
                    else if(!EmailValidator.emailValidator(takenEmail)){
                        email.requestFocus();
                        email.setError("Enter valid email.");
                    }
                    else{
                        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "","Please wait...", true);
                        String password = "tempPassword";
                        mAuth.createUserWithEmailAndPassword(takenEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();
                                if(task.isSuccessful()){
                                    Intent newIntent = new Intent(LoginActivity.this,FirstLoginActivity.class);
                                    startActivity(newIntent,options.toBundle());
                                    finish();
                                }
                                else{
                                    dialog.dismiss();
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                        Intent newIntent = new Intent(LoginActivity.this,EnterPasswordActivity.class);
                                        newIntent.putExtra("Email",takenEmail);
                                        startActivity(newIntent,options.toBundle());
                                    }
                                    else{

                                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}