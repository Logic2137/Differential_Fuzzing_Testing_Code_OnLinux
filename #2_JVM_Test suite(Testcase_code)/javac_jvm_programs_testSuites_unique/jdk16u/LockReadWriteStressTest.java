



import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;

public class LockReadWriteStressTest {
    public static void main(String[] args) throws Exception {
        Path path = Path.of("blah");
        ByteBuffer buf = ByteBuffer.allocate(16);
        for (int i=0; i < 1000; i++) {
            try (AsynchronousFileChannel ch = AsynchronousFileChannel.open(path,READ, WRITE, CREATE)) {
                FileLock lock = ch.lock().get();
                ch.read(buf, 0).get();
                buf.rewind();
                ch.write(buf, 0).get();
                lock.release();
                buf.clear();
            }
        }
    }
}
