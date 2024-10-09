



import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.util.Assert;

public class PrettyPrintingForLoopsTest {

    public static void main(String... args) throws IOException {
        new PrettyPrintingForLoopsTest().testForLoop();
    }

    public void testForLoop() throws IOException {
        List files = Arrays.asList(new JavaSource(""));
        JavacTool tool = JavacTool.create();
        try (StandardJavaFileManager fm = tool.getStandardFileManager(null, null, null)) {
            JavacTask task = tool.getTask(null, fm, null, null, null, files);
            Iterable trees = task.parse();
            CompilationUnitTree thisTree = (CompilationUnitTree)trees.iterator().next();
            String thisSrc = prettyPrint((JCTree) thisTree);
            Assert.check(thisSrc.equals(JavaSource.source));
        }
    }

    private static String prettyPrint(JCTree tree) throws IOException {
        StringWriter sw = new StringWriter();
        new Pretty(sw, true).printExpr(tree);
        return sw.toString().replaceAll(System.getProperty("line.separator"), "\n");
    }

    static class JavaSource extends SimpleJavaFileObject {
        static String source = "\n" +
                "class Test {\n" +
                "    \n" +
                "    void m() {\n" +
                "        for (int i; true; i++) {\n" +
                "            i = 0;\n" +
                "        }\n" +
                "        for (int i = 0, j, k = 23, l; i < 42; i++) {\n" +
                "        }\n" +
                "    }\n" +
                "}";

        public JavaSource(String stmt) {
            super(URI.create("myfo:/Test.java"), JavaFileObject.Kind.SOURCE);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return source;
        }
    }
}
