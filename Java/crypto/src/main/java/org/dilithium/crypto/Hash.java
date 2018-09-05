package org.dilithium.crypto;

import com.google.common.collect.Lists;
import org.bouncycastle.jcajce.provider.digest.Blake2b;
import org.bouncycastle.jcajce.provider.digest.Skein;
import org.dilithium.crypto.groestl.Groestl256;
import org.dilithium.crypto.groestl.Groestl512;
import org.dilithium.crypto.keccak.Keccak256;
import org.dilithium.crypto.keccak.Keccak512;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.dilithium.util.ByteUtil.concat;
import static org.dilithium.util.Hex.getHex;

public class Hash {

    public static byte[] keccak256(byte[] in) {
        MessageDigest md = new Keccak256();
        return md.digest(in);
    }

    public static byte[] keccak512(byte[] in) {
        MessageDigest md = new Keccak512();
        return md.digest(in);
    }

    public static byte[] skein256(byte[] in) {
        MessageDigest md = new Skein.Digest_256_256();
        return md.digest(in);
    }

    public static byte[] skein512(byte[] in) {
        MessageDigest md = new Skein.Digest_512_512();
        return md.digest(in);
    }

    public static byte[] blake256(byte[] in) {
        MessageDigest md = new Blake2b.Blake2b256();
        return md.digest(in);
    }

    public static byte[] blake512(byte[] in) {
        MessageDigest md = new Blake2b.Blake2b512();
        return md.digest(in);
    }

    public static byte[] groestl256(byte[] in) {
        return new Groestl256().digest(in);
    }

    public static byte[] groestl512(byte[] in) {
        return new Groestl512().digest(in);
    }

    public static byte[] longHash(byte[] in) {
        return concat(keccak256(in), skein256(in), blake256(in), groestl256(in));
    }

    private static byte[] merkleHash(byte[] in) {
        return blake256(in);
    }

    public static byte[] getMerkleRoot(ArrayList<byte[]> temp) {
        int count = temp.size();
        ArrayList<byte[]> prevLayer = new ArrayList<>();

        for(byte[] x : temp) {
            prevLayer.add(blake256(x));
        }

        ArrayList<byte[]> treeLayer = prevLayer;

        while(count > 1) {
            treeLayer = new ArrayList<byte[]>();

            for(int i = 1; i < prevLayer.size(); i++) {
                treeLayer.add(blake256(concat(prevLayer.get(i - 1), prevLayer.get(i))));
            }

            count = treeLayer.size();
            prevLayer = treeLayer;
        }

        return (treeLayer.size() == 1) ? treeLayer.get(0) : null;
    }

    public static byte[] multiMerkleRoot(ArrayList<byte[]> temp) {
        int count = temp.size();
        List<byte[]> hashList = new ArrayList<>();

        for(byte[] o : temp) {
            hashList.add(merkleHash(o));
        }

        if (count % 2 == 0) {
            return getRoot(hashList);
        } else {
            return merkleHash(concat(getRoot(hashList.subList(0, hashList.size() - 1)), hashList.get(hashList.size() - 1)));
        }
    }

    private static byte[] getRoot(List<byte[]> temp) {
        if(temp.size() % 2 != 0) {
            return merkleHash(concat(getRoot(temp.subList(0, temp.size() - 1)), temp.get(temp.size() - 1)));
        } else {
            if (temp.size() > 2) {
                List<List<byte[]>> subsets = Lists.partition(temp, temp.size() / 2);

                return merkleHash(concat(getRoot(subsets.get(0)), getRoot(subsets.get(1))));
            } else {
                return merkleHash(concat(temp.get(0), temp.get(1)));
            }
        }
    }

    public static byte[] trueMultiMerkleRoot(ArrayList<byte[]> temp, int threads) {
        try {
            int count = temp.size();
            List<byte[]> hashList = new ArrayList<>();

            for(byte[] o : temp) {
                hashList.add(merkleHash(o));
            }

            if(count % 2 == 0) {
                byte[] chunk1 = null;

                switch(threads) {
                    case 1: chunk1 = getRoot(hashList);
                            break;
                    case 2: chunk1 = twoThreadMerkle(hashList);
                            break;
                    default: System.out.println("You can only have the following threadcounts: 1, 2, 4, 8.");
                            break;
                }

                return chunk1;
            } else {
                byte[] chunk1 = null;
                byte[] chunk2 = hashList.get(hashList.size() - 1);

                switch(threads) {
                    case 1: chunk1 = getRoot(hashList.subList(0, hashList.size() - 1));
                        break;
                    case 2: chunk1 = twoThreadMerkle(hashList.subList(0, hashList.size() - 1));
                        break;
                    default: System.out.println("You can only have the following threadcounts: 1, 2, 4, 8.");
                        break;
                }

                return chunk1;
            }
        } catch(Exception e) {
            return null;
        }
    }

    private static byte[] twoThreadMerkle(List<byte[]> temp) throws Exception {
        if (!(temp.size() >= 2)) {
            return twoThreadMerkle(temp);
        } else {
            if(temp.size() % 2 != 0) {
                return getRoot(temp);
            } else {
                List<List<byte[]>> subsets = Lists.partition(temp, temp.size() / 2);

                Executor exe1 = Executors.newSingleThreadExecutor();
                Executor exe2 = Executors.newSingleThreadExecutor();

                Future<byte[]> fut1 = ((ExecutorService) exe1).submit(() -> getRoot(subsets.get(0)));
                Future<byte[]> fut2 = ((ExecutorService) exe2).submit(() -> getRoot(subsets.get(1)));

                while ((!fut1.isDone()) || (!fut2.isDone())) {
                    Thread.sleep(500);
                }

                return merkleHash(concat(fut1.get(), fut2.get()));
            }
        }
    }

    private static String toHex(String in) {
        try {
            return getHex(in.getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e) {
            System.out.println("Your computer does not support UTF-8. Exiting.");
            System.exit(0);
        }
        return null;
    }
}
