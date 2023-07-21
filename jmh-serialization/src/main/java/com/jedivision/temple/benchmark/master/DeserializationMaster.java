package com.jedivision.temple.benchmark.master;

import com.jedivision.temple.benchmark.AbstractState;
import com.jedivision.temple.entity.Master;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
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
@Measurement(iterations = 5, time = 2)
public class DeserializationMaster {

    @State(Scope.Benchmark)
    public static class SerializationState extends AbstractState {

        @Setup
        public void setup() throws IOException {
            initSerializers();
            initUsers("/jedi-master.json", Master.class);
            initSerializedBytesByType();
        }
    }
    @Benchmark
    public void jdk(SerializationState state) throws Exception {
        state.deserialize(JDK);
    }

    @Benchmark
    public void kryo(SerializationState state) throws Exception {
        state.deserialize(KRYO);
    }

    @Benchmark
    public void fastjson2(SerializationState state) throws Exception {
        state.deserialize(FASTJSON2);
    }

    @Benchmark
    public void protostuff(SerializationState state) throws Exception {
        state.deserialize(PROTOSTUFF);
    }

    @Benchmark
    public void protobuf(SerializationState state) throws Exception {
        state.deserialize(PROTOBUF);
    }

    @Benchmark
    public void fury(SerializationState state) throws Exception {
        state.deserialize(FURY);
    }

    @Benchmark
    public void furyUnsafe(SerializationState state) throws Exception {
        state.deserialize(FURY_UNSAFE);
    }

    public static void main(String[] args) throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now();
        Options opt = new OptionsBuilder()
                .include(DeserializationMaster.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
//                .output("deserialization-master-" + localDateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd-hh-mm-ss")) + ".json")
                .build();

        new Runner(opt).run();
        SerializationState ss = new SerializationState();
        ss.setup();
        ss.displaySerializedBytesByType();
    }
}
