package org.levk.UDP2Pv2.network.peerStorage;

import org.bouncycastle.util.encoders.Hex;
import org.levk.UDP2Pv2.util.ByteArrayWrapper;

import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

import java.util.*;

public class PeerSet implements Comparable<PeerSet> {
    private volatile int peerCount;

    private final int k;

    private volatile int[] buckets;

    private final Peer self;

    private final byte[] nodeAddress;

    private volatile HashMap<ByteArrayWrapper, Peer> peerStore;

    public PeerSet() throws UnknownHostException {
        this(new Peer(new byte[2], new byte[4], new byte[2]), 1);
    }

    public PeerSet(Peer self, int k) {
        this.self = self;
        this.nodeAddress = self.getBucketAddress();
        this.peerCount = 0;
        this.k = k;
        this.buckets = new int[160];

        this.peerStore = new HashMap<>();
    }

    public void add(Peer p) throws NoSpaceException {
        ByteArrayWrapper temp = new ByteArrayWrapper(p.getBucketAddress());
        /* Calculate relevant bucket */
        int bucketIndex = findBucket(p.getBucketAddress());

        /* Check if bucket has space. If not, throw exception. */
        if (buckets[bucketIndex] == k) throw new NoSpaceException();

        if (peerStore.containsKey(temp)) {
            return;
        }

        buckets[bucketIndex]++;
        peerStore.put(temp, p);
        peerCount++;
    }

    public void remove(byte[] address) {
        ByteArrayWrapper temp = new ByteArrayWrapper(address);
        if (peerStore.remove(temp) != null) {
            /* Calculate relevant bucket */
            int bucketIndex = findBucket(address);

            buckets[bucketIndex]--;
        }
    }

    public boolean contains(byte[] address) {
        return peerStore.containsKey(new ByteArrayWrapper(address));
    }

    public Peer getPeer(byte[] address) throws PeerNotFoundException {
        ByteArrayWrapper temp = new ByteArrayWrapper(address);
        Peer p = peerStore.get(temp);

        if (p == null) throw new PeerNotFoundException();

        return p;
    }

    public Peer getRandom() {
        /* Not using SecureRandom because it's super easy to "maliciously" send peers
         * no matter how this method works, and I can't fix that.
         * So untils some magical fix appears, I'm using the faster option.*/
        return new ArrayList<Peer>(peerStore.values()).get(ThreadLocalRandom.current().nextInt(0, peerCount));
    }

    public List<Peer> getPeers() {
        List<Peer> out = new ArrayList<>();

        for (Map.Entry<ByteArrayWrapper, Peer> e : peerStore.entrySet()) {
            out.add(e.getValue());
        }

        return out;
    }

    public int getPeerCount() {
        return peerCount;
    }

    public Peer getSelf() throws UnknownHostException {
        return new Peer(self.getVersionBytes(), self.getAddress(), self.getPortBytes());
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

    @Override
    public String toString() {
        return Hex.toHexString(nodeAddress);
    }

    @Override
    public int compareTo(PeerSet p) {
        for (int i = 0; i < 20; i++) {
            if ((this.nodeAddress[i] & 0xFF) > (p.nodeAddress[i] & 0xFF)) return -1;
            if ((this.nodeAddress[i] & 0xFF) < (p.nodeAddress[i] & 0xFF)) return 1;
        }

        return 0;
    }
}
