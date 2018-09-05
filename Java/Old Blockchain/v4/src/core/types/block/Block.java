package core.types.block;

import static core.types.block.BlockHeader.genesisHeader;

import static core.types.block.Payload.genesisTxSet;
import static core.types.block.PrevBlock.genesisParents;

public class Block implements Comparable<Block> {
    private BlockHeader header;
    private Payload txSet;
    private PrevBlock parents;

    public Block(BlockHeader header, Payload txSet, PrevBlock parents) {
        this.header = header;
        this.txSet = txSet;
        this.parents = parents;
    }

    public Block(long height, String prefix, String WIF, Payload txSet, PrevBlock parents) {
        this.header = new BlockHeader(height, prefix, txSet.getRoot(), parents.getRoot(), WIF);
        this.txSet = txSet;
        this.parents = parents;
    }

    private Block() {
        this.header = genesisHeader();
        this.txSet = genesisTxSet();
        this.parents = genesisParents();
    }

    public boolean validate() {
        if(!this.txSet.getRoot().equals(this.header.getTxRoot())) return false;
        if(!this.parents.getRoot().equals(this.header.getHashRoot())) return false;
        if(!this.txSet.validate()) return false;
        return true;
    }

    public static Block genesis() {
        return new Block();
    }

    public BlockHeader getHeader() {
        return header;
    }

    public Payload getTxs() {
        return txSet;
    }

    public PrevBlock getParents() {
        return parents;
    }

    public void process() {
        this.txSet.process();
    }

    @Override
    public int compareTo(Block block) {
        return block.getHeader().getPrefix().compareTo(this.getHeader().getPrefix());
    }


}
