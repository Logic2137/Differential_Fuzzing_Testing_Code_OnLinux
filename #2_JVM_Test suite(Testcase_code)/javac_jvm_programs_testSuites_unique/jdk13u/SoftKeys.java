import java.util.*;

public class SoftKeys {

    private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 512 * 1024; j++) {
                    new Locale(langForInt(j), "", "");
                }
            }
        } catch (OutOfMemoryError e) {
            System.gc();
        }
    }

    private static String langForInt(int val) {
        StringBuilder buf = new StringBuilder(4);
        buf.append(CHARS[(val >> 12) & 0xF]);
        buf.append(CHARS[(val >> 8) & 0xF]);
        buf.append(CHARS[(val >> 4) & 0xF]);
        buf.append(CHARS[(val >> 0) & 0xF]);
        return buf.toString();
    }
}
