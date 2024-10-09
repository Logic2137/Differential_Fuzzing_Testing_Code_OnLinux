import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RedirectWithLongFilename {

    public static void main(String[] args) throws Exception {
        Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
        File dir2 = null;
        File longFileName = null;
        try {
            dir2 = Files.createTempDirectory(tmpDir, "RedirectWithLongFilename").toFile();
            dir2.mkdirs();
            longFileName = new File(dir2, "012345678901234567890123456789012345678901234567890123456789" + "012345678901234567890123456789012345678901234567890123456789" + "012345678901234567890123456789012345678901234567890123456789" + "012345678901234567890123456789012345678901234567890123456789" + "0123456789");
            ProcessBuilder pb = new ProcessBuilder("hostname.exe");
            pb.redirectOutput(Redirect.appendTo(longFileName));
            Process p = pb.start();
            p.waitFor();
            if (longFileName.exists()) {
                System.out.println("OK");
            } else {
                throw new RuntimeException("Test failed.");
            }
        } finally {
            longFileName.delete();
            dir2.delete();
        }
    }
}
