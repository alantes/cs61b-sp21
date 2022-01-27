package deque;

import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeArrayDeque {
    private static void printTimingTable(ArrayDeque<Integer> Ns, ArrayDeque<Double> times,
                                         ArrayDeque<Integer> opCounts) {
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
        timeArrayDequeConstruction();
    }

    public static void timeArrayDequeConstruction() {
        ArrayDeque<Integer> Ns = new ArrayDeque<>();
        ArrayDeque<Double> times = new ArrayDeque<>();
        ArrayDeque<Integer> opCounts = new ArrayDeque<>();
        int i = 1000;
        while (i <= 128000) {
            Ns.addLast(i);
            int count = 0;
            ArrayDeque<Integer> tempList = new ArrayDeque<>();
            Stopwatch sw = new Stopwatch();
            for (int j = i; j > 0; j--) {
                tempList.addLast(j);
                count += 1;
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            opCounts.addLast(count);
            i *= 2;
        }
        printTimingTable(Ns, times, opCounts);
    }
}

