package org.dilithium;


import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.BigIntegers;
import org.dilithium.crypto.ChaCha;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.dilithium.util.ByteUtil.bigIntegerToBytes;

public class Start {
    public static void main(String[] args) throws Exception {

        /*
        BigInteger pKey = new BigInteger(Hex.fromHex("0000000000000000000000000000000000000000000000000000000000000001"));

        long started = System.currentTimeMillis();
        Point pub = secp256k1.pointMultiply(pKey, secp256k1.getG());
        long ended = System.currentTimeMillis();
        System.out.println("EdDSA point multiplication took " + (ended - started) + " ms");

        System.out.println(pKey.toString(16));
        System.out.println(pub.toString(false));
        System.out.println(secp256k1.isOnCurve(pub));

        System.out.println();
        System.out.println(secp256k1.sign(pKey, new BigInteger(Hex.fromHex("00967a4341b39ba3c8b982f4460e94f5f10462e9e671c5f1db5a3dfee3a4ed8909")), new BigInteger(keccak256("Hello".getBytes()))).toString());
        */
        BigInteger keyInt = BigIntegers.createRandomInRange(BigInteger.ZERO, BigInteger.TWO.pow(256), new SecureRandom());
        BigInteger keyInt2 = BigIntegers.createRandomInRange(BigInteger.ZERO, BigInteger.TWO.pow(256), new SecureRandom());

        byte[] key = bigIntegerToBytes(keyInt);
        byte[] key2 = bigIntegerToBytes(keyInt2);


        byte[] message = "FUck I really really really really wanna die sometimes when there's enough gay shit to go around penguin butt nuggets".getBytes();
        byte[] encrypted = ChaCha.encrypt(key, message);
        byte[] encrypteded = ChaCha.encrypt(key2, encrypted);
        byte[] decrypted = ChaCha.encrypt(key, encrypteded);
        byte[] decrypteded = ChaCha.encrypt(key2, decrypted);

        System.out.println(Hex.getHex(message) + " : " + message.length + " : " + new String(message));
        System.out.println(Hex.getHex(encrypteded) + " : " + encrypteded.length + " : ");
        System.out.println(Hex.getHex(decrypteded) + " : " + decrypteded.length + " : " + new String(decrypteded));


        /*
        boolean running = true;

        while (running) {
            ECKey key = new ECKey();

            Point pub = secp256k1.pointMultiply(key.getPrivKey(), secp256k1.getG());

            if(!pub.toString(false).equals(Hex.getHex(key.getPubKey()))) {
                running = false;
                System.out.println(pub.toString(false));
                System.out.println(Hex.getHex(key.getPubKey()));
            }
        }
        */
    }
}
