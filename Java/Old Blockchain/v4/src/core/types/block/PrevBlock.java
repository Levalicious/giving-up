package core.types.block;

import java.util.ArrayList;

import static crypto.Hash.getStringMerkleRoot;

public class PrevBlock {
    private ArrayList<String> hashSet;

    public PrevBlock(ArrayList<String> hashSet) {
        this.hashSet = new ArrayList<String>(hashSet);
    }

    public PrevBlock(ArrayList<Block> blockSet, boolean t) {
        this.hashSet = new ArrayList<String>();
        for(Block b : blockSet) {
            this.hashSet.add(b.getHeader().getBlockHash());
        }
    }

    private PrevBlock() {
        hashSet = new ArrayList<String>();
        hashSet.add("e8fd6189a584ff59fc531c5689b508295d28f6a7e14ab38d2eee1e7d8c1e7720");
    }

    public String getRoot() {
        return getStringMerkleRoot(hashSet);
    }

    public void add(String hash) {
        if(!hashSet.contains(hash)) {
            hashSet.add(hash);
        }else {
            System.out.println("Hash already in hashlist.");
        }
    }

    public static PrevBlock genesisParents() {
        return new PrevBlock();
    }
}
