



import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.lang.model.SourceVersion;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.Reporter;
import jdk.javadoc.doclet.DocletEnvironment;

public class TestUserDoclet implements Doclet {
    public static void main(String... args) throws Exception {
        new TestUserDoclet().run();
    }

    static final String docletWarning = "warning from test doclet";

    
    public boolean run(DocletEnvironment root) {
        reporter.print(javax.tools.Diagnostic.Kind.WARNING, docletWarning);
        return true;
    }

    
    void run() throws Exception {
        File javaHome = new File(System.getProperty("java.home"));
        File javadoc = new File(new File(javaHome, "bin"), "javadoc");
        File testSrc = new File(System.getProperty("test.src"));
        File testClasses = new File(System.getProperty("test.classes"));

        
        
        String thisClassName = TestUserDoclet.class.getName();
        List<String> cmdArgs = new ArrayList<>();
        cmdArgs.add(javadoc.getPath());
        cmdArgs.addAll(Arrays.asList(
                "-doclet", thisClassName,
                "-docletpath", testClasses.getPath(),
                new File(testSrc, thisClassName + ".java").getPath()
        ));
        Process p = new ProcessBuilder()
            .command(cmdArgs)
            .redirectErrorStream(true)
            .start();

        int actualDocletWarnCount = 0;
        int reportedDocletWarnCount = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.err.println(line);
                if (line.contains(docletWarning))
                    actualDocletWarnCount++;
                if (line.matches("[0-9]+ warning(s)?"))
                    reportedDocletWarnCount =
                            Integer.valueOf(line.substring(0, line.indexOf(" ")));
            }
        } finally {
            in.close();
        }
        int rc = p.waitFor();
        if (rc != 0)
            System.err.println("javadoc failed, rc:" + rc);

        int expectedDocletWarnCount = 1;
        checkEqual("actual", actualDocletWarnCount, "expected", expectedDocletWarnCount);
        checkEqual("actual", actualDocletWarnCount, "reported", reportedDocletWarnCount);
    }

    void checkEqual(String l1, int i1, String l2, int i2) throws Exception {
        if (i1 != i2)
            throw new Exception(l1 + " warn count, " + i1 + ", does not match "
                        + l2 + " warn count, " + i2);
    }

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public Set<Option> getSupportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    Reporter reporter;
    Locale locale;
    public void init(Locale locale, Reporter reporter) {
        this.locale = locale;
        this.reporter = reporter;
    }
}
