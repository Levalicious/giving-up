package core.types.transaction;

public class TransactionInput {
    private String transactionOutputHash;
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputHash) {
        this.transactionOutputHash = transactionOutputHash;
    }

    public String getSourceTx(){
        return transactionOutputHash;
    }

    public TransactionOutput getUTXO(){
        return UTXO;
    }
}
