package org.levk.udp2p.network;

import org.bouncycastle.math.ec.ECPoint;
import org.levk.udp2p.crypto.SchnorrKey;
import org.levk.udp2p.crypto.SchnorrSig;
import org.levk.udp2p.serialization.ENCItem;
import org.levk.udp2p.serialization.ENCList;
import org.levk.udp2p.serialization.TRENC;

import java.util.Arrays;

import static org.levk.udp2p.crypto.SchnorrKey.liftPoint;
import static org.levk.udp2p.util.ByteUtils.*;
import static org.levk.udp2p.util.HashUtil.blake2;
import static org.levk.udp2p.util.HashUtil.blake2omit12;

public class Packet {
    /* The encoding for this packet */
    private byte[] encoded;

    /* Whether the non-encoded fields have
     * had values assigned to them yet */
    private boolean parsed = false;

    /* An identifier as to what type of
     * packet this is. */
    private int packetType;

    /* The index of this packet within
     * the larger message it is conveying
     * From 0 to setSize - 1*/
    private int setIndex;

    /* The number of packets that the
     * message being conveyed takes up */
    private int setSize;

    /* Set to an empty byte array when
     * the packet has no target, otherwise
     * contains 20 byte address */
    private byte[] target;

    /* The payload of this packet */
    private byte[] payload;

    /* Hash of message this packet
     * is a part of */
    private byte[] messageHash;

    /* Identifier for the network
     * this packet was transmitted
     * on. To prevent different networks
     * using this protocol from merging. */
    private int networkId;

    /* Hash of packet. For validation, bloom
     * filters, and spam protection. */
    private byte[] hash;

    public Packet(byte[] encoded) {
        this.encoded = encoded;
    }

    public Packet(int setIndex, int setSize, int packetType, byte[] payload, byte[] messageHash, int networkId) {
        this(setIndex, setSize, new byte[20], packetType, payload, messageHash, networkId);
    }

    public Packet(int setIndex, int setSize, byte[] target, int packetType, byte[] payload, byte[] messageHash, int networkId) {
        this.setIndex = setIndex;
        this.setSize = setSize;

        this.target = target;
        this.packetType = packetType;
        this.payload = payload;
        this.messageHash = messageHash;
        this.networkId = networkId;

        parsed = true;

        this.hash = getHash();
    }

    public synchronized void parse() {
        if (parsed) return;

        try {
            ENCList decPacket = TRENC.decode(encoded);

            if (decPacket.size() > 7) throw new RuntimeException("Too many encoded elements.");
            for (ENCItem e : decPacket) {
                if (e.isList()) throw new RuntimeException("Packet elements should not be lists.");
            }

            this.setIndex = byteArrayToInt(decPacket.get(0).getEncData());
            this.setSize = byteArrayToInt(decPacket.get(1).getEncData());
            this.target = decPacket.get(2).getEncData();
            this.packetType = byteArrayToInt(decPacket.get(3).getEncData());
            this.payload = decPacket.get(4).getEncData();
            this.networkId = byteArrayToInt(decPacket.get(5).getEncData());
            this.messageHash = decPacket.get(6).getEncData();

            this.parsed = true;
            this.hash = getHash();
        } catch (Exception e) {
            throw new RuntimeException("Error on parsing encoding", e);
        }
    }

    public boolean hasTarget() {
        parse();
        return (!Arrays.equals(target, new byte[20]));
    }

    public int getSetIndex() {
        parse();
        return setIndex;
    }

    public int getSetSize() {
        parse();
        return setSize;
    }

    public byte[] getPacketECC() {
        parse();
        return Arrays.copyOfRange(getHash(), 28, 32);
    }

    public byte[] getTarget() {
        parse();
        return target;
    }

    public int getNetworkId() {
        parse();
        return networkId;
    }

    public int getPacketType() {
        parse();
        return packetType;
    }

    public byte[] getPayload() {
        parse();
        return payload;
    }

    public byte[] getMessageHash() {
        parse();
        return messageHash;
    }

    public byte[] getHash() {
        if(hash != null) return hash;

        parse();
        byte[] plainMsg = this.getEncoded();
        return blake2(plainMsg);
    }

    public byte[] getEncoded() {
        if (encoded != null) return encoded;

        this.encoded = TRENC.encode(intToBytes(setIndex), intToBytes(setSize), target, intToBytes(packetType), payload, intToBytes(networkId), messageHash);

        this.hash = this.getHash();

        return encoded;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Packet)) return false;

        return Arrays.equals(((Packet)o).getEncoded(), this.getEncoded());
    }
}
