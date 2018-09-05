package org.levk.UDP2Pv2.network.peerStorage;

public class NoSpaceException extends Exception {
    public NoSpaceException() {
        super();
    }

    public NoSpaceException(String message) {
        super(message);
    }
}