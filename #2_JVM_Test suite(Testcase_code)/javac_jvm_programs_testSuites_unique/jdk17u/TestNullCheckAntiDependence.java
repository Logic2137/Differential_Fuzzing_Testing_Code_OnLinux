
package compiler.uncommontrap;

public class TestNullCheckAntiDependence {

    private static class MyInteger {

        int val;
    }

    private static MyInteger foo = new MyInteger();

    private static MyInteger bar = new MyInteger();

    private static MyInteger[] global = { new MyInteger() };

    static void test1() {
        for (int i = 0; i < 1; i++) {
            foo.val = -bar.val;
            for (int k = 0; k < 10; k++) {
                foo.val = 0;
            }
        }
    }

    static void test2(MyInteger a, MyInteger b) {
        global[0].val = a.val + b.val * 31;
        global[0].val = 0;
        return;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10_000; i++) {
            test1();
        }
        for (int i = 0; i < 10_000; i++) {
            test2(new MyInteger(), new MyInteger());
        }
    }
}
