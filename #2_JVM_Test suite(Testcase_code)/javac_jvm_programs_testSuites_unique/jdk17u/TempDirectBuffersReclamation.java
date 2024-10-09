import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

public class TempDirectBuffersReclamation {

    public static void main(String[] args) throws IOException {
        BufferPoolMXBean dbPool = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class).stream().filter(bp -> bp.getName().equals("direct")).findFirst().orElseThrow(() -> new RuntimeException("Can't obtain direct BufferPoolMXBean"));
        long count0 = dbPool.getCount();
        long memoryUsed0 = dbPool.getMemoryUsed();
        Thread thread = new Thread(TempDirectBuffersReclamation::doFileChannelWrite);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long count1 = dbPool.getCount();
        long memoryUsed1 = dbPool.getMemoryUsed();
        if (count0 != count1 || memoryUsed0 != memoryUsed1) {
            throw new AssertionError("Direct BufferPool not same before thread activity and after thread exit.\n" + "Before: # of buffers: " + count0 + ", memory used: " + memoryUsed0 + "\n" + " After: # of buffers: " + count1 + ", memory used: " + memoryUsed1 + "\n");
        }
    }

    static void doFileChannelWrite() {
        try {
            Path file = Files.createTempFile("test", ".tmp");
            try (FileChannel fc = FileChannel.open(file, CREATE, WRITE, TRUNCATE_EXISTING)) {
                fc.write(ByteBuffer.wrap("HELLO".getBytes(StandardCharsets.UTF_8)));
            } finally {
                Files.delete(file);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
