package org.levk.udp2p.util.trees;

public interface Trie<V> extends Source<byte[], V> {

    byte[] getRootHash();

    void setRoot(byte[] root);

    /**
     * Recursively delete all nodes from root
     */
    void clear();
}