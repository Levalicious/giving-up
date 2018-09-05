import kademlia.NodeInfo;
import resources.Constants;
import kademlia.buckets.kTrie;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        kTrie tree = new kTrie(5);

        for(int i = 0; i < 1000; i++) {
            tree.add(new NodeInfo());
        }
        NodeInfo toFind = new NodeInfo();
        System.out.println("Searching the tree of address " + NodeInfo.fromBitSet(NodeInfo.fromByteArray(Constants.node.getID())) + " for the bucket of Node " + NodeInfo.fromBitSet(NodeInfo.fromByteArray(toFind.getID())) + ".");

        System.out.println("Found Bucket:" );
        System.out.println(Arrays.toString(tree.search(toFind).toArray()));

        Path file = Paths.get("out.txt");
        Files.write(file,tree.toString().getBytes());
    }
}
