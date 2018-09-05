package org.levk.udp2p.util.trees;

import org.bouncycastle.util.encoders.Hex;

import static org.levk.udp2p.util.HashUtil.blake2;

public class SecureTrie extends TrieImpl {
    public SecureTrie(byte[] root) {
        super(root);
    }

    public SecureTrie(Source<byte[], byte[]> cache) {
        super(cache, null);
    }

    public SecureTrie(Source<byte[], byte[]> cache, byte[] root) {
        super(cache, root);
    }

    @Override
    public byte[] get(byte[] key) {
        return super.get(blake2(key));
    }

    @Override
    public void put(byte[] key, byte[] value) {
        super.put(blake2(key), value);
    }

    @Override
    public void delete(byte[] key) {
        put(key, EMPTY_BYTE_ARRAY);
    }
}
