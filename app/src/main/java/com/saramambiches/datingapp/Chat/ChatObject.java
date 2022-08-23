package com.saramambiches.datingapp.Chat;

public class ChatObject {
    private String message;
    private String urlImage;
    private Boolean currentUser;
    public ChatObject(String message, String urlImage,Boolean currentUser) {
        this.message=message;
        this.currentUser=currentUser;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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
