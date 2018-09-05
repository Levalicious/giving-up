package core;

import java.math.BigInteger;
import java.util.Arrays;

import static com.google.common.primitives.Bytes.concat;
import static crypto.Hash.keccak256;
import static resources.Constants.SLASH_BYTE;

public class AccountState extends Hashable {
    /* Encoded account state */
    //TODO: Create serialization method and make encoded final
    private byte[] encoded;

    /* Address that this state describes */
    private byte[] address;

    /* Account nonce. Equal to the number of
     * transactions sent from this address. */
    private final BigInteger nonce;

    /* The amount of currency owned by this account */
    private final BigInteger balance;

    /* Hash of the account state */
    private final byte[] stateHash;

    public AccountState() {
        this.address = null;
        this.nonce = BigInteger.ZERO;
        this.balance = BigInteger.ZERO;
        this.stateHash = calcHash(nonce, balance);
    }

    public AccountState(byte[] address, BigInteger nonce, BigInteger balance) {
        this.address = address;
        this.nonce = nonce;
        this.balance = balance;
        this.stateHash = calcHash(nonce, balance);
    }

    private byte[] calcHash(BigInteger nonce, BigInteger balance) {
        return keccak256(concat(nonce.toByteArray(), SLASH_BYTE, balance.toByteArray()));
    }

    @Override
    public byte[] getAddress() {
        return address;
    }

    public BigInteger getNonce() {
        return this.nonce;
    }

    public BigInteger getBalance() {
        return this.balance;
    }

    @Override
    public byte[] getHash() {
        return stateHash;
    }

    public String toString() {
        String temp = " Nonce: " + this.getNonce().toString() + "\n" +
                " Balance: " + this.getBalance().toString();
        return temp;
    }

    public boolean equals(AccountState state) {
        return Arrays.equals(this.address, state.getAddress());
    }
}
