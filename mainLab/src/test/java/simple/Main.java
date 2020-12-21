package simple;

import com.company.simple.NaiveImpl;
import com.company.Packet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


@State(Scope.Thread)
public class Main {

    @Param({"4", "10", "20", "50"})
    int count;

    @Param({"1", "5", "10", "20"})
    int maxQueries;

    static NaiveImpl naive;
    AtomicInteger sum;
    LinkedList<Packet>[] packets;

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 20)
    @Fork(value = 1)
    @OperationsPerInvocation(value = 1)
    @Measurement(iterations = 40)
    public void measureName() throws InterruptedException {
        synchronized (sum) {
            naive.Start();
            while(sum.get() != count * maxQueries) {
                sum.wait();
            }
        }
    }

    @Setup(Level.Invocation)
    public void prepareData() {
        final Random random = new Random(1234);

        packets = new LinkedList[count];

        for(int i = 0; i < count; i ++) {
            packets[i] = new LinkedList<Packet>();
        }

        for(int from = 0; from < count; from++) {
            for (int i = 0; i < maxQueries; i++) {

                int to = random.nextInt(count);

                if (from == to) {
                    to = (from + 1) % count;
                }

                packets[from].add(new Packet(to, null));
            }
        }

        sum = new AtomicInteger();
        naive = new NaiveImpl(count, packets, sum, maxQueries * count);
    }


    public static void main(String[] args) throws IOException, RunnerException {
        Options opt = new OptionsBuilder()
                .include(Main.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
