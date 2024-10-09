



import java.io.File;
import java.io.FilePermission;
import java.lang.*;

public class Flag {
    public static void main(String[] args) throws Exception {

        boolean test1;
        boolean test2;

        String here = System.getProperty("user.dir");
        File abs = new File(here, "x");
        FilePermission fp1 = new FilePermission("x", "read");
        FilePermission fp2 = new FilePermission(abs.toString(), "read");
        test1 = fp1.equals(fp2);

        try {
            System.getSecurityManager().checkPermission(fp2);
            test2 = true;
        } catch (SecurityException se) {
            test2 = false;
        }

        if (test1 != Boolean.parseBoolean(args[0]) ||
                test2 != Boolean.parseBoolean(args[1])) {
            throw new Exception("Test failed: " + test1 + " " + test2);
        }
    }
}
