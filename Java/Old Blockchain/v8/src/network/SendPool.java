package network;

import network.messageTypes.MessageProtos.*;

import java.util.EmptyStackException;
import java.util.Stack;

public class SendPool {
    private static SendPool instance = new SendPool();

    public static SendPool getInstance() {
        return instance;
    }

    private Stack<Message> toSend;

    public SendPool() {
        this.toSend = new Stack<>();
    }

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
