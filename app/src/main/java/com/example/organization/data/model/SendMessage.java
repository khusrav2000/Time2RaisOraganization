package com.example.organization.data.model;

import com.google.gson.annotations.SerializedName;

public class SendMessage {

    @SerializedName("Text")
    String text;

    @SerializedName("DateTime")
    String dateTime;

    @SerializedName("RecipientId")
    int recipientId;

    @SerializedName("MessageId")
    int messageId;

    public SendMessage(String text, String dateTime, int recipientId, int messageId) {
        this.text = text;
        this.dateTime = dateTime;
        this.recipientId = recipientId;
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;

    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

}
