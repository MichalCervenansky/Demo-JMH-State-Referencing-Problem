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
public class StateReferencingNotWorking {

    /**
     * Unable to build due to:
     * /home/Example-jmh-state-referencing/target/classes/demo/StateReferencing/generated/StateReferencingNotWorking_sampleTime_jmhTest.java:[551,207] variable l_statereferencingnotworking0_G is already defined in method _jmh_tryInit_f_threadscope1_0(org.openjdk.jmh.runner.InfraControl,demo.StateReferencing.generated.StateReferencingNotWorking_jmhType,demo.StateReferencing.generated.StateReferencingNotWorking_jmhType)
     * It seems that two parameters of same type are generated into variables of the same names which causes this error.
     */

    Manager manager;

    @State(Scope.Thread)
    public static class ThreadScope {
        Engine engine;

        @Setup(Level.Invocation)
        public void init(StateReferencingNotWorking stateReferencing) {
            stateReferencing.manager.setUpSomething();
        }

        @TearDown(Level.Invocation)
        public void close(StateReferencingNotWorking stateReferencing) {
            stateReferencing.manager.disposeEngine(engine);
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
