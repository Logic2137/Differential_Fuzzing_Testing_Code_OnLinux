



import java.io.IOException;
import java.util.Arrays;
import javax.tools.*;

public class T6198196 {
    static String pkginf = "package-info";
    static void test(String pathname, String filename, boolean result) throws IOException {
        try (StandardJavaFileManager fm =
                ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null)) {
            JavaFileObject fo =
                    fm.getJavaFileObjectsFromStrings(Arrays.asList(pathname)).iterator().next();
            if (result != fo.isNameCompatible(filename, JavaFileObject.Kind.SOURCE))
                throw new AssertionError("endsWith(" + pathname + ", "
                                         + filename + ") != " + result);
            System.out.format("OK: endsWith(%s, %s) = %s%n", pathname, filename, result);
        }
    }
    public static void main(String[] args) throws IOException {
        boolean windows = System.getProperty("os.name").startsWith("Windows");
        test("/x/y/z/package-info.java", pkginf, true);
        if (windows) {
            test("\\x\\y\\z\\package-info.java", pkginf, true);
            test("..\\x\\y\\z\\package-info.java", pkginf, true);
        } else {
            test("\\x\\y\\z\\package-info.java", pkginf, false);
            test("..\\x\\y\\z\\package-info.java", pkginf, false);
        }
        test("Package-info.java", pkginf, false);
        test("../x/y/z/package-info.java", pkginf, true);
        test("/x/y/z/package-info.java", pkginf, true);
        test("x/y/z/package-info.java", pkginf, true);
        test("package-info.java", pkginf, true);
    }
}
