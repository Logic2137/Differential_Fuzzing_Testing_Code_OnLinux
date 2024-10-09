import java.util.Arrays;

public class TestShenandoahLRBInOuterStripMinedLoop {

    public static void main(String[] args) {
        A[] array = new A[4000];
        Arrays.fill(array, new A());
        for (int i = 0; i < 20_0000; i++) {
            test(array);
        }
    }

    private static int test(A[] array) {
        A a = null;
        int v = 1;
        A b = null;
        for (int i = 0; i < 2000; i++) {
            a = array[i];
            b = array[2 * i];
            v *= 2;
        }
        return a.f + b.f + v;
    }

    private static class A {

        public int f;
    }
}
