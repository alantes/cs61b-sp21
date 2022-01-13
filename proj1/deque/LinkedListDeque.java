package deque;

import net.sf.saxon.om.Item;

public class LinkedListDeque<T> { // 在外层定义 generics 即可
    private ItemNode sentinel;
    private int size;

    public class ItemNode { // 因为用到了外层的 generics, 所以不能是 static inner class
        public ItemNode prev;
        public T item;
        public ItemNode next;

        public ItemNode(ItemNode prev, T item, ItemNode next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    public LinkedListDeque() {
        sentinel = new ItemNode(null, null, null); // 因为不知道会输入什么类型，所以只能给 item 赋值为 null
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        ItemNode nextNode = new ItemNode(sentinel, item, sentinel.next);
        sentinel.next.prev = nextNode;
        sentinel.next = nextNode;
        size += 1;
    }

    public void addLast(T item) {
        ItemNode lastNode = new ItemNode(sentinel.prev, item, sentinel);
        sentinel.prev.next = lastNode;
        sentinel.prev = lastNode;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        ItemNode p = sentinel.next;
        while (p.next != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (!isEmpty()) {
            ItemNode firstItem = sentinel.next;
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
            ItemNode lastItem = sentinel.prev;
            sentinel.prev.prev.next = sentinel;
            sentinel.prev = sentinel.prev.prev;
            size -= 1;
            return lastItem.item;
        } else {
            return null;
        }
    }

    public T get(int index) {
        ItemNode p = sentinel.next;
        while (p != sentinel) {
            if (index == 0) {
                return p.item;
            }
            index -= 1;
        }
        return null;
    }

    private T getRecursive(int index, ItemNode node) {
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
