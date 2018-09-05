import user.Wallet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

import static user.Wallet.*;
import static utils.Base58.*;
import static utils.HexUtils.*;

public class Main {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        Wallet wallet1 = new Wallet();
        System.out.println(wallet1.toString());
        System.out.println(wallet1.getPubKey());
        System.out.println();
        System.out.println(wallet1.verify(wallet1.getPubKey(),"I", wallet1.sign("I")));
    }
}
