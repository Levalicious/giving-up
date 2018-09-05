package kademlia.buckets;

import kademlia.NodeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class kTrie {
    private kTrieNode root;

    public kTrie(int capacit) {
        this.root = new kTrieNode("", capacit, 0);
    }

    public void add(NodeInfo element) {
        Objects.requireNonNull(element, "Attempted to add null element to the tree.");
        root.add(element);
    }

    public void addAll(NodeInfo[] elements) {
        Objects.requireNonNull(elements, "Attempted to add null elements to the tree.");

        for(NodeInfo element : elements) {
            add(element);
        }
    }

    public void addAll(Collection<NodeInfo> elements) {
        Objects.requireNonNull(elements, "Attempted to add null elements to tree." );

        for(NodeInfo element : elements) {
            add(element);
        }
    }

    public ArrayList<NodeInfo> search(NodeInfo element) {
        if(root.isLeafNode()) return root.getValues();
        return root.search(element);
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
