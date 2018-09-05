package kademlia.buckets;

import kademlia.NodeInfo;
import resources.Constants;

import java.util.*;

public class kTrieNode {
    private Set<NodeInfo> values;

    private Map<Character, kTrieNode> children;

    private String prefix;

    private int capacity;

    private int depth;

    public kTrieNode(String prefix, int capacity, int depth) {
        this.prefix = prefix;
        this.capacity = capacity;
        this.depth = depth;
        this.values = new TreeSet<>();
        this.children = null;
    }

    public ArrayList<NodeInfo> getValues() {
        return new ArrayList<>(values);
    }

    public ArrayList<NodeInfo> search(NodeInfo value) {
        if(!value.hasDist()) value.setDist(Constants.node);

        String temp = value.getDist();

        if(temp.length() <= depth) {
            throw new RuntimeException("The value is not long enough to split further (" + value + ").");
        }

        char key = temp.charAt(depth);

        if(children.get(key).isLeafNode()) {
            return children.get(key).getValues();
        }else {
            return children.get(key).search(value);
        }
    }

    public void add(NodeInfo value) {
        Objects.requireNonNull(value, "Attempted to add a null value.");

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
            if(values.size() < capacity) {
                if (!values.contains(value)) {
                    values.add(value);
                }
            }
            System.out.println("K-bucket " + prefix + " cannot be split further.");
        }
    }

    private void addToBranchNode(NodeInfo value) {
        if(!value.hasDist()) value.setDist(Constants.node);

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
        for(NodeInfo value : values) {
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
            for(NodeInfo value : values) {
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
