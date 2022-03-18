package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private double maxLoad;
    private int size;
    private HashSet<K> keySet;
    private Iterator<K> keySetIterator;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.buckets = createTable(initialSize);
        this.maxLoad = maxLoad;
        this.size = 0;
        this.keySet = new HashSet<>();
        this.keySetIterator = keySet.iterator();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        this.buckets = createTable(size());
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (get(key) != null) {
            return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        int keyHashCode = key.hashCode();
        int keyIndex = Math.floorMod(keyHashCode, buckets.length);
        if (buckets[keyIndex] == null) {
            return null;
        }
        for (Node node : buckets[keyIndex]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int keyHashCode = key.hashCode();
        int keyIndex = Math.floorMod(keyHashCode, buckets.length);
        if (buckets[keyIndex] == null) {
            buckets[keyIndex] = createBucket();
        }

        /* check if key node exist. */
        if (keySet.contains(key)) {
            for (Node node : buckets[keyIndex]) {
                if (node.key.equals(key)) {
                    buckets[keyIndex].remove(node);
                    size -= 1;
                    keySet.remove(key);
                    break;
                }
            }
        }

        buckets[keyIndex].add(createNode(key, value));
        size += 1;
        keySet.add(key);

        if (size/buckets.length >= maxLoad) {
            Collection<Node>[] newBuckets = createTable(2 * buckets.length);
            for (K keyPointer : keySet) {
                int newKeyIndex = Math.floorMod(keyPointer.hashCode(), newBuckets.length);
                if (newBuckets[newKeyIndex] == null) {
                    newBuckets[newKeyIndex] = createBucket();
                }
                newBuckets[newKeyIndex].add(createNode(keyPointer, get(keyPointer)));
            }
            this.buckets = newBuckets;
        }

        this.keySetIterator = keySet.iterator();
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public Iterator<K> iterator() {
        return keySetIterator;
    }

    /** not required */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("unsupported!");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("unsupported!");
    }

}
