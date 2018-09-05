package network;

import crypto.ECDSA.ECKey;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import static crypto.Hash.k256;
import static util.ByteUtil.concat;
import static util.Hex.fromHex;
import static util.Hex.getHex;

public class EchoServer extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[1024];

    public EchoServer() {
        try{
            socket = new DatagramSocket(4445);
        } catch(Exception e) {
            System.out.println("Socket Initialization failed."  + e);
        }
    }

    public void run() {
        running = true;

        while (running) {
            byte[] ostart = new byte[1];
            ostart[0] = (byte)0x00;
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch(Exception e) {
                System.out.println("socket.receive failed."  + e);
            }

            byte[] received = packet.getData();

            while(getHex(received).startsWith("00")) {
                received = fromHex(getHex(received).substring(2));
            }

            NetworkMessageProtos.NetworkMessage message;

            boolean validSig = false;
            try {
                message = NetworkMessageProtos.NetworkMessage.parseFrom(received);
                ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(message.getR().toByteArray(),message.getS().toByteArray(),message.getV().toByteArray()[0]);
                System.out.println(message.toString());

                validSig = ECKey.verify(k256(message.getData().toByteArray()),sig, ECKey.signatureToKeyBytes(k256(message.getData().toByteArray()),sig));

            }catch(Exception e) {
                System.out.println("Failed to parse message." + e);
                e.printStackTrace();
            }

            System.out.println(validSig);



            /*
            ECKey.ECDSASignature signature = new ECKey.ECDSASignature(BigInteger.ONE);
            try {
                System.out.println(ECKey.verify(Arrays.copyOfRange(received,0,3),signature,ECKey.signatureToKeyBytes(Arrays.copyOfRange(received,0,3),signature)));
            }catch(Exception e) {
                System.out.println("SigCheck failed");
            }
            */

            System.out.println("Server Received " + getHex(received));

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);

            try{
                socket.send(packet);
            } catch(Exception e) {
                System.out.println("socket.send failed."  + e);
            }
        }
        socket.close();
    }
}