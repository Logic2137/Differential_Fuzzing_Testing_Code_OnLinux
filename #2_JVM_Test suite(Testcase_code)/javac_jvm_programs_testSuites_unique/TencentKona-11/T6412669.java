import java.io.*;
import java.net.*;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.tools.*;
import com.sun.source.util.*;
import com.sun.tools.javac.api.*;

@SupportedAnnotationTypes("*")
public class T6412669 extends AbstractProcessor {

    public static void main(String... args) throws Exception {
        File testSrc = new File(System.getProperty("test.src", "."));
        File testClasses = new File(System.getProperty("test.classes", "."));
        JavacTool tool = JavacTool.create();
        try (StandardJavaFileManager fm = tool.getStandardFileManager(null, null, null)) {
            fm.setLocation(StandardLocation.CLASS_PATH, Arrays.asList(testClasses));
            Iterable<? extends JavaFileObject> files = fm.getJavaFileObjectsFromFiles(Arrays.asList(new File(testSrc, T6412669.class.getName() + ".java")));
            String[] opts = { "-proc:only", "-processor", T6412669.class.getName(), "--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED" };
            StringWriter sw = new StringWriter();
            JavacTask task = tool.getTask(sw, fm, null, Arrays.asList(opts), null, files);
            boolean ok = task.call();
            String out = sw.toString();
            if (!out.isEmpty())
                System.err.println(out);
            if (!ok)
                throw new AssertionError("compilation of test program failed");
            if (!out.contains("processing element"))
                throw new AssertionError("expected text not found in compilation output");
        }
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Trees trees = Trees.instance(processingEnv);
        SourcePositions sp = trees.getSourcePositions();
        Messager m = processingEnv.getMessager();
        m.printMessage(Diagnostic.Kind.NOTE, "processing annotations");
        int count = 0;
        for (TypeElement anno : annotations) {
            count++;
            m.printMessage(Diagnostic.Kind.NOTE, "  processing annotation " + anno);
            for (Element e : roundEnv.getElementsAnnotatedWith(anno)) {
                m.printMessage(Diagnostic.Kind.NOTE, "    processing element " + e);
                TreePath p = trees.getPath(e);
                long start = sp.getStartPosition(p.getCompilationUnit(), p.getLeaf());
                long end = sp.getEndPosition(p.getCompilationUnit(), p.getLeaf());
                Diagnostic.Kind k = (start > 0 && end > 0 && start < end ? Diagnostic.Kind.NOTE : Diagnostic.Kind.ERROR);
                m.printMessage(k, "test [" + start + "," + end + "]", e);
            }
        }
        if (count == 0)
            m.printMessage(Diagnostic.Kind.NOTE, "no annotations found");
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
