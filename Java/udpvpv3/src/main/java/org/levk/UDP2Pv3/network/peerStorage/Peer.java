package org.levk.UDP2Pv3.network.peerStorage;

import org.bouncycastle.util.encoders.Hex;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

import static org.levk.UDP2Pv3.util.ByteUtils.longToBytes;
import static org.levk.UDP2Pv3.util.ByteUtils.merge;
import static org.levk.UDP2Pv3.util.HashUtil.blake2omit12;

public class Peer {
    public final static byte[] IPv4inIPv6Prefix = {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF};

    private byte[] encoded;
    private boolean parsed;

    /* The client version that
     * this peer is running */
    private byte[] version;

    /* When this peer was last
     * seen. More recently seen
     * peers are prioritized over
     * older ones. */
    private byte[] time;

    /* The network address of
     * this node. If IPv4, it's
     * an IPv4-mapped IPv6 address,
     * otherwise a plain IPv6 one. */
    private byte[] address;

    /* The InetAddress for
     * this node. */
    private InetAddress addr;

    /* The port this node is running on */
    private byte[] port;

    /* The hash of the version, port,
     * and address. For use with the
     * kademlia-esque routing tables. */
    private byte[] bucketAddress;

    /* Contains timestamps of witness times */
    private LinkedList<Long> timestamps;

    /* Whether this node is connected or not */
    private boolean connected;

    public Peer(byte[] encoded) {
        this.encoded = encoded;
        this.parsed = false;
    }

    public Peer(byte[] version, byte[] address, byte[] port) throws UnknownHostException {
        this(longToBytes(System.currentTimeMillis(), 4), version, address, port);
    }

    public Peer(byte[] time, byte[] version, byte[] address, byte[] port) throws UnknownHostException {
        if (address.length == 4) address = merge(IPv4inIPv6Prefix, address);
        if (!checkLengths(time, version, address, port)) throw new RuntimeException("Attempted to initialize Peer with invalid data.");

        this.time = time;
        this.version = version;
        this.address = address;
        this.port = port;

        this.timestamps = new LinkedList<>();

        initializeInet();

        this.bucketAddress = blake2omit12(merge(version, address, port));

        parsed = true;
    }

    private boolean checkLengths(byte[] time, byte[] version, byte[] address, byte[] port) {
        if (time.length != 4) return false;
        if (version.length != 2) return false;
        if (address.length != 16) return false;
        if (port.length != 2) return false;

        return true;
    }

    public void initializeInet() throws UnknownHostException {
        if (isIPv6(address)) {
            this.addr = InetAddress.getByAddress(address);
        } else {
            byte[] IPv4Address = Arrays.copyOfRange(address, 12, 16);
            this.addr = InetAddress.getByAddress(IPv4Address);
        }
    }

    public void witness() {
        long timestamp = System.currentTimeMillis();

        this.time = longToBytes(timestamp, 4);

        this.timestamps.addLast(timestamp);
    }

    public byte[] getTimeBytes() {
        parse();
        return time;
    }

    public long getTime() {
        parse();
        return Long.parseUnsignedLong(Hex.toHexString(time), 16);
    }

    public byte[] getVersionBytes() {
        parse();
        return version;
    }

    public int getVersion() {
        parse();
        return Integer.parseUnsignedInt(Hex.toHexString(version), 16);
    }

    public byte[] getAddress() {
        parse();
        return address;
    }

    public InetAddress getAddr() {
        parse();
        return addr;
    }

    public void connect() {
        this.connected = true;
    }

    public void disconnect() {
        this.connected = false;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public int getPort() {
        parse();
        return Integer.parseUnsignedInt(Hex.toHexString(port), 16);
    }

    public byte[] getPortBytes() {
        parse();
        return port;
    }

    public byte[] getBucketAddress() {
        parse();
        if (bucketAddress != null) return bucketAddress;

        this.bucketAddress = blake2omit12(merge(version, address, port));

        return bucketAddress;
    }

    public boolean isIPv6(byte[] address) {
        return !Arrays.equals(IPv4inIPv6Prefix, Arrays.copyOfRange(address, 0, 12));
    }

    public synchronized void parse() {
        if (parsed) return;

        if (encoded.length != 24) throw new RuntimeException("Tried to decode an invalid peer encoding.");

        try {
            this.time = Arrays.copyOfRange(encoded, 0, 4);
            this.version = Arrays.copyOfRange(encoded, 4, 6);
            this.address = Arrays.copyOfRange(encoded, 6, 22);
            this.port = Arrays.copyOfRange(encoded, 22, 24);

            this.bucketAddress = blake2omit12(merge(version, address, port));

            initializeInet();
        } catch (UnknownHostException u) {
            u.printStackTrace();
            throw new RuntimeException("Failed to parse peer encoding.");
        }
    }

    public byte[] getEncoded() {
        if (encoded != null) return encoded;

        this.encoded = merge(time, version, address, port);

        return encoded;
    }

    public static byte[] mapIPv4toIPv6(byte[] in) {
        if (in.length != 4) throw new RuntimeException("Input must be an IPv4 address.");

        return merge(IPv4inIPv6Prefix, in);
    }

    public static byte[] toIPv4(byte[] ipv6in) {
        if (ipv6in.length != 16) throw new RuntimeException("Input must be an IPv6 mapped IPv4 address");

        return Arrays.copyOfRange(ipv6in, 12, 16);
    }

    public boolean isOld() {
        /* If the peer hasn't been seen in 5 minutes... */
        return System.currentTimeMillis() - Long.parseUnsignedLong(Hex.toHexString(time), 16) > 5 * 60 * 1000;
    }

    public boolean toDelete() {
        /* If the peer hasn't been seen in 30 minutes? (also look into an hour & 3 hours) */
        return System.currentTimeMillis() - Long.parseUnsignedLong(Hex.toHexString(time), 16) > 30 * 60 * 1000;
    }

    public int getRate() {
        /* Cut away timestamps that are over an hour old to get the messages per hour rate */
        while (System.currentTimeMillis() - timestamps.getFirst() > (1 * 60 * 60 * 1000)) {
            timestamps.removeFirst();
        }

        return timestamps.size();
    }

    public boolean isSpammy() {
        /* If over 1000 messages per minute */
        return (this.getRate() > 60000);
    }

    @Override
    public String toString() {
        return "";
    }
}
