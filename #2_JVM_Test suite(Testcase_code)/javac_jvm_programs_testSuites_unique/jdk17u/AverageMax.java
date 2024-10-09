import java.nio.*;
import java.nio.charset.*;

public class AverageMax {

    static abstract class Test {

        public abstract void go() throws Exception;

        Test() throws Exception {
            try {
                go();
            } catch (Exception x) {
                if (x instanceof IllegalArgumentException) {
                    System.err.println("Thrown as expected: " + x);
                    return;
                }
                throw new Exception("Incorrect exception: " + x.getClass().getName(), x);
            }
            throw new Exception("No exception thrown");
        }
    }

    public static void main(String[] args) throws Exception {
        final Charset ascii = Charset.forName("US-ASCII");
        new Test() {

            public void go() throws Exception {
                new CharsetDecoder(ascii, 3.9f, 1.2f) {

                    protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
                        return null;
                    }
                };
            }
        };
        new Test() {

            public void go() throws Exception {
                new CharsetEncoder(ascii, 3.9f, 1.2f) {

                    protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
                        return null;
                    }
                };
            }
        };
    }
}
