import core.types.Blockchain;
import core.types.PoolManager;
import core.types.block.Block;
import core.types.block.Payload;
import core.types.block.PrevBlock;
import core.types.pools.TreeNode;
import core.types.pools.TxTree;
import core.types.transaction.Transaction;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import user.Wallet;

import java.security.Security;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

import static crypto.Hash.sha256;

public class Main {
    static ConcurrentHashMap<String, Block> blockPool;
    static Blockchain chain;
    static TxTree<Transaction> tree;
    static int cores = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        System.out.println("Would you like to generate new transactions? Respond wish 'yes' if you would.");
        Scanner s = new Scanner(System.in);
        if(s.next().equalsIgnoreCase("yes")) {
            ExecutorService genPool = Executors.newFixedThreadPool(cores - 1);

            for(int i = 0; i < cores - 1; i++) {
                genPool.submit(new Runnable() {
                    public void run() {
                        PoolManager.fillPool(187500);
                    }
                });
            }
            genPool.shutdown();
            genPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            PoolManager.writePool("TxPool");
        }

        long mainInit = 0;
        long blockProcessEnd = 65000;
        long start = System.currentTimeMillis();
        PoolManager.readPool("TxPool");
        long readEnd = System.currentTimeMillis();
        System.out.println("Reading in the TxPool took " + (readEnd - start) + " ms.");

        int transactionCount = 100000;
        while(blockProcessEnd - mainInit > 15000) {
            PoolManager.trimPool(transactionCount);
            chain = new Blockchain();
            chain.genesis();
            tree = new TxTree<Transaction>(2000);
            mainInit = System.currentTimeMillis();
            System.out.println("Initializing the chain took " + (mainInit - readEnd) + " ms.");

            PoolManager.groupPool(tree);
            long groupEnd = System.currentTimeMillis();
            System.out.println("Grouping the transactions took " + (groupEnd - mainInit) + " ms.");

            blockPool = new ConcurrentHashMap<>();

            PoolManager.leaves.entrySet().stream().parallel().forEach(e -> processNode(e));

            for(ConcurrentHashMap.Entry<String, Block> node : blockPool.entrySet()) {
                chain.addBlock(node.getValue(), 1);
            }

            blockProcessEnd = System.currentTimeMillis();
            System.out.println("Adding blocks to the chain took " + (blockProcessEnd - groupEnd) + " ms.");
            System.out.println("Pool of " + transactionCount + " transactions.");
            System.out.println("Pool of " + blockPool.size() + " blocks.");
            tree = null;
        }

        long printEnd = System.currentTimeMillis();
        System.out.println("Printing the chain took " + (printEnd - blockProcessEnd) + " ms.");
    }

    public static void processNode(Map.Entry<String,TreeNode> node) {
        blockPool.put(node.getKey(), new Block(1, node.getKey(), "0x8009401c20ef106463a61d4d6045ac9926cb835a2262f3500a86982fdb381378be73345156", new Payload(node.getValue().getValues()), new PrevBlock(chain.getLayer(0).getBlocks(),true)));
    }
}
