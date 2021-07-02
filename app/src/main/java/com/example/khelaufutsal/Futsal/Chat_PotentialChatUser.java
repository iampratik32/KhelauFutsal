package com.example.khelaufutsal.Futsal;

public class Chat_PotentialChatUser {
    private String userId;
    private String displayName;
    private String profileImage;

    public Chat_PotentialChatUser(String userId,String displayName){
        this.userId=userId;
        this.displayName =displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
