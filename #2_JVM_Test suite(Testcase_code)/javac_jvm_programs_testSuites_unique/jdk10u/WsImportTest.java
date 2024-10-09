



import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.FileVisitResult.*;

public class WsImportTest {

    public static void main(String[] args) throws IOException {

        String wsimport = getWsImport();
        String wsdl = getWSDLFilePath("test-service.wsdl");

        try {
            log("Importing wsdl: " + wsdl);
            String[] wsargs = {
                wsimport,
                "-p",
                "generated",
                "-J-Dfile.encoding=Cp037",
                wsdl
            };

            ProcessBuilder pb = new ProcessBuilder(wsargs);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            logOutput(p);
            int result = p.waitFor();
            p.destroy();

            if (result != 0) {
                fail("WsImport failed. TEST FAILED.");
            } else {
                log("Test PASSED.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            deleteGeneratedFiles();
        }
    }

    private static void fail(String message) {
        throw new RuntimeException(message);
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    private static void logOutput(Process p) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s = r.readLine();
        while (s != null) {
            log(s.trim());
            s = r.readLine();
        }
    }

    private static void deleteGeneratedFiles() {
        Path p = Paths.get("generated");
        if (Files.exists(p)) {
            try {
                Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file,
                        BasicFileAttributes attrs) throws IOException {

                        Files.delete(file);
                        return CONTINUE;
                    }
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir,
                        IOException exc) throws IOException {

                        if (exc == null) {
                            Files.delete(dir);
                            return CONTINUE;
                        } else {
                            throw exc;
                        }
                    }
                });
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private static String getWSDLFilePath(String filename) {
        String testSrc = System.getProperty("test.src");
        if (testSrc == null) testSrc = ".";
        return Paths.get(testSrc).resolve(filename).toString();
    }

    private static String getWsImport() {
        String javaHome = System.getProperty("java.home");
        String wsimport = javaHome + File.separator + "bin" + File.separator + "wsimport";
        if (System.getProperty("os.name").startsWith("Windows")) {
            wsimport = wsimport.concat(".exe");
        }
        return wsimport;
    }
}
