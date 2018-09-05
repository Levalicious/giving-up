package crypto;

import crypto.keccak.Keccak256;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import static crypto.Hash.keccak256;
import static util.ByteUtil.concat;

public class Merkle {
    private Queue<byte[]> data;

    public Merkle() {
        this.data = new LinkedList<>();
    }

    public Merkle(ArrayList<byte[]> in) {
        this.data = new LinkedList<>();

        this.data.addAll(in);
    }

    public void add(List<byte[]> in) {
        data.addAll(in);
    }

    public void add(byte[] in) {
        data.add(in);
    }

    public byte[] hash() {
        Queue<byte[]> nextLevel = new LinkedList<>();

        while((data.size() > 1) || (nextLevel.size() > 1)) {
            while(data.size() > 0) {
                if(data.size() > 1) {
                    nextLevel.add(merkleHash(data.remove(), data.remove()));
                } else {
                    nextLevel.add(data.remove());
                }

            }

            data.addAll(nextLevel);

            nextLevel.clear();
        }

        return data.remove();
    }

    private byte[] hash(Queue<byte[]> data) {
        Queue<byte[]> nextLevel = new LinkedList<>();

        while((data.size() > 1) || (nextLevel.size() > 1)) {

            while(data.size() > 0) {
                if(data.size() > 1) {
                    nextLevel.add(merkleHash(data.remove(), data.remove()));
                } else {
                    nextLevel.add(data.remove());
                }

            }

            data.addAll(nextLevel);

            nextLevel.clear();
        }

        return data.remove();
    }

    public byte[] dualHash() throws Exception {
        Queue<byte[]> temp1 = new LinkedList<>();
        Queue<byte[]> temp2 = new LinkedList<>();

        if(data.size() == Math.pow(2, log2(data.size()))) return hash();

        int temponesize = (int)Math.pow(2, log2(data.size()) + 1) / 2;
        while(temp1.size() < temponesize) {
            temp1.add(data.remove());
        }

        while(!data.isEmpty()) {
            temp2.add(data.remove());
        }

        /*
        ExecutorService exe1 = Executors.newSingleThreadExecutor();
        ExecutorService exe2 = Executors.newSingleThreadExecutor();
        Callable<byte[]> call1 = new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return hash(temp1);
            }
        };
        Callable<byte[]> call2 = new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return hash(temp2);
            }
        };

        Future<byte[]> fut1 = exe1.submit(call1);
        Future<byte[]> fut2 = exe2.submit(call2);
        System.out.println(Hex.toHexString(fut1.get()));
        System.out.println(Hex.toHexString(fut2.get()));
        */

        byte[] tem1 = hash(temp1);
        byte[] tem2 = hash(temp2);

        return merkleHash(tem1, tem2);
    }

    public byte[] dualTest() throws Exception {
        Queue<byte[]> temp1 = new LinkedList<>();
        Queue<byte[]> temp2 = new LinkedList<>();

        if(data.size() == Math.pow(2, log2(data.size()))) return hash();

        int temponesize = (int)Math.pow(2, log2(data.size()) + 1) / 2;
        while(temp1.size() < temponesize) {
            temp1.add(data.remove());
        }

        while(!data.isEmpty()) {
            temp2.add(data.remove());
        }

        ExecutorService exe1 = Executors.newSingleThreadExecutor();
        ExecutorService exe2 = Executors.newSingleThreadExecutor();
        Callable<byte[]> call1 = new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return hash(temp1);
            }
        };
        Callable<byte[]> call2 = new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return hash(temp2);
            }
        };

        Future<byte[]> fut1 = exe1.submit(call1);
        Future<byte[]> fut2 = exe2.submit(call2);

        return merkleHash(fut1.get(), fut2.get());
    }

    public int size() {
        return data.size();
    }

    private byte[] merkleHash(byte[] a, byte[] b) {
        return keccak256(concat(a, b));
    }

    private byte[] merkleHash(byte[] a) {
        return keccak256(a);
    }

    public static byte[] mHash(byte[] a) {
        return keccak256(a);
    }

    private int log2(int x) {
        return (int)Math.floor((Math.log(x))/(Math.log(2)));
    }
}
