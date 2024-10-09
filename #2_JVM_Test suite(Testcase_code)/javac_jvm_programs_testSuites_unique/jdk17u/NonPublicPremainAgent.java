import java.lang.RuntimeException;
import java.lang.instrument.Instrumentation;

public class NonPublicPremainAgent {

    static void premain(String agentArgs, Instrumentation inst) {
        throw new RuntimeException("premain: NonPublicPremainAgent was not expected to be loaded");
    }
}
