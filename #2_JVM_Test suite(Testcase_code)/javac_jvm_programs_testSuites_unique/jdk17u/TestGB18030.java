import java.io.*;
import java.nio.*;
import java.nio.charset.*;

public class TestGB18030 {

    public static void gb18030_1(boolean useDirect) throws Exception {
        for (char ch : new char[] { '\uFFFE', '\uFFFF' }) {
            char[] ca = new char[] { ch };
            Charset cs = Charset.forName("GB18030");
            CharsetEncoder ce = cs.newEncoder();
            CharsetDecoder cd = cs.newDecoder();
            CharBuffer cb = CharBuffer.wrap(ca);
            ByteBuffer bb;
            if (useDirect) {
                bb = ByteBuffer.allocateDirect((int) Math.ceil(ce.maxBytesPerChar()));
            } else {
                bb = ByteBuffer.allocate((int) Math.ceil(ce.maxBytesPerChar()));
            }
            CoderResult cr = ce.encode(cb, bb, true);
            if (!cr.isUnderflow()) {
                throw new RuntimeException(String.format("Encoder Error: \\u%04X: direct=%b: %s", (int) ch, useDirect, cr.toString()));
            }
            bb.position(0);
            cb = CharBuffer.allocate((int) Math.ceil(cd.maxCharsPerByte() * bb.limit()));
            cr = cd.decode(bb, cb, true);
            if (!cr.isUnderflow()) {
                throw new RuntimeException(String.format("Decoder Error: \\u%04X: direct=%b: %s", (int) ch, useDirect, cr.toString()));
            }
            if (ca[0] != cb.get(0)) {
                throw new RuntimeException(String.format("direct=%b: \\u%04X <> \\u%04X", useDirect, (int) ca[0], (int) cb.get(0)));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        gb18030_1(false);
        gb18030_1(true);
    }
}
