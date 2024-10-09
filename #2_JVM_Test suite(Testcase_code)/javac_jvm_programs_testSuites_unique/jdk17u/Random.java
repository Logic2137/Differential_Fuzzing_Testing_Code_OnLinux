
package vm.compiler.share;

public class Random {

    private int current;

    public Random(int init) {
        this.current = init;
    }

    private static long BASE = 1003001;

    public int nextInt() {
        current = (int) ((long) current * current % BASE - 1);
        return current;
    }

    public int nextInt(int n) {
        return nextInt() % n;
    }

    public static void main(String[] args) {
        Random r = new Random(11);
        int[] a = new int[100];
        for (int i = 0; i < 1000; i++) {
            a[r.nextInt(100)]++;
        }
        for (int i = 0; i < 100; i++) {
            System.out.println(times(a[i]));
        }
    }

    public static String times(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("*");
        }
        return sb.toString();
    }
}
