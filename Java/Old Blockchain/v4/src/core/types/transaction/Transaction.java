package core.types.transaction;

import core.types.pools.UTXOPool;
import user.Wallet;
import util.wallet.Public;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;

import static crypto.Hash.blake256;
import static util.Sign.signString;
import static util.Sign.verifySig;
import static util.wallet.Private.stringToPrivateKey;

public class Transaction implements TxBase, UTXOPool, Comparable<Transaction>, Serializable {
    private String sender;
    private String recipient;
    private long value;
    private String signature;
    private String senderKey;
    private String transactionHash;

    private ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    private ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    public Transaction(String sender, String recipient, long value, ArrayList<TransactionInput> inputs, String WIF) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
        this.inputs = inputs;
        genSig(stringToPrivateKey(WIF));
        this.transactionHash = calcHash();
    }

    private Transaction() {
        this.sender = "z";
        this.value = 100000000;
        this.recipient = "1348rnwexLA8ifeEaD78uhWCHVeV4FWXWTtayFD1wTMHKxGgfmYSM3rXLHUx4PRLkVBRbjC1pPo3yujzNNkzzK1q5";
        this.transactionHash = calcHash();
    }

    public static Transaction genesis() {
        return new Transaction();
    }

    public String calcHash() {
        return blake256("{" + sender + "," + recipient + "," + Long.toString(value) + "," + this.signature + "," + this.senderKey + "}");
    }

    public void genSig(Wallet wallet) {
        String data = "{" + sender + "," + recipient + "," + Long.toString(value) + "}";
        this.signature = signString(wallet.getWIF(),data);
        this.senderKey = wallet.getPubAddress();
    }

    public void genSig(PrivateKey privateKey) {
        String data = "{" + sender + "," + recipient + "," + Long.toString(value) + "}";
        this.signature = util.Sign.signString(privateKey, data);
        this.senderKey = util.wallet.Public.publicKeyToString(util.wallet.Public.privateKeyToPublicKey(privateKey));
    }

    private boolean checkSig() {
        String data = "{" + sender + "," + recipient + "," + Long.toString(value) + "}";
        return verifySig(senderKey, data, signature);
    }

    public boolean checkTx() {
        if(calcHash().equals("3428b4f575360d2145078c330e760b2b53cb9bb428be11f5c85e4afee1b9c5fb")) {
            return true;
        }

        if(checkSig()) {
            if(transactionHash.equals(calcHash())) {
                return this.sender.equals(Public.publicKeyToAddress(Public.stringToPublicKey(senderKey)));
            }
        }
        return false;
    }

    public boolean processTx() {

        if(!checkTx()) {
            return false;
        }

        if(calcHash().equals("3428b4f575360d2145078c330e760b2b53cb9bb428be11f5c85e4afee1b9c5fb")) {
            outputs.add(new TransactionOutput(this.recipient, value, transactionHash));

            for(TransactionOutput o : outputs) {
                UTXOPool.put(o.getHash(), o);
            }
        }

        for(TransactionInput i : inputs) {
            i.setUTXO(UTXOPool.get(i.getParentHash()));
        }

        if(getInputs() < 1) {
            System.out.println("Transaction inputs too small.");
            return false;
        }

        long leftOver = getInputs() - value;
        transactionHash = calcHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionHash));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionHash));

        for(TransactionOutput o : outputs) {
            UTXOPool.put(o.getHash(), o);
        }

        for(TransactionInput i : inputs) {
            if(i.getUTXO() == null) continue;
            UTXOPool.remove(i.getUTXO().getHash());
        }

        return true;
    }

    public long getInputs() {
        long total = 0;

        for(TransactionInput i : inputs) {
            if(i.getUTXO() == null) continue;
            total += i.getUTXO().getValue();
        }

        return total;
    }

    public long getOutputs() {
        long total = 0;

        for(TransactionOutput o : outputs) {
            total += o.getValue();
        }

        return total;
    }

    @Override
    public String toString() {
        return "{" + sender + "," + recipient + "," + Long.toString(value) + "," + this.signature + "," + this.senderKey + "," + this.transactionHash + "}";
    }

    @Override
    public String getSender() {
        return sender;
    }

    public int compareTo(Transaction tx) {
        return this.sender.compareTo(tx.getSender());
    }
}
