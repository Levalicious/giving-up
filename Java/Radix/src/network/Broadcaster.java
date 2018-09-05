package network;

import util.BIUtil;
import util.trie.Buckets;
import util.trie.kTrieNode;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import static util.ByteUtil.concat;

public class Broadcaster extends Thread {
    private ArrayList<byte[]> broadcastPool;
    private ArrayList<NetworkMessageProtos.NetworkMessage> messagePool;
    private DatagramSocket socket;
    private HashMap<byte[], Connections> peerList;
    private Buckets peers;
    private boolean running;
    private byte[] ostart = {(byte)0x00};
    private byte[] buf = new byte[1024];

    protected Broadcaster(HashMap<byte[],Connections> peerList,Buckets peers, ArrayList<byte[]> broadcastPool, ArrayList<NetworkMessageProtos.NetworkMessage> messagePool) {
        this.peerList = peerList;
        this.peers = peers;
        this.broadcastPool = broadcastPool;
        this.messagePool = messagePool;
        try {
            socket = new DatagramSocket(40424);
        }catch(SocketException s) {
            System.out.println("Socket initialization failed.");
            s.printStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            if(!broadcastPool.isEmpty()) {
                broadcastMessage(broadcastPool.get(0));
                broadcastPool.remove(0);
            }

            if(!messagePool.isEmpty()) {
                sendNearest(messagePool.get(0).getTarget().toByteArray(),messagePool.get(0).getData().toByteArray());
                messagePool.remove(0);
            }
        }
    }

    private void broadcastMessage(byte[] message) {
        for(Map.Entry<byte[], Connections> peer : peerList.entrySet()) {
            sendMessage(peer.getValue().peer.getIP(), peer.getValue().peer.getPort(), message);
        }
    }

    private byte[] padMessage(byte[] in) {
        while (in.length < buf.length) {
            in = concat(ostart, in);
        }

        return in;
    }

    private void sendMessage(String addr, int port, byte[] message) {
        message = padMessage(message);

        try{
            InetAddress address = InetAddress.getByName(addr);
            DatagramPacket packet = new DatagramPacket(message, buf.length, address, port);
            socket.send(packet);
        }catch(Exception e) {
            System.out.println("Packet sending failed.");
            e.printStackTrace();
        }
    }

    private void sendNearest(byte[] target, byte[] message) {
        BitSet s = NodeInfo.fromByteArray(target);

        byte[] min = new byte[32];
        BigInteger minDist = (BigInteger.TWO).pow(256);
        for(Map.Entry<byte[], Connections> e : peerList.entrySet()) {
            BitSet temp = s;
            temp.xor(NodeInfo.fromByteArray(e.getKey()));
            BigInteger dist = new BigInteger((concat(ostart,temp.toByteArray())));
            if(BIUtil.isLessThan(dist, minDist)) {
                min = e.getKey();
                minDist = dist;
            }
        }

        sendMessage(peerList.get(min).peer.getIP(), peerList.get(min).peer.getPort(), message);
    }

    public NodeInfo getNearest(byte[] target) {
        String s = NodeInfo.fromBitSet(NodeInfo.fromByteArray(target));
        int start = 0;
        int end = 0;
        int bucket = 0;
        if(s.startsWith("1")) {
            bucket = 1;
        }else {
            bucket = s.indexOf('1') + 1;
        }

        String temp = "";
        for(int i = 0; i < bucket; i++) {
            temp = temp + "1";
        }

        kTrieNode b = null;
        while(b == null) {
            if(peers.get(temp) != null) {
                b = (kTrieNode)peers.get(temp);
            }else {
                if(temp.length() > 0) {
                    temp = temp.substring(0,temp.length() - 1);
                }
                temp = temp + "0";
                if(peers.get(temp) != null) {
                    b = (kTrieNode)peers.get(temp);
                }
            }

        }

        NodeInfo min = new NodeInfo();
        BigInteger minDist = (BigInteger.TWO).pow(256);
        for(Object n : b.getValues()) {
            NodeInfo e = (NodeInfo)n;
            BitSet temp2 = NodeInfo.fromByteArray(target);
            temp2.xor(NodeInfo.fromByteArray((e.getID())));
            BigInteger dist = new BigInteger((concat(ostart,temp2.toByteArray())));
            if(BIUtil.isLessThan(dist, minDist)) {
                min = e;
                minDist = dist;
            }
        }

        return min;
    }
}
