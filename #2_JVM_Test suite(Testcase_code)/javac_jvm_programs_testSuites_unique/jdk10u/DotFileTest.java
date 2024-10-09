



import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class DotFileTest {
    public static void main(String... args) throws Exception {
        int errors = 0;
        errors += new DotFileTest().run();
        if (errors > 0)
            throw new Exception(errors + " errors found");
    }

    final Path dir;
    final Path dotoutput;
    DotFileTest() {
        this.dir = Paths.get(System.getProperty("test.classes", "."));
        this.dotoutput = dir.resolve("dots");
    }

    int run() throws IOException {
        File testDir = dir.toFile();
        
        test(new File(testDir, "Test.class"),
             new String[] {"java.lang", "p"},
             new String[] {"compact1", "not found"});
        
        test(new File(testDir, "p"),
             new String[] {"java.lang", "java.util", "java.lang.management", "javax.crypto"},
             new String[] {"compact1", "compact1", "compact3", "compact1"},
             new String[] {"-classpath", testDir.getPath()});
        
        test(new File(testDir, "Test.class"),
             new String[] {"java.lang.Object", "java.lang.String", "p.Foo", "p.Bar"},
             new String[] {"compact1", "compact1", "not found", "not found"},
             new String[] {"-verbose:class"});
        
        test(new File(testDir, "p"),
             new String[] {"java.lang", "java.util", "java.lang.management", "javax.crypto", "p"},
             new String[] {"compact1", "compact1", "compact3", "compact1", "p"},
             new String[] {"-classpath", testDir.getPath(), "-verbose:package", "-filter:none"});
        
        test(new File(testDir, "p"),
             new String[] {"java.lang", "java.util", "java.lang.management", "javax.crypto"},
             new String[] {"compact1", "compact1", "compact3", "compact1"},
             new String[] {"-classpath", testDir.getPath(), "-verbose:package", "-filter:archive"});
        
        test(new File(testDir, "Test.class"),
             new String[] {"p.Foo", "p.Bar"},
             new String[] {"not found", "not found"},
             new String[] {"-verbose:class", "-p", "p"});
        
        test(new File(testDir, "Test.class"),
             new String[] {"p.Foo", "p.Bar"},
             new String[] {"not found", "not found"},
             new String[] {"-verbose:class", "-e", "p\\..*"});
        test(new File(testDir, "Test.class"),
             new String[] {"java.lang"},
             new String[] {"compact1"},
             new String[] {"-verbose:package", "-e", "java\\.lang\\..*"});
        
        test(new File(testDir, "Test.class"),
             new String[] {"java.lang.Object", "java.lang.String", "p.Foo", "p.Bar"},
             new String[] {"compact1", "compact1", testDir.getName(), testDir.getName()},
             new String[] {"-v", "-classpath", testDir.getPath()});

        testSummary(new File(testDir, "Test.class"),
             new String[] {"java.base", testDir.getName()},
             new String[] {"compact1", ""},
             new String[] {"-classpath", testDir.getPath()});
        testSummary(new File(testDir, "Test.class"),
             new String[] {"java.lang", "p"},
             new String[] {"compact1", testDir.getName()},
             new String[] {"-v", "-classpath", testDir.getPath()});
        return errors;
    }

    void test(File file, String[] expect, String[] profiles) throws IOException {
        test(file, expect, profiles, new String[0]);
    }

    void test(File file, String[] expect, String[] profiles, String[] options)
        throws IOException
    {
        Path dotfile = dotoutput.resolve(file.toPath().getFileName().toString() + ".dot");

        List<String> args = new ArrayList<>(Arrays.asList(options));
        args.add("-dotoutput");
        args.add(dotoutput.toString());
        if (file != null) {
            args.add(file.getPath());
        }

        Map<String,String> result = jdeps(args, dotfile);
        checkResult("dependencies", expect, result.keySet());

        
        List<String> argsWithDashP = new ArrayList<>();
        argsWithDashP.add("-P");
        argsWithDashP.addAll(args);

        result = jdeps(argsWithDashP, dotfile);
        checkResult("profiles", expect, profiles, result);
    }

    void testSummary(File file, String[] expect, String[] profiles, String[] options)
        throws IOException
    {
        Path dotfile = dotoutput.resolve("summary.dot");

        List<String> args = new ArrayList<>(Arrays.asList(options));
        args.add("-dotoutput");
        args.add(dotoutput.toString());
        if (file != null) {
            args.add(file.getPath());
        }

        Map<String,String> result = jdeps(args, dotfile);
        checkResult("dependencies", expect, result.keySet());

        
        List<String> argsWithDashP = new ArrayList<>();
        argsWithDashP.add("-P");
        argsWithDashP.addAll(args);

        result = jdeps(argsWithDashP, dotfile);
        checkResult("profiles", expect, profiles, result);
    }

    Map<String,String> jdeps(List<String> args, Path dotfile) throws IOException {
        if (Files.exists(dotoutput)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dotoutput)) {
                for (Path p : stream) {
                    Files.delete(p);
                }
            }
            Files.delete(dotoutput);
        }
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        System.err.println("jdeps " + args.stream().collect(Collectors.joining(" ")));
        int rc = com.sun.tools.jdeps.Main.run(args.toArray(new String[0]), pw);
        pw.close();
        String out = sw.toString();
        if (!out.isEmpty())
            System.err.println(out);
        if (rc != 0)
            throw new Error("jdeps failed: rc=" + rc);

        
        if (Files.notExists(dotfile)) {
            throw new RuntimeException(dotfile + " doesn't exist");
        }
        return parse(dotfile);
    }
    private static Pattern pattern = Pattern.compile("(.*) -> +([^ ]*) (.*)");
    private Map<String,String> parse(Path outfile) throws IOException {
        Map<String,String> result = new LinkedHashMap<>();
        for (String line : Files.readAllLines(outfile)) {
            line = line.replace('"', ' ').replace(';', ' ');
            Matcher pm = pattern.matcher(line);
            if (pm.find()) {
                String origin = pm.group(1).trim();
                String target = pm.group(2).trim();
                String module = pm.group(3).replace('(', ' ').replace(')', ' ').trim();
                result.put(target, module);
            }
        }
        return result;
    }

    void checkResult(String label, String[] expect, Collection<String> found) {
        List<String> list = Arrays.asList(expect);
        if (!isEqual(list, found))
            error("Unexpected " + label + " found: '" + found + "', expected: '" + list + "'");
    }

    void checkResult(String label, String[] expect, String[] profiles, Map<String,String> result) {
        if (expect.length != profiles.length)
            error("Invalid expected names and profiles");

        
        checkResult(label, expect, result.keySet());
        
        checkResult(label, profiles, result.values());
        for (int i=0; i < expect.length; i++) {
            String profile = result.get(expect[i]);
            if (!profile.equals(profiles[i]))
                error("Unexpected profile: '" + profile + "', expected: '" + profiles[i] + "'");
        }
    }

    boolean isEqual(List<String> expected, Collection<String> found) {
        if (expected.size() != found.size())
            return false;

        List<String> list = new ArrayList<>(found);
        list.removeAll(expected);
        return list.isEmpty();
    }

    void error(String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    int errors;
}
