package core.types;

import java.util.ArrayList;

public class BlockHeader {
    public static final int MAX_HEADER_SIZE = 592;

    /* SHA3 256 bit hashes of all blocks from parent layer */
    private ArrayList<byte[]> parentHashes;

    /**/
    private byte[] coinbase;
}
