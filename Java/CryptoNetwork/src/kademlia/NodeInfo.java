package kademlia;

import crypto.ECDSA.ECKey;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Date;
import java.util.stream.IntStream;

import static util.ByteUtil.concat;
import static util.Hex.getHex;

public class NodeInfo implements Comparable<NodeInfo> {
    private String IP;
    private int port;
    private byte[] nodeId;
    private ECKey keyPair;
    private Date lastSeen;
    private String xorDist;

    public NodeInfo() {
        ECKey key = new ECKey();
        System.out.println("Generating new keypair for node: " + getHex(key.getAddress()));
        this.keyPair = key;
        this.nodeId = keyPair.getAddress();
        this.lastSeen = new Date();
        this.xorDist = null;
    }
    public NodeInfo(byte[] privKey) {
        this.keyPair = ECKey.fromPrivate(privKey);
        this.nodeId = keyPair.getAddress();
        this.lastSeen = new Date();
        this.xorDist = null;
    }

    public NodeInfo(byte[] ID, String IP, int port) {
        this.nodeId = ID;
        this.IP = IP;
        this.port = port;
        this.lastSeen = new Date();
        this.xorDist = null;
    }

    public String getIP() {
        return IP;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return (IP + ":" + port);
    }

    public byte[] getID() {
        return nodeId;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAddress(String address) {
        String[] temp = address.split(":");
        this.IP = temp[0];
        this.port = Integer.valueOf(temp[1]);
    }

    public boolean hasDist() {
        if(xorDist != null) return true;
        return false;
    }

    public void setDist(NodeInfo nI) {
        BitSet temp = fromByteArray(nodeId);
        temp.xor(fromByteArray(nI.getID()));
        this.xorDist = fromBitSet(temp);
    }

    public String getDist() {
        return xorDist;
    }

    public BigInteger xorDist(NodeInfo nI) {
        BitSet temp = fromByteArray(nodeId);
        temp.xor(fromByteArray(nI.getID()));
        byte[] ostart = new byte[1];
        ostart[0] = (byte)0x00;
        return new BigInteger(concat(ostart,temp.toByteArray()));
    }

    public static BitSet fromByteArray(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    public int compareTo(NodeInfo ni) {
        if(lastSeen.before(ni.lastSeen)) return -1;
        if(lastSeen.equals(ni.lastSeen)) return 0;
        if(lastSeen.after(ni.lastSeen)) return 1;
        return 0;
    }

    public String toString() {
        return (fromBitSet(fromByteArray(nodeId)));
    }

    public static String fromBitSet(BitSet set) {
        final StringBuilder buffer = new StringBuilder(set.length());
        IntStream.range(0, set.length()).mapToObj(i -> set.get(i) ? '1' : '0').forEach(buffer::append);
        String temp = buffer.reverse().toString();
        while(temp.length() < 256) {
            temp = 0 + temp;
        }

        return temp;
    }
}
