



package compiler.c2;

public class TestDeadPhiMergeMemLoop {

    public static boolean bFld = false;
    public static double dArrFld[] = new double[400];

    public static void main(String[] strArr) {
        int x = 1;
        int i = 0;
        int iArr[] = new int[400];
        dontInline();

        if (bFld) {
            x += x;
        } else if (bFld) {
            float f = 1;
            while (++f < 132) {
                if (bFld) {
                    dArrFld[5] = 3;
                    for (i = (int)(f); i < 12; i++) {
                    }
                }
            }
        }
    }

    
    public static void dontInline() {
    }
}
