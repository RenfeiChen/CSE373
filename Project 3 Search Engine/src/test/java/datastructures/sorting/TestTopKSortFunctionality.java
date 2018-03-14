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
	
	protected IList<Integer> makeBasicList() {
		IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        return list;
	}
	
	@Test(timeout=SECOND)
	public void testErrorHandling() {
		IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        try {
        	IList<Integer> top = Searcher.topKSort(-1, list);
    		fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
	}


	
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
    public void testWhenKEqualsSize() {
    	IList<Integer> list = makeBasicList();
    	
    	IList<Integer> top = Searcher.topKSort(20, list);
    	assertEquals(20, top.size());
    	for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testWhenKIsLargerThanSize() {
    	IList<Integer> list = makeBasicList();
    	
    	IList<Integer> top = Searcher.topKSort(25, list);
    	assertEquals(20, top.size());
    	for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testWhenStringInTheList() {
    	IList<String> list = new DoubleLinkedList<>();
    	list.add("a");
    	list.add("y");
    	list.add("x");
    	list.add("b");
    	list.add("c");
    	list.add("z");
    	IList<String> top = Searcher.topKSort(4, list);
    	assertEquals(4, top.size());
    	String[] array = new String[] {"c", "x", "y", "z"};
    	for (int i = 0; i < top.size(); i++) {
            assertEquals(array[i], top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testWhenDoubleAndNegativeInTheList() {
    	IList<Double> list = new DoubleLinkedList<>();
    	list.add(-0.48);
    	list.add(-45.0);
    	list.add(-0.10);
    	list.add(3.45);
    	list.add(3.14);
    	list.add(6.10);
    	IList<Double> top = Searcher.topKSort(10, list);
    	assertEquals(6, top.size());
    	Double[] array = new Double[] {-45.0, -0.48, -0.10, 3.14, 3.45, 6.10};
    	for (int i = 0; i < top.size(); i++) {
            assertEquals(array[i], top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testEmptyList() {
    	IList<Double> list = new DoubleLinkedList<>();
    	list.add(-0.48);
    	list.add(-45.0);
    	list.add(-0.10);
    	IList<Double> top = Searcher.topKSort(0, list);
    	assertEquals(0, top.size());
    	list.remove();
    	list.remove();
    	list.remove();
    	top = Searcher.topKSort(10, list);
    	assertEquals(0, top.size());
    	assertEquals(true, top.isEmpty());
    }
}
