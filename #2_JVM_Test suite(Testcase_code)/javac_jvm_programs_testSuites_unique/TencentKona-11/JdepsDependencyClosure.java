

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JdepsDependencyClosure {

    static boolean VERBOSE = false;
    static boolean COMPARE_TEXT = true;

    static final String JDEPS_SUMMARY_TEXT_FORMAT = "%s -> %s%n";
    static final String JDEPS_VERBOSE_TEXT_FORMAT = "   %-50s -> %-50s %s%n";

    
    static class TestCaseData {
        final Map<String, Set<String>> expectedDependencies;
        final String expectedText;
        final String[] args;
        final boolean closure;

        TestCaseData(Map<String, Set<String>> expectedDependencies,
                        String expectedText,
                        boolean closure,
                        String[] args) {
            this.expectedDependencies = expectedDependencies;
            this.expectedText = expectedText;
            this.closure = closure;
            this.args = args;
        }

        public void test() {
            if (expectedDependencies != null) {
                String format = closure
                        ? "Running (closure): jdeps %s %s %s %s"
                        : "Running: jdeps %s %s %s %s";
                System.out.println(String.format(format, (Object[])args));
            }
            JdepsDependencyClosure.test(args, expectedDependencies, expectedText, closure);
        }

        
        public static TestCaseData make(String pattern, String arcPath, String[][] classes,
                String[][] dependencies, String[][] archives, boolean closure) {
            final String[] args = new String[] {
                "-e", pattern, "-v", arcPath
            };
            Map<String, Set<String>> expected = new HashMap<>();
            String expectedText = "";
            for (int i=0; i<classes.length; i++) {
                final int index = i;
                expectedText += Stream.of(classes[i])
                    .map((cn) -> String.format(JDEPS_VERBOSE_TEXT_FORMAT, cn,
                            dependencies[index][0], archives[index][2]))
                    .reduce(String.format(JDEPS_SUMMARY_TEXT_FORMAT, archives[i][0],
                            archives[index][3]), (s1,s2) -> s1.concat(s2));
                for (String cn : classes[index]) {
                    expected.putIfAbsent(cn, new HashSet<>());
                    expected.get(cn).add(dependencies[index][0]);
                }
            }
            return new TestCaseData(expected, expectedText, closure, args);
        }

        public static TestCaseData valueOf(String[] args) {
            if (args.length == 1 && args[0].startsWith("--test:")) {
                
                int index = Integer.parseInt(args[0].substring("--test:".length()));
                if (index >= dataSuppliers.size()) {
                    throw new RuntimeException("No such test case: " + index
                            + " - available testcases are [0.."
                            + (dataSuppliers.size()-1) + "]");
                }
                return dataSuppliers.get(index).get();
            } else {
                
                
                
                return new TestCaseData(null, null, true, args);
            }
        }

    }

    static TestCaseData makeTestCaseOne() {
        final String arcPath = System.getProperty("test.classes", "build/classes");
        final String arcName = Paths.get(arcPath).getFileName().toString();
        final String[][] classes = new String[][] {
            {"use.indirect2.UseJdkInternalIndirectly2", "use.internal.UseClassWithJdkInternal"},
        };
        final String[][] dependencies = new String[][] {
            {"use.internal.UseJdkInternalClass"},
        };
        final String[][] archives = new String[][] {
            {arcName, arcPath, arcName, arcPath},
        };
        return TestCaseData.make("use.internal.UseJdkInternalClass", arcPath, classes,
                dependencies, archives, false);
    }

    static TestCaseData makeTestCaseTwo() {
        String arcPath = System.getProperty("test.classes", "build/classes");
        String arcName = Paths.get(arcPath).getFileName().toString();
        String[][] classes = new String[][] {
            {"use.internal.UseJdkInternalClass", "use.internal.UseJdkInternalClass2"}
        };
        String[][] dependencies = new String[][] {
            {"sun.security.x509.X509CertInfo"}
        };
        String[][] archive = new String[][] {
            {arcName, arcPath, "JDK internal API (java.base)", "java.base"},
        };
        return TestCaseData.make("sun.security.x509.X509CertInfo", arcPath, classes,
                dependencies, archive, false);
    }

    static TestCaseData makeTestCaseThree() {
        final String arcPath = System.getProperty("test.classes", "build/classes");
        final String arcName = Paths.get(arcPath).getFileName().toString();
        final String[][] classes = new String[][] {
            {"use.indirect2.UseJdkInternalIndirectly2", "use.internal.UseClassWithJdkInternal"},
            {"use.indirect.UseJdkInternalIndirectly"}
        };
        final String[][] dependencies = new String[][] {
            {"use.internal.UseJdkInternalClass"},
            {"use.internal.UseClassWithJdkInternal"}
        };
        final String[][] archives = new String[][] {
            {arcName, arcPath, arcName, arcPath},
            {arcName, arcPath, arcName, arcPath}
        };
        return TestCaseData.make("use.internal.UseJdkInternalClass", arcPath, classes,
                dependencies, archives, true);
    }


    static TestCaseData makeTestCaseFour() {
        final String arcPath = System.getProperty("test.classes", "build/classes");
        final String arcName = Paths.get(arcPath).getFileName().toString();
        final String[][] classes = new String[][] {
            {"use.internal.UseJdkInternalClass", "use.internal.UseJdkInternalClass2"},
            {"use.indirect2.UseJdkInternalIndirectly2", "use.internal.UseClassWithJdkInternal"},
            {"use.indirect.UseJdkInternalIndirectly"}
        };
        final String[][] dependencies = new String[][] {
            {"sun.security.x509.X509CertInfo"},
            {"use.internal.UseJdkInternalClass"},
            {"use.internal.UseClassWithJdkInternal"}
        };
        final String[][] archives = new String[][] {
            {arcName, arcPath, "JDK internal API (java.base)", "java.base"},
            {arcName, arcPath, arcName, arcPath},
            {arcName, arcPath, arcName, arcPath}
        };
        return TestCaseData.make("sun.security.x509.X509CertInfo", arcPath, classes, dependencies,
                archives, true);
    }

    static final List<Supplier<TestCaseData>> dataSuppliers = Arrays.asList(
        JdepsDependencyClosure::makeTestCaseOne,
        JdepsDependencyClosure::makeTestCaseTwo,
        JdepsDependencyClosure::makeTestCaseThree,
        JdepsDependencyClosure::makeTestCaseFour
    );



    
    static class OutputStreamParser extends OutputStream {
        
        
        
        
        final Map<String, Set<String>> deps;
        final StringBuilder text = new StringBuilder();

        StringBuilder[] lines = { new StringBuilder(), new StringBuilder() };
        int line = 0;
        int sepi = 0;
        char[] sep;

        public OutputStreamParser(Map<String, Set<String>> deps) {
            this.deps = deps;
            this.sep = System.getProperty("line.separator").toCharArray();
        }

        @Override
        public void write(int b) throws IOException {
            lines[line].append((char)b);
            if (b == sep[sepi]) {
                if (++sepi == sep.length) {
                    text.append(lines[line]);
                    if (lines[0].toString().startsWith("  ")) {
                        throw new RuntimeException("Bad formatting: "
                                + "summary line missing for\n"+lines[0]);
                    }
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    if (line == 1) {
                        
                        String line1 = lines[0].toString();
                        String line2 = lines[1].toString();
                        if (line2.startsWith("  ")) {
                            
                            parse(line1, line2);
                            
                            lines[1] = new StringBuilder();
                        } else {
                            
                            
                            
                            lines[0] = lines[1];
                            lines[1] = new StringBuilder();
                         }
                    } else {
                        
                        
                        line = 1;
                    }
                    sepi = 0;
                }
            } else {
                sepi = 0;
            }
        }

        
        
        
        void parse(String line1, String line2) {
            String archive = line1.substring(0, line1.indexOf(" -> "));
            int l2ArrowIndex = line2.indexOf(" -> ");
            String className = line2.substring(2, l2ArrowIndex).replace(" ", "");
            String depdescr = line2.substring(l2ArrowIndex + 4);
            String depclass = depdescr.substring(0, depdescr.indexOf(" "));
            deps.computeIfAbsent(archive, (k) -> new HashSet<>());
            deps.get(archive).add(className);
            if (VERBOSE) {
                System.out.println(archive+": "+className+" depends on "+depclass);
            }
        }

    }

    
    public static void main(String[] args) {
        runWithLocale(Locale.ENGLISH, TestCaseData.valueOf(args)::test);
    }

    private static void runWithLocale(Locale loc, Runnable run) {
        final Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(loc);
        try {
            run.run();
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }


    public static void test(String[] args, Map<String, Set<String>> expected,
            String expectedText, boolean closure) {
        try {
            doTest(args, expected, expectedText, closure);
        } catch (Throwable t) {
            try {
                printDiagnostic(args, expectedText, t, closure);
            } catch(Throwable tt) {
                throw t;
            }
            throw t;
        }
    }

    static class TextFormatException extends RuntimeException {
        final String expected;
        final String actual;
        TextFormatException(String message, String expected, String actual) {
            super(message);
            this.expected = expected;
            this.actual = actual;
        }
    }

    public static void printDiagnostic(String[] args, String expectedText,
            Throwable t, boolean closure) {
        if (expectedText != null || t instanceof TextFormatException) {
            System.err.println("=====   TEST FAILED   =======");
            System.err.println("command: " + Stream.of(args)
                    .reduce("jdeps", (s1,s2) -> s1.concat(" ").concat(s2)));
            System.err.println("===== Expected Output =======");
            System.err.append(expectedText);
            System.err.println("===== Command  Output =======");
            if (t instanceof TextFormatException) {
                System.err.print(((TextFormatException)t).actual);
            } else {
                com.sun.tools.jdeps.Main.run(args, new PrintWriter(System.err));
                if (closure) System.err.println("... (closure not available) ...");
            }
            System.err.println("=============================");
        }
    }

    public static void doTest(String[] args, Map<String, Set<String>> expected,
            String expectedText, boolean closure) {
        if (args.length < 3 || !"-e".equals(args[0]) || !"-v".equals(args[2])) {
            System.err.println("Syntax: -e <classname> -v [list of jars or directories]");
            return;
        }
        Map<String, Map<String, Set<String>>> alldeps = new HashMap<>();
        String depName = args[1];
        List<String> search = new ArrayList<>();
        search.add(depName);
        Set<String> searched = new LinkedHashSet<>();
        StringBuilder text = new StringBuilder();
        while(!search.isEmpty()) {
            args[1] = search.remove(0);
            if (VERBOSE) {
                System.out.println("Looking for " + args[1]);
            }
            searched.add(args[1]);
            Map<String, Set<String>> deps =
                    alldeps.computeIfAbsent(args[1], (k) -> new HashMap<>());
            OutputStreamParser parser = new OutputStreamParser(deps);
            PrintWriter writer = new PrintWriter(parser);
            com.sun.tools.jdeps.Main.run(args, writer);
            if (VERBOSE) {
                System.out.println("Found: " + deps.values().stream()
                        .flatMap(s -> s.stream()).collect(Collectors.toSet()));
            }
            if (expectedText != null) {
                text.append(parser.text.toString());
            }
            search.addAll(deps.values().stream()
                    .flatMap(s -> s.stream())
                    .filter(k -> !searched.contains(k))
                    .collect(Collectors.toSet()));
            if (!closure) break;
        }

        
        final Set<String> classes = alldeps.values().stream()
                .flatMap((m) -> m.values().stream())
                .flatMap(s -> s.stream()).collect(Collectors.toSet());
        Map<String, Set<String>> result = new HashMap<>();
        for (String c : classes) {
            Set<String> archives = new HashSet<>();
            Set<String> dependencies = new HashSet<>();
            for (String d : alldeps.keySet()) {
                Map<String, Set<String>> m = alldeps.get(d);
                for (String a : m.keySet()) {
                    Set<String> s = m.get(a);
                    if (s.contains(c)) {
                        archives.add(a);
                        dependencies.add(d);
                    }
                }
            }
            result.put(c, dependencies);
            System.out.println(c + " " + archives + " depends on " + dependencies);
        }

        
        if (expectedText != null && COMPARE_TEXT) {
            
            if (text.toString().equals(expectedText)) {
                System.out.println("SUCCESS - got expected text");
            } else {
                throw new TextFormatException("jdeps output is not as expected",
                                expectedText, text.toString());
            }
        }
        if (expected != null) {
            if (expected.equals(result)) {
                System.out.println("SUCCESS - found expected dependencies");
            } else if (expectedText == null) {
                throw new RuntimeException("Bad dependencies: Expected " + expected
                        + " but found " + result);
            } else {
                throw new TextFormatException("Bad dependencies: Expected "
                        + expected
                        + " but found " + result,
                        expectedText, text.toString());
            }
        }
    }
}
