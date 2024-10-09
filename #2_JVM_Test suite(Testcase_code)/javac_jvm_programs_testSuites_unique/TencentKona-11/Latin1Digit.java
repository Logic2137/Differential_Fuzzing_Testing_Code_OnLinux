



public class Latin1Digit {

    public static void main(String[] args) throws Exception {
        for (int ch = 0; ch < 256; ++ch) {
            for (int radix = -256; radix <= 256; ++radix) {
                test(ch, radix);
            }
            test(ch, Integer.MIN_VALUE);
            test(ch, Integer.MAX_VALUE);
        }
    }

    static void test(int ch, int radix) throws Exception {
        int d1 = Character.digit(ch, radix);
        int d2 = canonicalDigit(ch, radix);
        if (d1 != d2) {
            throw new Exception("Wrong result for char="
                    + ch + " (" + (char)ch + "), radix="
                    + radix + "; " + d1 + " != " + d2);
        }
    }

    
    static int canonicalDigit(int ch, int radix) {
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
            return -1;
        }
        if (ch >= '0' && ch <= '9' && ch < (radix + '0')) {
            return ch - '0';
        }
        if (ch >= 'A' && ch <= 'Z' && ch < (radix + 'A' - 10)) {
            return ch - 'A' + 10;
        }
        if (ch >= 'a' && ch <= 'z' && ch < (radix + 'a' - 10)) {
            return ch - 'a' + 10;
        }
        return -1;
    }
}
