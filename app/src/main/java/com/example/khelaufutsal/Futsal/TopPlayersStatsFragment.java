package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khelaufutsal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TopPlayersStatsFragment extends Fragment {

    ProgressBar progressBar;
    Map<String,Integer> countMap = new HashMap<>();
    LinkedList<Integer> revenue = new LinkedList();

    int totalCount=0;
    int totalEarned = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_top_players_stats, container, false);

        progressBar = view.findViewById(R.id.progressBar_topPlayersStats);

        final int price = Integer.parseInt(FutsalHolder.getFutsal().getFutsalPrice());
        Calendar calendar = Calendar.getInstance();
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("History").child(FutsalHolder.getFutsal().getUserId());
        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot post:dataSnapshot.getChildren()){
                        String[] date =  post.getKey().split("-");
                        if(!countMap.containsKey(date[1])){
                            countMap.put(date[1],(int)post.getChildrenCount());
                        }
                        else {
                            int prev = countMap.get(date[1]);
                            int n = prev+(int) post.getChildrenCount();
                            countMap.remove(date[1]);
                            countMap.put(date[1],n);
                        }
                        totalCount=totalCount+(int)post.getChildrenCount();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        CountDownTimer countDownTimer = new CountDownTimer(2500,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(countMap.get("January")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("January"));
                }
                if(countMap.get("February")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("February"));
                }
                if(countMap.get("March")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("March"));
                }
                if(countMap.get("April")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("April"));
                }
                if(countMap.get("May")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("May"));
                }
                if(countMap.get("June")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("June"));
                }
                if(countMap.get("July")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("July"));
                }
                if(countMap.get("August")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("August"));
                }
                if(countMap.get("September")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("September"));
                }
                if(countMap.get("October")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("October"));
                }
                if(countMap.get("November")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("November"));
                }
                if(countMap.get("December")==null){
                    revenue.add(0);
                }
                else {
                    revenue.add(price*countMap.get("December"));
                }
                GraphView graphView = view.findViewById(R.id.topPlayersStatsGraph);
                BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(new DataPoint[] {
                        new DataPoint(1, revenue.get(0)),
                        new DataPoint(2, revenue.get(1)),
                        new DataPoint(3, revenue.get(2)),
                        new DataPoint(4, revenue.get(3)),
                        new DataPoint(5, revenue.get(4)),
                        new DataPoint(6, revenue.get(5)),
                        new DataPoint(7, revenue.get(6)),
                        new DataPoint(8, revenue.get(7)),
                        new DataPoint(9, revenue.get(8)),
                        new DataPoint(10, revenue.get(9)),
                        new DataPoint(11, revenue.get(10)),
                        new DataPoint(12, revenue.get(11)),
                });
                barGraphSeries.setSpacing(20);
                barGraphSeries.setDrawValuesOnTop(true);
                barGraphSeries.setValuesOnTopColor(R.color.materialBlack);

                barGraphSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Log.d("D",dataPoint.toString());
                        //TODO RECYCLER VIEW MA DETAIL OF MONTH
                    }
                });
                graphView.addSeries(barGraphSeries);
                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
                staticLabelsFormatter.setHorizontalLabels(new String[] {"Jan", "Feb", "Mar","Apr","May","Jun","July","Aug","Sep","Oct","Nov","Dec"});
                graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


                progressBar.setVisibility(View.GONE);
                graphView.setVisibility(View.VISIBLE);
            }
        }.start();

        return view;
    }


}
