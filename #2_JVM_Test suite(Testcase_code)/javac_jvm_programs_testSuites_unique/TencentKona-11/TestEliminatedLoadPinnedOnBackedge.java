



public class TestEliminatedLoadPinnedOnBackedge {
    private static Object field2;

    final static int iters = 2000;

    public static void main(String[] args) {
        boolean[] flags = new boolean[iters];
        for (int i = 0; i < iters; i++) {
            flags[i] = i < iters/2;
        }
        for (int i = 0; i < 20_000; i++) {
            test1(flags);
            test2(flags, 1);
            test3(flags);
            inlined(new Object(), 1);
            inlined(new Object(), 4);
            inlined2(42);
            inlined2(0x42);
        }
    }

    static int field;

    private static int test1(boolean[] flags) {
        int k = 2;
        for (; k < 4; k *= 2) {
        }
        int[] array = new int[10];
        notInlined(array);
        
        
        int v = array[0];
        array[1] = 42;
        
        Object o = new Object();
        inlined(o, k);
        int i = 0;
        for (; ; ) {
            synchronized (array) {
            }
            if (i >= iters) {
                break;
            }
            v = array[0]; 
            if (flags[i]) {
                inlined2(array[1]);
            }
            i++;
        }
        return v;
    }

    private static int test2(boolean[] flags, int d) {
        int k = 2;
        for (; k < 4; k *= 2) {
        }
        int[] array = new int[10];
        notInlined(array);
        int v = array[0];
        array[1] = 42;
        Object o = new Object();
        inlined(o, k);
        int i = 0;
        for (; ; ) {
            synchronized (array) {
            }
            if (d == 0) {}
            if (i >= iters) {
                break;
            }
            v = (array[0] + array[2]) / d;
            if (flags[i]) {
                inlined2(array[1]);
            }
            i++;
        }
        return v;
    }

    private static int test3(boolean[] flags) {
        int k = 2;
        for (; k < 4; k *= 2) {
        }
        int[] array = new int[10];
        notInlined(array);
        int v1 = array[0];
        int v2 = array[2];
        array[1] = 42;
        Object o = new Object();
        inlined(o, k);
        int i = 0;
        for (; ; ) {
            synchronized (array) {
            }
            if (i >= iters) {
                break;
            }
            v1 = array[0];
            v2 = array[2];
            if (flags[i]) {
                inlined2(array[1]);
            }
            i++;
        }
        return v1 + v2;
    }

    private static void inlined2(int i) {
        if (i != 42) {
            field = 42;
        }
    }

    private static void inlined(Object o, int i) {
        if (i != 4) {
            field2 = o;
        }
    }

    private static void notInlined(int[] array) {
        java.util.Arrays.fill(array, 1);
    }
}
