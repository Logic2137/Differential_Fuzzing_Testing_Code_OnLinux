import com.sun.source.util.JavacTask;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import static javax.tools.Diagnostic.Kind.*;
import static javax.tools.JavaFileObject.Kind.*;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("*")
public class MessagerDiags extends AbstractProcessor {

    static final String CNAME = "Test";

    static final String TEST_JAVA = CNAME + ".java";

    static final String TEST_JAVA_URI_NAME = "myfo:/" + TEST_JAVA;

    static final String WRN_NO_SOURCE = "warning without source";

    static final String WRN_WITH_SOURCE = "warning with source";

    static final String NONE = "<none>";

    static final String[] EXPECTED = { NONE + ":-1--1:" + WRN_NO_SOURCE, TEST_JAVA + ":0-13:" + WRN_WITH_SOURCE, NONE + ":-1--1:" + WRN_NO_SOURCE };

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for (Element e : roundEnv.getRootElements()) {
            messager.printMessage(WARNING, WRN_NO_SOURCE);
            messager.printMessage(WARNING, WRN_WITH_SOURCE, e);
            messager.printMessage(WARNING, WRN_NO_SOURCE);
        }
        return false;
    }

    public static void main(String... args) throws IOException {
        final JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        assert tool != null;
        DiagnosticCollector<JavaFileObject> dc = new DiagnosticCollector<>();
        List<String> options = Arrays.asList("-source", "1.8", "-Xlint:-options", "-classpath", System.getProperty("java.class.path"), "-processor", MessagerDiags.class.getName());
        JavacTask ct = (JavacTask) tool.getTask(null, null, dc, options, null, Arrays.asList(new MyFileObject("class " + CNAME + " {}")));
        ct.analyze();
        List<String> obtainedErrors = new ArrayList<>();
        for (Diagnostic<? extends JavaFileObject> d : dc.getDiagnostics()) {
            String dSource;
            if (d.getSource() != null) {
                dSource = d.getSource().toUri().getPath();
                dSource = dSource.substring(dSource.lastIndexOf('/') + 1);
            } else {
                dSource = NONE;
            }
            obtainedErrors.add(dSource + ":" + d.getStartPosition() + "-" + d.getEndPosition() + ":" + d.getMessage(null));
        }
        List<String> expectedErrors = Arrays.asList(EXPECTED);
        if (!expectedErrors.equals(obtainedErrors)) {
            System.err.println("Expected: " + expectedErrors);
            System.err.println("Obtained: " + obtainedErrors);
            throw new AssertionError("Messages don't match");
        }
    }

    static class MyFileObject extends SimpleJavaFileObject {

        private String text;

        public MyFileObject(String text) {
            super(URI.create(TEST_JAVA_URI_NAME), SOURCE);
            this.text = text;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return text;
        }
    }
}
