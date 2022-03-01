package bstmap;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private int size;
    private BSTNode root;

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left;
        private BSTNode right;

        private BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    public BSTMap() {
        size = 0;
        root = null;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (iterateToNode(root, key) == null) {
            return false;
        }
        return iterateToNode(root, key).key != null;
    }


    @Override
    public V get(K key) {
        if (iterateToNode(root, key) == null) {
            return null;
        }
        return iterateToNode(root, key).value;
    }

    private BSTNode iterateToNode(BSTNode node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            return iterateToNode(node.right, key);
        }
        if (cmp < 0) {
            return iterateToNode(node.left, key);
        }
        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode node, K key, V value) {
        if (node == null) {
            size += 1;
            return new BSTNode(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            node.right = put(node.right, key, value);
        }
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        }
        return node;
    }


    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(BSTNode node) {
        if (node != null) {
            printInOrder(node.left);
            System.out.println(node.key);
            printInOrder(node.right);
        }
    }

    /** not required. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("unsupported!");
    }

    /** not required. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("unsupported!");
    }

    /** not required. */
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("unsupported!");
    }

    /** not required. */
    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("unsupported!");
    }
}
