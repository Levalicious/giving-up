package main;

import java.math.BigInteger;

public class WorldState {
    private byte[] nonce = {(byte)0x00};
    private byte[] balance;
    private byte[] storageRoot;
    private byte[] codeHash;

    public void iterateNonce() {
        nonce = (new BigInteger(nonce).add(BigInteger.ONE)).toByteArray();
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setBalance(byte[] newBalance) {
        this.balance = newBalance;
    }

    public byte[] getBalance() {
        return balance;
    }

    public void setStorageRoot(byte[] root) {
        this.storageRoot = root;
    }

    public byte[] getStorageRoot() {
        return storageRoot;
    }

    public void setCodeHash(byte[] codeHash) {
        this.codeHash = codeHash;
    }

    public byte[] getCodeHash() {
        return codeHash;
    }
}
