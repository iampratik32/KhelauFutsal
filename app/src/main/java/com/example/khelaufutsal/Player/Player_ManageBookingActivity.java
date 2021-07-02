package com.example.khelaufutsal.Player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.Futsal.BookingClass;
import com.example.khelaufutsal.Futsal.ChatWithUserActivity;
import com.example.khelaufutsal.Futsal.Chat_PotentialChatUser;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

import static com.example.khelaufutsal.Player.PlayerHolder.getPlayer;

public class Player_ManageBookingActivity extends AppCompatActivity {

    String futsalId,futsalName,date,time,futsalDisplyImageLink,contactNumber,futsalStatus;
    BottomSheetDialog menuBottomSheetDialog;
    LinearLayout cancelOption;
    ProgressBar cancelProgress;
    DatabaseReference bookingRef;
    TextView toWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player__manage_booking);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            futsalId=bundle.getString("FutsalId");
            futsalName=bundle.getString("FutsalName");
            time=bundle.getString("Time");
            date=bundle.getString("Date");
            futsalDisplyImageLink = bundle.getString("DisplayImageLink");
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));

        TextView userNameTextView = findViewById(R.id.futsalName_manageButton);
        ImageView closeButton = findViewById(R.id.closeButton_manageBooking);
        ImageView menuButton = findViewById(R.id.menuButton_manageBooking);
        ImageView displayPicture = findViewById(R.id.futsalImage_manageBooking);
        userNameTextView.setText(futsalName);
        Glide.with(getApplicationContext()).load(Uri.parse(futsalDisplyImageLink)).into(displayPicture);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView futsalDate = findViewById(R.id.futsalDate_manageBooking);
        TextView futsalTime = findViewById(R.id.futsalTime_manageBooking);
        final TextView futsalPrice =findViewById(R.id.futsalPrice_manageBooking);

        final DatabaseReference futsalRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(futsalId);
        futsalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    futsalPrice.setText(dataSnapshot.child("Price").getValue().toString());
                    contactNumber =  dataSnapshot.child("FutsalContact").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bookingRef = FirebaseDatabase.getInstance().getReference().child("Booking").child(futsalId).child(date).child(time);
        bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    futsalStatus=dataSnapshot.child("Confirmed").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        futsalDate.setText(date);
        futsalTime.setText(time);

        CardView call = findViewById(R.id.callCard_manageBooking);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO CALL
            }
        });
        ImageView chatIcon = findViewById(R.id.chatButton_manageBooking);
        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(Player_ManageBookingActivity.this, ChatWithUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("UserId",futsalId);
                bundle.putString("UserName",futsalName);
                bundle.putString("ImageLink",futsalDisplyImageLink);
                bundle.putString("UserType","Player");
                chatIntent.putExtras(bundle);
                startActivity(chatIntent);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(Player_ManageBookingActivity.this).inflate(R.layout.dialog_cancel_request,null);

                toWrite = view.findViewById(R.id.toWrite_cancelBooking);
                cancelProgress = view.findViewById(R.id.progressBar_cancelBooking);
                cancelOption = view.findViewById(R.id.cancelOptionDialog_cancelBooking);

                if(futsalStatus.equals("False")){
                    toWrite.setText("Cancel Pending Request");
                }
                else if(futsalStatus.equals("True")){
                    toWrite.setText("Request Futsal Cancellation");
                }
                else if(futsalStatus.equals("RequestCancel")){
                    toWrite.setText("Request Sent");
                    cancelOption.setEnabled(false);
                }

                cancelOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelProgress.setVisibility(View.VISIBLE);
                        if(futsalStatus.equals("False")){
                            bookingRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final DatabaseReference removePlayerBooking = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(getPlayer().getUserId()).child("Bookings");
                                    removePlayerBooking.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for(DataSnapshot post:dataSnapshot.getChildren()){
                                                    if(post.child("Date").getValue().toString().equals(date) && post.child("Time").getValue().toString().equals(time) && post.child("Futsal").getValue().toString().equals(futsalId)){
                                                        DatabaseReference toRemove =  removePlayerBooking.child(post.getKey());
                                                        toRemove.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    try {
                                                                        PlayerHolder.setPlayer(null);
                                                                        Intent intent = new Intent(getApplicationContext(),PlayersMainActivity.class);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivity(intent);
                                                                    }
                                                                    catch (IllegalStateException e){
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            });
                        }
                        else if(futsalStatus.equals("True")){
                            bookingRef.child("Confirmed").setValue("RequestCancel").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(),"Request Sent",Toast.LENGTH_SHORT).show();
                                    menuBottomSheetDialog.dismiss();

                                }
                            });
                        }
                    }
                });

                menuBottomSheetDialog = new BottomSheetDialog(Player_ManageBookingActivity.this);
                menuBottomSheetDialog.setContentView(view);
                menuBottomSheetDialog.show();
            }
        });

    }
}
