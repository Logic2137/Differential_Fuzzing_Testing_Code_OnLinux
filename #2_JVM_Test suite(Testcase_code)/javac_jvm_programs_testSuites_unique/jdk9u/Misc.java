




package jdk.test.lib.hprof.util;
import java.util.*;




public class Misc {

    private static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7',
                                     '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public final static String toHex(int addr) {
        char[] buf = new char[8];
        int i = 0;
        for (int s = 28; s >= 0; s -= 4) {
            buf[i++] = digits[(addr >> s) & 0xf];
        }
        return "0x" + new String(buf);
    }

    public final static String toHex(long addr) {
        return "0x" + Long.toHexString(addr);
    }

    public final static long parseHex(String value) {
        long result = 0;
        if (value.length() < 2 || value.charAt(0) != '0' ||
            value.charAt(1) != 'x') {
            return -1L;
        }
        for(int i = 2; i < value.length(); i++) {
            result *= 16;
            char ch = value.charAt(i);
            if (ch >= '0' && ch <= '9') {
                result += (ch - '0');
            } else if (ch >= 'a' && ch <= 'f') {
                result += (ch - 'a') + 10;
            } else if (ch >= 'A' && ch <= 'F') {
                result += (ch - 'A') + 10;
            } else {
                throw new NumberFormatException("" + ch
                                        + " is not a valid hex digit");
            }
        }
        return result;
    }

    public static String encodeHtml(String str) {
        final int len = str.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (ch == '<') {
                sb.append("&lt;");
            } else if (ch == '>') {
                sb.append("&gt;");
            } else if (ch == '"') {
                sb.append("&quot;");
            } else if (ch == '\'') {
                sb.append("&#039;");
            } else if (ch == '&') {
                sb.append("&amp;");
            } else if (ch < ' ') {
                sb.append("&#").append((int)ch).append(';');
            } else {
                int c = (ch & 0xFFFF);
                if (c > 127) {
                    sb.append("&#").append(c).append(';');
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }
}
