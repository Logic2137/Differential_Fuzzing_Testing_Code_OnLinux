

package compiler.c2;


public class TestSkipLongToIntCast {

    public static int[] pos = {0, 1, 2, 3};
    public static int[] neg = {0, -1, -2, -3};
    public static int[] max = {2147483647, 2147483646, 2147483645, 2147483644};
    public static int[] min = {-2147483648, -2147483647, -2147483646, -2147483645};
    public static int[] out = {(int)2147483648L, (int)-2147483649L, (int)Long.MAX_VALUE, (int)Long.MIN_VALUE};

    
    
    public static void cmplTest() throws Exception {
        
        if (pos[0] != 0L) { throw new Exception("pos[0] is " + pos[0]); }
        if (pos[1] != 1L) { throw new Exception("pos[1] is " + pos[1]); }
        if (pos[2] != 2L) { throw new Exception("pos[2] is " + pos[2]); }
        if (pos[3] != 3L) { throw new Exception("pos[3] is " + pos[3]); }

        if (neg[0] != -0L) { throw new Exception("neg[0] is " + neg[0]); }
        if (neg[1] != -1L) { throw new Exception("neg[1] is " + neg[1]); }
        if (neg[2] != -2L) { throw new Exception("neg[2] is " + neg[2]); }
        if (neg[3] != -3L) { throw new Exception("neg[3] is " + neg[3]); }

        
        if (max[0] != 2147483647L) { throw new Exception("max[0] is " + max[0]); }
        if (max[1] != 2147483646L) { throw new Exception("max[1] is " + max[1]); }
        if (max[2] != 2147483645L) { throw new Exception("max[2] is " + max[2]); }
        if (max[3] != 2147483644L) { throw new Exception("max[3] is " + max[3]); }

        if (min[0] != -2147483648L) { throw new Exception("min[0] is " + min[0]); }
        if (min[1] != -2147483647L) { throw new Exception("min[1] is " + min[1]); }
        if (min[2] != -2147483646L) { throw new Exception("min[2] is " + min[2]); }
        if (min[3] != -2147483645L) { throw new Exception("min[3] is " + min[3]); }

        
        if (out[0] == 2147483648L)  { throw new Exception("out[0] is " + out[0]); }
        if (out[1] == -2147483649L) { throw new Exception("out[1] is " + out[1]); }
        if (out[2] == Long.MAX_VALUE) { throw new Exception("out[2] is " + out[2]); }
        if (out[3] == Long.MIN_VALUE) { throw new Exception("out[3] is " + out[3]); }
    }

    
    public static void cmplTest_LHS() throws Exception {
        
        if (0L != pos[0]) { throw new Exception("LHS: pos[0] is " + pos[0]); }
        if (1L != pos[1]) { throw new Exception("LHS: pos[1] is " + pos[1]); }
        if (2L != pos[2]) { throw new Exception("LHS: pos[2] is " + pos[2]); }
        if (3L != pos[3]) { throw new Exception("LHS: pos[3] is " + pos[3]); }

        if (-0L != neg[0]) { throw new Exception("LHS: neg[0] is " + neg[0]); }
        if (-1L != neg[1]) { throw new Exception("LHS: neg[1] is " + neg[1]); }
        if (-2L != neg[2]) { throw new Exception("LHS: neg[2] is " + neg[2]); }
        if (-3L != neg[3]) { throw new Exception("LHS: neg[3] is " + neg[3]); }

        
        if (2147483647L != max[0]) { throw new Exception("LHS: max[0] is " + max[0]); }
        if (2147483646L != max[1]) { throw new Exception("LHS: max[1] is " + max[1]); }
        if (2147483645L != max[2]) { throw new Exception("LHS: max[2] is " + max[2]); }
        if (2147483644L != max[3]) { throw new Exception("LHS: max[3] is " + max[3]); }

        if (-2147483648L != min[0]) { throw new Exception("LHS: min[0] is " + min[0]); }
        if (-2147483647L != min[1]) { throw new Exception("LHS: min[1] is " + min[1]); }
        if (-2147483646L != min[2]) { throw new Exception("LHS: min[2] is " + min[2]); }
        if (-2147483645L != min[3]) { throw new Exception("LHS: min[3] is " + min[3]); }

        
        if (2147483648L == out[0])  { throw new Exception("LHS: out[0] is " + out[0]); }
        if (-2147483649L == out[1]) { throw new Exception("LHS: out[1] is " + out[1]); }
        if (Long.MAX_VALUE == out[2]) { throw new Exception("LHS: out[2] is " + out[2]); }
        if (Long.MIN_VALUE == out[3]) { throw new Exception("LHS: out[3] is " + out[3]); }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100_000; i++) {
            cmplTest();
            cmplTest_LHS();
        }
    }
}
