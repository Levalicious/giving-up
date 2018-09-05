package org.levk.UDP2Pv3.network;

import org.levk.UDP2Pv3.network.peerStorage.Peer;

import java.nio.channels.SocketChannel;

public class PeerSocket extends SocketChannel {
    Peer peer;
    SocketChannel socket;

    public PeerSocket(Peer peer, SocketChannel socket) {

    }


}
