



package compiler.loopopts;

public class TestSplitIfUnswitchedLoopsEliminated {

    static class A {
        int f;
    }

    static A aa = new A();
    static A aaa = new A();

    static int test_helper(int stop, boolean unswitch) {
        A a = null;
        for (int i = 3; i < 10; i++) {
            if (unswitch) {
                a = null;
            } else {
                a = aa;
                int v = a.f;
            }
        }
        if (stop != 4) {
            a = aaa;
        }
        if (a != null) {
            return a.f;
        }
        return 0;
    }

    static int test(boolean unswitch) {
        int stop = 1;
        for (; stop < 3; stop *= 4) {
        }
        return test_helper(stop, unswitch);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            test_helper(10, i%2 == 0);
            test(i%2 == 0);
        }
    }
}
