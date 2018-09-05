package network.messageTypes;

import network.messageTypes.MessageProtos.*;
import network.Peer;

import java.net.InetAddress;
import java.util.Arrays;

import static crypto.Hash.keccak256;
import static util.ByteUtil.concat;
import static util.Hex.fromHex;

public class TargetedMessage {
    private Peer target;
    private Message message;
    private byte[] messageId;
    private boolean received = false;
    private int timesSent;

    public TargetedMessage (Peer target, Message message) {
        this.target = target;
        this.message = message;
        this.messageId = keccak256(concat(fromHex(Integer.toHexString(message.getSetIndex())), message.getMessageHash().toByteArray()));
        this.timesSent = 0;
    }

    public boolean attemptAgain() {
        if(timesSent < 5) {
            this.timesSent++;
            return true;
        }

        return false;
    }

    public InetAddress getIP() {
        return target.ip;
    }

    public int getPort() {
        return target.port;
    }

    public boolean received() {
        return received;
    }

    public boolean attemptAcknowledge(byte[] messageHash) {
        received = Arrays.equals(messageHash, messageId);
        return received;
    }
}
