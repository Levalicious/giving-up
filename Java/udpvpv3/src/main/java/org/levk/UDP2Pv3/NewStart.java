package org.levk.UDP2Pv3;

import org.levk.SchnorrCode.crypto.SchnorrKey;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.levk.UDP2Pv3.util.ByteUtils.*;
import static org.levk.UDP2Pv3.util.HashUtil.blake2omit12;

public class NewStart {
    static volatile long start;
    static volatile long end;
    static volatile boolean testing;

    public static void main(String[] args) throws Exception {
        Vector<BigInteger> timestamps = new Vector<>();
        Vector<BigInteger> difficulties = new Vector<>();

        int threadcount = 6;

        int counter = 0;

        boolean running = true;

        while (running) {
            ThreadPoolExecutor e = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadcount);

            final BigInteger target = nextDifficulty(timestamps, difficulties);

            testing = true;
            start = System.currentTimeMillis() / 1000;

            for (int l = 0; l < threadcount; l++) {
                e.submit(() -> {

                    int i = 0;

                    byte[] pubkey = new SchnorrKey().getPubkey();

                    while (testing) {
                        BigInteger hash = bytesToBigInteger(blake2omit12(merge(intToBytes(i), pubkey)));

                        if (hash.compareTo(target) < 0) {
                            end = System.currentTimeMillis() / 1000;

                            testing = false;
                        } else {
                            if (i == -1) {
                                System.out.println("Beep beep overflow bitch");
                                pubkey = new SchnorrKey().getPubkey();
                            }
                            i++;
                        }
                    }
                });
            }

            e.shutdown();

            e.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

            timestamps.add(BigInteger.valueOf(end));
            difficulties.add(target);

            BigInteger newTarget = nextDifficulty(timestamps, difficulties);

            SimpleDateFormat form = new SimpleDateFormat("HH:mm:ss");
            Date d = new Date();

            System.out.println("Block " + ++counter + " took " + (((end - start))) + " seconds (" + (int)(((double)(end - start) / (double)(60))) + " minutes) at a target of " + pad(target.toString(16)) + ". Adjusting to " + pad(newTarget.toString(16)) + " at " + form.format(d) + ".");
        }
    }

    public static BigInteger nextDifficulty(Vector<BigInteger> timestamps, Vector<BigInteger> difficulties) {
        if (timestamps.size() > 10) {
            /* Target for 30 minutes */
            BigInteger T = BigInteger.valueOf(30 * 60);

            BigInteger N = BigInteger.valueOf((int)Math.floor(Math.pow(45.0 * 600.0 / T.floatValue(), (Math.pow(0.2, Math.pow(600 / (double)T.floatValue(), 0.2)) + 0.5))));

            if (timestamps.size() - 1 < N.intValueExact()) {
                N = BigInteger.valueOf(timestamps.size() - 1);
            }

            BigInteger k = N.multiply(N.add(BigInteger.ONE)).multiply(T.divide(BigInteger.TWO));


            BigInteger sum_target = BigInteger.ZERO;
            BigInteger t = BigInteger.ZERO;
            BigInteger j = BigInteger.ZERO;
            BigInteger solveTime;

            for (int i = timestamps.size() - N.intValueExact(); i < timestamps.size(); i++) {
                solveTime = timestamps.get(i).subtract(timestamps.get(i - 1));
                solveTime = max(T.multiply(BigInteger.valueOf(-6)), min(solveTime, T.multiply(BigInteger.valueOf(6))));
                j = j.add(BigInteger.ONE);
                t = t.add(solveTime.multiply(j));
                BigInteger target = difficulties.get(i);
                sum_target = sum_target.add(target.divide(k.multiply(N)));
            }

            if (t.compareTo(k.divide(BigInteger.TEN)) < 0) {
                t = k.divide(BigInteger.TEN);
            }

            BigInteger next_target = t.multiply(sum_target);

            return next_target;
        } else {
            BigInteger difficulty_guess = new BigInteger("197aa8d559eb679be2438dcb41cd87d90", 16);
            return difficulty_guess;
        }
    }

    public static BigInteger max(BigInteger p, BigInteger q) {
        if (p.compareTo(q) <= 0) return q;

        return p;
    }

    public static BigInteger min(BigInteger p, BigInteger q) {
        if (p.compareTo(q) >= 0) return q;

        return p;
    }

    public static String pad(String s) {
        while (s.length() < 40) {
            s = "0" + s;
        }

        return s;
    }
}
