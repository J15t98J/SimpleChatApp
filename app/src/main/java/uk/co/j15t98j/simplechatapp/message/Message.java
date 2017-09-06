package uk.co.j15t98j.simplechatapp.message;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

import uk.co.j15t98j.simplechatapp.MainActivity;

public class Message {

    private String author;
    private String content;
    private Long timestamp;
    private MessageType type;

    public Message(@NonNull String author, @NonNull String content, @NonNull Long timestamp, MessageType type) {
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Message(@NonNull DataSnapshot snapshot) {
        String ID = snapshot.getKey();
        author = snapshot.child("author").getValue().toString();
        content = snapshot.child("content").getValue().toString();
        timestamp = (Long)snapshot.child("timestamp").getValue();
        type = Objects.equals(author, MainActivity.author)? MessageType.SENT : MessageType.RECEIVED;
    }

    String getAuthor() {
        return author;
    }

    String getContent() {
        return content;
    }

    Long getTimestamp() {
        return timestamp;
    }

    MessageType getType() {
        return type;
    }


    @Override
    public boolean equals(Object obj) {
        Message message = (Message)obj;
        return obj.getClass().equals(Message.class)
                && message.getAuthor().equals(author)
                && message.getContent().equals(content)
                && message.getTimestamp().equals(timestamp);
    }
}
