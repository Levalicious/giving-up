package core.types.transaction;

import util.byteUtils.ByteUtil;

import java.io.Serializable;

import static util.byteUtils.ByteUtil.concat;

public class TransactionInput implements Serializable {
    private byte[] txOutputHash;
    private TransactionOutput UTXO;

    public TransactionInput(byte[] txOutputHash) {
        this.txOutputHash = txOutputHash;
    }

    public TransactionInput(TransactionOutput txo) {
        this.txOutputHash = txo.getHash();
        this.UTXO = txo;
    }

    public byte[] getParentHash() {
        return txOutputHash;
    }

    public void setUTXO(TransactionOutput in) {
        this.UTXO = in;
    }

    public TransactionOutput getUXTO() {
        return UTXO;
    }

    public byte[] toBytes() {
        return concat(txOutputHash, UTXO.toBytes());
    }
}
