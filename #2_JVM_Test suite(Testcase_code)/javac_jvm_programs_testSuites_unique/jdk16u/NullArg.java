



import java.util.Formatter;
import java.util.Locale;

public class NullArg {

    public static void main(String [] args) {
        char[] cs = new char[] {
            'b', 'B', 'h', 'H', 's', 'S', 'c', 'C', 'd', 'o', 'x', 'X',
            'e', 'E', 'f', 'g', 'G', 'a', 'A', 't', 'T',
        };
        char[] tcs = new char[] {
            'H', 'I', 'k', 'l', 'l', 'M', 'S', 'L', 'N', 'p', 'z', 'Z', 's',
            'Q', 'B', 'b', 'h', 'A', 'a', 'C', 'Y', 'y', 'j', 'm', 'd', 'e',
            'R', 'T', 'r', 'D', 'F', 'c'
        };
        for (char c : cs) {
            String expected = (c == 'b' || c == 'B') ? "false" : "null";
            if (Character.isUpperCase(c)) {
                expected = expected.toUpperCase(Locale.ROOT);
            }
            if (c == 't' || c == 'T') {
                for (char ct : tcs) {
                    if (!String.format("%" + c + ct, null).equals(expected)) {
                        throw new RuntimeException("%t" + ct + "null check failed.");
                    }
                }
            } else {
                if (!String.format("%" + c , null).equals(expected)) {
                    throw new RuntimeException("%" + c + "null check failed.");
                }
            }
        }
    }
}
