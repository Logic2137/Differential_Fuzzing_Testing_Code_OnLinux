
package compiler.loopopts;

public class TestPredicateLostDependency {

    static class A {

        int i;
    }

    static class B extends A {
    }

    static boolean crash = false;

    static boolean m2() {
        return crash;
    }

    static int m3(float[] arr) {
        return 0;
    }

    static float m1(A aa) {
        float res = 0;
        float[] arr = new float[10];
        for (int i = 0; i < 10; i++) {
            if (m2()) {
                arr = null;
            }
            m3(arr);
            int j = arr.length;
            int k = 0;
            for (k = 9; k < j; k++) {
            }
            if (k == 10) {
                if (aa instanceof B) {
                }
            }
            res += arr[0];
            res += arr[1];
        }
        return res;
    }

    static public void main(String[] args) {
        A a = new A();
        B b = new B();
        for (int i = 0; i < 20000; i++) {
            m1(a);
        }
        crash = true;
        try {
            m1(a);
        } catch (NullPointerException npe) {
        }
    }
}
