



public class TestUndefinedIdentifierStartPart {
    static int endValue = 0xFFFF;

    public static void main(String[] args) {
        for (int ch=0x0000; ch <= endValue; ch++) {
            if (!Character.isDefined((char)ch) &&
                    (Character.isJavaIdentifierStart((char)ch) ||
                     Character.isJavaIdentifierPart((char)ch) ||
                     Character.isUnicodeIdentifierStart((char)ch) ||
                     Character.isUnicodeIdentifierPart((char)ch))) {
                throw new RuntimeException("Char value " + Integer.toHexString((char)ch));
            }
        }
        System.out.println("Passed");
    }
}
