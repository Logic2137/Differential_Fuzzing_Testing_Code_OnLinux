
package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;

public class redefclass009r {

    public int checkIt(PrintStream out, boolean DEBUG_MODE) {
        int i = 234;
        if (DEBUG_MODE)
            out.println("OLD redefclass009r: inside the checkIt()");
        return 19;
    }

    static double statMethod(int x, int y, int z) {
        return 19.73D;
    }

    final void finMethod(char c, long i, int j, long k) {
    }
}
