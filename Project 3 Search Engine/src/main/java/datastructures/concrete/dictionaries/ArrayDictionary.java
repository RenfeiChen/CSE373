package datastructures.concrete.dictionaries;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
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
    private int size;

    // You're encouraged to add extra fields (and helper methods) though!
    //Create a new Pair object called pairs and initialize variable size
	@SuppressWarnings("unchecked")
	public ArrayDictionary() {
		pairs = (Pair<K, V>[]) new Pair[4];
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
    
    //Take in a key and return the value to the corresponding key, allow the user to know the value
    @Override
    public V get(K key) {
        for (int i = 0; i < size; i++) {
        	if (pairs[i].key == key || pairs[i].key.equals(key)) {
        		return pairs[i].value;
        	}
        }
        throw new NoSuchKeyException();
    }
    
    //Take in a key and a value, allow the user to input new items into the array
	@Override
    public void put(K key, V value) {
		boolean find = false;
		Pair<K, V> newPair = new Pair<K, V>(key, value);
		for (int i = 0; i < size; i++) {
			if (pairs[i].key == key || (pairs[i].key != null && pairs[i].key.equals(key))) {
				pairs[i].value = value;
				find = true;
				break;
			}
		}
		if (!find) {
			if (!(size == pairs.length)) {
				pairs[size] = newPair;
				size++;
			} else {
				Pair<K, V>[] newArray = makeArrayOfPairs(pairs.length * 2);
				for (int i = 0; i < pairs.length; i++) {
					newArray[i] = pairs[i];
				}
				pairs = newArray;
				pairs[size] = newPair;
				size++;
			}
		}
    }
	
	//Take in the key and return the value, allow user to remove target key in the array
    @Override
    public V remove(K key) {
    	boolean toRemove = false;
    	Pair<K, V> newPair = new Pair<K, V>(key, null);
    	for (int i = 0; i < size; i++) {
    		if (pairs[i].key == key || pairs[i].key.equals(key)) {
    			toRemove = true;
    			newPair = pairs[i];
    		}
    		if (toRemove) {
    			if (i != size - 1) {
    				pairs[i] = pairs[i + 1];
    			} else {
    				pairs[i] = null;
    			}
    		}
    	}
    	if (!toRemove) {
    		throw new NoSuchKeyException();
    	}
    	size--;
    	return newPair.value;
    }
    
    //Take in a key and return a boolean type, allow the user to know if the input key exist in the list
    @Override
    public boolean containsKey(K key) {
    	for (int i = 0; i < size; i++) {
        	if (pairs[i].key == key || (pairs[i].key != null && pairs[i].key.equals(key))) {
        		return true;
        	}
        }
    	return false;
    }
    
    //Return integer type, allow user to know the size of the array
    @Override
    public int size() {
    	return size;
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
    
    public Iterator<KVPair<K, V>> iterator(){
    	return new PairIterator<>(this.pairs);
    }
    
	private class PairIterator<K, V> implements Iterator<KVPair<K, V>> {
    	private Pair<K, V>[] pairs;
    	private int location;
    	
    	public PairIterator(Pair<K, V>[] pairs) {
    		this.pairs = pairs;
            this.location = 0;
        }

        @Override
        public boolean hasNext() {
            return (location < size);
        }

		@Override
        public KVPair<K, V> next() {
        	if (hasNext()) {
	        	KVPair<K, V> newPair = new KVPair<K, V>(pairs[location].key, pairs[location].value);
	        	location++;
	        	return newPair;
        	} else { 
        		throw new NoSuchElementException();
        	}
        }
    }
    
}
