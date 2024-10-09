



import java.lang.instrument.Instrumentation;


class NonPublicAgent {

    
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain: NonPublicAgent was loaded");
    }
}
