package util.trie;

import core.Hashable;
import org.bouncycastle.util.encoders.Hex;

import java.util.*;

public class PrefixTreeNode<T extends Hashable> {
    /** The values stored in this node. Or null if it is a branch. */
    private Set<T> values;

    /** The children of this node. Or null if it is a leaf. */
    private Map<Character, PrefixTreeNode<T>> children;

    /** The prefix that all elements in this node must have. */
    private String prefix;

    /** The maximum number of elements this node can hold before splitting. */
    private int capacity;

    /** The depth in the tree of this node. */
    private int depth;

    /**
     * <p>Creates a new {@code BlockTrieNode} instance.</p>
     *
     * @param prefix The prefix for all elements in this node.
     * @param capacity The capacity of the node.
     * @param depth The depth of the node.
     */
    public PrefixTreeNode(String prefix, int capacity, int depth) {
        this.prefix = prefix;
        this.capacity = capacity;
        this.depth = depth;
        this.values = new TreeSet<>();
        this.children = null;
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
    }

    /**
     * <p>Adds the value to this node if it is a branch.</p>
     *
     * @param value The value to add.
     */
    private void addToBranchNode(T value) {
        String temp = Hex.toHexString(value.getAddress());


        if(temp.length() <= depth) {
            throw new RuntimeException("The value is not long enough to split further (" + value + ").");
        }

        char key = temp.charAt(depth);

        PrefixTreeNode<T> child = children.computeIfAbsent(key, (k) -> new PrefixTreeNode<T>(prefix + k, capacity, depth + 1));
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

        this.values = null;
    }

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
            for(PrefixTreeNode<T> child : children.values()) {
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