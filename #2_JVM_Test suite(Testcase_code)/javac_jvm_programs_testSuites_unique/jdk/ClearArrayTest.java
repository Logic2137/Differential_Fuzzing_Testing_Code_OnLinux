



package compiler.codegen;

public class ClearArrayTest {
    static int[] f1;

    private static void test() {
        f1 = new int[8];
    }

    public static void main(String[] args) {
        for (int i=0; i<15000; i++) {
            test();
        }
    }
}
