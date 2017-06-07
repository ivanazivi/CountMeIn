package com.countmein.countmein.beans;

import java.util.Date;

/**
 * Created by Home on 4/22/2017.
 */

public class ChatMessageBean extends BaseModel {

    private String messageText;
    private String messageUser;
    private long messageTime;

    public ChatMessageBean(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = new Date().getTime();
    }

    public ChatMessageBean(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}