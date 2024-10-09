public class Test8011706 {

    int[] array;

    void m(boolean test, int[] array1, int[] array2) {
        int i = 0;
        if (test) {
            array = array1;
        } else {
            array = array2;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (true) {
            int v = array[i];
            i++;
            if (i >= 10)
                return;
        }
    }

    static public void main(String[] args) {
        int[] new_array = new int[10];
        Test8011706 ti = new Test8011706();
        boolean failed = false;
        try {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < 10000; i++) {
                ti.array = null;
                ti.m(true, new_array, new_array);
            }
        } catch (NullPointerException ex) {
            throw new RuntimeException("TEST FAILED", ex);
        }
        System.out.println("TEST PASSED");
    }
}
