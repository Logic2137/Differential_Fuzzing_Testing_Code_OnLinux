
package compiler.arraycopy;

public class TestArrayCopyOverflowArguments {

    static volatile int mod = Integer.MAX_VALUE;

    public static int[] m1(Object src) {
        if (src == null)
            return null;
        int[] dest = new int[10];
        try {
            int pos = 8 + mod + mod;
            int start = 2 + mod + mod;
            int len = 12 + mod + mod;
            System.arraycopy(src, pos, dest, 0, 10);
        } catch (ArrayStoreException npe) {
        }
        return dest;
    }

    static public void main(String[] args) throws Exception {
        int[] src = new int[20];
        for (int i = 0; i < 20; ++i) {
            src[i] = i * (i - 1);
        }
        for (int i = 0; i < 20000; i++) {
            m1(src);
        }
    }
}
