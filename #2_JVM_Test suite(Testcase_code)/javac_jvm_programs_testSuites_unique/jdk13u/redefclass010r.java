
package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;

public class redefclass010r {

    public int checkIt(PrintStream out, boolean DEBUG_MODE) {
        if (DEBUG_MODE)
            out.println("OLD redefclass010r: inside the checkIt()");
        return 19;
    }

    static double statMethod(int x, int y, int z) {
        double j = 5.0D;
        for (
        int i = 10; 
        i > z; i--) j += x * y;
        return j;
    }

    final void finMethod(char c, long i, int j, long k) {
    }
}
