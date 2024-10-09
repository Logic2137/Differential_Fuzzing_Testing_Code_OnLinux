
package compiler.codegen;

public class BadLogicCode {

    static int[] values = { Integer.MIN_VALUE, -1, 0, 1, 4, 16, 31, 32, 33, Integer.MAX_VALUE };

    static char[][] b = { null, new char[32] };

    static boolean nullPtr = false, indexOutBnd = false;

    static boolean indexOutBnd2 = false;

    public static void main(String[] args) throws Exception {
        int i = 1, j = 4, k = 9;
        nullPtr = (b[i] == null);
        int bufLen = nullPtr ? 0 : b[i].length;
        indexOutBnd = (values[j] < 0) || (values[j] > bufLen) || (values[k] < 0) || ((values[j] + values[k]) > bufLen) || ((values[j] + values[k]) < 0);
        indexOutBnd2 = (values[j] < 0);
        indexOutBnd2 = indexOutBnd2 || (values[j] > bufLen);
        indexOutBnd2 = indexOutBnd2 || (values[k] < 0);
        indexOutBnd2 = indexOutBnd2 || ((values[j] + values[k]) > bufLen);
        indexOutBnd2 = indexOutBnd2 || ((values[j] + values[k]) < 0);
        if (indexOutBnd != indexOutBnd2)
            throw new Error("logic expression generate different results");
    }
}
