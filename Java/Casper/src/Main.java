import core.AccountState;
import crypto.Merkle;
import crypto.ecdsa.ECKey;
import network.Peer;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;
import org.mapdb.DB;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static crypto.Hash.keccak256;
import static crypto.Hash.multiMerkleRoot;
import static crypto.Hash.trueMultiMerkleRoot;
import static util.ByteUtil.*;
import static util.NetUtil.fromBitSet;
import static util.NetUtil.fromByteArray;

public class Main {
    public static void main(String[] args) throws Exception {
        ECKey key = ECKey.fromPrivate(Hex.decode("c5ad5c5fda2a91da3c21a07367a469c53773b502b51ff8079e99f710de6315a8"));
        ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(bigIntegerToBytes(new BigInteger("40041439503964484675439198392148096334995579287456153148485597950302521894076")), bigIntegerToBytes(new BigInteger("22399722326077709505457888339386963161328160170751031944292965275534698983760")), (byte)0x27);
        System.out.println(key.verify(keccak256("test".getBytes()), sig));
        /*
        byte[] a = "lev forgot to hash before.".getBytes("UTF-8");
        byte[] b = "told you he's a downy".getBytes("UTF-8");

        String stringA = Hex.toHexString(keccak256(a));
        String stringB = Hex.toHexString(keccak256(b));

        System.out.println("Comparing: ");
        System.out.println(stringA);
        System.out.println(stringB);

        System.out.println();

        String indices = "0123456789abcdef";

        System.out.println("The smallest value was");

        boolean foundMin = false;
        int i = 0;
        while (!foundMin) {
            if (indices.indexOf(stringA.charAt(i)) < indices.indexOf(stringB.charAt(i))) {
                System.out.println(stringA);
                foundMin = true;
            } else if (indices.indexOf(stringA.charAt(i)) > indices.indexOf(stringB.charAt(i))) {
                System.out.println(stringB);
                foundMin = true;
            }
            i++;
        }
        */

        /*
        int desiredNum = 1000;
        StateTree<AccountState> worldState = new StateTree<>();

        for(int i = 0; i < desiredNum; i++) {
            ECKey temp = new ECKey();

            AccountState tempState = new AccountState(temp.getAddress(), )

        }
        */











        /*
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
        ArrayList<byte[]> s = new ArrayList<>();

        int desiredSize = 1048576;

        long startSetup = System.currentTimeMillis();
        while (s.size() < desiredSize) {
            byte[] temp = new byte[4];

            rand.nextBytes(temp);

            s.add(temp);
        }
        long endSetup = System.currentTimeMillis();
        Merkle n = new Merkle(s);

        System.out.println("Initializing the list took " + (endSetup - startSetup) + " ms.");
        long startSingleThread = System.currentTimeMillis();

        System.out.println(Hex.toHexString(n.hash()));
        long endSingleThread = System.currentTimeMillis();

        System.out.println("Running the single threaded merkle hashing took " + (endSingleThread - startSingleThread) + " ms.");

        n = new Merkle(s);

        long startMultiThread = System.currentTimeMillis();

        System.out.println(Hex.toHexString(n.dualTest()));
        long endMultiThread = System.currentTimeMillis();

        System.out.println("Running the multithreaded merkle hashing took " + (endMultiThread - startMultiThread) + " ms.");

        06b3dfaec148fb1bb2b066f10ec285e7c9bf402ab32aa78a5d38e34566810cd2
        49bdbd7436f2ca537665a0d4b0718d1f91a7748051a13e74a0ad89a143b5f8eb
        */
    }
}
