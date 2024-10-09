
package org.openjdk.bench.java.util.concurrent;



public class Problem {

    

    private final int[] data;
    private final int size;

    public Problem(int size) {
        this.size = size;
        data = new int[size];
    }

    public long solve() {
        return solve(0, size);
    }

    public long solve(int l, int r) {
        long sum = 0;
        for (int c = l; c < r; c++) {
            int v = hash(data[c]);
            if (filter(v)) {
                sum += v;
            }
        }
        return sum;
    }

    public int size() {
        return size;
    }

    public static int hash(int x) {
        x ^= (x << 21);
        x ^= (x >>> 31);
        x ^= (x << 4);
        return x;
    }

    public static boolean filter(int i) {
        return ((i & 0b101) == 0);
    }

}
