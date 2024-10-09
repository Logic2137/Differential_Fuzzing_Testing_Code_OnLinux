import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UncheckedIOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ForceException {

    public static void main(String[] args) throws IOException {
        int blockSize = 2048 * 1024;
        int numberOfBlocks = 200;
        int fileLength = numberOfBlocks * blockSize;
        File file = new File(System.getProperty("test.src", "."), "test.dat");
        file.deleteOnExit();
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(fileLength);
            int pos = (numberOfBlocks - 1) * blockSize;
            int size = (int) Math.min(blockSize, fileLength - pos);
            MappedByteBuffer mbb = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, pos, size);
            System.out.printf("Write region 0x%s..0x%s%n", Long.toHexString(pos), Long.toHexString(size));
            for (int k = 0; k < mbb.limit(); k++) {
                mbb.put(k, (byte) 65);
            }
            try {
                System.out.println("Force");
                mbb.force();
            } catch (UncheckedIOException legal) {
                System.out.printf("Caught legal exception %s%n", legal);
                IOException cause = legal.getCause();
                if (cause.getMessage().startsWith("Flush failed")) {
                    throw cause;
                }
            }
            System.out.println("OK");
        }
    }
}
