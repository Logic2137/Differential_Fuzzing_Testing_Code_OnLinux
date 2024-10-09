
package gc.epsilon;

import java.util.Random;

public class TestRefArrays {

    static long SEED = Long.getLong("seed", System.nanoTime());

    static int COUNT = Integer.getInteger("count", 1000);

    static MyObject[][] arr;

    public static void main(String[] args) throws Exception {
        Random r = new Random(SEED);
        arr = new MyObject[COUNT * 100][];
        for (int c = 0; c < COUNT; c++) {
            arr[c] = new MyObject[c * 100];
            for (int v = 0; v < c; v++) {
                arr[c][v] = new MyObject(r.nextInt());
            }
        }
        r = new Random(SEED);
        for (int c = 0; c < COUNT; c++) {
            MyObject[] b = arr[c];
            if (b.length != (c * 100)) {
                throw new IllegalStateException("Failure: length = " + b.length + ", need = " + (c * 100));
            }
            for (int v = 0; v < c; v++) {
                int actual = b[v].id();
                int expected = r.nextInt();
                if (actual != expected) {
                    throw new IllegalStateException("Failure: expected = " + expected + ", actual = " + actual);
                }
            }
        }
    }

    public static class MyObject {

        int id;

        public MyObject(int id) {
            this.id = id;
        }

        public int id() {
            return id;
        }
    }
}
