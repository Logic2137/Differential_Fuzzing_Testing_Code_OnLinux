



import java.lang.management.*;

public class UpTime {
    static final long DELAY = 5; 
    static final long TIMEOUT = 30; 
    static final long MULTIPLIER = 1000; 

    private static final RuntimeMXBean metrics
        = ManagementFactory.getRuntimeMXBean();

    public static void main(String argv[]) throws Exception {
        long jvmStartTime = metrics.getStartTime();
        
        long jvmUptime = System.currentTimeMillis() - jvmStartTime;
        long systemStartOuter = System_milliTime();
        long metricsStart = metrics.getUptime();
        long systemStartInner = System_milliTime();

        
        
        long testUptime = metricsStart - jvmUptime;

        
        
        if (testUptime > TIMEOUT * 60 * MULTIPLIER)
            throw new RuntimeException("Uptime of the JVM is more than 30 "
                                     + "minutes ("
                                     + (metricsStart / 60 / MULTIPLIER)
                                     + " minutes).");

        
        Object o = new Object();
        while (System_milliTime() < systemStartInner + DELAY * MULTIPLIER) {
            synchronized (o) {
                try {
                    o.wait(DELAY * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        long systemEndInner = System_milliTime();
        long metricsEnd = metrics.getUptime();
        long systemEndOuter = System_milliTime();

        long systemDifferenceInner = systemEndInner - systemStartInner;
        long systemDifferenceOuter = systemEndOuter - systemStartOuter;
        long metricsDifference = metricsEnd - metricsStart;

        
        
        
        
        
        
        if (metricsDifference - systemDifferenceInner < -1)
            throw new RuntimeException("Flow of the time in "
                                     + "RuntimeMXBean.getUptime() ("
                                     + metricsDifference + ") is slower than "
                                     + " in system (" + systemDifferenceInner
                                     + ")");
        if (metricsDifference - systemDifferenceOuter > 1)
            throw new RuntimeException("Flow of the time in "
                                     + "RuntimeMXBean.getUptime() ("
                                     + metricsDifference + ") is faster than "
                                     + "in system (" + systemDifferenceOuter
                                     + ")");

        System.out.println("Test passed.");
    }

    private static long System_milliTime() {
        return System.nanoTime() / 1000000; 
    }
}


