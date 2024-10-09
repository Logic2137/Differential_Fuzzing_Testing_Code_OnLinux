public class LoadDependsOnIfIdenticalToLoopExit {

    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            test1(false, false);
            test1(true, true);
        }
    }

    private static int test1(boolean flag1, boolean flag2) {
        int res = 1;
        int[] array = new int[10];
        not_inlined(array);
        int i;
        for (i = 0; i < 2000; i++) {
            res *= i;
        }
        if (flag1) {
            if (flag2) {
                res++;
            }
        }
        if (i >= 2000) {
            res *= array[0];
        }
        return res;
    }

    private static void not_inlined(int[] array) {
    }
}
