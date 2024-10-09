import java.io.*;

public class SubClass {

    static class PW extends PrintWriter {

        PW(Writer out) {
            super(out);
        }

        public void println() {
            try {
                out.write("[EOL]");
            } catch (IOException x) {
                setError();
            }
            super.println();
        }
    }

    public static void main(String[] args) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PW(sw);
        pw.println("Hello");
        pw.close();
        String s = sw.toString();
        System.err.print(s);
        if (!s.equals("Hello[EOL]" + System.getProperty("line.separator")))
            throw new Exception("Subclass broken");
    }
}
