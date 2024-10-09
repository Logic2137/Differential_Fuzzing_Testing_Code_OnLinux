
package org.openjdk.bench.java.util.stream.ops;

public class LongAccumulator {

    long acc;

    public LongAccumulator() {
        acc = 0;
    }

    public void add(long v) {
        acc += v;
    }

    public void merge(LongAccumulator other) {
        acc += other.acc;
    }

    public long get() {
        return acc;
    }
}
