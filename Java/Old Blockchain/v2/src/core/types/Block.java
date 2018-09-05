package core.types;

import static crypto.Parameters.KECCAK_256;
import static crypto.Parameters.KECCAK_512;
import static utils.HexUtils.getHex;
import core.types.transaction.*;

import crypto.Keccak;

import java.util.ArrayList;

public class Block {
    private int height;
    private int depth;
    private String prefix;
    private String hash;
    private String[] previousHashes;
    private ArrayList<Transaction> transactions;
    private String time;


}
