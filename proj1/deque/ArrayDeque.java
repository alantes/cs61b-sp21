package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T []) new Object[8];
        nextFirst = 4; // addLast 指向和 addFirst 不同位置的好处：addFirst 之后不用改变 nextLast 的值
        nextLast = 5;
        size = 0;
    }

    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1) % items.length;
        size += 1;
    }

    public void addLast(T item) {
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size += 1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int index = (nextFirst + 1) % items.length;
        while ((index % items.length) != nextLast) {
            System.out.print(items[index] + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        T firstItem = items[nextFirst + 1];
        items[nextFirst + 1] = null;
        nextFirst = (nextFirst + 1) % items.length;
        size -= 1;
        return firstItem;
    }

    public T removeLast() {
        T lastItem = items[nextLast - 1];
        items[nextLast - 1] = null;
        nextLast = (nextLast - 1) % items.length;
        size -= 1;
        return lastItem;
    }

    public T get(int index) {
        int rangeOfItems = (nextLast - nextFirst - 1) % items.length;
        if (index < rangeOfItems) {
            return items[(index + nextFirst + 1) % items.length];
        }
        return null;
    }
}
