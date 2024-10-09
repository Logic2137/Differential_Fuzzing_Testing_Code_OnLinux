






import java.io.*;
import java.util.*;
import javax.tools.*;
import com.sun.source.util.*;
import com.sun.tools.javac.api.*;


public class T6430209 {
    public static void main(String... args) throws IOException {
        
        File dir1 = new File("dir1");
        dir1.mkdir();
        BufferedWriter fout = new BufferedWriter(new FileWriter(new File(dir1, "test0.java")));
        fout.write("public class test0 { }");
        fout.close();

        
        
        String testSrc = System.getProperty("test.src", ".");
        String testClassPath = System.getProperty("test.class.path");
        JavacTool tool = JavacTool.create();
        try (StandardJavaFileManager fm = tool.getStandardFileManager(null, null, null)) {
            fm.setLocation(StandardLocation.CLASS_PATH, Arrays.asList(new File(".")));
            Iterable<? extends JavaFileObject> files = fm.getJavaFileObjectsFromFiles(Arrays.asList(
                new File(testSrc, "test0.java"), new File(testSrc, "test1.java")));
            Iterable<String> opts = Arrays.asList("-XDrawDiagnostics",
                                                  "-proc:only",
                                                  "-processor", "b6341534",
                                                  "-processorpath", testClassPath);
            StringWriter out = new StringWriter();
            JavacTask task = tool.getTask(out, fm, null, opts, null, files);
            task.call();
            String s = out.toString();
            System.err.print(s);
            s = s.replace(System.getProperty("line.separator"), "\n");
            String expected = "test0.java:1:8: compiler.err.duplicate.class: test0\n" +
                              "1 error\n";
            if (!expected.equals(s))
                throw new AssertionError("unexpected text in output");
        }
    }

}
