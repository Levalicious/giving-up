package user;

import core.types.pools.TxPool;
import core.types.pools.UTXOPool;
import core.types.transaction.Transaction;
import core.types.transaction.TransactionInput;
import core.types.transaction.TransactionOutput;
import util.byteUtils.BIUtil;
import util.wallet.ECKey;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

import static util.Hex.fromHex;
import static util.Hex.getHex;

public class Wallet {
    ECKey keyPair;
    ArrayList<TransactionInput> utxos = new ArrayList<>();

    public Wallet() {
        keyPair = new ECKey();
    }

    public Wallet(String privateKey) {
        keyPair = ECKey.fromPrivate(fromHex(privateKey));
    }

    public String toString() {
        if(keyPair.hasPrivKey()) {
            return keyPair.toStringWithPrivate();
        }else {
            return keyPair.toString();
        }
    }

    public String getPubAddress() {
        return "0x" + getHex(keyPair.getAddress());
    }

    public byte[] getPubKey() {
        return keyPair.getPubKey();
    }

    public byte[] getPrivKey() {
        return keyPair.getPrivKeyBytes();
    }

    public String getPubKeyString() {
        return "0x" + getHex(keyPair.getPubKey());
    }

    public String getPrivKeyString() {
        return "0x" + getHex(keyPair.getPrivKeyBytes());
    }

    public String getBalance() {
        BigInteger bal = BigInteger.ZERO;
        for(Map.Entry<byte[], TransactionOutput> e : UTXOPool.UTXOPool.entrySet()) {
            if(e.getValue().checkOwner(keyPair.getPubKey())) {
                utxos.add(new TransactionInput(e.getValue()));
                bal.add(new BigInteger(e.getValue().getValue()));
            }
        }

        return bal.toString(10);
    }

    public boolean sendTx(String receiver, String value) {
        byte[] rver;
        try {
            rver = fromHex(receiver);
        }catch(Exception e) {
            System.out.println("Receiver address invalid.");
            return false;
        }

        if(BIUtil.isLessThan(new BigInteger(getBalance()), new BigInteger(value))) return false;
        Transaction tx = new Transaction(keyPair.getAddress(), rver, fromHex(Integer.toHexString(Integer.valueOf(value))), utxos, keyPair.getPrivKeyBytes());
        if(!tx.checkTx()) return false;

        TxPool.TxPool.put(fromHex(tx.grabHash()), tx);
        return true;
    }
}
