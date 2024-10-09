

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class GCDuringDumpTransformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined,
                            ProtectionDomain pd, byte[] buffer) throws IllegalClassFormatException {
        try {
            makeGarbage();
        } catch (Throwable t) {
            t.printStackTrace();
            try {
                Thread.sleep(200); 
            } catch (Throwable t2) {}
        }

        return null;
    }

    private static Instrumentation savedInstrumentation;

    public static void premain(String agentArguments, Instrumentation instrumentation) {
        System.out.println("ClassFileTransformer.premain() is called");
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
}
