import java.lang.instrument.Instrumentation;

public class RedefineSubclassWithTwoInterfacesAgent {

    private static Instrumentation instrumentation;

    private RedefineSubclassWithTwoInterfacesAgent() {
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from " + "RedefineSubclassWithTwoInterfacesAgent!");
        System.out.println("isRedefineClassesSupported()=" + inst.isRedefineClassesSupported());
        instrumentation = inst;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
