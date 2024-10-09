
package compiler.uncommontrap;

public class TestNullCheckAntiDependence {

    private static class MyInteger {

        int val;
    }

    private static MyInteger foo = new MyInteger();

    private static MyInteger bar = new MyInteger();

    static void setFooToZero() {
        for (int i = 0; i < 1; i++) {
            foo.val = -bar.val;
            for (int k = 0; k < 10; k++) {
                foo.val = 0;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10_000; i++) {
            setFooToZero();
        }
    }
}
