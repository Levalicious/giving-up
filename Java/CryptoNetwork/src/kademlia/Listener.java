package kademlia;

import kademlia.struct.NetworkMessageProtos.*;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class Listener extends Thread {
    private DatagramSocket socket;
    HashMap<byte[], Connections> peerList;
    ArrayList<NetworkMessage> received;
    ArrayList<byte[]> broadcastPool;
    ArrayList<>

}
