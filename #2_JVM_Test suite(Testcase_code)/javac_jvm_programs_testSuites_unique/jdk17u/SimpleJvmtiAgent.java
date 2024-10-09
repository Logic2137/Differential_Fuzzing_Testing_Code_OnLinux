import java.lang.instrument.*;

public class SimpleJvmtiAgent {

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("attach succeeded (args: \"" + agentArgs + "\")");
    }
}
