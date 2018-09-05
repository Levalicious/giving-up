package network;

import com.google.common.math.BigIntegerMath;
import com.google.protobuf.ByteString;
import crypto.ECDSA.ECKey;
import util.BIUtil;
import util.trie.kTrie;
import util.trie.kTrieNode;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import static crypto.Hash.k256;
import static util.ByteUtil.concat;

public class Node {
    private byte[] ostart = {(byte)0x00};
    private Thread peerThread;
    private byte[] buf = new byte[1024];
    private DatagramSocket socket;
    //private static HashMap<String, >
    NodeInfo self;
    kTrie peerTree;
    HashMap<byte[],Connections> peerList;
    ArrayList<byte[]> receivePool;
    ArrayList<byte[]> broadcastPool;
    ArrayList<NetworkMessageProtos.NetworkMessage> messagePool;
    Broadcaster out;
    Listener in;

    public Node() {
    }

    public Node(byte[] privKey, String IP, int port) {
        this.self = new NodeInfo(privKey, IP, port);
        peerTree = new kTrie(5);
        in = new Listener(peerList,self.getID(),receivePool,broadcastPool,messagePool);
        out = new Broadcaster(peerList,peerTree.getLeaves(),broadcastPool,messagePool);
    }

    public void addNode(NodeInfo n) {
        peerTree.add(n);
        boolean valid = false;
        for(Object e : peerTree.getLeaves().entrySet()) {
            Map.Entry<String, kTrieNode> x = (Map.Entry<String, kTrieNode>)e;
            if(x.getValue().getValues().contains(n)) valid = true;
        }

        if(valid) peerList.put(n.getID(),new Connections(n));
    }

}
