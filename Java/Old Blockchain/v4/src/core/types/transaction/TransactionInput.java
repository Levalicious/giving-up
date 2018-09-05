package core.types.transaction;

import java.io.Serializable;

public class TransactionInput implements Serializable {
    private String txOutputHash;
    private TransactionOutput UTXO;

    public TransactionInput(String txOutputHash) {
        this.txOutputHash = txOutputHash;
    }

    public String getParentHash() {
        return txOutputHash;
    }

    public void setUTXO(TransactionOutput in) {
        this.UTXO = in;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }
}

