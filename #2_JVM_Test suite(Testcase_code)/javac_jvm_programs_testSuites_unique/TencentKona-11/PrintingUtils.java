

package jdk.test.lib.jittester.utils;

public class PrintingUtils {

    static public String align(int l) {
        String shift = "";
        for (int i = 0; i < l; i++) {
            shift += "    ";
        }
        return shift;
    }
}
