package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;
import org.junit.Test;
import static org.junit.Assert.fail;

/**
* See spec for details on what kinds of tests this class should include.
*/
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testIllegalInputs() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
        
    }
    
    @Test(timeout=SECOND)
    public void testEmptyInputList() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND)
    public void testKGreaterThanEqualsInputSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(10, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testKEqualsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(0, list);
        assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND)
    public void testStringInput() {
        IList<String> list = new DoubleLinkedList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");
        list.add("h");
        list.add("i");
        list.add("j");
        IList<String> top = Searcher.topKSort(3, list);
        assertEquals(3, top.size());
        assertEquals("h", top.get(0));
        assertEquals("i", top.get(1));
        assertEquals("j", top.get(2));
    }
    
    @Test(timeout=SECOND)
    public void testTrickyBug() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 3; j++) {
                list.add(0);
            }
            for (int j = 0; j < 5; j++) {
            	list.add(-1);
            }
            for (int j = 0; j < 7; j++) {
            	list.add(1);
            }
        }
        IList<Integer> top = Searcher.topKSort(150, list);
        for (int i = 0; i < 50; i++) {
            assertEquals(top.get(i), -1);
        }
        for (int i = 0; i < 30; i++) {
            assertEquals(top.get(i + 50), 0);
        }
        for (int i = 0; i < 70; i++) {
            assertEquals(top.get(i + 80), 1);
        }
    }
}
