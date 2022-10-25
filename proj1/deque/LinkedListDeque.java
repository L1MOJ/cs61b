package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private Node sentinel;
    private int size;



    public class Node {
        public T item;
        public Node prev;
        public Node next;
        public Node(T i , Node p,Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    public  LinkedListDeque() {
        sentinel = new Node(null,null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;

    }

    public  LinkedListDeque(LinkedListDeque other) {
        sentinel = new Node(null,null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
        Node nextNode = other.sentinel.next;
        while (this.size < other.size){
            this.addLast(nextNode.item);
            nextNode = nextNode.next;
        }
    }

    public void LinkedListDequeHelper(Node origin, Node provide,int size, int cnt) {
        if (size == cnt) {
            origin.next = sentinel;
            sentinel.prev = origin;
            origin.item = provide.item;
        }
    }
    @Override
    public void addFirst(T item) {
        Node prevFisrt = sentinel.next;
        Node newFirst = new Node(item,sentinel,prevFisrt);
        prevFisrt.prev = newFirst;
        sentinel.next = newFirst;
        size += 1;
    }
    @Override
    //Adds an item of type T to the back of the deque.
    public void addLast(T item) {
        Node prevLast = sentinel.prev;
        Node newLast = new Node(item,prevLast,sentinel);
        prevLast.next = newLast;
        sentinel.prev = newLast;
        size += 1;
    }
    @Override
    public int size(){
        return size;
    }

    @Override
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item+" ");
            p = p.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if(sentinel.next == sentinel) {
            return null;
        }
        Node prevFirst = sentinel.next;
        Node newFirst = prevFirst.next;
        sentinel.next = newFirst;
        newFirst.prev = sentinel;
        size -= 1;
        return  prevFirst.item;
    }

    @Override
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }
        Node prevLast = sentinel.prev;
        Node newLast = prevLast.prev;
        sentinel.prev = newLast;
        newLast.next = sentinel;
        size -= 1;
        return prevLast.item;
    }

    @Override
    public T get(int index) {
        Node p = sentinel.next;
        if (index >= size) {
            return null;
        }
        while (index > 0) {
            p = p.next;
            index--;
        }
        return p.item;
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(index, sentinel.next);
    }

    public T getRecursiveHelper(int index, Node p) {
        if (p == sentinel) {
            return null;
        }
        if (index == 0) {
            return p.item;
        }
        return getRecursiveHelper(index-1,p.next);
    }
    @Override
    public Iterator<T> iterator() {
        return new LLDqIterator();
    }

    private class LLDqIterator implements Iterator<T> {

        private Node wizPos;

        public LLDqIterator() {
            wizPos = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return wizPos != sentinel;
        }

        @Override
        public T next() {
            T returnItem = wizPos.item;
            wizPos = wizPos.next;
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null){
            return false;
        }
        if (!(o instanceof Deque)){
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            T items1 = this.get(i);
            T items2 = other.get(i);
            if (!items1.equals(items2)) {
                return false;
            }
        }
        return true;
    }
}
