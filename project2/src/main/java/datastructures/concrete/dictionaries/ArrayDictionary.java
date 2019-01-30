package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;
import datastructures.concrete.KVPair;
//import datastructures.concrete.dictionaries.ChainedHashDictionary.ChainedIterator;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!
    private int size;
    public ArrayDictionary() {
        this.pairs = this.makeArrayOfPairs(0);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if (key == null) {
                if (pairs[i].key == null) {
                    return pairs[i].value;
                }
            } else if (key.equals(pairs[i].key)) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
    	boolean containsKey = false;
    	for (int i = 0; i < this.size() && !containsKey; i++) {
    		if (this.pairs[i].key != null && this.pairs[i].key.equals(key) ||
    			this.pairs[i].key == null && key == null) {
    			this.pairs[i].value = value;
    			containsKey = true;
    		}
    	}
    	if (!containsKey) {
    		if (this.size() == 0 || this.pairs[this.size() - 1] != null) {
    			this.doubleSize();
    		}
    		this.pairs[this.size()] = new Pair<K, V>(key, value);
    		this.size++;
    	}
    }
    
    private void doubleSize() {
    	int newSize = 0;
    	if (this.size() == 0) {
    		newSize = 1;
    	} else {
    		newSize = this.size() * 2;
    	}
    	Pair<K, V>[] newArray = makeArrayOfPairs(newSize);
    	for (int i = 0; i < this.size(); i++) {
    		newArray[i] = this.pairs[i];
    	}
    	this.pairs = newArray;
    }

    @Override
    public V remove(K key) {
    	for (int i = 0; i < this.size(); i++) {
    		if (this.pairs[i].key != null && this.pairs[i].key.equals(key) ||
        		this.pairs[i].key == null && key == null) {
    			V rmValue = this.pairs[i].value;
    			shiftRestLeft(i);
    			return rmValue;
    		}
    	}
    	throw new NoSuchKeyException("There is no such key.");
    }
    
    private void shiftRestLeft(int startIndex) {
    	for (int i = startIndex; i < this.size() - 1; i++) {
    		this.pairs[i] = this.pairs[i + 1];
    	}
    	this.pairs[this.size() - 1] = null;
    	this.size--;
    }

    @Override
    public boolean containsKey(K key) {
    	for (int i = 0; i < this.size(); i++) {
    		if (this.pairs[i].key != null && this.pairs[i].key.equals(key) ||
        		this.pairs[i].key == null && key == null) {
    			return true;
    		}
    	}
    	return false;
    }

    @Override
    public int size() {
    	return this.size;
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ArrayDictionaryIterator<>(this.pairs, this.size);
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
	    public Pair(K key, V value) {
	        this.key = key;
	        this.value = value;
	    }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int size = 0;
        private int index = -1;
        
        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
        	return index + 1 < size;
        }
        
        @Override
        public KVPair<K, V> next() {
        	if (!this.hasNext()) {
        		throw new NoSuchElementException("There is no next element.");
        	}
        	index++;
        	return new KVPair<K, V>(pairs[index].key, pairs[index].value);
        }
    }
}

