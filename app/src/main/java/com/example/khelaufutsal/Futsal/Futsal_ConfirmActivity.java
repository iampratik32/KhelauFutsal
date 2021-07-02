package com.example.khelaufutsal.Futsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Futsal_ConfirmActivity extends AppCompatActivity {

    String userId, time, date, userName,booked,phone,displayImageLink,cancelRequest;
    BottomSheetDialog menuBottomSheetDialog;
    LinearLayout blockOption;
    ProgressBar blockProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal__confirm);

        Bundle getBundle = getIntent().getExtras();

        if(getIntent().getExtras()!=null){
            userId = getBundle.getString("UserId");
            time = getBundle.getString("Time");
            date = getBundle.getString("Date");
            userName = getBundle.getString("UserName");
            booked = getBundle.getString("Booked");
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));



        TextView userNameTextView = findViewById(R.id.userName_futsalConfirm);
        ImageView closeButton = findViewById(R.id.closeButton_futsalConfirm);
        ImageView menuButton = findViewById(R.id.menuButton_futsalConfirm);
        Button rejectButton = findViewById(R.id.rejectButton_confirmBooking);
        final CircleImageView userDisplayPicture = findViewById(R.id.userDisplayImage_futsalConfirm);
        ImageView chatIcon = findViewById(R.id.chatButton_bookingConfirmPage);
        TextView orText = findViewById(R.id.orText_confrim);
        userNameTextView.setText(userName);
        Button cancelOwnBooking = findViewById(R.id.cancelOwnBookingButton);
        LinearLayout phoneLayout = findViewById(R.id.phoneLayout_confirmBooking);
        Button confirmButton = findViewById(R.id.confirmBookingButton_confirmBooking);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.left_to_right,R.anim.exit_left);
                finish();
            }
        });

        TextView futsalDate = findViewById(R.id.futsalDate_pendingFutsal);
        TextView futsalTime = findViewById(R.id.futsalTime_pendingFutsal);
        TextView futsalPrice =findViewById(R.id.futsalPrice_pendingFutsal);

        futsalDate.setText(date);
        futsalTime.setText(time);
        futsalPrice.setText(FutsalHolder.getFutsal().getFutsalPrice());


        if(userName.equals(FutsalHolder.getFutsal().getFutsalName())){
            userName=FutsalHolder.getFutsal().getFutsalName();
            Glide.with(getApplicationContext()).load(Uri.parse(FutsalHolder.getFutsal().getDisplayPicture())).into(userDisplayPicture);
            chatIcon.setVisibility(View.GONE);
            phoneLayout.setVisibility(View.GONE);
            confirmButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            menuButton.setVisibility(View.GONE);
            orText.setVisibility(View.GONE);
            cancelOwnBooking.setVisibility(View.VISIBLE);
            cancelOwnBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference bookingDb = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId()).child(date).child(time);
                    bookingDb.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Booking Removed.",Toast.LENGTH_LONG).show();
                                Intent mainIntent = new Intent(getApplicationContext(),FutsalMainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(mainIntent);
                            }
                        }
                    });
                }
            });

        }
        else {
            if(booked.equals("True")){
                confirmButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                orText.setVisibility(View.GONE);

            }
            else {
                confirmButton.setVisibility(View.VISIBLE);
            }

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        phone = dataSnapshot.child("ContactNumber").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFutsalConfirmBooking dialogFutsalConfirmBooking = new DialogFutsalConfirmBooking(time,date,userId);
                    dialogFutsalConfirmBooking.show(getSupportFragmentManager(),"ConfirmDialog");
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFutsalRejectBooking dialogFutsalRejectBooking = new DialogFutsalRejectBooking(time,date,userId);
                    dialogFutsalRejectBooking.show(getSupportFragmentManager(),"RejectDialog");

                }
            });

            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = LayoutInflater.from(Futsal_ConfirmActivity.this).inflate(R.layout.dialog_confirm_menu,null);
                    blockProgress = view.findViewById(R.id.progressBar_blockUser);
                    blockOption = view.findViewById(R.id.blockOptionDialog_ConfirmMenu);
                    blockOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference blockList = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("BlockList");
                            Map<String,String> blockMap = new HashMap<>();
                            blockMap.put(userId,"true");
                            blockList.setValue(blockMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        final DatabaseReference bookingsDb = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId());
                                        bookingsDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    for(DataSnapshot post:dataSnapshot.getChildren()){
                                                        for(DataSnapshot p:post.getChildren()){
                                                            if(p.child("Taken").getValue().toString().equals(userId)){
                                                                bookingsDb.child(post.getKey()).child(p.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            final DatabaseReference playerBooking = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(userId).child("Bookings");
                                                                            playerBooking.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    if(dataSnapshot.exists()){
                                                                                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                            if(snapshot.child("Futsal").getValue().toString().equals(FutsalHolder.getFutsal().getUserId())){
                                                                                                playerBooking.child(snapshot.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if(task.isSuccessful()){
                                                                                                            //TODO UDATE BOOKINS
                                                                                                            //TODO UPDATE CHAT LIST
                                                                                                            overridePendingTransition(R.anim.left_to_right,R.anim.exit_left);
                                                                                                            finish();
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
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                    menuBottomSheetDialog = new BottomSheetDialog(Futsal_ConfirmActivity.this);
                    menuBottomSheetDialog.setContentView(view);
                    menuBottomSheetDialog.show();
                }
            });

            DatabaseReference imageLinkRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(userId).child("DisplayPicture");
            imageLinkRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        displayImageLink = dataSnapshot.getValue().toString();
                        Glide.with(getApplicationContext()).load(Uri.parse(displayImageLink)).into(userDisplayPicture);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            chatIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatIntent = new Intent(Futsal_ConfirmActivity.this,ChatWithUserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("UserId",userId);
                    bundle.putString("UserName",userName);
                    bundle.putString("ImageLink",displayImageLink);
                    bundle.putString("UserType","Futsal");
                    chatIntent.putExtras(bundle);
                    startActivity(chatIntent);
                }
            });
            Button cancelButton = findViewById(R.id.acceptCancelButton_confirmBooking);
            Button rejectCancelButton = findViewById(R.id.rejectCancelButton_confirmBooking);
            final LinearLayout cancelLayout = findViewById(R.id.cancelRequestLayout_futsalConfirmBooking);

            final DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId()).child(date).child(time);
            bookingReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        cancelRequest=dataSnapshot.child("Confirmed").getValue().toString();
                        if(cancelRequest.equals("RequestCancel")){
                            cancelLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookingReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                final DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(userId).child("Bookings");
                                userDb.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for(DataSnapshot post:dataSnapshot.getChildren()){
                                                if(post.child("Futsal").getValue().toString().equals(FutsalHolder.getFutsal().getUserId()) && post.child("Time").getValue().toString().equals(time) && post.child("Date").getValue().toString().equals(date)){
                                                    DatabaseReference toRemove = userDb.child(post.getKey());
                                                    toRemove.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(getApplicationContext(),"Booking Removed",Toast.LENGTH_SHORT).show();
                                                                Intent bookingIntent = new Intent(Futsal_ConfirmActivity.this,FutsalMainActivity.class);
                                                                bookingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(bookingIntent);
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
                        }
                    });
                }
            });

            rejectCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookingReference.child("Confirmed").setValue("True").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Rejected",Toast.LENGTH_SHORT).show();
                                cancelLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
        }




    }

    private void removeBooking() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId()).child(date).child(time);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    final DatabaseReference playerDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(userId).child("Bookings");
                    playerDb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for (DataSnapshot post:dataSnapshot.getChildren()){

                                    if(post.child("Date").getValue().toString().equals(date) && post.child("Time").getValue().toString().equals(time) && post.child("Futsal").getValue().toString().equals(FutsalHolder.getFutsal().getUserId())){
                                        DatabaseReference toDelete = playerDb.child(post.getKey());
                                        toDelete.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    for(int i=0;i<FutsalHolder.getFutsal().getFutsalBooking().size();i++){
                                                        if(FutsalHolder.getFutsal().getFutsalBooking().get(i).getUserId().equals(userId)){
                                                            FutsalHolder.getFutsal().getFutsalBooking().remove(i);
                                                        }
                                                    }
                                                    final DatabaseReference blockingDb = FirebaseDatabase.getInstance().getReference().child(FutsalHolder.getFutsal().getUserId());
                                                    blockingDb.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                                                    for(DataSnapshot post:postSnapshot.getChildren()){
                                                                        Log.d("Found",post.child("Taken").getValue().toString());
                                                                        if(post.child("Taken").getValue().toString().equals(userId)){
                                                                            Log.d("Found",post.child("Taken").getValue().toString());
                                                                            //TODO Block WALA
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

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
