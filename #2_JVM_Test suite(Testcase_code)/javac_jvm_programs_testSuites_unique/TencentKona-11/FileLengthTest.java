


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FileLengthTest {

    private static final int BUF_SIZE = 4096;
    private static RandomAccessFile randomAccessFile;
    private static Thread fileLengthCaller;
    private static Thread fileContentReader;
    private static StringBuilder fileContents;
    private static volatile boolean isFailed = false;

    
    private static void startLengthThread() {
        if (randomAccessFile == null) {
            return;
        }
        fileLengthCaller = new Thread(() -> {
            while (true) {
                try {
                    long length = randomAccessFile.length();
                    if (length < 0) {
                        return;
                    }
                } catch (IOException ex) {
                    return;
                }
            }
        });
        fileLengthCaller.setName("RandomAccessFile-length-caller");
        fileLengthCaller.setDaemon(true);
        fileLengthCaller.start();
    }

    
    private static void startReaderThread() {
        if (randomAccessFile == null) {
            return;
        }
        fileContentReader = new Thread(() -> {
            StringBuilder sb = new StringBuilder(BUF_SIZE);
            int i;
            byte arr[] = new byte[8];
            try {
                while ((i = randomAccessFile.read(arr)) != -1) {
                    sb.append(new String(arr, 0, i));
                }
                if (!sb.toString().equals(fileContents.toString())) {
                    isFailed = true;
                }
            } catch (IOException ex) {
            }
        });
        fileContentReader.setName("RandomAccessFile-content-reader");
        fileContentReader.setDaemon(true);
        fileContentReader.start();
    }

    public static void main(String args[]) {
        byte arr[] = new byte[BUF_SIZE];
        String testFile = "testfile.txt";
        try {
            createDummyFile(testFile);
            File file = new File(testFile);
            file.deleteOnExit();
            randomAccessFile = new RandomAccessFile(file, "r");
            int count = randomAccessFile.read(arr);
            randomAccessFile.seek(0);
            fileContents = new StringBuilder(BUF_SIZE);
            fileContents.append(new String(arr, 0, count));
            startLengthThread();
            startReaderThread();
            fileContentReader.join();
        } catch (FileNotFoundException | InterruptedException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException ex) {
            }
        }
        if (isFailed) {
            throw new RuntimeException("RandomAccessFile.length() changed the underlying file pointer.");
        }
    }

    private static void createDummyFile(String fileName) throws FileNotFoundException, IOException {
        try (FileOutputStream outputStream = new FileOutputStream(new File(fileName))) {
            String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            int count = 0;
            while ((count + str.length()) < BUF_SIZE) {
                outputStream.write(str.getBytes());
                count += str.length();
            }
            outputStream.flush();
        }
    }
}
