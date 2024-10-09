
package compiler.loopopts;

public class TestOptimizeFillWithStripMinedLoop {

    class Wrap {

        public int value;

        public Wrap(int v) {
            value = v;
        }
    }

    public static int size = 1024;

    public static int[] ia = new int[size];

    public static void main(String[] args) throws Exception {
        TestOptimizeFillWithStripMinedLoop m = new TestOptimizeFillWithStripMinedLoop();
        m.test();
    }

    public void test() throws Exception {
        for (int i = 0; i < 20_000; i++) {
            Wrap obj = null;
            if (i % 113 != 0) {
                obj = new Wrap(i);
            }
            foo(obj);
        }
    }

    public int foo(Wrap obj) throws Exception {
        boolean condition = false;
        int first = -1;
        if (obj == null) {
            condition = true;
            first = 24;
        }
        for (int i = 0; i < size; i++) {
            ia[i] = condition ? first : obj.value;
        }
        return 0;
    }
}
