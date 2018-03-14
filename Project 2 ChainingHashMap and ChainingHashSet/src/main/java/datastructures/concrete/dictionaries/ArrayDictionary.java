package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
/*import datastructures.concrete.ChainedHashSet.SetIterator;
import datastructures.concrete.DoubleLinkedList.DoubleLinkedListIterator;
import datastructures.concrete.DoubleLinkedList.Node;*/
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
    public int size;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        pairs = this.makeArrayOfPairs(8);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * Pair<K, V> objects.
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

    public Iterator<KVPair<K, V>> iterator() {
        return new PairIterator<>(this.pairs);
    }

    public class PairIterator<K, V> implements Iterator<KVPair<K, V>> {
        // This should be the only field you need
        public Pair<K, V>[] pairs;
        public int position;

        public PairIterator(Pair<K, V>[] pairs) {
            this.pairs = pairs;
            this.position = 0;
        }

        @Override
        public boolean hasNext() {
            return (position < size);
        }

        @Override
        public KVPair<K, V> next() {
            if (hasNext()) {
                KVPair<K, V> newPair = new KVPair<K, V>(pairs[position].key, pairs[position].value);
                position++;
                return newPair;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    @Override
    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if ((this.pairs[i].key == null && key == null) || this.pairs[i].key.equals(key)) {
                return this.pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        if (this.containsKey(key)) {
            for (int i = 0; i < size; i++) {
                if ((this.pairs[i].key == null && key == null) || this.pairs[i].key.equals(key)) {
                    this.pairs[i].value = value;
                    return;
                }
            }
        } else {
            if (!(size == pairs.length)) {
                this.pairs[size] = new Pair<K, V>(key, value);
                size++;
                return;
            }
            else
            {
                Pair<K, V>[] newArray = makeArrayOfPairs(pairs.length * 2);
                for (int i = 0; i < pairs.length; i++) {
                    newArray[i] = pairs[i];
                }
                pairs = newArray;
                pairs[size] = new Pair<K, V>(key, value);
                size++;
            }
        }
    }

    @Override
    public V remove(K key) {
        int pos = 0;
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        for (int i = 0; i < size; i++) {
            if ((this.pairs[i].key == null && key == null) || this.pairs[i].key.equals(key)) {
                pos = i;
                break;
            }
        }
        Pair<K, V> del = this.pairs[pos];
        for (int i = pos; i < size - 1; i++) {
            this.pairs[i] = this.pairs[i + 1];
        }
        this.pairs[size - 1] = null;
        size--;
        return del.value;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < size; i++) {
            if (this.pairs[i].key == null) {
                if (key == null) {
                    return true;
                }
            } else if (this.pairs[i].key.equals(key)) {
                return true;
            }
        }
        return false;
    }

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
}
