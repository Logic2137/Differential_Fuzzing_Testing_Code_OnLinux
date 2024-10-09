import java.io.*;

public class CreateNewFile {

    public static void main(String[] args) throws Exception {
        File f = new File(System.getProperty("test.dir", "."), "x.CreateNewFile");
        if (f.exists() && !f.delete())
            throw new Exception("Cannot delete test file " + f);
        if (!f.createNewFile())
            throw new Exception("Cannot create new file " + f);
        if (!f.exists())
            throw new Exception("Did not create new file " + f);
        if (f.createNewFile())
            throw new Exception("Created existing file " + f);
        try {
            f = new File("/");
            if (f.createNewFile())
                throw new Exception("Created root directory!");
        } catch (IOException e) {
        }
        testCreateExistingDir();
    }

    private static void testCreateExistingDir() throws IOException {
        File tmpFile = new File("hugo");
        if (tmpFile.exists() && !tmpFile.delete())
            throw new RuntimeException("Cannot delete " + tmpFile);
        if (!tmpFile.mkdir())
            throw new RuntimeException("Cannot create dir " + tmpFile);
        if (!tmpFile.exists())
            throw new RuntimeException("Cannot see created dir " + tmpFile);
        if (tmpFile.createNewFile())
            throw new RuntimeException("Should fail to create file " + tmpFile);
    }
}
