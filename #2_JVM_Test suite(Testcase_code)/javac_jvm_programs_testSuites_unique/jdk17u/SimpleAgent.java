import java.lang.instrument.Instrumentation;

class SimpleAgent {

    public static void premain(String args, Instrumentation inst) {
        System.out.println("in premain");
    }
}
