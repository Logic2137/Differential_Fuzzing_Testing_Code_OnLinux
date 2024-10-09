import java.io.IOException;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TestMaxCachedBufferSize {

    private static final int DEFAULT_ITERS = 10 * 1000;

    private static final int DEFAULT_THREAD_NUM = 4;

    private static final int SMALL_BUFFER_MIN_SIZE = 4 * 1024;

    private static final int SMALL_BUFFER_MAX_SIZE = 64 * 1024;

    private static final int SMALL_BUFFER_DIFF_SIZE = SMALL_BUFFER_MAX_SIZE - SMALL_BUFFER_MIN_SIZE;

    private static final int LARGE_BUFFER_MIN_SIZE = 512 * 1024;

    private static final int LARGE_BUFFER_MAX_SIZE = 4 * 1024 * 1024;

    private static final int LARGE_BUFFER_DIFF_SIZE = LARGE_BUFFER_MAX_SIZE - LARGE_BUFFER_MIN_SIZE;

    private static final int LARGE_BUFFER_FREQUENCY = 100;

    private static final String FILE_NAME_PREFIX = "nio-out-file-";

    private static final int VERBOSE_PERIOD = 5 * 1000;

    private static int iters = DEFAULT_ITERS;

    private static int threadNum = DEFAULT_THREAD_NUM;

    private static BufferPoolMXBean getDirectPool() {
        final List<BufferPoolMXBean> pools = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);
        for (BufferPoolMXBean pool : pools) {
            if (pool.getName().equals("direct")) {
                return pool;
            }
        }
        throw new Error("could not find direct pool");
    }

    private static final BufferPoolMXBean directPool = getDirectPool();

    private static class Worker implements Runnable {

        private final int id;

        private final CountDownLatch finishLatch, exitLatch;

        private final Random random = new Random();

        private long smallBufferCount = 0;

        private long largeBufferCount = 0;

        private int getWriteSize() {
            int minSize = 0;
            int diff = 0;
            if (random.nextInt() % LARGE_BUFFER_FREQUENCY != 0) {
                minSize = SMALL_BUFFER_MIN_SIZE;
                diff = SMALL_BUFFER_DIFF_SIZE;
                smallBufferCount += 1;
            } else {
                minSize = LARGE_BUFFER_MIN_SIZE;
                diff = LARGE_BUFFER_DIFF_SIZE;
                largeBufferCount += 1;
            }
            return minSize + random.nextInt(diff);
        }

        private void loop() {
            final String fileName = String.format("%s%d", FILE_NAME_PREFIX, id);
            try {
                for (int i = 0; i < iters; i += 1) {
                    final int writeSize = getWriteSize();
                    final ByteBuffer buffer = ByteBuffer.allocate(writeSize);
                    while (buffer.hasRemaining()) {
                        buffer.put((byte) random.nextInt());
                    }
                    buffer.rewind();
                    final Path file = Paths.get(fileName);
                    try (FileChannel outChannel = FileChannel.open(file, CREATE, TRUNCATE_EXISTING, WRITE)) {
                        long res = outChannel.write(buffer);
                    }
                    if ((i + 1) % VERBOSE_PERIOD == 0) {
                        System.out.printf(" Worker %3d | %8d Iters | Small %8d Large %8d | Direct %4d / %7dK\n", id, i + 1, smallBufferCount, largeBufferCount, directPool.getCount(), directPool.getTotalCapacity() / 1024);
                    }
                }
            } catch (IOException e) {
                throw new Error("I/O error", e);
            } finally {
                finishLatch.countDown();
                try {
                    exitLatch.await();
                } catch (InterruptedException e) {
                }
            }
        }

        @Override
        public void run() {
            loop();
        }

        public Worker(int id, CountDownLatch finishLatch, CountDownLatch exitLatch) {
            this.id = id;
            this.finishLatch = finishLatch;
            this.exitLatch = exitLatch;
        }
    }

    public static void checkDirectBuffers(long expectedCount, long expectedMax) {
        final long directCount = directPool.getCount();
        final long directTotalCapacity = directPool.getTotalCapacity();
        System.out.printf("Direct %d / %dK\n", directCount, directTotalCapacity / 1024);
        if (directCount > expectedCount) {
            throw new Error(String.format("inconsistent direct buffer total count, expected = %d, found = %d", expectedCount, directCount));
        }
        if (directTotalCapacity > expectedMax) {
            throw new Error(String.format("inconsistent direct buffer total capacity, expectex max = %d, found = %d", expectedMax, directTotalCapacity));
        }
    }

    public static void main(String[] args) {
        final String maxBufferSizeStr = System.getProperty("jdk.nio.maxCachedBufferSize");
        final long maxBufferSize = (maxBufferSizeStr != null) ? Long.valueOf(maxBufferSizeStr) : Long.MAX_VALUE;
        if ((SMALL_BUFFER_MIN_SIZE <= maxBufferSize && maxBufferSize <= SMALL_BUFFER_MAX_SIZE) || (LARGE_BUFFER_MIN_SIZE <= maxBufferSize && maxBufferSize <= LARGE_BUFFER_MAX_SIZE)) {
            throw new Error(String.format("max buffer size = %d not allowed", maxBufferSize));
        }
        System.out.printf("Threads %d | Iterations %d | MaxBufferSize %d\n", threadNum, iters, maxBufferSize);
        System.out.println();
        final CountDownLatch finishLatch = new CountDownLatch(threadNum);
        final CountDownLatch exitLatch = new CountDownLatch(1);
        final Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i += 1) {
            threads[i] = new Thread(new Worker(i, finishLatch, exitLatch));
            threads[i].start();
        }
        try {
            try {
                finishLatch.await();
            } catch (InterruptedException e) {
                throw new Error("finishLatch.await() interrupted!", e);
            }
            System.out.println();
            if (maxBufferSize < SMALL_BUFFER_MAX_SIZE) {
                checkDirectBuffers(0, 0);
            } else if (maxBufferSize < LARGE_BUFFER_MIN_SIZE) {
                checkDirectBuffers(threadNum, (long) threadNum * (long) SMALL_BUFFER_MAX_SIZE);
            } else {
                checkDirectBuffers(threadNum, (long) threadNum * (long) LARGE_BUFFER_MAX_SIZE);
            }
        } finally {
            exitLatch.countDown();
            try {
                for (int i = 0; i < threadNum; i += 1) {
                    threads[i].join();
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
