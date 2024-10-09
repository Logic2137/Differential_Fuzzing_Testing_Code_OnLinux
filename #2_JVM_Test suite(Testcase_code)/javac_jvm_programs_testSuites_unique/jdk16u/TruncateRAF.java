



import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TruncateRAF {

    static void checkState(RandomAccessFile raf, FileChannel fch,
            long expectedOffset, long expectedLength)
        throws IOException
    {
        long rafLength = raf.length();
        long rafOffset = raf.getFilePointer();
        long fchLength = fch.size();
        long fchOffset = fch.position();

        if (rafLength != expectedLength)
            throw new RuntimeException("rafLength (" + rafLength + ") != " +
                    "expectedLength (" + expectedLength + ")");
        if (rafOffset != expectedOffset)
            throw new RuntimeException("rafOffset (" + rafOffset + ") != " +
                    "expectedOffset (" + expectedOffset + ")");
        if (fchLength != expectedLength)
            throw new RuntimeException("fchLength (" + fchLength + ") != " +
                    "expectedLength (" + expectedLength + ")");
        if (fchOffset != expectedOffset)
            throw new RuntimeException("fchOffset (" + fchOffset + ") != " +
                    "expectedOffset (" + expectedOffset + ")");
    }

    public static void main(String[] args) throws Throwable {
        File file = new File("tmp");
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fch = raf.getChannel()) {

            
            checkState(raf, fch, 0, 0);

            
            raf.seek(42);
            checkState(raf, fch, 42, 0);

            
            fch.position(84);
            checkState(raf, fch, 84, 0);

            
            raf.write(1);
            checkState(raf, fch, 85, 85);

            
            raf.setLength(63);
            checkState(raf, fch, 63, 63);

            
            fch.write(ByteBuffer.wrap(new byte[1]));
            checkState(raf, fch, 64, 64);

            
            fch.position(32);
            checkState(raf, fch, 32, 64);

            
            fch.truncate(42);
            checkState(raf, fch, 32, 42);

            
            fch.truncate(16);
            checkState(raf, fch, 16, 16);

            
            fch.write(ByteBuffer.wrap(new byte[1]), 127);
            checkState(raf, fch, 16, 128);

            
            fch.write(ByteBuffer.wrap(new byte[1]), 42);
            checkState(raf, fch, 16, 128);

            
            raf.setLength(64);
            checkState(raf, fch, 16, 64);

            
            raf.seek(21);
            checkState(raf, fch, 21, 64);

            
            raf.skipBytes(4);
            checkState(raf, fch, 25, 64);

            
            raf.read();
            checkState(raf, fch, 26, 64);

            
            raf.setLength(0);
            checkState(raf, fch, 0, 0);

            
            fch.truncate(42);
            checkState(raf, fch, 0, 0);

            
            raf.setLength(42);
            checkState(raf, fch, 0, 42);

            
            raf.seek(512);
            checkState(raf, fch, 512, 42);

            
            fch.truncate(256);
            checkState(raf, fch, 256, 42);

            
            fch.truncate(42);
            checkState(raf, fch, 42, 42);

            
            fch.truncate(0);
            checkState(raf, fch, 0, 0);
        }
    }
}
