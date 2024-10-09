



package compiler.loopopts;

public class TestCMovWithOpaque {

    public static void test(int array[]) {
        for (int i = 1; i < 8; i += 3) {
            for (int j = 0; j < 4; ++j) {
                switch (i % 4) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    array[j] += 42;
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] array = new int[4];
        for (int i = 0; i < 20_000; ++i) {
            test(array);
        }
    }
}
