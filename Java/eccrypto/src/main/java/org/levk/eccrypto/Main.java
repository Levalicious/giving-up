package org.levk.eccrypto;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;
import org.levk.eccrypto.ecdsa.ECKey;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class Main {
    static long average = 0;
    static long n = 0;

    static long verAverage = 0;

    public static void main(String[] args) throws Exception {
        boolean running = true;

        SecureRandom rand = new SecureRandom();
        byte[] privkey = new byte[32];
        byte[] message = new byte[512];


        System.out.println("Average times: ");
        while (n < 1000000) {
            n++;
            MessageDigest md = new Keccak.Digest256();
            rand.nextBytes(privkey);
            rand.nextBytes(message);

            ECKey key = new ECKey();

            byte[] messagehash = md.digest(message);


            long signStart = System.nanoTime();
            ECKey.ECDSASignature sig = key.sign(messagehash);
            long signStop = System.nanoTime();

            long verStart = System.nanoTime();
            boolean valid = key.verify(messagehash, sig);
            long verStop = System.nanoTime();

            long signTime = (signStop - signStart);
            long verTime = (verStop - verStart);

            updateAverage(signTime, n);
            updateVerAverage(verTime, n);

            System.out.print("\rTo sign: " + average + " ns. To verify: " + verAverage + " ns. Round number: " + n);
        }
    }

    public static void updateAverage(long x, long n) {
        if (average != 0) {
            long num = (x + n * average);
            long den = n + 1;

            average = num / den;
        } else {
            average = x;
        }
    }

    public static void updateVerAverage(long x, long n) {
        if (verAverage != 0) {
            long num = (x + n * verAverage);
            long den = n + 1;

            verAverage = num / den;
        } else {
            verAverage = x;
        }
    }
}
