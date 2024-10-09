import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import com.sun.tools.javac.util.Assert;

public class RunCodingRules {

    public static void main(String... args) throws Exception {
        new RunCodingRules().run();
    }

    public void run() throws Exception {
        Path testSrc = Paths.get(System.getProperty("test.src", "."));
        Path targetDir = Paths.get(".");
        List<Path> sourceDirs = null;
        Path crulesDir = null;
        Path mainSrcDir = null;
        for (Path d = testSrc; d != null; d = d.getParent()) {
            if (Files.exists(d.resolve("TEST.ROOT"))) {
                d = d.getParent();
                Path toolsPath = d.resolve("make/tools");
                if (Files.exists(toolsPath)) {
                    mainSrcDir = d.resolve("src");
                    crulesDir = toolsPath;
                    sourceDirs = Files.walk(mainSrcDir, 1).map(p -> p.resolve("share/classes")).filter(p -> Files.isDirectory(p)).collect(Collectors.toList());
                    break;
                }
            }
        }
        if (sourceDirs == null || crulesDir == null) {
            System.err.println("Warning: sources not found, test skipped.");
            return;
        }
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = javaCompiler.getStandardFileManager(null, null, null)) {
            DiagnosticListener<JavaFileObject> noErrors = diagnostic -> {
                Assert.check(diagnostic.getKind() != Diagnostic.Kind.ERROR, diagnostic.toString());
            };
            String FS = File.separator;
            String PS = File.pathSeparator;
            List<File> crulesFiles = Files.walk(crulesDir).filter(entry -> entry.getFileName().toString().endsWith(".java")).filter(entry -> entry.getParent().endsWith("crules")).map(entry -> entry.toFile()).collect(Collectors.toList());
            Path crulesTarget = targetDir.resolve("crules");
            Files.createDirectories(crulesTarget);
            List<String> crulesOptions = Arrays.asList("--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED", "--add-exports", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED", "--add-exports", "jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED", "--add-exports", "jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED", "--add-exports", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED", "-d", crulesTarget.toString());
            javaCompiler.getTask(null, fm, noErrors, crulesOptions, null, fm.getJavaFileObjectsFromFiles(crulesFiles)).call();
            Path registration = crulesTarget.resolve("META-INF/services/com.sun.source.util.Plugin");
            Files.createDirectories(registration.getParent());
            try (Writer metaInfServices = Files.newBufferedWriter(registration, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                metaInfServices.write("crules.CodingRulesAnalyzerPlugin\n");
            }
            List<File> propertiesParserFiles = Files.walk(crulesDir.resolve("propertiesparser")).filter(entry -> entry.getFileName().toString().endsWith(".java")).map(entry -> entry.toFile()).collect(Collectors.toList());
            Path propertiesParserTarget = targetDir.resolve("propertiesParser");
            Files.createDirectories(propertiesParserTarget);
            List<String> propertiesParserOptions = Arrays.asList("-d", propertiesParserTarget.toString());
            javaCompiler.getTask(null, fm, noErrors, propertiesParserOptions, null, fm.getJavaFileObjectsFromFiles(propertiesParserFiles)).call();
            Path genSrcTarget = targetDir.resolve("gensrc");
            ClassLoader propertiesParserLoader = new URLClassLoader(new URL[] { propertiesParserTarget.toUri().toURL(), crulesDir.toUri().toURL() });
            Class propertiesParserClass = Class.forName("propertiesparser.PropertiesParser", false, propertiesParserLoader);
            Method propertiesParserRun = propertiesParserClass.getDeclaredMethod("run", String[].class, PrintStream.class);
            String compilerProperties = "jdk.compiler/share/classes/com/sun/tools/javac/resources/compiler.properties";
            Path propertiesPath = mainSrcDir.resolve(compilerProperties.replace("/", FS));
            Path genSrcTargetDir = genSrcTarget.resolve(mainSrcDir.relativize(propertiesPath.getParent()));
            Files.createDirectories(genSrcTargetDir);
            String[] propertiesParserRunOptions = new String[] { "-compile", propertiesPath.toString(), genSrcTargetDir.toString() };
            Object result = propertiesParserRun.invoke(null, propertiesParserRunOptions, System.err);
            if (!(result instanceof Boolean) || !(Boolean) result) {
                throw new AssertionError("Cannot parse properties: " + result);
            }
            List<File> sources = sourceDirs.stream().flatMap(dir -> silentFilesWalk(dir)).filter(entry -> entry.getFileName().toString().endsWith(".java")).map(p -> p.toFile()).collect(Collectors.toList());
            Path sourceTarget = targetDir.resolve("classes");
            Files.createDirectories(sourceTarget);
            String processorPath = crulesTarget + PS + crulesDir;
            List<String> options = Arrays.asList("-d", sourceTarget.toString(), "--module-source-path", mainSrcDir + FS + "*" + FS + "share" + FS + "classes" + PS + genSrcTarget + FS + "*" + FS + "share" + FS + "classes", "-XDaccessInternalAPI", "-processorpath", processorPath, "-Xplugin:coding_rules");
            javaCompiler.getTask(null, fm, noErrors, options, null, fm.getJavaFileObjectsFromFiles(sources)).call();
        }
    }

    Stream<Path> silentFilesWalk(Path dir) throws IllegalStateException {
        try {
            return Files.walk(dir);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
