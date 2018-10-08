package com.example.sy.infosys_ips.models;

public class Chat {
    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    boolean isseen;

    String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String receiver;

    public Chat(String sender, String receiver, String message , boolean isseen) {
        this.isseen = isseen;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    String message;

    public Chat() {
    }
}
