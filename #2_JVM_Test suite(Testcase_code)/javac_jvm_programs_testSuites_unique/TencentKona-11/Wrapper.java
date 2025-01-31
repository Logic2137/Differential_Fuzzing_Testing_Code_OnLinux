

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class Wrapper {
    public static void main(String... args) throws Exception {
        if (!isSupplementaryCharactersSupported()) {
            System.out.println("Unicode supplementary characters in filenames not supported: pass by default");
            return;
        }

        String testClassName = args[0];
        String[] testArgs = Arrays.copyOfRange(args, 1, args.length);

        File srcDir = new File(System.getProperty("test.src"));
        File clsDir = new File(System.getProperty("test.classes"));

        File src = new File(srcDir, testClassName + ".java");
        File cls = new File(clsDir, testClassName + ".class");

        if (cls.lastModified() < src.lastModified()) {
            System.err.println("Recompiling test class...");
            String[] javacArgs = { "-d", clsDir.getPath(), src.getPath() };
            int rc = com.sun.tools.javac.Main.compile(javacArgs);
            if (rc != 0)
                throw new Exception("compilation failed");
        }

        Class<?> mainClass = Class.forName(testClassName);
        Method main = mainClass.getMethod("main", String[].class);
        main.invoke(null, new Object[] { testArgs });
    }

    private static boolean isSupplementaryCharactersSupported() {
        try {
            String s = "p--\ud801\udc00--";
            System.err.println("Trying: Paths.get(" + s + ")");
            Path p1 = Paths.get(s);
            System.err.println("Found: " + p1);
            System.err.println("Trying: p1.resolve(" + s + ")");
            Path p2 = p1.resolve(s);
            System.err.println("Found: " + p2);
            return p1.toString().equals(s) && p2.toString().equals(s + java.io.File.separator + s);
        } catch (InvalidPathException e) {
            System.err.println(e);
            return false;
        }
    }
}
