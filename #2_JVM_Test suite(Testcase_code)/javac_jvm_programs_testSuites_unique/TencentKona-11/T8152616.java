import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import javax.tools.StandardJavaFileManager;
import javax.tools.DiagnosticListener;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.Pretty;
import java.io.IOException;

public class T8152616 {

    public String PrettyPrint(JCTree tree) {
        StringWriter s = new StringWriter();
        try {
            new Pretty(s, false).printExpr(tree);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return s.toString();
    }

    public static void main(String[] args) throws Exception {
        T8152616 obj = new T8152616();
        JavacTool javac = JavacTool.create();
        StandardJavaFileManager jfm = javac.getStandardFileManager(null, null, null);
        File file = File.createTempFile("test", ".java");
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write("enum Foo {AA(10), BB, CC { void m() {} }; void m() {};}".getBytes());
        JavacTask task = javac.getTask(null, jfm, null, null, null, jfm.getJavaFileObjects(file.getAbsolutePath()));
        Iterable<? extends CompilationUnitTree> trees = task.parse();
        CompilationUnitTree thisTree = trees.iterator().next();
        file.delete();
        outputStream = new FileOutputStream(file);
        outputStream.write((obj.PrettyPrint((JCTree) thisTree)).getBytes());
        task = javac.getTask(null, jfm, null, null, null, jfm.getJavaFileObjects(file.getAbsolutePath()));
        if (task.parse().toString().contains("ERROR")) {
            throw new AssertionError("parsing temp file failed with errors");
        } else {
            System.out.println("parsing successfull");
        }
        file.delete();
    }
}
