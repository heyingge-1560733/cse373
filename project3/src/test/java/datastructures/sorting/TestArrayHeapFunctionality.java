package datastructures.sorting;

import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;
import java.util.Random;

/**
* See spec for details on what kinds of tests this class should include.
*/
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 100; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(i, heap.peekMin());
            assertEquals(100 - i, heap.size());
            assertEquals(heap.removeMin(), i);
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinErrorHandle() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinNegative() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = -20; i < 0; i++) {
            heap.insert(i);
        }
        for (int i = -20; i < 0; i++) {
            assertEquals(i, heap.peekMin());
            assertEquals(0 - i, heap.size());
            heap.removeMin();
        }
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 100; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(i, heap.peekMin());
            assertEquals(100 - i, heap.size());
            heap.removeMin();
        }
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinErrorHandle() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i > -100; i--) {
            heap.insert(i);
            assertEquals(i, heap.peekMin());
            assertEquals(-i + 1, heap.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertErrorHandle() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentEception");
        } catch (IllegalArgumentException ex) {
            // do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testRandomInsertion() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Random rand = new Random();
        int[] counter = new int[10000];
        for (int i = 0; i < 100; i++) {
            int element = rand.nextInt(10000);
            counter[element]++;
            heap.insert(element);
        }
        int min = 0;
        while (counter[min] == 0) {
            min++;
        }
        while (!heap.isEmpty()) {
            assertEquals(heap.removeMin(), min);
            counter[min]--;
            while (min < 10000 && counter[min] == 0) {
                min++;
            }
        }
    }
    
    @Test(timeout=SECOND)
    public void testTrickyBug() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 3; j++) {
                heap.insert(0);
            }
            for (int j = 0; j < 5; j++) {
                heap.insert(-1);
            }
            for (int j = 0; j < 7; j++) {
                heap.insert(1);
            }
        }
        for (int i = 0; i < 50; i++) {
            assertEquals(heap.removeMin(), -1);
        }
        for (int i = 0; i < 30; i++) {
            assertEquals(heap.removeMin(), 0);
        }
        for (int i = 0; i < 70; i++) {
            assertEquals(heap.removeMin(), 1);
        }
    }
}