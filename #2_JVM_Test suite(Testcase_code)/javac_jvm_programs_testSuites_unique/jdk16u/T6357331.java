


import java.io.*;
import java.util.*;
import javax.tools.*;
import com.sun.source.util.*;

public class T6357331
{
    public static void main(String... args) throws IOException {
        JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        PrintWriter out = new PrintWriter(new StringWriter());
        List<String> opts = Arrays.asList("-d", ".");
        try (StandardJavaFileManager fm = tool.getStandardFileManager(null, null, null)) {
            File thisFile = new File(System.getProperty("test.src"), "T6357331.java");
            Iterable<? extends JavaFileObject> files = fm.getJavaFileObjects(thisFile);
            final JavacTask task = (JavacTask) (tool.getTask(out, fm, null, opts, null, files));

            
            
            task.setTaskListener(new TaskListener() {
                    public void started(TaskEvent e) {
                        task.getElements();
                        task.getTypes();
                    }
                    public void finished(TaskEvent e) { }
                });

            task.call();

            
            try {
                task.getElements();
                throw new AssertionError("IllegalStateException not thrown");
            }
            catch (IllegalStateException e) {
                
            }

            try {
                task.getTypes();
                throw new AssertionError("IllegalStateException not thrown");
            }
            catch (IllegalStateException e) {
                
            }
        }
    }
}
