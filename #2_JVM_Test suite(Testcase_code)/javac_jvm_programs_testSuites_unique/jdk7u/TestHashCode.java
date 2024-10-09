



public class TestHashCode {
    static class A {
        int i;
    }

    static class B extends A {
    }

    static boolean crash = false;

    static A m2() {
        if (crash) {
            return null;
        }
        return new A();
    }

    static int m1(A aa) {
        int res = 0;
        for (int i = 0; i < 10; i++) {
            A a = m2();
            int j = a.i;
            if (aa instanceof B) {
            }
            res += a.hashCode();
        }
        return res;
    }

    public static void main(String[] args) {
        A a = new A();
        for (int i = 0; i < 20000; i++) {
            m1(a);
        }
        crash = true;
        try {
          m1(a);
        } catch (NullPointerException e) {
            System.out.println("Test passed");
        }
    }
}
