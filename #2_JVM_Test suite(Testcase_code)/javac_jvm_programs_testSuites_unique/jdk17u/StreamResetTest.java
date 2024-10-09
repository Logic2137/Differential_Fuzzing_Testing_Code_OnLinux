import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStreamImpl;

public class StreamResetTest {

    public static void main(String[] args) {
        IOException expectedException = null;
        TestStream iis = new TestStream();
        ImageReader wbmp = ImageIO.getImageReadersByFormatName("WBMP").next();
        if (wbmp == null) {
            System.out.println("No WBMP reader: skip the test");
            return;
        }
        ImageReaderSpi spi = wbmp.getOriginatingProvider();
        iis.checkPosition();
        try {
            spi.canDecodeInput(iis);
        } catch (IOException e) {
            expectedException = e;
        }
        if (expectedException == null) {
            throw new RuntimeException("Test FAILED: stream was not used");
        }
        iis.checkPosition();
        System.out.println("Test PASSED");
    }

    private static class TestStream extends ImageInputStreamImpl {

        private final int errorPos = 1;

        @Override
        public int read() throws IOException {
            if (streamPos == errorPos) {
                throw new IOException("Test exception");
            }
            streamPos++;
            return 0x03;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            streamPos += len;
            return len;
        }

        public void checkPosition() {
            if (streamPos != 0) {
                throw new RuntimeException("Test FAILED");
            }
        }
    }
}
