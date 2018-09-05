package org.levk.p2pnet;

import org.levk.p2pnet.util.serialization.RLP;

import java.security.SecureRandom;

public class Main {
    static SecureRandom rand = new SecureRandom();
    public static void main(String[] args) {
        byte[][] data = randomSet(50, 120);
        byte[][] almostEncoded = new byte[50][];

        for (int i = 0; i < data.length; i++) {
            almostEncoded[i] = RLP.encodeElement(data[i]);
        }

        byte[] encoded = RLP.encode(almostEncoded);

        System.out.println(encoded.length);
    }

    public static byte[][] randomSet(int i, int j) {
        byte[][] out = new byte[i][j];

        for (int k = 0; k < i; k++) {
            rand.nextBytes(out[k]);
        }

        return out;
    }
}
