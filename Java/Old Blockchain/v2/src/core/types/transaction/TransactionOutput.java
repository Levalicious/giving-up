package core.types.transaction;

import utils.StringUtil;

import static crypto.Hash.*;
import java.security.PublicKey;

public class TransactionOutput {
    private String hash;
    private PublicKey recipient;
    private long value;
    private String parentTransactionHash;

    public TransactionOutput(PublicKey recipient, long value, String parentTransactionHash) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionHash = parentTransactionHash;
        this.hash = calcHash();
    }

    public String getHash(){
        return hash;
    }

    public String calcHash() {
        return sha256(StringUtil.toString(recipient) + Long.toString(value) + parentTransactionHash);
    }

    public String getOwner(){
        return StringUtil.toString(recipient);
    }

    public long getValue(){
        return this.value;
    }

    public String getParent() {
        return this.parentTransactionHash;
    }

    public boolean isMine(PublicKey publicKey) {
        return (publicKey == recipient);
    }
}
