


package compiler.c2;

public class TestDeadNodeDuringIGVN {
    static int res;

    static void test(int len) {
        int array[] = new int[len];
        for (long l = 0; l < 10; l++) {
            float e = 1;
            do { } while (++e < 2);
            res += l;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 40_000; ++i) {
            res = 0;
            test(1);
            if (res != 45) {
                throw new RuntimeException("Test failed: res = " + res);
            }
        }
    }
}
