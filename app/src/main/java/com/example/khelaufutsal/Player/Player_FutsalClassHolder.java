package com.example.khelaufutsal.Player;

public class Player_FutsalClassHolder {

    private static Player_FutsalClass futsalClass = null;

    public static void setFutsal(Player_FutsalClass futsal){
        futsalClass = futsal;
    }
    public static Player_FutsalClass getFutsal(){
        return futsalClass;
    }

}
