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

abstract class A implements I1 {
}

class B extends A implements I2 {
}

public class InlineDefaultMethod1 {

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
