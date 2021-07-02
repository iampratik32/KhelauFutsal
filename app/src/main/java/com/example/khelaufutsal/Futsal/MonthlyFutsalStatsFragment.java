package com.example.khelaufutsal.Futsal;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static java.lang.Math.toIntExact;


public class MonthlyFutsalStatsFragment extends Fragment {

    int jan=0,feb=0,mar=0,apr=0,may=0,jun=0,jul=0,aug=0,sep=0,oct=0,nov=0,dec=0;
    int [] months = new int[12];
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_monthly_futsal_stats, container, false);

        progressBar = view.findViewById(R.id.progressBar_monthlyStats);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("History").child(FutsalHolder.getFutsal().getUserId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        String date = postSnapshot.getKey();
                        String[] dates =  date.split("-");
                        if(dates[1].equals("January")){
                            jan=jan+ toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("February")){
                            feb=feb+toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("March")){
                            mar=mar+ toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("April")){
                            apr=apr+ toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("May")){
                            may=may+toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("June")){
                            jun=jun+toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("July")){
                            jul=jul+ toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("August")){
                            aug=aug+toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("September")){
                            sep=sep+ toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("October")){
                            oct=oct+toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("November")){
                            nov=nov+ toIntExact(postSnapshot.getChildrenCount());
                        }
                        if(dates[1].equals("December")){
                            dec=dec+toIntExact(postSnapshot.getChildrenCount());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                months[0]=jan;
                months[1]=feb;
                months[2]=mar;
                months[3]=apr;
                months[4]=may;
                months[5]=jun;
                months[6]=jul;
                months[7]=aug;
                months[8]=sep;
                months[9]=oct;
                months[10]=nov;
                months[11]=dec;
                GraphView lineGraph = view.findViewById(R.id.monthlyStatsGraph);
                LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(1, months[0]),
                        new DataPoint(2, months[1]),
                        new DataPoint(3, months[2]),
                        new DataPoint(4, months[3]),
                        new DataPoint(5, months[4]),
                        new DataPoint(6, months[5]),
                        new DataPoint(7, months[6]),
                        new DataPoint(8, months[7]),
                        new DataPoint(9, months[8]),
                        new DataPoint(10, months[9]),
                        new DataPoint(11, months[10]),
                        new DataPoint(12, months[11])
                });
                lineSeries.setAnimated(true);
                lineSeries.setTitle("Futsal Monthly Booked");
                lineGraph.addSeries(lineSeries);
                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(lineGraph);
                staticLabelsFormatter.setHorizontalLabels(new String[] {"Jan", "Feb", "Mar","Apr","May","Jun","July","Aug","Sep","Oct","Nov","Dec"});
                lineGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                progressBar.setVisibility(View.GONE);
                lineGraph.setVisibility(View.VISIBLE);
            }
        }.start();

        return view;
    }
}
