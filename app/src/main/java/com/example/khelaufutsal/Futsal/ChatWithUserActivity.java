package com.example.khelaufutsal.Futsal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.Notification.Client;
import com.example.khelaufutsal.Notification.Data;
import com.example.khelaufutsal.Notification.MyResponse;
import com.example.khelaufutsal.Notification.Sender;
import com.example.khelaufutsal.Notification.Token;
import com.example.khelaufutsal.Player.PlayerHolder;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatWithUserActivity extends AppCompatActivity {

    private ImageView sendButton;
    private CircleImageView userImageTop;
    private EditText messageBox;
    ChatAdapter chatAdapter;
    List<Chat> chatList;
    private String chatUserId, userType;
    RecyclerView chatRecyclerView;
    BottomSheetDialog menuBottomSheetDialog, chooseMediaBottomSheetDialog;
    LinearLayout unmatchOption, reportOption, cameraOption, galleryOption, cancelPictureOption;
    public static final int CHAT_CAMERA_REQUEST = 1984;
    public static final int CHAT_GALLERY_REQUEST = 1985;
    APIService apiService;
    Bitmap bitmap;
    private String forChatUserId, forChatUserName;
    boolean notify = false;
    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_user);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.topBar));

        Bundle getBundle = getIntent().getExtras();
        String userName = "";
        String userId = "";
        String userImage = "";
        if(getBundle!=null){
            userName = getBundle.getString("UserName");
            userId = getBundle.getString("UserId");
            chatUserId = userId;
            forChatUserName = userName;
            userImage = getBundle.getString("ImageLink");
            userType=getBundle.getString("UserType");
        }
        final String finalUserId = userId;
        chatUserId = finalUserId;
        final String finalUserName = userName;

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        ImageView chatMenu = findViewById(R.id.chat_menu);


        ImageView cameraIcon = findViewById(R.id.chooseMedia_chatActivity);
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ChatWithUserActivity.this).inflate(R.layout.dialog_chat_choose_media,null);
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
                        startActivityForResult(intent, CHAT_CAMERA_REQUEST);
                    }
                });
                galleryOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(ChatWithUserActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                            return;
                        }
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent,"Select File"),CHAT_GALLERY_REQUEST);
                    }
                });
                chooseMediaBottomSheetDialog = new BottomSheetDialog(ChatWithUserActivity.this);
                chooseMediaBottomSheetDialog.setContentView(view);
                chooseMediaBottomSheetDialog.show();
            }
        });



        userImageTop = findViewById(R.id.chatWithUser_userProfilePhoto);
        if(userImage!=null && !userImage.isEmpty()){
            Glide.with(getApplicationContext()).load(Uri.parse(userImage)).into(userImageTop);
        }
        userImageTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent userProfile = new Intent(v.getContext(),MatchesProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("UserId",finalUserId);
                bundle.putString("UserName",finalUserName);
                bundle.putString("UserGender",userGender.getText().toString());
                userProfile.putExtras(bundle);
                v.getContext().startActivity(userProfile);*/
            }
        });

        TextView userNameTextView = findViewById(R.id.chatWithUser_userName);
        userNameTextView.setText(userName);

        ImageView backButton = findViewById(R.id.chatWithUser_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

        sendButton = findViewById(R.id.chatWithUser_sendButton);
        messageBox = findViewById(R.id.chatWithUser_sendMessageEditText);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                if(!messageBox.getText().toString().trim().isEmpty()){
                    if(userType.equals("Futsal")){
                        sendMessage(FutsalHolder.getFutsal().getUserId(),finalUserId, messageBox.getText().toString(), new Date().getTime());
                    }
                    else {
                        sendMessage(PlayerHolder.getPlayer().getUserId(),finalUserId, messageBox.getText().toString(), new Date().getTime());
                    }
                    messageBox.setText("");
                }
            }
        });

        if(userType.equals("Futsal")){
            readMessages(FutsalHolder.getFutsal().getUserId(),finalUserId,userImage);
        }
        else {
            readMessages(PlayerHolder.getPlayer().getUserId(),finalUserId,userImage);
        }

        seenMessage(finalUserId);
    }

    private void seenMessage(final String userId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        seenListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Chat chat = postSnapshot.getValue(Chat.class);
                    if(userType.equals("Futsal")){
                        if(chat.getReceiver().equals(FutsalHolder.getFutsal().getUserId()) && chat.getSender().equals(userId)){
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("seen","true");
                            postSnapshot.getRef().updateChildren(hashMap);
                        }
                    }
                    else {
                        if(chat.getReceiver().equals(PlayerHolder.getPlayer().getUserId()) && chat.getSender().equals(userId)){
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("seen","true");
                            postSnapshot.getRef().updateChildren(hashMap);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message, long messageTime){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Chat chat = new Chat(sender,receiver,message,messageTime,"text","false");
        databaseReference.child("Chats").push().setValue(chat);

        final String msg = message;
        DatabaseReference databaseReference1=null;
        if(userType.equals("Futsal")){
            databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId());
        }
        else {
            databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child("Players").child(PlayerHolder.getPlayer().getUserId());
        }
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(notify){
                    if(userType.equals("Futsal")){
                        sendNotification(receiver,FutsalHolder.getFutsal().getFutsalName(),msg);
                    }
                    else {
                        sendNotification(receiver,PlayerHolder.getPlayer().getDisplayName(),msg);
                    }

                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String message){
        try{
            DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
            Query query = tokens.orderByKey().equalTo(receiver);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Token token = snapshot.getValue(Token.class);
                        Data data;
                        if(userType.equals("Futsal")){
                            data = new Data(FutsalHolder.getFutsal().getUserId(),R.drawable.football_player_icon,username+": "+message,"New Message",chatUserId);
                        }
                        else {
                            data = new Data(PlayerHolder.getPlayer().getUserId(),R.drawable.football_player_icon,username+": "+message,"New Message",chatUserId);
                        }

                        Sender sender = new Sender(data,token.getToken());
                        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if(response.code()==200){
                                    if(response.body().success!=1){
                                        Toast.makeText(ChatWithUserActivity.this,"Failed",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (IllegalArgumentException e){

        }
    }

    private void readMessages(final String userId, final String senderId, final String imageUrl){
        chatList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    try{
                        Chat chat = postSnapshot.getValue(Chat.class);
                        if(chat!=null){
                            if(chat.getReceiver().equals(userId) && chat.getSender().equals(senderId) || chat.getReceiver().equals(senderId) && chat.getSender().equals(userId)){
                                chatList.add(chat);
                            }
                        }
                    }
                    catch (DatabaseException e){

                    }

                }
                chatAdapter = new ChatAdapter(ChatWithUserActivity.this,chatList,imageUrl,userType);
                chatRecyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        try {
            super.onActivityResult(requestCode,resultCode,data);
            if(requestCode==CHAT_GALLERY_REQUEST){
                if(data!=null){
                    Uri selectedUri = data.getData();
                    try{
                        bitmap = MediaStore.Images.Media.getBitmap(ChatWithUserActivity.this.getContentResolver(), selectedUri);
                        chooseMediaBottomSheetDialog.dismiss();
                        sendImage(bitmap);
                    }
                    catch (IOException e){

                    }
                }
            }
            else if(requestCode==CHAT_CAMERA_REQUEST){
                if(data!=null){
                    bitmap = (Bitmap)data.getExtras().get("data");
                    chooseMediaBottomSheetDialog.dismiss();
                    sendImage(bitmap);
                }
            }
        }
        catch (NullPointerException e ){

        }
    }
    private void sendImage(Bitmap bitmap){
        notify = true;
        String fileName = "ChatImages/"+"post_"+new Date().getTime();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,baos);
        byte[] byteData = baos.toByteArray();
        final StorageReference finalFileName = FirebaseStorage.getInstance().getReference().child(fileName);
        finalFileName.putBytes(byteData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                finalFileName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        DatabaseReference finalDatabase = FirebaseDatabase.getInstance().getReference().child("Chats");
                        HashMap<String,Object> hashMap = new HashMap<>();
                        if(userType.equals("Futsal")){
                            hashMap.put("sender",FutsalHolder.getFutsal().getUserId());
                        }
                        else {
                            hashMap.put("sender",PlayerHolder.getPlayer().getUserId());
                        }
                        hashMap.put("receiver",chatUserId);
                        hashMap.put("message",url);
                        hashMap.put("messageTime",new Date().getTime());
                        hashMap.put("type","image");
                        hashMap.put("seen","false");
                        finalDatabase.push().setValue(hashMap);

                        if(userType.equals("Futsal")){
                            DatabaseReference getDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId());
                            getDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(notify){
                                        sendNotification(forChatUserId,forChatUserName,"Sent You a Photo.");
                                    }
                                    notify = false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            DatabaseReference getDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId());
                            getDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(notify){
                                        sendNotification(forChatUserId,forChatUserName,"Sent You a Photo.");
                                    }
                                    notify = false;
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
    }

    private void currentUser(String userId){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
        editor.putString("currentuser",userId);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser(chatUserId);
    }

    @Override
    protected void onPause() {
        if(userType.equals("Futsal")){
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId());
            databaseReference.removeEventListener(seenListener);
            currentUser("");
        }
        else {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(PlayerHolder.getPlayer().getUserId());
            databaseReference.removeEventListener(seenListener);
            currentUser("");
        }
        super.onPause();

    }
}