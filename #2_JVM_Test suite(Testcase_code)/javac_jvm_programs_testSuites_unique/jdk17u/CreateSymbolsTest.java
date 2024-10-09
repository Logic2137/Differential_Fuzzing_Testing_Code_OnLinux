import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class CreateSymbolsTest {

    static final String CREATE_SYMBOLS_NAME = "symbolgenerator.CreateSymbols";

    public static void main(String... args) throws Exception {
        new CreateSymbolsTest().doRun();
    }

    void doRun() throws Exception {
        Path testClasses = Paths.get(System.getProperty("test.classes"));
        Path compileDir = testClasses.resolve("data");
        deleteRecursively(compileDir);
        Files.createDirectories(compileDir);
        Path createSymbols = findFile("../../make/langtools/src/classes/build/tools/symbolgenerator/CreateSymbols.java");
        if (createSymbols == null) {
            System.err.println("Warning: cannot find CreateSymbols, skipping.");
            return;
        }
        Path createTestImpl = findFile("tools/javac/platform/createsymbols/CreateSymbolsTestImpl.java");
        if (createTestImpl == null) {
            throw new AssertionError("Warning: cannot find CreateSymbolsTestImpl, skipping.");
        }
        Path toolBox = findFile("tools/lib/toolbox/");
        if (toolBox == null) {
            throw new AssertionError("Warning: cannot find ToolBox, skipping.");
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null)) {
            List<Path> files = new ArrayList<>();
            files.add(createSymbols);
            files.add(createTestImpl);
            files.add(toolBox.resolve("AbstractTask.java"));
            files.add(toolBox.resolve("JavacTask.java"));
            files.add(toolBox.resolve("Task.java"));
            files.add(toolBox.resolve("ToolBox.java"));
            Boolean res = compiler.getTask(null, null, null, List.of("-d", compileDir.toAbsolutePath().toString(), "-g", "--add-modules", "jdk.jdeps", "--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED", "--add-exports", "jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED", "--add-exports", "jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED", "--add-exports", "jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED", "--add-exports", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED", "--add-exports", "jdk.jdeps/com.sun.tools.classfile=ALL-UNNAMED"), null, fm.getJavaFileObjectsFromPaths(files)).call();
            if (!res) {
                throw new IllegalStateException("Cannot compile test.");
            }
        }
        URLClassLoader cl = new URLClassLoader(new URL[] { testClasses.toUri().toURL(), compileDir.toUri().toURL() });
        Class<?> createSymbolTest = cl.loadClass("CreateSymbolsTestImpl");
        createSymbolTest.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
    }

    Path findFile(String path) {
        Path testSrc = Paths.get(System.getProperty("test.src", "."));
        for (Path d = testSrc; d != null; d = d.getParent()) {
            if (Files.exists(d.resolve("TEST.ROOT"))) {
                Path file = d.resolve(path);
                if (Files.exists(file)) {
                    return file;
                }
            }
        }
        return null;
    }

    void deleteRecursively(Path dir) throws IOException {
        Files.walkFileTree(dir, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
