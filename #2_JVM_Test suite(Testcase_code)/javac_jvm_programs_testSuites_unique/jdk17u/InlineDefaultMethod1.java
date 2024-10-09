
package compiler.inlining;

public class InlineDefaultMethod1 {

    interface I1 {

        default public int m() {
            return 0;
        }
    }

    interface I2 extends I1 {

        default public int m() {
            return 1;
        }
    }

    static abstract class A implements I1 {
    }

    static class B extends A implements I2 {
    }

    public static void test(A obj) {
        int id = obj.m();
        if (id != 1) {
            throw new AssertionError("Called wrong method: 1 != " + id);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        test(new B());
        System.out.println("TEST PASSED");
    }
}
