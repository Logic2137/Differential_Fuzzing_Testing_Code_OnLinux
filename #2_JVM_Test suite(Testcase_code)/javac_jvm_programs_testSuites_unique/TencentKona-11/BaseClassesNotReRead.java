



import java.io.*;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import com.sun.source.util.JavacTask;


@SupportedAnnotationTypes("*")
public class BaseClassesNotReRead extends AbstractProcessor {
    public static void main(String... args) throws IOException {
        new BaseClassesNotReRead().run();
    }

    void run() throws IOException {
        File sources = new File(System.getProperty("test.src"));
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> files =
                    fm.getJavaFileObjects(new File(sources, "BaseClassesNotReReadSource.java"));
            DiagnosticListener<JavaFileObject> noErrors = new DiagnosticListener<JavaFileObject>() {
                @Override
                public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                    throw new IllegalStateException(diagnostic.toString());
                }
            };
            JavaFileManager manager = new OnlyOneReadFileManager(fm);
            Iterable<String> options = Arrays.asList("-processor", "BaseClassesNotReRead");
            JavacTask task = (JavacTask) compiler.getTask(null, manager, noErrors, options, null, files);
            task.analyze();
        }
    }

    int round = 1;
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        if (round++ == 1) {
            for (int c = 1; c <= 6; c++) {
                generateSource("GenClass" + c,
                               "public class GenClass" + c + " { public void test() { } }");
            }
            for (int c = 1; c <= 3; c++) {
                generateSource("GenIntf" + c,
                               "public interface GenIntf" + c + " { public void test(); }");
            }
            generateSource("GenAnnotation",
                           "public @interface GenAnnotation { }");
            generateSource("GenException",
                           "public class GenException extends Exception { }");
        }

        return false;
    }

    private void generateSource(String name, String code) {
        Filer filer = processingEnv.getFiler();
        try (Writer out = filer.createSourceFile(name).openWriter()) {
            out.write(code);
            out.close();
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    final class OnlyOneReadFileManager extends ForwardingJavaFileManager<JavaFileManager> {

        public OnlyOneReadFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind)
                throws IOException {
            JavaFileObject fo = super.getJavaFileForInput(location, className, kind);
            return (fo == null) ? null : new OnlyOneReadJavaFileObject(fo);
        }

        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds,
                boolean recurse) throws IOException {
            List<JavaFileObject> result = new ArrayList<>();
            for (JavaFileObject jfo : super.list(location, packageName, kinds, recurse)) {
                result.add(new OnlyOneReadJavaFileObject(jfo));
            }
            return result;
        }

        @Override
        public String inferBinaryName(Location location, JavaFileObject file) {
            return super.inferBinaryName(location,
                                         ((OnlyOneReadJavaFileObject) file).getFileObject());
        }

    }

    final class OnlyOneReadJavaFileObject extends ForwardingJavaFileObject<JavaFileObject> {

        public OnlyOneReadJavaFileObject(JavaFileObject fileObject) {
            super(fileObject);
        }

        boolean used;

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            if (used) throw new IllegalStateException("Already read.");
            used = true;
            return super.getCharContent(ignoreEncodingErrors);
        }

        @Override
        public InputStream openInputStream() throws IOException {
            if (used) throw new IllegalStateException("Already read.");
            used = true;
            return super.openInputStream();
        }

        @Override
        public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
            if (used) throw new IllegalStateException("Already read.");
            used = true;
            return super.openReader(ignoreEncodingErrors);
        }

        public JavaFileObject getFileObject() {
            return fileObject;
        }
    }
}
