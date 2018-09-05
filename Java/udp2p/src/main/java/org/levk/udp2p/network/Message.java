package org.levk.udp2p.network;

import org.levk.udp2p.network.peers.Peer;

import java.util.Arrays;

public class Message {
    private Peer target;
    private Packet message;
    private int attempts;

    public Message(Peer target, Packet message) {
        this.target = target;
        this.message = message;
        this.attempts = 0;
    }

    public Peer getPeer() {
        return target;
    }

    public Packet getPacket() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof  Message)) {
            return false;
        }

        if (!Arrays.equals(((Message) o).getPacket().getHash(), message.getHash())) {
            return false;
        }

        return ((Message) o).getPeer().equals(target);
    }

    public void attemptSend() {
        this.attempts++;
    }

    public boolean abandon() {
        return (this.attempts >= 10000);
    }
}
