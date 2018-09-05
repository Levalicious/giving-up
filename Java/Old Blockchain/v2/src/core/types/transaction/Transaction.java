package core.types.transaction;

import static crypto.Hash.*;

import utils.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    public int nonce = 0;
    private String transactionHash;
    private PublicKey sender;
    private PublicKey recipient;
    private long value;
    private byte[] signature;

    private ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    private ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    public Transaction(PublicKey from, PublicKey to, long value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        return sha256(
                Integer.toString(nonce) +
                        StringUtil.toString(sender) +
                        StringUtil.toString(recipient) +
                        Long.toString(value)
        );
    }

    public void sign(PrivateKey pk) {
        String tx = StringUtil.toString(sender) + StringUtil.toString(recipient) + Long.toString(value);
    }

    public String getHash(){
        return transactionHash;
    }


}
