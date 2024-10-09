import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;
import com.sun.tools.classfile.ClassFile;
import com.sun.tools.classfile.ClassWriter;
import com.sun.tools.javac.Main;

public class BadClass {

    static String makeClass(String dir, String filename, String body) throws IOException {
        File file = new File(dir, filename);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(body);
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        String[] args = { "-cp", dir, "-d", dir, "-XDrawDiagnostics", file.getPath() };
        Main.compile(args, pw);
        pw.close();
        return sw.toString();
    }

    public static void main(String... args) throws Exception {
        new File("d1").mkdir();
        new File("d2").mkdir();
        makeClass("d1", "Empty.java", "abstract class Empty implements Readable {}");
        ClassFile cf = ClassFile.read(new File("d1", "Empty.class"));
        cf.interfaces[0] = cf.constant_pool.size() + 10;
        ClassWriter cw = new ClassWriter();
        cw.write(cf, new File("d2", "Empty.class"));
        String result = makeClass("d2", "EmptyUse.java", "class EmptyUse { Empty e; }");
        if (!result.contains("compiler.misc.bad.class.file")) {
            System.out.println(result);
            throw new Exception("test failed");
        }
    }
}
