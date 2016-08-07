package uk.co.j15t98j.simplechatapp.util;

import java.util.Date;

public class Message {
    private String content;
    private Date timestamp;
    private MessageType type;

    public Message(String content, Date timestamp, MessageType type) {
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public MessageType getType() {
        return type;
    }
}
