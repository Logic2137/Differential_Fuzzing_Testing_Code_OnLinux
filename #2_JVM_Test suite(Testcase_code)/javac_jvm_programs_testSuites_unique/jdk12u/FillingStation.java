



package gc.memory.FillingStation;

public class FillingStation {

    public static final long minObjectSize  = 4;
    public static final long freeSpaceLimit = 64;
    public static final long maxObjectSize  = 32*1024;

    public static final boolean debug        = false;

    public static void main(String[] arg) {
        prologue();
        fill();
        epilogue();
    }

    public static void prologue() {
        _beforeMillis = System.currentTimeMillis();
    }

    public static void epilogue() {
        _afterMillis = System.currentTimeMillis();
        if (_overflow) {
            System.out.println("Overflowed!");
        }
        final double deltaSecs = (_afterMillis - _beforeMillis) / 1000.0;
        final double freeMegs = ((double) _freeBytes) / (1024.0 * 1024.0);
        final double totalMegs = ((double) _totalBytes) / (1024.0 * 1024.0);
        final double memRatio = freeMegs / totalMegs;
        System.out.println("Runtime.freeMemory()/Runtime.totalMemory: " +
                           Long.toString(_freeBytes) +
                           "/" +
                           Long.toString(_totalBytes) +
                           " = " +
                           Double.toString(memRatio));
        System.out.println("That took: " +
                           Double.toString(deltaSecs) +
                           " seconds");
    }

    public static void fill() {
        boolean _overflow = false;
        Runtime rt = java.lang.Runtime.getRuntime();
        java.util.Random stream = new java.util.Random();
        Space next = null;
        try {
            for (long available = rt.freeMemory();
                 available > freeSpaceLimit;
                 available = rt.freeMemory()) {
                long request   = (available - freeSpaceLimit) / 2;
                int maxRequest = (int) Math.min(maxObjectSize, request);
                int minRequest = (int) Math.max(minObjectSize, maxRequest);
                int size = stream.nextInt(minRequest);
                if (debug) {
                    System.err.println("available: " + Long.toString(available) +
                                       "  maxRequest: " + Integer.toString(maxRequest) +
                                       "  minRequest: " + Integer.toString(minRequest) +
                                       "  size: " + Integer.toString(size));
                }
                next = new Space(size, next);
            }
        } catch (OutOfMemoryError oome) {
            _overflow = true;
        }
        _freeBytes = rt.freeMemory();
        _totalBytes = rt.totalMemory();
    }

    static long    _beforeMillis = 0L;
    static long    _afterMillis  = 0L;
    static long    _freeBytes    = 0L;
    static long    _totalBytes   = 0L;
    static boolean _overflow     = false;
}

class Space {
    public Space(int bytes, Space next) {
        _next = next;
        _space = new byte[bytes];
    }
    private Space              _next  = null;
    private byte[]             _space = null;
}
