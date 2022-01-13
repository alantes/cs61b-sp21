package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void randomizedTest() {
        ArrayDeque<Integer> tempArrayDeque = new ArrayDeque<>();
        LinkedListDeque<Integer> tempLinkedDeque = new LinkedListDeque<>();
        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                tempArrayDeque.addLast(randVal);
                tempLinkedDeque.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size1 = tempArrayDeque.size();
                int size2 = tempLinkedDeque.size();
                assertEquals(size1, size2);
            } else if (operationNumber == 2) {
                // get random item
                int lastL = 0;
                int lastBuggy = 0;
                int randVal = 0;
                if (tempArrayDeque.size() != 0) {
                    randVal = StdRandom.uniform(0, tempArrayDeque.size());
                }

                if (tempArrayDeque.size() > 0) {
                    lastL = tempArrayDeque.get(randVal);
                }
                if (tempLinkedDeque.size() > 0) {
                    lastBuggy = tempLinkedDeque.get(randVal);
                }
                assertEquals(lastL, lastBuggy);
            } else if (operationNumber == 3) {
                // removeLast
                int lastL = 0;
                int lastBuggy = 0;
                if (tempArrayDeque.size() > 0) {
                    lastL = tempArrayDeque.removeLast();
                }
                if (tempLinkedDeque.size() > 0) {
                    lastBuggy = tempLinkedDeque.removeLast();
                }
                assertEquals(lastL, lastBuggy);
            }
        }
    }
}
