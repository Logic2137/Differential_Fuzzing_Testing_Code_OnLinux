



import java.io.*;
import java.net.*;
import java.util.*;

import javax.tools.*;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.tree.JCTree;

public class PostAttrConstructor {

    static class JavaSource extends SimpleJavaFileObject {

        final static String source =
                        "class C {\n" +
                        "    public C() {}\n" +
                        "}";

        JavaSource() {
            super(URI.create("myfo:/C.java"), JavaFileObject.Kind.SOURCE);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return source;
        }
    }

    public static void main(String... args) throws IOException {
        new PostAttrConstructor().run();
    }

    void run() throws IOException {
        File destDir = new File("classes"); destDir.mkdir();
        final JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        JavaSource source = new JavaSource();
        JavacTaskImpl ct = (JavacTaskImpl)tool.getTask(null, null, null,
                Arrays.asList("-d", destDir.getPath()),
                null,
                Arrays.asList(source));
        CompilationUnitTree cut = ct.parse().iterator().next();
        Attr attr = Attr.instance(ct.getContext());
        attr.postAttr((JCTree) cut);
    }

}
