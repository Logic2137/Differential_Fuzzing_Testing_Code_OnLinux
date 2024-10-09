
package vm.compiler.jbe.hoist.hoist04;

public class hoist04 {

    int LEN = 5000;

    double[] a = new double[LEN];

    double[] aopt = new double[LEN];

    boolean bool_val = true;

    int i1 = 1, i2 = 2, i3 = 3, i4 = 4, i5 = 5, i6 = 6, i7 = 7, i8 = 8, i9 = 9;

    public static void main(String[] args) {
        hoist04 hst = new hoist04();
        hst.f();
        hst.fopt();
        if (hst.eCheck()) {
            System.out.println("Test hoist04 Passed.");
        } else {
            throw new Error("Test hoist04 Failed.");
        }
    }

    void f() {
        int i = 0;
        do a[i++] = bool_val ? (double) (i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9) : (double) (i1 * i2 * i3 * i4 * i5 * i6 * i7 * i8 * i9); while (i < a.length);
    }

    void fopt() {
        int i = 0;
        int t = bool_val ? (i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9) : (i1 * i2 * i3 * i4 * i5 * i6 * i7 * i8 * i9);
        do aopt[i++] = (double) t; while (i < aopt.length);
    }

    boolean eCheck() {
        for (int i = 0; i < a.length; i++) if (a[i] != aopt[i]) {
            System.out.println("a[" + i + "]=" + a[i] + "; aopt[" + i + "]=" + aopt[i]);
            return false;
        }
        return true;
    }
}
