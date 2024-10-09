

package jdk.testlibrary;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Deprecated
public final class JDKToolFinder {

    private JDKToolFinder() {
    }

    
    public static String getJDKTool(String tool) {

        
        try {
            return getTool(tool, "test.jdk");
        } catch (FileNotFoundException e) {

        }

        
        try {
            return getTool(tool, "compile.jdk");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find " + tool +
                    ", looked in test.jdk (" + System.getProperty("test.jdk") +
                    ") and compile.jdk (" + System.getProperty("compile.jdk") + ")");
        }
    }

    
    public static String getCompileJDKTool(String tool) {
        try {
            return getTool(tool, "compile.jdk");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    
    public static String getTestJDKTool(String tool) {
        try {
            return getTool(tool, "test.jdk");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getTool(String tool, String property) throws FileNotFoundException {
        String jdkPath = System.getProperty(property);

        if (jdkPath == null) {
            throw new RuntimeException(
                    "System property '" + property + "' not set. This property is normally set by jtreg. "
                    + "When running test separately, set this property using '-D" + property + "=/path/to/jdk'.");
        }

        Path toolName = Paths.get("bin", tool + (isWindows() ? ".exe" : ""));

        Path jdkTool = Paths.get(jdkPath, toolName.toString());
        if (!jdkTool.toFile().exists()) {
            throw new FileNotFoundException("Could not find file " + jdkTool.toAbsolutePath());
        }

        return jdkTool.toAbsolutePath().toString();
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }
}
