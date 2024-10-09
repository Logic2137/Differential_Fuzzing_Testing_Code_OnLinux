import java.io.File;

public class TestSecurityManager extends SecurityManager {

    public TestSecurityManager() {
    }

    public void checkWrite(String file) {
        if (file.endsWith("log" + File.separatorChar + "Snapshot.6")) {
            System.out.println("writing file: " + file + " simulating log failure");
            throw new SecurityException("simulating log failure");
        }
    }

    public void checkRead(String file) {
        if (file.endsWith("log" + File.separatorChar + "Logfile.6")) {
            System.out.println("reading file: " + file + " simulating log failure");
            throw new SecurityException("simulating log failure");
        }
    }
}
