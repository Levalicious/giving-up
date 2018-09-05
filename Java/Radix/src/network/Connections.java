package network;

import java.util.ArrayList;

public class Connections extends Thread {
    public ArrayList<byte[]> receivePool;

    public NodeInfo peer;

    public Connections(NodeInfo peer) {
        this.peer = peer;
    }
}
