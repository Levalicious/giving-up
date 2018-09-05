package network;

import crypto.ecdsa.ECKey;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import util.BIUtil;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static network.NetUtils.fromBitSet;
import static network.NetUtils.fromByteArray;
import static util.ByteUtil.xor;

public class Peer implements Comparable<Peer> {
    public InetAddress ip;
    public int port;
    public byte[] id;
    public byte[] dist = null;
    public long lastSeen;

    public Peer() {
        try {
            this.ip = InetAddress.getByName("127.0.0.1");
            this.port = 40424;
            this.id = (new ECKey()).getAddress();
            this.dist = xor(id, id);
            this.lastSeen = System.currentTimeMillis();
        } catch (UnknownHostException u) {

        }
    }

    public Peer(byte[] ip, int port, byte[] id, byte[] clientId) {
        try {
            this.ip = InetAddress.getByAddress(ip);
            this.port = port;
            this.id = id;
            this.dist = xor(id, clientId);
            this.lastSeen = System.currentTimeMillis();
        } catch (UnknownHostException u ) {

        }
    }

    public boolean hasDist() {
        return dist != null;
    }

    public void calcDist(Peer in) {
        this.dist = xor(this.id, in.id);
    }

    public String getDist() {
        return fromBitSet(fromByteArray(dist));
    }

    public boolean equals(Peer p) {
        return ByteUtils.equals(id, p.id);
    }

    public boolean isOld() {
        if((System.currentTimeMillis() - lastSeen) >= 300000) return true;
        return false;
    }

    public boolean toTerminate() {
        if((System.currentTimeMillis() - lastSeen) >= 600000) return true;
        return false;
    }

    @Override
    public int compareTo(Peer p) {
        if(BIUtil.isLessThan(new BigInteger(this.dist), new BigInteger(p.dist))) return -1;
        if(BIUtil.isLessThan(new BigInteger(p.dist), new BigInteger(this.dist))) return 1;
        return 0;
    }
}
