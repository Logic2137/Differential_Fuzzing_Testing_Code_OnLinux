public class TestSplitIfPinnedCMove {

    static void not_inlined() {
    }

    static class A {

        A(int f) {
            this.f = f;
        }

        int f;
    }

    static int test(int i1, int i3, A a1, A a2) {
        int res = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    res++;
                }
            }
        }
        res += a1.f + a2.f;
        boolean f2 = false;
        if (i1 > 0) {
            not_inlined();
            f2 = true;
        }
        res += (i3 > 0 ? a1 : a2).f;
        if (f2) {
            not_inlined();
            res += 42;
        }
        if (i3 > 0) {
            res++;
        }
        return res;
    }

    public static void main(String[] args) {
        A a = new A(42);
        for (int i = 0; i < 20_000; i++) {
            test((i % 2) == 0 ? 0 : 1, (i % 2) == 0 ? 0 : 1, a, a);
        }
    }
}
