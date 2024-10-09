



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
public class RemoteRuntimeImageTest {
    
    private static final String JRTFS_JAR = "jrt-fs.jar";
    private static final String SRC_DIR = System.getProperty("test.src");
    private static final String CLASSES_DIR = "classes";
    private static final String TEST_JAVAHOME = System.getProperty("test.jdk");

    public static void main(String[] args) throws Exception {
        
        String jdk8Home = System.getenv("JDK8_HOME");
        if (jdk8Home == null || jdk8Home.isEmpty()) {
            System.err.println("Failed to locate JDK8 with system "
                + "environment variable 'JDK8_HOME'. Skip testing!");
            return;
        }

        Path jdk8Path = getJdk8Path(jdk8Home);
        if (!isJdk8(jdk8Path)) {
            System.err.println("This test is only for JDK 8. Skip testing");
            return;
        }

        String java = jdk8Path.resolve("bin/java").toAbsolutePath().toString();
        String javac = jdk8Path.resolve("bin/javac").toAbsolutePath().toString();
        Files.createDirectories(Paths.get(".", CLASSES_DIR));
        String jrtJar = Paths.get(TEST_JAVAHOME, "lib", JRTFS_JAR).toAbsolutePath().toString();

        
        List<List<String>> cmds = Arrays.asList(
                
                Arrays.asList(javac, "-d", CLASSES_DIR, "-cp", jrtJar,
                        SRC_DIR + File.separatorChar + "Main.java"),
                
                Arrays.asList(java, "-cp", CLASSES_DIR, "Main", TEST_JAVAHOME),
                
                
                Arrays.asList(java, "-cp", CLASSES_DIR + File.pathSeparatorChar + jrtJar,
                        "Main", TEST_JAVAHOME, "installed")
                );

        cmds.forEach(cmd -> execCmd(cmd));
    }

    private static void execCmd(List<String> command){
        System.out.println();
        System.out.println("Executing command: " + command);
        Process p = null;
        try {
            p = new ProcessBuilder(command).inheritIO().start();
            p.waitFor();
            int rc = p.exitValue();
            if (rc != 0) {
                throw new RuntimeException("Unexpected exit code:" + rc);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null && p.isAlive()){
                p.destroy();
            }
        }
    }

    private static Path getJdk8Path(String jdk8Home) {
        Path jdk8Path = Paths.get(jdk8Home);
        
        return Files.isDirectory(jdk8Path)? jdk8Path : jdk8Path.getParent().getParent();
    }

    private static boolean isJdk8(Path jdk8Home) throws FileNotFoundException, IOException {
        File file = jdk8Home.resolve("release").toFile();
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
        }

        String version = props.getProperty("JAVA_VERSION", "");
        System.out.println("JAVA_VERSION is " + version);
        return version.startsWith("\"1.8");
    }
}
