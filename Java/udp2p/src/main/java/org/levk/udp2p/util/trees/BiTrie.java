package org.levk.udp2p.util.trees;

import org.levk.udp2p.serialization.ENCItem;
import org.levk.udp2p.serialization.ENCList;
import org.levk.udp2p.serialization.TRENC;
import org.levk.udp2p.util.inmem.HashMapDB;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static org.levk.udp2p.util.ByteUtils.merge;
import static org.levk.udp2p.util.HashUtil.blake2;

public class BiTrie {
    private HashMapDB map;
    private Stack<byte[]> zerohashes;

    long totalPut = 0;
    long sampleSize = 0;

    long totalGet = 0;
    long sampleDif = 0;

    private byte[] t;

    public BiTrie() {
        map = new HashMapDB();
        zerohashes = new Stack<>();
        zerohashes.push(new byte[32]);

        for (var i = 0; i < 255; i++) {
            zerohashes.insertElementAt(blake2(merge(zerohashes.get(0), zerohashes.get(0))), 0);
        }

        var h = new byte[32];

        for (var i = 0; i < 256; i++) {
            var newh = blake2(merge(h, h));
            map.put(newh, merge(h, h));
            h = newh;
        }

        this.t = new_tree();
    }

    public BiTrie(byte[] data) {
        map = new HashMapDB();
        zerohashes = new Stack<>();
        zerohashes.push(new byte[32]);

        for (var i = 0; i < 255; i++) {
            zerohashes.insertElementAt(blake2(merge(zerohashes.get(0), zerohashes.get(0))), 0);
        }

        var h = new byte[32];

        for (var i = 0; i < 256; i++) {
            var newh = blake2(merge(h, h));
            map.put(newh, merge(h, h));
            h = newh;
        }

        this.t = import_tree(data);
    }

    public byte[] import_tree(byte[] serializedTree) {
        byte[] newt = Arrays.copyOfRange(serializedTree, 0, 32);
        serializedTree = Arrays.copyOfRange(serializedTree, 32, serializedTree.length);
        ENCList itemList = TRENC.decode(serializedTree);

        for (ENCItem i : itemList) {
            ENCList temp = TRENC.decode(i.getEncData());
            map.put(temp.get(0).getEncData(), temp.get(1).getEncData());
        }

        return newt;
    }

    public byte[] getT() {
        return t;
    }

    /* Good */
    public byte[] new_tree() {
        var h = new byte[32];

        for (var i = 0; i < 256; i++) {
            var newh = blake2(merge(h, h));
            map.put(newh, merge(h, h));
            h = newh;
        }

        return h;
    }

    /* Good */
    public BigInteger key_to_path(byte[] key) {
        var o = BigInteger.ZERO;

        for (byte c : key) {
            o = (o.shiftLeft(8)).add(BigInteger.valueOf((c & 0xFF)));
        }

        return o;
    }

    /* Unchecked, but also unused ? */
    public byte[] descend(BigInteger path) {
        var v = t;
        for (int i = 0; i < path.bitLength(); i++) {
            var currentData = map.get(v);

            if (path.testBit(i)) {
                v = Arrays.copyOfRange(currentData, 32, currentData.length);
            } else {
                v = Arrays.copyOfRange(currentData, 0, 32);
            }
        }

        return v;
    }

    /* Checked ? */
    public byte[] get(byte[] key) {
        var v = t;

        var path = key_to_path(key);

        for (var i = 0; i < 256; i++) {
            var currentData = map.get(v);

            if (path.shiftRight(255).and(BigInteger.ONE).intValue() == 1) {
                v = Arrays.copyOfRange(currentData, 32, currentData.length);
            } else {
                v = Arrays.copyOfRange(currentData, 0, 32);
            }

            path = path.shiftLeft(1);
        }

        return v;
    }

    public byte[] grabDb(byte[] key) {
        return map.get(key);
    }

    public byte[] getItem(byte[] key) {
        try {
            return Snappy.uncompress(map.get(get(key)));
        } catch (Exception e) {
            return map.get(get(key));
        }
    }

    public boolean putItem(byte[] key, byte[] data) {
        try {
            data = Snappy.compress(data);
            byte[] dat = blake2(data);
            map.put(dat, data);
            update(key, dat);
            return true;
        } catch (IOException i) {
            return false;
        }
    }

    /* Checked */
    public byte[] update(byte[] key, byte[] value) {
        var v = t;
        var path = key_to_path(key);
        var path2 = new BigInteger(path.toString());

        var sidenodes = new Stack<byte[]>();

        for (var i = 0; i < 256; i++) {
            var currentData = map.get(v);

            if ((path.shiftRight(255).and(BigInteger.ONE).intValue() == 1)) {
                sidenodes.push(Arrays.copyOfRange(currentData, 0,32));
                v = Arrays.copyOfRange(currentData, 32, currentData.length);
            } else {
                sidenodes.push(Arrays.copyOfRange(currentData, 32, currentData.length));
                v = Arrays.copyOfRange(currentData, 0, 32);
            }

            path = path.shiftLeft(1);
        }

        v = value;

        for (var i = 0; i < 256; i++) {
            var newv = new byte[32];
            if (path2.and(BigInteger.ONE).intValue() == 1) {
                newv = blake2(merge(sidenodes.peek(), v));
                map.put(newv, merge(sidenodes.peek(), v));
            } else {
                newv = blake2(merge(v, sidenodes.peek()));
                map.put(newv, merge(v, sidenodes.peek()));
            }

            path2 = path2.shiftRight(1);

            v = newv;
            sidenodes.pop();
        }

        this.t = v;
        return v;
    }

    public Stack<byte[]> make_merkle_proof(byte[] key) {
        var v = t;
        var path = key_to_path(key);

        var sidenodes = new Stack<byte[]>();

        for (var i = 0; i < 256; i++) {
            var currentData = map.get(v);

            if (path.shiftRight(255).and(BigInteger.ONE).intValue() == 1) {
                sidenodes.push(Arrays.copyOfRange(currentData, 0, 32));
                v = Arrays.copyOfRange(currentData, 32, currentData.length);
            } else {
                sidenodes.push(Arrays.copyOfRange(currentData, 32, currentData.length));
                v = Arrays.copyOfRange(currentData, 0, 32);
            }

            path = path.shiftLeft(1);
        }

        return sidenodes;
    }

    public boolean verify_item_proof(Stack<byte[]> proof, byte[] key, byte[] item) {
        try {
            byte[] data = Snappy.compress(item);
            byte[] dat = blake2(data);

            return verify_proof(proof, key, dat);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verify_proof(Stack<byte[]> proof, byte[] key, byte[] value) {
        var path = key_to_path(key);
        var v = value;

        for (var i = 0; i < 256; i++) {
            var newv = new byte[32];

            if (path.and(BigInteger.ONE).intValue() == 1) {
                newv = blake2(merge(proof.get(proof.size() - i - 1), v));
            } else {
                newv = blake2(merge(v, proof.get(proof.size() - i - 1)));
            }

            path = path.shiftRight(1);
            v = newv;
        }

        return Arrays.equals(t, v);
    }

    public byte[] compress_proof(Stack<byte[]> proof) {
        var bits = new byte[32];
        var oproof = new byte[0];

        for (var i = 0; i < proof.size(); i++) {
            var p = proof.get(i);
            if (Arrays.equals(p, zerohashes.get(i))) {
                bits[Math.floorDiv(i, 8)] ^= 1 << i % 8;
            } else {
                oproof = merge(oproof, p);
            }
        }

        return merge(bits, oproof);
    }

    public Stack<byte[]> decompress_proof(byte[] oproof) {
        var proof = new Stack<byte[]>();
        var bits = Arrays.copyOfRange(oproof, 0, 32);

        var pos = 32;
        for (var i = 0; i < 256; i++) {
            if ((((bits[Math.floorDiv(i, 8)] & 0xFF) & (1 << (i % 8)))) != 0) {
                proof.push(zerohashes.get(i));
            } else {
                proof.push(Arrays.copyOfRange(oproof, pos, pos + 32));
                pos += 32;
            }
        }

        return proof;
    }

    public byte[] serialize() {
        Set<Map.Entry<byte[], byte[]>> temp = map.getStorage().entrySet();

        int i = 0;
        byte[][] tempList = new byte[temp.size()][];
        for (Map.Entry<byte[], byte[]> item : temp) {
            tempList[i] = TRENC.encode(item.getKey(), item.getValue());
            i++;
        }

        return merge(t, TRENC.encode(tempList));
    }
}
