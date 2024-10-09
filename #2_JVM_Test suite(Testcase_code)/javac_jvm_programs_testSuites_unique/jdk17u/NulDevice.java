import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.*;
import java.util.concurrent.ExecutionException;

public class NulDevice {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        Path path = Path.of("nul");
        try (OutputStream os = Files.newOutputStream(path)) {
            os.write(0x02);
            try (InputStream is = Files.newInputStream(path)) {
                if (is.available() != 0) {
                    throw new RuntimeException("No bytes should be available");
                }
                int aByte = is.read();
                if (aByte != -1) {
                    throw new RuntimeException("Should only read -1 from NUL");
                }
            }
        }
        try (OutputStream os = Files.newOutputStream(path, WRITE)) {
            os.write(0x02);
        }
        try (FileChannel ch = FileChannel.open(path, CREATE, TRUNCATE_EXISTING, WRITE)) {
            byte[] bytes = "Whatever".getBytes();
            ByteBuffer buf = ByteBuffer.allocate(2 * bytes.length);
            buf.put(bytes);
            int nw = ch.write(buf);
            if (nw != bytes.length) {
                throw new RuntimeException("Should write " + bytes.length + " to NUL");
            }
        }
        try (FileChannel ch = FileChannel.open(path, READ)) {
            if (ch.size() != 0) {
                throw new RuntimeException("Size should be zero");
            }
            ByteBuffer buf = ByteBuffer.allocate(10);
            int nr = ch.read(buf);
            if (nr != -1) {
                throw new RuntimeException("Read returns " + nr + " not -1");
            }
        }
        try (AsynchronousFileChannel ch = AsynchronousFileChannel.open(path, READ, WRITE)) {
            if (ch.size() != 0) {
                throw new RuntimeException("Size should be zero");
            }
            int nw = ch.write(ByteBuffer.wrap(new byte[] { (byte) 0x02 }), 0L).get();
            if (nw != 1) {
                throw new RuntimeException("Wrote " + nw + " bytes, not one");
            }
            ByteBuffer buf = ByteBuffer.allocate(10);
            int nr = ch.read(buf, 0L).get();
            if (nr != -1) {
                throw new RuntimeException("Read returns " + nr + " not -1");
            }
        }
    }
}
