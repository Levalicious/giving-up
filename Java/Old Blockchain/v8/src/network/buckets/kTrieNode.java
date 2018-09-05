package network.buckets;

import network.Peer;
import resources.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class kTrieNode {
    /* List of peers in this bucket */
    private ArrayList<Peer> values;

    /* List of children this node has */
    private Map<Character, kTrieNode> children;

    /* Node prefix (Bucket identifier */
    private String prefix;

    /* Max bucket size (k value for whole tree */
    private int capacity;

    /* Depth on the tree */
    private int depth;

    public kTrieNode(String prefix, int capacity, int depth) {
        this.prefix = prefix;
        this.capacity = capacity;
        this.depth = depth;
        this.values = new ArrayList<>();
        this.children = null;
    }

    public ArrayList<Peer> getValues() {
        return values;
    }

    public ArrayList<Peer> search(Peer value) {
        if(!value.hasDist()) value.calcDist(Constants.node);

        String temp = value.getDist();

        /* Identifies which child to search for the requested value */
        if(temp.length() <= depth) {
            throw new RuntimeException("The value is not long enough to split further (" + value + ").");
        }

        char key = temp.charAt(depth);

        /* If the node identified has values, return them. Else, search that node's children. */
        if(children.get(key).isLeafNode()) {
            return children.get(key).getValues();
        } else {
            return children.get(key).search(value);
        }
    }

    public void add(Peer value) {
        Objects.requireNonNull(value, "Attempted to add a null peer.");

        if(!prefix.endsWith("1")) {
            if(isLeafNode()) {
                if(values.size() == capacity && !values.contains(value)) {
                    convertToBranchNode();
                    addToBranchNode(value);
                } else {
                    if(values.contains(value)) {
                        System.out.println("Node alreading in buckets.");
                    } else {
                        values.add(value);
                    }
                }
            } else {
                addToBranchNode(value);
            }
        } else {
            if(!values.contains(value)) {
                if(values.size() < capacity) {
                    values.add(value);
                } else {
                    ArrayList<Peer> toCut = new ArrayList<>();

                    for(Peer p : values) {
                        if(p.isOld()) {
                            Constants.client.schedulePing(p);
                        }

                        if(p.toTerminate()) {
                            toCut.add(p);
                        }
                    }

                    values.removeAll(toCut);

                    if(values.size() < capacity) {
                        values.add(value);
                    }
                }
            }

            System.out.println("K-bucket " + prefix + " cannot be split further.");
        }
    }

    public void remove(Peer value) {
        if(isLeafNode()) {
            values.remove(value);
        } else {
            if(!value.hasDist()) value.calcDist(Constants.node);

            String temp = value.getDist();

            if(temp.length() <= depth) {
                throw new RuntimeException("The value is not long enough to split further (" + value + ").");
            }

            char key = temp.charAt(depth);

            children.get(key).remove(value);
        }
    }

    private void addToBranchNode(Peer value) {
        if(!value.hasDist()) value.calcDist(Constants.node);

        String temp = value.getDist();

        if(temp.length() <= depth) {
            throw new RuntimeException("The value is not long enough to split further (" + value + ").");
        }

        char key = temp.charAt(depth);

        kTrieNode child = children.computeIfAbsent(key, (k) -> new kTrieNode(this.prefix + k, capacity, depth + 1));
        child.add(value);
    }

    private void convertToBranchNode() {
        this.children = new HashMap<>();
        for(Peer value : values) {
            addToBranchNode(value);
        }

        this.values = null;
    }

    public boolean isLeafNode() {
        return children == null;
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder();

        addIndent(bldr, ' ').append("Node ('").append(prefix).append("'):\n");
        if(isLeafNode()) {
            for(Peer value : values) {
                addIndent(bldr, ' ').append("- ").append(value).append('\n');
            }
        } else {
            for(kTrieNode child : children.values()) {
                bldr.append(child.toString());
            }
        }

        return bldr.toString();
    }

    public String getPrefix() {
        return this.prefix;
    }

    /**
     * <p>Adds indentations to the node's output.</p>
     * @param bldr The builder to append to.
     * @param ch The character to indent with.
     * @return The builder.
     */
    private StringBuilder addIndent(StringBuilder bldr, char ch) {
        for(int i = 0; i < depth; ++i) {
            bldr.append(ch);
        }

        return bldr;
    }
}
