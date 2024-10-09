import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class StringFoldingPosTest {

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    public static void main(String[] args) throws IOException {
        StringFoldingPosTest t = new StringFoldingPosTest();
        JavaFileObject source = t.makeSource("C", "class C {String X=\"F\" + \"O\" + \"L\" + \"D\" + \"E\" + \"D\";}");
        t.run(source, "FOLDED", 18, 51);
        source = t.makeSource("C", "class C {String X=(\"F\" + \"O\") + (\"L\" + \"D\") + (\"E\" + \"D\");}");
        t.run(source, "FO", 19, 28);
        t.run(source, "LD", 33, 42);
        t.run(source, "ED", 47, 56);
    }

    private static JavaFileObject makeSource(String name, String code) {
        return new SimpleJavaFileObject(URI.create("file:/" + name.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE) {

            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return code;
            }
        };
    }

    private void run(JavaFileObject source, String expectedLiteral, long expectedStartPos, long expectedEndPos) throws IOException {
        JavacTask ct = (JavacTask) compiler.getTask(null, null, null, null, null, Arrays.asList(source));
        SourcePositions positions = Trees.instance(ct).getSourcePositions();
        Iterable<? extends CompilationUnitTree> trees = ct.parse();
        boolean[] found = { false };
        for (CompilationUnitTree tree : trees) {
            new TreeScanner<Void, Void>() {

                @Override
                public Void visitLiteral(LiteralTree literal, Void v) {
                    if (literal.getKind() == Tree.Kind.STRING_LITERAL && literal.getValue().equals(expectedLiteral)) {
                        long startPos = positions.getStartPosition(tree, literal);
                        long endPos = positions.getEndPosition(tree, literal);
                        if (startPos != expectedStartPos) {
                            throw new AssertionError("Expected start position " + expectedStartPos + ", but was " + startPos);
                        }
                        if (endPos != expectedEndPos) {
                            throw new AssertionError("Expected end position " + expectedEndPos + ", but was " + endPos);
                        }
                        found[0] = true;
                    }
                    return null;
                }
            }.scan(trees, null);
        }
        if (found[0]) {
            return;
        }
        throw new AssertionError("Expected string literal " + expectedLiteral + " not found");
    }
}
