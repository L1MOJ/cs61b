package hashmap;

import java.security.Key;
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
    private int elementNum;
    private int bucketNum;
    private Set<K> keys;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_INITIAL_SIZE = 16;

    private int initialSize;
    private double loadFactor;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_INITIAL_SIZE,DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize,DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.loadFactor = maxLoad;
        this.elementNum = 0;
        this.bucketNum = initialSize;
        buckets = createTable(bucketNum);
        for (int i = 0; i < bucketNum; i++) {
            buckets[i] = createBucket();
        }
        keys = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {

        Node newNode = new Node(key,value);
        return newNode;
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
    public void clear() {
        for (int i =0; i < bucketNum; i++) {
            buckets[i].clear();
        }
        keys.clear();
        this.elementNum = 0;

    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    public Node getNode(K key) {
        if (key==null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }
        int i = getHashCode(key);
        for(Node node : buckets[i]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    public int getHashCode(K key) {
        return Math.floorMod(key.hashCode(),bucketNum);
    }
    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        Node res = getNode(key);
        if (res==null) {
            return null;
        }
        return res.value;
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return elementNum;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    public void put(K key, V value) {
        if (key==null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }
        Node ins = getNode(key);
        if (ins == null) {
            int i = getHashCode(key);
            Node newNode = new Node(key,value);
            buckets[i].add(newNode);
            elementNum++;
            keys.add(key);
            if(elementNum/bucketNum > loadFactor) {
                resize(bucketNum*2);
            }
        }
        else {
            ins.value = value;
        }
    }
    private void resize(int bucketNum) {
        MyHashMap<K,V> newMap = new MyHashMap<>(bucketNum,this.loadFactor);
        for(K key : keySet()) {
            newMap.put(key,get(key));
        }
        this.buckets = newMap.buckets;
        this.bucketNum = newMap.bucketNum;

    }
    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        return keys;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
