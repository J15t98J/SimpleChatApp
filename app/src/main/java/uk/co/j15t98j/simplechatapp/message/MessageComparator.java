package uk.co.j15t98j.simplechatapp.message;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message one, Message two) {
        return one.getTimestamp().compareTo(two.getTimestamp());
    }
}
