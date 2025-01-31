



import com.sun.source.tree.BlockTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;

import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.JavacTool;

public class TestToString {
    String[] statements = {
        "i = i + 1;",
        "i++;",
        "m();",
        ";",
        "if (i == 0) return;",
        "while (i > 0) i--;",
        "{ }",
        "{ i++; }",
        "class C { }"
    };

    public static void main(String... args) throws Exception {
        new TestToString().run();
    }

    void run() throws Exception {
        try {
            for (String s: statements) {
                test(s);
            }

            if (errors > 0)
                throw new Exception(errors + " errors found");
        } finally {
            fm.close();
        }
    }

    void test(String stmt) throws IOException {
        System.err.println("Test: " + stmt);
        List<String> options = Collections.<String>emptyList();
        List<? extends JavaFileObject> files = Arrays.asList(new JavaSource(stmt));
        JavacTask t = tool.getTask(null, fm, null, options, null, files);
        checkEqual(scan(t.parse()), stmt);
    }

    String scan(Iterable<? extends CompilationUnitTree> trees) {
        class Scanner extends TreeScanner<Void,StringBuilder> {
            String scan(Iterable<? extends Tree> trees) {
                StringBuilder sb = new StringBuilder();
                scan(trees, sb);
                return sb.toString();
            }
            @Override
            public Void scan(Tree tree, StringBuilder sb) {
                if (print && tree instanceof StatementTree) {
                    sb.append(PREFIX);
                    sb.append(tree);
                    sb.append(SUFFIX);
                    return null;
                } else {
                    return super.scan(tree, sb);
                }
            }
            @Override
            public Void visitBlock(BlockTree tree, StringBuilder sb) {
                print = true;
                try {
                    return super.visitBlock(tree, sb);
                } finally {
                    print = false;
                }
            }
            boolean print = false;
        }
        return new Scanner().scan(trees);
    }

    void checkEqual(String found, String expect) {
        boolean match = (found == null) ? (expect == null) :
            expect.equals(found
                .replaceAll("^\\Q" + PREFIX + "\\E\\s*", "")
                .replaceAll("\\s*\\Q" + SUFFIX + "\\E$", "")
                .replaceAll("\\s+", " "));

        if (!match)
            error("Mismatch: expected: " + expect + " found: " + found);
    }



    void error(String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    static final String PREFIX = "#<";
    static final String SUFFIX = "#>";

    JavacTool tool = JavacTool.create();
    StandardJavaFileManager fm = tool.getStandardFileManager(null, null, null);
    int errors = 0;

    static class JavaSource extends SimpleJavaFileObject {

        String source =
                "class Test {\n" +
                "    int i;\n" +
                "    void m() {\n" +
                "        #S\n" +
                "    }\n" +
                "}";

        public JavaSource(String stmt) {
            super(URI.create("myfo:/Test.java"), JavaFileObject.Kind.SOURCE);
            source = source.replace("#S", stmt);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return source;
        }
    }
}
