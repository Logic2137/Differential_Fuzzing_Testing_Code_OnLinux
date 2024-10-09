
package compiler.codegen;

public class LoadWithMask2 {

    static int x;

    static long foo1() {
        return x & 0xfffffffe;
    }

    static long foo2() {
        return x & 0xff000000;
    }

    static long foo3() {
        return x & 0x8abcdef1;
    }

    public static void main(String[] args) {
        x = -1;
        long l = 0;
        for (int i = 0; i < 100000; ++i) {
            l = foo1() & foo2() & foo3();
        }
        if (l > 0) {
            System.out.println("FAILED");
            System.exit(97);
        }
        System.out.println("PASSED");
    }
}
