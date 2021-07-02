package com.example.khelaufutsal.Player;

public class Player_BookedFutsalClass {
    private String futsalId;
    private String futsalLocation;
    private String futsalTime;
    private String futsalDate;
    private String futsalContact;
    private String futsalEmail;
    private String futsalDisplayImageLink;
    private String futsalName;

    public Player_BookedFutsalClass(String futsalId, String futsalTime, String futsalDate) {
        this.futsalId = futsalId;
        this.futsalTime = futsalTime;
        this.futsalDate = futsalDate;
    }

    public String getFutsalId() {
        return futsalId;
    }

    public void setFutsalId(String futsalId) {
        this.futsalId = futsalId;
    }

    public String getFutsalName() {
        return futsalName;
    }

    public void setFutsalName(String futsalName) {
        this.futsalName = futsalName;
    }

    public String getFutsalLocation() {
        return futsalLocation;
    }

    public void setFutsalLocation(String futsalLocation) {
        this.futsalLocation = futsalLocation;
    }

    public String getFutsalTime() {
        return futsalTime;
    }

    public void setFutsalTime(String futsalTime) {
        this.futsalTime = futsalTime;
    }

    public String getFutsalDate() {
        return futsalDate;
    }

    public void setFutsalDate(String futsalDate) {
        this.futsalDate = futsalDate;
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

    public String getFutsalDisplayImageLink() {
        return futsalDisplayImageLink;
    }

    public void setFutsalDisplayImageLink(String futsalDisplayImageLink) {
        this.futsalDisplayImageLink = futsalDisplayImageLink;
    }
}
