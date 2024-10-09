
import java.lang.instrument.Instrumentation;

public class SimpleAgent {
    public static void premain(String agentArg, Instrumentation instrumentation) throws Exception {
        System.out.println("inside SimpleAgent");
        
        if (agentArg != null && agentArg.equals("OldSuper")) {
            Class<?> cls = Class.forName("OldSuper", true, ClassLoader.getSystemClassLoader());
        }
    }
}
