


interface I {
    default int m() { return 0; }
}

class A implements I {}

class C extends A { }
class D extends A { public int m() { return 1; } }

public class DefaultAndConcreteMethodsCHA {
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
