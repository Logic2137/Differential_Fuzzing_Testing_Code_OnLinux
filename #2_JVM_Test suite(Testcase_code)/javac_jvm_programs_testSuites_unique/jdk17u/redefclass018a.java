
package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;

class redefclass018a extends Thread {

    final static int LOOP_COUNT = 30000;

    public static boolean testFailed = false;

    public static PrintStream sout;

    public void run() {
        try {
            for (int i = 0; ; i++) {
                if (i > LOOP_COUNT) {
                    thrower();
                    break;
                }
            }
            sout.println("# thrower() does not throw any exception");
            testFailed = true;
        } catch (NumberFormatException ex) {
        } catch (Exception ex) {
            sout.println("# thrower() throws unexpected exception:");
            sout.println("# " + ex);
            testFailed = true;
        }
    }

    private void thrower() throws NumberFormatException {
        throw new NumberFormatException("redefined");
    }
}
