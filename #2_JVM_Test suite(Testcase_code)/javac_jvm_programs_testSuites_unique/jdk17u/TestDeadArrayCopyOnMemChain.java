
package compiler.arraycopy;

public class TestDeadArrayCopyOnMemChain {

    static class A {

        int f;
    }

    static void test_helper(Object o) {
    }

    static void test(int src_off, boolean flag) {
        Object[] dst = new Object[10];
        Object[] src = new Object[10];
        try {
            System.arraycopy(src, src_off, dst, 0, 10);
        } catch (IndexOutOfBoundsException ioobe) {
            if (flag) {
                test_helper(src);
            }
        }
    }

    static public void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            test((i % 2) == 0 ? 0 : -1, false);
        }
    }
}
