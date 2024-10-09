

package gc.testlibrary;

import sun.jvmstat.monitor.Monitor;


public class PerfCounter {
    private final Monitor monitor;
    private final String name;

    PerfCounter(Monitor monitor, String name) {
        this.monitor = monitor;
        this.name = name;
    }

    
    public Object value() {
        return monitor.getValue();
    }

    
    public long longValue() {
        Object value = monitor.getValue();
        if (value instanceof Long) {
            return ((Long) value).longValue();
        }
        throw new RuntimeException("Expected " + monitor.getName() + " to have a long value");
    }

    
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
