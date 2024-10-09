



import java.io.*;
import java.nio.*;
import java.util.*;
import java.nio.channels.*;

public class Force {
    public static void main(String[] args) throws Exception {
        Random random = new Random();
        long filesize = random.nextInt(3*1024*1024);
        int cut = random.nextInt((int)filesize);
        File file = File.createTempFile("Blah", null);
        file.deleteOnExit();
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(filesize);
            FileChannel fc = raf.getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, cut, filesize-cut);
            mbb.force();
        }

        
        System.gc();
        Thread.sleep(500);
    }
}
