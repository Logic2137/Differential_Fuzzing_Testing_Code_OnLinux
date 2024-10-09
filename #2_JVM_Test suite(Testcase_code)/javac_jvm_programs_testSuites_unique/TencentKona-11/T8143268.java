



import java.io.*;

public class T8143268 {
    public static void main(String... args) throws Exception{
        new T8143268().run();
    }

    void run() throws IOException {
        File src = new File("src");
        src.mkdirs();
        try (FileWriter out = new FileWriter(new File(src, "Test.java"))) {
            out.write("public class Test { native void m(); }");
        }

        javac("-d", "classes", "-h", "hdr", "src/Test.java");

        check("classes/Test.class");
        check("hdr/Test.h");
    }

    void javac(String... args) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out))) {
            int rc = com.sun.tools.javac.Main.compile(args, pw);
            if (rc != 0) {
                throw new Error("compilation failed: " + rc);
            }
        }
    }

    void check(String path) {
        if (!new File(path).exists()) {
            throw new Error("file not found: " + path);
        }
    }
}

