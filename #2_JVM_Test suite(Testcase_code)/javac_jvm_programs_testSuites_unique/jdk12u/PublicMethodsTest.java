

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;


public class PublicMethodsTest {

    public static void main(String[] args) {
        Case c = new Case1();

        int[] diffs = new int[1];
        try (Stream<Map.Entry<int[], Map<String, String>>>
                 expected = expectedResults(c)) {
            diffResults(c, expected)
                .forEach(diff -> {
                    System.out.println(diff);
                    diffs[0]++;
                });
        }

        if (diffs[0] > 0) {
            throw new RuntimeException(
                "There were " + diffs[0] + " differences.");
        }
    }

    
    public static class Generate {
        public static void main(String[] args) {
            Case c = new Case1();
            dumpResults(generateResults(c))
                .forEach(System.out::println);
        }
    }

    interface Case {
        Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(.+?)}");

        
        List<String> INTERFACE_METHODS = List.of(
            "", "void m();", "default void m() {}", "static void m() {}"
        );

        
        List<String> CLASS_METHODS = List.of(
            "", "public abstract void m();",
            "public void m() {}", "public static void m() {}"
        );

        
        String template();

        
        
        Map<String, List<String>> replacements();

        
        List<String> replacementKeys();

        
        List<String> classNames();
    }

    static class Case1 implements Case {

        private static final String TEMPLATE = Stream.of(
            "interface I { ${I} }",
            "interface J { ${J} }",
            "interface K extends I, J { ${K} }",
            "abstract class C { ${C} }",
            "abstract class D extends C implements I { ${D} }",
            "abstract class E extends D implements J, K { ${E} }"
        ).collect(joining("\n"));

        private static final Map<String, List<String>> REPLACEMENTS = Map.of(
            "I", INTERFACE_METHODS,
            "J", INTERFACE_METHODS,
            "K", INTERFACE_METHODS,
            "C", CLASS_METHODS,
            "D", CLASS_METHODS,
            "E", CLASS_METHODS
        );

        private static final List<String> REPLACEMENT_KEYS = REPLACEMENTS
            .keySet().stream().sorted().collect(Collectors.toList());

        @Override
        public String template() {
            return TEMPLATE;
        }

        @Override
        public Map<String, List<String>> replacements() {
            return REPLACEMENTS;
        }

        @Override
        public List<String> replacementKeys() {
            return REPLACEMENT_KEYS;
        }

        @Override
        public List<String> classNames() {
            
            
            return REPLACEMENT_KEYS;
        }
    }

    
    
    
    
    
    static Stream<int[]> combinations(Case c) {
        int[] sizes = c.replacementKeys().stream()
                       .mapToInt(key -> c.replacements().get(key).size())
                       .toArray();

        return Stream.iterate(
            new int[sizes.length],
            state -> state != null,
            state -> {
                int[] newState = state.clone();
                for (int i = 0; i < state.length; i++) {
                    if (++newState[i] < sizes[i]) {
                        return newState;
                    }
                    newState[i] = 0;
                }
                
                return null;
            }
        );
    }

    
    static String expandTemplate(Case c, int[] combination) {

        
        Map<String, String> map = new HashMap<>(combination.length * 4 / 3 + 1);
        for (int i = 0; i < combination.length; i++) {
            String key = c.replacementKeys().get(i);
            String repl = c.replacements().get(key).get(combination[i]);
            map.put(key, repl);
        }

        return Case.PLACEHOLDER_PATTERN
            .matcher(c.template())
            .replaceAll(match -> map.get(match.group(1)));
    }

    
    static TestClassLoader compile(String source) throws CompileException {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        if (javac == null) {
            throw new AssertionError("No Java compiler tool found.");
        }

        ErrorsCollector errorsCollector = new ErrorsCollector();
        StandardJavaFileManager standardJavaFileManager =
            javac.getStandardFileManager(errorsCollector, Locale.ROOT,
                                         Charset.forName("UTF-8"));
        TestFileManager testFileManager = new TestFileManager(
            standardJavaFileManager, source);

        JavaCompiler.CompilationTask javacTask;
        try {
            javacTask = javac.getTask(
                null, 
                testFileManager,
                errorsCollector,
                null,
                null,
                List.of(testFileManager.getJavaFileForInput(
                    StandardLocation.SOURCE_PATH,
                    TestFileManager.TEST_CLASS_NAME,
                    JavaFileObject.Kind.SOURCE))
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        javacTask.call();

        if (errorsCollector.hasError()) {
            throw new CompileException(errorsCollector.getErrors());
        }

        return new TestClassLoader(ClassLoader.getSystemClassLoader(),
                                   testFileManager);
    }

    static class CompileException extends Exception {
        CompileException(List<Diagnostic<?>> diagnostics) {
            super(diagnostics.stream()
                             .map(diag -> diag.toString())
                             .collect(Collectors.joining("\n")));
        }
    }

    static class TestFileManager
        extends ForwardingJavaFileManager<StandardJavaFileManager> {
        static final String TEST_CLASS_NAME = "Test";

        private final String testSource;
        private final Map<String, ClassFileObject> classes = new HashMap<>();

        TestFileManager(StandardJavaFileManager fileManager, String source) {
            super(fileManager);
            testSource = "public class " + TEST_CLASS_NAME + " {}\n" +
                         source; 
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location,
                                                  String className,
                                                  JavaFileObject.Kind kind)
        throws IOException {
            if (location == StandardLocation.SOURCE_PATH &&
                kind == JavaFileObject.Kind.SOURCE &&
                TEST_CLASS_NAME.equals(className)) {
                return new SourceFileObject(className, testSource);
            }
            return super.getJavaFileForInput(location, className, kind);
        }

        private static class SourceFileObject extends SimpleJavaFileObject {
            private final String source;

            SourceFileObject(String className, String source) {
                super(
                    URI.create("memory:/src/" +
                               className.replace('.', '/') + ".java"),
                    Kind.SOURCE
                );
                this.source = source;
            }

            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return source;
            }
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location,
                                                   String className,
                                                   JavaFileObject.Kind kind,
                                                   FileObject sibling)
        throws IOException {
            if (kind == JavaFileObject.Kind.CLASS) {
                ClassFileObject cfo = new ClassFileObject(className);
                classes.put(className, cfo);
                return cfo;
            }
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }

        private static class ClassFileObject extends SimpleJavaFileObject {
            final String className;
            ByteArrayOutputStream byteArrayOutputStream;

            ClassFileObject(String className) {
                super(
                    URI.create("memory:/out/" +
                               className.replace('.', '/') + ".class"),
                    Kind.CLASS
                );
                this.className = className;
            }

            @Override
            public OutputStream openOutputStream() throws IOException {
                return byteArrayOutputStream = new ByteArrayOutputStream();
            }

            byte[] getBytes() {
                if (byteArrayOutputStream == null) {
                    throw new IllegalStateException(
                        "No class file written for class: " + className);
                }
                return byteArrayOutputStream.toByteArray();
            }
        }

        byte[] getClassBytes(String className) {
            ClassFileObject cfo = classes.get(className);
            return (cfo == null) ? null : cfo.getBytes();
        }
    }

    static class ErrorsCollector implements DiagnosticListener<JavaFileObject> {
        private final List<Diagnostic<?>> errors = new ArrayList<>();

        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                errors.add(diagnostic);
            }
        }

        boolean hasError() {
            return !errors.isEmpty();
        }

        List<Diagnostic<?>> getErrors() {
            return errors;
        }
    }

    static class TestClassLoader extends ClassLoader implements Closeable {
        private final TestFileManager fileManager;

        public TestClassLoader(ClassLoader parent, TestFileManager fileManager) {
            super(parent);
            this.fileManager = fileManager;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] classBytes = fileManager.getClassBytes(name);
            if (classBytes == null) {
                throw new ClassNotFoundException(name);
            }
            return defineClass(name, classBytes, 0, classBytes.length);
        }

        @Override
        public void close() throws IOException {
            fileManager.close();
        }
    }

    static Map<String, String> generateResult(Case c, ClassLoader cl) {
        return
            c.classNames()
             .stream()
             .map(cn -> {
                 try {
                     return Class.forName(cn, false, cl);
                 } catch (ClassNotFoundException e) {
                     throw new RuntimeException("Class not found: " + cn, e);
                 }
             })
             .flatMap(clazz -> Stream.of(
                 Map.entry(clazz.getName() + ".gM", generateGetMethodResult(clazz)),
                 Map.entry(clazz.getName() + ".gMs", generateGetMethodsResult(clazz))
             ))
             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    static String generateGetMethodResult(Class<?> clazz) {
        try {
            Method m = clazz.getMethod("m");
            return m.getDeclaringClass().getName() + "." + m.getName();
        } catch (NoSuchMethodException e) {
            return "-";
        }
    }

    static String generateGetMethodsResult(Class<?> clazz) {
        return Stream.of(clazz.getMethods())
                     .filter(m -> m.getDeclaringClass() != Object.class)
                     .map(m -> m.getDeclaringClass().getName()
                               + "." + m.getName())
                     .collect(Collectors.joining(", ", "[", "]"));
    }

    static Stream<Map.Entry<int[], Map<String, String>>> generateResults(Case c) {
        return combinations(c)
            .flatMap(comb -> {
                String src = expandTemplate(c, comb);
                try {
                    try (TestClassLoader cl = compile(src)) {
                        
                        return Stream.of(Map.entry(
                            comb,
                            generateResult(c, cl)
                        ));
                    } catch (CompileException e) {
                        
                        return Stream.empty();
                    }
                } catch (IOException ioe) {
                    
                    throw new UncheckedIOException(ioe);
                }
            });
    }

    static Stream<Map.Entry<int[], Map<String, String>>> expectedResults(Case c) {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(
                c.getClass().getResourceAsStream(
                    c.getClass().getSimpleName() + ".results"),
                "UTF-8"
            ));

            return parseResults(r.lines())
                .onClose(() -> {
                    try {
                        r.close();
                    } catch (IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static Stream<Map.Entry<int[], Map<String, String>>> parseResults(
        Stream<String> lines
    ) {
        return lines
            .map(l -> l.split(Pattern.quote("#")))
            .map(lkv -> Map.entry(
                Stream.of(lkv[0].split(Pattern.quote(",")))
                      .mapToInt(Integer::parseInt)
                      .toArray(),
                Stream.of(lkv[1].split(Pattern.quote("|")))
                      .map(e -> e.split(Pattern.quote("=")))
                      .collect(toMap(ekv -> ekv[0], ekv -> ekv[1]))
            ));
    }

    static Stream<String> dumpResults(
        Stream<Map.Entry<int[], Map<String, String>>> results
    ) {
        return results
            .map(le ->
                     IntStream.of(le.getKey())
                              .mapToObj(String::valueOf)
                              .collect(joining(","))
                     + "#" +
                     le.getValue().entrySet().stream()
                       .map(e -> e.getKey() + "=" + e.getValue())
                       .collect(joining("|"))
            );
    }

    static Stream<String> diffResults(
        Case c,
        Stream<Map.Entry<int[], Map<String, String>>> expectedResults
    ) {
        return expectedResults
            .flatMap(exp -> {
                int[] comb = exp.getKey();
                Map<String, String> expected = exp.getValue();

                String src = expandTemplate(c, comb);
                Map<String, String> actual;
                try {
                    try (TestClassLoader cl = compile(src)) {
                        actual = generateResult(c, cl);
                    } catch (CompileException ce) {
                        return Stream.of(src + "\n" +
                                         "got compilation error: " + ce);
                    }
                } catch (IOException ioe) {
                    
                    return Stream.of(src + "\n" +
                                     "got IOException: " + ioe);
                }

                if (actual.equals(expected)) {
                    return Stream.empty();
                } else {
                    Map<String, String> diff = new HashMap<>(expected);
                    diff.entrySet().removeAll(actual.entrySet());
                    return Stream.of(
                        diff.entrySet()
                            .stream()
                            .map(e -> "expected: " + e.getKey() + ": " +
                                      e.getValue() + "\n" +
                                      "  actual: " + e.getKey() + ": " +
                                      actual.get(e.getKey()) + "\n")
                            .collect(joining("\n", src + "\n\n", "\n"))
                    );
                }
            });
    }
}
