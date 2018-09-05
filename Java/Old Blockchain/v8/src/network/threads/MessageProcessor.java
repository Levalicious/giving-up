package network.threads;

import network.NetUtils;
import network.Peer;
import network.ReceivePool;
import network.messageTypes.Data;
import network.messageTypes.MessageProtos.*;
import network.messageTypes.BigMessage;

import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class MessageProcessor extends Thread {
    private ReceivePool received;

    private boolean running;

    private ConcurrentHashMap<byte[], BigMessage> messages;
    private Stack<Data> objects;
    private Stack<BigMessage> toRelay;
    private Stack<byte[]> acknowledged;
    private Stack<Peer> joinRequest;
    private Stack<Peer> leaveRequest

    public MessageProcessor() {
        received = ReceivePool.getInstance();
    }

    @Override
    public void run() {
        running = true;

        while(running) {
            Message m = received.grab();

            if(m != null) {
                /* If it's acknowledging receiving a message from you, handle it */
                if(!isAcknowledge(m)) {
                    /* If it's not an acknowledge, assemble the message from component packets. */
                    if(messages.containsKey(m.getMessageHash().toByteArray())) {
                        messages.get(m.getMessageHash().toByteArray()).addMessage(m);
                    } else {
                        messages.put(m.getMessageHash().toByteArray(), new BigMessage(m));
                    }

                    /* Whatever assembled messages need to be relay them, relay them. */
                    if(!messages.get(m.getMessageHash().toByteArray()).toRelay()) {
                        /* If it's not supposed to be relayed, deserialize and add to the pool. */
                        if(messages.get(m.getMessageHash().toByteArray()).isComplete()) {
                            objects.push(new Data(messages.remove(m.getMessageHash().toByteArray())));
                        }
                    } else {
                        /* If it is supposed to be relayed, handle it */
                        handleRelay(messages.remove(m.getMessageHash().toByteArray()));
                    }
                } else {
                    /* Handle the acknowledge */
                    handleAcknowledge(m);
                }
            }
        }

    }

    public void handleMessage(Message m) throws Exception {
        switch(m.getMessageType()) {
            case 0x00:
                break;
            case 0x01:
                break;
            case 0x02:
                break;
            case 0x03:
                break;
            case 0x04:
                break;
            case 0x05:
                break;
            case 0x06:
                break;
            case 0x07:
                break;
            case 0x08:
                break;
            case 0x09:
                break;
            case 0x0A:
                break;
            case 0x0B:
                break;
            case 0x0C:
                break;
            case 0x0D:
                break;
            case 0x0E:
                break;
            case 0x0F:
                break;
            default: throw new Exception();
        }
    }

    public Data grabDeserialized() {
        if(!objects.isEmpty()) {
            return objects.pop();
        } else {
            return null;
        }
    }

    public BigMessage grabToRelay() {
        if(!toRelay.isEmpty()) {
            return toRelay.pop();
        } else {
            return null;
        }
    }

    public byte[] grabAcknowledged() {
        if(!toRelay.isEmpty()) {
            return acknowledged.pop();
        } else {
            return null;
        }
    }

    public boolean isAcknowledge(Message m) {
        return (m.getMessageType() == 3);
    }

    public void handleAcknowledge(Message m) {
        acknowledged.push(NetUtils.getData(m));
    }

    public void handleRelay(BigMessage m) {
        toRelay.push(m);
    }
}
