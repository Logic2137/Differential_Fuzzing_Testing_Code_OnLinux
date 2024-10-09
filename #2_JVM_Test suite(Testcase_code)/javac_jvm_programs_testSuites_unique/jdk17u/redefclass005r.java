
package nsk.jvmti.RedefineClasses;

import java.io.PrintStream;

public class redefclass005r {

    static final int intStatFinFld = 10;

    static final long longStatFinFld = 200L;

    static public byte bytePubStatFld = 1;

    static public short shortPubStatFld = 2;

    static public int intPubStatFld = 3;

    static public long longPubStatFld = 4L;

    static public float floatPubStatFld = 5.1F;

    static public double doublePubStatFld = 6.2D;

    static public char charPubStatFld = 'a';

    static public boolean booleanPubStatFld = false;

    static public String strPubStatFld = "static field";

    byte byteFld = 1;

    short shortFld = 2;

    int intFld = 3;

    long longFld = 4L;

    float floatFld = 5.1F;

    double doubleFld = 6.2D;

    char charFld = 'a';

    boolean booleanFld = false;

    String strFld = "instance field";

    public byte bytePubFld = 1;

    public short shortPubFld = 2;

    public int intPubFld = 3;

    public long longPubFld = 4L;

    public float floatPubFld = 5.1F;

    public double doublePubFld = 6.2D;

    public char charPubFld = 'a';

    public boolean booleanPubFld = false;

    public String strPubFld = "public instance field";

    public redefclass005r() {
        this.intFld = -2;
    }

    public redefclass005r(int par) {
        this.intFld = par;
    }

    public int checkIt(PrintStream out, boolean DEBUG_MODE) {
        if (DEBUG_MODE)
            out.println("redefclass005r: inside the checkIt()");
        return 19;
    }

    public static int staticMethod(int j) {
        int i = 1 + j;
        return i;
    }
}
