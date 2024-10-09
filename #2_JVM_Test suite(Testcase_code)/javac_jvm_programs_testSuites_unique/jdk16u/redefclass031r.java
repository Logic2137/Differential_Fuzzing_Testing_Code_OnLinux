

package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;

public class redefclass031r {
    public redefclass031r() {
    }

    public int checkIt(PrintStream out, boolean DEBUG_MODE) {
        nativeMethod();
        if (DEBUG_MODE)
            out.println("redefclass031r: inside the checkIt()");
        return 19;
    }

    public static native void nativeMethod();
}
