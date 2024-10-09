

package jdk.test.lib;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class LockFreeLogger {
    private final AtomicInteger logCntr = new AtomicInteger(0);
    private final Collection<Map<Integer, String>> allRecords = new ConcurrentLinkedQueue<>();
    private final ThreadLocal<Map<Integer, String>> records = ThreadLocal.withInitial(ConcurrentHashMap::new);

    public LockFreeLogger() {
        allRecords.add(records.get());
    }

    
    public void log(String format, Object ... params) {
        int id = logCntr.getAndIncrement();
        records.get().put(id, String.format(format, params));
    }

    
    @Override
    public String toString() {
        return allRecords.stream()
            .flatMap(m -> m.entrySet().stream())
            .sorted(Comparator.comparing(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .collect(Collectors.joining());
    }
}
