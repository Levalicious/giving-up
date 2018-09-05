package network.buckets;

import network.Peer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class kTrie {
    private network.buckets.kTrieNode root;
    private int capacity;

    public kTrie(int capacit) {
        this.root = new network.buckets.kTrieNode("", capacit, 0);
        this.capacity = capacit;
    }

    public void add(Peer element) {
        Objects.requireNonNull(element, "Attempted to add null element to the tree.");
        root.add(element);
    }

    public void addAll(Peer[] elements) {
        Objects.requireNonNull(elements, "Attempted to add null elements to the tree.");

        for(Peer element : elements) {
            add(element);
        }
    }

    public void addAll(Collection<Peer> elements) {
        Objects.requireNonNull(elements, "Attempted to add null elements to tree." );

        for(Peer element : elements) {
            add(element);
        }
    }

    public ArrayList<Peer> search(Peer element) {
        if(root.isLeafNode()) return root.getValues();
        return root.search(element);
    }

    public void remove(Peer n) {
        root.remove(n);
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
