import com.sun.source.util.JavacTask;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class T8000316 {

    final static int N_METHODS = 1024;

    final static int N_CLASSES = 8;

    static class TestClass extends SimpleJavaFileObject {

        String methTemplate = "    public static Runnable get#N() {\n" + "        return new Runnable() {\n" + "            @Override\n" + "            public void run() {\n" + "                return;\n" + "            }\n" + "        };\n" + "    }\n";

        String classTemplate = "\n\nclass Chain#N {\n\n#M }";

        String source;

        public TestClass() {
            super(URI.create("myfo:/Test.java"), JavaFileObject.Kind.SOURCE);
            StringBuilder buf = new StringBuilder();
            StringBuilder fileBuf = new StringBuilder();
            for (int i = 0; i < N_CLASSES; i++) {
                for (int j = 0; j < N_METHODS; j++) {
                    buf.append(methTemplate.replace("#N", String.valueOf(j)));
                    buf.append("\n");
                }
                fileBuf.append(classTemplate.replace("#M", buf.toString()).replace("#N", String.valueOf(i)));
                buf = new StringBuilder();
                source = fileBuf.toString();
            }
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return source;
        }
    }

    public static void main(String... args) throws Exception {
        ArrayList<JavaFileObject> sources = new ArrayList<>();
        sources.add(new TestClass());
        new T8000316().run(sources);
    }

    void run(List<JavaFileObject> sources) throws Exception {
        javax.tools.DiagnosticListener<JavaFileObject> dc = (diagnostic) -> {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                throw new AssertionError("unexpected diagnostic: " + diagnostic.getMessage(Locale.getDefault()));
            }
        };
        JavaCompiler comp = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = comp.getStandardFileManager(null, null, null)) {
            JavacTask ct = (JavacTask) comp.getTask(null, fm, dc, null, null, sources);
            ct.analyze();
        }
    }
}
