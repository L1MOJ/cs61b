package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void SimpleTest(){
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(5);
        assertEquals(ad1.size(),1);
        ad1.addFirst(1);
        ad1.addLast(8);
        assertEquals(ad1.size(),3);
        System.out.println("Print Deque");
        ad1.printDeque();
    }
    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest(){
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        assertTrue("Empty",ad1.isEmpty());
        ad1.addFirst(4);
        assertFalse("Size 1", ad1.isEmpty());
        int x = ad1.removeFirst();
        assertTrue("Empty", ad1.isEmpty());
    }
    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {



        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);
        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }
    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        ArrayDeque<String>  lld1 = new ArrayDeque<String>();
        ArrayDeque<Double>  lld2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {


        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {


        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }
    @Test
    /*Copy a LinkedListDeque*/
    public void copyTest(){
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.addLast(1);
        q.addLast(3);
        q.addLast(9);
        q.addFirst(7);
        ArrayDeque<Integer> p =new ArrayDeque<>(q);
        for (int i = 0; i<q.size();i++){
            assertEquals(p.get(i), q.get(i));
        }
    }

    @Test
    /*Add Get Test*/
    public void addGetTest(){
        ArrayDeque<Integer> q = new ArrayDeque<>();
        assertEquals(null,q.get(0));
        q.addFirst(0);
        int x = q.get(0);
        assertEquals(0,x);
    }
}
