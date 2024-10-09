



public class TestDeadCountedLoop {
    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            test(true, new int[10], false, 0, 1);
            test(false, new int[10], false, 0, 1);
        }
    }

    private static int test(boolean flag, int[] array2, boolean flag2, int start, int stop) {
        if (array2 == null) {
        }
        int[] array;
        if (flag) {
            array = new int[1];
        } else {
            array = new int[2];
        }
        int len = array.length;
        int v = 1;
        for (int j = start; j < stop; j++) {
            for (int i = 0; i < len; i++) {
                if (i > 0) {
                    if (flag2) {
                        break;
                    }
                    v *= array2[i + j];
                }
            }
        }

        return v;
    }
}
