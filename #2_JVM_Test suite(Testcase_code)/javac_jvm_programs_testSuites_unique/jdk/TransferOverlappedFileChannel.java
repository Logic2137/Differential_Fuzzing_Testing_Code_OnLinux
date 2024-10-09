


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Random;

public class TransferOverlappedFileChannel {

    public static void main(String[] args) throws Exception {
        File file = File.createTempFile("readingin", null);
        file.deleteOnExit();
        generateBigFile(file);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        try (FileChannel channel = raf.getChannel()) {
            transferToNoOverlap(file, channel);
            transferToOverlap(file, channel);
            transferFromNoOverlap(file, channel);
            transferFromOverlap(file, channel);
        } finally {
            file.delete();
        }
    }

    private static void transferToNoOverlap(File file, FileChannel channel)
        throws IOException {
        final long length = file.length();

        
        channel.position(length*3/4);
        
        
        
        channel.transferTo(length / 2, length / 4, channel);
        System.out.println("transferToNoOverlap: OK");
    }

    private static void transferToOverlap(File file, FileChannel channel)
        throws IOException {
        final long length = file.length();

        
        channel.position(length/2);
        
        
        
        channel.transferTo(length / 4, length / 2, channel);
        System.out.println("transferToOverlap: OK");
    }

    private static void transferFromNoOverlap(File file, FileChannel channel)
        throws IOException {
        final long length = file.length();

        
        channel.position(length*3/4);
        
        
        
        channel.transferFrom(channel, length / 2, length / 4);
        System.out.println("transferFromNoOverlap: OK");
    }

    private static void transferFromOverlap(File file, FileChannel channel)
        throws IOException {
        final long length = file.length();

        
        channel.position(length/2);
        
        
        
        channel.transferFrom(channel, length / 4, length / 2);
        System.out.println("transferFromOverlap: OK");
    }

    private static void generateBigFile(File file) throws Exception {
        try (OutputStream out = new BufferedOutputStream(
                new FileOutputStream(file))) {
            byte[] randomBytes = new byte[1024];
            Random rand = new Random(0);
            rand.nextBytes(randomBytes);
            for (int i = 0; i < 1024; i++) {
                out.write(randomBytes);
            }
            out.flush();
        }
    }
}
