import java.lang.management.ManagementFactory;

public class TestMemoryManagerMXBean {

    private static void checkName(String name) throws Exception {
        if (name == null || name.length() == 0) {
            throw new Exception("Invalid name");
        }
    }

    public static void main(String[] args) throws Exception {
        int zgcCyclesMemoryManagers = 0;
        int zgcPausesMemoryManagers = 0;
        int zgcCyclesMemoryPools = 0;
        int zgcPausesMemoryPools = 0;
        for (final var memoryManager : ManagementFactory.getMemoryManagerMXBeans()) {
            final var memoryManagerName = memoryManager.getName();
            checkName(memoryManagerName);
            System.out.println("MemoryManager: " + memoryManagerName);
            if (memoryManagerName.equals("ZGC Cycles")) {
                zgcCyclesMemoryManagers++;
            } else if (memoryManagerName.equals("ZGC Pauses")) {
                zgcPausesMemoryManagers++;
            }
            for (final var memoryPoolName : memoryManager.getMemoryPoolNames()) {
                checkName(memoryPoolName);
                System.out.println("   MemoryPool:   " + memoryPoolName);
                if (memoryPoolName.equals("ZHeap")) {
                    if (memoryManagerName.equals("ZGC Cycles")) {
                        zgcCyclesMemoryPools++;
                    } else if (memoryManagerName.equals("ZGC Pauses")) {
                        zgcPausesMemoryPools++;
                    }
                }
            }
        }
        if (zgcCyclesMemoryManagers != 1) {
            throw new Exception("Unexpected number of cycle MemoryManagers");
        }
        if (zgcPausesMemoryManagers != 1) {
            throw new Exception("Unexpected number of pause MemoryManagers");
        }
        if (zgcCyclesMemoryPools != 1) {
            throw new Exception("Unexpected number of cycle MemoryPools");
        }
        if (zgcPausesMemoryPools != 1) {
            throw new Exception("Unexpected number of pause MemoryPools");
        }
    }
}
