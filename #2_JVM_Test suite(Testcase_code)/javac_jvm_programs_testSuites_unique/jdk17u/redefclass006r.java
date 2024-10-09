
package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;

public class redefclass006r {

    public int checkIt(PrintStream out, boolean DEBUG_MODE) {
        if (DEBUG_MODE)
            out.println("redefclass006r: inside the checkIt()");
        return 19;
    }
}
