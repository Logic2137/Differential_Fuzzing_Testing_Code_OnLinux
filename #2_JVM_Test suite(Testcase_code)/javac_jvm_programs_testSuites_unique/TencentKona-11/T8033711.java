



import java.io.*;

public class T8033711 {
    public static void main(String[] args) throws Exception {
        new T8033711().run();
    }

    public void run() throws Exception {
        String out = javap("-classpath");
        if (out.contains("IllegalArgumentException"))
            throw new Exception("exception found in javap output");
        if (!out.contains("Error: invalid use of option"))
            throw new Exception("expected error message not found in javap output");
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
