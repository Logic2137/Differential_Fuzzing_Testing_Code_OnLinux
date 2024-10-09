



import java.io.*;

public class ReadFully {

    private static final void testNegativeOffset() throws Exception {
        File file = new File(System.getProperty("test.src"),
                "ReadFully.java");
        try (FileInputStream in = new FileInputStream(file);
             DataInputStream dis = new DataInputStream(in);) {
            byte[] buffer = new byte[100];
            dis.readFully(buffer, -1, buffer.length);
            throw new RuntimeException("Test testNegativeOffset() failed");
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    private static final void testNegativeLength() throws Exception {
        File file = new File(System.getProperty("test.src"),
                "ReadFully.java");
        try (FileInputStream in = new FileInputStream(file);
             DataInputStream dis = new DataInputStream(in);) {
            byte[] buffer = new byte[100];
            dis.readFully(buffer, 0, -1);
            throw new RuntimeException("Test testNegativeLength() failed");
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    private static final void testNegativeOffsetZeroLength() throws Exception {
        File file = new File(System.getProperty("test.src"),
                "ReadFully.java");
        try (FileInputStream in = new FileInputStream(file);
             DataInputStream dis = new DataInputStream(in);) {
            byte[] buffer = new byte[100];
            dis.readFully(buffer, -1, 0);
            throw new RuntimeException("Test testNegativeOffsetZeroLength() failed");
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    private static final void testBigOffsetLength1() throws Exception {
        File file = new File(System.getProperty("test.src"),
                "ReadFully.java");
        try (FileInputStream in = new FileInputStream(file);
             DataInputStream dis = new DataInputStream(in);) {
            byte[] buffer = new byte[100];
            dis.readFully(buffer, 0, buffer.length + 1);
            throw new RuntimeException("Test testBigOffsetLength1() failed");
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    private static final void testBigOffsetLength2() throws Exception {
        File file = new File(System.getProperty("test.src"),
                "ReadFully.java");
        try (FileInputStream in = new FileInputStream(file);
             DataInputStream dis = new DataInputStream(in);) {
            byte[] buffer = new byte[100];
            dis.readFully(buffer, 1, buffer.length);
            throw new RuntimeException("Test testBigOffsetLength2() failed");
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    private static final void testBigOffsetLength3() throws Exception {
        File file = new File(System.getProperty("test.src"),
                "ReadFully.java");
        try (FileInputStream in = new FileInputStream(file);
             DataInputStream dis = new DataInputStream(in);) {
            byte[] buffer = new byte[100];
            dis.readFully(buffer, buffer.length, 1);
            throw new RuntimeException("Test testBigOffsetLength3() failed");
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    private static final void testBigOffsetLength4() throws Exception {
        File file = new File(System.getProperty("test.src"),
                "ReadFully.java");
        try (FileInputStream in = new FileInputStream(file);
             DataInputStream dis = new DataInputStream(in);) {
            byte[] buffer = new byte[100];
            dis.readFully(buffer, buffer.length + 1, 0);
            throw new RuntimeException("Test testBigOffsetLength4() failed");
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    public static final void main(String[] args) throws Exception {
        testNegativeOffset();
        testNegativeLength();
        testNegativeOffsetZeroLength();
        testBigOffsetLength1();
        testBigOffsetLength2();
        testBigOffsetLength3();
        testBigOffsetLength4();
    }

}
