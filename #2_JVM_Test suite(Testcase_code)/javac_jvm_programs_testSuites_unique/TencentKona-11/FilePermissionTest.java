

import java.io.File;
import java.io.FilePermission;
import java.util.Arrays;
import java.util.List;


public class FilePermissionTest {

    public static void main(String[] args) throws Exception {

        final File realFile = new File("exist.file");
        try {
            if (!realFile.createNewFile()) {
                throw new RuntimeException("Unable to create a file.");
            }
            check(Arrays.asList(realFile.getName(), "notexist.file"), args[0]);
        } finally {
            if (realFile.exists()) {
                realFile.delete();
            }
        }
    }

    private static void check(List<String> files, String expected) {

        StringBuilder actual = new StringBuilder();
        files.forEach(f -> {
            StringBuilder result = new StringBuilder();
            FilePermission fp1 = new FilePermission(f, "read");
            FilePermission fp2 = new FilePermission(
                    new File(f).getAbsolutePath(), "read");
            result.append(fp1.equals(fp2));
            result.append(fp1.implies(fp2));
            result.append(fp1.hashCode() == fp2.hashCode());
            System.out.println(fp1 + " Vs. " + fp2 + " : Result: " + result);
            actual.append(result);
        });
        if (!expected.equals(actual.toString())) {
            throw new RuntimeException("Failed: " + expected + "/" + actual);
        }
    }
}
