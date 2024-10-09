
package nsk.jvmti.CompiledMethodUnload;

import java.io.*;
import java.math.*;
import java.util.*;

public class compmethunload001u {

    StringBuffer strBuf;

    BigInteger bigInt = BigInteger.ONE;

    int iter;

    public compmethunload001u() {
        strBuf = new StringBuffer(iter);
        this.iter = 100000;
    }

    public void entryMethod() {
        for (int i = 0; i < iter; i++) hotMethod(i);
    }

    void hotMethod(int i) {
        strBuf.append(Integer.toString(i) + " ");
    }

    public void entryNewMethod() {
        BigInteger bigInt2 = new BigInteger("100");
        for (int i = 0; i < iter; i++) newHotMethod(bigInt2);
    }

    void newHotMethod(BigInteger bigInt2) {
        bigInt = bigInt.add(bigInt2);
    }
}
