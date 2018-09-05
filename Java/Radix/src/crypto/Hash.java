package crypto;

import crypto.groestl.Groestl256;
import crypto.keccak.Keccak256;
import crypto.keccak.Keccak512;
import org.bouncycastle.jcajce.provider.digest.Blake2b;
import org.bouncycastle.jcajce.provider.digest.Skein;

import java.security.MessageDigest;
import java.util.Arrays;

import static util.ByteUtil.concat;
import static util.ByteUtil.xor;
import static util.Hex.getHex;

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

    public static byte[] hash(byte[] in) {
        byte[] keccak = k256(in);
        md = new Skein.Digest_256_256();
        byte[] skein = md.digest(in);
        MessageDigest md2 = new Blake2b.Blake2b256();
        byte[] blake = md2.digest(in);
        byte[] groestl = new Groestl256().digest(in);

        System.out.println("Keccak   : " + getHex(keccak).replaceAll("..", "$0 "));
        System.out.println("Skein    : " + getHex(skein).replaceAll("..", "$0 "));
        System.out.println("Blake    : " + getHex(blake).replaceAll("..", "$0 "));
        System.out.println("Groestl  : " + getHex(groestl).replaceAll("..", "$0 "));

        return xor(xor(xor(keccak, skein), blake), groestl);
    }
}
