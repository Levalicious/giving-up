package util.MPT;

public interface MPT<V> {
    byte[] getRootHash();

    void setRoot(byte[] root);

    void clear();
}
