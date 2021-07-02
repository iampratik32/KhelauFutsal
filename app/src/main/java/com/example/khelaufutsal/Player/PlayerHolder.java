package com.example.khelaufutsal.Player;

public class PlayerHolder {

    private static PlayerClass player = null;

    public static void setPlayer(PlayerClass user){
        player = user;
    }
    public static PlayerClass getPlayer(){
        return player;
    }
}
