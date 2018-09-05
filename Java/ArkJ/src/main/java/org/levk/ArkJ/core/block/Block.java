package org.levk.ArkJ.core.block;

public class Block {
    /* The hash of the previous block */
    private byte[] prevBlockHash;

    /* The merkle root of the block's transactions */
    private byte[] txMerkleRoot;

    /* The state of all accounts on the network */
    private byte[] worldState;

    /* The signature of the block's validator */
    private byte[] signature;

    /* The pubkey of the block's validator */
    private byte[] pubkey;



}
