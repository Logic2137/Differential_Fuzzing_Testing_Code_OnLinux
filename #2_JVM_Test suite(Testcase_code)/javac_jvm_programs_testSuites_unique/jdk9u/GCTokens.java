

package gc.g1.humongousObjects.objectGraphTest;


public final class GCTokens {
    
    private GCTokens() {
    }

    public static final String WB_INITIATED_YOUNG_GC = "Young (WhiteBox Initiated Young GC)";
    public static final String WB_INITIATED_MIXED_GC = "Pause Mixed (WhiteBox Initiated Young GC)";
    public static final String WB_INITIATED_CMC = "WhiteBox Initiated Concurrent Mark";
    public static final String FULL_GC = "Full (System.gc())";
    public static final String FULL_GC_MEMORY_PRESSURE = "WhiteBox Initiated Full GC";
    public static final String CMC = "Concurrent Mark)";
    public static final String YOUNG_GC = "GC pause (young)";
}
