import crypto.ECDSA.ECKey;
import network.*;
import org.bouncycastle.jcajce.provider.digest.GOST3411;
import util.trie.kTrie;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static crypto.Hash.hash;
import static crypto.Hash.k256;
import static util.ByteUtil.concat;
import static util.Hex.getHex;

import static resources.Constants.*;

public class Main {
    public static void main(String[] args) throws Exception {

        ECKey key = new ECKey();

        ECKey.ECDSASignature sig = key.sign(k256("Hello".getBytes()));

        System.out.println(ECKey.verify(k256("Hello".getBytes()), sig, ECKey.signatureToKeyBytes(k256("Hello".getBytes()), sig)));

        ECKey.ECDSASignature recoveredSig = ECKey.ECDSASignature.de;

        System.out.println(ECKey.verify(k256("Hello".getBytes()), recoveredSig, ECKey.signatureToKeyBytes(k256("Hello".getBytes()), recoveredSig)));

       /*
        NodeInfo sender = new NodeInfo();

        kTrie buckets = new kTrie(20);
        for(int i = 1; i < 5000; i++) {
            buckets.add(new NodeInfo(k256(("hello" + i).getBytes()),"127.0.0." + i, 40424));
        }

        Path file = Paths.get("out.txt");
        Files.write(file,buckets.toString().getBytes());
        System.out.println("1".endsWith("1"));
        */
        /*
        for(int i = 2; i < 2002; i++) {
            node.addNode(new NodeInfo(k256(("Hello" + i).getBytes()),"127.0.0." + i,40424));
        }

        ArrayList<NodeInfo> list = new ArrayList<>();

        for(int i = 1; i <= 10; i++) {
            Thread.sleep(1);
            list.add(new NodeInfo(k256(("Hello" + i).getBytes()),"192.168.1." + i,40424));
        }

        Collections.shuffle(list);
        Collections.sort(list);
        for(NodeInfo n : list) {
            System.out.println(n.toString());
        }
        ECKey keyPair = new ECKey();
        ECKey keyPair2 = new ECKey();
        Node NoD = new Node(keyPair.getAddress(), "127.0.0.1",50000);
        System.out.println(getHex(keyPair.getAddress()));
        System.out.println(NoD.getBucket(NoD.xorDist(new NodeInfo(keyPair2.getAddress(),"127.0.0.2",  50000))));
        */

        /*
        new EchoServer().start();
        EchoClient client = new EchoClient();

        System.out.println(client.ping(sender));
        */















        /*
        System.out.println("First Hash:                   0000011010110011110111111010111011000001010010001111101100011011101100101011000001100110111100010000111011000010100001011110011111001001101111110100000000101010101100110010101010100111100010100101110100111000111000110100010101100110100000010000110011010010");

        ExecutorService threadpool = Executors.newFixedThreadPool(7);

        for(int i = 0; i < 7; i++) {
            threadpool.submit(new Runnable() {
                @Override
                public void run() {
                    find();
                }
            });
        }
        threadpool.shutdown();
        threadpool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        */
    }
}