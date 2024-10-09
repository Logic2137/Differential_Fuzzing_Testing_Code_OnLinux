import java.nio.charset.Charset;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class TestMS950 {

    private final static String[] MS950B2C = new String[] { "0xF9FA  0x256D", "0xF9FB  0x256E", "0xF9FC  0x2570", "0xF9FD  0x256F", "0xA2CC  0x5341", "0xA2CE  0x5345", "0xF9F9  0x2550", "0xF9E9  0x255E", "0xF9EA  0x256A", "0xF9EB  0x2561", "0xA27E  0x256D", "0xA2A1  0x256E", "0xA2A2  0x2570", "0xA2A3  0x256F", "0xA451  0x5341", "0xA4CA  0x5345", "0xA2A4  0x2550", "0xA2A5  0x255E", "0xA2A6  0x256A", "0xA2A7  0x2561" };

    private final static String[] MS950C2B = new String[] { "0xF9FA -> u256D -> 0xA27E", "0xF9FB -> u256E -> 0xA2A1", "0xF9FC -> u2570 -> 0xA2A2", "0xF9FD -> u256F -> 0xA2A3", "0xA2CC -> u5341 -> 0xA451", "0xA2CE -> u5345 -> 0xA4CA", "0xA2A4 -> u2550 -> 0xF9F9", "0xA2A5 -> u255E -> 0xF9E9", "0xA2A6 -> u256A -> 0xF9EA", "0xA2A7 -> u2561 -> 0xF9EB" };

    private static byte[] hex2ba(String s) {
        byte[] ba;
        if (s.startsWith("0x")) {
            s = s.substring(2);
        }
        try {
            ByteBuffer bb = ByteBuffer.allocate((int) (s.length() / 2));
            StringBuilder sb = new StringBuilder(s.substring(0, bb.limit() * 2));
            while (sb.length() > 0) {
                bb.put((byte) Integer.parseInt(sb.substring(0, 2), 16));
                sb.delete(0, 2);
            }
            ba = bb.array();
        } catch (NumberFormatException nfe) {
            ba = new byte[0];
        }
        return ba;
    }

    private static String hex2s(String s) {
        char[] ca;
        if (s.startsWith("0x")) {
            s = s.substring(2);
        } else if (s.startsWith("u")) {
            s = s.substring(1);
        }
        try {
            CharBuffer cb = CharBuffer.allocate((int) (s.length() / 4));
            StringBuilder sb = new StringBuilder(s.substring(0, cb.limit() * 4));
            while (sb.length() > 0) {
                cb.put((char) Integer.parseInt(sb.substring(0, 4), 16));
                sb.delete(0, 4);
            }
            ca = cb.array();
        } catch (NumberFormatException nfe) {
            ca = new char[0];
        }
        return new String(ca);
    }

    public static void main(String[] args) throws Exception {
        Charset cs = Charset.forName("MS950");
        int diffs = 0;
        for (int i = 0; i < MS950B2C.length; ++i) {
            String[] sa = MS950B2C[i].split("\\s+");
            String s = new String(hex2ba(sa[0]), cs);
            if (!s.equals(hex2s(sa[1]))) {
                ++diffs;
                System.out.printf("b2c: %s, expected:%s, result:0x", sa[0], sa[1]);
                for (char ch : s.toCharArray()) {
                    System.out.printf("%04X", (int) ch);
                }
                System.out.println();
            }
        }
        for (int i = 0; i < MS950C2B.length; ++i) {
            String[] sa = MS950C2B[i].split("\\s+->\\s+");
            byte[] ba = hex2s(sa[1]).getBytes(cs);
            if (!Arrays.equals(ba, hex2ba(sa[2]))) {
                ++diffs;
                System.out.printf("c2b: %s, expected:%s, result:0x", sa[1], sa[2]);
                for (byte b : ba) {
                    System.out.printf("%02X", (int) b & 0xFF);
                }
                System.out.println();
            }
        }
        if (diffs > 0) {
            throw new Exception("Failed");
        }
    }
}
