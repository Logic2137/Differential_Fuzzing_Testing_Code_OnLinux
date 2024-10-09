



import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import static java.nio.file.StandardOpenOption.*;
import java.util.concurrent.TimeUnit;

public class LargeFileAvailable {
    public static void main(String args[]) throws Exception {
        
        
        
        
        File file = File.createTempFile("largefile", null, new File("."));
        long spaceavailable = file.getUsableSpace();
        long filesize = Math.min(spaceavailable,  7405576182L);
        if (spaceavailable == 0L) {
            
            throw new RuntimeException("No space available for temp file.");
        }

        createLargeFile(filesize, file);

        try (FileInputStream fis = new FileInputStream(file)) {
            if (file.length() != filesize) {
                throw new RuntimeException("unexpected file size = "
                                           + file.length());
            }

            long bigSkip = Math.min(filesize/2, 3110608882L);
            long remaining = filesize;
            remaining -= skipBytes(fis, bigSkip, remaining);
            remaining -= skipBytes(fis, 10L, remaining);
            remaining -= skipBytes(fis, bigSkip, remaining);
            int expected = (remaining >= Integer.MAX_VALUE)
                           ? Integer.MAX_VALUE
                           : (remaining > 0 ? (int) remaining : 0);
            if (fis.available() != expected) {
                throw new RuntimeException("available() returns "
                        + fis.available() + " but expected " + expected);
            }
        } finally {
            file.delete();
        }

        System.out.println("Test succeeded.");
        System.out.flush();
    }

    
    
    private static long skipBytes(InputStream is, long toSkip, long avail)
            throws IOException {
        long skip = is.skip(toSkip);
        if (skip != toSkip) {
            throw new RuntimeException("skip() returns " + skip
                                       + " but expected " + toSkip);
        }
        long remaining = avail - skip;
        int expected = (remaining >= Integer.MAX_VALUE)
                       ? Integer.MAX_VALUE
                       : (remaining > 0 ? (int) remaining : 0);

        System.out.println("Skipped " + skip + " bytes, available() returns "
                           + expected + ", remaining " + remaining);
        if (is.available() != expected) {
            throw new RuntimeException("available() returns "
                    + is.available() + " but expected " + expected);
        }
        return skip;
    }

    private static void createLargeFile(long filesize,
                                        File file) throws Exception {
        
        Files.delete(file.toPath());

        try (FileChannel fc =
             FileChannel.open(file.toPath(),
                              CREATE_NEW, WRITE, SPARSE)) {
            ByteBuffer bb = ByteBuffer.allocate(1).put((byte)1);
            bb.rewind();
            System.out.println("  Writing large file...");
            long t0 = System.nanoTime();
            int rc = fc.write(bb, filesize - 1);
            long t1 = System.nanoTime();
            System.out.printf("  Wrote large file in %d ns (%d ms) %n",
                t1 - t0, TimeUnit.NANOSECONDS.toMillis(t1 - t0));

            if (rc != 1) {
                throw new RuntimeException("Failed to write 1 byte"
                                           + " to the large file");
            }
        }
        return;
    }
}
