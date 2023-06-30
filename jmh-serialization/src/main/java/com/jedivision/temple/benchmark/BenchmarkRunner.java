package com.jedivision.temple.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static org.openjdk.jmh.annotations.Mode.*;

@BenchmarkMode({AverageTime, SingleShotTime})
@OutputTimeUnit(MICROSECONDS)
@Fork(2)
@Warmup(iterations = 5)
@Measurement(iterations = 5, time = 2)
public class BenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        System.out.println(BenchmarkRunner.class.getSimpleName());
        Options opt = new OptionsBuilder()
                .include(BenchmarkRunner.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }
}
