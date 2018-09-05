package core.types.transaction;

import java.io.Serializable;

import static crypto.Hash.blake256;
import static util.wallet.Public.*;

public class TransactionOutput implements Serializable {
    private String hash;
    private String recipient;
    private long value;
    private String parentTxHash;

    public TransactionOutput(String recipient, long value, String parentTxHash) {
        this.recipient = recipient;
        this.value = value;
        this.parentTxHash = parentTxHash;
        this.hash = calcHash();
    }

    private String calcHash() {
        return blake256("{" + parentTxHash + "," + Long.toString(value) + "," + recipient + "}");
    }

    public boolean checkOwner(String pubKey) {
        return recipient.equals(publicKeyToAddress(stringToPublicKey(pubKey)));
    }

    public String getHash() {
        return hash;
    }

    public long getValue() {
        return value;
    }
}
