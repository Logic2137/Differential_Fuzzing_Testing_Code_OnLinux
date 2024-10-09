import java.io.File;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import com.sun.tools.javap.JavapFileManager;
import com.sun.tools.javap.JavapTask;

public class JavapTaskCtorFailWithNPE {

    private static final String expOutput = "Compiled from \"JavapTaskCtorFailWithNPE.java\"\n" + "public class JavapTaskCtorFailWithNPE {\n" + "  public JavapTaskCtorFailWithNPE();\n" + "  public static void main(java.lang.String[]);\n" + "}\n";

    public static void main(String[] args) {
        new JavapTaskCtorFailWithNPE().run();
    }

    private void run() {
        File classToCheck = new File(System.getProperty("test.classes"), getClass().getSimpleName() + ".class");
        DiagnosticCollector<JavaFileObject> dc = new DiagnosticCollector<JavaFileObject>();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        JavaFileManager fm = JavapFileManager.create(dc, pw);
        JavapTask t = new JavapTask(pw, fm, dc, null, Arrays.asList(classToCheck.getPath()));
        if (t.run() != 0)
            throw new Error("javap failed unexpectedly");
        List<Diagnostic<? extends JavaFileObject>> diags = dc.getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> d : diags) {
            if (d.getKind() == Diagnostic.Kind.ERROR)
                throw new AssertionError(d.getMessage(Locale.ENGLISH));
        }
        String lineSep = System.getProperty("line.separator");
        String out = sw.toString().replace(lineSep, "\n");
        if (!out.equals(expOutput)) {
            throw new AssertionError("The output is not equal to the one expected");
        }
    }
}
