import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class AnonVmClassesDuringDumpTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain pd, byte[] buffer) throws IllegalClassFormatException {
        return null;
    }

    private static Instrumentation savedInstrumentation;

    public static void premain(String agentArguments, Instrumentation instrumentation) {
        System.out.println("ClassFileTransformer.premain() is called");
        instrumentation.addTransformer(new AnonVmClassesDuringDumpTransformer(), true);
        savedInstrumentation = instrumentation;
        Runnable r = () -> {
            System.out.println("Invoked inside a Lambda");
        };
        r.run();
    }

    public static Instrumentation getInstrumentation() {
        return savedInstrumentation;
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        premain(args, inst);
    }
}
