public class TransformUtil {

    public static final String BeforePattern = "this-should-be-transformed";

    public static final String AfterPattern = "this-has-been--transformed";

    public static final String ParentCheckPattern = "parent-transform-check: ";

    public static final String ChildCheckPattern = "child-transform-check: ";

    public static int replace(byte[] buff, String from, String to) {
        if (to.length() != from.length()) {
            throw new RuntimeException("bad strings");
        }
        byte[] f = asciibytes(from);
        byte[] t = asciibytes(to);
        byte f0 = f[0];
        int numReplaced = 0;
        int max = buff.length - f.length;
        for (int i = 0; i < max; ) {
            if (buff[i] == f0 && replace(buff, f, t, i)) {
                i += f.length;
                numReplaced++;
            } else {
                i++;
            }
        }
        return numReplaced;
    }

    public static boolean replace(byte[] buff, byte[] f, byte[] t, int i) {
        for (int x = 0; x < f.length; x++) {
            if (buff[x + i] != f[x]) {
                return false;
            }
        }
        for (int x = 0; x < f.length; x++) {
            buff[x + i] = t[x];
        }
        return true;
    }

    static byte[] asciibytes(String s) {
        byte[] b = new byte[s.length()];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) s.charAt(i);
        }
        return b;
    }
}
