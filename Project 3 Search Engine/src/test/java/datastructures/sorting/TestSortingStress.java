package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;


/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout=10*SECOND)
    public void testPlaceholder() {
    	IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 80000; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(80000, list);
        assertEquals(80000, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
        
        for (int i = top.size() - 1; i >=0; i--) {
            assertEquals(list.remove(), top.get(i));
        }
    }
}
