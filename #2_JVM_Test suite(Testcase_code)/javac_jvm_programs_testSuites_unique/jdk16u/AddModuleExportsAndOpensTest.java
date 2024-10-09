

package MyPackage;



import java.io.PrintStream;

public class AddModuleExportsAndOpensTest {

    static {
        try {
            System.loadLibrary("AddModuleExportsAndOpensTest");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println("Could not load AddModuleExportsAndOpensTest library");
            System.err.println("java.library.path: "
                + System.getProperty("java.library.path"));
            throw ule;
        }
    }

    native static int check(Module baseModule, Module thisModule);

    public static void main(String args[]) {
        Module baseModule = Object.class.getModule();
        Module thisModule = AddModuleExportsAndOpensTest.class.getClassLoader().getUnnamedModule();
        int status = check(baseModule, thisModule);
        if (status != 0) {
            throw new RuntimeException("Non-zero status returned from the agent: " + status);
        }
    }
}
