



import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class ReportOnImportedModuleAnnotation {

    public static void main(String[] args) throws Exception {
        final Path testBasePath = Path.of(System.getProperty("test.src"));
        final Path testOutputPath = Path.of(System.getProperty("test.classes"));

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        fileManager.setLocationFromPaths(StandardLocation.MODULE_SOURCE_PATH, List.of(testBasePath.resolve("mods-src1/")));
        fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(testOutputPath));
        compiler.getTask(new PrintWriter(System.out), fileManager, null, List.of("--module", "annotation,processor"), null, null).call();

        
        fileManager = compiler.getStandardFileManager(null, null, null);
        fileManager.setLocationFromPaths(StandardLocation.MODULE_SOURCE_PATH, List.of(testBasePath.resolve("mods-src2/")));
        fileManager.setLocationFromPaths(StandardLocation.MODULE_PATH, List.of(testOutputPath.resolve("annotation")));
        fileManager.setLocationFromPaths(StandardLocation.ANNOTATION_PROCESSOR_MODULE_PATH, List.of(testOutputPath.resolve("processor")));
        fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(testOutputPath));

        final StringWriter outputWriter = new StringWriter();
        compiler.getTask(outputWriter, fileManager, null, List.of("-XDrawDiagnostics", "--module", "mod"), null, null).call();

        String actualOutput = outputWriter.toString();
        String expectedOutput = Files.readString(testBasePath.resolve("ReportOnImportedModuleAnnotation.out"));

        String lineSep = System.getProperty("line.separator");
        if(!actualOutput.replace(lineSep, "\n").equals(expectedOutput.replace(lineSep, "\n"))) {
            System.err.println("Expected: [" + expectedOutput + "]");
            System.err.println("Received: [" + actualOutput + "]");
            throw new Exception("Invalid output");
        }
    }
}
