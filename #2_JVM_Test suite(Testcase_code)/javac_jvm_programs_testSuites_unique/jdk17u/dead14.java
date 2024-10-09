
package vm.compiler.jbe.dead.dead14;

public class dead14 {

    int SIZE = 30;

    public static void main(String[] args) {
        dead14 dce = new dead14();
        System.out.println("f()=" + dce.f() + "; fopt()=" + dce.fopt());
        if (dce.f() == dce.fopt()) {
            System.out.println("Test dead14 Passed.");
        } else {
            throw new Error("Test dead14 Failed: f()=" + dce.f() + " != fopt()=" + dce.fopt());
        }
    }

    int f() {
        int[] a = new int[SIZE];
        int j = 1;
        if (j != 0)
            a[0] = 0;
        if (j != 0)
            a[1] = a[0] + 1;
        if (j != 0)
            a[2] = a[1] + 2;
        if (j != 0)
            a[3] = a[2] + 3;
        if (j != 0)
            a[4] = a[3] + 4;
        if (j != 0)
            a[5] = a[4] + 5;
        if (j != 0)
            a[6] = a[5] + 6;
        if (j != 0)
            a[7] = a[6] + 7;
        if (j != 0)
            a[8] = a[7] + 8;
        if (j != 0)
            a[9] = a[8] + 9;
        if (j != 0)
            a[10] = a[9] + 10;
        if (j != 0)
            a[11] = a[10] + 11;
        if (j != 0)
            a[12] = a[11] + 12;
        if (j != 0)
            a[13] = a[12] + 13;
        if (j != 0)
            a[14] = a[13] + 14;
        if (j != 0)
            a[15] = a[14] + 15;
        if (j != 0)
            a[16] = a[15] + 16;
        if (j != 0)
            a[17] = a[16] + 17;
        if (j != 0)
            a[18] = a[17] + 18;
        if (j != 0)
            a[19] = a[18] + 19;
        if (j != 0)
            a[20] = a[19] + 20;
        if (j != 0)
            a[21] = a[20] + 21;
        if (j != 0)
            a[22] = a[21] + 22;
        if (j != 0)
            a[23] = a[22] + 23;
        if (j != 0)
            a[24] = a[23] + 24;
        if (j != 0)
            a[25] = a[24] + 25;
        if (j != 0)
            a[26] = a[25] + 26;
        if (j != 0)
            a[27] = a[26] + 27;
        if (j != 0)
            a[28] = a[27] + 28;
        if (j != 0)
            a[29] = a[28] + 29;
        return a[1];
    }

    int fopt() {
        int[] a = new int[SIZE];
        int j = 1;
        if (j != 0)
            a[0] = 0;
        if (j != 0)
            a[1] = a[0] + 1;
        return a[1];
    }
}
