package org.levk.UDP2Pv3.network.peerStorage;

public class PeerNotFoundException extends Exception {
    public PeerNotFoundException() {
        super();
    }

    public PeerNotFoundException(String message) {
        super(message);
    }
}
