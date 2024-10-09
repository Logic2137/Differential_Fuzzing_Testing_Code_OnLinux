
package compiler.c2;

import java.util.function.Supplier;

public class TestIfWithDeadRegion {

    static String msg;

    static String getString(String s, int i) {
        String current = s + String.valueOf(i);
        System.nanoTime();
        return current;
    }

    static void test(Supplier<String> supplier) {
        msg = supplier.get();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20_000; ++i) {
            test(() -> getString("Test1", 42));
            test(() -> getString("Test2", 42));
        }
    }
}
