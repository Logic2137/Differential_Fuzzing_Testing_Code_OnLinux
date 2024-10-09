
package nsk.jvmti.RedefineClasses;

import java.io.*;

class redefclass017a extends Thread {

    public static boolean testFailed = false;

    public static PrintStream sout;

    public void run() {
        try {
            checkPoint();
            sout.println("# checkPoint() does not throw any exception");
            testFailed = true;
        } catch (NumberFormatException ex) {
        } catch (Exception ex) {
            sout.println("# checkPoint() throws unexpected exception:");
            sout.println("# " + ex);
            testFailed = true;
        }
    }

    private void checkPoint() throws IOException {
        throw new NumberFormatException("redefined");
    }
}
