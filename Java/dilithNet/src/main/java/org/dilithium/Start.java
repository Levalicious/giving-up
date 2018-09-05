package org.dilithium;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.dilithium.crypto.ecdsa.ECKey;
import org.dilithium.network.Packet;
import org.dilithium.network.messages.uMessage;

import java.security.SecureRandom;

import static org.dilithium.crypto.Hash.keccak256;

public class Start {
    public static void main(String[] args) throws Exception {
        ECKey key1 = new ECKey();
        ECKey key2 = new ECKey();
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");

        byte[] temp = new byte[300];

        rand.nextBytes(temp);

        Packet p1 = new Packet(key2.getAddress(), 1, temp, key1.getPrivKeyBytes());

        Packet p2 = new Packet(p1.getEncoded());


        System.out.println(p1.getMessageType() + " " + p2.getMessageType());

        System.out.println(ByteUtils.equals(temp, p2.getPayload()));
    }
}
