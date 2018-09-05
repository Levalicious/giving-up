package util.trie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class kTrie<T>  {
        private Buckets triePool;

        /** The tree's root node. */
        private kTrieNode<T> root;

        /**
         * <p>Creates a new {@link util.trie.Trie} instance with the specified capacity.</p>
         *
         * @param capacit The maximum number of elements within a node.
         */
        public kTrie(int capacit) {
            triePool = new Buckets();
            this.root = new kTrieNode<T>("", capacit, 0, triePool);
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

        public Buckets getLeaves() {
            return triePool;
        }

        @Override
        public String toString() {
            return root.toString();
        }
    }