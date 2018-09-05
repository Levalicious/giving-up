package network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import network.messageTypes.PeerProtos;
import network.messageTypes.PeerProtos.*;
import resources.Constants;

public class Serialize {
    public static byte[] serializePeerList(ArrayList<Peer> peerList) {
        ArrayList<PeerProtos.Peer> temp = new ArrayList<>();

        for(Peer p : peerList) {
            temp.add(PeerProtos.Peer.newBuilder().setIP(ByteString.copyFrom(p.ip.getAddress())).setPort(p.port).setId(ByteString.copyFrom(p.id)).build());
        }

        return PeerList.newBuilder().addAllPeers(temp).build().toByteArray();
    }

    public static ArrayList<Peer> deserializePeerList(byte[] in) {
        try {
            ArrayList<Peer> temp = new ArrayList<>();

            for(PeerProtos.Peer p : PeerList.parseFrom(in).getPeersList()) {
                temp.add(new Peer(p.getIP().toByteArray(), p.getPort(), p.getId().toByteArray(), Constants.node.id));
            }

            return temp;
        } catch(InvalidProtocolBufferException i) {
            return null;
        }
    }

    public static byte[] ipStringToBytes(String s) {
        try {
            InetAddress ip = InetAddress.getByName(s);
            return ip.getAddress();
        } catch (UnknownHostException u) {
            return null;
        }
    }

    public static String ipBytesToString(byte[] in) {
        String s = new String();

        for(byte b : in) {
            s = s + (b & 0x0FF);
        }

        return s;
    }
}
