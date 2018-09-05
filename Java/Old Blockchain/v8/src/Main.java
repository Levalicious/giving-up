import com.google.protobuf.ByteString;
import crypto.ecdsa.ECKey;

import static crypto.Hash.keccak256;

public class Main {
    public static void main(String[] args) throws Exception {
        ECKey node1 = new ECKey();
        ECKey node2 = new ECKey();

        ECKey.ECDSASignature sig = node1.sign(keccak256("Test".getBytes()));
        byte[] sigV = {sig.v};

        byte[] messageData = new byte[378];
        MessageProtos.Message m = MessageProtos.Message.newBuilder().setSetIndex(Integer.MAX_VALUE).setSetSize(Integer.MAX_VALUE).setTarget(ByteString.copyFrom((new ECKey()).getAddress())).setMessageType(Integer.MAX_VALUE).setData(ByteString.EMPTY).setR(ByteString.copyFrom(sig.r.toByteArray())).setS(ByteString.copyFrom(sig.s.toByteArray())).setV(ByteString.copyFrom(sigV)).setMessageHash(ByteString.copyFrom(keccak256(messageData))).setHops(256).build();

        System.out.println(m.toByteArray().length);

        /* A message is, at most, 163 bytes without any data, including a target. */

        MessageProtos.Message m12 = MessageProtos.Message.newBuilder().setSetIndex(Integer.MAX_VALUE).setSetSize(Integer.MAX_VALUE).setTarget(ByteString.copyFrom((new ECKey()).getAddress())).setMessageType(Integer.MAX_VALUE).setData(ByteString.copyFrom(messageData)).setR(ByteString.copyFrom(sig.r.toByteArray())).setS(ByteString.copyFrom(sig.s.toByteArray())).setV(ByteString.copyFrom(sigV)).setMessageHash(ByteString.copyFrom(keccak256(messageData))).setHops(256).build();

        /* Any change in size, in this case, is directly proportional to the byte length of the datafield. */

        System.out.println(m12.toByteArray().length);

        MessageProtos.Message m2 = MessageProtos.Message.newBuilder().setSetIndex(Integer.MAX_VALUE).setSetSize(Integer.MAX_VALUE).setMessageType(Integer.MAX_VALUE).setData(ByteString.EMPTY).setR(ByteString.copyFrom(sig.r.toByteArray())).setS(ByteString.copyFrom(sig.s.toByteArray())).setV(ByteString.copyFrom(sigV)).setMessageHash(ByteString.copyFrom(keccak256(messageData))).setHops(256).build();

        /* A message without a target is, at most, 129 bytes. */

        System.out.println(m2.toByteArray().length);

        MessageProtos.Message m22 = MessageProtos.Message.newBuilder().setSetIndex(Integer.MAX_VALUE).setSetSize(Integer.MAX_VALUE).setMessageType(Integer.MAX_VALUE).setData(ByteString.copyFrom(messageData)).setR(ByteString.copyFrom(sig.r.toByteArray())).setS(ByteString.copyFrom(sig.s.toByteArray())).setV(ByteString.copyFrom(sigV)).setMessageHash(ByteString.copyFrom(keccak256(messageData))).setHops(256).build();

        /* Any change in size, in this case also, is directly proportional to the byte length of the datafield. */
        System.out.println(m22.toByteArray().length);

        if((m12.toByteArray().length - m.toByteArray().length) != messageData.length) System.out.println("Datafield length affect on message was incorrectly assumed for targetted messages. An error of " + ((m12.toByteArray().length - m.toByteArray().length) - messageData.length) + " was observed.");
        if((m22.toByteArray().length - m2.toByteArray().length) != messageData.length) System.out.println("Datafield length affect on message was incorrectly assumed for untargetted messages. An error of " + ((m22.toByteArray().length - m2.toByteArray().length) - messageData.length) + " was observed.");

        /*
        While the datafield is 127 bytes or less, 1 byte added to the datafield is 1 byte added to the serialized message.
        While the datafield is 16383 bytes or less, 1 extra byte is added.
        While the datafield is 2097151 bytes or less, 2 extra bytes are added.
         */

        /*
        For semi-safe routing (so packets aren't automatically dropped/ignored by routers/dns) a
        targeted packet has a max datafield of 344.
        A non targeted packet has a max datafield of 378 bytes.
         */
    }
}
