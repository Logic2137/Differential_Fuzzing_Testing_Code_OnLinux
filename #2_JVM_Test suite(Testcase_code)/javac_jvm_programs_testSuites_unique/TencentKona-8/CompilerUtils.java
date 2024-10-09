

package com.oracle.java.testlibrary.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;


public final class CompilerUtils {
    private CompilerUtils() {
    }

    
    public static boolean compile(Path source, Path destination, String... options)
            throws IOException {
        return compile(source, destination, true, options);
    }

    

    public static boolean compile(Path source, Path destination, boolean recurse, String... options)
            throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            
            throw new UnsupportedOperationException("Unable to get system java compiler. "
                    + "Perhaps, jdk.compiler module is not available.");
        }
        StandardJavaFileManager jfm = compiler.getStandardFileManager(null, null, null);

        List<File> sources
                = Files.find(source, (recurse ? Integer.MAX_VALUE : 1),
                (file, attrs) -> (file.toString().endsWith(".java")))
                .map(e -> e.toFile()) 
                .collect(Collectors.toList());

        Files.createDirectories(destination);

        jfm.setLocation(StandardLocation.CLASS_OUTPUT,
                Collections.singletonList(destination.toFile()));

        List<String> opts = Arrays.asList(options);
        JavaCompiler.CompilationTask task
                = compiler.getTask(null, jfm, null, opts, null,
                jfm.getJavaFileObjectsFromFiles(sources)); 

        return task.call();
    }
}
