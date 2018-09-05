package core.types.block;

import core.types.transaction.Transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static crypto.Hash.getTxMerkleRoot;

public class Payload {
    private ArrayList<Transaction> txSet;

    public Payload(ArrayList<Transaction> txSet) {
        this.txSet = new ArrayList<Transaction>(txSet);
    }

    private Payload() {
        txSet = new ArrayList<Transaction>();
        txSet.add(Transaction.genesis());
    }

    public String getRoot() {
        return getTxMerkleRoot(this.txSet);
    }

    public void add(Transaction tx) {
        if(!txSet.contains(tx)) {
            txSet.add(tx);
        }else {
            System.out.println("Transaction already in payload.");
        }
    }

    public static Payload genesisTxSet() {
        return new Payload();
    }

    public boolean validate() {
        for(long i = 0; i < txSet.size(); i++) {
            if(!txSet.get((int)i).checkTx()) return false;
        }

        return true;
    }

    public void process() {
        for(long i = 0; i < txSet.size(); i++) {
            txSet.get((int)i).processTx();
        }
    }
}
