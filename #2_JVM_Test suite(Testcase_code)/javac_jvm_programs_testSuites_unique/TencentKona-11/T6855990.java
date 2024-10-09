



import java.io.*;

public class T6855990 {
    public static void main(String[] args) throws Exception {
        new T6855990().run();
    }

    public void run() throws Exception {
        @Simple String[] args = { "-c", "-XDdetails:typeAnnotations", "T6855990" };
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        int rc = com.sun.tools.javap.Main.run(args, pw);
        pw.close();
        String out = sw.toString();
        System.out.println(out);
        if (out.indexOf("@Simple: LOCAL_VARIABLE") == -1)
            throw new Exception("expected output not found");
    }
}

@interface Simple { }

