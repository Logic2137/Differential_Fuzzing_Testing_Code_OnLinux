import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.CountDownLatch;

public class Sharing {

    static final int numFiles = 10;

    static volatile boolean fail;

    public static void main(String[] args) throws Exception {
        TestFinalizer();
        TestMultipleFD();
        TestIsValid();
        MultiThreadedFD();
        TestCloseAll();
    }

    private static void TestFinalizer() throws Exception {
        FileDescriptor fd = null;
        File tempFile = new File("TestFinalizer1.txt");
        tempFile.deleteOnExit();
        try (Writer writer = new FileWriter(tempFile)) {
            for (int i = 0; i < 5; i++) {
                writer.write("test file content test file content");
            }
        }
        FileInputStream fis1 = new FileInputStream(tempFile);
        fd = fis1.getFD();
        try (FileInputStream fis2 = new FileInputStream(fd)) {
            fis1 = null;
            int ret = 0;
            while (ret >= 0) {
                System.gc();
                System.out.print(".");
                ret = fis2.read();
            }
        }
        File testFinalizerFile = new File("TestFinalizer");
        RandomAccessFile raf = new RandomAccessFile(testFinalizerFile, "rw");
        raf.writeBytes("test file content test file content");
        raf.seek(0L);
        fd = raf.getFD();
        try (FileInputStream fis3 = new FileInputStream(fd)) {
            raf = null;
            int ret = 0;
            while (ret >= 0) {
                System.gc();
                System.out.print(".");
                ret = fis3.read();
            }
        } finally {
            testFinalizerFile.delete();
        }
    }

    private static void TestMultipleFD() throws Exception {
        RandomAccessFile raf = null;
        FileOutputStream fos = null;
        FileInputStream fis = null;
        FileChannel fc = null;
        FileLock fileLock = null;
        File test1 = new File("test1");
        try {
            raf = new RandomAccessFile(test1, "rw");
            fos = new FileOutputStream(raf.getFD());
            fis = new FileInputStream(raf.getFD());
            fc = raf.getChannel();
            fileLock = fc.lock();
            raf.setLength(0L);
            fos.flush();
            fos.write("TEST".getBytes());
        } finally {
            if (fileLock != null)
                fileLock.release();
            if (fis != null)
                fis.close();
            if (fos != null)
                fos.close();
            if (raf != null)
                raf.close();
            test1.delete();
        }
        File test2 = new File("test2");
        try {
            raf = new RandomAccessFile(test2, "rw");
            fos = new FileOutputStream(raf.getFD());
            fis = new FileInputStream(raf.getFD());
            fc = raf.getChannel();
            fileLock = fc.lock();
            raf.setLength(0L);
            fos.flush();
            fos.write("TEST".getBytes());
        } finally {
            if (fileLock != null)
                fileLock.release();
            if (raf != null)
                raf.close();
            if (fos != null)
                fos.close();
            if (fis != null)
                fis.close();
            test2.delete();
        }
        File test3 = new File("test3");
        try {
            raf = new RandomAccessFile(test3, "rw");
            fos = new FileOutputStream(raf.getFD());
            fis = new FileInputStream(raf.getFD());
            fc = raf.getChannel();
            fileLock = fc.lock();
            raf.setLength(0L);
            fos.flush();
            fos.write("TEST".getBytes());
        } finally {
            if (fileLock != null)
                fileLock.release();
            if (fos != null)
                fos.close();
            if (raf != null)
                raf.close();
            if (fis != null)
                fis.close();
            test3.delete();
        }
    }

    private static void TestIsValid() throws Exception {
        FileDescriptor fd = null;
        RandomAccessFile raf = null;
        FileOutputStream fos = null;
        FileInputStream fis = null;
        FileChannel fc = null;
        File test1 = new File("test1");
        try {
            raf = new RandomAccessFile(test1, "rw");
            fd = raf.getFD();
            fos = new FileOutputStream(fd);
            fis = new FileInputStream(fd);
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (fd.valid()) {
                    throw new RuntimeException("[FIS close()] FileDescriptor shouldn't be valid");
                }
                if (fos != null)
                    fos.close();
                if (raf != null)
                    raf.close();
            } finally {
                test1.delete();
            }
        }
        File test2 = new File("test2");
        try {
            raf = new RandomAccessFile(test2, "rw");
            fd = raf.getFD();
            fos = new FileOutputStream(fd);
            fis = new FileInputStream(fd);
        } finally {
            try {
                if (raf != null)
                    raf.close();
                if (fd.valid()) {
                    throw new RuntimeException("[RAF close()] FileDescriptor shouldn't be valid");
                }
                if (fos != null)
                    fos.close();
                if (fis != null)
                    fis.close();
            } finally {
                test2.delete();
            }
        }
        File test3 = new File("test3");
        try {
            raf = new RandomAccessFile(test3, "rw");
            fd = raf.getFD();
            fos = new FileOutputStream(fd);
            fis = new FileInputStream(fd);
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (fd.valid()) {
                    throw new RuntimeException("[FOS close()] FileDescriptor shouldn't be valid");
                }
                if (raf != null)
                    raf.close();
                if (fis != null)
                    fis.close();
            } finally {
                test3.delete();
            }
        }
    }

    private static void MultiThreadedFD() throws Exception {
        RandomAccessFile raf = null;
        FileDescriptor fd = null;
        int numThreads = 2;
        CountDownLatch done = new CountDownLatch(numThreads);
        OpenClose[] fileOpenClose = new OpenClose[numThreads];
        File MultipleThreadedFD = new File("MultipleThreadedFD");
        try {
            raf = new RandomAccessFile(MultipleThreadedFD, "rw");
            fd = raf.getFD();
            for (int count = 0; count < numThreads; count++) {
                fileOpenClose[count] = new OpenClose(fd, done);
                fileOpenClose[count].start();
            }
            done.await();
        } finally {
            try {
                if (raf != null)
                    raf.close();
                if (fd.valid()) {
                    throw new RuntimeException("FileDescriptor should not be valid");
                }
                if (fail) {
                    throw new RuntimeException("OpenClose thread tests failed.");
                }
            } finally {
                MultipleThreadedFD.delete();
            }
        }
    }

    private static void TestCloseAll() throws Exception {
        File testFile = new File("test");
        testFile.deleteOnExit();
        RandomAccessFile raf = new RandomAccessFile(testFile, "rw");
        FileInputStream fis = new FileInputStream(raf.getFD());
        fis.close();
        if (raf.getFD().valid()) {
            throw new RuntimeException("FD should not be valid.");
        }
        raf = new RandomAccessFile(testFile, "rw");
        fis = new FileInputStream(raf.getFD());
        BadFileInputStream bfis1 = new BadFileInputStream(raf.getFD());
        BadFileInputStream bfis2 = new BadFileInputStream(raf.getFD());
        BadFileInputStream bfis3 = new BadFileInputStream(raf.getFD());
        bfis3 = null;
        try {
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            if (ioe.getSuppressed().length != 2) {
                throw new RuntimeException("[FIS]Incorrect number of suppressed " + "exceptions received : " + ioe.getSuppressed().length);
            }
        }
        if (raf.getFD().valid()) {
            throw new RuntimeException("[FIS]TestCloseAll : FD still valid.");
        }
        raf = new RandomAccessFile(testFile, "rw");
        FileOutputStream fos = new FileOutputStream(raf.getFD());
        BadFileOutputStream bfos1 = new BadFileOutputStream(raf.getFD());
        BadFileOutputStream bfos2 = new BadFileOutputStream(raf.getFD());
        BadFileOutputStream bfos3 = new BadFileOutputStream(raf.getFD());
        bfos3 = null;
        try {
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            if (ioe.getSuppressed().length != 2) {
                throw new RuntimeException("[FOS]Incorrect number of suppressed " + "exceptions received : " + ioe.getSuppressed().length);
            }
        }
        if (raf.getFD().valid()) {
            throw new RuntimeException("[FOS]TestCloseAll : FD still valid.");
        }
    }

    private static class OpenClose extends Thread {

        private FileDescriptor fd = null;

        private CountDownLatch done;

        FileInputStream[] fisArray = new FileInputStream[numFiles];

        FileOutputStream[] fosArray = new FileOutputStream[numFiles];

        OpenClose(FileDescriptor filedescriptor, CountDownLatch done) {
            this.fd = filedescriptor;
            this.done = done;
        }

        public void run() {
            try {
                for (int i = 0; i < numFiles; i++) {
                    fisArray[i] = new FileInputStream(fd);
                    fosArray[i] = new FileOutputStream(fd);
                }
                for (int i = 0; i < numFiles; i++) {
                    if (fisArray[i] != null)
                        fisArray[i].close();
                    if (fosArray[i] != null)
                        fosArray[i].close();
                }
            } catch (IOException ioe) {
                System.out.println("OpenClose encountered IO issue :" + ioe);
                fail = true;
            } finally {
                if (fd.valid()) {
                    System.out.println("OpenClose: FileDescriptor shouldn't be valid");
                    fail = true;
                }
                done.countDown();
            }
        }
    }

    private static class BadFileInputStream extends FileInputStream {

        BadFileInputStream(FileDescriptor fd) {
            super(fd);
        }

        public void close() throws IOException {
            throw new IOException("Bad close operation");
        }
    }

    private static class BadFileOutputStream extends FileOutputStream {

        BadFileOutputStream(FileDescriptor fd) {
            super(fd);
        }

        public void close() throws IOException {
            throw new IOException("Bad close operation");
        }
    }
}
