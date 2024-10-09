import java.io.File;
import java.io.IOException;

public class NameTooLong {

    public static void main(String[] args) {
        String[][] prefixSuffix = new String[][] { new String[] { "1234567890123456789012345678901234567xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx89012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "txt" }, new String[] { "prefix", "1234567890123456789012345678901234567xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx89012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.txt" }, new String[] { "prefix", ".txt1234567890123456789012345678901234567xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx89012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" } };
        int failures = 0;
        int index = 0;
        for (String[] ps : prefixSuffix) {
            File f;
            try {
                f = File.createTempFile(ps[0], ps[1], new File(System.getProperty("test.dir", ".")));
                String s = f.toPath().getFileName().toString();
                if (!s.startsWith(ps[0].substring(0, 3))) {
                    System.err.printf("%s did not start with %s%n", s, ps[0].substring(0, 3));
                    failures++;
                }
                if (ps[1].startsWith(".") && !s.contains(ps[1].substring(0, 4))) {
                    System.err.printf("%s did not contain %s%n", s, ps[1].substring(0, 4));
                    ;
                    failures++;
                }
            } catch (IOException e) {
                failures++;
                System.err.println();
                e.printStackTrace();
                System.err.println();
            }
            index++;
        }
        if (failures != 0) {
            throw new RuntimeException("Test failed!");
        }
    }
}
