
package vm.share.monitoring;

import java.lang.management.*;

public enum MemoryPoolFinder {
    CODE_CACHE,
        EDEN_SPACE,
        SURVIVOR_SPACE,
        OLD_GEN,
        PERM_GEN,
        METASPACE,
        CLASS_METASPACE;

    public static MemoryPoolMXBean findPool(MemoryPoolFinder pool) {
        for(MemoryPoolMXBean candidate : ManagementFactory.getMemoryPoolMXBeans()) {
            boolean found = false;
            switch(pool) {
            case CODE_CACHE:
                found = candidate.getName().contains("Code Cache");
                break;
            case EDEN_SPACE:
                found = candidate.getName().contains("Eden");
                break;
            case SURVIVOR_SPACE:
                found = candidate.getName().contains("Survivor");
                break;
            case OLD_GEN:
                found = candidate.getName().contains("Old") || candidate.getName().contains("Tenured");
                break;
            case PERM_GEN:
                found = candidate.getName().contains("Perm");
                break;
            case METASPACE:
                found = candidate.getName().contains("Metaspace") && !candidate.getName().contains("Class Metaspace");
                break;
            case CLASS_METASPACE:
                found = candidate.getName().contains("Class Metaspace");
                break;
            }
            if (found) return candidate;
        }
        return null;
    }
}
