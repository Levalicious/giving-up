package network;

import crypto.ecdsa.ECKey;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import util.ByteUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static util.ByteUtil.xor;
import static util.NetUtil.fromBitSet;
import static util.NetUtil.fromByteArray;

public class Peer implements Comparable<Peer> {
    private boolean initialized = false;
    private ECKey key;
    private InetAddress address;
    private int port;
    private long lastSeen;

    /* Constructor Methods */
    public Peer(ECKey key) {
        try {
            this.key = key;
            this.address = InetAddress.getLocalHost();
            this.port = 40424;
            this.lastSeen = System.currentTimeMillis();
            this.initialized = true;
        } catch (UnknownHostException u) {
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
        } catch (UnknownHostException u) {
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
        } catch (UnknownHostException u) {
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

    /* Peer State */
    public boolean isOld() {
        return ((System.currentTimeMillis() - lastSeen) >= 300000);
    }

    public boolean toTerminate() {
        return ((System.currentTimeMillis() - lastSeen) >= 600000);
    }

    /* Update Peer Data Methods */
    public void witness() {
        this.lastSeen = System.currentTimeMillis();
    }

    /* Prefix Tree Methods */
    public String calcDist(Peer in) {
        return fromBitSet(fromByteArray(xor(this.key.getAddress(), in.getKey().getAddress())));
    }

    public String calcDist(byte[] in) {
        return fromBitSet(fromByteArray(xor(this.key.getAddress(), in)));
    }

    /* Comparable Implement Override */
    public boolean equals(Peer p) {
        return key.equals(p.getKey());
    }

    @Override
    public int compareTo(Peer p) {
        if(this.lastSeen < p.lastSeen) return -1;
        if(this.lastSeen > p.lastSeen) return 1;
        return 0;
    }
}
