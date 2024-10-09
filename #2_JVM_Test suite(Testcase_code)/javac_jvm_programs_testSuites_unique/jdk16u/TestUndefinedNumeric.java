



public class TestUndefinedNumeric {
    static int endValue = 0xFFFF;

    public static void main(String[] args) {
        for (int ch = 0x0000; ch <= 0xFFFF; ch++) {
            if (!Character.isDefined((char)ch) &&
                    Character.getNumericValue((char)ch) != -1) {
                throw new RuntimeException("Char value " + Integer.toHexString((char)ch));

            }

        }
        System.out.println("Passed.");

    }
}
