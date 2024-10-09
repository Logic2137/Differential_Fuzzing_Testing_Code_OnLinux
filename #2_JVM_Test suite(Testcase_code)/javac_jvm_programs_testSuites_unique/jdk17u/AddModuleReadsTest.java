
package MyPackage;

import java.io.PrintStream;
import java.lang.instrument.Instrumentation;

public class AddModuleReadsTest {

    static {
        try {
            System.loadLibrary("AddModuleReadsTest");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println("Could not load AddModuleReadsTest library");
            System.err.println("java.library.path: " + System.getProperty("java.library.path"));
            throw ule;
        }
    }

    native static int check(Module unnamed, Module base, Module instrument);

    public static void main(String[] args) {
        Module unnamed = AddModuleReadsTest.class.getClassLoader().getUnnamedModule();
        Module base = Object.class.getModule();
        Module instrument = Instrumentation.class.getModule();
        int status = check(unnamed, base, instrument);
        if (status != 0) {
            throw new RuntimeException("Non-zero status returned from the agent: " + status);
        }
    }
}
