package network.messageTypes;

import network.NetUtils;
import resources.Constants;

import java.util.Arrays;

public class BigMessage {
    MessageProtos.Message[] messageSet;
    byte[] identifierHash;
    int messagesNeeded;
    int messagesAssembled;
    int firstReceived;

    public BigMessage(MessageProtos.Message message) {
        this.identifierHash = message.getMessageHash().toByteArray();
        this.messageSet[message.getSetIndex() - 1] = message;
        this.messagesNeeded = message.getSetSize();
        this.messagesAssembled = 1;
        this.firstReceived = message.getSetIndex() - 1;
    }

    public void addMessage(MessageProtos.Message message) {
        if(Arrays.equals(identifierHash, message.getMessageHash().toByteArray())) {
            messageSet[message.getSetIndex() - 1] = message;
            messagesAssembled++;
        }
    }

    public int getMessageType() {
        return messageSet[firstReceived].getMessageType();
    }

    public boolean isComplete() {
        if(messagesAssembled == messagesNeeded) return true;

        return false;
    }

    public boolean toRelay() {
        if(messageSet[firstReceived].hasTarget()) {
            if(!Arrays.equals(messageSet[0].getTarget().toByteArray(), Constants.client.getId())) {
                return true;
            }
        }

        return false;
    }

    public byte[] getData() {
        if(isComplete()) {
            return NetUtils.getData(messageSet);
        } else {
            return null;
        }
    }
}