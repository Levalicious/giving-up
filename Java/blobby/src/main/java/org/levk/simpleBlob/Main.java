package org.levk.simpleBlob;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.levk.simpleBlob.ENC3B.*;

public class Main {
    static long average = 0;
    public static void main(String[] args) throws Exception {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");

        byte[] dat1 = new byte[35];

        rand.nextBytes(dat1);

        boolean running = true;
        long count = 0;
        long found = 0;

        while (running) {
            count++;
            if (isEnc3b(dat1)) {
                updateAverage(count, found);
                found++;
                System.out.println("Valid encoding found in the form of \n" + new ENCItem(dat1).toString() + "\nafter " + count + " attempts. Average: " + average + ". Data point " + found);
                count = 0;
            }
            rand.nextBytes(dat1);
        }
    }

    public static void updateAverage(long x, long n) {
        if (average != 0) {
            long num = (x + n * average);
            long den = n + 1;

            average = num / den;
        } else {
            average = x;
        }
    }
}
