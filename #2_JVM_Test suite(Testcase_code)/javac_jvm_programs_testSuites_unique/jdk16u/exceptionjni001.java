

package nsk.share.ExceptionCheckingJniEnv;

import java.io.PrintStream;

public class exceptionjni001 {
    static {
        try {
            System.loadLibrary("exceptionjni001");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println("Could not load exceptionjni001 library");
            System.err.println("java.library.path:"
                + System.getProperty("java.library.path"));
            throw ule;
        }
    }

    
    int anInteger;

    
    native static boolean check();

    public static void main(String args[]) {
        if (!check()) {
          throw new RuntimeException("Problem with ExceptionCheckingJniEnv");
        }
    }
}
