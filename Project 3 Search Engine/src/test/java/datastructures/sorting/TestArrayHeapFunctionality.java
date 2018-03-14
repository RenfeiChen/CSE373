package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    protected IPriorityQueue<Integer> makeBasicHeap() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(5);
    	heap.insert(2);
    	heap.insert(3);
    	heap.insert(4);
    	heap.insert(1);
    	return heap;
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testInsertBasic() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	heap.insert(0);
    	assertEquals(heap.peekMin(), 0);
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMin() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	assertEquals(heap.removeMin(), 1);
    	assertEquals(heap.removeMin(), 2);
    	assertEquals(heap.removeMin(), 3);
    	assertEquals(heap.removeMin(), 4);
    	assertEquals(heap.removeMin(), 5);
    }
    
    @Test(timeout=SECOND)
    public void testReverseInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 20; i++) {
            heap.insert(i);
            assertEquals(heap.peekMin(), 0);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals(heap.removeMin(), i);
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndRemoveMultiple() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	heap.insert(8);
    	heap.insert(6);
    	heap.insert(11);
    	heap.insert(0);
    	int[] array = new int[] {0, 1, 2, 3, 4, 5, 6, 8, 11};
    	for (int i = 0; i < heap.size(); i++) {
    		assertEquals(heap.removeMin(), array[i]);
    	}
    }
    
    @Test(timeout=SECOND)
    public void testPeekMultiple() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	heap.insert(8);
    	heap.insert(6);
    	heap.insert(11);
    	heap.insert(0);
    	int[] array = new int[] {0, 1, 2, 3, 4, 5, 6, 8, 11};
    	for (int i = 0; i < heap.size(); i++) {
    		assertEquals(heap.peekMin(), array[i]);
    		heap.removeMin();
    	}
    }
    
    @Test(timeout=SECOND)
    public void testInsertRemoveWithSize() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	heap.removeMin();
    	heap.insert(7);
    	heap.removeMin();
    	heap.insert(10000);
    	heap.removeMin();
    	heap.insert(2);
    	heap.insert(8000);
    	heap.insert(0);
    	int[] array = new int[] {0, 2, 4, 5, 7, 8000, 10000};
    	assertTrue(heap.size() == 7);
    	for (int i = 0; i < heap.size(); i++) {
    		assertEquals(heap.removeMin(), array[i]);
    	}
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndRemoveNegativeElement() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	heap.removeMin();
    	heap.insert(7);
    	heap.removeMin();
    	heap.insert(100);
    	heap.removeMin();
    	heap.insert(-9);
    	heap.insert(-30);
    	heap.insert(-4);
    	int[] array = new int[] {-30, -9, -4, 4, 5, 7, 100};
    	assertTrue(heap.size() == 7);
    	for (int i = 0; i < heap.size(); i++) {
    		assertEquals(heap.removeMin(), array[i]);
    	}
    }
    
    @Test(timeout=SECOND)
    public void testRemoveErrorHandling() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	assertEquals(heap.size(), 0);
    	try {
    		heap.removeMin();
    		fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testPeekErrorHandling() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	assertEquals(heap.size(), 0);
    	try {
    		heap.peekMin();
    		fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertErrorHandling() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	assertEquals(heap.size(), 0);
    	try {
    		heap.insert(null);
    		fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndRemoveString() {
    	IPriorityQueue<String> heap = this.makeInstance();
    	heap.insert("a");
    	heap.insert("b");
    	heap.insert("c");
    	heap.insert("g");
    	heap.insert("m");
    	heap.insert("n");
    	heap.removeMin();
    	String[] array = new String[] {"b", "c", "g", "m", "n"};
    	assertTrue(heap.size() == 5);
    	for (int i = 0; i < heap.size(); i++) {
    		assertEquals(heap.removeMin(), array[i]);
    	}
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndRemoveDouble() {
    	IPriorityQueue<Double> heap = this.makeInstance();
    	heap.insert(0.49);
    	heap.insert(0.55);
    	heap.insert(4.79);
    	heap.insert(7.65);
    	heap.removeMin();
    	heap.insert(100.87);
    	heap.insert(10000.49);
    	heap.insert(0.12);
    	heap.insert(0.14);
    	heap.removeMin();
    	Double[] array = new Double[] {0.14, 0.55, 4.79, 7.65, 100.87, 10000.49};
    	assertTrue(heap.size() == 6);
    	for (int i = 0; i < heap.size(); i++) {
    		assertEquals(heap.removeMin(), array[i]);
    	}
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndRemoveDoubleWithNegative() {
    	IPriorityQueue<Double> heap = this.makeInstance();
    	heap.insert(0.49);
    	heap.insert(0.55);
    	heap.insert(-4.79);
    	heap.insert(7.65);
    	heap.removeMin();
    	heap.insert(100.87);
    	heap.insert(-10000.49);
    	heap.insert(0.12);
    	heap.insert(-0.14);
    	heap.removeMin();
    	Double[] array = new Double[] {-0.14, 0.12, 0.49, 0.55, 7.65, 100.87};
    	assertTrue(heap.size() == 6);
    	for (int i = 0; i < heap.size(); i++) {
    		assertEquals(heap.removeMin(), array[i]);
    	}
    }
}
