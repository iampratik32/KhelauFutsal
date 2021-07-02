package com.example.khelaufutsal.Futsal;

public class BookingClass {
    private String futsalId;
    private String date;
    private String time;
    private String booked;
    private String confirmed;
    private String userId;
    private String futsalDisplayImage;
    private String futsalName;

    public String getFutsalDisplayImage() {
        return futsalDisplayImage;
    }

    public void setFutsalDisplayImage(String futsalDisplayImage) {
        this.futsalDisplayImage = futsalDisplayImage;
    }

    public String getFutsalName() {
        return futsalName;
    }

    public void setFutsalName(String futsalName) {
        this.futsalName = futsalName;
    }

    public BookingClass(String futsalId, String date, String time, String booked, String confirmed, String userId) {
        this.futsalId = futsalId;
        this.date = date;
        this.time = time;
        this.booked = booked;
        this.confirmed = confirmed;
        this.userId = userId;
    }

    public String getFutsalId() {
        return futsalId;
    }

    public void setFutsalId(String futsalId) {
        this.futsalId = futsalId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBooked() {
        return booked;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
