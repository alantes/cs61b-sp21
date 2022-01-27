package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> myComparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        myComparator = c;
    }

    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        T maxItem = get(0);
        for (int i = 0; i < size(); i += 1) {
            T currentItem = get(i);
            if (c.compare(currentItem, maxItem) > 0) {
                maxItem = currentItem;
            }
        }
        return maxItem;
    }

    public T max() {
        return max(myComparator);
    }

    /*
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        MaxArrayDeque<T> o = (MaxArrayDeque<T>) other;
        if (this.size() != o.size()) {
            return false;
        } else {
            for (int i = 0; i < size(); i += 1) {
                if (this.get(i) != o.get(i)) {
                    return false;
                }
            }
            return true;
        }
    } */
}
