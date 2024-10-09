

import java.lang.instrument.Instrumentation;

public class RedefineMethodDelInvokeAgent {
    private static Instrumentation instrumentation;

    private RedefineMethodDelInvokeAgent() {
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from RedefineMethodDelInvokeAgent!");
        System.out.println("isRedefineClassesSupported()=" +
            inst.isRedefineClassesSupported());

        instrumentation = inst;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
