



import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestFullGCCount {

    static List<GarbageCollectorMXBean> collectors = ManagementFactory.getGarbageCollectorMXBeans();

    public static void main(String[] args) {
        int iterations = 20;
        boolean failed = false;
        String errorMessage = "";
        HashMap<String, List> counts = new HashMap<>();

        
        for (int i = 0; i < collectors.size(); i++) {
            GarbageCollectorMXBean collector = collectors.get(i);
            counts.put(collector.getName(), new ArrayList<>(iterations));
        }

        
        for (int i = 0; i < iterations; i++) {
            System.gc();
            addCollectionCount(counts, i);
        }

        
        
        
        
        for (String collector : counts.keySet()) {
            System.out.println("Checking: " + collector);

            for (int i = 0; i < iterations - 1; i++) {
                List<Long> theseCounts = counts.get(collector);
                long a = theseCounts.get(i);
                long b = theseCounts.get(i + 1);
                if (b - a >= 2) {
                    failed = true;
                    errorMessage += "Collector '" + collector + "' has increment " + (b - a) +
                                    " at iteration " + i + "\n";
                }
            }
        }
        if (failed) {
            System.err.println(errorMessage);
            throw new RuntimeException("FAILED: System.gc collections miscounted.");
        }
        System.out.println("Passed.");
    }

    private static void addCollectionCount(HashMap<String, List> counts, int iteration) {
        for (int i = 0; i < collectors.size(); i++) {
            GarbageCollectorMXBean collector = collectors.get(i);
            List thisList = counts.get(collector.getName());
            thisList.add(collector.getCollectionCount());
        }
    }
}
