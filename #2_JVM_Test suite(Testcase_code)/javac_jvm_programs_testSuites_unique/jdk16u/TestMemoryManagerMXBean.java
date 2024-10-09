



import java.lang.management.ManagementFactory;

public class TestMemoryManagerMXBean {
    private static void checkName(String name) throws Exception {
        if (name == null || name.length() == 0) {
            throw new Exception("Invalid name");
        }
    }

    public static void main(String[] args) throws Exception {
        int zgcMemoryManagers = 0;
        int zgcMemoryPools = 0;

        for (final var memoryManager : ManagementFactory.getMemoryManagerMXBeans()) {
            final var memoryManagerName = memoryManager.getName();
            checkName(memoryManagerName);

            System.out.println("MemoryManager: " + memoryManagerName);

            if (memoryManagerName.equals("ZGC")) {
                zgcMemoryManagers++;
            }

            for (final var memoryPoolName : memoryManager.getMemoryPoolNames()) {
                checkName(memoryPoolName);

                System.out.println("   MemoryPool:   " + memoryPoolName);

                if (memoryPoolName.equals("ZHeap")) {
                    zgcMemoryPools++;
                }
            }

            if (zgcMemoryManagers != zgcMemoryPools) {
                throw new Exception("MemoryManagers/MemoryPools mismatch");
            }
        }

        if (zgcMemoryManagers != 1) {
            throw new Exception("All MemoryManagers not found");
        }

        if (zgcMemoryPools != 1) {
            throw new Exception("All MemoryPools not found");
        }
    }
}
