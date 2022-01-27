package deque;

public class ArrayDeque<T> implements Deque<T>{
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T []) new Object[8];
        nextFirst = 0; // addLast 指向和 addFirst 不同位置的好处：addFirst 之后不用改变 nextLast 的值
        nextLast = 1;
        size = 0;
    }

    private void resize(int newRange) {
        T[] tempItems = (T []) new Object[newRange];
        int index = regularizeIndexes(nextFirst + 1);
        int tempIndex = 1;
        while (index != nextLast) {
            tempItems[tempIndex] = items[index];
            tempIndex += 1;
            index = regularizeIndexes(index + 1);
        }
        items = tempItems;
        nextFirst = 0;
        nextLast = nextFirst + size + 1;
    }

    private void checkUsage() {
        float floatSize = size;
        if (items.length >= 16 && floatSize / items.length <= 0.25) {
            resize(items.length / 2);
        }
         else if (size == items.length - 1) {
             resize(items.length * 2);
         }
    }

    private int calculateMod(int a, int b) {
        return ((a % b) + b) % b;
    }

    private int regularizeIndexes(int x) {
        return calculateMod(x, items.length);
    }

    @Override
    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst = regularizeIndexes(nextFirst - 1);
        size += 1;
        checkUsage();
    }
    @Override
    public void addLast(T item) {
        items[nextLast] = item;
        nextLast = regularizeIndexes(nextLast + 1);
        size += 1;
        checkUsage();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int index = regularizeIndexes(nextFirst + 1);
        while (index != nextLast) {
            System.out.print(items[index] + " ");
            index = regularizeIndexes(index + 1);
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (!isEmpty()) {
            T firstItem = items[regularizeIndexes(nextFirst + 1)];
            items[regularizeIndexes(nextFirst + 1)] = null;
            nextFirst = regularizeIndexes(nextFirst + 1);
            size -= 1;
            checkUsage();
            return firstItem;
        } else {
            return null;
        }

    }

    @Override
    public T removeLast() {
        if (!isEmpty()) {
            T lastItem = items[regularizeIndexes(nextLast - 1)];
            items[regularizeIndexes(nextLast - 1)] = null;
            nextLast = regularizeIndexes(nextLast - 1);
            size -= 1;
            checkUsage();
            return lastItem;
        } else {
            return null;
        }
    }

    @Override
    public T get(int index) {
        int rangeOfItems = regularizeIndexes(nextLast - nextFirst - 1);
        if (index < rangeOfItems) {
            return items[regularizeIndexes(index + nextFirst + 1)];
        }
        return null;
    }
}
