package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> tempAList = new AListNoResizing<>();
        BuggyAList<Integer> tempBuggy = new BuggyAList<>();
        tempAList.addLast(4);
        tempAList.addLast(5);
        tempAList.addLast(6);

        tempBuggy.addLast(4);
        tempBuggy.addLast(5);
        tempBuggy.addLast(6);

        assertEquals(tempAList.removeLast(), tempBuggy.removeLast());
        assertEquals(tempAList.removeLast(), tempBuggy.removeLast());
        assertEquals(tempAList.removeLast(), tempBuggy.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> tempBuggy = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                tempBuggy.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size1 = L.size();
                int size2 = tempBuggy.size();
                assertEquals(size1, size2);
            } else if (operationNumber == 2) {
                // getLast
                int lastL = 0;
                int lastBuggy = 0;
                if (L.size() > 0) {
                    lastL = L.getLast();
                }
                if (tempBuggy.size() > 0) {
                    lastBuggy = tempBuggy.getLast();
                }
                assertEquals(lastL, lastBuggy);
            } else if (operationNumber == 3) {
                // removeLast
                int lastL = 0;
                int lastBuggy = 0;
                if (L.size() > 0) {
                    lastL = L.removeLast();
                }
                if (tempBuggy.size() > 0) {
                    lastBuggy = tempBuggy.removeLast();
                }
                assertEquals(lastL, lastBuggy);
            }
        }
    }
}
