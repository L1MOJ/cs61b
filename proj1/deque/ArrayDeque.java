package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextlast;
    private int nextfirst;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        setNext(4, 5);
    }

    private void setNext(int f, int l) {
        this.nextfirst = f;
        this.nextlast = l;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextfirst] = item;
        decNextFirst();
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextlast] = item;
        incNextLast();
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = getFirstIndex(); i <= getLastIndex(); i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (size * 2 < items.length) {
            resize(size + 5);
        }
        T x = getFirst();
        int ind = getFirstIndex();
        items[ind] = null;
        incNextFirst();
        size -= 1;
        return x;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (size * 2 < items.length) {
            resize(size + 5);
        }
        T x = getLast();
        int ind = getLastIndex();
        items[ind] = null;
        decNextLast();
        size -= 1;
        return x;
    }

    private T getFirst() {
        return items[getFirstIndex()];
    }

    private int getFirstIndex() {
        return (nextfirst + 1) % items.length;
    }

    private T getLast() {
        return items[getLastIndex()];
    }

    private int getLastIndex() {
        return (nextlast - 1 + items.length) % items.length;
    }

    @Override
    public T get(int index) {
        return items[(nextfirst + 1 + index) % items.length];
    }

    private void incNextFirst() {
        nextfirst = (nextfirst + 1) % items.length;
    }

    private void decNextFirst() {
        nextfirst = (nextfirst - 1 + items.length) % items.length;
    }

    private void incNextLast() {
        nextlast = (nextlast + 1) % items.length;
    }

    private void decNextLast() {
        nextlast = (nextlast - 1 + items.length) % items.length;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int first = getFirstIndex();
        int last = getLastIndex();
        if (first < last) {
            System.arraycopy(items, first, a, 0, size);
        } else {
            System.arraycopy(items, first, a, 0, size - first);
            System.arraycopy(items, 0, a, size - first, last + 1);
        }
        items = a;
        setNext(items.length - 1, size);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDqIterator();
    }

    private class ArrayDqIterator implements Iterator<T> {

        private int wizPos;

        ArrayDqIterator() {
            wizPos = 0;
        }
        @Override
        public boolean hasNext() {
            return wizPos < size;
        }

        @Override
        public T next() {
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }
    }

    @Override
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
        Deque<T> oher = (Deque<T>) o;
        if (size() != oher.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            T item1 = get(i);
            T item2 = oher.get(i);
            if (!item1.equals(item2)) {
                return false;
            }
        }
        return true;
    }
}
