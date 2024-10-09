



import java.io.*;
import java.util.*;
import javax.tools.*;
import com.sun.tools.javap.*;

public class T7186925
{
    public static void main(String... args) {
        new T7186925().run();
    }

    void run() {
        verify("java.lang.Object");
        if (errors > 0)
            throw new Error(errors + " found.");
    }

    void verify(String className) {
        try {
            JavaFileManager fileManager = JavapFileManager.create(null, null);
            JavaFileObject fo = fileManager.getJavaFileForInput(StandardLocation.PLATFORM_CLASS_PATH, className, JavaFileObject.Kind.CLASS);
            if (fo == null) {
                error("Can't find " + className);
            } else {
                JavapTask t = new JavapTask(null, fileManager, null);
                t.handleOptions(new String[] { "-sysinfo", className });
                JavapTask.ClassFileInfo cfInfo = t.read(fo);
                expectEqual(cfInfo.cf.byteLength(), cfInfo.size);
            }
        } catch (NullPointerException ee) {
            ee.printStackTrace();
            error("Exception: " + ee);
        } catch (Exception ee) {
            System.err.println("Caught exception: " + ee);
        }
    }

    void expectEqual(int found, int expected) {
        if (found != expected)
            error("bad value found: " + found + " expected: " + expected);
    }

    void error(String msg) {
        System.err.println(msg);
        errors++;
    }

    int errors;
}
