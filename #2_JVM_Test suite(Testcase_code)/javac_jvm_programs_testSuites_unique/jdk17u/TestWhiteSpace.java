public class TestWhiteSpace {

    public static void main(String[] args) {
        char[] whiteSpace = { '\u00A0', '\u2007', '\u202F' };
        for (int x = 0; x < whiteSpace.length; x++) {
            if (Character.isWhitespace(whiteSpace[x])) {
                throw new RuntimeException("Invalid whitespace: \\u" + Integer.toString((int) whiteSpace[x], 16));
            }
        }
        System.out.println("Passed.");
    }
}
