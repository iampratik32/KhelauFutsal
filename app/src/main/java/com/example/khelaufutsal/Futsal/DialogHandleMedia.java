package com.example.khelaufutsal.Futsal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.ChooseMediaType;
import com.example.khelaufutsal.ChooseMultipleImages;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import static com.example.khelaufutsal.Futsal.Futsal_CustomizeFutsalFragment.REQUEST_CODE;

public class DialogHandleMedia extends AppCompatDialogFragment {

    ImageView firstImage,secondImage,thirdImage,fourthImage,fifthImage,sixthImage,seventhImage,eighthImage,ninthmage;
    ImageView firstAddButton,secondAddButton,thirdAddButton,fourthAddButton,fifthAddButton,sixthAddButton,seventhAddButton,eighthAddButton,ninthAddButton;
    private LinkedList checkSize = new LinkedList();
    int previousSize=0;

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    dismiss();
                    return true;
                }
                else{
                    return false;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        final LinkedList <String> keepImages = new LinkedList<>();
        int tempSize = 0;
        if(FutsalHolder.getFutsal().getFutsalImages()!=null){
            tempSize = FutsalHolder.getFutsal().getFutsalImages().size();
        }
        if(previousSize!=tempSize){
            FutsalHolder.getFutsal().setFutsalImages(null);
            DatabaseReference allImages = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Images");
            allImages.addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_futsal_choose_media,null);
        builder.setView(view);

        Button addPhotos = view.findViewById(R.id.addPhoto_handleMedia);
        CardView firstCardView = view.findViewById(R.id.firstCardView_handleMedia);
        CardView secondCardView = view.findViewById(R.id.secondCardView_handleMedia);
        CardView thirdCardView = view.findViewById(R.id.thirdCardView_handleMedia);
        CardView fourthCardView = view.findViewById(R.id.fourthCardView_handleMedia);
        CardView fifthCardView = view.findViewById(R.id.fifthCardView_handleMedia);
        CardView sixthCardView = view.findViewById(R.id.sixthCardView_handleMedia);
        CardView seventhCardView = view.findViewById(R.id.seventhCardView_handleMedia);
        CardView eighthCardView = view.findViewById(R.id.eighthCardView_handleMedia);
        CardView ninthCardView = view.findViewById(R.id.ninthCardView_handleMedia);

        firstImage = view.findViewById(R.id.handleMedia_firstPhoto);
        secondImage = view.findViewById(R.id.handleMedia_secondPhoto);
        thirdImage = view.findViewById(R.id.handleMedia_thirdPhoto);
        fourthImage = view.findViewById(R.id.handleMedia_fourthPhoto);
        fifthImage = view.findViewById(R.id.handleMedia_fifthPhoto);
        sixthImage = view.findViewById(R.id.handleMedia_sixthPhoto);
        seventhImage = view.findViewById(R.id.handleMedia_seventhPhoto);
        eighthImage = view.findViewById(R.id.handleMedia_eighthPhoto);
        ninthmage = view.findViewById(R.id.handleMedia_ninthPhoto);

        firstAddButton = view.findViewById(R.id.first_addButton_dialog);
        secondAddButton = view.findViewById(R.id.second_addButton_dialog);
        thirdAddButton = view.findViewById(R.id.third_addButton_dialog);
        fourthAddButton = view.findViewById(R.id.fourth_addButton_dialog);
        fifthAddButton = view.findViewById(R.id.fifth_addButton_dialog);
        sixthAddButton = view.findViewById(R.id.sixth_addButton_dialog);
        seventhAddButton = view.findViewById(R.id.seventh_addButton_dialog);
        eighthAddButton = view.findViewById(R.id.eighth_addButton_dialog);
        ninthAddButton = view.findViewById(R.id.ninth_addButton_dialog);

        final LoadMediaAsyncTask loadMediaAsyncTask = new LoadMediaAsyncTask();
        LinkedList userImages = FutsalHolder.getFutsal().getFutsalImages();
        if(userImages==null){
            try {
                int result = new LoadMediaAsyncTask().execute(firstImage).get();
                Log.d("REsutl",String.valueOf(result));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(loadMediaAsyncTask.getStatus()== AsyncTask.Status.FINISHED){
            addMedia();
        }
        if(userImages!=null){
            addMedia();
        }

        ImageView closeButton = view.findViewById(R.id.closeButton_handleMedia);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        if(FutsalHolder.getFutsal().getFutsalImages()!=null){
            final int noOfImages = FutsalHolder.getFutsal().getFutsalImages().size();
            final LinkedList images = FutsalHolder.getFutsal().getFutsalImages();
            firstAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noOfImages>0){
                        firstAddButton.setImageResource(R.drawable.add_button);
                        firstImage.setImageResource(R.drawable.no_photo);
                        DeleteImagesAsyncTask deleteImagesAsyncTask = new DeleteImagesAsyncTask();
                        deleteImagesAsyncTask.execute(0);
                    }
                    else {
                        chooseMedia();
                    }
                }
            });
            secondAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noOfImages>1){
                        secondAddButton.setImageResource(R.drawable.add_button);
                        secondImage.setImageResource(R.drawable.no_photo);
                        DeleteImagesAsyncTask deleteImagesAsyncTask = new DeleteImagesAsyncTask();
                        deleteImagesAsyncTask.execute(1);
                    }
                    else {
                        chooseMedia();
                    }
                }
            });
            thirdAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noOfImages>2){
                        thirdAddButton.setImageResource(R.drawable.add_button);
                        thirdImage.setImageResource(R.drawable.no_photo);
                        DeleteImagesAsyncTask deleteImagesAsyncTask = new DeleteImagesAsyncTask();
                        deleteImagesAsyncTask.execute(2);
                    }
                    else {
                        chooseMedia();
                    }
                }
            });
            fourthAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noOfImages>3){
                        fourthAddButton.setImageResource(R.drawable.add_button);
                        fourthImage.setImageResource(R.drawable.no_photo);
                        DeleteImagesAsyncTask deleteImagesAsyncTask = new DeleteImagesAsyncTask();
                        deleteImagesAsyncTask.execute(3);
                    }
                    else {
                        chooseMedia();
                    }
                }
            });
            fifthAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noOfImages>4){
                        fifthAddButton.setImageResource(R.drawable.add_button);
                        fifthImage.setImageResource(R.drawable.no_photo);
                        DeleteImagesAsyncTask deleteImagesAsyncTask = new DeleteImagesAsyncTask();
                        deleteImagesAsyncTask.execute(4);
                    }
                    else {
                        chooseMedia();
                    }
                }
            });
            sixthAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noOfImages>5){
                        sixthAddButton.setImageResource(R.drawable.add_button);
                        sixthImage.setImageResource(R.drawable.no_photo);
                        DeleteImagesAsyncTask deleteImagesAsyncTask = new DeleteImagesAsyncTask();
                        deleteImagesAsyncTask.execute(5);
                    }
                    else {
                        chooseMedia();
                    }
                }
            });
            seventhAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noOfImages>6){
                        seventhAddButton.setImageResource(R.drawable.add_button);
                        seventhImage.setImageResource(R.drawable.no_photo);
                        DeleteImagesAsyncTask deleteImagesAsyncTask = new DeleteImagesAsyncTask();
                        deleteImagesAsyncTask.execute(6);
                    }
                    else {
                        chooseMedia();
                    }
                }
            });
            eighthAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noOfImages>7){
                        eighthAddButton.setImageResource(R.drawable.add_button);
                        eighthImage.setImageResource(R.drawable.no_photo);
                        DeleteImagesAsyncTask deleteImagesAsyncTask = new DeleteImagesAsyncTask();
                        deleteImagesAsyncTask.execute(7);
                    }
                    else {
                        chooseMedia();
                    }
                }
            });
            ninthAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noOfImages>8){
                        ninthAddButton.setImageResource(R.drawable.add_button);
                        ninthmage.setImageResource(R.drawable.no_photo);
                        DeleteImagesAsyncTask deleteImagesAsyncTask = new DeleteImagesAsyncTask();
                        deleteImagesAsyncTask.execute(8);
                    }
                    else {
                        chooseMedia();
                    }
                }
            });
        }


        addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });
        firstCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });
        secondCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });
        thirdCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });
        fourthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });
        fifthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });
        sixthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });
        seventhCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });
        eighthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });
        ninthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMedia();
            }
        });

        return builder.create();

    }

    private void chooseMedia(){
        if(FutsalHolder.getFutsal().getFutsalImages()!=null && FutsalHolder.getFutsal().getFutsalImages().size()!=9){
            Intent chooseMultipleIntent = new Intent(getActivity(), ChooseMultipleImages.class);
            startActivityForResult(chooseMultipleIntent,REQUEST_CODE);
            dismiss();
        }
        else {
            if(FutsalHolder.getFutsal().getFutsalImages()!=null){
                Toast.makeText(getContext(),"Maximum Limit Reached",Toast.LENGTH_SHORT).show();
            }
            else {
                Intent chooseMultipleIntent = new Intent(getActivity(), ChooseMultipleImages.class);
                startActivityForResult(chooseMultipleIntent,REQUEST_CODE);
                dismiss();
            }
        }

    }

    private class  DeleteImagesAsyncTask extends AsyncTask<Integer, Integer, LinkedList> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(LinkedList linkedList) {
            super.onPostExecute(linkedList);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected LinkedList doInBackground(final Integer... integers) {

            final LinkedList images = FutsalHolder.getFutsal().getFutsalImages();
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(String.valueOf(images.get(integers[0])));
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    final DatabaseReference imageDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Images");
                    imageDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                    final DatabaseReference finalImage = imageDatabase.child(postSnapshot.getKey()).child("Link");
                                    finalImage.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                String imageLink = dataSnapshot.getValue().toString();
                                                if(imageLink.equals(images.get(integers[0]))){
                                                    finalImage.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                images.remove(integers[0]);
                                                            }
                                                            else {
                                                                Toast.makeText(getContext(),"Could not Delete At This Moment.",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e.getMessage().equals("Object does not exist at location.")){
                        chooseMedia();
                        FutsalHolder.getFutsal().setFutsalImages(images);
                    }
                }
            });
            return images;
        }
    }

    private class LoadMediaAsyncTask extends AsyncTask<ImageView,Integer,Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(final ImageView... imageViews) {
            final LinkedList keepImages = new LinkedList();
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
            return keepImages.size();
        }
    }

    private void addMedia(){
        if(FutsalHolder.getFutsal().getFutsalImages()!=null){
            int noOfImages = FutsalHolder.getFutsal().getFutsalImages().size();
            if(noOfImages>0){
                Glide.with(getContext()).load(Uri.parse(FutsalHolder.getFutsal().getFutsalImages().get(0))).into(firstImage);
                firstAddButton.setImageResource(R.drawable.small_close_button);
                if(noOfImages>1){
                    Glide.with(getContext()).load(Uri.parse(FutsalHolder.getFutsal().getFutsalImages().get(1))).into(secondImage);
                    secondAddButton.setImageResource(R.drawable.small_close_button);
                    if(noOfImages>2){
                        Glide.with(getContext()).load(Uri.parse(FutsalHolder.getFutsal().getFutsalImages().get(2))).into(thirdImage);
                        thirdAddButton.setImageResource(R.drawable.small_close_button);
                        if(noOfImages>3){
                            Glide.with(getContext()).load(Uri.parse(FutsalHolder.getFutsal().getFutsalImages().get(3))).into(fourthImage);
                            fourthAddButton.setImageResource(R.drawable.small_close_button);
                            if(noOfImages>4){
                                Glide.with(getContext()).load(Uri.parse(FutsalHolder.getFutsal().getFutsalImages().get(4))).into(fifthImage);
                                fifthAddButton.setImageResource(R.drawable.small_close_button);
                                if(noOfImages>5){
                                    Glide.with(getContext()).load(Uri.parse(FutsalHolder.getFutsal().getFutsalImages().get(5))).into(sixthImage);
                                    sixthAddButton.setImageResource(R.drawable.small_close_button);
                                    if(noOfImages>6){
                                        Glide.with(getContext()).load(Uri.parse(FutsalHolder.getFutsal().getFutsalImages().get(6))).into(seventhImage);
                                        seventhAddButton.setImageResource(R.drawable.small_close_button);
                                        if(noOfImages>7){
                                            Glide.with(getContext()).load(Uri.parse(FutsalHolder.getFutsal().getFutsalImages().get(7))).into(eighthImage);
                                            eighthAddButton.setImageResource(R.drawable.small_close_button);
                                            if(noOfImages>8){
                                                Glide.with(getContext()).load(Uri.parse(FutsalHolder.getFutsal().getFutsalImages().get(8))).into(ninthmage);
                                                ninthAddButton.setImageResource(R.drawable.small_close_button);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}