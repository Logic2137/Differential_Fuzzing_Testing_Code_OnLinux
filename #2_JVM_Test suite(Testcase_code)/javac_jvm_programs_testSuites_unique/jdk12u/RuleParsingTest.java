



import java.io.StringWriter;
import java.net.URI;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.tools.*;

import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

public class RuleParsingTest {

    public static void main(String[] args) throws Exception {
        new RuleParsingTest().testParseComplexExpressions();
    }

    void testParseComplexExpressions() throws Exception {
        String[] expressions = {
            "(a)",
            "a",
            "a + a",
            "~a + a",
            "a = a",
            "a += a",
            "a + (a)",
            "a + (a) b",
            "true ? a : b",
            "m(() -> {})",
            "m(() -> 1)",
            "m(a -> 1)",
            "m((t a) -> 1)",
        };
        StringBuilder code = new StringBuilder();
        List<Entry<Long, Long>> spans = new ArrayList<>();
        code.append("class Test {\n" +
                    "    void t(int i) {\n");
        for (boolean switchExpr : new boolean[] {false, true}) {
            if (switchExpr) {
                code.append("         int j = switch(i) {\n");
            } else {
                code.append("         switch(i) {\n");
            }
            for (String expr : expressions) {
                code.append("case ");
                int start = code.length();
                code.append(expr);
                spans.add(new SimpleEntry<>((long) start, (long) code.length()));
                code.append(" -> {}");
            }
            code.append("         };\n");
        }
        code.append("    }\n" +
                    "}\n");
        final JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        assert tool != null;
        DiagnosticListener<JavaFileObject> noErrors = d -> { throw new AssertionError(d.getMessage(null)); };

        StringWriter out = new StringWriter();
        JavacTask ct = (JavacTask) tool.getTask(out, null, noErrors,
            List.of("--enable-preview", "-source", "12"), null,
            Arrays.asList(new MyFileObject(code.toString())));
        CompilationUnitTree cut = ct.parse().iterator().next();
        Trees trees = Trees.instance(ct);
        new TreePathScanner<Void, Void>() {
            @Override
            public Void visitCase(CaseTree node, Void p) {
                long start = trees.getSourcePositions().getStartPosition(cut, node.getExpression());
                long end = trees.getSourcePositions().getEndPosition(cut, node.getExpression());
                if (!spans.remove(new SimpleEntry<>(start, end))) {
                    throw new AssertionError("Did not find an expression span in expected spans: " +
                                             start + "-" + end +
                                             " '" + node.getExpression().toString() + "'");
                }
                return super.visitCase(node, p);
            }
        }.scan(cut, null);

        if (!spans.isEmpty()) {
            throw new AssertionError("Remaning spans: " + spans);
        }
    }

    static class MyFileObject extends SimpleJavaFileObject {
        private String text;

        public MyFileObject(String text) {
            super(URI.create("myfo:/Test.java"), JavaFileObject.Kind.SOURCE);
            this.text = text;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return text;
        }
    }
}
