package org.levk.udp2p.network.peers;

import org.bouncycastle.util.encoders.Hex;
import org.levk.udp2p.serialization.ENCItem;
import org.levk.udp2p.serialization.ENCList;
import org.levk.udp2p.serialization.TRENC;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Peer {
    private final static byte[] IPv4inIPv6Prefix = {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF};

    private byte[] encoded;
    private boolean parsed;

    private byte[] address;
    private InetAddress ipAddress;

    private long messagesSeen;
    private boolean isMalicious;
    private long lastSeen;
    private long firstSeen;

    public Peer(byte[] encoded) {
        this.encoded = encoded;
        this.parsed = false;

        this.isMalicious = false;
        this.messagesSeen = 0;
    }

    public Peer(byte[] address, byte[] ipAddress) throws UnknownHostException {
        this.address = address;
        this.ipAddress = InetAddress.getByAddress(ipAddress);
        parsed = true;

        this.isMalicious = false;
        this.firstSeen = 0;
        this.messagesSeen = 0;
    }

    public synchronized void parse() {
        if (parsed) return;

        try {
            ENCList decPeer = TRENC.decode(encoded);

            if (decPeer.size() > 2) throw new RuntimeException("Too many encoded elements.");
            for (ENCItem e : decPeer) {
                if (e.isList()) throw new RuntimeException("Packet elements should not be lists.");
            }

            this.address = decPeer.get(0).getEncData();
            this.ipAddress = InetAddress.getByAddress(decPeer.get(1).getEncData());

            this.parsed = true;

        } catch (Exception e) {
            throw new RuntimeException("Error on parsing encoding", e);
        }
    }

    public byte[] getEncoded() {
        if (encoded != null) return encoded;

        encoded = TRENC.encode(this.address, this.ipAddress.getAddress());

        return encoded;
    }

    public void witness() {
        this.firstSeen = (firstSeen == 0) ? System.currentTimeMillis() : firstSeen;
        this.lastSeen = System.currentTimeMillis();
        this.messagesSeen++;
    }

    public byte[] getAddress() {
        parse();
        return address;
    }

    public InetAddress getIpAddress() {
        parse();
        return ipAddress;
    }

    public boolean isOld() {
        return System.currentTimeMillis() - this.lastSeen > 5000;
    }

    public boolean toDelete() {
        return System.currentTimeMillis() - this.lastSeen > 15000;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Peer)) {
            return false;
        }

        if (!Arrays.equals(((Peer) o).getAddress(), this.address)) {
            return false;
        }

        return ((Peer) o).getIpAddress().equals(this.ipAddress);
    }

    public String toString() {
        return Hex.toHexString(this.address) + " : " + this.ipAddress.toString() + " : " + messagesSeen + "\n";
    }

    public boolean isSpammy() {
        return ((messagesSeen / ((lastSeen - firstSeen) / 1000)) > 100);
    }

    private static boolean isIPv4(byte[] address) {
        return (Arrays.equals(IPv4inIPv6Prefix, Arrays.copyOfRange(address, 0, 12)));
    }

    private static byte[] toIPv4(byte[] address) {
        return Arrays.copyOfRange(address, 12, 16);
    }
}
