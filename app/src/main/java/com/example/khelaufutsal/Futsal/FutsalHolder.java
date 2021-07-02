package com.example.khelaufutsal.Futsal;

import com.example.khelaufutsal.Futsal.FutsalClass;

public class FutsalHolder {

    private static FutsalClass futsal = null;

    public static void setFutsal(FutsalClass user){
        futsal = user;
    }
    public static FutsalClass getFutsal(){
        return futsal;
    }


}
