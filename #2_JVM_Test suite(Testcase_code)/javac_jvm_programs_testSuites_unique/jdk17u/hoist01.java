
package vm.compiler.jbe.hoist.hoist01;

public class hoist01 {

    final static double PI = 3.14159;

    int LEN = 5000;

    double[] a = new double[LEN];

    double[] aopt = new double[LEN];

    int i1 = 1, i2 = 2, i3 = 3, i4 = 4, i5 = 5, i6 = 6, i7 = 7, i8 = 8;

    int i9 = 9, i10 = 10, i11 = 11, i12 = 12, i13 = 13, i14 = 14, i15 = 15;

    public static void main(String[] args) {
        hoist01 hst = new hoist01();
        hst.f();
        hst.fopt();
        if (hst.eCheck()) {
            System.out.println("Test hoist01 Passed.");
        } else {
            throw new Error("Test hoist01 Failed.");
        }
    }

    void f() {
        for (int i = 1; i < a.length; i++) {
            a[0] = Math.sin(2 * PI * Math.pow(1 / PI, 3));
            a[i] = a[0] + (i1 + i2) * PI + i3 + i4 + i5 / i6 * i7 + i9 % i8 + i10 * (i11 * i12) + i13 + i14 + i15;
        }
    }

    void fopt() {
        double t;
        aopt[0] = Math.sin(2 * PI * Math.pow(1 / PI, 3));
        t = aopt[0] + (i1 + i2) * PI + i3 + i4 + i5 / i6 * i7 + i9 % i8 + i10 * (i11 * i12) + i13 + i14 + i15;
        for (int i = 1; i < aopt.length; i++) {
            aopt[i] = t;
        }
    }

    boolean eCheck() {
        for (int i = 0; i < a.length; i++) if (a[i] != aopt[i])
            return false;
        return true;
    }
}
