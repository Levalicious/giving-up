package core.types.block;

import java.util.Date;

import static crypto.Hash.blake256;
import static util.wallet.Private.*;
import static util.wallet.Public.*;
import static util.Sign.*;

public class BlockHeader {
    private long height;
    private String prefix;
    private String hashRoot;
    private String txRoot;
    private String timestamp;
    private String sig;
    private String pubKey;
    private String blockHash;

    protected BlockHeader(long height, String prefix, String txRoot, String hashRoot, String key) {
        this.height = height;
        this.prefix = prefix;
        this.txRoot = txRoot;
        this.hashRoot = hashRoot;
        this.timestamp = new Date().toString();
        this.sig = signBlock(key);
        this.pubKey = publicKeyToString(privateKeyToPublicKey(stringToPrivateKey(key)));
        this.blockHash = calculateHash();
    }

    private BlockHeader() {
        this.height = 0;
        this.prefix = "Z";
        this.txRoot = "3428b4f575360d2145078c330e760b2b53cb9bb428be11f5c85e4afee1b9c5fb";
        this.hashRoot = "b6ba0af19e59cea958ccb2f558108015bb162f6cd07f69b631859a6693bc0d88";
        this.timestamp = "Tue Mar 20 03:09:31 EDT 2018";
        this.sig = "0";
        this.pubKey = "0";
        this.blockHash = calculateHash();
    }

    protected static BlockHeader genesisHeader() {
        return new BlockHeader();
    }

    private String signBlock(String privKey) {
        String sig = signString(privKey, "{" +
                Long.toString(height) + "," +
                prefix + "," +
                hashRoot + "," +
                txRoot + "," +
                timestamp + "}");

        return sig;
    }

    public boolean checkSig() {
        String data = ("{" +
                Long.toString(height) + "," +
                prefix + "," +
                hashRoot + "," +
                txRoot + "," +
                timestamp + "}");
        return verifySig(this.pubKey, data, this.sig);
    }

    private String calculateHash() {
        String hash = blake256("{" +
                Long.toString(height) + "," +
                prefix + "," +
                hashRoot + "," +
                txRoot + "," +
                timestamp + "," +
                sig + "," +
                pubKey + "}");

        return hash;
    }

    public long getHeight() {
        return height;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getHashRoot() {
        return hashRoot;
    }

    public String getTxRoot() {
        return txRoot;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String getSig() {
        return sig;
    }

    public String getPubKey() {
        return pubKey;
    }

    @Override
    public String toString() {
        String temp = ("{{" +
                Long.toString(height) + "," +
                prefix + "," +
                hashRoot + "," +
                txRoot + "," +
                timestamp + "}," + blockHash + "}");
        return temp;
    }
}
