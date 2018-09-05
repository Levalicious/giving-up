import user.Wallet;

import static crypto.Hash.*;

public class Main {
    public static void main(String[] args) {
        Wallet wallet1 = new Wallet();
        String sig = wallet1.sign("Y tho");
        System.out.println(sig);
        System.out.println(wallet1.verify(wallet1.getPubKey(),"Y tho", sig));
    }
}
