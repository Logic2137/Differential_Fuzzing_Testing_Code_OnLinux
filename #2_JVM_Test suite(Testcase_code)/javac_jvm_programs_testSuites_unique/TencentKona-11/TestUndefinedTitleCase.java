


public class TestUndefinedTitleCase {
    static int endCharValue = 0xFFFF;

    public static void main(String[] args) {
        for(int ch=0x0000; ch <= endCharValue; ch++) {
            if (!Character.isDefined((char)ch) && Character.toTitleCase((char)ch) != (char)ch) {
                throw new RuntimeException("Char value " + Integer.toHexString((char)ch));
            }
        }
        System.out.println("Passed");
    }
}
