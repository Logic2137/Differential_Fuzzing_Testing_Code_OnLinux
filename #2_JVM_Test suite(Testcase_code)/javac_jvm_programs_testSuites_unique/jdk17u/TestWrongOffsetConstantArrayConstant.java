public class TestWrongOffsetConstantArrayConstant {

    private static volatile char volatileField;

    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            if (test('o') != 'o') {
                throw new RuntimeException("bad result");
            }
        }
    }

    static final byte[] str = { 'f', 'o' };

    private static char test(char b1) {
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                for (int l = 0; l < 3; l++) {
                }
            }
        }
        int i = 0;
        for (; i < str.length; i++) {
            final byte b = str[i];
            if (b == b1) {
                break;
            }
        }
        final char c = (char) (str[i] & 0xff);
        volatileField = c;
        final char c2 = (char) (str[i] & 0xff);
        return c2;
    }
}
