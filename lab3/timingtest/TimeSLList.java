package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> num = new AList<>();
        AList<Double> time = new AList<>();
        AList<Integer> ops = new AList<>();

        for (int i =0;i<8;i++){
            ops.addLast(10000);
        }

        num.addLast(1000);
        num.addLast(2000);
        num.addLast(4000);
        num.addLast(8000);
        num.addLast(16000);
        num.addLast(32000);
        num.addLast(64000);
        num.addLast(128000);

        for (int i =0;i< num.size();i++){
            SLList<Integer> test = new SLList<>();
            for (int j =0; j<num.get(i); j++){
                test.addLast(1);
            }
            int last;
            Stopwatch sw = new Stopwatch();
            for (int q =0;q<10000;q++){
                last = test.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            time.addLast(timeInSeconds);
        }

        printTimingTable(num,time,ops);
    }

}
