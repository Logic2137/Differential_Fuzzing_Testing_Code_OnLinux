

package gc.arguments;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Objects;


public final class GCTypes {

    private static <T extends GCType> T getCurrentGCType(Class<T> type) {
        return ManagementFactory.getGarbageCollectorMXBeans().stream()
                .map(bean -> getGCTypeByName(type, bean.getName()))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private static <T extends GCType> T getGCTypeByName(Class<T> type, String name) {
        return Arrays.stream(type.getEnumConstants())
                .filter(e -> e.getGCName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private static <T extends GCType> GarbageCollectorMXBean getGCBeanByType(Class<T> type) {
        return ManagementFactory.getGarbageCollectorMXBeans().stream()
                .filter(bean -> Arrays.stream(type.getEnumConstants())
                        .filter(enumName -> enumName.getGCName().equals(bean.getName()))
                        .findFirst()
                        .isPresent()
                )
                .findFirst()
                .orElse(null);
    }

    
    private interface GCType {

        String getGCName();
    }

    public static enum YoungGCType implements GCType {
        DefNew("Copy"),
        PSNew("PS Scavenge"),
        G1("G1 Young Generation");

        @Override
        public String getGCName() {
            return gcTypeName;
        }
        private final String gcTypeName;

        private YoungGCType(String name) {
            gcTypeName = name;
        }

        public static YoungGCType getYoungGCType() {
            return GCTypes.getCurrentGCType(YoungGCType.class);
        }

        public static GarbageCollectorMXBean getYoungGCBean() {
            return GCTypes.getGCBeanByType(YoungGCType.class);
        }
    }

    public static enum OldGCType implements GCType {
        Serial("MarkSweepCompact"),
        PSOld("PS MarkSweep"),
        G1("G1 Old Generation");

        private final String gcTypeName;

        private OldGCType(String name) {
            gcTypeName = name;
        }

        public static OldGCType getOldGCType() {
            return GCTypes.getCurrentGCType(OldGCType.class);
        }

        @Override
        public String getGCName() {
            return gcTypeName;
        }
    }
}
