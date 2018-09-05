package org.levk.UDP2Pv2.util;

import org.bouncycastle.jcajce.provider.digest.Blake2b;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jcajce.provider.digest.SHA1;
import org.bouncycastle.jcajce.provider.digest.SHA256;

import java.security.MessageDigest;
import java.util.Arrays;

public class HashUtil {
    private static MessageDigest blake = new Blake2b.Blake2b256();
    private static MessageDigest keccak = new Keccak.Digest256();
    private static MessageDigest sha1Bouncy = new SHA1.Digest();
    private static MessageDigest sha2Bouncy = new SHA256.Digest();

    public static byte[] blake2(byte[] input) {
        blake.reset();
        return blake.digest(input);
    }

    public static byte[] sha3(byte[] input) {
        keccak.reset();
        return keccak.digest(input);
    }

    public static byte[] blake2omit12(byte[] input) {
        byte[] hash = blake2(input);
        return Arrays.copyOfRange(hash, 12, hash.length);
    }

    public static byte[] blake2ECC(byte[] input) {
        byte[] hash = blake2(input);
        return Arrays.copyOfRange(hash, 28, hash.length);
    }

    public static byte[] sha1B(byte[] input) {
        sha1Bouncy.reset();
        return sha1Bouncy.digest(input);
    }

    public static byte[] sha2B(byte[] input) {
        sha2Bouncy.reset();
        return sha2Bouncy.digest(input);
    }

    public static byte[] sha2omit12(byte[] input) {
        byte[] hash = sha2B(input);
        return Arrays.copyOfRange(hash, 28, hash.length);
    }
}