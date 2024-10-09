public class TestArrayCopy6769124 {

    public static void main(String[] args) {
        int k = 1 << 31;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int j = 0; j < 1000000; j++) {
            int i = -1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            while (i < 10) {
                i++;
            }
            int m = k * i;
            int[] O1 = new int[20];
            int[] O2 = new int[20];
            System.arraycopy(O1, i, O2, i, 1);
            System.arraycopy(O1, m, O2, m, 1);
        }
    }
}
