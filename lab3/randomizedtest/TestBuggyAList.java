package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> standard = new AListNoResizing<>();
        BuggyAList<Integer> flaw = new BuggyAList<>();

        standard.addLast(4);
        flaw.addLast(4);
        standard.addLast(5);
        flaw.addLast(5);
        standard.addLast(6);
        flaw.addLast(6);
        for (int i =0;i<3;i++){
            int a = standard.removeLast();
            int b = flaw.removeLast();
            assertEquals(a,b);
        }
    }

    @Test
    public void randonizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B =new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int sizeb = B.size();
                assertEquals(size,sizeb);
                System.out.println("size: " + size);
            } else if (operationNumber ==2) {
                //getlast
                if (L.size() == 0){

                }
                else {
                    int last = L.getLast();
                    int lastb = B.getLast();
                    assertEquals(lastb,last);
                    System.out.println("last = " + last);
                }
            } else if (operationNumber == 3) {
                //removeLast
                if (L.size() == 0){

                }
                else {
                    int last = L.removeLast();
                    int lastb = B.removeLast();
                    assertEquals(lastb,last);
                    System.out.println(last + " removed");
                }
            }
        }
    }
}
