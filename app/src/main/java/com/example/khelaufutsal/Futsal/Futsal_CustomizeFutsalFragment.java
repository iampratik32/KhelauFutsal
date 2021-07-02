package com.example.khelaufutsal.Futsal;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dpro.widgets.WeekdaysPicker;
import com.example.khelaufutsal.Player.Player_FutsalClass;
import com.example.khelaufutsal.Player.Player_GroundFragment;
import com.example.khelaufutsal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;


public class Futsal_CustomizeFutsalFragment extends Fragment {
    public static final int REQUEST_CODE = 10001;

    String openTime="";
    String closeTime="";
    private DatabaseReference futsalDatabase;
    private Spinner openingTime;
    private Spinner closingTime;
    private WeekdaysPicker weekdaysPicker;
    private TextInputEditText priceEditText;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView extraRecyclerView;
    View view;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> extraThings = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_futsal__customize_futsal, container, false);

        loadFutsalExtras();

        ImageView backButton = view.findViewById(R.id.backButton_futsalAdditionalInfoPage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        futsalDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId());
        openingTime = view.findViewById(R.id.openingTimeSpinner_futsalAdditionalInfo);
        final ArrayAdapter<String> openingAdapter;
        final ArrayAdapter<String> closingAdapter;
        String[] myResArray = getResources().getStringArray(R.array.time);
        List<String>  arrayList = Arrays.asList(myResArray);
        openingAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_layout,arrayList){
            @Override
            public boolean isEnabled(int position) {
                if(position==24){
                    return false;
                }
                else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View spinnerView = super.getDropDownView(position, convertView, parent);
                TextView spinnerTextView = (TextView) spinnerView;
                if(position==24){
                    spinnerTextView.setTextColor(Color.parseColor("#bf360c"));
                }
                else {
                    spinnerTextView.setTextColor(Color.BLACK);
                }
                return spinnerView;
            }
        };
        closingAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_layout,arrayList){
            @Override
            public boolean isEnabled(int position) {
                if(openingTime.getSelectedItemPosition()>=position){
                    return false;
                }
                else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View spinnerView = super.getDropDownView(position, convertView, parent);
                TextView spinnerTextView = (TextView) spinnerView;

                if(openingTime.getSelectedItemPosition()>=position){
                    spinnerTextView.setTextColor(Color.parseColor("#bf360c"));
                }
                else {
                    spinnerTextView.setTextColor(Color.BLACK);
                }
                return spinnerView;

            }
        };
        openingAdapter.setDropDownViewResource(R.layout.spinner_layout);
        closingAdapter.setDropDownViewResource(R.layout.spinner_layout);
        closingTime = view.findViewById(R.id.closingTimeSpinner_futsalAdditionalInfo);
        openingTime.setAdapter(openingAdapter);
        closingTime.setAdapter(closingAdapter);

        openingTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>=closingTime.getSelectedItemPosition()){
                    closingTime.setSelection(position+1);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        if(FutsalHolder.getFutsal().getFutsalOpenTime()!=null){
            String tempOpenTime = FutsalHolder.getFutsal().getFutsalOpenTime().getFirst().toString();
            String tempCloseTime = FutsalHolder.getFutsal().getFutsalOpenTime().getLast().toString();

            for(int i=0;i<tempOpenTime.length();i++){
                if(String.valueOf(tempOpenTime.charAt(i)).equals(":")){
                    break;
                }
                openTime=openTime+tempOpenTime.charAt(i);
            }
            for(int i=0;i<tempCloseTime.length();i++){
                if(String.valueOf(tempCloseTime.charAt(i)).equals(":")){
                    break;
                }
                closeTime=closeTime+tempCloseTime.charAt(i);
            }

            openingTime.post(new Runnable() {
                @Override
                public void run() {
                    openingTime.setSelection(Integer.parseInt(openTime));
                }
            });

            closingTime.post(new Runnable() {
                @Override
                public void run() {
                    closingTime.setSelection(Integer.parseInt(closeTime));
                }
            });

        }


        Button uploadPhotoButton = view.findViewById(R.id.choosePhoto_additionalInformationFutsal);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHandleMedia dialog = new DialogHandleMedia();
                dialog.show(getFragmentManager(),"Dialog");
            }
        });

        weekdaysPicker = view.findViewById(R.id.weekdays_additionalInfoFutsal);

        LinkedHashMap<Integer,Boolean> daysMap = new LinkedHashMap<>();

        LinkedList<String> openDays = FutsalHolder.getFutsal().getFutsalOpenDays();
        if(openDays.contains("Sunday")){
            daysMap.put(SUNDAY,true);
        }
        else {
            daysMap.put(SUNDAY,false);
        }
        if(openDays.contains("Monday")){
            daysMap.put(MONDAY,true);
        }
        else {
            daysMap.put(MONDAY,false);
        }
        if(openDays.contains("Tuesday")){
            daysMap.put(TUESDAY,true);
        }
        else {
            daysMap.put(TUESDAY,false);
        }
        if(openDays.contains("Wednesday")){
            daysMap.put(WEDNESDAY,true);
        }
        else {
            daysMap.put(WEDNESDAY,false);
        }
        if(openDays.contains("Thursday")){
            daysMap.put(THURSDAY,true);
        }
        else {
            daysMap.put(THURSDAY,false);
        }
        if(openDays.contains("Friday")){
            daysMap.put(FRIDAY,true);
        }
        else {
            daysMap.put(FRIDAY,false);
        }
        if (openDays.contains("Saturday")){
            daysMap.put(SATURDAY,true);
        }
        else {
            daysMap.put(SATURDAY,false);
        }
        weekdaysPicker.setCustomDays(daysMap);

        priceEditText = view.findViewById(R.id.futsalPrice_customizeFutsal);

        if(FutsalHolder.getFutsal().getFutsalPrice()!=null){
            priceEditText.setText(FutsalHolder.getFutsal().getFutsalPrice());
        }
        else {
            priceEditText.setHint("Futsal Price");
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        futsalDatabase.child("Timing").child("OpeningTime").setValue(openingTime.getSelectedItem().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                futsalDatabase.child("Timing").child("ClosingTime").setValue(closingTime.getSelectedItem().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        LoadCustomizableData.keepTiming(openingTime.getSelectedItem().toString(),closingTime.getSelectedItem().toString());
                    }
                });
            }
        });

        final List<String> selectedDays = weekdaysPicker.getSelectedDaysText();
        final LinkedHashMap<String,String> daysMap = new LinkedHashMap<>();
        for(int i=0; i<selectedDays.size();i++){
            daysMap.put(selectedDays.get(i),"true");
        }
        futsalDatabase.child("Timing").child("OpenDays").setValue(daysMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                LinkedList linkedList = new LinkedList();
                for(int i=0;i<selectedDays.size();i++){
                    linkedList.add(selectedDays.get(i));
                }
                FutsalHolder.getFutsal().setFutsalOpenDays(linkedList);
            }
        });

        if(!priceEditText.getText().toString().equals("")){
            futsalDatabase.child("Price").setValue(priceEditText.getText().toString().trim());
            FutsalHolder.getFutsal().setFutsalPrice(priceEditText.getText().toString().trim());
        }
    }

    private List<String> getExtras(){
        return extraThings;
    }

    private void loadFutsalExtras(){
        readAllExtras(new OnGetDataListener() {
            @Override
            public void onSuccess(ArrayList<String> allExtras) {
                extraRecyclerView = view.findViewById(R.id.recyclerView_customizeFutsal);
                layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                extraRecyclerView.setLayoutManager(layoutManager);
                adapter = new FutsalExtraAdapter(getExtras(),getContext());
                extraRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    public interface OnGetDataListener{
        void onSuccess(ArrayList<String> allExtras);
        void onStart();
        void onFailure();
    }

    public void readAllExtras(final Futsal_CustomizeFutsalFragment.OnGetDataListener listener){
        listener.onStart();
        final DatabaseReference extrasDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Futsal").child(FutsalHolder.getFutsal().getUserId()).child("Extras");
        extrasDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot post:dataSnapshot.getChildren()){
                        extraThings.add(post.getKey());
                    }
                    listener.onSuccess(extraThings);
                }
                else {
                    extraThings.add("NullValue");
                    listener.onSuccess(extraThings);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }
}
