package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
* See the spec and IDictionary for more details on what each method should do
*/
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    
    // You're encouraged to add extra fields (and helper methods) though!
    private int hashSize;
    
    public ChainedHashDictionary() {
        this.chains = makeArrayOfChains(100);
        this.hashSize = 0;
    }
    
    /**
    * This method will return a new, empty array of the given size
    * that can contain IDictionary<K, V> objects.
    *
    * Note that each element in the array will initially be null.
    */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }
    
    private int hashFunction(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % this.chains.length);
    }
    
    @Override
    public V get(K key) {
        int hashIndex = this.hashFunction(key);
        if (this.chains[hashIndex] != null && this.chains[hashIndex].containsKey(key)) {
            return this.chains[hashIndex].get(key);
        }
        throw new NoSuchKeyException("There is not such key.");
    }
    
    @Override
    public void put(K key, V value) {
        int hashIndex = this.hashFunction(key);
        if (this.chains[hashIndex] == null) {
            this.chains[hashIndex] = new ArrayDictionary<K, V>();
        }
        if (!this.chains[hashIndex].containsKey(key)) {
            this.hashSize++;
        }
        this.chains[hashIndex].put(key, value);
        if (this.hashSize > 0.75 * this.chains.length) {
            this.chains = this.doubleSize();
        }
    }
    
    private IDictionary<K, V>[] doubleSize() {
        int newSize = this.chains.length * 2;
        IDictionary<K, V>[] newChains = this.makeArrayOfChains(newSize);
        for (int i = 0; i < this.chains.length; i++) {
            if (this.chains[i] != null) {
                for (KVPair<K, V> pair : this.chains[i]) {
                    int hashIndex = Math.abs(pair.getKey().hashCode() % newSize);
                    if (newChains[hashIndex] == null) {
                        newChains[hashIndex] = new ArrayDictionary<K, V>();
                    }
                    newChains[hashIndex].put(pair.getKey(), pair.getValue());
                }
            }
        }
        return newChains;
    }
    
    @Override
    public V remove(K key) {
        int hashIndex = this.hashFunction(key);
        try {
            hashSize--;
            if (this.chains[hashIndex] != null) {
                return this.chains[hashIndex].remove(key);
            } else {
                throw new NoSuchKeyException();
            }
        } catch (NoSuchKeyException e) {
            hashSize++;
            throw e;
        }
    }
    
    @Override
    public boolean containsKey(K key) {
        int hashIndex = this.hashFunction(key);
        if (this.chains[hashIndex] != null && chains[hashIndex].containsKey(key)) {
            return true;
        }
        return false;
    }
    
    @Override
    public int size() {
        return this.hashSize;
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }
    
    /**
    * Hints:
    *
    * 1. You should add extra fields to keep track of your iteration
    *    state. You can add as many fields as you want. If it helps,
    *    our reference implementation uses three (including the one we
    *    gave you).
    *
    * 2. Before you try and write code, try designing an algorithm
    *    using pencil and paper and run through a few examples by hand.
    *
    * 3. Think about what exactly your *invariants* are. An *invariant*
    *    is something that must *always* be true once the constructor is
    *    done setting up the class AND must *always* be true both before and
    *    after you call any method in your class.
    *
    *    Once you've decided, write them down in a comment somewhere to
    *    help you remember.
    *
    *    You may also find it useful to write a helper method that checks
    *    your invariants and throws an exception if they're violated.
    *    You can then call this helper method at the start and end of each
    *    method if you're running into issues while debugging.
    *
    *    (Be sure to delete this method once your iterator is fully working.)
    *
    * Implementation restrictions:
    *
    * 1. You **MAY NOT** create any new data structures. Iterators
    *    are meant to be lightweight and so should not be copying
    *    the data contained in your dictionary to some other data
    *    structure.
    *
    * 2. You **MAY** call the `.iterator()` method on each IDictionary
    *    instance inside your 'chains' array, however.
    */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private Iterator<KVPair<K, V>> iter;
        private int hashIndex;
        
        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            while (hashIndex < this.chains.length) {
                if (this.chains[hashIndex] == null || this.chains[hashIndex].isEmpty()) {
                    hashIndex++;
                } else {
                    iter = this.chains[hashIndex].iterator();
                    break;
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            if (this.hashIndex == this.chains.length) {
                return false;
            }
            Iterator<KVPair<K, V>> iterTemp = this.iter;
            if (iterTemp.hasNext()) {
                return true;
            } else {
                int hashIndexTemp = this.hashIndex;
                
                while (hashIndexTemp < this.chains.length - 1) {
                    hashIndexTemp++;
                    if (this.chains[hashIndexTemp] != null && !this.chains[hashIndexTemp].isEmpty()) {
                        iterTemp = this.chains[this.hashIndex].iterator();
                        break;
                    }
                }
                return iterTemp.hasNext();
            }
        }
        
        @Override
        public KVPair<K, V> next() {
            if (hashIndex == this.chains.length) {
                throw new NoSuchElementException();
            }
            if (iter.hasNext()) {
                return iter.next();
            } else {
                while (this.hashIndex < this.chains.length - 1) {
                    this.hashIndex++;
                    if (this.chains[this.hashIndex] != null && !this.chains[this.hashIndex].isEmpty()) {
                        iter = this.chains[this.hashIndex].iterator();
                        break;
                    }
                }
                return iter.next();
            }
        }
    }
}
