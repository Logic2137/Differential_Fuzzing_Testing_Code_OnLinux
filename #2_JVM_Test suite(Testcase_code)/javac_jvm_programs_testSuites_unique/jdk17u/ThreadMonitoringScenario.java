
package nsk.monitoring.share.thread;

import java.lang.management.ThreadMXBean;

public interface ThreadMonitoringScenario {

    public void begin();

    public void waitState();

    public void check(ThreadMXBean threadMXBean);

    public void finish();

    public void end();
}
