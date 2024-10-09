



import java.lang.instrument.Instrumentation;

public class InheritAgent1000 extends InheritAgent1000Super {

    

    
}

class InheritAgent1000Super {

    

    
    public static void premain (String agentArgs, Instrumentation instArg) {
        System.out.println("Hello from Double-Arg InheritAgent1000Super!");
        throw new Error("ERROR: THIS AGENT SHOULD NOT HAVE BEEN CALLED.");
    }
}
