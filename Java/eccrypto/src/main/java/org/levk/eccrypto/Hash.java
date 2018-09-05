package org.levk.eccrypto;

import org.bouncycastle.jcajce.provider.digest.Blake2b;
import org.bouncycastle.jcajce.provider.digest.Keccak;

import java.security.MessageDigest;

import static java.util.Arrays.copyOfRange;

public class Hash {
    private static MessageDigest md;

    public static byte[] keccak256(byte[] in) {
        md = new Keccak.Digest256();
        return md.digest(in);
    }

    public static byte[] keccak512(byte[] in) {
        md = new Keccak.Digest512();
        return md.digest(in);
    }

    public static byte[] keccak256Omit12(byte[] in) {
        byte[] hash = keccak256(in);
        return copyOfRange(hash, 12, hash.length);
    }

    public static byte[] blake256(byte[] in) {
        md = new Blake2b.Blake2b256();
        return md.digest(in);
    }

    public static byte[] blake512(byte[] in) {
        md = new Blake2b.Blake2b512();
        return md.digest(in);
    }
}
