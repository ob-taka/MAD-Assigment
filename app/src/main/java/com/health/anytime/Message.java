package com.health.anytime;

public class Message
{
    private String messageID, from, to, message, type, name;

    public Message()
    {
    }

    public Message(String messageID, String from, String to, String message, String type, String name) {
        this.messageID = messageID;
        this.from = from;
        this.to = to;
        this.message = message;
        this.type = type;
        this.name = name;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
