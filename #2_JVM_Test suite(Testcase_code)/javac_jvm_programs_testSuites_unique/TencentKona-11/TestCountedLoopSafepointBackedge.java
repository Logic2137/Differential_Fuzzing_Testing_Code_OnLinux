



public class TestCountedLoopSafepointBackedge {
    static void test(int[] arr, int inc) {
        int i = 0;
        for (;;) {
            for (int j = 0; j < 10; j++);
            arr[i] = i;
            i++;
            if (i >= 100) {
                break;
            }
            for (int j = 0; j < 10; j++);
        }
    }

    static public void main(String[] args) {
        int[] arr = new int[100];
        for (int i = 0; i < 20000; i++) {
             test(arr, 1);
        }
    }
}
