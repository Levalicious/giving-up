package util.trie;

import core.Hashable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * <p>A tree whose nodes can contain a specified number of elements before splitting into more nodes.</p>
 * <p>The location of an element when it is inserted into the tree is determined by its toString() method.</p>
 *
 * @author Brendan Jones
 *
 */
public class PrefixTree<T extends Hashable> {
    /** The tree's root node. */
    private PrefixTreeNode<T> root;

    /**
     * <p>Creates a new {@link PrefixTree} instance with the specified capacity.</p>
     *
     * @param capacit The maximum number of elements within a node.
     */
    public PrefixTree(int capacit) {
        this.root = new PrefixTreeNode<T>("", capacit, 0);
    }

    /**
     * <p>Adds the specified element to the tree.</p>
     */
    public void add(T element) {
        Objects.requireNonNull(element, "Attempted to add null element to tree.");
        root.add(element);
    }

    /**
     * <p>Adds the specified elements to the tree.</p>
     *
     * @param elements The elements to add.
     */
    public void addAll(T[] elements) {
        Objects.requireNonNull(elements, "Attempted to add null elements to tree.");

        for(T element : elements) {
            add(element);
        }
    }

    /**
     * <p>Adds the specified elements to the tree.</p>
     *
     * @param elements The elements to add.
     */
    public void addAll(Collection<T> elements) {
        Objects.requireNonNull(elements, "Attempted to add null elements to tree.");
        for(T element : elements) {
            add(element);
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }
}