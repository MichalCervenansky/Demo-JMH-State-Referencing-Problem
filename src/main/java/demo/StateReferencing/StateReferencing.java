package demo.StateReferencing;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 1, time = 1)
@Threads(1)
public class StateReferencing {
    Manager manager;

    @State(Scope.Thread)
    public static class ThreadScope {
        // Workaround
        Manager manager;
        Engine engine;

        @Setup(Level.Invocation)
        public void init(StateReferencing stateReferencing) {
            manager = stateReferencing.manager;
            manager.setUpSomething();
        }

        @TearDown(Level.Invocation)
        public void close() {
            manager.disposeEngine(engine);
        }
    }

    @Setup(Level.Iteration)
    public void init() {
        manager = new Manager();
    }

    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Benchmark
    public void Throughput(ThreadScope threadScope) {
        execute(threadScope);
    }

    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Benchmark
    public void sampleTime(ThreadScope threadScope) {
        execute(threadScope);
    }

    private void execute(ThreadScope threadScope) {
        threadScope.engine.compute();
    }

    @TearDown(Level.Iteration)
    public void close() {
        manager.close();
    }
}
