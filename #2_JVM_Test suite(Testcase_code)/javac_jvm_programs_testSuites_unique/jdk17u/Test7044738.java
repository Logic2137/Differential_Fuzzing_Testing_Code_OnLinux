
package compiler.loopopts;

public class Test7044738 {

    private static final int INITSIZE = 10000;

    public int[] d = { 1, 2, 3, 4 };

    public int i, size;

    private static int iter = 5;

    boolean done() {
        return (--iter > 0);
    }

    public static void main(String[] args) {
        Test7044738 t = new Test7044738();
        t.test();
    }

    int test() {
        while (done()) {
            size = INITSIZE;
            for (i = 0; i < size; i++) {
                d[0] = d[1];
                d[1] = d[2];
                d[2] = d[3];
                d[3] = d[0];
                d[0] = d[1];
                d[1] = d[2];
                d[2] = d[3];
                d[3] = d[0];
                d[0] = d[1];
                d[1] = d[2];
                d[2] = d[3];
                d[3] = d[0];
                d[0] = d[1];
                d[1] = d[2];
                d[2] = d[3];
                d[3] = d[0];
                d[0] = d[1];
                d[1] = d[2];
                d[2] = d[3];
                d[3] = d[0];
                d[0] = d[1];
                d[1] = d[2];
                d[2] = d[3];
                d[3] = d[0];
                d[0] = d[1];
                d[1] = d[2];
                d[2] = d[3];
                d[3] = d[0];
                d[0] = d[1];
                d[1] = d[2];
                d[2] = d[3];
                d[3] = d[0];
            }
            if (d[0] == d[1]) {
                System.out.println("test failed: iter=" + iter + "  i=" + i + " d[] = { " + d[0] + ", " + d[1] + ", " + d[2] + ", " + d[3] + " } ");
                System.exit(97);
            }
        }
        return d[3];
    }
}
