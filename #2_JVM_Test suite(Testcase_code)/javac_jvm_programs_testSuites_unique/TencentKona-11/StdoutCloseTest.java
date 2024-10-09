




import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.main.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StdoutCloseTest {

    public static void main(String[] args) throws Exception {
        new StdoutCloseTest().test();
    }

    static final String program = "public class Test {\n" +
                                  "  public boolean test() {\n" +
                                  "    int i;\n" +
                                  "    if (i > 0) return true;\n" +
                                  "    return false;\n" +
                                  "  }\n" +
                                  "}\n";

    public void test() throws Exception {
        final String sourceName = "Test.java";
        final String outName = "Test.out";
        File source = new File(sourceName);
        PrintWriter pw = new PrintWriter(source);
        pw.write(program);
        pw.flush();
        pw.close();

        PrintWriter log = compileClass(sourceName, outName);

        File outFile = new File(outName);
        if (!outFile.exists()) {
            throw new Exception("Output file was not created!");
        }
        if (!log.checkError()) { 
            log.close(); 
            throw new Exception("Output file was still open!");
        }
    }

    public PrintWriter compileClass(String src, String out) {
        List<String> options = new ArrayList<>();
        options.add("-Xstdout");
        options.add(out);
        options.add(src);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Main compiler = new Main("javac", pw);
        compiler.compile(options.toArray(new String[options.size()]));
        pw.flush();
        if (sw.getBuffer().length() > 0) {
            System.err.println(sw.toString());
        }
        return compiler.log.getWriter(Log.WriterKind.NOTICE);
    }
}
