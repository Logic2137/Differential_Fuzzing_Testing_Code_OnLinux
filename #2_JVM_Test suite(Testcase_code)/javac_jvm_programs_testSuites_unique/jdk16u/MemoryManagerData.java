
package vm.share.monitoring.data;

import java.lang.management.*;
import javax.management.*;
import java.io.Serializable;

public class MemoryManagerData implements MemoryManagerMXBean, Serializable {
        private String[] memoryPoolNames;
        private String name;
        private boolean valid;

        public MemoryManagerData(String[] memoryPoolNames, String name, boolean valid) {
                this.memoryPoolNames = memoryPoolNames;
                this.name = name;
                this.valid = valid;
        }

        public MemoryManagerData(MemoryManagerMXBean memoryManager) {
                this.memoryPoolNames = memoryManager.getMemoryPoolNames();
                this.name = memoryManager.getName();
                this.valid = memoryManager.isValid();
        }

        public String[] getMemoryPoolNames() {
                return memoryPoolNames;
        }

        public String getName() {
                return name;
        }

        public boolean isValid() {
                return valid;
        }

    public ObjectName getObjectName() {
        return null;
    }
}
