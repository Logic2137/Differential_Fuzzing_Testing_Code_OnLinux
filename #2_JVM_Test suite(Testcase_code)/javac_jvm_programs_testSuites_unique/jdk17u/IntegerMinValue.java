public class IntegerMinValue {

    public void test() {
        int i = Integer.MIN_VALUE;
        String s = "" + i;
        if (!"-2147483648".equals(s)) {
            throw new IllegalStateException("Failed: " + s);
        }
        System.out.println(s);
    }

    public static void main(String[] strArr) {
        IntegerMinValue t = new IntegerMinValue();
        for (int i = 0; i < 100_000; i++) {
            t.test();
        }
    }
}
