import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.channels.FileChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import static java.nio.file.StandardOpenOption.*;

public class BlockDeviceSize {

    private static final String BLK_FNAME = "/dev/sda1";

    private static final Path BLK_PATH = Paths.get(BLK_FNAME);

    public static void main(String[] args) throws Throwable {
        try (FileChannel ch = FileChannel.open(BLK_PATH, READ);
            RandomAccessFile file = new RandomAccessFile(BLK_FNAME, "r")) {
            long size1 = ch.size();
            long size2 = file.length();
            if (size1 != size2) {
                throw new RuntimeException("size differs when retrieved" + " in different ways: " + size1 + " != " + size2);
            }
            System.out.println("OK");
        } catch (NoSuchFileException nsfe) {
            System.err.println("File " + BLK_FNAME + " not found." + " Skipping test");
        } catch (AccessDeniedException ade) {
            System.err.println("Access to " + BLK_FNAME + " is denied." + " Run test as root.");
        }
    }
}
