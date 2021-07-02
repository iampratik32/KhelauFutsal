package com.example.khelaufutsal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.Player.PlayerHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ChooseMediaType extends AppCompatActivity {

    private StorageReference mStorageRef;
    DatabaseReference databaseReference;
    BottomSheetDialog chooseMediaBottomSheetDialog;
    public static final int CAMERA_REQ = 1984;
    public static final int GALLERY_REQ = 1985;
    LinearLayout cameraOption, galleryOption, cancelPictureOption;
    Bitmap bitmap;
    Button updateButton, goBackButton, chooseMedia;
    ImageView displayPicture;
    ProgressBar progressBar;
    String user ="Futsal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_media_type);
        displayPicture = findViewById(R.id.userDisplayPicture_chooseMedia);
        if(FutsalHolder.getFutsal()==null){
            user = "Player";
        }
        if(user.equals("Player")){
            mStorageRef = FirebaseStorage.getInstance().getReference(PlayerHolder.getPlayer().getUserId());
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId());
            if(PlayerHolder.getPlayer().getDisplayPicture()!=null){
                Glide.with(ChooseMediaType.this).load(Uri.parse(PlayerHolder.getPlayer().getDisplayPicture())).into(displayPicture);
            }
            else {
                displayPicture.setImageResource(R.drawable.football_player_icon);
            }
        }
        else {
            mStorageRef = FirebaseStorage.getInstance().getReference(FutsalHolder.getFutsal().getUserId());
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId());
            if(FutsalHolder.getFutsal().getDisplayPicture()!=null){
                Glide.with(ChooseMediaType.this).load(Uri.parse(FutsalHolder.getFutsal().getDisplayPicture())).into(displayPicture);
            }
            else {
                displayPicture.setImageResource(R.drawable.futsal_icon);
            }
        }


        chooseMedia = findViewById(R.id.chooseMediaButton_chooseMedia);
        updateButton = findViewById(R.id.updatePhotoButton_chooseMedia);
        goBackButton = findViewById(R.id.goBackButton_chooseMedia);
        chooseMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ChooseMediaType.this).inflate(R.layout.dialog_choose_media,null);
                cameraOption = view.findViewById(R.id.cameraOption_chooseMenu);
                galleryOption = view.findViewById(R.id.galleryOption_chooseMenu);
                cancelPictureOption = view.findViewById(R.id.cancelOption_chooseMenu);
                cancelPictureOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseMediaBottomSheetDialog.dismiss();
                    }
                });
                cameraOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_REQ);
                    }
                });
                galleryOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(ChooseMediaType.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                            return;
                        }
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent,"Select File"),GALLERY_REQ);
                    }
                });
                chooseMediaBottomSheetDialog = new BottomSheetDialog(ChooseMediaType.this);
                chooseMediaBottomSheetDialog.setContentView(view);
                chooseMediaBottomSheetDialog.show();
            }
        });
        progressBar = findViewById(R.id.progressBar_chooseMedia);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.GONE);
                    goBackButton.setVisibility(View.GONE);
                    chooseMedia.setVisibility(View.GONE);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] byteData = baos.toByteArray();
                    final StorageReference finalFileName = mStorageRef.child("DisplayPicture");
                    finalFileName.putBytes(byteData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            finalFileName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String url = uri.toString();
                                    DatabaseReference finalDatabase = databaseReference.child("DisplayPicture");
                                    finalDatabase.removeValue();
                                    finalDatabase.setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            appear();
                                            if(user.equals("Player")){
                                                PlayerHolder.getPlayer().setDisplayPicture(url);
                                            }
                                            else {
                                                FutsalHolder.getFutsal().setDisplayPicture(url);
                                            }
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        try {
            super.onActivityResult(requestCode,resultCode,data);
            if(requestCode==GALLERY_REQ){
                if(data!=null){
                    Uri selectedUri = data.getData();
                    try{
                        bitmap = MediaStore.Images.Media.getBitmap(ChooseMediaType.this.getContentResolver(), selectedUri);
                        displayPicture.setImageBitmap(bitmap);
                        chooseMediaBottomSheetDialog.dismiss();
                        updateButton.setVisibility(View.VISIBLE);
                        chooseMedia.setText("Try Another One");
                    }
                    catch (IOException e){

                    }
                }
            }
            else if(requestCode== CAMERA_REQ){
                if(data!=null){
                    bitmap = (Bitmap)data.getExtras().get("data");
                    displayPicture.setImageBitmap(bitmap);
                    chooseMediaBottomSheetDialog.dismiss();
                    updateButton.setVisibility(View.VISIBLE);
                    chooseMedia.setText("Try Another One");
                }
            }
        }
        catch (NullPointerException e ){

        }
    }

    private void appear(){
        progressBar.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.VISIBLE);
        goBackButton.setVisibility(View.VISIBLE);
        chooseMedia.setVisibility(View.VISIBLE);
    }
}
