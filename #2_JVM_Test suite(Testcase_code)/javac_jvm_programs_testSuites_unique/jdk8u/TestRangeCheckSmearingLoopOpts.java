public class TestRangeCheckSmearingLoopOpts {

    static int dummy;

    static int m1(int[] array, int i) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (; ; ) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (; ; ) {
                if (array[i] < 0) {
                    break;
                }
                i++;
            }
            if ((i % 2) == 0) {
                if ((array[i] % 2) == 0) {
                    dummy = i;
                }
            }
            if (array[i - 1] == 9) {
                int res = array[i - 3];
                res += array[i];
                res += array[i - 2];
                return res;
            }
            i++;
        }
    }

    static public void main(String[] args) {
        int[] array = { 0, 1, 2, -3, 4, 5, -2, 7, 8, 9, -1 };
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            m1(array, 0);
        }
        array[0] = -1;
        try {
            m1(array, 0);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
    }
}
