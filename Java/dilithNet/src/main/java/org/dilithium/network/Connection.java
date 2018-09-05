package org.dilithium.network;

import org.dilithium.network.messages.Message;

import java.net.Socket;
import java.util.Stack;

public class Connection {
    Socket socket;
    Peer peer;
    Stack<Message> receivedMessages;

    public boolean send(byte[] toSend) {
        return true;
    }
}
