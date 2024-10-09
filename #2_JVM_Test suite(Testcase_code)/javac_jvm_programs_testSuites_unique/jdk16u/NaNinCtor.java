



import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

public class NaNinCtor {

    public static void main(String[] args) throws Throwable {
        Charset ascii = Charset.forName("US-ASCII");

        
        new MyDecoder(ascii, 0.5f, 1.5f);

        
        test(() -> new MyDecoder(ascii, 0.0f, 1.0f));
        test(() -> new MyDecoder(ascii, 1.0f, 0.0f));
        test(() -> new MyDecoder(ascii, -1.0f, 1.0f));
        test(() -> new MyDecoder(ascii, 1.0f, -1.0f));
        test(() -> new MyDecoder(ascii, Float.NaN, 1.0f));
        test(() -> new MyDecoder(ascii, 1.0f, Float.NaN));
        test(() -> new MyDecoder(ascii, 1.5f, 0.5f));
    }

    static void test(Runnable r) {
        try {
            r.run();
            throw new RuntimeException("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException expected) {
        }
    }

    static class MyDecoder extends CharsetDecoder {
        public MyDecoder(Charset cs, float avg, float max) {
            super(cs, avg, max);
        }
        protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
            return null;
        }
    }
}
