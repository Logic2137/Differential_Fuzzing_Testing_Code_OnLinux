



import java.io.*;
import java.util.*;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.tools.*;

import com.sun.tools.javac.api.JavacTaskImpl;


public class T6358786 {
    public static void main(String... args) throws IOException {
        JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = tool.getStandardFileManager(null, null, null)) {
            String srcdir = System.getProperty("test.src");
            File file = new File(srcdir, args[0]);
            List<String> options = Arrays.asList(
                "--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED"
            );
            JavacTaskImpl task = (JavacTaskImpl)tool.getTask(null, fm, null, options, null, fm.getJavaFileObjectsFromFiles(Arrays.asList(file)));
            Elements elements = task.getElements();
            for (Element clazz : task.enter(task.parse())) {
                String doc = elements.getDocComment(clazz);
                if (doc == null)
                    throw new AssertionError(clazz.getSimpleName() + ": no doc comment");
                System.out.format("%s: %s%n", clazz.getSimpleName(), doc);
            }
        }
    }
}
