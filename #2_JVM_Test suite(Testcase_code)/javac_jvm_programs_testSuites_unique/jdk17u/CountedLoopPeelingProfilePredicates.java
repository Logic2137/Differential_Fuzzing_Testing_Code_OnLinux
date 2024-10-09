import java.util.Arrays;

public class CountedLoopPeelingProfilePredicates {

    public static void main(String[] args) {
        int stop = 2;
        boolean[] flags1 = new boolean[stop];
        flags1[stop - 1] = true;
        boolean[] flags2 = new boolean[stop];
        flags2[0] = true;
        boolean[] flags3 = new boolean[100];
        Arrays.fill(flags3, true);
        flags3[0] = false;
        for (int i = 0; i < 20_000; i++) {
            test_helper(stop, flags1, false);
            test_helper(stop, flags2, false);
            test_helper(stop, flags2, false);
        }
        for (int i = 0; i < 20_000; i++) {
            test(stop, flags1, false, flags3, 1);
            test(stop, flags2, false, flags3, 1);
            test(stop, flags2, false, flags3, 1);
        }
    }

    private static void test(int stop, boolean[] flags1, boolean flag2, boolean[] flags3, int inc) {
        for (int j = 0; j < 100; j += inc) {
            if (flags3[j]) {
                test_helper(stop, flags1, flag2);
            }
        }
    }

    private static void test_helper(int stop, boolean[] flags1, boolean flag2) {
        for (int i = 0; i < stop; i++) {
            if (flags1[i]) {
                return;
            }
            if (flag2) {
            }
        }
    }
}
