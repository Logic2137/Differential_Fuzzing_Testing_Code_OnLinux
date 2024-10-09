
package compiler.c2;

public class Test6695810 {

    Test6695810 _t;

    static void test(Test6695810 t1, Test6695810 t2) {
        if (t2 != null)
            t1._t = t2;
        if (t2 != null)
            t1._t = t2;
    }

    public static void main(String[] args) {
        Test6695810 t = new Test6695810();
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 100; j++) {
                test(t, t);
            }
            test(t, null);
        }
        for (int i = 0; i < 10000; i++) {
            test(t, t);
        }
        test(t, null);
    }
}
