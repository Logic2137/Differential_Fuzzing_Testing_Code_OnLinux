import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Context.Factory;

public class AvoidInfiniteReattribution {

    public static void main(String... args) throws Exception {
        new AvoidInfiniteReattribution().run();
    }

    void run() throws IOException {
        JavacTool tool = JavacTool.create();
        JavaSource source = new JavaSource("class Test {" + "    I i = STOP -> {};" + "    interface I {" + "        public void test(int i) {}" + "    }" + "}");
        Context context = new Context();
        CrashingAttr.preRegister(context);
        List<JavaSource> inputs = Arrays.asList(source);
        JavacTaskImpl task = (JavacTaskImpl) tool.getTask(null, null, null, null, null, inputs, context);
        try {
            task.analyze(null);
            throw new AssertionError("Expected exception not seen.");
        } catch (StopException ex) {
        }
    }

    static class CrashingAttr extends Attr {

        static void preRegister(Context context) {
            context.put(attrKey, (Factory<Attr>) c -> new CrashingAttr(c));
        }

        CrashingAttr(Context context) {
            super(context);
        }

        @Override
        public void visitVarDef(JCVariableDecl tree) {
            if (tree.name.contentEquals("STOP"))
                throw new StopException();
            super.visitVarDef(tree);
        }
    }

    static class StopException extends NullPointerException {
    }

    class JavaSource extends SimpleJavaFileObject {

        String source;

        JavaSource(String source) {
            super(URI.create("myfo:/Test.java"), JavaFileObject.Kind.SOURCE);
            this.source = source;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return source;
        }
    }
}
