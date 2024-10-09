



import java.io.File;

public class UserDirChangedTest {
    public static void main(String[] args) throws Exception {
        String keyUserDir = "user.dir";
        String userDirNew = "/home/a/b/c/";
        String fileName = "./a";

        String userDir = System.getProperty(keyUserDir);
        File file = new File(fileName);
        String canFilePath = file.getCanonicalPath();

        
        System.setProperty(keyUserDir,  userDirNew);
        String newCanFilePath = file.getCanonicalPath();
        System.out.format("%24s %48s%n", "Canonical Path = ", canFilePath);
        System.out.format("%24s %48s%n", "new Canonical Path = ", newCanFilePath);
        if (!canFilePath.equals(newCanFilePath)) {
            throw new RuntimeException("Changing property user.dir should have no effect on getCanonicalPath");
        }
    }
}
