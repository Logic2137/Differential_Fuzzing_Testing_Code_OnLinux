public class InheritAgent0100 extends InheritAgent0100Super {
}

class InheritAgent0100Super {

    public static void premain(String agentArgs) {
        System.out.println("Hello from Single-Arg InheritAgent0100Super!");
        throw new Error("ERROR: THIS AGENT SHOULD NOT HAVE BEEN CALLED.");
    }
}
