import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

public class SelectFdsLimit {

    static final int FDTOOPEN = 1023;

    static final String TESTFILE = "testfile";

    static FileInputStream[] testFIS;

    static void prepareTestEnv() throws IOException {
        File fileToCreate = new File(TESTFILE);
        if (!fileToCreate.exists())
            if (!fileToCreate.createNewFile())
                throw new RuntimeException("Can't create test file");
    }

    static void openFiles(int fn, File f) throws FileNotFoundException, IOException {
        testFIS = new FileInputStream[FDTOOPEN];
        for (; ; ) {
            if (0 == fn)
                break;
            FileInputStream fis = new FileInputStream(f);
            testFIS[--fn] = fis;
        }
    }

    public static void main(String[] args) throws IOException, FileNotFoundException {
        if (!System.getProperty("os.name").contains("OS X")) {
            return;
        }
        prepareTestEnv();
        openFiles(FDTOOPEN, new File(TESTFILE));
        ServerSocket socket = new ServerSocket(0);
        socket.setSoTimeout(1);
        try {
            socket.accept();
        } catch (SocketTimeoutException e) {
        }
    }
}
