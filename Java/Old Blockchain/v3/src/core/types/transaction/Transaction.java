package core.types.transaction;

impor user.Wallet;

import user.Wallet;

import java.security.PrivateKey;
import java.util.ArrayList;

import static crypto.Hash.sha256;

public class Transaction {
    private String transactionHash;
    private String sender;
    private String senderKey;
    private String recipient;
    private long value;
    private String signature;

    private ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    private ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int nonce = 0;

    public Transaction(String sender, String recipient, long value, ArrayList<TransactionInput> inputs) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
        this.inputs = inputs;
    }

    private String calcHash() {
        nonce++;
        return sha256("{" + sender + "," + recipient + "," + Long.toString(value) + "," + nonce + "}");
    }

    private void genSig(Wallet wallet) {
        String data = "{" + sender + "," + recipient + "," + Long.toString(value) + "," + nonce + "}";
        this.signature = wallet.sign(data);
        this.senderKey = wallet.getPubKey();
    }

    private boolean checkSig() {
        String data = "{" + sender + "," + recipient + "," + Long.toString(value) + "," + nonce + "}";
        return verify(senderKey, data, signature);
    }
}
