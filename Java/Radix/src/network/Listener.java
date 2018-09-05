package network;

import com.google.common.graph.Network;
import com.google.protobuf.InvalidProtocolBufferException;
import core.types.Payload;
import crypto.ECDSA.ECKey;
import util.ByteUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import static crypto.Hash.k256;
import static util.ByteUtil.concat;
import static util.Hex.fromHex;
import static util.Hex.getHex;

public class Listener extends Thread {
    private DatagramSocket socket;
    HashMap<byte[], Connections> peerList;
    ArrayList<byte[]> received;
    ArrayList<byte[]> broadcastPool;
    ArrayList<NetworkMessageProtos.NetworkMessage> messagePool;
    private boolean running;
    private byte[] self;
    private byte[] buf = new byte[1024];

    protected Listener(HashMap<byte[],Connections> peerList, byte[] self, ArrayList<byte[]> receivePool, ArrayList<byte[]> bcastPool, ArrayList<NetworkMessageProtos.NetworkMessage> mPool) {
        this.peerList = peerList;
        this.self = self;
        this.received = receivePool;
        this.broadcastPool = bcastPool;
        this.messagePool = mPool;
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
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try{
                socket.receive(packet);
            }catch(IOException i) {
                System.out.println("Packet receive failed.");
                i.printStackTrace();
            }

            byte[] received = packet.getData();

            while(getHex(received).startsWith("00")) {
                received = fromHex(getHex(received).substring(2));
            }

            NetworkMessageProtos.NetworkMessage message;
            byte[] dat = null;
            try{
                message = NetworkMessageProtos.NetworkMessage.parseFrom(received);

                if(!toRelay(message)) {
                    if(checkMessage(message)) dat = message.getData().toByteArray();

                    if(dat != null) {
                        if(peerList.containsKey(getPubKey(message))) peerList.get(getPubKey(message)).receivePool.add(dat);
                    }
                } else {
                    if(checkRelayMessage(message)) {
                        messagePool.add(message);
                    }
                }
            }catch(InvalidProtocolBufferException i) {
                System.out.println("Message parsing failed.");
                i.printStackTrace();
            }
        }

        socket.close();
    }

    private boolean toRelay(NetworkMessageProtos.NetworkMessage message) {
        if(message.hasTarget()) {
            if(ByteUtil.toHexString(this.self).equals(ByteUtil.toHexString(message.getTarget().toByteArray()))) {
                return false;
            }

            return true;
        }

        return false;
    }

    private static boolean checkMessage(NetworkMessageProtos.NetworkMessage message) {
        byte[] data = message.getData().toByteArray();
        byte[] r = message.getR().toByteArray();
        byte[] s = message.getS().toByteArray();
        byte v = message.getV().toByteArray()[0];

        try {
            ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(r,s,v);
            return ECKey.verify(k256(data), sig, getPubKey(message));
        }catch(Exception e) {
            return false;
        }
    }

    private static boolean checkRelayMessage(NetworkMessageProtos.NetworkMessage message) {
        byte[] target = message.getTarget().toByteArray();
        byte[] data = message.getData().toByteArray();
        byte[] r = message.getR().toByteArray();
        byte[] s = message.getS().toByteArray();
        byte v = message.getV().toByteArray()[0];

        try {
            ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(r,s,v);
            return ECKey.verify(k256(concat(target, data)), sig, getRelayPubKey(message));
        }catch(Exception e) {
            return false;
        }
    }

    private static byte[] getPubKey(NetworkMessageProtos.NetworkMessage message) {
        byte[] data = message.getData().toByteArray();
        byte[] r = message.getR().toByteArray();
        byte[] s = message.getS().toByteArray();
        byte v = message.getV().toByteArray()[0];

        try {
            ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(r,s,v);
            return ECKey.signatureToKeyBytes(k256(data),sig);
        }catch(Exception e) {
            return null;
        }
    }

    private static byte[] getRelayPubKey(NetworkMessageProtos.NetworkMessage message) {
        byte[] target = message.getTarget().toByteArray();
        byte[] data = message.getData().toByteArray();
        byte[] r = message.getR().toByteArray();
        byte[] s = message.getS().toByteArray();
        byte v = message.getV().toByteArray()[0];

        try {
            ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(r,s,v);
            return ECKey.signatureToKeyBytes(k256(concat(target, data)),sig);
        }catch(Exception e) {
            return null;
        }
    }
}
