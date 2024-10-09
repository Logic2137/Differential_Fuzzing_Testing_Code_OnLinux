import com.sun.tools.javac.Main;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

@SupportedAnnotationTypes("*")
public class T8076104 extends AbstractProcessor {

    public static void main(String[] args) throws Exception {
        new T8076104().run();
    }

    void run() throws Exception {
        File testJar = createJar();
        doTest(testJar);
    }

    File createJar() throws Exception {
        File testJar = new File(System.getProperty("test.classes"), "T8076104-test.jar");
        testJar.delete();
        try (OutputStream fileOut = new FileOutputStream(testJar);
            JarOutputStream jarOut = new JarOutputStream(new BufferedOutputStream(fileOut))) {
            jarOut.putNextEntry(new JarEntry("d1/A.java"));
            jarOut.write("1".getBytes());
            jarOut.putNextEntry(new JarEntry("d2/A.java"));
            jarOut.write("2".getBytes());
        }
        return testJar;
    }

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    void doTest(File testJar, String... additionalArgs) {
        List<String> options = new ArrayList<>();
        options.add("-proc:only");
        options.add("-processor");
        options.add("T8076104");
        options.add("-classpath");
        options.add(System.getProperty("test.classes") + File.pathSeparator + testJar.getAbsolutePath());
        options.addAll(Arrays.asList(additionalArgs));
        options.add(System.getProperty("test.src") + File.separator + "T8076104.java");
        int res = Main.compile(options.toArray(new String[0]));
        if (res != 0) {
            throw new AssertionError("Unexpected error code: " + res);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        assertFileContent("d1/A.java", "1");
        assertFileContent("d2/A.java", "2");
        return false;
    }

    void assertFileContent(String relPath, String expectedContent) {
        try {
            FileObject fo = processingEnv.getFiler().getResource(StandardLocation.CLASS_PATH, "", relPath);
            String actualContent = fo.getCharContent(false).toString();
            if (!expectedContent.equals(actualContent)) {
                throw new AssertionError("Actual content not matching the expected content: " + actualContent);
            }
        } catch (IOException ex) {
            throw new AssertionError("Unexpected exception: ", ex);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
