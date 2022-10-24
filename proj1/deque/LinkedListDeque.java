package deque;

public class LinkedListDeque<T> implements Deque<T> {
    private Node sentinel;
    private int size;

    public class Node {
        public T item;
        public Node prev;
        public Node next;
        public Node(T i , Node p,Node n){
            item = i;
            prev = p;
            next = n;
        }
    }

    public  LinkedListDeque(){
        sentinel = new Node(null,null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;

    }

    public  LinkedListDeque(LinkedListDeque other){
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

    public void LinkedListDequeHelper(Node origin, Node provide,int size, int cnt){
        if (size == cnt){
            origin.next = sentinel;
            sentinel.prev = origin;
            origin.item = provide.item;
        }
    }

    public void addFirst(T item){
        Node prevFisrt = sentinel.next;
        Node newFirst = new Node(item,sentinel,prevFisrt);
        prevFisrt.prev = newFirst;
        sentinel.next = newFirst;
        size += 1;
    }

    //Adds an item of type T to the back of the deque.
    public void addLast(T item){
        Node prevLast = sentinel.prev;
        Node newLast = new Node(item,prevLast,sentinel);
        prevLast.next = newLast;
        sentinel.prev = newLast;
        size += 1;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        Node p = sentinel.next;
        while (p != sentinel){
            System.out.print(p.item+" ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst(){
        if(sentinel.next == sentinel){
            return null;
        }
        Node prevFirst = sentinel.next;
        Node newFirst = prevFirst.next;
        sentinel.next = newFirst;
        newFirst.prev = sentinel;
        size -= 1;
        return  prevFirst.item;
    }

    public T removeLast(){
        if (sentinel.prev == sentinel){
            return null;
        }
        Node prevLast = sentinel.prev;
        Node newLast = prevLast.prev;
        sentinel.prev = newLast;
        newLast.next = sentinel;
        size -= 1;
        return prevLast.item;
    }

    public T get(int index){
        Node p = sentinel.next;
        if (index >= size){
            return null;
        }
        while (index > 0){
            p = p.next;
            index--;
        }
        return p.item;
    }

    public T getRecursive(int index){
        return getRecursiveHelper(index, sentinel.next);
    }

    public T getRecursiveHelper(int index, Node p){
        if (p == sentinel){
            return null;
        }
        if (index == 0){
            return p.item;
        }
        return getRecursiveHelper(index-1,p.next);
    }
}
