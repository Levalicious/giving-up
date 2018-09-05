package mine;

import crypto.*;

public class Miner {
    public Miner() {

    }

    public static void forgeRandom() {

    }

    public String randomString(int length) {
        int temp = (int) Math.floor((Math.random() * (double)Integer.MAX_VALUE));
        if (length > 32) {
            System.out.println("A string of that length cannot be generated.");
            return "";
        } else {
            String s = StringUtil.applySha256(Integer.toString(temp)).substring(0, length);
            return s;
        }
    }
}
