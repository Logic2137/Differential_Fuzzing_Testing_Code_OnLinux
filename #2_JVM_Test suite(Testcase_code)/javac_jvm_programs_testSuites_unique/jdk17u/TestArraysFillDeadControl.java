
package compiler.loopopts;

import java.util.Arrays;

public class TestArraysFillDeadControl {

    static void dont_inline() {
    }

    static int i = 1;

    public static void main(String[] args) {
        for (int j = 0; j < 200000; j++) {
            int[] a = new int[2];
            int b = i;
            Arrays.fill(a, 1);
            Arrays.fill(a, 1 + b);
            dont_inline();
        }
    }
}
