
package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;

public class redefclass004r {

    public byte byteFld = 10;

    public short shortFld = 20;

    public int intFld = 30;

    public long longFld = 40L;

    public float floatFld = 50.2F;

    public double doubleFld = 60.3D;

    public char charFld = 'b';

    public boolean booleanFld = true;

    public String stringFld = "OLD redefclass004r";

    public int checkIt(PrintStream out, boolean DEBUG_MODE) {
        if (DEBUG_MODE)
            out.println("OLD redefclass004r: inside the checkIt()");
        return 19;
    }
}
