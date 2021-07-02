package com.example.khelaufutsal.Player;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

class FutsalGroundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Player_FutsalClass> groundList;
    private Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public FutsalGroundAdapter(List<Player_FutsalClass> groundList, Context context) {
        this.groundList = groundList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_ITEM){
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.futsal_ground_list,null,false);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(layoutParams);

            return new FutsalGroundHolder(layoutView);
        }
        else {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,null,false);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(layoutParams);

            return new LoadingViewHolder(layoutView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FutsalGroundHolder){
            populateItemRows((FutsalGroundHolder) holder,position);
        }
        else if(holder instanceof LoadingViewHolder){
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount()
    {
        return groundList==null ? 0 :groundList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return groundList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

    }

    private void populateItemRows(FutsalGroundHolder holder, int position){
        if(groundList.get(position).getFutsalVerified().equals("true")){
            holder.verificationIcon.setVisibility(View.VISIBLE);
        }
        else {
            holder.verificationIcon.setVisibility(View.GONE);
        }
        holder.futsalName.setText(groundList.get(position).getFutsalName());

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            String[] latlong =  groundList.get(position).getFutsalLocation().split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);

            LatLng latLng = new LatLng(latitude,longitude);
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            holder.futsalLocation.setText(addresses.get(0).getAddressLine(0));
            holder.latLan.setText(groundList.get(position).getFutsalLocation());
        } catch (Exception e) {
            holder.futsalLocation.setText(groundList.get(position).getFutsalLocation());
        }
        holder.futsalId.setText(groundList.get(position).getFutsalId());
        holder.futsalEmail.setText(groundList.get(position).getFutsalEmail());
        holder.futsalNumber.setText(groundList.get(position).getFutsalContact());
        if(groundList.get(position).getFutsalDisplayImageLink()!=null){
            holder.futsalDisplayImage.setText(groundList.get(position).getFutsalDisplayImageLink());
            Glide.with(context).load(Uri.parse(groundList.get(position).getFutsalDisplayImageLink())).into(holder.displayImage);
        }
        if(groundList.get(position).getNoOfImages()!=0){
            holder.numberOfImages.setText(groundList.get(position).getNoOfImages());
        }
        if(groundList.get(position).getFutsalRating()!=0.0f){
            holder.ratingBar.setRating(groundList.get(position).getFutsalRating());
        }
        if(groundList.get(position).getFutsalPrice()!=null){
            holder.futsalPrice.setText("Rs. "+groundList.get(position).getFutsalPrice());
        }

    }

    private void showLoadingView(LoadingViewHolder holder, int position) {
        //ProgressBar would be displayed

    }
}
