package crypto;

import crypto.keccak.Keccak256;
import crypto.keccak.Keccak512;

import java.security.MessageDigest;

public class Hash {
    private static MessageDigest md;

    public static byte[] k256(byte[] in) {
        md = new Keccak256();
        return md.digest(in);
    }

    public static byte[] k512(byte[] in) {
        md = new Keccak512();
        return md.digest(in);
    }
}
