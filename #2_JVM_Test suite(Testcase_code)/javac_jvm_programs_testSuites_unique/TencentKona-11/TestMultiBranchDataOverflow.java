



package compiler.profiling;

public class TestMultiBranchDataOverflow {

    public static int test(int val, long max) {
        int res = 0;
        for (long l = 0; l < max; ++l) {
            switch (val) {
            case 0:
                return 0;
            case 42:
                res++;
                break;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        
        
        long max = Integer.MAX_VALUE + 100_000L;
        test(42, max);

        
        for (int i = 0; i < 10_000; ++i) {
            test(42, 1);
        }
    }
}
