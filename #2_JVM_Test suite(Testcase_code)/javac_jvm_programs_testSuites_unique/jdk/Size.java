



import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.util.Random;




public class Size {

    public static void main(String[] args) throws Exception {
        testSmallFile();
        testLargeFile();
    }

    private static void testSmallFile() throws Exception {
        File smallFile = new File("smallFileTest");
        Random generator = new Random();
        for(int i=0; i<100; i++) {
            long testSize = generator.nextInt(1000);
            initTestFile(smallFile, testSize);
            try (FileChannel c = new FileInputStream(smallFile).getChannel()) {
                if (c.size() != testSize) {
                    throw new RuntimeException("Size failed in testSmallFile. "
                                             + "Expect size " + testSize
                                             + ", actual size " + c.size());
                }
            }
        }
        smallFile.deleteOnExit();
    }

    
    private static void testLargeFile() throws Exception {
        File largeFile = new File("largeFileTest");
        long testSize = ((long)Integer.MAX_VALUE) * 2;
        initTestFile(largeFile, 10);
        try (FileChannel fc = new RandomAccessFile(largeFile, "rw").getChannel()) {
            fc.size();
            fc.map(FileChannel.MapMode.READ_WRITE, testSize, 10);
            if (fc.size() != testSize + 10) {
                throw new RuntimeException("Size failed in testLargeFile. "
                                         + "Expect size " + (testSize + 10)
                                         + ", actual size " + fc.size());
            }
        }
        largeFile.deleteOnExit();
    }

    
    private static void initTestFile(File f, long size) throws Exception {
        try (BufferedWriter awriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(f), "8859_1")))
        {
            for(int i=0; i<size; i++) {
                awriter.write("e");
            }
        }
    }
}
