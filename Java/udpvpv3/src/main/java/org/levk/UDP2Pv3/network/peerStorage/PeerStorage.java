package org.levk.UDP2Pv3.network.peerStorage;

import org.levk.UDP2Pv3.util.datastore.MemoryDB;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PeerStorage {
    private static volatile int instanceCounter = 0
            ;
    private volatile int peerCount;
    private final int k;
    private volatile int[] buckets;
    private final byte[] nodeAddress;
    private volatile MemoryDB<Peer> peerStore;

    public PeerStorage(byte[] nodeAddress, int k) {
        instanceCounter++;

        this.nodeAddress = nodeAddress;
        this.peerCount = 0;
        this.k = k;
        this.buckets = new int[160];

        this.peerStore = new MemoryDB<>("peerStore-" + Integer.toString(instanceCounter));
    }

    public void add(Peer p) throws NoSpaceException {
        int bucketIndex = findBucket(p.getBucketAddress());

        if (buckets[bucketIndex] == k) throw new NoSpaceException();

        if (peerStore.contains(p.getBucketAddress())) return;

        buckets[bucketIndex]++;
        peerStore.put(p.getBucketAddress(), p);
        peerCount++;
    }

    public void remove(byte[] address) {
        if (peerStore.delete(address) != null) {
            int bucketIndex = findBucket(address);

            buckets[bucketIndex]--;
        }
    }

    public boolean contains(byte[] address) {
        return peerStore.contains(address);
    }

    public Peer getPeer(byte[] address) throws PeerNotFoundException {
        Peer p = peerStore.get(address);

        if (p == null) throw new PeerNotFoundException();

        return p;
    }

    public Peer getRandom() {
        return new ArrayList<Peer>(peerStore.values()).get(ThreadLocalRandom.current().nextInt(0, peerCount));
    }

    public ArrayList<Peer> getPeers() {
        return new ArrayList<Peer>(peerStore.values());
    }

    private int findBucket(byte[] address) {
        for (int i = 0; i < address.length; i++) {
            int temp = (address[i] ^ nodeAddress[i]) & 0xFF;

            if (temp != 0) {
                return i * 8 + Integer.numberOfLeadingZeros(temp) - 24;
            }
        }

        return -1;
    }
}
