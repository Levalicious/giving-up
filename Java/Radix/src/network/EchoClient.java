package network;

import com.google.protobuf.ByteString;
import crypto.ECDSA.ECKey;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static crypto.Hash.k256;
import static util.ByteUtil.concat;
import static util.Hex.getHex;

public class EchoClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public EchoClient() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch(Exception e) {
            System.out.println("Socket Initialization failed."  + e);
        }
    }

    public void sendMessage(byte[] message) {

    }

    public boolean ping(NodeInfo self) {
        byte[] pingMessage = new byte[1];
        pingMessage[0] = (byte)0x01;
        ECKey.ECDSASignature sig = self.signMessage(k256(pingMessage));
        byte[] sigV = new byte[1];
        sigV[0] = sig.v;
        System.out.println(sig.v);

        NetworkMessageProtos.NetworkMessage message = NetworkMessageProtos.NetworkMessage.newBuilder().setSetIndex(1).setSetSize(1).setData(ByteString.copyFrom(pingMessage)).setR(ByteString.copyFrom(sig.r.toByteArray())).setS(ByteString.copyFrom(sig.s.toByteArray())).setV(ByteString.copyFrom(sigV)).build();
        byte[] temp = message.toByteArray();
        byte[] ostart = new byte[1];
        ostart[0] = (byte)0x00;

        while(temp.length < 1024) {
            temp = concat(ostart, temp);
        }

        try {
            DatagramPacket packet = new DatagramPacket(temp, temp.length, address, 4445);
            socket.setSoTimeout(5000);
            socket.send(packet);
            socket.receive(packet);
            byte[] received = packet.getData();
            System.out.println();
            return true;
        }catch(Exception e) {
            return false;
        }

    }

    public String sendEcho(String msg) {
        buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        try{
            socket.send(packet);
        } catch(Exception e) {
            System.out.println("socket.send failed."  + e);
        }
        packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch(Exception e) {
            System.out.println("socket.receive failed."  + e);
        }
        String received = new String(packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }
}
