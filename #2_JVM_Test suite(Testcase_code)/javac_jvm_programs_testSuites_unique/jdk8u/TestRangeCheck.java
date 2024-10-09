public class TestRangeCheck {

    public static void main(String[] args) {
        try {
            test();
            throw new AssertionError("Expected ArrayIndexOutOfBoundsException was not thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Expected ArrayIndexOutOfBoundsException was thrown");
        }
    }

    private static void test() {
        int[] arr = new int[1];
        int result = 1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
        }
        if (result > 0 && arr[~result] > 0) {
            arr[~result] = 0;
        }
    }
}
