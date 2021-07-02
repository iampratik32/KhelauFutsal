package com.example.khelaufutsal.Futsal;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LoadFutsalInformation extends AsyncTask<Integer,Integer,Integer> {

    LinkedList<BookingClass> listOfBooking;
    LinkedList<BookingClass> listOfHistory;
    LinkedList<BookingClass> toAddHistory;
    SimpleDateFormat date;
    SimpleDateFormat toLongDate;
    Date checkingDate;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    @Override
    protected Integer doInBackground(Integer... integers) {

        readData(new onGetDataListener() {
            @Override
            public void onSuccess(FutsalClass futsalClass) {
                getBookings(new onGetDataListener() {
                    @Override
                    public void onSuccess(FutsalClass futsalClass) {
                        getHistory(new onGetDataListener() {
                            @Override
                            public void onSuccess(FutsalClass futsalClass) {
                                DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("History").child(FutsalHolder.getFutsal().getUserId());
                                if (listOfHistory.size() == 0) {
                                    toAddHistory = new LinkedList<>();
                                    historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                    for (DataSnapshot post : postSnapshot.getChildren()) {
                                                        BookingClass booking = new BookingClass(FutsalHolder.getFutsal().getUserId(), postSnapshot.getKey(), post.getKey(), post.child("Booked").getValue().toString()
                                                                , "", post.child("Taken").getValue().toString());
                                                        toAddHistory.add(booking);
                                                    }
                                                }
                                                FutsalHolder.getFutsal().setFutsalHistory(toAddHistory);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailure() {

                    }
                });

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });

        return null;
    }

    public interface onGetDataListener {
        void onSuccess(FutsalClass futsalClass);

        void onStart();

        void onFailure();
    }

    public void readData(final LoadFutsalInformation.onGetDataListener listener) {
        listener.onStart();

        final String userId = FirebaseAuth.getInstance().getUid();
        final DatabaseReference futsalDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(userId);
        futsalDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final FutsalClass futsalClass = new FutsalClass(userId, dataSnapshot.child("FutsalContact").getValue().toString(), dataSnapshot.child("FutsalLocation").getValue().toString()
                            , dataSnapshot.child("FutsalName").getValue().toString(), dataSnapshot.child("OwnerName").getValue().toString(), dataSnapshot.child("PersonalContact").getValue().toString()
                            , dataSnapshot.child("PersonalEmail").getValue().toString(), dataSnapshot.child("Verified").getValue().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), dataSnapshot.child("ShowUs").getValue().toString());

                    if (dataSnapshot.child("VerificationRequest").exists()) {
                        futsalClass.setVerificationRequest(dataSnapshot.child("VerificationRequest").getValue().toString());
                    }

                    if(dataSnapshot.child("DisplayPicture").exists()){
                        futsalClass.setDisplayPicture(dataSnapshot.child("DisplayPicture").getValue().toString());
                    }

                    if(dataSnapshot.child("Ratings").exists()){
                        float totalRating = Float.parseFloat(dataSnapshot.child("Ratings").child("Rating").getValue().toString());
                        int totalUser = Integer.parseInt(dataSnapshot.child("Ratings").child("TotalUsers").getValue().toString());
                        futsalClass.setFutsalRating(totalRating/totalUser);
                    }
                    else {
                        futsalClass.setFutsalRating(0.0f);
                    }

                    if(dataSnapshot.child("Price").exists()){
                        futsalClass.setFutsalPrice(dataSnapshot.child("Price").getValue().toString());
                    }

                    LoadCustomizableData.loadTiming(futsalDatabaseReference);
                    LoadCustomizableData.loadOpenDays(futsalDatabaseReference);
                    LoadCustomizableData.loadImages(futsalDatabaseReference);

                    FutsalHolder.setFutsal(futsalClass);
                    listener.onSuccess(futsalClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getBookings(final LoadFutsalInformation.onGetDataListener listener) {
        listener.onStart();
        try {
            date = new SimpleDateFormat("dd-MMMM");
            toLongDate = new SimpleDateFormat("MM/dd/yyyy");
            checkingDate = date.parse(date.format(Calendar.getInstance().getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId());
        listOfBooking = new LinkedList<>();
        listOfHistory = new LinkedList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot post : postSnapshot.getChildren()) {
                            BookingClass booking = new BookingClass(FutsalHolder.getFutsal().getUserId(), postSnapshot.getKey(), post.getKey(), post.child("Booked").getValue().toString()
                                    , post.child("Confirmed").getValue().toString(), post.child("Taken").getValue().toString());
                            try {
                                Date dDate = date.parse(booking.getDate());
                                if (checkingDate.compareTo(dDate) == 1) {
                                    listOfHistory.add(booking);
                                } else if (checkingDate.compareTo(dDate) == 0) {
                                    String hour = booking.getTime();
                                    String futTime = "";
                                    for (int i = 0; i < hour.length(); i++) {
                                        if (String.valueOf(hour.charAt(i)).equals(":")) {
                                            break;
                                        }
                                        futTime = futTime + hour.charAt(i);
                                    }
                                    DateFormat hourFormat = new SimpleDateFormat("HH");
                                    String thisHour = hourFormat.format(Calendar.getInstance().getTime());
                                    if (Integer.parseInt(thisHour) >= Integer.parseInt(futTime)) {
                                        listOfHistory.add(booking);
                                    } else {
                                        listOfBooking.add(booking);
                                    }
                                } else {
                                    listOfBooking.add(booking);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    FutsalHolder.getFutsal().setFutsalBooking(listOfBooking);
                    FutsalHolder.getFutsal().setFutsalHistory(listOfHistory);
                    listener.onSuccess(FutsalHolder.getFutsal());
                } else {
                    listener.onSuccess(FutsalHolder.getFutsal());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getHistory(LoadFutsalInformation.onGetDataListener listener) {
        listener.onStart();

        if (listOfHistory.size() == 0) {
            listener.onSuccess(FutsalHolder.getFutsal());
        } else if (listOfBooking.size() == 0) {
            listener.onSuccess(FutsalHolder.getFutsal());
        } else {
            for (int i = 0; i < listOfHistory.size(); i++) {
                final DatabaseReference removeBookings = FirebaseDatabase.getInstance().getReference().child("Booking").child(FutsalHolder.getFutsal().getUserId()).child(listOfHistory.get(i).getDate());
                final int tempVar = i;
                removeBookings.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            removeBookings.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference().child("History").child(FutsalHolder.getFutsal().getUserId())
                                                .child(listOfHistory.get(tempVar).getDate()).child(listOfHistory.get(tempVar).getTime());
                                        Map historyMap = new HashMap();
                                        historyMap.put("Booked", listOfHistory.get(tempVar).getBooked());
                                        historyMap.put("Taken", listOfHistory.get(tempVar).getUserId());
                                        historyReference.setValue(historyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                final DatabaseReference forRatingDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Players").child(listOfHistory.get(tempVar).getUserId());
                                                forRatingDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()){
                                                            forRatingDb.child("ToRate").setValue(FutsalHolder.getFutsal().getUserId());
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                listener.onSuccess(FutsalHolder.getFutsal());
            }
        }
    }
}
