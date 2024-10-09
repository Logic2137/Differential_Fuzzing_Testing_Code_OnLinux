



import java.net.URI;
import java.util.Arrays;

import javax.lang.model.type.TypeKind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.*;

public class VarInstanceMemberTest {

    static class JavaSource extends SimpleJavaFileObject {

        final static String sourceStub = "class Test {var v1;}";

        public JavaSource() {
            super(URI.create("myfo:/Test.java"), JavaFileObject.Kind.SOURCE);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return sourceStub;
        }

    }

    public static void main(String[] args) throws Exception {
        check();

    }

    static void check() throws Exception {
        final JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> dc = new DiagnosticCollector<JavaFileObject>();
        JavacTask ct = (JavacTask) tool.getTask(null, null, dc,
                null, null, Arrays.asList(new JavaSource()));
        CompilationUnitTree tree = ct.parse().iterator().next();

        ct.analyze();
        new TreePathScanner<Object, Object>() {
            @Override
            public Object visitVariable(VariableTree node, Object p) {
                TypeKind kind = Trees.instance(ct).getElement(getCurrentPath()).asType().getKind();
                if (kind != TypeKind.ERROR) {
                    throw new AssertionError("Kind = " + Trees.instance(ct).getElement(getCurrentPath()).asType().getKind());
                }
                return super.visitVariable(node, p);
            }
        }.scan(tree, null);
    }

}

