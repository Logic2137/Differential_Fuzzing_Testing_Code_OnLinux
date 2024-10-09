



package compiler.arraycopy;

import java.util.Arrays;

public class TestIllegalArrayCopyBeforeInfiniteLoop {
    private static char src[] = new char[10];
    private static int count = 0;
    private static final int iter = 10_000;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < iter; ++i) {
            foo();
        }
        if (count != iter) {
            throw new RuntimeException("test failed");
        }
    }

    static void foo() {
        try {
            Arrays.copyOfRange(src, -1, 128);
            do {
            } while (true);
        } catch (ArrayIndexOutOfBoundsException ex) {
            count++;
        }
    }
}
