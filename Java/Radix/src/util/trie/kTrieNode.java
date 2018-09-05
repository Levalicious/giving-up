package util.trie;

import network.Node;
import network.NodeInfo;
import resources.Constants;

import java.util.*;

public class kTrieNode<T> {
    /** The values stored in this node. Or null if it is a branch. */
    private Set<T> values;

    /** The children of this node. Or null if it is a leaf. */
    private Map<Character, kTrieNode<T>> children;

    /** The prefix that all elements in this node must have. */
    private String prefix;

    /** The maximum number of elements this node can hold before splitting. */
    private int capacity;

    /** The depth in the tree of this node. */
    private int depth;

    private Buckets triePool;

    /**
     * <p>Creates a new {@code BlockTrieNode} instance.</p>
     *
     * @param prefix The prefix for all elements in this node.
     * @param capacity The capacity of the node.
     * @param depth The depth of the node.
     */
    public kTrieNode(String prefix, int capacity, int depth, Buckets triePool) {
        this.prefix = prefix;
        this.capacity = capacity;
        this.depth = depth;
        this.values = new TreeSet<>();
        this.children = null;
        this.triePool = triePool;
    }

    public ArrayList<T> getValues() {
        return new ArrayList<>(values);
    }

    /**
     * <p>Adds the specified value to the node.</p>
     *
     * @param value The value to add.
     */
    public void add(T value) {
        Objects.requireNonNull(value, "Attempted to add a null value.");
        if(!prefix.endsWith("1")) {
            if(isLeafNode()) {
                //Convert to a branch node if the value would put us over capacity.
                if(values.size() == capacity && !values.contains(value)) {
                    convertToBranchNode();
                    addToBranchNode(value);
                } else {
                    if(values.contains(value)) {
                        System.out.println("Transaction already in tree.");
                    }else{
                        values.add(value);
                    }
                }
            } else {
                addToBranchNode(value);
            }
        }else {
            if(values.size() < capacity) {
                if(!values.contains(value)) {
                    values.add(value);
                }
            }
            System.out.println("This k-bucket (" + prefix + ") cannot be split further.");
        }
    }

    /**
     * <p>Adds the value to this node if it is a branch.</p>
     *
     * @param value The value to add.
     */
    private void addToBranchNode(T value) {
        String temp = value.toString();


        if(temp.length() <= depth) {
            throw new RuntimeException("The value is not long enough to split further (" + value + ").");
        }

        char key = temp.charAt(depth);


        kTrieNode<T> child = children.computeIfAbsent(key, (k) -> new kTrieNode<T>(this.prefix + k, capacity, depth + 1, triePool));
        child.add(value);
    }

    /**
     * <p>Converts this node from a leaf node to a branch node.</p>
     */
    private void convertToBranchNode() {
        this.children = new HashMap<>();

        for(T value : values) {
            addToBranchNode(value);
        }

        triePool.remove(this.prefix);

        for(kTrieNode node : this.children.values()) {
            triePool.put(node.getPrefix(),node);
        }

        this.values = null;
    }

    /*
    private void trimDead() {
        for(T n: values) {
            NodeInfo x = (NodeInfo)n;
            if(!Constants.node.ping(x)) values.remove(x);
        }
    }
    */

    /**
     * <p>Checks whether this node is a leaf or a branch.</p>
     *
     * @return true if this node is a leaf, false if it is a branch.
     */
    public boolean isLeafNode() {

        return children == null;
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder();

        addIndent(bldr, ' ').append("Node ('").append(prefix).append("'):\n");
        if(isLeafNode()) {
            for(T value : values) {
                addIndent(bldr, ' ').append("- ").append(value).append('\n');
            }
        } else {
            for(kTrieNode<T> child : children.values()) {
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