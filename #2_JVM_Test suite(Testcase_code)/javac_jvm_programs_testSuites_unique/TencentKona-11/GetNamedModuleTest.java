

package MyPackage;



import java.io.PrintStream;

public class GetNamedModuleTest {

    static {
        try {
            System.loadLibrary("GetNamedModuleTest");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println("Could not load GetNamedModuleTest library");
            System.err.println("java.library.path: "
                + System.getProperty("java.library.path"));
            throw ule;
        }
    }

    native static int check();

    public static void main(String args[]) {
        int status = check();
        if (status != 0) {
            throw new RuntimeException("Non-zero status returned from the agent: " + status);
        }
    }
}
