package com.example.khelaufutsal.Player;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class Player_GroundDetailAdapter extends PagerAdapter {

    private Context context;
    private int noOfImages;
    private LayoutInflater layoutInflater;
    private String futsalId;
    private LinkedList listOfMedia;
    private int check =0;
    private LinkedList checkSize = new LinkedList();

    public Player_GroundDetailAdapter(Context context, int noOfImages, String futsalId) {
        this.context = context;
        this.noOfImages = noOfImages;
        this.futsalId = futsalId;
    }

    @Override
    public int getCount() {
        return noOfImages;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.player_ground_detail_slide_layout,null,false);
        ImageView images = view.findViewById(R.id.extraInformation_slideImage);
        listOfMedia = new LinkedList();
        LoadImagesAsyncTask loadImagesAsyncTask = new LoadImagesAsyncTask();
        loadImagesAsyncTask.execute(images);
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }

    private class LoadImagesAsyncTask extends AsyncTask<ImageView,Integer,LinkedList> {
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
        protected LinkedList doInBackground(final ImageView... imageViews) {
            DatabaseReference imagesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(futsalId).child("Images");
            imagesDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            if(postSnapshot.child("Link").exists() && postSnapshot.child("Link")!=null){
                                String imageName = postSnapshot.child("Link").getValue().toString();
                                if(!checkSize.contains(imageName)){
                                    checkSize.add(imageName);
                                    Glide.with(context).load(Uri.parse(imageName)).into(imageViews[0]);
                                    break;
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
}
