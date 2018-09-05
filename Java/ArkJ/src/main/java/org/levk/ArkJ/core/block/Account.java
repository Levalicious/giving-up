package org.levk.ArkJ.core.block;

import org.levk.ArkJ.serialization.ENCItem;
import org.levk.ArkJ.serialization.ENCList;
import org.levk.ArkJ.serialization.TRENC;

import java.math.BigInteger;
import java.util.Arrays;

import static org.levk.ArkJ.util.ByteUtils.bigIntegerToBytes;
import static org.levk.ArkJ.util.ByteUtils.bytesToBigInteger;
import static org.levk.ArkJ.util.HashUtil.blake2;
import static org.levk.ArkJ.util.HashUtil.blake2omit12;

public class Account {
    /* The TRENC encoding of this account state */
    private byte[] encoded;

    /* Whether this account state has been deserialized yet */
    private boolean parsed;

    /* The address of this account */
    private byte[] address;

    /* The pubkey of this account */
    private byte[] pubkey;

    /* The nonce associated with this account */
    private BigInteger nonce;

    /* The balance associated with this account */
    private BigInteger balance;

    /* Whether this account is registered as a delegate */
    private boolean isDelegate;

    /* The number of votes this account has. If
     * it's a delegate account, is used to rank
     * account in the delegate list, otherwise
     * is used for a form of liquid democracy. */
    private BigInteger votes;

    /* The number of accounts voting for this account */
    private BigInteger voters;

    /* Who this account is voting for (can be
     * a delegate, or someone representing your
     * voice on the network) */
    private byte[] votingFor;

    /* The hash of the current account state */
    private byte[] hash;

    public Account(byte[] encoded) {
        this.encoded = encoded;
        this.parsed = false;
    }

    public Account(byte[] pubkey, BigInteger balance) {
        this(blake2omit12(pubkey), pubkey, BigInteger.ZERO, balance, false, BigInteger.ZERO, BigInteger.ZERO, new byte[20]);
    }

    public Account(byte[] pubkey, BigInteger nonce, BigInteger balance, boolean isDelegate, BigInteger votes, BigInteger voters, byte[] votingFor) {
        this(blake2omit12(pubkey), pubkey, nonce, balance, isDelegate, votes, voters, votingFor);
    }
    public Account(byte[] address, byte[] pubkey, BigInteger nonce, BigInteger balance, boolean isDelegate, BigInteger votes, BigInteger voters, byte[] votingFor) {
        if (!Arrays.equals(address, blake2omit12(pubkey))) throw new RuntimeException("Attempted to generate an invalid address/pubkey pair.");

        this.address = address;
        this.pubkey = pubkey;
        this.nonce = nonce;
        this.balance = balance;
        this.isDelegate = isDelegate;
        this.votes = votes;
        this.voters = voters;
        this.votingFor = votingFor;

        this.parsed = true;
    }

    public byte[] getAddress() {
        parse();
        return address;
    }

    public byte[] getPubkey() {
        parse();
        return pubkey;
    }

    public BigInteger getNonce() {
        parse();
        return nonce;
    }

    public BigInteger getBalance() {
        parse();
        return balance;
    }

    public boolean isDelegate() {
        parse();
        return isDelegate;
    }

    public BigInteger getVotes() {
        parse();
        return votes;
    }

    public BigInteger getVoterCount() {
        parse();
        return voters;
    }

    public boolean isVoting() {
        parse();

        return !Arrays.equals(new byte[20], votingFor);
    }

    public byte[] votingFor() {
        parse();
        return votingFor;
    }

    public synchronized void parse() {
        if (parsed) return;

        try {
            ENCList decAccount = TRENC.decode(encoded);

            if (decAccount.size() > 8) throw new RuntimeException("Too many encoded elements.");
            for (ENCItem e : decAccount) {
                if (e.isList()) throw new RuntimeException("Account elements should not be lists.");
            }

            this.address = decAccount.get(0).getEncData();
            this.pubkey = decAccount.get(1).getEncData();
            this.nonce = bytesToBigInteger(decAccount.get(2).getEncData());
            this.balance = bytesToBigInteger(decAccount.get(3).getEncData());
            this.isDelegate = (decAccount.get(4).getEncData()[0] == 1);
            this.votes = bytesToBigInteger(decAccount.get(5).getEncData());
            this.voters = bytesToBigInteger(decAccount.get(6).getEncData());
            this.votingFor = decAccount.get(7).getEncData();

            this.parsed = true;
            this.hash = getHash();
        } catch (Exception e) {
            throw new RuntimeException("Error on parsing encoding", e);
        }
    }

    public byte[] getHash() {
        if (hash != null) return hash;

        parse();
        byte[] plainMsg = this.getEncoded();
        return blake2(plainMsg);
    }

    public byte[] getEncoded() {
        if (encoded != null) return encoded;

        byte[] isDelegate = new byte[1];
        if (this.isDelegate) {
            isDelegate[0] = (byte)0x01;
        } else {
            isDelegate[0] = (byte)0x00;
        }

        this.encoded = TRENC.encode(address, pubkey, bigIntegerToBytes(nonce, 32), bigIntegerToBytes(balance, 32), isDelegate, bigIntegerToBytes(votes, 32), bigIntegerToBytes(voters, 32), votingFor);

        hash = this.getHash();

        return encoded;
    }
}
