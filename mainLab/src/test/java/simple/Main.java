package simple;

import com.company.separate_que.NaiveImpl;
import com.company.Packet;
import java.util.logging.Logger;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;


@State(Scope.Thread)
public class Main {

    static Logger logger = Logger.getLogger("Main.class");
    static FileHandler fileHandler;

    @Param({"4", "6", "8", "12", "16", "32"})
    int count;

    @Param({"1000", "2000", "3000", "4000", "5000", "10000", "20000", "30000", "40000", "100000", "150000", "300000", "1000000"})
    int maxQueries;

    static NaiveImpl naive;
    AtomicInteger sum;
    LinkedList<Packet>[] packets;
    long[] times;

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 3)
    @Threads(value = 1)
    @Fork(value = 1)
    @OperationsPerInvocation(value = 1)
    @Measurement(iterations = 5)
    public void measureName() throws InterruptedException {
        synchronized (sum) {
            naive.Start();
            while(sum.get() != count * maxQueries) {
                sum.wait();
            }
        }
    }

    @TearDown(Level.Invocation)
    public void calcLatency() {
        long latencyAvg = 0;
        for(int i = 0; i < count; i++) {
            latencyAvg += times[i];
        }
        System.out.println("\r\nCount of Edges = " + String.valueOf(count) +
                " , Packets = " + String.valueOf(maxQueries) + ", nanotimes = " + String.valueOf(latencyAvg) + "\r\n");
    }

    @Setup(Level.Invocation)
    public void prepareData() throws IOException {
        final Random random = new Random(1234);

        packets = new LinkedList[count];
        times = new long[count];

        for(int i = 0; i < count; i ++) {
            packets[i] = new LinkedList<Packet>();
        }

        for(int from = 0; from < count; from++) {
            for (int i = 0; i < maxQueries; i++) {

                int to = (from - 1 + count) % count;
                packets[from].add(new Packet(to, null));
            }
        }

        sum = new AtomicInteger();
        naive = new NaiveImpl(count, packets, sum, maxQueries * count, times);
    }


    public static void main(String[] args) throws IOException, RunnerException {
        Options opt = new OptionsBuilder()
                .include(Main.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
