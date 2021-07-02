package com.example.khelaufutsal.Futsal;

import com.google.firebase.auth.AuthCredential;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class FutsalClass {

    private String userId;
    private String topUserName;

    public String getTopUserName() {
        return topUserName;
    }

    public void setTopUserName(String topUserName) {
        this.topUserName = topUserName;
    }

    private String futsalContact;
    private String futsalLocation;
    private String futsalName;
    private String ownerName;
    private String personalContact;
    private String personalEmail;
    private String verified;
    private String futsalEmail;
    private String showFutsal;
    private String futsalPrice;
    private LinkedList futsalOpenDays;
    private String verificationRequest;
    private String displayPicture;
    private float futsalRating;
    private ArrayList<Chat_PotentialChatUser> chatUsers;
    private AuthCredential credential;

    public AuthCredential getCredential() {
        return credential;
    }

    public void setCredential(AuthCredential credential) {
        this.credential = credential;
    }

    public String getFutsalPrice() {
        return futsalPrice;
    }

    public ArrayList<Chat_PotentialChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(ArrayList<Chat_PotentialChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public float getFutsalRating() {
        return futsalRating;
    }

    public void setFutsalRating(float futsalRating) {
        this.futsalRating = futsalRating;
    }

    public void setFutsalPrice(String futsalPrice) {
        this.futsalPrice = futsalPrice;
    }

    private LinkedList<String> futsalImages;
    private LinkedList futsalOpenTime;
    private LinkedList<BookingClass> futsalBooking;
    private LinkedList<BookingClass> futsalHistory;

    public FutsalClass(String userId, String futsalContact, String futsalLocation, String futsalName, String ownerName, String personalContact, String personalEmail, String verified, String futsalEmail, String showFutsal) {
        this.userId = userId;
        this.futsalContact = futsalContact;
        this.futsalLocation = futsalLocation;
        this.futsalName = futsalName;
        this.ownerName = ownerName;
        this.personalContact = personalContact;
        this.personalEmail = personalEmail;
        this.verified = verified;
        this.futsalEmail = futsalEmail;
        this.showFutsal = showFutsal;
    }

    public String getVerificationRequest() {
        return verificationRequest;
    }

    public void setVerificationRequest(String verificationRequest) {
        this.verificationRequest = verificationRequest;
    }

    public LinkedList<BookingClass> getFutsalHistory() {
        return futsalHistory;
    }

    public void setFutsalHistory(LinkedList<BookingClass> futsalHistory) {
        this.futsalHistory = futsalHistory;
    }

    public LinkedList<BookingClass> getFutsalBooking() {
        return futsalBooking;
    }

    public void setFutsalBooking(LinkedList<BookingClass> futsalBooking) {
        this.futsalBooking = futsalBooking;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }

    public LinkedList<String> getFutsalImages() {
        return futsalImages;
    }

    public void setFutsalImages(LinkedList<String> futsalImages) {
        this.futsalImages = futsalImages;
    }

    public String getShowFutsal() {
        return showFutsal;
    }

    public void setShowFutsal(String showFutsal) {
        this.showFutsal = showFutsal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFutsalContact() {
        return futsalContact;
    }

    public void setFutsalContact(String futsalContact) {
        this.futsalContact = futsalContact;
    }

    public String getFutsalLocation() {
        return futsalLocation;
    }

    public void setFutsalLocation(String futsalLocation) {
        this.futsalLocation = futsalLocation;
    }

    public String getFutsalName() {
        return futsalName;
    }

    public void setFutsalName(String futsalName) {
        this.futsalName = futsalName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPersonalContact() {
        return personalContact;
    }

    public void setPersonalContact(String personalContact) {
        this.personalContact = personalContact;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getVerified() {
        return verified;
    }

    public String getFutsalEmail() {
        return futsalEmail;
    }

    public void setFutsalEmail(String futsalEmail) {
        this.futsalEmail = futsalEmail;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public LinkedList getFutsalOpenDays() {
        return futsalOpenDays;
    }

    public void setFutsalOpenDays(LinkedList futsalOpenDays) {
        this.futsalOpenDays = futsalOpenDays;
    }

    public LinkedList getFutsalOpenTime() {
        return futsalOpenTime;
    }

    public void setFutsalOpenTime(LinkedList futsalOpenTime) {
        this.futsalOpenTime = futsalOpenTime;
    }
}
