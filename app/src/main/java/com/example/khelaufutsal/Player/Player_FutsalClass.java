package com.example.khelaufutsal.Player;

import java.util.LinkedList;

public class Player_FutsalClass {
    private String futsalContact;
    private String futsalEmail;
    private String futsalName;
    private String futsalOwner;
    private String futsalLocation;
    private String futsalId;
    private String futsalDisplayImageLink;
    private int noOfImages;
    private LinkedList futsalTiming;
    private LinkedList futsalOpenDays;
    private float futsalRating;
    private String futsalPrice;
    private String futsalVerified;
    private String futsalLatLan;

    public String getFutsalLatLan() {
        return futsalLatLan;
    }

    public void setFutsalLatLan(String futsalLatLan) {
        this.futsalLatLan = futsalLatLan;
    }

    public String getFutsalVerified() {
        return futsalVerified;
    }

    public void setFutsalVerified(String futsalVerified) {
        this.futsalVerified = futsalVerified;
    }


    public String getFutsalPrice() {
        return futsalPrice;
    }

    public void setFutsalPrice(String futsalPrice) {
        this.futsalPrice = futsalPrice;
    }

    public float getFutsalRating() {
        return futsalRating;
    }

    public void setFutsalRating(float futsalRating) {
        this.futsalRating = futsalRating;
    }

    public String getFutsalLocation() {
        return futsalLocation;
    }

    public void setFutsalLocation(String futsalLocation) {
        this.futsalLocation = futsalLocation;
    }

    public Player_FutsalClass(String futsalContact, String futsalEmail, String futsalName, String futsalOwner, String futsalLocation, String futsalId, String futsalVerified) {
        this.futsalContact = futsalContact;
        this.futsalEmail = futsalEmail;
        this.futsalName = futsalName;
        this.futsalOwner = futsalOwner;
        this.futsalLocation = futsalLocation;
        this.futsalId = futsalId;
        this.futsalVerified = futsalVerified;
    }

    public LinkedList getFutsalOpenDays() {
        return futsalOpenDays;
    }

    public void setFutsalOpenDays(LinkedList futsalOpenDays) {
        this.futsalOpenDays = futsalOpenDays;
    }

    public LinkedList getFutsalTiming() {
        return futsalTiming;
    }

    public void setFutsalTiming(LinkedList futsalTiming) {
        this.futsalTiming = futsalTiming;
    }

    public int getNoOfImages() {
        return noOfImages;
    }

    public void setNoOfImages(int noOfImages) {
        this.noOfImages = noOfImages;
    }

    public String getFutsalDisplayImageLink() {
        return futsalDisplayImageLink;
    }

    public void setFutsalDisplayImageLink(String futsalDisplayImageLink) {
        this.futsalDisplayImageLink = futsalDisplayImageLink;
    }

    public String getFutsalContact() {
        return futsalContact;
    }

    public void setFutsalContact(String futsalContact) {
        this.futsalContact = futsalContact;
    }

    public String getFutsalEmail() {
        return futsalEmail;
    }

    public void setFutsalEmail(String futsalEmail) {
        this.futsalEmail = futsalEmail;
    }

    public String getFutsalName() {
        return futsalName;
    }

    public void setFutsalName(String futsalName) {
        this.futsalName = futsalName;
    }

    public String getFutsalOwner() {
        return futsalOwner;
    }

    public void setFutsalOwner(String futsalOwner) {
        this.futsalOwner = futsalOwner;
    }

    public String getFutsalId() {
        return futsalId;
    }

    public void setFutsalId(String futsalId) {
        this.futsalId = futsalId;
    }
}
