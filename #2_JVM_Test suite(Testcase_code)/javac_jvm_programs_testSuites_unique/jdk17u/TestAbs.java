
package compiler.c2;

public class TestAbs {

    public static void test() {
        Math.abs(1);
        Math.abs(-1);
        Math.abs(1L);
        Math.abs(-1L);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            test();
        }
    }
}
