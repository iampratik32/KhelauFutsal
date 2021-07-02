package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khelaufutsal.Player.Player_BookingAdapter;
import com.example.khelaufutsal.Player.Player_FutsalClassHolder;
import com.example.khelaufutsal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class Futsal_AllBookedBookingFragment extends Fragment {

    SimpleDateFormat date;
    SimpleDateFormat toLongDate;
    String longDate;
    TextView chosenDate;
    LinkedList openDays;
    ViewPager viewPager;
    private int lastPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_futsal__all_booked_booking, container, false);

        date = new SimpleDateFormat("dd-MMMM");
        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        final String formattedDate = date.format(Calendar.getInstance().getTime());
        toLongDate =new SimpleDateFormat("MM/dd/yyyy");
        longDate= toLongDate.format(Calendar.getInstance().getTime());
        LinkedList toSendDate = new LinkedList();
        LinkedList toSendDays = new LinkedList();
        toSendDate.add(formattedDate);
        toSendDays.add(day);

        try {
            Date d1 = toLongDate.parse(longDate);
            Calendar tC = Calendar.getInstance();
            tC.setTime(d1);
            for(int i=0;i<14;i++){
                tC.add(Calendar.DATE,1);
                Date d2 = tC.getTime();
                toSendDate.add(date.format(d2));
                String d3 = new SimpleDateFormat("EEEE",Locale.ENGLISH).format(d2.getTime());
                toSendDays.add(d3);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        ImageView closeButton = view.findViewById(R.id.closeButton_allBooking);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        viewPager = view.findViewById(R.id.viewPager_futsalAllBooking);
        Futsal_AllPagerAdapter futsal_allPagerAdapter = new Futsal_AllPagerAdapter(getContext(),toSendDate,toSendDays);
        viewPager.setAdapter(futsal_allPagerAdapter);


        chosenDate = view.findViewById(R.id.thisDayText_futsalAllBooking);
        chosenDate.setText(formattedDate+" "+day);

        openDays = FutsalHolder.getFutsal().getFutsalOpenDays();

        final ImageView previousDay = view.findViewById(R.id.previousDayArrow_futsalAllBooking);
        ImageView nextDay = view.findViewById(R.id.nextDayArrow_futsalAllBooking);
        previousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousDaySelected(1);
            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDaySelected(1);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (lastPosition > position) {
                    previousDaySelected(0);
                }else if (lastPosition < position) {
                    nextDaySelected(0);
                }
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }

    private void previousDaySelected(int temp){
        try {
            Date dq = toLongDate.parse(longDate);
            Date checkingDate = toLongDate.parse(toLongDate.format(Calendar.getInstance().getTime()));
            long mill = dq.getTime();
            if(dq.compareTo(checkingDate)==1){
                changeCalendarText(mill,-1,temp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void nextDaySelected(int temp){
        try {
            Date dq = toLongDate.parse(longDate);
            Date checkingDate = toLongDate.parse(toLongDate.format(Calendar.getInstance().getTime()));
            Calendar checkingCalendar = Calendar.getInstance();
            checkingCalendar.setTime(checkingDate);
            checkingCalendar.add(Calendar.DATE,+15);
            Date finalCheckingDate = checkingCalendar.getTime();
            long mill = dq.getTime();
            if(dq.compareTo(finalCheckingDate)==-1){
                changeCalendarText(mill,1,temp);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void changeCalendarText(long mill, int v, int temp){
        if(temp==1){
            viewPager.setCurrentItem(viewPager.getCurrentItem() + v, false);
        }
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(mill));
            calendar.add(Calendar.DATE,v);
            Date aq = calendar.getTime();
            mill = aq.getTime();
            longDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(mill));
            String thisDay = new SimpleDateFormat("EEEE",Locale.ENGLISH).format(new Date(mill));
            chosenDate.setText(date.format(new Date(mill))+" "+thisDay);
        }
    }

}
