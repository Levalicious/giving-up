package org.levk.UDP2Pv3.network.peerStorage;

public class NoSpaceException extends Exception {
    public NoSpaceException() {
        super();
    }

    public NoSpaceException(String message) {
        super(message);
    }
}