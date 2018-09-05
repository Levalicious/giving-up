package org.levk.udp2p.network;

import org.levk.udp2p.crypto.SchnorrKey;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.levk.udp2p.util.HashUtil.blake2ECC;

public class PacketSet {
    private static final SecureRandom rand = new SecureRandom();

    private int packetType;
    private Packet[] packets;
    private int packetCount = 0;

    private boolean complete;

    public PacketSet(int packetType, byte[] data, int networkId) {
        byte[] messageHash = blake2ECC(data);

        this.packetType = packetType;

        byte[][] temp = partition(data, 973);

        packets = new Packet[temp.length];

        for (int i = 0; i < temp.length; i++) {
            packets[i] = new Packet(i, temp.length, packetType, temp[i], messageHash, networkId);
            packetCount++;
        }

        complete = true;
    }

    public PacketSet(int packetType, byte[] target, byte[] data, int networkId) {
        byte[] messageHash = blake2ECC(data);

        this.packetType = packetType;

        byte[][] temp = partition(data, 943);

        System.out.println(temp[0].length);

        packets = new Packet[temp.length];

        for (int i = 0; i < temp.length; i++) {
            packets[i] = new Packet(i, temp.length, target, packetType, temp[i], messageHash, networkId);
            packetCount++;
        }

        complete = true;
    }

    public PacketSet(Packet packet) {
        packets = new Packet[packet.getSetSize()];
        packets[packet.getSetIndex()] = packet;
        packetCount++;
        packetType = packet.getPacketType();

        updateComplete();
    }

    public void add(Packet packet) {
        if (complete) return;

        if (packets[packet.getSetIndex()] == null) {
            packets[packet.getSetIndex()] = packet;
            packetCount++;

            updateComplete();
        }
    }

    public int getSize() {
        return packetCount;
    }

    public int getSetType() {
        return packetType;
    }

    private void updateComplete() {
        complete = (packetCount == packets.length);
    }

    public boolean isComplete() {
        return complete;
    }

    public List<Packet> getPackets() {
        List<Packet> temp = new ArrayList<>();

        for (int i = 0; i < packets.length; i++) {
            temp.add(packets[i]);
        }

        return temp;
    }

    public Packet getRandom() {
        return packets[rand.nextInt(packets.length)];
    }



    private static byte[][] partition(byte[] in, int partitionSize)
    {
        int partitionCount =  (int)Math.ceil((double)in.length / (double) partitionSize);

        byte[][] temp = new byte[partitionCount][];

        for (int p = 0; p < partitionCount; p++)
        {
            int start = p * partitionSize;
            int len = (p != partitionCount - 1) ? partitionSize : in.length - start;
            byte[] partition = new byte[len];

            System.arraycopy(in, start, partition, 0, len);

            temp[p] = partition;
        }

        return temp;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PacketSet)) return false;

        try {
            for (int i = 0; i < packets.length; i++) {
                if (!packets[i].equals(((PacketSet)o).getPackets().get(i))) return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
