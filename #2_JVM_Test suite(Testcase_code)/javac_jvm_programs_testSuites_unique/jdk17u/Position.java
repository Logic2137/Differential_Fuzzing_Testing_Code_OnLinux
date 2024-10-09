import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;
import java.nio.charset.Charset;
import java.util.Random;

public class Position {

    private static final Charset ISO8859_1 = Charset.forName("8859_1");

    private static final Random generator = new Random();

    public static void main(String[] args) throws Exception {
        Path blah = Files.createTempFile("blah", null);
        blah.toFile().deleteOnExit();
        initTestFile(blah);
        for (int i = 0; i < 10; i++) {
            try (FileChannel fc = (generator.nextBoolean()) ? FileChannel.open(blah, READ) : new FileInputStream(blah.toFile()).getChannel()) {
                for (int j = 0; j < 100; j++) {
                    long newPos = generator.nextInt(1000);
                    fc.position(newPos);
                    if (fc.position() != newPos)
                        throw new RuntimeException("Position failed");
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            try (FileChannel fc = (generator.nextBoolean()) ? FileChannel.open(blah, APPEND) : new FileOutputStream(blah.toFile(), true).getChannel()) {
                for (int j = 0; j < 10; j++) {
                    if (fc.position() != fc.size())
                        throw new RuntimeException("Position expected to be size");
                    byte[] buf = new byte[generator.nextInt(100)];
                    fc.write(ByteBuffer.wrap(buf));
                }
            }
        }
        Files.delete(blah);
    }

    private static void initTestFile(Path blah) throws IOException {
        try (BufferedWriter awriter = Files.newBufferedWriter(blah, ISO8859_1)) {
            for (int i = 0; i < 4000; i++) {
                String number = new Integer(i).toString();
                for (int h = 0; h < 4 - number.length(); h++) awriter.write("0");
                awriter.write("" + i);
                awriter.newLine();
            }
        }
    }
}
