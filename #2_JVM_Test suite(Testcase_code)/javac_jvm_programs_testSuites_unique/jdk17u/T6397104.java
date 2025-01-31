import java.io.File;
import java.net.URI;
import java.util.Arrays;
import javax.tools.*;
import javax.tools.JavaFileManager.Location;
import static javax.tools.StandardLocation.CLASS_OUTPUT;

public class T6397104 {

    JavaCompiler tool = ToolProvider.getSystemJavaCompiler();

    void test(StandardJavaFileManager fm, Location location, File siblingFile, String relName, String expectedPath) throws Exception {
        JavaFileObject sibling = siblingFile == null ? null : fm.getJavaFileObjectsFromFiles(Arrays.asList(siblingFile)).iterator().next();
        FileObject fileObject = fm.getFileForOutput(location, "java.lang", relName, sibling);
        File expectedFile = new File(expectedPath).getCanonicalFile();
        File fileObjectFile = new File(fileObject.toUri()).getCanonicalFile();
        if (!fileObjectFile.equals(expectedFile))
            throw new AssertionError("Expected " + expectedFile + ", got " + fileObjectFile);
        System.err.format("OK: (%s, %s) => %s%n", siblingFile, relName, fileObjectFile);
    }

    void test(boolean hasLocation, File siblingFile, String relName, String expectedPath) throws Exception {
        System.err.format("test: hasLocation:%s, siblingFile:%s, relName:%s, expectedPath:%s%n", hasLocation, siblingFile, relName, expectedPath);
        try (StandardJavaFileManager fm = tool.getStandardFileManager(null, null, null)) {
            if (hasLocation) {
                for (Location location : StandardLocation.values()) {
                    System.err.format("  location:%s, moduleLocn:%b%n", location, location.isModuleOrientedLocation());
                    if (!location.isOutputLocation()) {
                        continue;
                    }
                    fm.setLocation(location, Arrays.asList(new File(".")));
                    test(fm, location, siblingFile, relName, expectedPath);
                }
            } else {
                test(fm, CLASS_OUTPUT, siblingFile, relName, expectedPath);
            }
        }
    }

    public static void main(String... args) throws Exception {
        T6397104 tester = new T6397104();
        tester.test(false, new File(new File("foo", "bar"), "baz.java"), "qux/baz.xml", "foo/bar/baz.xml");
        tester.test(false, null, "qux/baz.xml", "baz.xml");
        tester.test(true, new File(new File("foo", "bar"), "baz.java"), "qux/baz.xml", "./java/lang/qux/baz.xml");
        tester.test(true, null, "qux/baz.xml", "./java/lang/qux/baz.xml");
    }
}
