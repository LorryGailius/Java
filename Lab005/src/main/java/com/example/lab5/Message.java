package com.example.lab5;

public class Message {

    public enum MessageType {
        CONNECT,
        DISCONNECT,
        MESSAGE,
        PRIVATE_MESSAGE,
        ERROR,
        SUCCESS,
    }

    public MessageType type;
    public String username;
    public String receiver;
    public String group;
    public String content;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
