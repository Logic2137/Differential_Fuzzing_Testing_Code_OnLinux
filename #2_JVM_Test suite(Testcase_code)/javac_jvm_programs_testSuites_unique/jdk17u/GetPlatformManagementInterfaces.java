import java.lang.management.*;
import java.io.IOException;
import java.util.*;
import javax.management.*;
import static java.lang.management.ManagementFactory.*;

public class GetPlatformManagementInterfaces {

    private static enum ManagementInterfaces {

        CLASS_LOADING_MXBEAN(ClassLoadingMXBean.class),
        COMPILATION_MXBEAN(CompilationMXBean.class),
        MEMORY_MXBEAN(MemoryMXBean.class),
        OPERATING_SYSTEM_MXBEAN(OperatingSystemMXBean.class),
        RUNTIME_MXBEAN(RuntimeMXBean.class),
        THREAD_MXBEAN(ThreadMXBean.class),
        GARBAGE_COLLECTOR_MXBEAN(GarbageCollectorMXBean.class),
        MEMORY_MANAGER_MXBEAN(MemoryManagerMXBean.class),
        MEMORY_POOL_MXBEAN(MemoryPoolMXBean.class);

        private final Class<? extends PlatformManagedObject> managementInterface;

        private ManagementInterfaces(Class<? extends PlatformManagedObject> minterface) {
            managementInterface = minterface;
        }

        public Class<? extends PlatformManagedObject> getManagementInterface() {
            return managementInterface;
        }
    }

    public static void main(String[] args) {
        Set<Class<? extends PlatformManagedObject>> interfaces = ManagementFactory.getPlatformManagementInterfaces();
        for (Class<? extends PlatformManagedObject> pom : interfaces) {
            List<? extends PlatformManagedObject> list = ManagementFactory.getPlatformMXBeans(pom);
        }
        for (ManagementInterfaces mi : ManagementInterfaces.values()) {
            if (!interfaces.contains(mi.getManagementInterface())) {
                throw new RuntimeException(mi.getManagementInterface() + " not in ManagementInterfaces set");
            }
        }
    }
}
