package network;

import network.messageTypes.MessageProtos.*;

import java.util.EmptyStackException;
import java.util.Stack;

public class ReceivePool {
    private static ReceivePool instance = new ReceivePool();

    public static ReceivePool getInstance() {
        return instance;
    }

    private Stack<Message> toSend = new Stack<>();

    public void addItem(Message message) {
        toSend.add(message);
    }

    public Message grab() {
        try {
            return toSend.pop();
        } catch (EmptyStackException e) {
            return null;
        }
    }
}
