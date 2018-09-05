package crypto;

import core.types.transaction.Transaction;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.jcajce.provider.digest.Blake2b;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import resources.Config;
import sun.plugin2.message.Message;

import static utils.Base58.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;

import static crypto.Parameters.*;
import static utils.HexUtils.getHex;

public class Hash implements Config {
    public static String sha256(String in) {
        return keccak.getHash(process(in),KECCAK_256);
    }

    public static String sha512(String in) {
        return keccak.getHash(process(in),KECCAK_512);
    }

    public static String addressGen(byte[] in) {
        return encode(in);
    }

    private static String getMerkleRoot(ArrayList<Transaction> transactions) {
        int count = transactions.size();
        ArrayList<String> prevLayer = new ArrayList<String>();

        for(Transaction transaction : transactions) {
            prevLayer.add(transaction.getHash());
        }

        ArrayList<String> treeLayer = prevLayer;

        while(count > 1) {
            treeLayer = new ArrayList<String>();

            for(int i = 1; i < prevLayer.size(); i++) {
                treeLayer.add(sha256((prevLayer.get(i - 1) + prevLayer.get(i))));
            }

            count = treeLayer.size();
            prevLayer = treeLayer;
        }

        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }

    private static String process(String in) {
        return getHex(in.getBytes());
    }
}
