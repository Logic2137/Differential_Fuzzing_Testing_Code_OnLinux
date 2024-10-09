

package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;


public class redefclass008r {
    public int checkIt(PrintStream out, boolean DEBUG_MODE) {
        if (DEBUG_MODE)
            out.println("NEW redefclass008r: inside the checkIt()");
        return 73;
    }


    static int statMethod(int x, int y, int z) {
        int j = 3;

        for (int i=0; i<z; i++) {
            j += x*y;
        }
        return j;
    }

    final void finMethod(long i, int j, long k) {
        long l = 30000L;

        while(true) {
            if (i == 123456789L)
                break;
            j += k*(l-i);
        }
        return;
    }
}
