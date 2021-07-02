package com.example.khelaufutsal.Player;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.Futsal.BookingClass;
import com.example.khelaufutsal.Futsal.FutsalHolder;
import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;


public class FutsalBookingRecyclerAdapter extends RecyclerView.Adapter<FutsalBookingHolder> {
    private Context context;
    private String date;
    String timeText;
    String thisStatus;


    public FutsalBookingRecyclerAdapter(Context context,String date) {
        this.context = context;
        this.date = date;
    }

    @NonNull
    @Override
    public FutsalBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_futsal_booking_list,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        FutsalBookingHolder holder = new FutsalBookingHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FutsalBookingHolder holder, int position) {
        timeText = (Player_FutsalClassHolder.getFutsal().getFutsalTiming().get(position))+"-"+Player_FutsalClassHolder.getFutsal().getFutsalTiming().get(position+1);
        holder.timeTextView.setText(timeText);
        holder.statusTextView.setText("Free");
        holder.dateTextView.setText(String.valueOf(date));
        GetStatus getStatus = new GetStatus();
        getStatus.execute(holder);

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM");
            Date thisDate = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
            String date = dateFormat.format(thisDate);
            if(holder.dateTextView.getText().equals(date)){
                String hour = holder.timeTextView.getText().toString();
                String futTime ="";
                for(int i=0;i<hour.length();i++){
                    if(String.valueOf(hour.charAt(i)).equals(":")){
                        break;
                    }
                    futTime = futTime+hour.charAt(i);
                }
                DateFormat hourFormat = new SimpleDateFormat("HH");
                String thisHour = hourFormat.format(Calendar.getInstance().getTime());
                if(Integer.parseInt(thisHour)>Integer.parseInt(futTime)) {
                    holder.statusTextView.setText("Already Played");
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#0277bd"));

                }
                else if(Integer.parseInt(thisHour)==Integer.parseInt(futTime)){
                    holder.statusTextView.setText("In Progress");
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#4db6ac"));
                }
                else {
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#FFF59D"));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Player_FutsalClassHolder.getFutsal().getFutsalTiming().size()-1;
    }

    private class GetStatus extends AsyncTask<FutsalBookingHolder,Integer,Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected Integer doInBackground(final FutsalBookingHolder... holders) {
            readData(new onGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    holders[0].statusTextView.setText(thisStatus);
                    holders[0].cardView.setCardBackgroundColor(Color.parseColor("#757575"));
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFailure() {

                }
            },timeText);
            return null;
        }
    }
    public interface onGetDataListener{
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }

    public void readData(final FutsalBookingRecyclerAdapter.onGetDataListener listener, final String time){
        listener.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking").child(Player_FutsalClassHolder.getFutsal().getFutsalId()).child(date)
                .child(time);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String confirmed = dataSnapshot.child("Confirmed").getValue().toString();
                    if(confirmed.equals("False")){
                        thisStatus = "Pending";
                    }
                    else {
                        thisStatus = "Booked";
                    }
                    listener.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
