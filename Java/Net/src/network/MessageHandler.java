package network;

import network.types.MessageProtos.Message;

import java.util.Stack;

public class MessageHandler {
    /* TODO: USE TARGETTED MESSAGE */
    private static Stack<byte[]> toSend;

    public static byte[] grabPending() {
        return toSend.pop();
    }

    public static void sendMessage(BigMessage m)
}
