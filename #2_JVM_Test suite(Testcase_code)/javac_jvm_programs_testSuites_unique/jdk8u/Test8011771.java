public class Test8011771 {

    static void m(int[] a, int[] b, int j) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10; i++) {
            a[i] = i;
        }
        a[j] = 0;
        a[j + 5] = 0;
        b[j + 4] = 0;
    }

    static public void main(String[] args) {
        int[] arr1 = new int[10], arr2 = new int[10];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 5000; i++) {
            m(arr1, arr2, 0);
        }
        try {
            m(new int[1], null, 0);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 5000; i++) {
            m(arr1, arr2, 0);
        }
        boolean success = false;
        try {
            m(arr1, new int[1], 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            success = true;
        }
        if (success) {
            System.out.println("TEST PASSED");
        } else {
            throw new RuntimeException("TEST FAILED: erroneous bound check elimination");
        }
    }
}
