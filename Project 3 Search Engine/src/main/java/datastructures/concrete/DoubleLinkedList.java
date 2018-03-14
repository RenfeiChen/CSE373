package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see the source
 * code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    public void add(T item) {
        Node<T> node = new Node<T>(item);
        this.size++;
        if (this.back == null) {
            this.front = node;
            this.back = node;
        } else {
            node.prev = this.back;
            this.back.next = node;
            this.back = node;
        }
    }

    public T remove() {
        Node<T> node = this.back;
        if (this.size == 0) {
            throw new EmptyContainerException("Not Found!!");
        }
        if (this.size == 1) {
            this.front = null;
            this.back = null;
            this.size--;
            return node.data;
        }
        this.back = node.prev;
        this.back.next = null;
        this.size--;
        return node.data;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Not Found!!");
        }
        if (index <= this.size / 2) {
            Node<T> node = this.front;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node.data;
        } else {
            Node<T> node = this.back;
            for (int i = this.size - 1; i > index; i--) {
                node = node.prev;
            }
            return node.data;
        }
    }

    public void set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Not Found!!");
        }
        this.delete(index);
        this.insert(index, item);
    }

    public void insert(int index, T item) {
        if (index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            if (this.size != 0) {
                Node<T> node = new Node<T>(null, item, this.front);
                this.front.prev = node;
                this.front = node;
                this.size++;
            } else {
                Node<T> node = new Node<T>(item);
                this.front = node;
                this.back = node;
                this.size++;
            }
        } else {
            if (index == this.size()) {
                add(item);
            } else {
                Node<T> nextnode = this.front;
                if (index <= this.size / 2) {
                    nextnode = this.front;
                    for (int i = 0; i < index; i++) {
                        nextnode = nextnode.next;
                    }
                } else {
                    nextnode = this.back;
                    for (int i = this.size - 1; i > index; i--) {
                        nextnode = nextnode.prev;
                    }
                }
                Node<T> prevnode = nextnode.prev;
                Node<T> node = new Node<T>(prevnode, item, nextnode);
                prevnode.next = node;
                nextnode.prev = node;
                this.size++;
            }
        }
    }

    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Not Found!!");
        } else {
            if (index == 0) {
                if (this.size() != 1) {
                    Node<T> del = this.front;
                    this.front = this.front.next;
                    this.front.prev = null;
                    this.size--;
                    return del.data;
                } else {
                    Node<T> del = this.front;
                    this.front = null;
                    this.back = null;
                    this.size = 0;
                    return del.data;
                }
            } else {
                if (index == (this.size - 1)) {
                    Node<T> del = this.back;
                    del.prev.next = null;
                    this.back = del.prev;
                    this.size--;
                    return del.data;
                } else {
                    Node<T> del = this.front;
                    for (int i = 0; i < index; i++) {
                        del = del.next;
                    }
                    del.next.prev = del.prev;
                    del.prev.next = del.next;
                    this.size--;
                    return del.data;
                }
            }
        }
    }

    @Override
    public int indexOf(T item) {
        int index = 0;
        Node<T> node = this.front;
        while (!node.data.equals(item)) {
            node = node.next;
            index++;
            if (index == this.size) {
                return -1;
            }
            if (node.data == null && item == null) {
                return index;
            }
        }
        return index;
    }

    public int size() {
        return this.size;
    }

    public boolean contains(T other) {
        return !(this.indexOf(other) == -1);
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at; returns 'false'
         * otherwise.
         */
        public boolean hasNext() {
            return !(this.current == null);
        }

        /**
         * Returns the next item in the iteration and internally updates the iterator to
         * advance one element forward.
         *
         * @throws NoSuchElementException
         *             if we have reached the end of the iteration and there are no more
         *             elements to look at.
         */
        public T next() {
            if (this.hasNext()) {
                Node<T> node = this.current;
                this.current = this.current.next;
                return node.data;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
