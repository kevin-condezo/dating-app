package com.saramambiches.datingapp.Chat;

public class ChatObject {
    private String message;
    private String imageUrl_match;
    private Boolean currentUser;
    public ChatObject(String message, Boolean currentUser) {
        this.message=message;
        this.imageUrl_match=imageUrl_match;
        this.currentUser=currentUser;
    }

    public String getImageUrl_match() {
        return imageUrl_match;
    }

    public void setImageUrl_match(String imageUrl_match) {
        this.imageUrl_match = imageUrl_match;
    }

    public String getMessage() { return message; }
    public void setMessage(String userId) {
        this.message = message;
    }

    public Boolean getCurrentUser() { return currentUser; }
    public void setCurrentUser(Boolean CurrentUser) {
        this.currentUser = currentUser;
    }
}
