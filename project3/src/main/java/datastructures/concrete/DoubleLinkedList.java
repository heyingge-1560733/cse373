package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* Note: For more info on the expected behavior of your methods, see
* the source code for IList.
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
    
    @Override
    public void add(T item) {
        if (this.back == null) {
            Node<T> newNode = new Node<T>(item);
            this.front = newNode;
            this.back = newNode;
        } else {
            Node<T> newNode = new Node<T>(this.back, item, null);
            this.back.next = newNode;
            this.back = this.back.next;
        }
        this.size++;
    }
    
    @Override
    public T remove() {
        if (this.size() == 0) {
            throw new EmptyContainerException();
        }
        Node<T> curr = this.back;
        if (this.size() == 1) {
            this.front = null;
            this.back = null;
        } else {
            this.back = curr.prev;
            curr.prev = null;
            this.back.next = null;
        }
        this.size--;
        return curr.data;
    }
    
    @Override
    public T get(int index) {
        if (index < 0 || index > this.size() - 1) {
            throw new IndexOutOfBoundsException("Index is out of bounds.");
        }
        Node<T> curr = this.front;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.data;
    }
    
    @Override
    public void set(int index, T item) {
        if (index < 0 || index > this.size() - 1) {
            throw new IndexOutOfBoundsException("Index is out of bounds.");
        }
        Node<T> curr = this.front;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        if (index == 0) {
            if (this.size() == 1) {
                this.front = new Node<T>(item);
                this.back = this.front;
            } else {
                this.front = new Node<T>(null, item, curr.next);
                curr.next.prev = this.front;
            }
        } else if (index == this.size() - 1) {
            curr.prev.next = new Node<T>(curr.prev, item, null);
            this.back = curr.prev.next;
        } else {
            curr.prev.next = new Node<T>(curr.prev, item, curr.next);
            curr.next.prev = curr.prev.next;
        }
    }
    
    @Override
    public void insert(int index, T item) {
        if (index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException("Index is out of bounds.");
        }
        if (index == this.size()) {
            this.add(item);
        } else if (index == 0) {
            this.front = new Node<T>(null, item, this.front);
            this.front.next.prev = this.front;
            this.size++;
        } else {
            Node<T> curr;
            if (this.size() - index - 1 > index) {
                curr = this.front;
                for (int i = 0; i < index; i++) {
                    curr = curr.next;
                }
            } else {
                curr = this.back;
                for (int i = 0; i < this.size() - index; i++) {
                    curr = curr.prev;
                }
            }
            curr.next = new Node<T>(curr, item, curr.next);
            curr.next.next.prev = curr.next;
            this.size++;
        }
    }
    
    @Override
    public T delete(int index) {
        if (index < 0 || index > this.size() - 1) {
            throw new IndexOutOfBoundsException();
        } else if (index == this.size() - 1) {
            return this.remove();
        } else if (index == 0) {
            Node<T> temp = this.front;
            this.front = this.front.next;
            this.size--;
            return temp.data;
        } else {
            Node<T> curr = this.front;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;
            this.size--;
            return curr.data;
        }
    }
    
    @Override
    public int indexOf(T item) {
        int index = 0;
        Node<T> curr = this.front;
        while (curr != null) {
            if (item == null && curr.data == null || curr.data.equals(item)) {
                return index;
            }
            curr = curr.next;
            index++;
        }
        return -1;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean contains(T other) {
        Node<T> curr = this.front;
        while (curr != null) {
            if (other == null && curr.data == null || curr.data.equals(other)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
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
        * Returns 'true' if the iterator still has elements to look at;
        * returns 'false' otherwise.
        */
        public boolean hasNext() {
            return this.current != null;
        }
        
        /**
        * Returns the next item in the iteration and internally updates the
        * iterator to advance one element forward.
        *
        * @throws NoSuchElementException if we have reached the end of the iteration and
        *         there are no more elements to look at.
        */
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("There is no next element.");
            }
            T temp = this.current.data;
            this.current = this.current.next;
            return temp;
        }
        
        public String toString() {
            return current.data.toString();
        }
    }
}