package org.dilithium.network;

import org.dilithium.crypto.ecdsa.ECKey;

import java.net.InetAddress;

public class Peer {
    private boolean initialized = false;
    private ECKey key;
    private InetAddress address;
    private int port;
    private long lastSeen;

    /* Constructors */
    public Peer(ECKey key) {
        try {
            this.key = key;
            this.address = InetAddress.getLocalHost();
            this.port = 40424;
            this.lastSeen = System.currentTimeMillis();
            this.initialized = true;
        } catch (Exception e) {
            initialized = false;
        }
    }

    public Peer(ECKey key, String ip, int port) {
        try {
            this.key = key;
            this.address = InetAddress.getByName(ip);
            this.port = port;
            this.lastSeen = System.currentTimeMillis();
            this.initialized = true;
        } catch (Exception e) {
            initialized = false;
        }
    }

    public Peer(ECKey key, byte[] ip, int port) {
        try {
            this.key = key;
            this.address = InetAddress.getByAddress(ip);
            this.port = port;
            this.lastSeen = System.currentTimeMillis();
            this.initialized = true;
        } catch (Exception e) {
            initialized = false;
        }
    }

    public Peer(ECKey key, InetAddress address, int port) {
        this.key = key;
        this.address = address;
        this.port = port;
        this.lastSeen = System.currentTimeMillis();
        this.initialized = true;
    }

    /* Getters */
    public ECKey getKey() {
        return key;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
