



import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.regex.*;


public class ClassVersionChecker {
    private static enum Version {
        SEVEN("7", 51),
        EIGHT("8", 52),
        NINE("9", 53),
        TEN("10", 54),
        ELEVEN("11", 55),
        TWELVE("12", 56),
        THIRTEEN("13", 57),
        FOURTEEN("14", 58),
        FIFTEEN("15", 59);

        private Version(String release, int classFileVer) {
            this.release = release;
            this.classFileVer = classFileVer;
        }
        private final String release;
        private final int classFileVer;

        String release() {return release;}
        int classFileVer() {return classFileVer;}
    }

    static final Version CURRENT;
    static {
        Version[] versions = Version.values();
        int index = versions.length;
        CURRENT = versions[index - 1];
    }

    int errors;

    File javaFile = null;

    public static void main(String[] args) throws Throwable {
        new ClassVersionChecker().run();
    }

    void run() throws Exception {
        writeTestFile();
        
        test("", "", CURRENT.classFileVer());
        for (Version source : Version.values()) {
            test(source.release(), "", CURRENT.classFileVer()); 
            for (Version target : Version.values()) {
                if (target.compareTo(source) < 0)
                    continue; 
                else {
                    logMsg("Running for src = " + source + " target = "+ target +
                           " expected = " + target.classFileVer());
                    test(source.release(), target.release(), target.classFileVer());
                }
            }
        }

        if (errors > 0)
            throw new Exception(errors + " errors found");
    }

    void test(String i, String j, int expected) {
        File classFile = compileTestFile(i, j, javaFile);
        short majorVer = getMajorVersion(classFile);
        checkVersion(majorVer, expected);
    }

    void writeTestFile() throws IOException {
        javaFile = new File("Test.java");
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(javaFile)));) {
            out.println("class Test { ");
            out.println("  public void foo() { }");
            out.println("}");
        } catch (IOException ioe) {
            error("IOException while creating Test.java" + ioe);
        }
    }

    File compileTestFile(String i, String j, File f) {
        int rc = -1;
        
        if (i.isEmpty() && j.isEmpty() ) {
            rc = compile("-g", f.getPath());
        } else if( j.isEmpty()) {  
            rc = compile("-source", i, "-g", f.getPath());
        } else {
            rc = compile("-source", i, "-target", j, "-g", f.getPath());
        }
        if (rc != 0)
            throw new Error("compilation failed. rc=" + rc);
        String path = f.getPath();
        return new File(path.substring(0, path.length() - 5) + ".class");
    }

    int compile(String... args) {
        return com.sun.tools.javac.Main.compile(args);
    }

    void logMsg (String str) {
        System.out.println(str);
    }

    short getMajorVersion(File f) {
        List<String> args = new ArrayList<String>();
        short majorVer = 0;
        try(DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));) {
            in.readInt();
            in.readShort();
            majorVer = in.readShort();
            System.out.println("major version:" +  majorVer);
        } catch (IOException e) {
            error("IOException while reading Test.class" + e);
        }
        return majorVer;
    }

    void checkVersion(short majorVer, int expected) {
        if (majorVer != expected ) {
            error("versions did not match, Expected: " + expected + "Got: " + majorVer);
        }
    }

    void error(String msg) {
       System.out.println("error: " + msg);
       errors++;
    }
}
