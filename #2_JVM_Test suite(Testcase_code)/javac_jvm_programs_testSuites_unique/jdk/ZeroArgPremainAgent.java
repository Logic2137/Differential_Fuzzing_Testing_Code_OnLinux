



public class ZeroArgPremainAgent {

    
    public static void premain () {
        System.out.println("Hello from ZeroArgInheritAgent!");
        throw new Error("ERROR: THIS AGENT SHOULD NOT HAVE BEEN CALLED.");
    }
}
