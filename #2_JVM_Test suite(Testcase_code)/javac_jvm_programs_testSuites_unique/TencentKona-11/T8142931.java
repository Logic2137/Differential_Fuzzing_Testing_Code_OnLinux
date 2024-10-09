



import java.io.*;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.DeclaredType;
import javax.tools.*;
import com.sun.source.util.*;
import com.sun.tools.javac.api.*;

@SupportedAnnotationTypes("*")
public class T8142931 extends AbstractProcessor {

    public java.util.List<? extends javax.xml.namespace.QName> f0;

    public static void main(String... args) throws IOException {
        String testSrc = System.getProperty("test.src", ".");
        String testClasses = System.getProperty("test.classes");
        JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        MyDiagListener dl = new MyDiagListener();
        try (StandardJavaFileManager fm = tool.getStandardFileManager(dl, null, null)) {
            Iterable<? extends JavaFileObject> files =
                fm.getJavaFileObjectsFromFiles(Arrays.asList(new File(testSrc, T8142931.class.getName()+".java")));
            Iterable<String> opts = Arrays.asList(
                "--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
                "-XDaccessInternalAPI",
                "-proc:only",
                "-processor", "T8142931",
                "-processorpath", testClasses);
            StringWriter out = new StringWriter();
            JavacTask task = (JavacTask)tool.getTask(out, fm, dl, opts, null, files);
            task.call();
            String s = out.toString();
            System.err.print(s);
            System.err.println(dl.count + " diagnostics; " + s.length() + " characters");
            if (dl.count != 0 || s.length() != 0)
                throw new AssertionError("unexpected output from compiler");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getRootElements();
        for (Element element : set) {
            Collection<VariableElement> fields = ElementFilter.fieldsIn(((TypeElement) element).getEnclosedElements());
            for (VariableElement field : fields) {
                TypeMirror listType = field.asType();
                List<? extends TypeMirror> typeArgs = ((DeclaredType) listType).getTypeArguments();
                TypeMirror arg = typeArgs.get(0);
                String erasure = processingEnv.getTypeUtils().erasure(arg).toString();
                if (!erasure.equals("javax.xml.namespace.QName"))
                    throw new AssertionError("Wrong Erasure: " + erasure);
            }
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    static class MyDiagListener implements DiagnosticListener {
        public void report(Diagnostic d) {
            System.err.println(d);
            count++;
        }

        public int count;
    }
}

