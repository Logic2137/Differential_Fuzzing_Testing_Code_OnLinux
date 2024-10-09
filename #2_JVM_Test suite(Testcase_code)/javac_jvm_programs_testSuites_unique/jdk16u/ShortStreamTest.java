



import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ShortStreamTest {
    public static void main(String[] args) throws IOException {
        TestCase[]  tests = createTests();

        for (TestCase t : tests) {
            t.test();
        }
    }

    private static abstract class TestCase {
        abstract void testRead(ImageInputStream iis) throws IOException;

        public void test() {
            boolean gotException = false;

            ImageInputStream iis = createShortStream();

            try {
                testRead(iis);
            } catch (IOException e) {
                e.printStackTrace(System.out);
                gotException = true;
            }

            if (!gotException) {
                throw new RuntimeException("Test failed.");
            }
            System.out.println("Test PASSED");
        }
    }


    private static ImageInputStream createShortStream() {
        try {
            byte[] integerTestArray = new byte[] { 80 };
            ByteArrayInputStream bais = new ByteArrayInputStream(integerTestArray);

            return ImageIO.createImageInputStream(bais);
        } catch (IOException e) {
            return null;
        }
    }

    private static TestCase[] createTests() {
        return new TestCase[]{
                new TestCase() {
                    @Override
                    void testRead(ImageInputStream iis) throws IOException {
                        iis.readInt();
                    }
                },
                new TestCase() {
                    @Override
                    void testRead(ImageInputStream iis) throws IOException {
                        iis.readShort();
                    }
                },
                new TestCase() {
                    @Override
                    void testRead(ImageInputStream iis) throws IOException {
                        iis.readDouble();
                    }
                },
                new TestCase() {
                    @Override
                    void testRead(ImageInputStream iis) throws IOException {
                        iis.readFloat();
                    }
                },
                new TestCase() {
                    @Override
                    void testRead(ImageInputStream iis) throws IOException {
                        iis.readLong();
                    }
                },
                new TestCase() {
                    @Override
                    void testRead(ImageInputStream iis) throws IOException {
                        iis.readUnsignedInt();
                    }
                },
                new TestCase() {
                    @Override
                    void testRead(ImageInputStream iis) throws IOException {
                        iis.readUnsignedShort();
                    }
                }
        };
    }
}
