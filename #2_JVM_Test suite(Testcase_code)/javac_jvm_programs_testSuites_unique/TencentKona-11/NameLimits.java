



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NameLimits {

    static final int MAX_PATH = 255;
    static final int MIN_PATH = 8;     

    static Path generatePath(int len) {
        if (len < MIN_PATH)
            throw new RuntimeException("Attempting to generate path less than MIN_PATH");
        StringBuilder sb = new StringBuilder(len);
        sb.append("name");
        while (sb.length() < len) {
            sb.append('X');
        }
        return Paths.get(sb.toString());
    }

    static boolean tryCreateFile(int len) throws IOException {
        Path name = generatePath(len);
        try {
            Files.createFile(name);
        } catch (IOException ioe) {
            System.err.format("Unable to create file of length %d (full path %d), %s%n",
                name.toString().length(), name.toAbsolutePath().toString().length(), ioe);
            return false;
        }
        Files.delete(name);
        return true;
    }

    static boolean tryCreateDirectory(int len) throws IOException {
        Path name = generatePath(len);
        try {
            Files.createDirectory(name);
        } catch (IOException ioe) {
            System.err.format("Unable to create directory of length %d (full path %d), %s%n",
                name.toString().length(), name.toAbsolutePath().toString().length(), ioe);
            return false;
        }
        Files.delete(name);
        return true;
    }

    public static void main(String[] args) throws Exception {
        int len;

        
        len = MAX_PATH;
        while (!tryCreateFile(len)) {
            len--;
        }
        System.out.format("Testing createFile on paths %d .. %d%n", MIN_PATH, len);
        while (len >= MIN_PATH) {
            if (!tryCreateFile(len--))
                throw new RuntimeException("Test failed");
        }

        
        len = MAX_PATH;
        while (!tryCreateDirectory(len)) {
            len--;
        }
        System.out.format("Testing createDirectory on paths %d .. %d%n", MIN_PATH, len);
        while (len >= MIN_PATH) {
            if (!tryCreateDirectory(len--))
                throw new RuntimeException("Test failed");
        }
    }
}
