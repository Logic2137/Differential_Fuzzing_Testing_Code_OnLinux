import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;

public class ReadIntoReadOnlyBuffer {

    private static final String THE_STRING = "123";

    public static void main(String[] args) throws Exception {
        CharBuffer buf = CharBuffer.allocate(8).asReadOnlyBuffer();
        StringReader r = new StringReader(THE_STRING);
        read(r, buf);
        buf = ByteBuffer.allocateDirect(16).asCharBuffer().asReadOnlyBuffer();
        r = new StringReader(THE_STRING);
        read(r, buf);
    }

    private static void read(Reader r, CharBuffer b) throws IOException {
        try {
            r.read(b);
            throw new RuntimeException("ReadOnlyBufferException expected");
        } catch (ReadOnlyBufferException expected) {
        }
        char[] c = new char[3];
        int n = r.read(c);
        if (n != c.length) {
            throw new RuntimeException("Expected " + c.length + ", got " + n);
        }
        String s = new String(c);
        if (!s.equals(THE_STRING)) {
            throw new RuntimeException("Expected " + THE_STRING + ", got " + s);
        }
    }
}
