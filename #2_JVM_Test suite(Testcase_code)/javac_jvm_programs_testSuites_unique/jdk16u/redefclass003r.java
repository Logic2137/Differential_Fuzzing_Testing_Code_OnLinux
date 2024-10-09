

package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;

public class redefclass003r {
    static public byte byteFld = 1;
    static public short shortFld = 2;
    static public int intFld = 3;
    static public long longFld = 4L;
    static public float floatFld = 5.1F;
    static public double doubleFld = 6.0D;
    static public char charFld = 'a';
    static public boolean booleanFld = false;
    static public String stringFld = "OLD redefclass003r";

    public int checkIt(PrintStream out, boolean DEBUG_MODE) {
        if (DEBUG_MODE)
            out.println("OLD redefclass003r: inside the checkIt()");
        return 19;
    }
}
