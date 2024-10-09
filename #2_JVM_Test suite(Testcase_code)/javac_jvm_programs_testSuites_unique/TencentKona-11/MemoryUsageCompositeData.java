



import javax.management.openmbean.*;
import java.lang.management.MemoryUsage;

public class MemoryUsageCompositeData {
    public static void main(String[] argv) throws Exception {
        createGoodCompositeData();
        badTypeCompositeData();
        badNameCompositeData();
        System.out.println("Test passed");
    }

    public static void createGoodCompositeData() throws Exception {
        final int K = 1024;
        
        final Object[] values = {
            new Long(5 * K),  
            new Long(1 * K),  
            new Long(10 * K), 
            new Long(2 * K),  
            "Dummy",
            "Dummy",
        };

        CompositeType muct =
            new CompositeType("MyMemoryUsageCompositeType",
                              "CompositeType for MemoryUsage",
                              memoryUsageItemNames,
                              memoryUsageItemNames,
                              memoryUsageItemTypes);
        CompositeData cd =
            new CompositeDataSupport(muct,
                                     memoryUsageItemNames,
                                     values);
        MemoryUsage u = MemoryUsage.from(cd);
        if (u.getInit() != ((Long) values[INIT]).longValue()) {
            throw new RuntimeException("init = " + u.getInit() +
               " expected = " + values[INIT]);
        }
        if (u.getUsed() != ((Long) values[USED]).longValue()) {
            throw new RuntimeException("used = " + u.getUsed() +
               " expected = " + values[USED]);
        }
        if (u.getCommitted() != ((Long) values[COMMITTED]).longValue()) {
            throw new RuntimeException("committed = " + u.getCommitted() +
               " expected = " + values[COMMITTED]);
        }
        if (u.getMax() != ((Long) values[MAX]).longValue()) {
            throw new RuntimeException("max = " + u.getMax() +
               " expected = " + values[MAX]);
        }
        System.out.println(u);
    }

    public static void badTypeCompositeData() throws Exception {
        final int K = 1024;
        final Object[] values = {
            new Integer(5 * K),
            new Long(1 * K),
            new Long(10 * K),
            new Long(2 * K),
            "Dummy",
            "Dummy",
        };

        CompositeType muct =
            new CompositeType("MyMemoryUsageCompositeType",
                              "CompositeType for MemoryUsage",
                              memoryUsageItemNames,
                              memoryUsageItemNames,
                              badMUItemTypes);
        CompositeData cd =
           new CompositeDataSupport(muct,
                                    memoryUsageItemNames,
                                    values);
        try {
            MemoryUsage u = MemoryUsage.from(cd);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected exception: " +
                e.getMessage());
            return;
        }
        throw new RuntimeException(
            "IllegalArgumentException not thrown");
    }

    public static void badNameCompositeData() throws Exception {
        final int K = 1024;
        final Object[] values = {
            new Long(5 * K),
            new Long(1 * K),
            new Long(10 * K),
            new Long(2 * K),
            "Dummy",
            "Dummy",
        };

        CompositeType muct =
            new CompositeType("MyMemoryUsageCompositeType",
                              "CompositeType for MemoryUsage",
                               badMUItemNames,
                               badMUItemNames,
                               memoryUsageItemTypes);
        CompositeData cd =
            new CompositeDataSupport(muct,
                                     badMUItemNames,
                                     values);
        try {
            MemoryUsage u = MemoryUsage.from(cd);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected exception: " +
                e.getMessage());
            return;
        }
        throw new RuntimeException(
            "IllegalArgumentException not thrown");
    }

    private static final int COMMITTED = 0;
    private static final int INIT      = 1;
    private static final int MAX       = 2;
    private static final int USED      = 3;
    private static final String[] memoryUsageItemNames = {
        "committed",
        "init",
        "max",
        "used",
        "dummy1",
        "dummy2",
    };
    private static final OpenType[] memoryUsageItemTypes = {
        SimpleType.LONG,
        SimpleType.LONG,
        SimpleType.LONG,
        SimpleType.LONG,
        SimpleType.STRING,
        SimpleType.STRING,
    };
    private static final String[] badMUItemNames = {
        "Committed",
        "Init",
        "max",
        "used",
        "dummy1",
        "dummy2",
    };
    private static final OpenType[] badMUItemTypes = {
        SimpleType.INTEGER,
        SimpleType.LONG,
        SimpleType.LONG,
        SimpleType.LONG,
        SimpleType.STRING,
        SimpleType.STRING,
    };
}
