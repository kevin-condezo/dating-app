package com.saramambiches.datingapp.Matches;

public class MatchesObject {
    private String lastMessage;
    private String name;
    private String profileImageUrl;

    public MatchesObject(){ }

    public MatchesObject(String userId, String name, String profileImageUrl) {
        this.lastMessage = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() { return lastMessage; }
    public void setUserId(String userId) {
        this.lastMessage = userId;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
