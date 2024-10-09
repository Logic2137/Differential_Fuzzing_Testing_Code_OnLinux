import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class WriteBitsTest {

    private static void verify(ImageOutputStream ios, long expstreampos, int expbitoffset) throws IOException, RuntimeException {
        long actstreampos = ios.getStreamPosition();
        int actbitoffset = ios.getBitOffset();
        if ((actstreampos != expstreampos) || (actbitoffset != expbitoffset)) {
            System.err.println("Expected stream position: " + expstreampos + " Actual: " + actstreampos);
            System.err.println("Expected bit offset: " + expbitoffset + " Actual: " + actbitoffset);
            throw new RuntimeException("Test failed.");
        }
    }

    public static void main(String[] argv) throws RuntimeException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        MemoryCacheImageOutputStream mcios = new MemoryCacheImageOutputStream(ostream);
        try {
            long streampos = 0;
            int bitoffset = 0;
            mcios.setBitOffset(bitoffset);
            verify(mcios, streampos, bitoffset);
            bitoffset = 3;
            mcios.setBitOffset(bitoffset);
            verify(mcios, streampos, bitoffset);
            for (int incr = 3; incr <= 15; incr += 12) {
                for (int i = 0; i < 64; i += incr) {
                    mcios.writeBits(10, incr);
                    bitoffset += incr;
                    if (bitoffset > 7) {
                        int stroffset = bitoffset / 8;
                        bitoffset = bitoffset % 8;
                        streampos += stroffset;
                    }
                    verify(mcios, streampos, bitoffset);
                }
            }
            byte[] bytearr = new byte[2];
            mcios.seek(2);
            mcios.setBitOffset(3);
            int numread = mcios.read(bytearr, 0, 2);
            if (numread != 2) {
                throw new RuntimeException("Error in mcios.read([BII)I");
            }
            verify(mcios, 4, 0);
            mcios.setBitOffset(3);
            mcios.read();
            verify(mcios, 5, 0);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException: " + e);
        }
    }
}
