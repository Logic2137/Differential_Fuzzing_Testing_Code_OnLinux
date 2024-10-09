



package compiler.inlining;

public class DefaultAndConcreteMethodsCHA {
    interface I {
        default int m() { return 0; }
    }

    static class A implements I {}

    static class C extends A { }
    static class D extends A { public int m() { return 1; } }

    public static int test(A obj) {
        return obj.m();
    }
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            int idC = test(new C());
            if (idC != 0) {
                throw new Error("C.m didn't invoke I.m: id "+idC);
            }

            int idD = test(new D());
            if (idD != 1) {
                throw new Error("D.m didn't invoke D.m: id "+idD);
            }
        }

    }
}
