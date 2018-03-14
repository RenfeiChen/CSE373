/*

Author : Renfei Chen
         HanTao Liu

*/
package misc;

import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import java.util.Iterator;

import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.ArrayHeap;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "biggest".
     *
     * If the input list contains fewer then 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        // Implementation notes:
        //
        // - This static method is a _generic method_. A generic method is similar to
        //   the generic methods we covered in class, except that the generic parameter
        //   is used only within this method.
        //
        //   You can implement a generic method in basically the same way you implement
        //   generic classes: just use the 'T' generic type as if it were a regular type.
        //
        // - You should implement this method by using your ArrayHeap for the sake of
        //   efficiency.
        if (k < 0) {
            throw new IllegalArgumentException();
        }
        IList<T> topK = new DoubleLinkedList<>();
        IPriorityQueue<T> topKheap = new ArrayHeap<>();
        Iterator<T> iter = input.iterator();
        int inputsize = input.size();
        if (k == 0) {
            return topK;
        }
        if (inputsize <= k) {
            for (int i = 0; i < inputsize; i++) {
                topKheap.insert(iter.next());
            }
        } else {
            for (int i = 0; i < k; i++) {
                topKheap.insert(iter.next());
            }
            for (int i = k; i < inputsize; i++) {
                T now = iter.next();
                if (now.compareTo(topKheap.peekMin()) > 0) {
                    topKheap.removeMin();
                    topKheap.insert(now);
                }
            }
        }
        
        while (topKheap.size() != 0) {
            topK.add(topKheap.removeMin());
        }
        return topK;       
    }
}
