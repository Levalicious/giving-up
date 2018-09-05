package network;

import java.util.ArrayList;
import java.util.HashMap;
import network.MessageProtos.Message;

public class Node {
    HashMap<NodeInfo,Connections> peerList;
    ArrayList<Message> received;
    ArrayList<Message> broadcastPool;
    ArrayList<Message> messagePool;

    private Listener receiver;
    private Broadcaster sender;

    private NodeInfo self;

    public Node(NodeInfo self) {
        initializeLists();

        this.self = self;

    }

    private void initializeLists() {
        peerList = new HashMap<>();
        received = new ArrayList<>();
        broadcastPool = new ArrayList<>();
        messagePool = new ArrayList<>();
    }
}
