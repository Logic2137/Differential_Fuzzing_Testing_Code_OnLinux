



import com.sun.tools.javac.api.JavacTool;
import java.lang.annotation.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import com.sun.source.tree.*;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import javax.tools.StandardJavaFileManager;


public class ArrayPositionConsistency {
    public static void main(String[] args) throws Exception {
        PrintWriter out = new PrintWriter(System.out, true);
        JavacTool tool = JavacTool.create();
        try (StandardJavaFileManager fm = tool.getStandardFileManager(null, null, null)) {
            File testSrc = new File(System.getProperty("test.src"));
            Iterable<? extends JavaFileObject> f =
                fm.getJavaFileObjectsFromFiles(Arrays.asList(new File(testSrc, "ArrayPositionConsistency.java")));
            JavacTask task = tool.getTask(out, fm, null, null, null, f);
            Iterable<? extends CompilationUnitTree> trees = task.parse();
            out.flush();

            Scanner s = new Scanner();
            for (CompilationUnitTree t: trees)
                s.scan(t, null);
        }
    }

    private static class Scanner extends TreeScanner<Void,Void> {
        int foundAnnotations = 0;
        public Void visitCompilationUnit(CompilationUnitTree node, Void ignore) {
            super.visitCompilationUnit(node, ignore);
            if (foundAnnotations != expectedAnnotations) {
                throw new AssertionError("Expected " + expectedAnnotations +
                        " annotations but found: " + foundAnnotations);
            }
            return null;
        }

        private void testAnnotations(List<? extends AnnotationTree> annos, int found) {
            String annotation = annos.get(0).toString();
            foundAnnotations++;

            int expected = -1;
            if (annotation.equals("@A"))
                expected = 0;
            else if (annotation.equals("@B"))
                expected = 1;
            else if (annotation.equals("@C"))
                expected = 2;
            else
                throw new AssertionError("found an unexpected annotation: " + annotation);
            if (found != expected) {
                throw new AssertionError("Unexpected found length" +
                    ", found " + found + " but expected " + expected);
            }
        }

        public Void visitAnnotatedType(AnnotatedTypeTree node, Void ignore) {
            testAnnotations(node.getAnnotations(), arrayLength(node));
            return super.visitAnnotatedType(node, ignore);
        }

        private int arrayLength(Tree tree) {
            switch (tree.getKind()) {
            case ARRAY_TYPE:
                return 1 + arrayLength(((ArrayTypeTree)tree).getType());
            case ANNOTATED_TYPE:
                return arrayLength(((AnnotatedTypeTree)tree).getUnderlyingType());
            default:
                return 0;
            }
        }
    }

    static int expectedAnnotations = 23;

    
    @A String @C [] @B [] field1;
    @A String @C []    [] field2;
    @A String    [] @B [] field3;
       String    [] @B [] field4;

    @A List<String> @C [] @B [] genfield1;
    @A List<String> @C []    [] genfield2;
    @A List<String>    [] @B [] genfield3;
       List<String>    [] @B [] genfield4;

    List<@A String @C [] @B []> typearg1;
    List<@A String @C []    []> typearg2;
    List<@A String    [] @B []> typearg3;
    List<   String    [] @B []> typearg4;

    void vararg1(@A String @C [] @B ... arg) {}
    void vararg2(@A String @C []    ... arg) {}
    void vararg3(@A String    [] @B ... arg) {}
    void vararg4(   String    [] @B ... arg) {}

    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @interface A {}
    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @interface B {}
    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @interface C {}

}
