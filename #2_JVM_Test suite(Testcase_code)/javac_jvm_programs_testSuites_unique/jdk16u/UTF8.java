



import java.io.*;

public class UTF8 {

    static String test
    = "This is a simple\ntest of the UTF8\r\nbyte-to-char and char-to-byte\nconverters.";

    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(bo, "UTF8");
        out.write(test);
        out.close();

        Reader in
            = new InputStreamReader(new ByteArrayInputStream(bo.toByteArray()),
                                    "UTF8");

        StringBuffer sb = new StringBuffer();
        char buf[] = new char[1000];
        int n;
        while ((n = in.read(buf, 0, buf.length)) >= 0) {
            sb.append(buf, 0, n);
            System.err.println(n);
        }
        if (! sb.toString().equals(test)) {
            System.err.println("In: [" + test + "]");
            System.err.println("Out: [" + sb.toString() + "]");
            throw new RuntimeException("Output does not match input");
        }

    }

}
