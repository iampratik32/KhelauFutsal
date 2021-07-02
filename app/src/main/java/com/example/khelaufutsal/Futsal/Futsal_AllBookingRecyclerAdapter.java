package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khelaufutsal.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Futsal_AllBookingRecyclerAdapter extends RecyclerView.Adapter<Futsal_AllBookingHolder> {

    private Context context;
    private String date;
    private LinkedList linkedList;
    String timeText;
    String thisStatus;

    public Futsal_AllBookingRecyclerAdapter(Context context,String date) {
        this.context = context;
        this.date = date;
    }

    @NonNull
    @Override
    public Futsal_AllBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.futsal_all_booking_list,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        Futsal_AllBookingHolder holder = new Futsal_AllBookingHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Futsal_AllBookingHolder holder, int position) {

        timeText = (FutsalHolder.getFutsal().getFutsalOpenTime().get(position))+"-"+FutsalHolder.getFutsal().getFutsalOpenTime().get(position+1);
        holder.timeTextView.setText(timeText);
        holder.statusTextView.setText("Not Taken");
        holder.dateTextView.setText(String.valueOf(date));

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM");
            Date thisDate = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
            String date = dateFormat.format(thisDate);
            Log.d("hq", holder.dateTextView.getText().toString());
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
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LinkedList<BookingClass> bookings = FutsalHolder.getFutsal().getFutsalBooking();
        if(bookings!=null){
            for(int i=0;i<bookings.size();i++){
                if(bookings.get(i).getDate().equals(date) && bookings.get(i).getTime().equals(timeText)){
                    if(bookings.get(i).getConfirmed().equals("False")){
                        holder.statusTextView.setText("Pending");
                    }
                    else if(bookings.get(i).getBooked().equals("True")){
                        holder.statusTextView.setText("Booked");
                    }
                }
            }
        }
        if(holder.statusTextView.getText().equals("Booked") || holder.statusTextView.getText().equals("Pending")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFF59D"));
        }

    }

    @Override
    public int getItemCount() {
        return FutsalHolder.getFutsal().getFutsalOpenTime().size()-1;
    }

}
