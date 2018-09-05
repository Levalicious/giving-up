package org.levk.udp2p.util.trees;

import java.util.*;

import static org.levk.udp2p.util.HashUtil.blake2;

public class TrieTest {
    public static void main(String[] args) {
        SecureTrie trie = new SecureTrie((byte[]) null);



        /*
        var keys = 2500;
        BiTrie tree = new BiTrie();

        for (var i = 0; i < keys; i++) {
            tree.update(blake2(Integer.toString(i).getBytes()), blake2(Integer.toString((int)Math.pow(i, 3)).getBytes()));
        }

        System.out.println(keys + " elements added");

        for (var i = 0; i < keys; i++) {
            assert Arrays.equals(tree.get(blake2(Integer.toString(i).getBytes())), blake2(Integer.toString((int)Math.pow(i, 3)).getBytes()));
        }

        System.out.println("Get requests for present elements successful");

        for (var i = keys + 1; i < keys * 2; i++) {
            assert Arrays.equals(tree.get(blake2(Integer.toString(i).getBytes())), new byte[32]);
        }

        System.out.println("Get requests for absent elements successful");

        var TL = 0;

        for (var i = 0; i < keys * 2; i++) {
            try {
                var key = blake2(Integer.toString(i).getBytes());
                var value = new byte[32];

                if (i < keys) {
                    value = blake2(Integer.toString((int)Math.pow(i, 3)).getBytes());
                }

                var proof = tree.make_merkle_proof(key);
                assert tree.verify_proof(proof, key, value);
                List<byte[]> decomped = tree.decompress_proof(tree.compress_proof(proof));

                for (var j = 0; j < decomped.size(); j++) {
                    assert Arrays.equals(decomped.get(j), proof.get(j));
                }

                TL += tree.compress_proof(proof).length;
            } catch (AssertionError e) {
                e.printStackTrace();
                System.exit(150);
            }

        }

        System.out.println("Average total length at " + keys + " keys: " + (Math.floorDiv(Math.floorDiv(TL, keys), 2)) + ", " + (Math.floorDiv(Math.floorDiv(TL, keys), 2) + 32) + " including key");
        */
    }
}
