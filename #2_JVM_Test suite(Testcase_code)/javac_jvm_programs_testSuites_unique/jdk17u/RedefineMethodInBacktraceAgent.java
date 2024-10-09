import java.lang.instrument.Instrumentation;

public class RedefineMethodInBacktraceAgent {

    private static Instrumentation instrumentation;

    private RedefineMethodInBacktraceAgent() {
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from RedefineMethodInBacktraceAgent!");
        System.out.println("isRedefineClassesSupported()=" + inst.isRedefineClassesSupported());
        instrumentation = inst;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
