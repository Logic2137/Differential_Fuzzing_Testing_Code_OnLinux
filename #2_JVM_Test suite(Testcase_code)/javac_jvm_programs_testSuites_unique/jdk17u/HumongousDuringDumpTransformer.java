import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class HumongousDuringDumpTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain pd, byte[] buffer) throws IllegalClassFormatException {
        if (name.equals("Hello")) {
            try {
                makeHumongousRegions();
            } catch (Throwable t) {
                array = null;
                humon = null;
                System.out.println("Unexpected error: " + t);
                t.printStackTrace();
            }
        }
        array = null;
        return null;
    }

    private static Instrumentation savedInstrumentation;

    public static void premain(String agentArguments, Instrumentation instrumentation) {
        long xmx = Runtime.getRuntime().maxMemory();
        if (xmx < 60 * 1024 * 1024 || xmx > 80 * 1024 * 1024) {
            System.out.println("Running with incorrect heap size: " + xmx);
            System.exit(1);
        }
        System.out.println("ClassFileTransformer.premain() is called");
        instrumentation.addTransformer(new HumongousDuringDumpTransformer(), true);
        savedInstrumentation = instrumentation;
    }

    public static Instrumentation getInstrumentation() {
        return savedInstrumentation;
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        premain(args, inst);
    }

    Object[] array;

    static final int DUMMY_SIZE = 4096 - 16 - 8;

    static final int HUMON_SIZE = 4 * 1024 * 1024 - 16 - 8;

    static final int SKIP = 13;

    byte[] humon = null;

    boolean first = true;

    public synchronized void makeHumongousRegions() {
        if (!first) {
            return;
        }
        System.out.println("===============================================================================");
        first = false;
        int total = 0;
        array = new Object[100000];
        System.out.println(array);
        for (int n = 0, i = 0; total < 8 * 1024 * 1024; n++) {
            Object x = new byte[DUMMY_SIZE];
            if ((n % SKIP) == 0) {
                array[i++] = x;
                total += DUMMY_SIZE;
            }
        }
        System.gc();
        humon = new byte[HUMON_SIZE];
        array = null;
        System.gc();
    }
}
