import java.io.*;

public class T8035104 {

    public static void main(String[] args) throws Exception {
        new T8035104().run();
    }

    public void run() throws Exception {
        String[] lines = javap("-v", T8035104.class.getName()).split("[\r\n]+");
        int minor = -1;
        int SourceFile = -1;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.matches(" *minor version: [0-9.]+"))
                minor = i;
            if (line.matches(" *SourceFile: .+"))
                SourceFile = i;
        }
        if (minor == -1)
            throw new Exception("minor version not found");
        if (SourceFile == -1)
            throw new Exception("SourceFile not found");
        if (SourceFile < minor)
            throw new Exception("unexpected order of output");
        System.out.println("output OK");
    }

    String javap(String... args) {
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        int rc = com.sun.tools.javap.Main.run(args, out);
        out.close();
        System.out.println(sw.toString());
        System.out.println("javap exited, rc=" + rc);
        return sw.toString();
    }
}
