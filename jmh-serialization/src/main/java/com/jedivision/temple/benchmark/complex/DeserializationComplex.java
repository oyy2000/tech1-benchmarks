package com.jedivision.temple.benchmark.complex;

import com.jedivision.temple.benchmark.AbstractState;
import com.jedivision.temple.entity.pojo.FareSearchResponse;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.jedivision.temple.serialization.SerializationType.*;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Mode.SingleShotTime;

@BenchmarkMode({AverageTime, SingleShotTime})
@OutputTimeUnit(MICROSECONDS)
@Fork(2)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 5, time = 2)
public class DeserializationComplex {

    @State(Scope.Benchmark)
    public static class SerializationState extends AbstractState {
        @Setup
        public void setup() throws IOException {
            initSerializers();
            initUsers("SHA-HKG-2023-05-25.json", FareSearchResponse.class);
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

//    @Benchmark
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

    public static void main(String[] args) throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now();
        Options opt = new OptionsBuilder()
                .include(DeserializationComplex.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .output("deserialization-complex-" + localDateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd-hh-mm-ss")) + ".json")
                .build();

        new Runner(opt).run();
    }
}
//deserialization-complex-23-06-29-02-18-16