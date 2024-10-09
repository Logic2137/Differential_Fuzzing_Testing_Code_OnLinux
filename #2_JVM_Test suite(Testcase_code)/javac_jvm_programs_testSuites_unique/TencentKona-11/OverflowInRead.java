



import java.io.StringBufferInputStream;

public class OverflowInRead {
    public static void main(String[] args) throws Exception {
        String s = "_123456789_123456789_123456789_123456789"; 
        try (StringBufferInputStream sbis = new StringBufferInputStream(s)) {
            int len1 = 33;
            byte[] buf1 = new byte[len1];
            if (sbis.read(buf1, 0, len1) != len1)
                throw new Exception("Expected to read " + len1 + " bytes");

            int len2 = Integer.MAX_VALUE - 32;
            byte[] buf2 = new byte[len2];
            int expLen2 = s.length() - len1;
            if (sbis.read(buf2, 0, len2) != expLen2)
                throw new Exception("Expected to read " + expLen2 + " bytes");
        }
    }
}
