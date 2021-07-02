package com.example.khelaufutsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPasswordActivity extends AppCompatActivity {

    String userName;
    String displayPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Button revokeButton = findViewById(R.id.revokeButton_password);
        final TextInputEditText emailAddress = findViewById(R.id.email_forgotPassword);
        final ProgressBar progressBar = findViewById(R.id.progressBar_forgotPassword);
        final TextView error = findViewById(R.id.errorText_forgotPassword);

        progressBar.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        revokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailAddress.getText().toString().trim().isEmpty()){
                    emailAddress.setError("Enter Email Address");
                    emailAddress.requestFocus();
                }
                else if(!EmailValidator.emailValidator(emailAddress.getText().toString().trim())){
                    emailAddress.setError("Enter Valid Email");
                    emailAddress.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);

                    final FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(emailAddress.getText().toString().trim(),"tempPassword").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                try {
                                    progressBar.setVisibility(View.GONE);
                                    throw task.getException();
                                }
                                catch (FirebaseAuthInvalidUserException inValid){
                                    error.setText("No such email exists in Khelau Futsal.");
                                    error.setVisibility(View.VISIBLE);

                                }
                                catch (FirebaseAuthInvalidCredentialsException wrongPassword){
                                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                error.setText("Check your Email to reset your password.");
                                                emailAddress.setText("");
                                                emailAddress.clearFocus();
                                            }
                                            else {
                                                error.setText("We couldn't reach to your Email Address at this moment.");
                                            }
                                        }
                                    });
                                    error.setVisibility(View.VISIBLE);
                                }
                                catch (Exception e) {
                                    error.setText("Unknown Error Occured.");
                                    error.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }

            }
        });

        ImageView backButton = findViewById(R.id.closeButton_forgetPassword);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_to_left,R.anim.exit_right);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.right_to_left,R.anim.exit_right);
    }
}
