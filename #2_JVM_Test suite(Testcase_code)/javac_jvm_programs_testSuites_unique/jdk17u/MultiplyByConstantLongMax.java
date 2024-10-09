public class MultiplyByConstantLongMax {

    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            if (test(1) != Long.MAX_VALUE) {
                throw new RuntimeException("incorrect result");
            }
        }
    }

    private static long test(long v) {
        return v * Long.MAX_VALUE;
    }
}
