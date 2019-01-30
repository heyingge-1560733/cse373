package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

/**
* See spec for details on what kinds of tests this class should include.
*/
public class TestSortingStress extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=10*SECOND)
    public void testInsertTiming() {
        int testNum = 1000000;
        IPriorityQueue<Integer> heap = new ArrayHeap<Integer>();
        for (int i = 0; i < testNum; i++) {
            heap.insert(i);
        }
        assertEquals(heap.size(), testNum);
    }
    
    @Test(timeout=10*SECOND)
    public void testRemoveMinTiming() {
        int testNum = 1000000;
        IPriorityQueue<Integer> heap = new ArrayHeap<Integer>();
        for (int i = 0; i < testNum; i++) {
            heap.insert(i);
        }
        assertEquals(heap.size(), testNum);
        for (int i = 0; i < testNum; i++) {
            assertEquals(heap.removeMin(), i);
        }
        assertEquals(heap.size(), 0);
    }
    
    @Test(timeout=10*SECOND)
    public void testRandomInsertion() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Random rand = new Random();
        int[] counter = new int[10000];
        for (int i = 0; i < 1000000; i++) {
            int element = rand.nextInt(10000);
            counter[element]++;
            heap.insert(element);
            assertEquals(heap.size(), i + 1);
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
        assertEquals(heap.size(), 0);
    }
    
    @Test(timeout=10*SECOND)
    public void testSortTiming() {
        int testNum = 1000000;
        List<Integer> arrayList = new ArrayList<Integer>();
        IList<Integer> doubleLinkedList = new DoubleLinkedList<Integer>();
        Random rand = new Random();
        for (int i = 0; i < testNum; i++) {
            int temp = rand.nextInt(1000);
            arrayList.add(temp);
            doubleLinkedList.add(temp);
        }
        IList<Integer> top = Searcher.topKSort(testNum, doubleLinkedList);
        Collections.sort(arrayList);
        Iterator<Integer> aryListItr = arrayList.iterator();
        Iterator<Integer> dblkListItr = top.iterator();
        for (int i = 0; i < testNum; i++) {
            int expected = aryListItr.next();
            int actual = dblkListItr.next();
            assertEquals(expected, actual);
        }
    }
    
    @Test(timeout=10*SECOND)
    public void testReverseOrderSortTiming() {
        int testNum = 1000000;
        List<Integer> arrayList = new ArrayList<Integer>();
        IList<Integer> doubleLinkedList = new DoubleLinkedList<Integer>();
        for (int i = 0; i < testNum; i++) {
            int temp = testNum - i;
            arrayList.add(temp);
            doubleLinkedList.add(temp);
        }
        IList<Integer> top = Searcher.topKSort(testNum, doubleLinkedList);
        Collections.sort(arrayList);
        Iterator<Integer> lkListItr = arrayList.iterator();
        Iterator<Integer> dblkListItr = top.iterator();
        for (int i = 0; i < testNum; i++) {
            assertEquals(lkListItr.next(), dblkListItr.next());
        }
    }
    
    @Test(timeout=10*SECOND)
    public void testOrderedSortTiming() {
        int testNum = 1000000;
        List<Integer> arrayList = new ArrayList<Integer>();
        IList<Integer> doubleLinkedList = new DoubleLinkedList<Integer>();
        for (int i = 0; i < testNum; i++) {
            int temp = i;
            arrayList.add(temp);
            doubleLinkedList.add(temp);
        }
        IList<Integer> top = Searcher.topKSort(testNum, doubleLinkedList);
        Collections.sort(arrayList);
        Iterator<Integer> lkListItr = arrayList.iterator();
        Iterator<Integer> dblkListItr = top.iterator();
        for (int i = 0; i < testNum; i++) {
            assertEquals(lkListItr.next(), dblkListItr.next());
        }
    }
}