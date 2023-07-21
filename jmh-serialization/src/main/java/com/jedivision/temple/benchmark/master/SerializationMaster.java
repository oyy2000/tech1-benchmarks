package com.jedivision.temple.benchmark.master;

import com.jedivision.temple.benchmark.AbstractState;
import com.jedivision.temple.entity.Master;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.jedivision.temple.serialization.SerializationType.*;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Mode.SingleShotTime;

@BenchmarkMode({AverageTime, SingleShotTime})
@OutputTimeUnit(MICROSECONDS)
@Fork(2)
@Warmup(iterations = 5, time = 2)
@State(Scope.Benchmark)
@Measurement(iterations = 5, time = 2)
public class SerializationMaster {

    @State(Scope.Benchmark)
    public static class SerializationState extends AbstractState {
        @Setup
        public void setup() throws IOException {
            initSerializers();
            initUsers("/jedi-master.json", Master.class);
        }
    }

    @Benchmark
    public long jdk(SerializationState state) throws Exception {
        return state.serialize(JDK);
    }


    @Benchmark
    public long fastjson2(SerializationState state) throws Exception {
        return state.serialize(FASTJSON2);
    }


    @Benchmark
    public long kryo(SerializationState state) throws Exception {
        return state.serialize(KRYO);
    }

    @Benchmark
    public long protostuff(SerializationState state) throws Exception {
        return state.serialize(PROTOSTUFF);
    }

    @Benchmark
    public long protobuf(SerializationState state) throws Exception {
        return state.serialize(PROTOBUF);
    }

    @Benchmark
    public long fury(SerializationState state) throws Exception {
        return state.serialize(FURY);
    }

    @Benchmark
    public long furyUnsafe(SerializationState state) throws Exception {
        return state.serialize(FURY_UNSAFE);
    }

    public static void main(String[] args) throws RunnerException {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.currentTimeMillis();
        Options opt = new OptionsBuilder()
                .include(SerializationMaster.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
//                .output("serialization-master-" + localDateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd-hh-mm-ss")) + ".json")
                .build();

        new Runner(opt).run();
    }
}
