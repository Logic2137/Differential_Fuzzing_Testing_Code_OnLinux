

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.ref.Cleaner;
import java.util.ArrayList;
import java.util.List;
import java.security.ProtectionDomain;

public class GCDuringDumpTransformer implements ClassFileTransformer {
    static boolean TEST_WITH_CLEANER = Boolean.getBoolean("test.with.cleaner");
    static boolean TEST_WITH_EXCEPTION = Boolean.getBoolean("test.with.exception");
    static boolean TEST_WITH_OOM = Boolean.getBoolean("test.with.oom");
    static List<byte[]> waste = new ArrayList();

    static Cleaner cleaner;
    static Thread thread;
    static Object garbage;

    static {
        if (TEST_WITH_CLEANER) {
            cleaner = Cleaner.create();
            garbage = new Object();
            cleaner.register(garbage, new MyCleaner());
            System.out.println("Registered cleaner");
        }
    }

    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined,
                            ProtectionDomain pd, byte[] buffer) throws IllegalClassFormatException {
        
        if (name.equals("jdk/internal/math/FDBigInteger")) {
            System.out.println("Transforming class jdk/internal/math/FDBigInteger");
            if (TEST_WITH_EXCEPTION) {
              System.out.println("Return bad buffer for " + name);
              return new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
            }
            if (TEST_WITH_OOM) {
                
                System.out.println("Fill objects until OOM");
                for (;;) {
                    waste.add(new byte[64*1024]);
                }
            }
        }

        if (TEST_WITH_CLEANER) {
            if (name.equals("Hello")) {
                garbage = null;
                System.out.println("Unreferenced GCDuringDumpTransformer.garbage");
            }
        } else {
            try {
                makeGarbage();
            } catch (Throwable t) {
                t.printStackTrace();
                try {
                    Thread.sleep(200); 
                } catch (Throwable t2) {}
            }
        }
        return null;
    }

    private static Instrumentation savedInstrumentation;

    public static void premain(String agentArguments, Instrumentation instrumentation) {
        System.out.println("ClassFileTransformer.premain() is called: TEST_WITH_CLEANER = " + TEST_WITH_CLEANER);
        System.out.println("ClassFileTransformer.premain() is called: TEST_WITH_EXCEPTION = " + TEST_WITH_EXCEPTION);
        System.out.println("ClassFileTransformer.premain() is called: TEST_WITH_OOM = " + TEST_WITH_OOM);
        instrumentation.addTransformer(new GCDuringDumpTransformer(), true);
        savedInstrumentation = instrumentation;
    }

    public static Instrumentation getInstrumentation() {
        return savedInstrumentation;
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        premain(args, inst);
    }

    public static void makeGarbage() {
        for (int x=0; x<10; x++) {
            Object[] a = new Object[10000];
        }
    }

    static class MyCleaner implements Runnable {
        static int count = 0;
        int i = count++;
        public void run() {
            
            
            Object o = new Object();
            System.out.println("cleaning " + i);
        }
    }
}
