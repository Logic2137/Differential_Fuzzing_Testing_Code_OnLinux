
package compiler.loopopts;

public class Test6860469 {

    private static final int H = 16;

    private static final int F = 9;

    static int[] fl = new int[1 << F];

    static int C(int ll, int f) {
        int max = -1;
        int min = H + 1;
        if (ll != 0) {
            if (ll < min) {
                min = ll;
            }
            if (ll > max) {
                max = ll;
            }
        }
        if (f > max) {
            f = max;
        }
        if (min > f) {
            min = f;
        }
        for (int mc = 1 >> max - f; mc <= 0; mc++) {
            int i = mc << (32 - f);
            fl[i] = max;
        }
        return min;
    }

    public static void main(String[] argv) {
        C(0, 10);
    }
}
