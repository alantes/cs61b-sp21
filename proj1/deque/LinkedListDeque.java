package deque;

public class LinkedListDeque<T> implements Deque<T>{
    private ItemNode<T> sentinel;
    private int size;

    public static class ItemNode<T> {
        public ItemNode<T> prev;
        public T item;
        public ItemNode<T> next;

        public ItemNode(ItemNode<T> prev, T item, ItemNode<T> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    public LinkedListDeque() {
        sentinel = new ItemNode<>(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        ItemNode<T> nextNode = new ItemNode<>(sentinel, item, sentinel.next);
        sentinel.next.prev = nextNode;
        sentinel.next = nextNode;
        size += 1;
    }

    public void addLast(T item) {
        ItemNode<T> lastNode = new ItemNode<>(sentinel.prev, item, sentinel);
        sentinel.prev.next = lastNode;
        sentinel.prev = lastNode;
        size += 1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        ItemNode<T> p = sentinel.next;
        while (p.next != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

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
        if(sentinel.next != sentinel) {
            return getRecursive(index, sentinel);
        } else {
            return null;
        }
    }
}
