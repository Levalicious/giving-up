package user;

import core.types.pools.TxPool;
import core.types.pools.UTXOPool;
import core.types.transaction.Transaction;
import core.types.transaction.TransactionInput;
import core.types.transaction.TransactionOutput;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static util.Sign.signString;
import static util.wallet.Private.*;
import static util.wallet.Public.*;

public class Wallet implements UTXOPool, TxPool {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private HashMap<String,TransactionOutput> walletUTXO = new HashMap<String,TransactionOutput>();

    public Wallet() {
        newWallet();
        refreshKeyPair();
    }

    private Wallet(String wif) {
        this.privateKey = stringToPrivateKey(wif);
        refreshKeyPair();
    }

    public static Wallet importWallet(String wif) {
        Wallet wallet = new Wallet(wif);
        return wallet;
    }

    @Override
    public String toString() {
        refreshKeyPair();
        String s = "+====+ Public Address +====+\n";
        s = s + publicKeyToAddress(this.publicKey) + "\n";
        s = s + "\n";
        s = s + "+====+ WIF +====+\n";
        s = s + privateKeyToWIF(this.privateKey) + "\n";
        s = s + "\n";
        return s;
    }

    public String getKeys() {
        refreshKeyPair();
        String s = "+====+ Public Key +====+\n";
        s = s + publicKeyToString(this.publicKey) + "\n";
        s = s + "\n";
        s = s + "+====+ Private Key +====+\n";
        s = s + privateKeyToString(this.privateKey) + "\n";
        s = s + "\n";
        return s;
    }

    public String getPubAddress() {
        refreshKeyPair();
        return publicKeyToAddress(this.publicKey);
    }

    public String getPubKey() {
        refreshKeyPair();
        return publicKeyToString(this.publicKey);
    }

    public String signMessage(String message) {
        return signString(privateKey,message);
    }

    public String getWIF() {
        return privateKeyToWIF(this.privateKey);
    }

    public String getPrivKey() {
        return privateKeyToString(this.privateKey);
    }

    private void refreshKeyPair() {
        this.publicKey = privateKeyToPublicKey(this.privateKey);
    }

    public void newWallet() {
        this.privateKey = generateNewPrivateKey();
    }

    public long getBalance(String publicAddress) {
        long total = 0;
        for(Map.Entry<String, TransactionOutput> item : UTXOPool.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if(UTXO.checkOwner(publicAddress)) {
                walletUTXO.put(UTXO.getHash(), UTXO);
                total += UTXO.getValue();
            }
        }

        return total;
    }

    public void sendTx(String recipient, long value) {
        if(getBalance(getPubAddress()) < value) {
            System.out.println("Not enough funds to send transaction.");
        } else {
            ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

            long total = 0;

            for(Map.Entry<String, TransactionOutput> item : walletUTXO.entrySet()) {
                TransactionOutput UTXO = item.getValue();
                total += UTXO.getValue();
                inputs.add(new TransactionInput(UTXO.getHash()));
                if(total > value) break;
            }

            Transaction newTx = new Transaction(getPubAddress(), recipient, value, inputs, this.getWIF());
            newTx.genSig(this.privateKey);

            for(TransactionInput input : inputs) {
                walletUTXO.remove(input.getParentHash());
            }

            TxPool.put(newTx.calcHash(), newTx);
        }
    }
}
