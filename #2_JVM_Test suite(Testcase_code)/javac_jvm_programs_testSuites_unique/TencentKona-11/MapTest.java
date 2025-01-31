



import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import static java.nio.file.StandardOpenOption.*;
import static java.nio.charset.StandardCharsets.*;
import java.util.Random;




public class MapTest {

    private static PrintStream out = System.out;
    private static PrintStream err = System.err;

    private static Random generator = new Random();

    private static int CHARS_PER_LINE = File.separatorChar == '/' ? 5 : 6;

    private static File blah;

    public static void main(String[] args) throws Exception {
        blah = File.createTempFile("blah", null);
        blah.deleteOnExit();
        initTestFile(blah);
        try {
            out.println("Test file " + blah + " initialized");
            testZero();
            out.println("Zero size: OK");
            testRead();
            out.println("Read: OK");
            testWrite();
            out.println("Write: OK");
            testHighOffset();
            out.println("High offset: OK");
            testExceptions();
            out.println("Exceptions: OK");
        } finally {
            blah.delete();
        }
    }

    
    private static void initTestFile(File blah) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(blah.toPath(), ISO_8859_1)) {
            for (int i=0; i<4000; i++) {
                String number = new Integer(i).toString();
                for (int h=0; h<4-number.length(); h++)
                    writer.write("0");
                writer.write(""+i);
                writer.newLine();
            }
        }
    }

    
    private static void testZero() throws Exception {
        try (FileInputStream fis = new FileInputStream(blah)) {
            FileChannel fc = fis.getChannel();
            MappedByteBuffer b = fc.map(MapMode.READ_ONLY, 0, 0);
        }
    }

    
    private static void testRead() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.setLength(4);

        for (int x=0; x<1000; x++) {
            try (FileInputStream fis = new FileInputStream(blah)) {
                FileChannel fc = fis.getChannel();

                long offset = generator.nextInt(10000);
                long expectedResult = offset / CHARS_PER_LINE;
                offset = expectedResult * CHARS_PER_LINE;

                MappedByteBuffer b = fc.map(MapMode.READ_ONLY,
                                            offset, 100);

                for (int i=0; i<4; i++) {
                    byte aByte = b.get(i);
                    sb.setCharAt(i, (char)aByte);
                }

                int result = Integer.parseInt(sb.toString());
                if (result != expectedResult) {
                    err.println("I expected "+expectedResult);
                    err.println("I got "+result);
                    throw new Exception("Read test failed");
                }
            }
        }
    }

    
    private static void testWrite() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.setLength(4);

        for (int x=0; x<1000; x++) {
            try (RandomAccessFile raf = new RandomAccessFile(blah, "rw")) {
                FileChannel fc = raf.getChannel();

                long offset = generator.nextInt(1000);
                MappedByteBuffer b = fc.map(MapMode.READ_WRITE,
                                            offset, 100);

                for (int i=0; i<4; i++) {
                    b.put(i, (byte)('0' + i));
                }

                for (int i=0; i<4; i++) {
                    byte aByte = b.get(i);
                    sb.setCharAt(i, (char)aByte);
                }
                if (!sb.toString().equals("0123"))
                    throw new Exception("Write test failed");
            }
        }
    }

    private static void testHighOffset() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.setLength(4);

        for (int x=0; x<1000; x++) {
            try (RandomAccessFile raf = new RandomAccessFile(blah, "rw")) {
                FileChannel fc = raf.getChannel();
                long offset = 66000;
                MappedByteBuffer b = fc.map(MapMode.READ_WRITE,
                                            offset, 100);
            }
        }
    }

    
    private static void testExceptions() throws Exception {
        
        try (FileChannel fc = FileChannel.open(blah.toPath(), READ)) {
            testExceptions(fc);

            checkException(fc, MapMode.READ_WRITE, 0L, fc.size(),
                           NonWritableChannelException.class);

            checkException(fc, MapMode.READ_WRITE, -1L, fc.size(),
                           NonWritableChannelException.class, IllegalArgumentException.class);

            checkException(fc, MapMode.READ_WRITE, 0L, -1L,
                           NonWritableChannelException.class, IllegalArgumentException.class);

            checkException(fc, MapMode.PRIVATE, 0L, fc.size(),
                           NonWritableChannelException.class);

            checkException(fc, MapMode.PRIVATE, -1L, fc.size(),
                           NonWritableChannelException.class, IllegalArgumentException.class);

            checkException(fc, MapMode.PRIVATE, 0L, -1L,
                           NonWritableChannelException.class, IllegalArgumentException.class);
        }

        
        try (FileChannel fc = FileChannel.open(blah.toPath(), WRITE)) {
            testExceptions(fc);

            checkException(fc, MapMode.READ_ONLY, 0L, fc.size(),
                           NonReadableChannelException.class);

            checkException(fc, MapMode.READ_ONLY, -1L, fc.size(),
                           NonReadableChannelException.class, IllegalArgumentException.class);

            
            
            
            
            
        }

        
        try (FileChannel fc = FileChannel.open(blah.toPath(), READ, WRITE)) {
            testExceptions(fc);
        }
    }

    private static void testExceptions(FileChannel fc) throws IOException {
        checkException(fc, null, 0L, fc.size(),
                       NullPointerException.class);

        checkException(fc, MapMode.READ_ONLY, -1L, fc.size(),
                       IllegalArgumentException.class);

        checkException(fc, null, -1L, fc.size(),
                       IllegalArgumentException.class, NullPointerException.class);

        checkException(fc, MapMode.READ_ONLY, 0L, -1L,
                       IllegalArgumentException.class);

        checkException(fc, null, 0L, -1L,
                       IllegalArgumentException.class, NullPointerException.class);

        checkException(fc, MapMode.READ_ONLY, 0L, Integer.MAX_VALUE + 1L,
                       IllegalArgumentException.class);

        checkException(fc, null, 0L, Integer.MAX_VALUE + 1L,
                       IllegalArgumentException.class, NullPointerException.class);

        checkException(fc, MapMode.READ_ONLY, Long.MAX_VALUE, 1L,
                       IllegalArgumentException.class);

        checkException(fc, null, Long.MAX_VALUE, 1L,
                       IllegalArgumentException.class, NullPointerException.class);

    }

    
    private static void checkException(FileChannel fc,
                                       MapMode mode,
                                       long position,
                                       long size,
                                       Class<?>... expected)
        throws IOException
    {
        Exception exc = null;
        try {
            fc.map(mode, position, size);
        } catch (Exception actual) {
            exc = actual;
        }
        if (exc != null) {
            for (Class<?> clazz: expected) {
                if (clazz.isInstance(exc)) {
                    return;
                }
            }
        }
        System.err.println("Expected one of");
        for (Class<?> clazz: expected) {
            System.out.println(clazz);
        }
        if (exc == null) {
            throw new RuntimeException("No expection thrown");
        } else {
            throw new RuntimeException("Unexpected exception thrown", exc);
        }
    }
}
