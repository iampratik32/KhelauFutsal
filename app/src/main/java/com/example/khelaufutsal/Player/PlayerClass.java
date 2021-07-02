package com.example.khelaufutsal.Player;

import com.example.khelaufutsal.Futsal.BookingClass;
import com.example.khelaufutsal.Futsal.Chat_PotentialChatUser;
import com.google.firebase.auth.AuthCredential;

import java.util.ArrayList;
import java.util.LinkedList;

public class PlayerClass {
    private String userId;
    private String userEmail;
    private String contactNumber;
    private String displayName;
    private String userName;
    private String displayPicture;
    private AuthCredential credential;

    public AuthCredential getCredential() {
        return credential;
    }

    public void setCredential(AuthCredential credential) {
        this.credential = credential;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }

    private LinkedList<BookingClass> playerBookings;
    private LinkedList<BookingClass> playerHistory;
    private ArrayList<Chat_PotentialChatUser> chatUsers;

    public ArrayList<Chat_PotentialChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(ArrayList<Chat_PotentialChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public LinkedList<BookingClass> getPlayerBookings() {
        return playerBookings;
    }

    public void setPlayerBookings(LinkedList<BookingClass> playerBookings) {
        this.playerBookings = playerBookings;
    }

    public LinkedList<BookingClass> getPlayerHistory() {
        return playerHistory;
    }

    public void setPlayerHistory(LinkedList<BookingClass> playerHistory) {
        this.playerHistory = playerHistory;
    }

    public PlayerClass(String userId, String userEmail, String contactNumber, String displayName, String userName) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.contactNumber = contactNumber;
        this.displayName = displayName;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
