
package vm.compiler.jbe.subcommon.subcommon02;

import java.io.*;

public class subcommon02 {

    int LEN = 5000;

    int WIDTH = 20;

    int ngrt10000 = 0;

    int ngrtO10000 = 0;

    int ngrt1000 = 0;

    int ngrtO1000 = 0;

    int ngrt100 = 0;

    int ngrtO100 = 0;

    int nsmet100 = 0;

    int nsmetO100 = 0;

    double[][] a = new double[LEN][WIDTH];

    double[][] aopt = new double[LEN][WIDTH];

    public static void main(String[] args) {
        subcommon02 sce = new subcommon02();
        sce.f();
        sce.fopt();
        if (sce.eCheck()) {
            System.out.println("Test subcommon02 Passed.");
        } else {
            throw new Error("Test subcommon02 Failed.");
        }
    }

    double nPower(int x, int pwr) {
        return Math.pow(x, pwr);
    }

    void f() {
        for (int x = 0; x < LEN; x++) {
            for (int n = 0; n < WIDTH; n++) {
                if (nPower(x, n) > 10000) {
                    a[x][n] = nPower(x, n);
                    ngrt10000++;
                } else if (nPower(x, n) > 1000) {
                    a[x][n] = nPower(x, n);
                    ngrt1000++;
                } else if (nPower(x, n) > 100) {
                    a[x][n] = nPower(x, n);
                    ngrt100++;
                } else {
                    a[x][n] = nPower(x, n);
                    nsmet100++;
                }
            }
        }
    }

    void fopt() {
        for (int x = 0; x < LEN; x++) {
            for (int n = 0; n < WIDTH; n++) {
                double tmp = nPower(x, n);
                aopt[x][n] = tmp;
                if (tmp > 10000)
                    ngrtO10000++;
                else if (tmp > 1000)
                    ngrtO1000++;
                else if (tmp > 100)
                    ngrtO100++;
                else
                    nsmetO100++;
            }
        }
    }

    boolean eCheck() {
        boolean r = true;
        for (int i = 0; i < LEN; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (ulpDiff(a[i][j], aopt[i][j]) > 1) {
                    System.out.println("Bad result: a[" + i + "," + j + "]=" + a[i][j] + "; aopt[" + i + "," + j + "]=" + aopt[i][j]);
                    r = false;
                }
            }
        }
        if ((ngrt10000 != ngrtO10000) || (ngrt1000 != ngrtO1000) || (ngrt100 != ngrtO100) || (nsmetO100 != nsmetO100)) {
            System.out.println("Bad result: number of elements found is not matching");
            r = false;
        }
        return r;
    }

    public static double nextAfter(double base, double direction) {
        if (Double.isNaN(base) || Double.isNaN(direction)) {
            return base + direction;
        } else if (base == direction) {
            return base;
        } else {
            long doppelganger;
            double result = 0.0;
            doppelganger = Double.doubleToLongBits(base + 0.0);
            if (direction > base) {
                if (doppelganger >= 0)
                    result = Double.longBitsToDouble(++doppelganger);
                else
                    result = Double.longBitsToDouble(--doppelganger);
            } else if (direction < base) {
                if (doppelganger > 0)
                    result = Double.longBitsToDouble(--doppelganger);
                else if (doppelganger < 0)
                    result = Double.longBitsToDouble(++doppelganger);
                else
                    result = -Double.MIN_VALUE;
            }
            return result;
        }
    }

    static double ulp(double d) {
        d = Math.abs(d);
        if (Double.isNaN(d))
            return Double.NaN;
        else if (Double.isInfinite(d))
            return Double.POSITIVE_INFINITY;
        else {
            if (d == Double.MAX_VALUE)
                return 1.9958403095347198E292;
            else
                return nextAfter(d, Double.POSITIVE_INFINITY) - d;
        }
    }

    static double ulpDiff(double ref, double test) {
        double ulp;
        if (Double.isInfinite(ref)) {
            if (ref == test)
                return 0.0;
            else
                return Double.POSITIVE_INFINITY;
        } else if (Double.isNaN(ref)) {
            if (Double.isNaN(test))
                return 0.0;
            else
                return Double.NaN;
        } else {
            ulp = ulp(ref);
            return (test - ref) / ulp;
        }
    }
}
