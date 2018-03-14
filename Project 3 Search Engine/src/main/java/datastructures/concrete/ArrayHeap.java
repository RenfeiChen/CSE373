/*

Author : Renfei Chen
         HanTao Liu

*/
package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int heapsize;

    // Feel free to add more fields and constants.
    private void percolateup(int index) {
        int parent = (index - 1) / NUM_CHILDREN;
        if (index == 0 || this.heap[index].compareTo(this.heap[parent]) >= 0) {
            return;
        }
        T temp;
        temp = this.heap[index];
        this.heap[index] = this.heap[parent];
        this.heap[parent] = temp;
        percolateup(parent);
    }

    private void percolatedown(int index) {
        int child = index * NUM_CHILDREN + 1;
        if (child > this.heapsize - 1) {
            return;
        }
        T temp = this.heap[child];
        int range = child + NUM_CHILDREN;
        int min = child;
        if (range > this.heapsize) {
            range = this.heapsize;
        }
        for (int i = child + 1; i < range; i++) {
            if (temp.compareTo(this.heap[i]) > 0) {
                min = i;
                temp = this.heap[i];
            }
        }
        if (this.heap[index].compareTo(this.heap[min]) <= 0) {
            return;
        }
        temp = this.heap[index];
        this.heap[index] = this.heap[min];
        this.heap[min] = temp;
        percolatedown(min);
    }

    public ArrayHeap() {
        this.heap = makeArrayOfT(100000);
        this.heapsize = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (this.heapsize == 0) {
            throw new EmptyContainerException();
        }
        T result = this.heap[0];
        this.heapsize--;
        if (this.heapsize == 0) {
            return result;
        }
        this.heap[0] = this.heap[this.heapsize];
        percolatedown(0);
        return result;
    }

    @Override
    public T peekMin() {
        if (this.heapsize == 0) {
            throw new EmptyContainerException();
        }
        return this.heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        this.heap[this.heapsize] = item;
        percolateup(this.heapsize);
        this.heapsize++;
    }

    @Override
    public int size() {
        return this.heapsize;
    }
}
