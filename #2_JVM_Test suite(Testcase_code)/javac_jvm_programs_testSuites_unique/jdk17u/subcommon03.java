
package vm.compiler.jbe.subcommon.subcommon03;

public class subcommon03 {

    int k, m, n;

    float a, b, c, d, x;

    float[] arr = new float[100];

    float[] arr_opt = new float[100];

    float[][] arr1 = new float[10][10];

    float[][] arr1_opt = new float[10][10];

    public static void main(String[] args) {
        subcommon03 sce = new subcommon03();
        sce.init();
        sce.un_optimized();
        sce.hand_optimized();
        sce.mat();
        if (sce.eCheck()) {
            System.out.println("Test subcommon03 Passed.");
        } else {
            throw new Error("Test subcommon03 Failed.");
        }
    }

    void init() {
        for (int i = 0; i < 10; i++) {
            arr[i] = (float) 1E-2;
            arr_opt[i] = (float) 1E-2;
        }
        for (m = 0; m < 10; m++) {
            arr1[0][m] = arr[m];
            arr1_opt[0][m] = arr_opt[m];
        }
    }

    void mat() {
        for (k = 1; k < 10; k++) {
            n = k * 10;
            for (m = 0; m < 10; m++) {
                arr[n + m] = (float) Math.exp((double) arr[m]);
                arr1[k][m] = (float) (arr[m] * 1 / Math.PI);
                arr_opt[n + m] = (float) Math.exp((double) arr_opt[m]);
                arr1_opt[k][m] = (float) (arr_opt[m] * 1 / Math.PI);
            }
        }
    }

    void un_optimized() {
        c = (float) 1.123456789;
        d = (float) 1.010101012;
        a = (float) ((c * Math.sqrt(d * 2.0)) / (2.0 * d));
        b = (float) ((c / Math.sqrt(d * 2.0)) / (2.0 * d));
        System.out.print("a=" + a + ";  b=" + b);
        c = arr[0] / (arr[0] * arr[0] + arr[1] * arr[1]);
        d = arr[1] * (arr[0] * arr[0] + arr[1] * arr[1]);
        System.out.println(";  c=" + c + ";  d=" + d);
        k = 0;
        float t1 = arr[k];
        float t2 = arr[k] * 2;
        arr[2] = a;
        arr[3] = b;
        arr[4] = c;
        arr[5] = d;
        arr[8] = b / c;
        arr[9] = c - a;
        c = t2 / t1 * b / a;
        x = (float) (d * 2.0);
        d = t2 / t1 * b / a;
        arr[6] = c;
        arr[7] = d;
    }

    void hand_optimized() {
        c = (float) 1.123456789;
        d = (float) 1.010101012;
        x = d * (float) 2.0;
        double t1 = Math.sqrt((double) x);
        double t2 = 1.0 / (double) x;
        a = (float) (c * t1 * t2);
        b = (float) (c / t1 * t2);
        System.out.print("a_opt=" + a + ";  b_opt=" + b);
        t1 = (double) arr_opt[0];
        t2 = (double) arr_opt[1];
        double t3 = 1.0 / (t1 * t1 + t2 * t2);
        c = (float) t1 * (float) t3;
        d = (float) t2 / (float) t3;
        System.out.println(";  c_opt=" + c + ";  d_opt=" + d);
        t2 = t1 * 2;
        arr_opt[2] = a;
        arr_opt[3] = b;
        arr_opt[4] = c;
        arr_opt[5] = d;
        arr_opt[8] = b / c;
        arr_opt[9] = c - a;
        c = (float) t2 / (float) t1 * b / a;
        d = c;
        arr_opt[6] = c;
        arr_opt[7] = d;
    }

    boolean eCheck() {
        boolean st = true;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != arr_opt[i]) {
                System.out.println(">>Bad output: arr[" + i + "]=" + arr[i] + "; arr_opt[" + i + "]=" + arr_opt[i]);
                st = false;
            }
        }
        for (int i = 0; i < arr1.length; i++) for (int j = 0; j < arr1[i].length; j++) {
            if (arr1[i][j] != arr1_opt[i][j]) {
                System.out.println(">>Bad output: arr[" + i + "][" + j + "]=" + arr1[i][j] + "; arr_opt[" + i + "][" + j + "]=" + arr1_opt[i][j]);
                st = false;
            }
        }
        return st;
    }
}
