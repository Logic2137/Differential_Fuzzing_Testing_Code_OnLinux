
package jdk.test.lib;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class LockFreeLogger {

    private final Queue<String> records = new ConcurrentLinkedQueue<>();

    public LockFreeLogger() {
    }

    public void log(String format, Object... params) {
        records.add(String.format(format, params));
    }

    @Override
    public String toString() {
        return records.stream().collect(Collectors.joining());
    }
}
