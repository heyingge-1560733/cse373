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
    private int heapSize;
    
    // Feel free to add more fields and constants.
    
    public ArrayHeap() {
        this.heap = makeArrayOfT(1);
        this.heapSize = 0;
    }
    
    /**
    * This method will return a new, empty array of the given size
    * that can contain elements of type T.
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
        if (this.size() == 0) {
            throw new EmptyContainerException();
        }
        this.heapSize--;
        T min = heap[0];
        heap[0] = heap[heapSize];
        heap[0] = removeMinHelper(1, heap[0]);
        return min;
    }
    
    private T removeMinHelper(int index, T max) {
        if (index < this.size()) {
            T min = heap[index];
            int minIndex = index;
            for (int i = 1; i < NUM_CHILDREN; i++) {
                if (index + i < this.heapSize) {
                    if (min.compareTo(heap[index + i]) > 0) {
                        min = heap[index + i];
                        minIndex = index + i;
                    }
                } else {
                    break;
                }
            }
            if (min.compareTo(max) < 0) {
                this.heap[minIndex] = removeMinHelper(minIndex * NUM_CHILDREN + 1, max);
                return min;
            } else {
                return max;
            }
        } else {
            return max;
        }
    }
    
    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        resize();
        this.heap[heapSize] = item;
        this.heapSize++;
        heap[this.size() - 1] = insertHelper((this.size() - 2) / NUM_CHILDREN, item);
    }
    
    private T insertHelper(int index, T child) {
        T curr = heap[index];
        if (curr.compareTo(child) > 0) {
            if (index > 0) {
                heap[index] = insertHelper((index - 1) / NUM_CHILDREN, child);
            } else {
                heap[0] = child;
            }
            return curr;
        } else {
            return child;
        }
    }
    
    @Override
    public int size() {
        return this.heapSize;
    }
    
    @Override
    public T peekMin() {
        if (this.size() == 0) {
            throw new EmptyContainerException();
        }
        return this.heap[0];
    }
    
    private void resize() {
        if (this.size() + 1 > this.heap.length) {
            int newLength = this.heap.length * NUM_CHILDREN + 1;
            T[] newHeap = makeArrayOfT(newLength);
            for (int i = 0; i < this.size(); i++) {
                newHeap[i] = this.heap[i];
            }
            this.heap = newHeap;
        }
    }
    
    public String toString() {
        String result = "Heap: ";
        for (int i = 0; i < this.size(); i++) {
            result += heap[i] + " ";
        }
        return result;
    }
}
