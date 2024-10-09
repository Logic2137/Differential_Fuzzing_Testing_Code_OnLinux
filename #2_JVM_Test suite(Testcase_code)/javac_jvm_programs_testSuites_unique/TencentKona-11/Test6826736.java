



package compiler.runtime;

public class Test6826736 {
    int[] arr;
    int[] arr2;
    int test(int r) {
        for (int i = 0; i < 100; i++) {
            for (int j = i; j < 100; j++) {
               int a = 0;
               for (long k = 0; k < 100; k++) {
                  a += k;
               }
               if (arr != null)
                   a = arr[j];
               r += a;
            }
        }
        return r;
    }

    public static void main(String[] args) {
        int r = 0;
        Test6826736 t = new Test6826736();
        for (int i = 0; i < 100; i++) {
            t.arr = new int[100];
            r = t.test(r);
        }
        System.out.println("Warmup 1 is done.");
        for (int i = 0; i < 100; i++) {
            t.arr = null;
            r = t.test(r);
        }
        System.out.println("Warmup 2 is done.");
        for (int i = 0; i < 100; i++) {
            t.arr = new int[100];
            r = t.test(r);
        }
        System.out.println("Warmup is done.");
        for (int i = 0; i < 100; i++) {
            t.arr = new int[1000000];
            t.arr = null;
            r = t.test(r);
        }
    }
}
