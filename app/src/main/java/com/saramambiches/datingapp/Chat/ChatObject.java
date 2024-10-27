package com.saramambiches.datingapp.Chat;

public class ChatObject {
    private String message;
    private String messageImage;
    private String typeMessage;
    private String  idChat;
    private Boolean currentUser;
    private String chatKey;

    public ChatObject(){

    }

    public ChatObject(String idChat) {
        this.idChat = idChat;
    }

    public ChatObject(String message, String messageImage, String typeMessage, Boolean currentUser, String idChat, String chatKey) {
        this.message = message;
        this.messageImage = messageImage;
        this.typeMessage = typeMessage;
        this.currentUser = currentUser;
        this.idChat = idChat;
        this.chatKey = chatKey;
    }


    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getMessage() { return message; }
    public void setMessage(String userId) {
        this.message = message;
    }

    public Boolean getCurrentUser() { return currentUser; }
    public void setCurrentUser(Boolean CurrentUser) {
        this.currentUser = currentUser;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }
}
