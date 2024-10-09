public class TestImpossibleIV {

    static private void testMethod() {
        int sum = 0;
        for (int i = 100000; i >= 0; i--) {
            sum += Integer.MIN_VALUE;
        }
    }

    public static void main(String[] args) {
        testMethod();
    }
}
