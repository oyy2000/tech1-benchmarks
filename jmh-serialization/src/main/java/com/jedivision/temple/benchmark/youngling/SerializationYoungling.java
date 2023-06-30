package com.jedivision.temple.benchmark.youngling;

import com.jedivision.temple.benchmark.AbstractState;
import com.jedivision.temple.entity.Youngling;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.jedivision.temple.serialization.SerializationType.*;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Mode.SingleShotTime;

@BenchmarkMode({AverageTime, SingleShotTime})
@OutputTimeUnit(MICROSECONDS)
@Fork(2)
@Warmup(iterations = 5)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
public class SerializationYoungling {

    @State(Scope.Benchmark)
    public static class SerializationState extends AbstractState {

        @Setup
        public void setup() throws IOException {
            initSerializers();
            initUsers("/jedi-youngling.json", Youngling.class);
        }
    }

    @Benchmark
    public long jdk(SerializationState state) throws Exception {
        return state.serialize(JDK);
    }

    @Benchmark
    public long fastjson(SerializationState state) throws Exception {
        return state.serialize(FASTJSON);
    }

    @Benchmark
    public long fastjson2(SerializationState state) throws Exception {
        return state.serialize(FASTJSON2);
    }

//    @Benchmark
    public long jacksonJson(SerializationState state) throws Exception {
        return state.serialize(JACKSON_JSON);
    }

//    @Benchmark
    public long jacksonSmile(SerializationState state) throws Exception {
        return state.serialize(JACKSON_SMILE);
    }

//    @Benchmark
    public long fst(SerializationState state) throws Exception {
        return state.serialize(FST);
    }

//    @Benchmark
    public long fstUnsafe(SerializationState state) throws Exception {
        return state.serialize(FST_UNSAFE);
    }

    @Benchmark
    public long kryo(SerializationState state) throws Exception {
        return state.serialize(KRYO);
    }

//    @Benchmark
    public long kryoUnsafe(SerializationState state) throws Exception {
        return state.serialize(KRYO_UNSAFE);
    }

//    @Benchmark
    public long messagePack(SerializationState state) throws Exception {
        return state.serialize(MESSAGE_PACK);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SerializationYoungling.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }

}
