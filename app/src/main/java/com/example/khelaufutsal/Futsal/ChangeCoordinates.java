package com.example.khelaufutsal.Futsal;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChangeCoordinates {


    public static String changeToText(String[] latlong, Context context){

        LatLng latLngUser;
        String result="";
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        latLngUser = new LatLng(latitude,longitude);

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            result = addresses.get(0).getAddressLine(0).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
