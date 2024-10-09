
package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;

public class redefclass001r {

    public int checkIt(boolean DEBUG_MODE, PrintStream out) {
        if (DEBUG_MODE)
            out.println("OLD redefclass001r: inside the checkIt()");
        return 19;
    }
}
