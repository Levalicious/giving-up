package network;

import java.security.SignatureException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;
import java.util.stream.IntStream;

import com.google.protobuf.ByteString;
import network.messageTypes.MessageProtos.*;
import crypto.ecdsa.ECKey;

import static crypto.Hash.keccak256;
import static util.ByteUtil.concat;

public class NetUtils {
    private static int targetedPacketDataSize = 344;
    private static int untargetedPacketDataSize = 378;

    private static byte[] startByte = {(byte)0xFF};
    private static byte[] zero = {(byte)0x00};

    public static BitSet fromByteArray(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    public static String fromBitSet(BitSet set) {
        final StringBuilder buffer = new StringBuilder(set.length());
        IntStream.range(0, set.length()).mapToObj(i -> set.get(i) ? '1' : '0').forEach(buffer::append);
        String temp = buffer.reverse().toString();
        while(temp.length() < 256) {
            temp = 0 + temp;
        }

        return temp;
    }

    public static boolean validateMessage(Message[] message) {
        for(int i = 0; i < message.length; i++) {
            if(!validateMessage(message[i])) return false;
        }

        return true;
    }

    public static boolean validateMessage(Message message) {
        ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(message.getR().toByteArray(), message.getS().toByteArray(), message.getV().toByteArray()[0]);
        try {
            return ECKey.verify(keccak256(message.getData().toByteArray()), sig, ECKey.signatureToKeyBytes(keccak256(message.getData().toByteArray()), sig));
        } catch (SignatureException s) {
            return false;
        }
    }

    public static Message[] createMessage(int messageType, byte[] data, byte[] privKey) {
        Objects.requireNonNull(data);
        return splitUntargetedMessage(messageType, data, privKey);
    }

    public static Message[] createMessage(byte[] target, int messageType, byte[] data, byte[] privKey) {
        Objects.requireNonNull(data);
        return splitTargetedMessage(target, messageType, data, privKey);
    }

    private static Message createMessage(int setIndex, int setSize, int messageType, byte[] data, byte[] identifier, byte[] privKey) {
        ECKey.ECDSASignature sig = (ECKey.fromPrivate(privKey)).sign(keccak256(data));
        byte[] sigV = {sig.v};
        return Message.newBuilder().setSetIndex(setIndex).setSetSize(setSize).setMessageType(messageType).setData(ByteString.copyFrom(data)).setR(ByteString.copyFrom(sig.r.toByteArray())).setS(ByteString.copyFrom(sig.s.toByteArray())).setV(ByteString.copyFrom(sigV)).setMessageHash(ByteString.copyFrom(identifier)).setHops(0).build();
    }

    private static Message createMessage(int setIndex, int setSize, byte[] target, int messageType, byte[] data, byte[] identifier, byte[] privKey) {
        ECKey.ECDSASignature sig = (ECKey.fromPrivate(privKey)).sign(keccak256(data));
        byte[] sigV = {sig.v};
        return Message.newBuilder().setSetIndex(setIndex).setSetSize(setSize).setTarget(ByteString.copyFrom(target)).setMessageType(messageType).setData(ByteString.copyFrom(data)).setR(ByteString.copyFrom(sig.r.toByteArray())).setS(ByteString.copyFrom(sig.s.toByteArray())).setV(ByteString.copyFrom(sigV)).setMessageHash(ByteString.copyFrom(identifier)).setHops(0).build();
    }

    public static Message iterateHops(Message message) {
        if(message.hasTarget()) {
            return iterateHopsTargeted(message);
        } else {
            return iterateHopsUntargeted(message);
        }
    }

    private static Message iterateHopsUntargeted(Message message) {
        return Message.newBuilder().setSetIndex(message.getSetIndex()).setSetSize(message.getSetSize()).setMessageType(message.getMessageType()).setData(message.getData()).setR(message.getR()).setS(message.getS()).setV(message.getV()).setMessageHash(message.getMessageHash()).setHops(message.getHops() + 1).build();
    }

    private static Message iterateHopsTargeted(Message message) {
        return Message.newBuilder().setSetIndex(message.getSetIndex()).setSetSize(message.getSetSize()).setTarget(message.getTarget()).setMessageType(message.getMessageType()).setData(message.getData()).setR(message.getR()).setS(message.getS()).setV(message.getV()).setMessageHash(message.getMessageHash()).setHops(message.getHops() + 1).build();
    }

    private static Message[] splitUntargetedMessage(int messageType, byte[] data, byte[] privKey) {
        int bytes = untargetedPacketDataSize;

        data = concat(startByte, data);

        while((data.length % bytes) != 0) {
            data = concat(zero, data);
        }

        byte[] identifierHash = keccak256(data);

        Message[] mSet = new Message[data.length / bytes];

        for(int i = 1; i <= (data.length / bytes); i++) {
            mSet[i - 1] = createMessage(i, data.length / bytes, messageType, Arrays.copyOfRange(data, ((i - 1) * bytes), (i * bytes)), identifierHash, privKey);
        }

        return mSet;
    }

    private static Message[] splitTargetedMessage(byte[] target, int messageType, byte[] data, byte[] privKey) {
        int bytes = targetedPacketDataSize;

        data = concat(startByte, data);

        while((data.length % bytes) != 0) {
            data = concat(zero, data);
        }

        byte[] identifierHash = keccak256(data);

        Message[] mSet = new Message[data.length / bytes];

        for(int i = 1; i <= (data.length / bytes); i++) {
            mSet[i - 1] = createMessage(i, data.length / bytes, target, messageType, Arrays.copyOfRange(data, ((i - 1) * bytes), (i * bytes)), identifierHash, privKey);
        }

        return mSet;
    }

    public static byte[] getData(Message message) {
        byte[] data = message.getData().toByteArray();

        return removePadding(data);
    }

    public static byte[] getData(Message[] messages) {
        byte[] data = messages[0].getData().toByteArray();

        for(int i = 1; i < messages.length; i++) {
            data = concat(data, messages[i].getData().toByteArray());
        }

        return removePadding(data);
    }

    private static byte[] removePadding(byte[] data) {
        while(data[0] == (byte) 0x00) {
            data = Arrays.copyOfRange(data, 1, data.length);
        }

        if(data[0] == startByte[0]) {
            data = Arrays.copyOfRange(data, 1, data.length);
        }

        return data;
    }
}
