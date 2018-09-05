package org.levk.UDP2Pv3.network;

import org.levk.UDP2Pv3.network.peerStorage.Peer;
import org.levk.UDP2Pv3.network.peerStorage.PeerStorage;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class P2PSocket {
    private final SecureRandom rand;

    private volatile Queue<byte[]> toSend;
    private volatile Queue<byte[]> received;

    private final int networkId;
    private final int version;
    private final int port;
    private boolean running;
    private
    private volatile PeerStorage peers;
    private ThreadPoolExecutor workerPool;


    public P2PSocket(int version, int threadCount, int networkId, Peer self, int k, int port) {
        if (threadCount < 1) throw new RuntimeException("A P2PSocket instance requires at least 1 threads.");

        this.version = version;
        this.workerPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
        this.rand = new SecureRandom();
        this.toSend = new LinkedList<>();
        this.received = new LinkedList<>();

        this.networkId = networkId;
        this.port = port;

        this.peers = new PeerStorage(self.getBucketAddress(), k);

        this.running = true;
    }

    /* Threads use this to send & receive messages to & from accepted peers. */
    public void run() {

    }

    /* Threads use this to accept & confirm new peers */
    public void serverRun() {

    }
}
