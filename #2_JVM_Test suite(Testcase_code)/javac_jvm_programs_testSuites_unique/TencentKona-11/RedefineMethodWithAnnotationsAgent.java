

import java.lang.instrument.Instrumentation;

public class RedefineMethodWithAnnotationsAgent {
    private static Instrumentation instrumentation;

    private RedefineMethodWithAnnotationsAgent() {}

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from RedefineMethodWithAnnotationsAgent!");
        System.out.println("isRedefineClassesSupported()=" +
                inst.isRedefineClassesSupported());

        instrumentation = inst;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
