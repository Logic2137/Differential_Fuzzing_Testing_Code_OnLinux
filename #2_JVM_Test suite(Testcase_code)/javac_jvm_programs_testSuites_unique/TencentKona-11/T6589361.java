



import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;
import java.io.File;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;
import java.util.Set;
import java.util.HashSet;

public class T6589361 {
    public static void main(String [] args) throws Exception {
        JavacFileManager fm = null;
        try {
            fm = new JavacFileManager(new Context(), false, null);
            Set<JavaFileObject.Kind> set = new HashSet<JavaFileObject.Kind>();
            set.add(JavaFileObject.Kind.CLASS);
            Iterable<JavaFileObject> files = fm.list(StandardLocation.PLATFORM_CLASS_PATH, "java.lang", set, false);
            for (JavaFileObject file : files) {
                
                
                if (file.getName().replace(File.separatorChar, '/').contains("java/lang/Object.class")) {
                    String str = fm.inferBinaryName(StandardLocation.PLATFORM_CLASS_PATH, file);
                    if (!str.equals("java.lang.Object")) {
                        System.err.println("file object: " + file);
                        System.err.println("      class: " + file.getClass());
                        System.err.println("       name: " + file.getName());
                        System.err.println("binary name: " + str);
                        throw new AssertionError("Error in JavacFileManager.inferBinaryName method!");
                    }
                    else {
                        return;
                    }
                }
            }
        }
        finally {
            if (fm != null) {
                fm.close();
            }
        }
        throw new AssertionError("Could not find java/lang/Object.class while compiling");
    }

}
