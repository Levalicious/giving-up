package kademlia;

import java.util.ArrayList;

public class Connections {
    public ArrayList<byte[]> receivePool;

    public NodeInfo peer;

    public Connections(NodeInfo peer) {
        this.peer = peer;
    }
}
