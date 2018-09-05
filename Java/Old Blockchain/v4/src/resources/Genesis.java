package resources;

import core.types.block.Block;

public interface Genesis {
    /* SHA-256 Hash of "Sam, Kennedy, Lev, Carol, and Sami" without quotes */
    String GEN_HASH = "e8fd6189a584ff59fc531c5689b508295d28f6a7e14ab38d2eee1e7d8c1e7720";

    /* Declaring genesis block */
    //(long height, String prefix, String WIF, Payload txSet, PrevBlock parents)
    //Block GENESIS = new Block(0, "0", );
}
