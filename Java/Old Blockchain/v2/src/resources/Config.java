package resources;

import crypto.Keccak;

import java.math.BigInteger;

public interface Config {
    Keccak keccak = new Keccak();
    BigInteger MIN_GAS_PRICE = new BigInteger("1");
    BigInteger DEFAULT_GAS_COST = new BigInteger("21000");
    String NETWORK_ID = "0x00";
    String PUBKEY_PREFIX = "0x04";
    String WIF_PREFIX = "0x80";
}
