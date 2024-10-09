
package nsk.monitoring.share;

import java.util.List;
import java.lang.management.*;
import javax.management.*;

public interface MonitoringFactory {

    public ClassLoadingMXBean getClassLoadingMXBean();

    public boolean hasCompilationMXBean();

    public CompilationMXBean getCompilationMXBean();

    public List<GarbageCollectorMXBean> getGarbageCollectorMXBeans();

    public RuntimeMXBean getRuntimeMXBean();

    public MemoryMXBean getMemoryMXBean();

    public NotificationEmitter getMemoryMXBeanNotificationEmitter();

    public List<MemoryPoolMXBean> getMemoryPoolMXBeans();

    public ThreadMXBean getThreadMXBean();

    public boolean hasThreadMXBeanNew();

    public ThreadMXBean getThreadMXBeanNew();
}
