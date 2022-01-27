package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private ItemNode<T> sentinel;
    private int size;

    private static class ItemNode<T> {
        private ItemNode<T> prev;
        private T item;
        private ItemNode<T> next;

        ItemNode(ItemNode<T> prev, T item, ItemNode<T> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;

        if (this.size() != other.size()) {
            return false;
        }

        for (int i = 0; i < size(); i += 1) {
            if (!(this.get(i).equals(other.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new LinkedListDeque.LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int wizPos;

        LinkedListDequeIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public T next() {
            wizPos += 1;
            return get(wizPos - 1);
        }

    }

    public LinkedListDeque() {
        sentinel = new ItemNode<>(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        ItemNode<T> nextNode = new ItemNode<>(sentinel, item, sentinel.next);
        sentinel.next.prev = nextNode;
        sentinel.next = nextNode;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        ItemNode<T> lastNode = new ItemNode<>(sentinel.prev, item, sentinel);
        sentinel.prev.next = lastNode;
        sentinel.prev = lastNode;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        ItemNode<T> p = sentinel.next;
        while (p.next != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (!isEmpty()) {
            ItemNode<T> firstItem = sentinel.next;
            sentinel.next.next.prev = sentinel;
            sentinel.next = sentinel.next.next;
            size -= 1;
            return firstItem.item;
        } else {
            return null;
        }
    }

    @Override
    public T removeLast() {
        if (!isEmpty()) {
            ItemNode<T> lastItem = sentinel.prev;
            sentinel.prev.prev.next = sentinel;
            sentinel.prev = sentinel.prev.prev;
            size -= 1;
            return lastItem.item;
        } else {
            return null;
        }
    }

    @Override
    public T get(int index) {
        ItemNode<T> p = sentinel.next;
        while (p != sentinel) {
            if (index == 0) {
                return p.item;
            }
            p = p.next;
            index -= 1;
        }
        return null;
    }

    private T getRecursive(int index, ItemNode<T> node) {
        if (index == 0) {
            return node.item;
        } else {
            return getRecursive(index - 1, node.next);
        }
    }

    public T getRecursive(int index) {
        if (index >= size() || size() == 0) {
            return null;
        }
        return getRecursive(index, sentinel.next);
    }
}
