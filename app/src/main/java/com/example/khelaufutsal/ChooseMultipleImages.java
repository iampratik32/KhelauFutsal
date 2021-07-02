package com.example.khelaufutsal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class ChooseMultipleImages extends AppCompatActivity {

    private static final int MULTIPLE_CAMERA_REQUEST = 1923;
    private static final int MULTIPLE_GALLERY_REQUEST = 1922;
    private LinkedList imagesList;
    ArrayList<Uri> fileList = new ArrayList<Uri>();
    Bitmap bitmap;
    private StorageReference mStorageRef;
    ProgressBar progressBar;
    private DatabaseReference databaseReference;
    int globalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_multiple_images);
        progressBar = findViewById(R.id.progressBar_multi);

        Button backButton = findViewById(R.id.goBackButton_chooseMedia);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout chooseFromCamera = findViewById(R.id.chooseFromCamera_linearLayout);
        LinearLayout chooseFromGallery = findViewById(R.id.chooseFromGallery_linearLayout);

        chooseFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, MULTIPLE_CAMERA_REQUEST);

            }
        });
        chooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ChooseMultipleImages.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent,"Select File"),MULTIPLE_GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode== Activity.RESULT_OK) {
            mStorageRef = FirebaseStorage.getInstance().getReference(FutsalHolder.getFutsal().getUserId());
            if(requestCode== MULTIPLE_CAMERA_REQUEST){
                progressBar.setVisibility(View.VISIBLE);
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] byteData = baos.toByteArray();
                String dateAndTime = String.valueOf(Calendar.getInstance().getTime());
                final StorageReference finalFileName = mStorageRef.child("File"+dateAndTime);
                finalFileName.putBytes(byteData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final DatabaseReference finalDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Images");
                        final HashMap<String,String> hashMap = new HashMap<>();
                        finalFileName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                hashMap.put("Link",url);
                                finalDatabaseReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            CountDownTimer countDownTimer = new CountDownTimer(1500,1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {

                                                }

                                                @Override
                                                public void onFinish() {
                                                    reloadImages();
                                                    finish();
                                                }
                                            };
                                            countDownTimer.start();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });

            }
            else if(requestCode==MULTIPLE_GALLERY_REQUEST){
                progressBar.setVisibility(View.VISIBLE);
                ClipData clipData = data.getClipData();
                if(clipData!=null){
                    int count = clipData.getItemCount();
                    int proposedCount=0;
                    if(FutsalHolder.getFutsal().getFutsalImages()!=null){
                        proposedCount = FutsalHolder.getFutsal().getFutsalImages().size()+count;
                    }
                    if(proposedCount>=9){
                        int r = 9-FutsalHolder.getFutsal().getFutsalImages().size();
                        Toast.makeText(getApplicationContext(),"Can't Upload "+count+" images. You can upload only "+r+" images more.",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                    int i=0;
                    while (i<count){
                        Uri file = clipData.getItemAt(i).getUri();
                        fileList.add(file);
                        i++;
                    }
                    globalCount=count;
                    for(int j=0;j<fileList.size();j++){
                        Uri image = fileList.get(j);
                        uploadImage(image);
                    }
                }
                else if (data.getData() != null) {
                    Uri image = Uri.parse("content://media"+data.getData().getPath());
                    uploadImage(image);
                    globalCount=1;
                }
                setResult(Activity.RESULT_OK);
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Images");
                CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        reloadImages();
                        finish();
                    }
                };
                countDownTimer.start();
            }
        }

        /*ClipData clipData = null;
        Uri file;
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == MULTIPLE_CAMERA_REQUEST) {
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    Toast.makeText(getApplicationContext(), "WIll Upload", Toast.LENGTH_LONG).show();
                }
            }
            else if(requestCode == MULTIPLE_GALLERY_REQUEST) 
                clipData = data.getClipData();
                if(clipData!=null){
                int count = clipData.getItemCount();
                int i=0;
                while (i<count){
                    file = clipData.getItemAt(i).getUri();
                    fileList.add(file);
                    i++;
                }
                for(int j=0;j<fileList.size();j++){
                    Uri images = fileList.get(j);

                    try{
                        InputStream is = getContentResolver().openInputStream(images);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
                        byte[] byteData = baos.toByteArray();
                        mStorageRef = FirebaseStorage.getInstance().getReference(FutsalHolder.getFutsal().getUserId());
                        final StorageReference finalFileName = mStorageRef.child("File"+images.getLastPathSegment());
                        finalFileName.putBytes(byteData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                final DatabaseReference finalDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Images");
                                final HashMap<String,String> hashMap = new HashMap<>();
                                finalFileName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        hashMap.put("Link",url);
                                        finalDatabaseReference.push().setValue(hashMap);
                                        fileList.clear();
                                    }
                                });
                            }
                        });
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
            else if(data.getClipData() !=null){

            }
            setResult(Activity.RESULT_OK);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Images");
            finish();
        }
        catch(NullPointerException e){

        }*/
    }

    private void uploadImage(Uri image){
        try{
            InputStream is = getContentResolver().openInputStream(image);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
            byte[] byteData = baos.toByteArray();
            final StorageReference finalFileName = mStorageRef.child("File"+image.getLastPathSegment());
            finalFileName.putBytes(byteData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    final DatabaseReference finalDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Images");
                    final HashMap<String,String> hashMap = new HashMap<>();
                    finalFileName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            hashMap.put("Link",url);
                            finalDatabaseReference.push().setValue(hashMap);
                            fileList.clear();
                        }
                    });
                }
            });
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            Log.d("QK","IDK");
        }
    }

    private void reloadImages(){
        final LinkedList <String> keepImages = new LinkedList<>();
        FutsalHolder.getFutsal().setFutsalImages(null);
        DatabaseReference allImages = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Images");
        allImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        if(postSnapshot.child("Link").exists() && postSnapshot.child("Link")!=null){
                            String imageName = postSnapshot.child("Link").getValue().toString();
                            keepImages.add(imageName);
                        }
                    }
                    FutsalHolder.getFutsal().setFutsalImages(keepImages);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
