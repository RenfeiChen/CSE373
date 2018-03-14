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
    private int size;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        chains = makeArrayOfChains(10539);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * IDictionary<K, V> objects.
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

    @Override
    public V get(K key) {
        int index = 0;
        if (key != null) {
            index = key.hashCode() % chains.length;
            if (index < 0) {
                index += chains.length;
            }
        }
        if (chains[index] == null || !chains[index].containsKey(key)) {
            throw new NoSuchKeyException();
        } else {
            return chains[index].get(key);
        }
    }

    @SuppressWarnings("rawtypes")
    public void put(K key, V value) {
        int index = 0;
        if (key != null) {
            index = key.hashCode() % chains.length;
            if (index < 0) {
                index += chains.length;
            }
            if (index > chains.length) {
            	@SuppressWarnings("unchecked")
				ChainedIterator<K, V> itr = new ChainedIterator(chains);
            	IDictionary<K, V>[] newChains = makeArrayOfChains(chains.length * 2);
            	while (itr.hasNext()) {
            		KVPair<K, V> pair = itr.next();
            		int newIndex = pair.getKey().hashCode();
            		newChains[newIndex] = chains[newIndex];
            	}
            	chains = newChains;
            }
        }
        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<K, V>();
        }
        if (!chains[index].containsKey(key)) {
            chains[index].put(key, value);
            size++;
        } else {
            chains[index].remove(key);
            chains[index].put(key, value);
        }
    }

    @Override
    public V remove(K key) {
        int index = 0;
        if (key != null) {
            index = key.hashCode() % chains.length;
            if (index < 0) {
                index += chains.length;
            }
        }
        if (chains[index] == null || !chains[index].containsKey(key)) {
            throw new NoSuchKeyException();
        } else {
            size--;
            return chains[index].remove(key);
        }
    }

    @Override
    public boolean containsKey(K key) {
        int index = 0;
        if (key != null) {
            index = key.hashCode() % chains.length;
            if (index < 0) {
                index += chains.length;
            }
        }
        if (chains[index] == null) {
            return false;
        } else {
            return (chains[index].containsKey(key));
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration state. You can
     * add as many fields as you want. If it helps, our reference implementation
     * uses three (including the one we gave you).
     *
     * 2. Before you try and write code, try designing an algorithm using pencil and
     * paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant* is
     * something that must *always* be true once the constructor is done setting up
     * the class AND must *always* be true both before and after you call any method
     * in your class.
     *
     * Once you've decided, write them down in a comment somewhere to help you
     * remember.
     *
     * You may also find it useful to write a helper method that checks your
     * invariants and throws an exception if they're violated. You can then call
     * this helper method at the start and end of each method if you're running into
     * issues while debugging.
     *
     * (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators are meant to be
     * lightweight and so should not be copying the data contained in your
     * dictionary to some other data structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary instance
     * inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int pos;
        public Iterator<KVPair<K, V>> iter;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            pos = -1;
        }

        @Override
        public boolean hasNext() {
            if (pos != -1) {
                if (iter.hasNext()) {
                    return true;
                }
            }
            for (int i = pos + 1; i < chains.length; i++) {
                if (chains[i] != null && chains[i].size() != 0) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            if (hasNext()) {
                if (pos != -1) {
                    if (iter.hasNext()) {
                        return iter.next();
                    }
                }
                for (int i = pos + 1; i < chains.length; i++) {
                    if (chains[i] != null && chains[i].size() != 0) {
                        pos = i;
                        iter = chains[pos].iterator();
                        return iter.next();
                    }
                }
                throw new NoSuchElementException();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
