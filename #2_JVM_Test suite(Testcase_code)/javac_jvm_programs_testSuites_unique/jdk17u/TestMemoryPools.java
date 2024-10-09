import java.lang.management.*;
import java.util.*;

public class TestMemoryPools {

    public static void main(String[] args) throws Exception {
        List<MemoryManagerMXBean> mms = ManagementFactory.getMemoryManagerMXBeans();
        if (mms == null) {
            throw new RuntimeException("getMemoryManagerMXBeans is null");
        }
        if (mms.isEmpty()) {
            throw new RuntimeException("getMemoryManagerMXBeans is empty");
        }
        for (MemoryManagerMXBean mmBean : mms) {
            String[] names = mmBean.getMemoryPoolNames();
            if (names == null) {
                throw new RuntimeException("getMemoryPoolNames() is null");
            }
            if (names.length == 0) {
                throw new RuntimeException("getMemoryPoolNames() is empty");
            }
            for (String name : names) {
                if (name == null) {
                    throw new RuntimeException("pool name is null");
                }
                if (name.length() == 0) {
                    throw new RuntimeException("pool name is empty");
                }
            }
        }
    }
}
