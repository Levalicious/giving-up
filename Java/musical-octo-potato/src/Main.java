import core.*;
import mine.*;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        System.out.println(blockchain.getLayer(0).toString());

        Miner forger = new Miner();
        
        System.out.println();
        System.out.println();
        System.out.println(forger.randomString(32));
    }
}
