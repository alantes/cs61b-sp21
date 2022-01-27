package deque;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Comparator;

public class MaxArrayDequeTest {
    private static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public static Comparator<Integer> getIntegerComparator() {
        return new IntegerComparator();
    }


    @Test
    public void testMax() {
        MaxArrayDeque<Integer> a = new MaxArrayDeque(MaxArrayDequeTest.getIntegerComparator());
        MaxArrayDeque<Integer> b = new MaxArrayDeque(MaxArrayDequeTest.getIntegerComparator());
        Integer maxInteger = 499;
        for (int i = 0; i <= maxInteger; i += 1) {
            a.addLast(i);
            b.addFirst(i);
        }
        assertEquals(a.max(), maxInteger);
        assertEquals(b.max(), maxInteger);
    }
}
