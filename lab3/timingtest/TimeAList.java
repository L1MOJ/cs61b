package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> num = new AList<>();
        AList<Double> time = new AList<>();

        num.addLast(1000);
        num.addLast(2000);
        num.addLast(4000);
        num.addLast(8000);
        num.addLast(16000);
        num.addLast(32000);
        num.addLast(64000);
        num.addLast(128000);

        for (int i =0; i< num.size();i++){
            AList<Integer> temp = new AList<>();
            int max = num.get(i);
            Stopwatch sw = new Stopwatch();
            for (int j =0;j<max;j++){
                temp.addLast(1);
            }
            double timeInSeconds = sw.elapsedTime();
            time.addLast(timeInSeconds);
        }

        printTimingTable(num,time,num);
    }
}
