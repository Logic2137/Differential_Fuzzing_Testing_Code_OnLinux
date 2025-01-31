import java.io.*;
import java.net.*;
import java.util.*;
import javax.tools.*;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.*;

public class BrokenAnonymous {

    static class JavaSource extends SimpleJavaFileObject {

        final static String source = "class C {\n" + "    Object o1 = new Undef1() {" + "        int i = 0;\n" + "        int m(int j) { return i + j; }\n" + "    }\n" + "    Object o2 = new Undef2<>() {" + "        int i = 0;\n" + "        int m(int j) { return i + j; }\n" + "    }\n" + "}";

        JavaSource() {
            super(URI.create("myfo:/C.java"), JavaFileObject.Kind.SOURCE);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return source;
        }
    }

    public static void main(String... args) throws IOException {
        new BrokenAnonymous().run();
    }

    void run() throws IOException {
        File destDir = new File("classes");
        destDir.mkdir();
        final JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        JavaSource source = new JavaSource();
        JavacTask ct = (JavacTask) tool.getTask(null, null, null, Arrays.asList("-d", destDir.getPath()), null, Arrays.asList(source));
        CompilationUnitTree cut = ct.parse().iterator().next();
        Trees trees = Trees.instance(ct);
        ct.analyze();
        new TreePathScanner<Void, Void>() {

            @Override
            public Void visitVariable(VariableTree node, Void p) {
                verifyElementType();
                return super.visitVariable(node, p);
            }

            @Override
            public Void visitMethod(MethodTree node, Void p) {
                verifyElementType();
                return super.visitMethod(node, p);
            }

            @Override
            public Void visitIdentifier(IdentifierTree node, Void p) {
                verifyElementType();
                return super.visitIdentifier(node, p);
            }

            private void verifyElementType() {
                TreePath tp = getCurrentPath();
                assertNotNull(trees.getElement(tp));
                assertNotNull(trees.getTypeMirror(tp));
            }
        }.scan(cut, null);
    }

    private void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Unexpected null value.");
        }
    }
}
