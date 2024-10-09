

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CompilerUtils {
    private CompilerUtils() { }

    
    public static boolean compile(Path source, Path destination, String... options)
        throws IOException
    {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager jfm = compiler.getStandardFileManager(null, null, null);

        List<Path> sources
            = Files.find(source, Integer.MAX_VALUE,
                         (file, attrs) -> (file.toString().endsWith(".java")))
                   .collect(Collectors.toList());

        Files.createDirectories(destination);
        jfm.setLocationFromPaths(StandardLocation.CLASS_OUTPUT,
                                 Arrays.asList(destination));

        List<String> opts = Arrays.asList(options);
        JavaCompiler.CompilationTask task
            = compiler.getTask(null, jfm, null, opts, null,
                               jfm.getJavaFileObjectsFromPaths(sources));

        return task.call();
    }

    
    public static boolean compileModule(Path source, Path destination,
                                        String moduleName, String... options) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager jfm = compiler.getStandardFileManager(null, null, null);

        try {
            Files.createDirectories(destination);
            jfm.setLocationFromPaths(StandardLocation.CLASS_OUTPUT,
                                     Arrays.asList(destination));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Stream<String> opts = Arrays.stream(new String[] {
            "--module-source-path", source.toString(), "-m", moduleName
        });
        List<String> javacOpts = Stream.concat(opts, Arrays.stream(options))
                                        .collect(Collectors.toList());
        JavaCompiler.CompilationTask task
            = compiler.getTask(null, jfm, null, javacOpts, null, null);
        return task.call();
    }


    public static void cleanDir(Path dir) throws IOException {
        if (Files.notExists(dir)) return;

        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
            {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                    throws IOException
            {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    
                    throw e;
                }
            }
        });
        Files.deleteIfExists(dir);
    }
}
